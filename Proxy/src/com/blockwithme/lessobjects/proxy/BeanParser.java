/*******************************************************************************
 * Copyright 2013 Sebastien Diot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.blockwithme.lessobjects.proxy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.annotations.StructField;
import com.blockwithme.lessobjects.beans.ChildProperties;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.prim.Converter;
import com.blockwithme.prim.ConverterRegistry;
import com.blockwithme.prim.LongConverter;
import com.google.common.base.Preconditions;

/**
 * The BeanParser class Extracts the FieldInfo using javassist.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class BeanParser {

    /** The Member Properties Bean class to pass around a bunch of properties,
     * and compare groups of properties */
    private static class MemberProperties {

        /** The child properties. */
        @Nonnull
        private final ChildProperties childProperties;

        /** The field properties. */
        @Nonnull
        private final FieldProperties fieldProperties;

        /** The object to byte converter. */
        @SuppressWarnings("rawtypes")
        @Nonnull
        private final Class<? extends Converter<?>> objectToByteConverter;

        /** The array size. */
        final int arraySize;

        /** The object to long converter. */
        @SuppressWarnings("rawtypes")
        final Class<? extends LongConverter> objectToLongConverter;

        /** Instantiates a new member properties. */
        @SuppressWarnings("rawtypes")
        MemberProperties(final FieldProperties theFieldProperties,
                final ChildProperties theChildProperties,
                final Class<? extends Converter<?>> theObjectConverter,
                final Class<? extends LongConverter> theSimpleObjectConverter,
                final int theArraySize) {
            fieldProperties = theFieldProperties;
            childProperties = theChildProperties;
            objectToByteConverter = theObjectConverter;
            objectToLongConverter = theSimpleObjectConverter;
            arraySize = theArraySize;
        }

        /**
         * @return the childProperties
         */
        @SuppressWarnings("null")
        ChildProperties childProperties() {
            return childProperties;
        }

        /**
         * @return the fieldProperties
         */
        @SuppressWarnings("null")
        FieldProperties fieldProperties() {
            return fieldProperties;
        }

        /**
         * @return the objectToByteConverter
         */
        @SuppressWarnings({ "null", "rawtypes" })
        Class<? extends Converter<?>> objectToByteConverter() {
            return objectToByteConverter;
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(@Nullable final Object theObj) {
            if (this == theObj) {
                return true;
            }
            if (theObj == null) {
                return false;
            }
            if (getClass() != theObj.getClass()) {
                return false;
            }
            final MemberProperties other = (MemberProperties) theObj;
            if (childProperties == null) {
                if (other.childProperties != null) {
                    return false;
                }
            } else if (!childProperties.equals(other.childProperties)) {
                return false;
            }
            if (fieldProperties == null) {
                if (other.fieldProperties != null) {
                    return false;
                }
            } else if (!fieldProperties.equals(other.fieldProperties)) {
                return false;
            }
            if (objectToByteConverter == null) {
                if (other.objectToByteConverter != null) {
                    return false;
                }
            } else if (objectToByteConverter != other.objectToByteConverter) { // $codepro.audit.disable
                                                                               // useEquals
                return false;
            }
            if (objectToLongConverter == null) {
                if (other.objectToLongConverter != null) {
                    return false;
                }
            } else if (objectToLongConverter != other.objectToLongConverter) { // $codepro.audit.disable
                                                                               // useEquals
                return false;
            }
            return true;
        }

    }

    /** Size of String "get" */
    private static final int GET_SIZE = "get".length();

    /** Size of String "is" */
    private static final int IS_SIZE = "is".length();

    /** The a list of java key words, to ensure that the 'field' names in the
     * generated classes do not conflict with java key-words. */
    private static final Set<String> JAVA_KEY_WORDS = new HashSet<>(
            Arrays.asList(new String[] { "abstract", "continue", "for", "new",
                    "switch", "assert", "default", "goto", "package",
                    "synchronized", "boolean", "do", "if", "private", "this",
                    "break", "double", "implements", "protected", "throw",
                    "byte", "else", "import", "public", "throws", "case",
                    "enum", "instanceof", "return", "transient", "catch",
                    "extends", "int", "short", "try", "char", "final",
                    "interface", "static", "void", "class", "finally", "long",
                    "strictfp", "volatile", "const", "float", "native",
                    "super", "while" }));

    /** Check for conflicting field names; throw if found */
    private static void checkConflict(final Map<String, FieldInfo> theMap,
            final String theFieldName) {
        checkArgument(!theMap.containsKey(Util.toLower(theFieldName)),
                "Conflicting field names " + "present for field - "
                        + theFieldName + ", in the interface hierarchy!");
    }

    /** Compares setter properties with getter properties and returns the one
     * with non-default values. Throws exception in case if any conflicts are
     * found. */
    private static MemberProperties commonProps(
            final MemberProperties theSetterProps,
            final MemberProperties theGetterProps, final String theFieldName,
            final String theInterfaceName) {

        if (theSetterProps.equals(theGetterProps)) {
            return theGetterProps;
        } else if (theSetterProps.fieldProperties().hasDefaultValues()) {
            return theGetterProps;
        } else if (theSetterProps.fieldProperties().hasDefaultValues()) {
            return theSetterProps;
        } else {
            throw new IllegalArgumentException("Conflicting field properties "
                    + "present for field - " + theFieldName + " interface - " //$NON-NLS-2$
                    + theInterfaceName);
        }
    }

    /** Parses the method signature and annotations to create MemberProperties
     * object. */
    @SuppressWarnings("null")
    private static MemberProperties fieldProps(final String theName,
            final CtMethod theMethod) throws ClassNotFoundException {
        final StructField field = (StructField) theMethod
                .getAnnotation(StructField.class);
        // The default value of bits will be 0 which would mean 'full bits'
        final int bits;
        final FieldProperties props;
        final ChildProperties cProps;
        final Class<? extends Converter<?>> objectToByteConverter;
        final Class<? extends LongConverter<?>> objectToLongConverter;
        boolean isOptional = false;
        int arraySize = 0;
        if (field == null) {
            bits = 0;
            props = new FieldProperties(theName, 0);
            objectToByteConverter = null;
            objectToLongConverter = null;
        } else {
            bits = field.bits();
            isOptional = field.optional();
            final boolean isGlobal = field.global();
            final boolean isVirtual = field.converter() != StructField.DEFAULT_CONVERTER.class;
            final boolean isObject = field.converter() != StructField.DEFAULT_CONVERTER.class;
            final boolean isSimpleObject = field.converter() != StructField.DEFAULT_CONVERTER.class;
            arraySize = field.arraySize();
            props = (FieldProperties) new FieldProperties(theName, 0)
                    .setVirtual(isVirtual).setGlobal(isGlobal);

            objectToByteConverter = isObject ? field.converter() : null;
            objectToLongConverter = (Class<? extends LongConverter<?>>) (isSimpleObject ? field
                    .converter() : null);

        }
        cProps = new ChildProperties(theName, bits).setOptional(isOptional);
        return new MemberProperties(props, cProps, objectToByteConverter,
                objectToLongConverter, arraySize);

    }

    private static boolean invalidGetterType(final FieldInfo theAttribute) {
        return !Util.isField(theAttribute)
                && !theAttribute.type().isInterface()
                && !theAttribute.isChildArray();
    }

    private static boolean invalidlistMethodArgs(final CtMethod theMethod,
            final FieldInfo theAttrib) throws NotFoundException {
        return theAttrib.isList()
                && (invalidSetterParams(theMethod) || theMethod
                        .getParameterTypes()[0] != CtClass.intType);
    }

    private static boolean invalidMethodArgs(final CtMethod theMethod,
            final FieldInfo theAttrib) throws NotFoundException {
        return theMethod.getParameterTypes() != null
                && theMethod.getParameterTypes().length > 0
                && !theAttrib.isList();
    }

    @SuppressWarnings("null")
    private static boolean invalidSetterMethodName(final String theIName,
            final String thePropertyName, final CtClass theFirstParam) {
        return thePropertyName == null || thePropertyName.isEmpty()
                || theFirstParam == null || theIName == null;
    }

    private static boolean invalidSetterParams(final CtMethod theMethod)
            throws NotFoundException {
        return theMethod.getParameterTypes() == null
                || theMethod.getParameterTypes().length != 1;
    }

    /** Process the getXyz()/isXyz() method and extract Field info
     *
     * @throws ClassNotFoundException */
    @SuppressWarnings("null")
    private static void processGetter(final CtClass theInterface,
            final Map<String, FieldInfo> theMap, final CtMethod theMethod,
            final boolean theAllowConflictFlag,
            final ConverterRegistry theRegistry) throws NotFoundException,
            IllegalClassFormatException, ClassNotFoundException {
        final CtClass type = theMethod.getReturnType();
        final String mName = theMethod.getName();
        final String aName = mName.startsWith("get") ? mName
                .substring(GET_SIZE) : mName.substring(IS_SIZE);

        final String fName = Util.toLower(aName);
        Preconditions.checkArgument(!JAVA_KEY_WORDS.contains(fName), "Field :"
                + fName + " is a java keyword. Hence not allowed.");
        boolean hasSetter = true;
        final String setterName = "set" + aName;
        try {
            theInterface.getDeclaredMethod(setterName);
        } catch (final NotFoundException nfe) {
            hasSetter = false;
        }
        if (!theAllowConflictFlag) {
            checkConflict(theMap, aName);
        }

        final MemberProperties props = fieldProps(fName, theMethod);

        final FieldInfoBuilder builder = new FieldInfoBuilder();
        builder.setName(fName).setSimpleName(fName).setHasGetter(true);
        builder.setHasSetter(hasSetter).setType(type).setGetterMethod(mName);
        builder.setSetterMethod(setterName).setInterfaceName(
                theInterface.getName());
        builder.setFieldProps(props.fieldProperties()).setChildProps(
                props.childProperties());
        builder.setObjectToByteConverter(props.objectToByteConverter());
        builder.setObjectToLongConverter(props.objectToLongConverter);
        builder.setInherited(false).setArraySize(props.arraySize)
                .setRegistry(theRegistry);

        final FieldInfo attrib = new FieldInfo(builder);
        checkArgument(!invalidGetterType(attrib), "Field :" + aName
                + " in interface " + theInterface.getName()
                + " must be of Primitives or interface type.");

        checkArgument(!invalidMethodArgs(theMethod, attrib),
                "Getter method " + theMethod.getLongName() + " in interface "
                        + theInterface.getName()
                        + ", should not have any method arguments.");

        checkArgument(
                !invalidlistMethodArgs(theMethod, attrib),
                "Getter method "
                        + theMethod.getLongName()
                        + " in interface "
                        + theInterface.getName()
                        + ", should have an int type argument. "
                        + "Accessor methods for collection type fields must specify "
                        + "size of Collection to be returned. ");

        theMap.put(attrib.name(), attrib);
    }

    /** Process the setXyz() method and extract Field info */
    @SuppressWarnings("null")
    private static void processSetter(final CtClass theInterface,
            final Map<String, FieldInfo> theMap, final CtMethod theMethod)
            throws NotFoundException, IllegalClassFormatException,
            ClassNotFoundException {
        final String iName = theInterface.getName();
        checkArgument(!invalidSetterParams(theMethod), "Setter method "
                + theMethod.getLongName() + " in interface " + iName
                + ", should have only one method argument.");

        final String mName = theMethod.getName();
        final String substring = mName.substring(3);
        final CtClass firstParam = theMethod.getParameterTypes()[0];
        checkState(!invalidSetterMethodName(iName, substring, firstParam),
                "Invalid setter method : " + theMethod + " in interface: "
                        + iName);

        final String aName = Util.toLower(substring);
        final MemberProperties props = fieldProps(aName, theMethod);
        checkState(theMap.containsKey(aName), "Interface :" + iName
                + " Contains field : " + aName
                + " that with a setter method only.");
        // getter method is present and fieldInfo has already been
        // processed. we need to check here, if field Properties in setters
        // and getters do not conflict. Point to be noted here is that
        // getters are always processed before setters because methods array
        // is sorted on names
        FieldInfo fInfo = theMap.get(aName);
        if (fInfo != null) {
            checkArgument(Util.isField(fInfo), "Interface :" + iName
                    + " can have setter methods only for Primitives."
                    + " Only getter methods should exist for Array or Child"
                    + " interface types ");
        }
        final MemberProperties memProps = commonProps(
                props,
                new MemberProperties(fInfo.fieldProps(), fInfo.childProps(),
                        fInfo.objectToByteConverter(), fInfo
                                .objectToLongConverter(), fInfo.arraySize()),
                aName, iName);

        fInfo = fInfo.setFieldProps(memProps.fieldProperties()).setChildProps(
                memProps.childProperties());
    }

    /** Gets the field list.
     *
     * @param theInterface the interface
     * @return the field list */
    public static SortedMap<String, FieldInfo> getFieldInfo(
            final CtClass theInterface, final boolean theAllowConflictFlag,
            final SortedMap<String, FieldInfo> theFieldMap,
            final boolean theRecursiveFlag, final ConverterRegistry theRegistry)
            throws IllegalClassFormatException, ClassNotFoundException {
        try {
            final CtClass[] superInterfaces = theInterface.getInterfaces();
            if (theRecursiveFlag && superInterfaces != null) {
                for (final CtClass i : superInterfaces) {
                    if (i != null) {
                        theFieldMap.putAll(getFieldInfo(i,
                                theAllowConflictFlag, theFieldMap, true,
                                theRegistry));
                    }
                }
            }
            final CtMethod[] methods = theInterface.getDeclaredMethods();
            // Sort methods so that getters are processed before setters.
            Arrays.sort(methods, new Comparator<CtMethod>() {
                @SuppressWarnings("null")
                @Override
                public int compare(final CtMethod theFirstMethod,
                        final CtMethod theSecondMethod) {
                    return theFirstMethod.getLongName().compareTo(
                            theSecondMethod.getLongName());
                }
            });

            for (final CtMethod m : methods) {
                final String mName = m.getName();
                if (mName.startsWith("get") || mName.startsWith("is")) { //$NON-NLS-2$
                    processGetter(theInterface, theFieldMap, m,
                            theAllowConflictFlag, theRegistry);
                } else if (mName.startsWith("set")) {
                    processSetter(theInterface, theFieldMap, m);
                } else {
                    throw new IllegalClassFormatException(
                            "Cannot process method: "
                                    + m.getLongName()
                                    + " in interface "
                                    + theInterface.getName()
                                    + ", All methods in the interface must follow "
                                    + "getXXX/setXXX/isXXX method convention.");
                }
            }
            return theFieldMap;
        } catch (final NotFoundException e) {
            e.printStackTrace();
            throw new IllegalClassFormatException(e.getMessage());
        }
    }

}
