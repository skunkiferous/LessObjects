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
// $codepro.audit.disable handleNumericParsingErrors, com.instantiations.assist.eclipse.arrayIsStoredWithoutCopying
package com.blockwithme.lessobjects.proxy;

import static com.blockwithme.lessobjects.proxy.ProxyConstants.CHANGE_ADAPTER_CLASS_NAME;
import static com.blockwithme.lessobjects.proxy.ProxyConstants.CHANGE_LISTENER_CLASS_NAME;
import static com.blockwithme.lessobjects.proxy.ProxyConstants.COPY_BEAN_CLASS_NAME;
import static com.blockwithme.lessobjects.proxy.ProxyConstants.LISTENER_INTERFACE_CLASS_NAME;
import static com.blockwithme.lessobjects.proxy.ProxyConstants.PROXY_CLASS_NAME;
import static com.blockwithme.lessobjects.proxy.ProxyConstants.TRANS_CHANGE_LISTENER_CLASS_NAME;
import static com.blockwithme.lessobjects.proxy.Util.isField;
import static com.blockwithme.lessobjects.proxy.Util.toLower;
import static com.blockwithme.lessobjects.proxy.Util.toUpper;
import static com.blockwithme.lessobjects.util.StructConstants.BYTE_BITS;
import static com.blockwithme.lessobjects.util.StructConstants.CHAR_BITS;
import static com.blockwithme.lessobjects.util.StructConstants.FACTORY;
import static com.blockwithme.lessobjects.util.StructConstants.FIELD_CLASS_NAME;
import static com.blockwithme.lessobjects.util.StructConstants.INDEX_FIELD_NAME;
import static com.blockwithme.lessobjects.util.StructConstants.INT_BITS;
import static com.blockwithme.lessobjects.util.StructConstants.LONG_BITS;
import static com.blockwithme.lessobjects.util.StructConstants.SHORT_BITS;
import static com.blockwithme.lessobjects.util.StructConstants.STORAGE_CLASS_NAME;
import static com.blockwithme.lessobjects.util.StructConstants.STRING_CLASS_NAME;
import static com.blockwithme.lessobjects.util.StructConstants.STRUCT_CLASS_NAME;
import static com.blockwithme.lessobjects.util.Util.childrenArray;
import static com.blockwithme.lessobjects.util.Util.fieldArray;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.reflect.Array.newInstance;
import static java.lang.reflect.Array.set;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Array;
import com.blockwithme.lessobjects.ChildrenArray;
import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldType;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.annotations.Enrich;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.FloatOptionalField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
import com.blockwithme.lessobjects.fields.optional.LongOptionalField;
import com.blockwithme.lessobjects.fields.optional.ShortOptionalField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.TransactionManager;
import com.blockwithme.lessobjects.util.StructConstants;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.BooleanConverter;
import com.blockwithme.prim.ByteConverter;
import com.blockwithme.prim.CharConverter;
import com.blockwithme.prim.Converter;
import com.blockwithme.prim.ConverterRegistry;
import com.blockwithme.prim.DoubleConverter;
import com.blockwithme.prim.FloatConverter;
import com.blockwithme.prim.IntConverter;
import com.blockwithme.prim.LongConverter;
import com.blockwithme.prim.ShortConverter;

/**
 * Using Struct Proxies, a particular 'Struct' schema can be represented as a
 * group of java bean-like interfaces containing accessor and mutator methods.
 * Given a list of bean-style interfaces, optionally annotated with
 * {@link com.blockwithme.lessobjects.annotations.StructProxy}
 * {@link com.blockwithme.lessobjects.annotations.StructField}, dynamic proxies can be created that
 * allow the access to the selected struct in a storage as if it was a real
 * bean-style object.
 *
 * This class parses the bean-style interfaces and performs code generation to
 * create concrete classes that have reference to the corresponding compiled
 * struct, and the storage objects, so that it can use
 * the fields of the generated bean to access the storage wrapper. The proxy
 * implements interfaces, and when the getters/setters are called, it delegates
 * to the Storage class.
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class ProxyCursor {

    /** The Class used as a key to cache compiled classes and Struct. */
    protected static class CacheKey {
        /** The interface class. */
        Class<? extends Compiler> compilerClass;

        /** The sorted set of interfaces */
        SortedSet<Class<?>> interfaceSet;

        /** Instantiates a new cache key. */
        CacheKey(final Class<? extends Compiler> theCompilerClass,
                final Class<?>[] theInterfaces) {
            compilerClass = theCompilerClass;
            interfaceSet = new TreeSet<>(new Comparator<Class<?>>() {
                @SuppressWarnings("null")
                @Override
                public int compare(final Class<?> theClass1,
                        final Class<?> theClass2) {
                    return theClass1.getName().compareTo(theClass2.getName());
                }
            });
            for (final Class<?> classs : theInterfaces) {
                interfaceSet.add(classs);
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(@Nullable final Object theObject) { // $codepro.audit.disable
                                                                  // checkTypeInEquals
            if (this == theObject) {
                return true;
            }
            if (theObject == null || getClass() != theObject.getClass()) {
                return false;
            }
            final CacheKey other = (CacheKey) theObject;
            if (interfaceSet == null) {
                if (other.interfaceSet != null) {
                    return false;
                }
            } else if (!interfaceSet.equals(other.interfaceSet)) {
                return false;
            }
            if (!compilerClass.equals(other.compilerClass)) {
                return false;
            }
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + (interfaceSet == null ? 0 : interfaceSet.hashCode());
            result = prime * result
                    + (compilerClass == null ? 0 : compilerClass.hashCode());
            return result;
        }
    }

    /** The Class used as a value to cache compiled classes and Struct. */
    protected static class StructInfo {
        /** The implementation class. */
        @Nonnull
        private CtClass implClass;

        /** Is it an array ? */
        int arraySize;

        /** The field map at current level. */
        SortedMap<String, FieldInfo> attributeMap;

        /** The children. */
        List<StructInfo> children;

        /** The instance of compiled implementation class */
        Object instance;

        /** The interface class. */
        CtClass interfaceClass;

        /** The name. */
        String name;

        /** The struct. */
        Struct struct;

        /** The super classes. */
        List<StructInfo> superClasses;

        /** Create copy without copying instances. */
        StructInfo blankCopy() {
            final StructInfo copy = new StructInfo();
            copy.name = name;
            copy.interfaceClass = interfaceClass;
            copy.implClass = implClass;
            copy.attributeMap = attributeMap;
            copy.arraySize = arraySize;

            if (children != null) {
                copy.children = new ArrayList<>();
                for (final StructInfo c : children) {
                    copy.children.add(c.blankCopy());
                }
            }
            if (superClasses != null) {
                copy.superClasses = new ArrayList<>();
                for (final StructInfo s : superClasses) {
                    copy.superClasses.add(s.blankCopy());
                }
            }
            copy.struct = struct;
            return copy;
        }

        /** Impl class. */
        @SuppressWarnings("null")
        public CtClass implClass() {
            return implClass;
        }

        /** Impl class. */
        public void implClass(final CtClass theImplClass) {
            implClass = theImplClass;
        }
    }

    /** The save proxy classes system property. */
    private static String SAVE_PROXY_CLASSES = "saveProxyClasses";

    /** Class name of ArrayUtil class used in code generation */
    static String ARRAY_UTIL = Util.getClassName(ArrayUtil.class);

    /** The Constant PROXY_FIELD_NAME. */
    private static final String PROXY_FIELD_NAME = "_proxy";

    /** The Constant STORAGE_FIELD_NAME. */
    private static final String STORAGE_FIELD_NAME = "_storage";

    /** The Constant STRUCT_FIELD_NAME. */
    private static final String STRUCT_FIELD_NAME = "_struct";

    /** The Map used to cache compiled classes and Struct. */
    protected static final Map<CacheKey, StructInfo> CACHE = new HashMap<>();

    /** The Constant Class Pool. (package private constant) */
    protected static final ClassPool POOL = ClassPool.getDefault();

    /** The Constant STRUCT. */
    protected static final CtClass STRUCT;

    /** The enrich map contains a map of interfaces that are to be 'Enriched' */
    @Nonnull
    private final Map<CtClass, CtClass> enrichMap;

    /** The struct compiler */
    @Nonnull
    protected final Compiler compiler;

    /** The default type class. */
    @Nonnull
    protected final Class<?> defaultType;

    /** The index field. */
    @SuppressWarnings("rawtypes")
    @Nonnull
    protected final CharField indexField;

    /** Class to StructInfo map */
    @Nonnull
    protected final Map<Class<?>, StructInfo> indexMap;

    /** The sorted array of Interfaces. */
    protected final Class<?>[] interfaces;

    /** The registry. */
    @Nullable
    protected final ConverterRegistry registry;

    /** The actual storage instance. */
    @Nonnull
    protected final Storage storage;

    /** The root level struct info. */
    @Nonnull
    protected final StructInfo structInfo;

    static {
        try {
            STRUCT = POOL.get(Struct.class.getName());
        } catch (final NotFoundException e) {
            // this should never happen.
            throw new Error(e);
        }
    }

    /** Adds an array struct to the Children-List */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    private static void addArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo, final Field<?, ?> theField) {
        theChildren.add(new Array(theFieldInfo.name(), theField,
                theFieldInfo.arraySize).arrayStruct());
    }

    /** Adds an array field to the CtClass */
    private static void addArrayField(final CtClass theClass,
            final FieldInfo theFieldInfo) throws CannotCompileException {
        final String fieldDeclaration1 = "private final "
                + StructConstants.FIELD_CLASS_NAME + "[] "
                + theFieldInfo.name() + ";";
        theClass.addField(CtField.make(fieldDeclaration1, theClass));
    }

    /** Adds a boolean array struct to the Children-List */
    @SuppressWarnings("null")
    private static void addBooleanArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {
        addArray(theChildren, theFieldInfo,
                FACTORY.newBooleanField(theFieldInfo.name()));
    }

    /** Adds a boolean type field to the Fields-List */
    private static void addBooleanField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {
        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newBooleanOptional(theFieldInfo.name()));
        } else {
            theFields.add(FACTORY.newBooleanField(theFieldInfo.name()));
        }
    }

    /** Adds a byte array struct to the Children-List */
    @SuppressWarnings("null")
    private static void addByteArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? BYTE_BITS : bits;
        addArray(theChildren, theFieldInfo,
                FACTORY.newByteField(theFieldInfo.name(), bits));
    }

    /** Adds a Byte type field to the Fields-List */
    @SuppressWarnings("null")
    private static void addByteField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? BYTE_BITS : bits;
        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newByteOptional(theFieldInfo.name(), bits));
        } else {
            theFields.add(FACTORY.newByteField(theFieldInfo.name(), bits));
        }
    }

    /** Adds a char array struct to the Children-List */
    private static void addCharArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? CHAR_BITS : bits;
        final CharField<?, ?> fld = FACTORY.newCharField(theFieldInfo.name(),
                bits);
        addArray(theChildren, theFieldInfo, fld);
    }

    /** Adds a char type field to the Fields-List */
    @SuppressWarnings("null")
    private static void addCharField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? StructConstants.CHAR_BITS : bits;

        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newCharOptional(theFieldInfo.name(), bits));
        } else {
            theFields.add(FACTORY.newCharField(theFieldInfo.name(), bits));
        }
    }

    /** Adds a double array struct to the Children-List */
    @SuppressWarnings("null")
    private static void addDoubleArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {
        addArray(theChildren, theFieldInfo,
                FACTORY.newDoubleField(theFieldInfo.name()));
    }

    /** Adds a double type field to the Fields-List */
    @SuppressWarnings("null")
    private static void addDoubleField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {
        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newDoubleOptional(theFieldInfo.name()));
        } else {
            theFields.add(FACTORY.newDoubleField(theFieldInfo.name()));
        }
    }

    /** Adds the required fields to the class */
    private static void addField(final CtClass theClass,
            final String theFieldtype, final String theName)
            throws CannotCompileException {
        final String fieldDeclaration1 = "private final " + theFieldtype + " "
                + theName + ";";
        theClass.addField(CtField.make(fieldDeclaration1, theClass));
    }

    /** Adds a float array struct to the Children-List */
    @SuppressWarnings("null")
    private static void addFloatArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {
        addArray(theChildren, theFieldInfo,
                FACTORY.newFloatField(theFieldInfo.name()));
    }

    /** Adds a float type field to the Fields-List */
    @SuppressWarnings("null")
    private static void addFloatField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {
        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newFloatOptional(theFieldInfo.name()));
        } else {
            theFields.add(FACTORY.newFloatField(theFieldInfo.name()));
        }
    }

    /** Adds a getter method for a particular field.
     *
     * @throws NotFoundException */
    @SuppressWarnings("null")
    private static void addGetter(final CtClass theClass,
            final FieldInfo theFieldInfo) throws CannotCompileException,
            NotFoundException {
        /*
         * Generates the field Getter Method which looks something like :
           1. In case of normal fields
               public byte getXyx(){
                    return _storage.read(this.xyz);
               }
           2. In case of array fields
               public Type[] getXyzArray(){
                    return (Type[])ArrayUtil.readArray[TYPE](_storage, this.xyzArray);
               }
           3. In case of Iterable fields:
                public Iterator getList(int i)
                {
                    return IterableFieldsUtil.createIterator(_storage, list, "list", i);
                }
           4. In case child interface
                public ProxyChild1 getChild1()
                {
                    return child1;
                }
         */

        final String argTypeName = theFieldInfo.type.getName();
        String mbody = null;
        if (isField(theFieldInfo)) {
            final String typeCast = theFieldInfo.type.isPrimitive() ? "" : "("
                    + theFieldInfo.type.getName() + ")";
            if (!theFieldInfo.isPrimitiveArray) {
                mbody = "public " + argTypeName + " "
                        + theFieldInfo.getterMethod + "() { return " + typeCast
                        + STORAGE_FIELD_NAME + ".read(this."
                        + theFieldInfo.name() + "); }";
            } else {
                final String methodName;
                final CtClass arrayType = theFieldInfo.type.getComponentType();
                if (arrayType.getName().equals(STRING_CLASS_NAME)) {
                    methodName = "readArrayString";
                } else if (arrayType.isPrimitive()) {
                    methodName = "readArray" + toUpper(arrayType.getName());
                } else if (theFieldInfo.objectToByteConverter != null) {
                    methodName = "readArrayObject";
                } else {
                    methodName = "readArraySimpleObject";
                }
                mbody = "public " + argTypeName + " "
                        + theFieldInfo.getterMethod + "() { return " + typeCast
                        + ARRAY_UTIL + "." + methodName + '('
                        + STORAGE_FIELD_NAME + ", " + theFieldInfo.name()
                        + "); }";
            }
        } else {
            if (theFieldInfo.isList) {
                mbody = "public " + argTypeName + " "
                        + theFieldInfo.getterMethod + "(int theSize) { return "
                        + IterableFieldsUtil.class.getName()
                        + ".createIterator( " + STORAGE_FIELD_NAME + ", "
                        + theFieldInfo.name() + ",\"" + theFieldInfo.name()
                        + "\", theSize); }";

            } else {
                mbody = "public " + argTypeName + " "
                        + theFieldInfo.getterMethod + "() { return this."
                        + theFieldInfo.name() + "; }";
            }
        }
        final CtMethod mthd = CtNewMethod.make(mbody, theClass);
        theClass.addMethod(mthd);
    }

    /** Adds an int array struct to the Children-List */
    @SuppressWarnings("null")
    private static void addIntArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? INT_BITS : bits;

        addArray(theChildren, theFieldInfo,
                FACTORY.newIntField(theFieldInfo.name(), bits));
    }

    /** Adds a int type field to the Fields-List */
    private static void addIntField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? INT_BITS : bits;

        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newIntOptional(theFieldInfo.name(), bits));
        } else {
            theFields.add(FACTORY.newIntField(theFieldInfo.name(), bits));
        }
    }

    /** Adds a long array struct to the Children-List */
    @SuppressWarnings("null")
    private static void addLongArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? LONG_BITS : bits;
        addArray(theChildren, theFieldInfo,
                FACTORY.newLongField(theFieldInfo.name(), bits));

    }

    /** Adds a long type field to the Fields-List */
    @SuppressWarnings("null")
    private static void addLongField(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? LONG_BITS : bits;

        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newLongOptional(theFieldInfo.name(), bits));
        } else {
            theFields.add(FACTORY.newLongField(theFieldInfo.name(), bits));
        }
    }

    /** Adds the all the required in the class methods.
     *
     * @throws IllegalAccessException
     * @throws InstantiationException */
    private static void addMethods(
            final Map<String, FieldInfo> theAttributeMap, final CtClass theClass)
            throws CannotCompileException, NotFoundException,
            InstantiationException, IllegalAccessException {

        for (final Entry<String, FieldInfo> attrib : theAttributeMap.entrySet()) {
            final FieldInfo a = attrib.getValue();
            if (a.hasGetter) {
                addGetter(theClass, a);
            }
            if (a.hasSetter) {
                addSetter(theClass, a);
            }
        }
    }

    /** Adds the required fields to the class */
    @SuppressWarnings("null")
    private static void addNormalField(final StructInfo theStruct,
            final CtClass theClass, final FieldInfo theFieldInfo)
            throws CannotCompileException {

        final Field<?, ?> f = getFieldByName(theStruct, theFieldInfo);
        addField(theClass, f.getClass().getName(), theFieldInfo.name());
    }

    /** Adds a Object array struct to the Children-List. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    private static void addObjectArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) throws InstantiationException,
            IllegalAccessException, NotFoundException {

        Array childArray = null;
        final boolean isOptional = theFieldInfo.childProps().isOptional();

        if (theFieldInfo.objectConverter != null) {

            @SuppressWarnings("unchecked")
            final Converter converter = theFieldInfo.objectConverter
                    .newInstance();
            if (converter instanceof BooleanConverter<?>) {

                final BooleanConverter<?> convtr = (BooleanConverter<?>) converter;
                if (!isOptional) {
                    final BooleanField<?, ?> fld = FACTORY.newBooleanField(
                            convtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final BooleanOptionalField<?, ?> fld = FACTORY
                            .newBooleanOptional(convtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }
            } else if (converter instanceof ByteConverter<?>) {

                final ByteConverter<?> cnvrtr = (ByteConverter<?>) converter;
                if (!isOptional) {
                    final ByteField<?, ?> fld = FACTORY.newByteField(cnvrtr,
                            theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final ByteOptionalField<?, ?> fld = FACTORY
                            .newByteOptional(cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }

            } else if (converter instanceof CharConverter<?>) {
                final CharConverter<?> cnvrtr = (CharConverter<?>) converter;
                if (!isOptional) {
                    final CharField<?, ?> fld = FACTORY.newCharField(cnvrtr,
                            theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final CharOptionalField<?, ?> fld = FACTORY
                            .newCharOptional(cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }
            } else if (converter instanceof DoubleConverter<?>) {

                final DoubleConverter<?> cnvrtr = (DoubleConverter<?>) converter;
                if (!isOptional) {
                    final DoubleField<?, ?> fld = FACTORY.newDoubleField(
                            cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    childArray = new Array(theFieldInfo.name(),
                            FACTORY.newDoubleOptional(cnvrtr,
                                    theFieldInfo.name()),
                            theFieldInfo.arraySize);
                }

            } else if (converter instanceof FloatConverter<?>) {

                final FloatConverter<?> cnvrtr = (FloatConverter<?>) converter;
                if (!isOptional) {
                    final FloatField<?, ?> fld = FACTORY.newFloatField(cnvrtr,
                            theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final FloatOptionalField<?, ?> fld = FACTORY
                            .newFloatOptional(cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }

            } else if (converter instanceof IntConverter<?>) {

                final IntConverter<?> cnvrtr = (IntConverter<?>) converter;

                if (!isOptional) {
                    final IntField<?, ?> fld = FACTORY.newIntField(cnvrtr,
                            theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final IntOptionalField<?, ?> fld = FACTORY.newIntOptional(
                            cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }

            } else if (converter instanceof LongConverter<?>) {

                final LongConverter<?> cnvrtr = (LongConverter<?>) converter;
                if (!isOptional) {
                    final LongField<?, ?> fld = FACTORY.newLongField(cnvrtr,
                            theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final LongOptionalField<?, ?> fld = FACTORY
                            .newLongOptional(cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }
            } else if (converter instanceof ShortConverter<?>) {
                final ShortConverter<?> cnvrtr = (ShortConverter<?>) converter;
                if (!isOptional) {

                    final ShortField<?, ?> fld = FACTORY.newShortField(cnvrtr,
                            theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {

                    final ShortOptionalField<?, ?> fld = FACTORY
                            .newShortOptional(cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }
            } else {

                final Converter<?> cnvrtr = converter;
                if (!isOptional) {
                    final ObjectField<?, ?> fld = FACTORY.newObjectField(
                            cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                } else {
                    final ObjectField<?, ?> fld = FACTORY.newObjectOptional(
                            cnvrtr, theFieldInfo.name());
                    childArray = new Array(theFieldInfo.name(), fld,
                            theFieldInfo.arraySize);
                }
            }
        } else if (STRING_CLASS_NAME.equals(theFieldInfo.type
                .getComponentType().getName())) {
            if (!isOptional) {
                final ObjectField<?, ?> fld = FACTORY
                        .newStringField(theFieldInfo.name());
                childArray = new Array(theFieldInfo.name(), fld,
                        theFieldInfo.arraySize);
            } else {
                final ObjectField<?, ?> fld = FACTORY
                        .newStringOptional(theFieldInfo.name());
                childArray = new Array(theFieldInfo.name(), fld,
                        theFieldInfo.arraySize);
            }
        }
        if (childArray != null) {

            theChildren.add(childArray.arrayStruct());
        } else {

            throw new IllegalArgumentException("Could Not convert field :"
                    + theFieldInfo);
        }
    }
    /** Adds a setter method for a particular field.
     * @throws IllegalAccessException
     * @throws InstantiationException */
    private static void addSetter(final CtClass theClass,
            final FieldInfo theFieldInfo) throws CannotCompileException,
            NotFoundException, InstantiationException, IllegalAccessException {
        /*
         * The Generated setter method looks like :
               public void setXyz(byte byte0) {
                    _storage.write(this.xyz, byte0);
               }
         */
        checkArgument(isField(theFieldInfo), "Interface :" + theClass.getName()
                + " can have setter methods only for Primitives."
                + " Only getter methods should exist for Array or Child"
                + " interface types ");

        final String theArgTypeName = theFieldInfo.type.getName();
        String methodBody = null;
        if (isField(theFieldInfo)) {
            if (!theFieldInfo.isPrimitiveArray) {
                methodBody = "public void " + theFieldInfo.setterMethod + "("
                        + theArgTypeName + " a) { " + STORAGE_FIELD_NAME
                        + ".write(this." + theFieldInfo.name() + ", $1); }";
            } else {
                final String methodName;
                final CtClass arrayType = theFieldInfo.type.getComponentType();

                if (arrayType.getName().equals(STRING_CLASS_NAME)) {
                    methodName = "writeObjectArray";
                } else if (arrayType.isPrimitive()) {
                    methodName = "writeArray";
                } else {

                    // check converter type.
                    final Class<? extends Converter<?>> converter = theFieldInfo.objectToByteConverter;
                    final Converter<?> instance = converter.newInstance();
                    if (instance instanceof BooleanConverter<?>) {
                        methodName = "writeBooleanToObjectArray";
                    } else if (instance instanceof ByteConverter<?>) {
                        methodName = "writeByteToObjectArray";
                    } else if (instance instanceof CharConverter<?>) {
                        methodName = "writeCharToObjectArray";
                    } else if (instance instanceof DoubleConverter<?>) {
                        methodName = "writeDoubleToObjectArray";
                    } else if (instance instanceof FloatConverter<?>) {
                        methodName = "writeFloatToObjectArray";
                    } else if (instance instanceof IntConverter<?>) {
                        methodName = "writeIntToObjectArray";
                    } else if (instance instanceof LongConverter<?>) {
                        methodName = "writeLongToObjectArray";
                    } else if (instance instanceof ShortConverter<?>) {
                        methodName = "writeShortToObjectArray";
                    } else {
                        methodName = "writeObjectArray";
                    }
                }

                methodBody = "public void " + theFieldInfo.setterMethod + "("
                        + theArgTypeName + " a) { " + ARRAY_UTIL + "."
                        + methodName + "(" + STORAGE_FIELD_NAME + ", "
                        + theFieldInfo.name() + ", $1); }";
            }
        } else {
            methodBody = "public void " + theFieldInfo.setterMethod + "("
                    + theArgTypeName + " a) { this." + theFieldInfo.name()
                    + "= $1; }";
        }
        final CtMethod mthd = CtNewMethod.make(methodBody, theClass);
        theClass.addMethod(mthd);
    }

    /** Adds a short array struct to the Children-List. */
    @SuppressWarnings("null")
    private static void addShortArray(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? SHORT_BITS : bits;

        addArray(theChildren, theFieldInfo,
                FACTORY.newShortField(theFieldInfo.name(), bits));
    }

    /** Adds a short type field to the Fields-List. */
    private static void addShortfield(final List<Field<?, ?>> theFields,
            final FieldInfo theFieldInfo) {

        int bits = theFieldInfo.childProps.getBits();
        bits = bits == 0 ? SHORT_BITS : bits;

        if (theFieldInfo.childProps.isOptional()) {
            theFields.add(FACTORY.newShortOptional(theFieldInfo.name(), bits));
        } else {
            theFields.add(FACTORY.newShortField(theFieldInfo.name(), bits));
        }
    }

    /** Checks if the current class is already added as Parent. */
    @Nullable
    private static StructInfo alreadyAdded(final CtClass theClass,
            final List<StructInfo> theFlatList) {
        for (final StructInfo sinfo : theFlatList) {
            if (sinfo.interfaceClass.getName().equals(theClass.getName())) {
                return sinfo;
            }
        }
        return null;
    }

    /** Creates AddListener methods */
    private static void createAddListenerMethods(final CtClass theClass)
            throws CannotCompileException {
        /*
         * Adds a method which looks like :
                public void addListener(ValueChangeListener changelistener)
                {
                    ChangeListenerAdapter changelisteneradapter = new ChangeListenerAdapter(changelistener, _proxy);
                    Field afield[] = _struct.fields();
                    for(int i = 0; i < afield.length; i++)
                        _storage.changeListenerSupport().addListener(afield[i], changelisteneradapter);
                }
         */
        final String mBody = "public void addListener("
                + CHANGE_LISTENER_CLASS_NAME + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME + "( theListener, "
                + PROXY_FIELD_NAME + ");" + FIELD_CLASS_NAME + "[] fields = "
                + STRUCT_FIELD_NAME + ".fields();"
                + "for(int i = 0 ; i < fields.length ; i++){"
                + STORAGE_FIELD_NAME
                + ".changeListenerSupport().addListener(fields[i], _adapter);"
                + "}}";

        final CtMethod m1 = CtNewMethod.make(mBody, theClass);
        theClass.addMethod(m1);

        /*
         * Adds a method which looks like :
                public void addListener(String s, ChangeListener changelistener)
                {
                    ChangeListenerAdapter changelisteneradapter = new ChangeListenerAdapter(changelistener, _proxy);
                    Field afield[] = _struct.searchFields(s);
                    for(int i = 0; i < afield.length; i++)
                        _storage.changeListenerSupport().addListener(afield[i], changelisteneradapter);
                }
         *
         */

        final String mBody2 = "public void addListener(String theFieldName, "
                + CHANGE_LISTENER_CLASS_NAME + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME + "( theListener, "
                + PROXY_FIELD_NAME + ");" + FIELD_CLASS_NAME + "[] fields = "
                + STRUCT_FIELD_NAME + ".searchFields(theFieldName);"
                + "for(int i = 0 ; i < fields.length ; i++){"
                + STORAGE_FIELD_NAME
                + ".changeListenerSupport().addListener(fields[i], _adapter);"
                + "}}";

        final CtMethod m2 = CtNewMethod.make(mBody2, theClass);
        theClass.addMethod(m2);

        final String mBody3 = "public void addPostCommitListener("
                + TRANS_CHANGE_LISTENER_CLASS_NAME + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME + "( theListener, "
                + PROXY_FIELD_NAME + ");" + STORAGE_FIELD_NAME
                + ".changeListenerSupport()"
                + ".addPostCommitListener(_adapter);" + "}";

        final CtMethod m3 = CtNewMethod.make(mBody3, theClass);
        theClass.addMethod(m3);

        final String mBody4 = "public void addPreCommitListener("
                + TRANS_CHANGE_LISTENER_CLASS_NAME + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME + "( theListener, "
                + PROXY_FIELD_NAME + ");" + STORAGE_FIELD_NAME
                + ".changeListenerSupport()"
                + ".addPreCommitListener(_adapter);" + "}";

        final CtMethod m4 = CtNewMethod.make(mBody4, theClass);
        theClass.addMethod(m4);

        final String mBody5 = "public void removeListener("
                + CHANGE_LISTENER_CLASS_NAME
                + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME
                + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME
                + "( theListener, "
                + PROXY_FIELD_NAME
                + ");"
                + FIELD_CLASS_NAME
                + "[] fields = "
                + STRUCT_FIELD_NAME
                + ".fields();"
                + "for(int i = 0 ; i < fields.length ; i++){"
                + STORAGE_FIELD_NAME
                + ".changeListenerSupport().removeListener(fields[i], _adapter);"
                + "}}";

        final CtMethod m5 = CtNewMethod.make(mBody5, theClass);
        theClass.addMethod(m5);

        final String mBody6 = "public void removeListener(String theFieldName, "
                + CHANGE_LISTENER_CLASS_NAME
                + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME
                + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME
                + "( theListener, "
                + PROXY_FIELD_NAME
                + ");"
                + FIELD_CLASS_NAME
                + "[] fields = "
                + STRUCT_FIELD_NAME
                + ".searchFields(theFieldName);"
                + "for(int i = 0 ; i < fields.length ; i++){"
                + STORAGE_FIELD_NAME
                + ".changeListenerSupport().removeListener(fields[i], _adapter);"
                + "}}";

        final CtMethod m6 = CtNewMethod.make(mBody6, theClass);
        theClass.addMethod(m6);

        final String mBody7 = "public void removePostCommitListener("
                + TRANS_CHANGE_LISTENER_CLASS_NAME + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME + "( theListener, "
                + PROXY_FIELD_NAME + ");" + STORAGE_FIELD_NAME
                + ".changeListenerSupport()"
                + ".addPostCommitListener(_adapter);" + "}";

        final CtMethod m7 = CtNewMethod.make(mBody7, theClass);
        theClass.addMethod(m7);

        final String mBody8 = "public void removePreCommitListener("
                + TRANS_CHANGE_LISTENER_CLASS_NAME + " theListener){"
                + CHANGE_ADAPTER_CLASS_NAME + " _adapter = new "
                + CHANGE_ADAPTER_CLASS_NAME + "( theListener, "
                + PROXY_FIELD_NAME + ");" + STORAGE_FIELD_NAME
                + ".changeListenerSupport()"
                + ".removePreCommitListener(_adapter);" + "}";

        final CtMethod m8 = CtNewMethod.make(mBody8, theClass);
        theClass.addMethod(m8);

    }

    /** Creates the base struct info. */
    private static StructInfo createBaseStructInfo(
            final List<StructInfo> theStructInfoList)
            throws InvocationTargetException, CannotCompileException,
            NotFoundException {
        final Struct[] children = new Struct[theStructInfoList.size()];
        int count = 0;
        for (final StructInfo sInfo : theStructInfoList) {
            children[count++] = sInfo.struct;
        }

        final Struct s = new Struct("_Base", true, children,
                (Field<?, ?>[]) null);
        final StructInfo sInfo = new StructInfo();
        sInfo.children = null;
        sInfo.attributeMap = null;
        sInfo.interfaceClass = null;
        sInfo.struct = s;
        sInfo.name = s.name();
        sInfo.superClasses = theStructInfoList;
        return sInfo;
    }

    /** Creates copy method. */
    private static void createCopy(final CtClass theClass,
            final StructInfo theStruct) throws CannotCompileException {
        /*
         *  Generates a method similar to :
                public Object copy(Storage storage)
                {
                    return new [ClassName](storage, _struct, _proxy, field1, field2..);
                }
         */
        final StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("public ")
                .append(Object.class.getName())
                .append(" copy( " + StructConstants.STORAGE_CLASS_NAME
                        + " theStorage ){");
        sBuilder.append("return new ").append(theClass.getName())
                .append("(theStorage, ").append(STRUCT_FIELD_NAME).append(", ")
                .append(PROXY_FIELD_NAME);
        if (theStruct.children != null && theStruct.children.size() > 0) {
            for (final StructInfo csInfo : theStruct.children) {
                sBuilder.append(',').append(csInfo.name);
            }
        }
        for (final FieldInfo fInfo : theStruct.attributeMap.values()) {
            if (fInfo.inherited || isField(fInfo)) {
                // we skip the fields of 'children' type because we have
                // processed them already.
                sBuilder.append(',').append(fInfo.name());
            }
        }

        sBuilder.append(");}");
        // System.out.println(sBuilder);
        final CtMethod con = CtNewMethod.make(sBuilder.toString(), theClass);
        theClass.addMethod(con);
    }

    @SuppressWarnings({ "unused", "boxing" })
    private static void debugPrint(final CtClass theClass) {
        final String saveClasses = System.getProperty(SAVE_PROXY_CLASSES);
        if (saveClasses != null && Boolean.valueOf(saveClasses)) {
            /* If Generating the actual '.class' files for debugging purpose is needed,
             * un-comment the following try/catch block to generate '.class' files for all the
             * generated classes. NOTE - If this is un-commented, classes will be
             * generated only on the first execution */
            try {
                theClass.writeFile(System.getProperty("java.io.tmpdir"));
            } catch (final Exception e) { // $codepro.audit.disable
                                          // emptyCatchClause
            }
        }
    }
    /** Searches for, and returns the Field for the given name in FieldInfo. */
    private static Field<?, ?> getFieldByName(final StructInfo theStruct,
            final FieldInfo theFieldInfo) {
        final Field<?, ?> f;
        if (theStruct.arraySize == 0) {
            f = theStruct.struct.field(theFieldInfo.name());
        } else {
            final Struct s = theStruct.struct.child(theStruct.name + "[0]");
            f = (Field<?, ?>) s.field(theFieldInfo.name());
        }
        return f;
    }

    /** Process primitive arrays. */
    private static void processArrays(final List<Struct> theChildren,
            final FieldInfo theFieldInfo) throws NotFoundException,
            InstantiationException, IllegalAccessException {
        final FieldType ft = FieldType.from(theFieldInfo.type
                .getComponentType().getName().toUpperCase());
        switch (ft) {
        case BYTE:
            addByteArray(theChildren, theFieldInfo);
            break;
        case CHAR:
            addCharArray(theChildren, theFieldInfo);
            break;
        case DOUBLE:
            addDoubleArray(theChildren, theFieldInfo);
            break;
        case FLOAT:
            addFloatArray(theChildren, theFieldInfo);
            break;
        case INT:
            addIntArray(theChildren, theFieldInfo);
            break;
        case LONG:
            addLongArray(theChildren, theFieldInfo);
            break;
        case SHORT:
            addShortArray(theChildren, theFieldInfo);
            break;
        case BOOLEAN:
            addBooleanArray(theChildren, theFieldInfo);
            break;
        default:
            addObjectArray(theChildren, theFieldInfo);
        }
    }

    /** Process primitive fields. */
    private static void processPrimitiveFields(
            final List<Field<?, ?>> theFields, final FieldInfo theFieldInfo) {
        // All primitive are fields
        final FieldType ft = FieldType.from(theFieldInfo.type.getName());
        switch (ft) {
        case BYTE:
            addByteField(theFields, theFieldInfo);
            break;
        case CHAR:
            addCharField(theFields, theFieldInfo);
            break;
        case DOUBLE:
            addDoubleField(theFields, theFieldInfo);
            break;
        case FLOAT:
            addFloatField(theFields, theFieldInfo);
            break;
        case INT:
            addIntField(theFields, theFieldInfo);
            break;
        case LONG:
            addLongField(theFields, theFieldInfo);
            break;
        case SHORT:
            addShortfield(theFields, theFieldInfo);
            break;
        case BOOLEAN:
            addBooleanField(theFields, theFieldInfo);
            break;
        default:
            break;
        }
    }

    /** Process super class fields. */
    private static void processSuperFields(
            @Nullable final List<StructInfo> theSuperInterfaces,
            final SortedMap<String, FieldInfo> theCurrentMap)
            throws CannotCompileException, NotFoundException {

        if (theSuperInterfaces != null) {
            for (final StructInfo psInfo : theSuperInterfaces) {
                // Add all super class fields into current class.
                // This will include all the classes up the tree.
                // just change the field names to avoid conflicts.
                for (final Entry<String, FieldInfo> e : psInfo.attributeMap
                        .entrySet()) {

                    // create a copy with a qualified name.
                    final FieldInfo original = e.getValue();
                    boolean alreadyPresent = false;
                    // currently we don't allow any duplicate fields names.
                    for (final Entry<String, FieldInfo> ef : theCurrentMap
                            .entrySet()) {
                        final FieldInfo fino = ef.getValue();
                        if (fino.getterMethod.equals(original.getterMethod)
                                || fino.setterMethod != null
                                && fino.setterMethod
                                        .equals(original.setterMethod)) {
                            checkArgument(original.type.equals(fino.type),
                                    "Duplicate fields found in class hierarchy - Field "
                                            + original.name()
                                            + " in interface "
                                            + original.interfaceName);
                            alreadyPresent = true;
                        }
                    }

                    if (!alreadyPresent) {
                        final String fullName = psInfo.interfaceClass
                                .getSimpleName() + "_" + original.name();
                        final FieldInfo fInfo = original.setInherited(true)
                                .setName(fullName);
                        theCurrentMap.put(fInfo.name(), fInfo);
                    }
                }
            }
        }
    }

    /** Instantiates a new proxy cursor. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    private ProxyCursor(final Class<?>[] theInterfaces,
            final Class<?> theDefaultType, final Compiler theCompiler,
            @Nullable final Storage theStorage, final int theCapacity,
            @Nullable final ConverterRegistry theRegistry) {

        checkNotNull(theInterfaces);
        checkNotNull(theDefaultType);
        checkNotNull(theCompiler);

        boolean found = false;
        for (final Class<?> clz : theInterfaces) {
            if (theDefaultType.equals(clz)) {
                found = true;
            }
        }
        checkArgument(
                found,
                "Invalid defaultType - "
                        + theDefaultType.getName()
                        + ", It must be equal to one of the inteface types passed!");

        defaultType = theDefaultType;
        interfaces = theInterfaces;
        compiler = theCompiler;
        registry = theRegistry;
        try {
            enrichMap = Collections.unmodifiableMap(getEnrichmentMap());
            final StructInfo baseInfo = getStructInfo(theCompiler);
            structInfo = baseInfo.blankCopy();
            storage = theStorage != null ? theStorage : theCompiler
                    .initStorage(structInfo.struct, theCapacity);
            createInstances(structInfo, structInfo.implClass(), storage, -1);
            indexField = structInfo.struct.field(INDEX_FIELD_NAME);
            indexMap = new HashMap<>();

            for (final StructInfo s : structInfo.superClasses) {
                CtClass interfaceClass = s.interfaceClass;
                if (interfaceClass.hasAnnotation(Enrich.class)) {
                    interfaceClass = s.interfaceClass.getInterfaces()[0];
                }
                indexMap.put(Class.forName(interfaceClass.getName()), s);
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | InvocationTargetException
                | IllegalClassFormatException | NotFoundException
                | CannotCompileException exp) {
            throw new IllegalStateException(exp.getMessage(), exp);
        }
    }

    /**
     * Instantiates a new proxy cursor.
     *
     * @param theInterfaces an array of proxy interfaces to be processed, the proxy can take the form of any elements
     * int this array, the array can also contain 'Enrich' (interface annotated by @Enrich) interfaces.
     * @param theDefaultType the default type this proxy should take
     * @param theCompiler the compiler
     * @param theCapacity the capacity
     */
    public ProxyCursor(final Class<?>[] theInterfaces,
            final Class<?> theDefaultType, final Compiler theCompiler,
            final int theCapacity) {
        this(theInterfaces, theDefaultType, theCompiler, null, theCapacity,
                null);
    }

    /**
     * Instantiates a new proxy cursor.
     *
     * @param theInterfaces an array of proxy interfaces to be processed, the proxy can take the form of any elements
     * int this array, the array can also contain 'Enrich' (interface annotated by @Enrich) interfaces.
     * @param theDefaultType the default type this proxy should take
     * @param theCompiler the compiler
     * @param theCapacity the capacity
     * @param theRegistry pass an instance of converter registry in case the proxy contains primitiveable fields.
     */
    public ProxyCursor(final Class<?>[] theInterfaces,
            final Class<?> theDefaultType, final Compiler theCompiler,
            final int theCapacity, final ConverterRegistry theRegistry) {
        this(theInterfaces, theDefaultType, theCompiler, null, theCapacity,
                theRegistry);
    }

    /**
     * Instantiates a new proxy cursor.
     *
     * @param theInterfaces an array of proxy interfaces to be processed, the proxy can take the form of any elements
     * int this array, the array can also contain 'Enrich' (interface annotated by @Enrich) interfaces.
     * @param theCompiler the compiler
     * @param theCapacity the capacity
     */
    @SuppressWarnings("null")
    public ProxyCursor(final Class<?>[] theInterfaces,
            final Compiler theCompiler, final int theCapacity) {
        this(theInterfaces, theInterfaces[0], theCompiler, theCapacity);
    }

    /**
     * Instantiates a new proxy cursor.
     *
     * @param theInterfaces an array of proxy interfaces to be processed, the proxy can take the form of any elements
     * int this array, the array can also contain 'Enrich' (interface annotated by @Enrich) interfaces.
     * @param theCompiler the compiler
     * @param theCapacity the capacity
     * @param theRegistry pass an instance of converter registry in case the proxy contains primitiveable fields.
     */
    @SuppressWarnings("null")
    public ProxyCursor(final Class<?>[] theInterfaces,
            final Compiler theCompiler, final int theCapacity,
            final ConverterRegistry theRegistry) {
        this(theInterfaces, theInterfaces[0], theCompiler, theCapacity,
                theRegistry);
    }

    /** Adds the constructor parameters to String.
     * @throws NotFoundException */
    private void addParameters(final StringBuilder theBuilder,
            final StructInfo theStruct) throws NotFoundException {
        if (theStruct.children == null && theStruct.attributeMap == null) {
            return;
        }
        theBuilder.append(STORAGE_CLASS_NAME).append(" a0, ");
        theBuilder.append(STRUCT_CLASS_NAME).append(" a1, ");
        theBuilder.append(PROXY_CLASS_NAME).append(" a2");
        int count = 3;

        if (theStruct.children != null) {
            for (final StructInfo sInfo : theStruct.children) {

                theBuilder.append(", ");
                CtClass interfaceClass = sInfo.interfaceClass;
                if (interfaceClass.hasAnnotation(Enrich.class)) {
                    interfaceClass = interfaceClass.getInterfaces()[0];
                }
                final String name = interfaceClass.getName();

                if (sInfo.arraySize == 0) {
                    theBuilder.append(name).append(" a").append(count++);
                } else {
                    theBuilder.append(name + "[]").append(" a").append(count++);

                }
            }
        }
        for (final FieldInfo fInfo : theStruct.attributeMap.values()) {
            if (!fInfo.inherited) {
                if (!isField(fInfo)) {
                    continue;
                }
                if (!fInfo.isPrimitiveArray) {
                    final Field<?, ?> f = getFieldByName(theStruct, fInfo);
                    theBuilder.append(", ");
                    theBuilder.append(f.getClass().getName()).append(" a")
                            .append(count++);
                } else {
                    theBuilder.append(", ");
                    theBuilder
                            .append(StructConstants.FIELD_CLASS_NAME + "[] a")
                            .append(count++);
                }
            } else {
                if (!isField(fInfo)) {
                    theBuilder.append(", ");
                    theBuilder.append(fInfo.type.getName()).append(" a")
                            .append(count++);
                } else {
                    @SuppressWarnings("rawtypes")
                    final Class<? extends Field> f = findSuperFieldClass(
                            theStruct, fInfo);
                    if (f != null) {
                        theBuilder.append(", ");
                        if (!fInfo.isPrimitiveArray) {
                            theBuilder.append(f.getName()).append(" a")
                                    .append(count++);
                        } else {
                            theBuilder.append(
                                    StructConstants.FIELD_CLASS_NAME + "[] a")
                                    .append(count++);
                        }
                    }
                }
            }
        }
    }

    /** Creates Class Constructor.
     * @throws NotFoundException */
    private void createConstructor(final CtClass theClass,
            final StructInfo theStruct) throws CannotCompileException,
            NotFoundException {
        /*
         * Creates a constructor which looks something like :

             public [ClassName](Storage storage, ProxyStruct proxystruct, ProxyCursor proxycursor,
                                 Child1 aChild1, Child2 aChild2, [Type]Field aField1, [Type]Field aField2
                                 //other fields sorted by name
             ){
                   _storage = storage;
                   _struct = proxystruct;
                   _proxy = proxycursor;
                   child1 = aChild1;
                   child2 = aChild1;
                   field1 = aField1;
                   field2 = aField2;
                   //..
               }
         *
         */
        final StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("public ").append(theClass.getSimpleName()).append('(');
        addParameters(sBuilder, theStruct);
        sBuilder.append("){");
        sBuilder.append(STORAGE_FIELD_NAME + " = a0;");
        sBuilder.append(STRUCT_FIELD_NAME + " = a1;");
        sBuilder.append(PROXY_FIELD_NAME + " = a2;");
        int count = 3;

        if (theStruct.children != null && theStruct.children.size() > 0) {
            for (final StructInfo csInfo : theStruct.children) {
                final String name = csInfo.name;
                sBuilder.append('\n').append(name).append(" = a")
                        .append(count++).append(';');
            }
        }
        for (final FieldInfo fInfo : theStruct.attributeMap.values()) {
            if (fInfo != null) {
                if (isField(fInfo) || fInfo.inherited) {
                    // we skip the fields of 'children' type because we have
                    // processed them already.
                    sBuilder.append('\n').append(fInfo.name()).append(" = a")
                            .append(count++).append(';');
                }
            }
        }
        sBuilder.append('}');
        final CtConstructor con = CtNewConstructor.make(sBuilder.toString(),
                theClass);
        theClass.addConstructor(con);
    }

    /** Recursively creates the instances of the generated classes. */
    private void createInstances(final StructInfo theSInfo,
            @Nullable final CtClass theClass, final Storage theStorage,
            final int theArrayIndex) throws CannotCompileException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {
        if (theSInfo.children != null) {
            for (final StructInfo c : theSInfo.children) {
                if (c.arraySize == 0) {
                    createInstances(c, c.implClass(), theStorage, -1);
                } else {
                    for (int i = 0; i < c.arraySize; i++) {
                        createInstances(c, c.implClass(), theStorage, i);
                    }
                }
            }
        }
        if (theSInfo.superClasses != null) {
            for (final StructInfo c : theSInfo.superClasses) {
                if (c.instance == null) {
                    createInstances(c, c.implClass(), theStorage, -1);
                }
            }
        }
        // theClass will be null for base struct-info (only super-classes will
        // be processed)
        if (theClass != null) {
            debugPrint(theClass);
            Class<?> c;
            try {
                c = Class.forName(theClass.getName());
            } catch (final ClassNotFoundException e) {
                c = theClass.toClass();
            }
            final List<Object> args = new ArrayList<>();
            args.add(theStorage);
            final Struct strct = theSInfo.struct;
            args.add(strct);
            args.add(this);

            if (theSInfo.children != null) {
                for (final StructInfo info : theSInfo.children) {
                    args.add(info.instance);
                }
            }
            for (final FieldInfo fInfo : theSInfo.attributeMap.values()) {
                if (!fInfo.inherited) {
                    if (!isField(fInfo)) {
                        continue;
                    }
                    if (!fInfo.isPrimitiveArray) {
                        final Field<?, ?> f = getFieldByName(theSInfo, fInfo);
                        args.add(f);
                    } else {
                        final Struct childArray = strct.child(fInfo.name());
                        args.add(childArray.fields());
                    }
                } else {
                    if (!isField(fInfo)) {
                        args.add(findSuperChildField(theSInfo, fInfo, ""));
                    } else {
                        if (!fInfo.isPrimitiveArray) {
                            final Field<?, ?> f = findSuperField(theSInfo,
                                    fInfo);
                            args.add(f);
                        } else {
                            final Field<?, ?>[] fArray = findSuperFieldArray(
                                    theSInfo, fInfo);
                            args.add(fArray);
                        }
                    }
                }
            }
            if (theArrayIndex == -1) {
                final Constructor<?> constructor = c.getConstructors()[0];
                theSInfo.instance = constructor.newInstance(args.toArray());
            } else {
                if (theSInfo.instance == null) {
                    theSInfo.instance = newInstance(c, theSInfo.arraySize);
                }
                set(theSInfo.instance, theArrayIndex,
                        c.getConstructors()[0].newInstance(args.toArray()));
            }
        }
    }

    /** Recursively creates StructInfo for an interface and super-interfaces. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    private StructInfo createStructInfo(final CtClass theInterface,
            final String theStructName, final List<StructInfo> theFlatList,
            final int theArraySize, final Set<CtClass> theSkipClasses)
            throws IllegalClassFormatException, ClassNotFoundException,
            NotFoundException, InvocationTargetException,
            CannotCompileException, InstantiationException,
            IllegalAccessException {

        checkArgument(theInterface.isInterface(),
                "The Type :" + theInterface.getName()
                        + " must be an interface.");

        // Get enriched interface if any.
        final CtClass interfaceC;

        // skip classes helps in avoiding infinite loop occurrence due to the
        // fact that the enrich interfaces extend proxy interfaces that are
        // being enriched.
        if (enrichMap.containsKey(theInterface)
                && !theSkipClasses.remove(theInterface)) {
            interfaceC = enrichMap.get(theInterface);
            theSkipClasses.add(theInterface);
        } else {
            interfaceC = theInterface;
        }

        // Get all the super interfaces of the current class and create a list
        // of parent StructInfo.
        final List<StructInfo> parents = new ArrayList<>();
        final List<Struct> pchList = new ArrayList<>();

        if (interfaceC.getInterfaces() != null) {
            for (final CtClass p : interfaceC.getInterfaces()) {
                if (p.getName().equals(LISTENER_INTERFACE_CLASS_NAME)) {
                    continue;
                }

                // We don't want a class to be added again if its already
                // present in the hierarchy, we don't add that if already
                // present because we want to allocate space for a class and
                // its fields only once in a type hierarchy. If the parent is
                // already present we just use the same reference.

                StructInfo psInfo = alreadyAdded(p, theFlatList);
                if (psInfo == null) {
                    psInfo = createStructInfo(p, toLower(p.getSimpleName()),
                            theFlatList, 0, theSkipClasses);
                    pchList.add(psInfo.struct);
                    parents.add(psInfo);
                }
                parents.add(psInfo);
                theFlatList.add(psInfo);
            }
        }

        // Get Field info of the current class.
        final SortedMap<String, FieldInfo> attributeMap = BeanParser
                .getFieldInfo(interfaceC, false,
                        new TreeMap<String, FieldInfo>(), false, registry);
        // Add fields of parent classes.
        processSuperFields(parents, attributeMap);
        final List<Field<?, ?>> fields = new ArrayList<>(attributeMap.size());
        final List<StructInfo> children = new ArrayList<>();
        final List<Struct> sChildren = new ArrayList<>();

        for (final Entry<String, FieldInfo> attrib : attributeMap.entrySet()) {
            final FieldInfo fInfo = attrib.getValue();
            if (fInfo.inherited) {
                // don't add the inherited fields inside this struct, those are
                // present in the super-struct's children.
                continue;
            }
            /* Types can be
             * 1. Primitive.
             * 2. A String field
             * 3. A Struct child
             * 4. A Struct Optional Child.
             * 5. An Object field convertible to byte[] .
             * 6. Primitiveable Object Field.
             * 7. An Array.
             * 8. An Enum.
             * 9. A Struct array of fixed size.
             */
            if (fInfo.type.isPrimitive()) {
                processPrimitiveFields(fields, fInfo);
            } else if (fInfo.isPrimitiveArray) {
                processArrays(sChildren, fInfo);
            } else {
                final boolean isOptional = fInfo.childProps.isOptional();
                final boolean isGlobal = fInfo.childProps.global();

                if (fInfo.isObjectField) {
                    // Indicates Object fields
                    final Converter<Object> converter = (Converter<Object>) fInfo.objectToByteConverter
                            .newInstance();
                    final ObjectField objectField = new ObjectField(converter,
                            fInfo.name(), isGlobal, isOptional, false, null);
                    fields.add(objectField);

                } else if (fInfo.type.isEnum()) {
                    // indicates enum field type
                    final Class enumClass = Class.forName(fInfo.type.getName());
                    final ByteField newEnumField = FACTORY.newEnumField(
                            fInfo.name(), enumClass);
                    fields.add(newEnumField);
                } else if (fInfo.type.getName().equals(STRING_CLASS_NAME)) {

                    // indicates String field type
                    if (isOptional) {
                        fields.add(FACTORY.newStringOptional(fInfo.name()));
                    } else {
                        fields.add(FACTORY.newStringField(fInfo.name()));
                    }
                } else {
                    if (fInfo.isList) {
                        final StructInfo info = createStructInfo(
                                POOL.get(fInfo.listInterface), fInfo.name(),
                                new ArrayList<StructInfo>(), 0, theSkipClasses);
                        info.struct = info.struct.setList(true);
                        sChildren.add(info.struct);
                        children.add(info);
                    } else if (fInfo.isChildArray) {
                        // Indicates child array field types.
                        final StructInfo info = createStructInfo(
                                fInfo.type.getComponentType(), fInfo.name(),
                                new ArrayList<StructInfo>(), fInfo.arraySize,
                                theSkipClasses);
                        if (isOptional) {
                            info.struct = info.struct.setOptional(true);
                        }
                        sChildren.add(info.struct);
                        children.add(info);

                    } else {
                        // Indicates child interface field types.
                        final StructInfo info = createStructInfo(fInfo.type,
                                fInfo.name(), new ArrayList<StructInfo>(), 0,
                                theSkipClasses);
                        if (isOptional) {
                            info.struct = info.struct.setOptional(true);
                        }
                        sChildren.add(info.struct);
                        children.add(info);
                    }
                }
            }
        }
        if (!pchList.isEmpty()) {
            sChildren.addAll(pchList);
        }

        final Struct s;
        if (theArraySize == 0) {
            s = new Struct(theStructName, childrenArray(sChildren),
                    fieldArray(fields));
        } else {
            s = new ChildrenArray(theStructName, new Struct(theStructName,
                    sChildren.toArray(new Struct[sChildren.size()]),
                    fields.toArray(new Field<?, ?>[fields.size()])),
                    theArraySize).arrayStruct();
        }
        final StructInfo sInfo = new StructInfo();
        sInfo.children = children;
        sInfo.attributeMap = attributeMap;
        sInfo.interfaceClass = interfaceC;
        sInfo.struct = s;
        sInfo.name = s.name();
        sInfo.superClasses = parents;
        sInfo.arraySize = theArraySize;
        return sInfo;

    }

    /** Find super child field. */
    @SuppressWarnings("null")
    private Object findSuperChildField(final StructInfo theStruct,
            final FieldInfo theFieldInfo, final String thePrefix) {

        final int index = theFieldInfo.name().indexOf('_', thePrefix.length());
        final String interfaceName = theFieldInfo.name().substring(
                thePrefix.length(), index);
        final String fieldName = theFieldInfo.name().substring(index + 1);

        for (final StructInfo s : theStruct.superClasses) {
            if (s.interfaceClass.getSimpleName().equals(interfaceName)) {
                for (final StructInfo child : s.children) {
                    if (child.name.equals(fieldName)) {
                        return child.instance;
                    }
                }
                return findSuperChildField(s, theFieldInfo, thePrefix
                        + interfaceName + "_");
            }
        }
        // 'return null' is intentional.
        return null;
    }

    /** Find super child field. */
    @SuppressWarnings("null")
    private Field<?, ?> findSuperField(final StructInfo theStruct,
            final FieldInfo theFieldInfo) {
        Field<?, ?> result = null;
        for (final StructInfo s : theStruct.superClasses) {
            if (!s.interfaceClass.getName().equals(theFieldInfo.interfaceName)) {
                result = findSuperField(s, theFieldInfo);
                if (result != null) {
                    return result;
                }
            } else {
                return s.struct.field(theFieldInfo.simpleName);
            }
        }
        return result;
    }

    /** Find super field array. */
    @SuppressWarnings("null")
    private Field<?, ?>[] findSuperFieldArray(final StructInfo theStruct,
            final FieldInfo theFieldInfo) {
        Field<?, ?>[] results = null;
        for (final StructInfo s : theStruct.superClasses) {
            if (!s.interfaceClass.getName().equals(theFieldInfo.interfaceName)) {
                results = findSuperFieldArray(s, theFieldInfo);
                if (results != null) {
                    return results;
                }
            } else {
                return s.struct.child(theFieldInfo.simpleName).fields();
            }
        }
        return results;
    }

    /** Find super field class. */
    @SuppressWarnings({ "unchecked", "null" })
    @Nullable
    private Class<? extends Field<?, ?>> findSuperFieldClass(
            final StructInfo theStruct, final FieldInfo theFieldInfo) {
        Class<? extends Field<?, ?>> result = null;
        for (final StructInfo s : theStruct.superClasses) {
            if (!s.interfaceClass.getName().equals(theFieldInfo.interfaceName)) {
                result = findSuperFieldClass(s, theFieldInfo);
                if (result != null) {
                    return result;
                }
            } else {
                if (!theFieldInfo.isPrimitiveArray) {
                    return (Class<? extends Field<?, ?>>) s.struct.field(
                            theFieldInfo.simpleName).getClass();
                }
                return (Class<? extends Field<?, ?>>) s.struct.child(
                        theFieldInfo.simpleName).fields()[0].getClass();
            }
        }
        return result;
    }

    /** Generates a particular class, fields and methods.It will not re-create
     * children if the children already exist in the class pool */
    @SuppressWarnings("null")
    private CtClass generateClass(final StructInfo theStruct,
            final String theClassName, final boolean isList)
            throws CannotCompileException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NotFoundException {

        final CtClass cc = POOL.makeClass(theClassName);
        if (theStruct.interfaceClass != null) {
            cc.addInterface(theStruct.interfaceClass);
        } else {
            for (final Class<?> c : interfaces) {
                cc.addInterface(POOL.get(c.getName()));
            }
        }
        addField(cc, STORAGE_CLASS_NAME, STORAGE_FIELD_NAME);
        addField(cc, STRUCT_CLASS_NAME, STRUCT_FIELD_NAME);
        addField(cc, PROXY_CLASS_NAME, PROXY_FIELD_NAME);

        // Process children structs
        if (theStruct.children != null) {
            for (final StructInfo childStruct : theStruct.children) {
                if (childStruct.arraySize == 0) {
                    addField(cc, childStruct.interfaceClass.getName(),
                            childStruct.struct.name());
                } else {
                    addField(cc, childStruct.interfaceClass.getName() + "[]",
                            childStruct.struct.name());
                }
            }
        }
        for (final FieldInfo fInfo : theStruct.attributeMap.values()) {
            if (!fInfo.inherited) {
                if (!isField(fInfo)) {
                    continue;
                }
                if (!fInfo.isPrimitiveArray && !fInfo.isChildArray) {
                    addNormalField(theStruct, cc, fInfo);
                } else {
                    addArrayField(cc, fInfo);
                }
            } else {
                if (!isField(fInfo)) {
                    addField(cc, fInfo.type.getName(), fInfo.name());
                } else {
                    @SuppressWarnings("rawtypes")
                    final Class<? extends Field> f = findSuperFieldClass(
                            theStruct, fInfo);
                    checkNotNull(f, "Super class Field not found");
                    addField(cc, f.getName(), fInfo.name());
                }
            }
        }
        createConstructor(cc, theStruct);
        if (isList) {
            cc.addInterface(POOL.get(COPY_BEAN_CLASS_NAME));
            createCopy(cc, theStruct);
        }
        createAddListenerMethods(cc);
        cc.addInterface(POOL.get(LISTENER_INTERFACE_CLASS_NAME));
        addMethods(theStruct.attributeMap, cc);
        cc.setModifiers(Modifier.PUBLIC);
        cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);
        return cc;
    }

    /** Loads the class if already exists else creates the class. */
    @SuppressWarnings("null")
    private CtClass generateClasses(final StructInfo theStruct,
            final boolean isList) throws CannotCompileException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NotFoundException {
        CtClass cClass;
        final String clsName = theStruct.interfaceClass.getName()
                + "CompactImpl";
        // generate the super classes first
        if (theStruct.superClasses != null) {
            for (final StructInfo psInfo : theStruct.superClasses) {
                if (psInfo.implClass == null) {
                    psInfo.implClass(generateClasses(psInfo, false));
                }
            }
        }
        // Now process children structs
        if (theStruct.children != null) {
            for (final StructInfo childStruct : theStruct.children) {
                boolean listChild = false;
                if (theStruct.attributeMap != null) {
                    final FieldInfo fInfo = theStruct.attributeMap
                            .get(childStruct.name);
                    listChild = fInfo.isList;
                }
                generateClasses(childStruct, listChild);
            }
        }
        try {
            cClass = POOL.get(clsName);
        } catch (final NotFoundException exp) {
            cClass = generateClass(theStruct, clsName, isList);
        }
        theStruct.implClass(cClass);
        return cClass;
    }

    /** Builds the enrichment map */
    private Map<CtClass, CtClass> getEnrichmentMap() throws NotFoundException,
            ClassNotFoundException {

        final Map<CtClass, CtClass> tempMap = new HashMap<>();
        for (final Class<?> c : interfaces) {

            final CtClass theInterface = POOL.get(c.getName());
            final Enrich isEnrich = (Enrich) theInterface
                    .getAnnotation(Enrich.class);

            if (isEnrich != null) {
                final CtClass[] enrichable = theInterface.getInterfaces();
                checkState(enrichable.length == 1,
                        "An @Enrich interface can enrich exactly one interface!");
                checkState(!tempMap.containsKey(enrichable[0]),
                        "Duplicate @Enrich interface found for: "
                                + enrichable[0]);
                tempMap.put(enrichable[0], theInterface);
            }
        }
        return tempMap;
    }

    /** Finds or generates the StructInfo for our configuration for the given compiler. */
    @SuppressWarnings("null")
    private StructInfo getStructInfo(final Compiler theCompiler)
            throws NotFoundException, IllegalClassFormatException,
            ClassNotFoundException, InvocationTargetException,
            CannotCompileException, InstantiationException,
            IllegalAccessException {

        final CacheKey key = new CacheKey(theCompiler.getClass(), interfaces);
        StructInfo baseInfo = CACHE.get(key);
        if (baseInfo != null) {
            return baseInfo;
        }
        final List<StructInfo> structInfoList = new ArrayList<>();
        for (final Class<?> c : interfaces) {
            final CtClass theInterface = POOL.get(c.getName());
            if (!theInterface.hasAnnotation(Enrich.class)) {
                structInfoList
                        .add(createStructInfo(theInterface,
                                toLower(c.getSimpleName()),
                                new ArrayList<StructInfo>(), 0,
                                new HashSet<CtClass>()));
            }
        }
        checkState(structInfoList.size() > 0,
                "The Array of interfaces passed to the proxy "
                        + "contains only @Enrich Interfaces ! ");
        baseInfo = createBaseStructInfo(structInfoList);
        for (final StructInfo member : baseInfo.superClasses) {
            generateClasses(member, false);
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Struct compiled = theCompiler.compile(baseInfo.struct);
        // System.out.println(compiled);
        updateReference(baseInfo, compiled);
        CACHE.put(key, baseInfo);
        return baseInfo;
    }

    /** Select type. */
    @SuppressWarnings("null")
    private void selectType(final Class<?> theType1) {
        checkNotNull(theType1);
        final Class<?> requestedType = theType1.getAnnotation(Enrich.class) == null ? theType1
                : theType1.getInterfaces()[0];
        checkArgument(indexMap.containsKey(requestedType),
                "The Type: " + requestedType.getName()
                        + " is not a valid type: " + indexMap.keySet());
        final int index = indexMap.get(requestedType).struct.childIndex();
        storage.selectUnionPosition(structInfo.struct, index);
    }

    /** Recursively updates references of all the children structs from non
     * compiled to compiled */
    private void updateReference(final StructInfo theStructInfo,
            final Struct theCompiledStruct) {
        theStructInfo.struct = theCompiledStruct;
        final List<StructInfo> children = theStructInfo.children;
        final List<StructInfo> parents = theStructInfo.superClasses;
        if (children != null || parents != null) {
            for (final Struct child : theCompiledStruct.children()) {
                boolean found = false;
                if (parents != null) {
                    for (final StructInfo sInfo : parents) {
                        if (sInfo.struct.name().equals(child.name())) {
                            updateReference(sInfo, child);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found && children != null) {
                    for (final StructInfo sInfo : children) {
                        if (sInfo.struct.name().equals(child.name())) {
                            updateReference(sInfo, child);
                        }
                    }
                }
            }
        }
    }

    /** Returns a new instance of this proxy cursor with existing copied */
    @SuppressWarnings("null")
    public final ProxyCursor copy() {
        return new ProxyCursor(interfaces, defaultType, compiler,
                storage.copy(), storage.getCapacity(), registry);
    }

    /** @see Storage#enableTransactions(boolean)*/
    public void enableTransactions(final boolean theEnableFlag) {
        storage.enableTransactions(theEnableFlag);
    }

    /** Gets the single instance of Proxy *
     *
     * @param theIndex the index of the instance needs to be loaded.
     * @param theType the type of  proxy instance, pass null to get default type.
     * @return single instance of StrctProxyGenerator */
    @SuppressWarnings({ "unchecked", "null" })
    public <T> T getInstance(final int theIndex,
            @Nullable final Class<T> theType) {
        checkArgument(theIndex >= 0);
        checkArgument(theIndex < storage.getCapacity(),
                "Storage capacity is less then the element index passed!");
        storage.selectStructure(theIndex);

        final Class<?> requestedType = theType != null ? theType : defaultType;
        final int index = storage.read(indexField);
        if (index == 0) {
            selectType(requestedType);
        } else if (index != indexMap.get(requestedType).struct.childIndex()) {
            throw new ClassCastException("Current type "
                    + interfaces[index - 1] + " cannot be type cast to "
                    + requestedType.getName());
        }
        return (T) indexMap.get(requestedType).instance;
    }

    /** Returns a new instance of this proxy cursor *without* the existing data. */
    @SuppressWarnings("null")
    public final ProxyCursor reCompile(final int theCapacity,
            final Compiler theCompiler) {
        checkArgument(theCapacity > 0);
        checkNotNull(theCompiler);
        return new ProxyCursor(interfaces, defaultType, theCompiler,
                theCapacity);
    }

    /** Resize the underlying storage.
     *
     * @param theNewCapacity the new capacity */
    public void resize(final int theNewCapacity) {
        checkArgument(theNewCapacity > 0);
        storage.resizeStorage(theNewCapacity);
    }

    /** Root Struct. */
    public Struct rootStruct() {
        return storage.rootStruct();
    }

    /** Selects the class type, this method is used when the proxy is generated
     * from multiple interfaces, to switch from one interface to another
     *
     * @param theIndex the index
     * @param theType the type of proxy instance pass null to get the default type.*/
    @SuppressWarnings("null")
    public void selectType(final int theIndex, @Nullable final Class<?> theType) {
        checkArgument(theIndex >= 0);
        storage.selectStructure(theIndex);
        if (theType != null) {
            selectType(theType);
        } else {
            selectType(defaultType);
        }
    }

    /**
     * The underlying storage are transactional, which means changes can be
     * committed/rolled-back as needed, This method returns the underlying
     * Transaction manager,
     *
     * @return the transaction manager
     */
    public TransactionManager transactionManager() {
        return storage.transactionManager();
    }

    /**
     * Transactions enabled ?
     *
     * @return true, if enabled
     * @see Storage#transactionsEnabled()
     */
    public boolean transactionsEnabled() {
        return storage.transactionsEnabled();
    }
}
