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
// $codepro.audit.disable largeNumberOfFields
package com.blockwithme.lessobjects.proxy;

import static com.blockwithme.lessobjects.util.StructConstants.ITERATOR_CLASS_NAME;
import static com.blockwithme.lessobjects.util.StructConstants.STRING_CLASS_NAME;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javassist.CtClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.ChildProperties;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.object.StringPseudoConverter;
import com.blockwithme.prim.Converter;
import com.blockwithme.prim.ConverterRegistry;
import com.blockwithme.prim.LongConverter;

/**
 * The FieldInfo class wraps information about a particular field in the parsed
 * interface, an internal wrapper object used only during proxy generation.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class FieldInfo {

    /** The array size. */
    final int arraySize;

    /** The child props. */
    @Nonnull
    final ChildProperties childProps;

    /** The field props. */
    @Nonnull
    final FieldProperties fieldProps;

    /** The getter method name */
    final String getterMethod;

    /** if field is accessed by a getter method in the interface */
    final boolean hasGetter;

    /** if field is accessed by a setter method in the interface */
    final boolean hasSetter;

    /** The is inherited. */
    final boolean inherited;

    /** The interface name. */
    @Nonnull
    final String interfaceName;

    /** Indicates if the Field is a child type array. */
    boolean isChildArray;

    /** The is list. */
    boolean isList;

    /** Indicates if the Field is an object convertible to byte[] */
    boolean isObjectField;

    /** Indicates if the Field is a primitive type array. */
    boolean isPrimitiveArray;

    /** The list interface. */
    @Nullable
    String listInterface;

    /** The field name. */
    @Nonnull
    final String name;

    /** The object to byte converter. */
    @SuppressWarnings("rawtypes")
    final Class<? extends Converter<?>> objectToByteConverter;

    /** The object to byte converter. */
    @SuppressWarnings("rawtypes")
    final Class<? extends LongConverter> objectConverter;

    /** The registry. */
    final ConverterRegistry registry;

    /** The setter method name */
    final String setterMethod;

    /** The field name. */
    final String simpleName;

    /** The field class type can be primitive or child interfaces. */
    final CtClass type;

    /** The field class type can be primitive or child interfaces. */
    final Class<?> typeClass;

    /** Package private constructor, class should be accessed within the package. */
    @SuppressWarnings("rawtypes")
    public FieldInfo(final FieldInfoBuilder theBuilder) {
        try {
            childProps = theBuilder.getChildProps();
            fieldProps = theBuilder.getFieldProps();
            getterMethod = theBuilder.getGetterMethod();
            hasGetter = theBuilder.hasGetter();
            hasSetter = theBuilder.hasSetter();
            inherited = theBuilder.isInherited();
            interfaceName = theBuilder.getInterfaceName();
            name = theBuilder.getName();
            objectToByteConverter = theBuilder.getObjectToByteConverter();
            objectConverter = theBuilder.getObjectToLongConverter();
            registry = theBuilder.getRegistry();
            setterMethod = theBuilder.getSetterMethod();
            simpleName = theBuilder.getSimpleName();
            type = theBuilder.getType();
            if (!type.isPrimitive() && !type.isArray()) {
                // No choice to use Class.forName() instead of
                // Util.classForName(),
                // because we get a Javassist name
                typeClass = Class.forName(type.getName());
            } else if (type.isArray() && !type.getComponentType().isPrimitive()) {
                // No choice to use Class.forName() instead of
                // Util.classForName(),
                // because we get a Javassist name
                typeClass = Class.forName(type.getComponentType().getName());
            } else {
                typeClass = null;
            }

            isObjectField = objectToByteConverter != null
                    && objectToByteConverter != StringPseudoConverter.class; // $codepro.audit.disable
                                                                             // useEquals
            if (type.isPrimitive()) {
                isPrimitiveArray = false;
                isChildArray = false;
            } else if (type.isArray()) {
                if (type.getComponentType().isPrimitive()) {
                    isPrimitiveArray = true;
                    isChildArray = false;
                } else if (STRING_CLASS_NAME.equals(type.getComponentType()
                        .getName())) {
                    isPrimitiveArray = true;
                    isChildArray = false;
                } else if (objectToByteConverter != null) {
                    isPrimitiveArray = true;
                    isChildArray = false;
                } else if (objectConverter != null) {
                    isPrimitiveArray = true;
                    isChildArray = false;
                } else if (registry != null && registry.find(typeClass) != null) {
                    isChildArray = false;
                    isPrimitiveArray = false;
                } else if (type.getComponentType().isInterface()) {
                    isChildArray = true;
                    isPrimitiveArray = false;
                } else {
                    throw new IllegalArgumentException(
                            "Object type Arrays should have object to long/byte[] converters, field name: "
                                    + name + " interface name: "
                                    + interfaceName);
                }
                if (theBuilder.getArraySize() <= 0) {
                    throw new IllegalArgumentException(
                            "Array types should have appropriate array size, field name: "
                                    + name + " interface name: "
                                    + interfaceName);
                }

            } else {
                isPrimitiveArray = false;
                isChildArray = false;
            }

            if (!isPrimitiveArray && !isChildArray) {
                arraySize = 0;
            } else {
                arraySize = theBuilder.getArraySize();
            }

            // Using reflection to get the Iterator generic type.
            if (ITERATOR_CLASS_NAME.equals(type.getName())) {
                final Class<?> c = Class.forName(interfaceName);
                final Method method = c.getMethod(getterMethod,
                        new Class<?>[] { int.class });
                final Type genericType = method.getGenericReturnType();
                final ParameterizedType pt = (ParameterizedType) genericType;
                final Type t = pt.getActualTypeArguments()[0];
                final String tName = t.toString();
                if (tName.startsWith("interface")) {
                    listInterface = tName.substring("interface".length())
                            .trim();
                } else {
                    listInterface = tName;
                }
                isList = true;
            } else {
                listInterface = null;
                isList = false;
            }
        } catch (final Exception exp) {
            throw new IllegalStateException(exp.getMessage(), exp);
        }
    }

    @SuppressWarnings("null")
    private FieldInfoBuilder getBuilder() {
        return new FieldInfoBuilder().setArraySize(arraySize)
                .setChildProps(childProps).setFieldProps(fieldProps)
                .setGetterMethod(getterMethod).setSetterMethod(setterMethod)
                .setHasSetter(hasSetter).setHasGetter(hasGetter)
                .setInherited(inherited).setInterfaceName(interfaceName)
                .setName(name).setSimpleName(simpleName)
                .setObjectToByteConverter(objectToByteConverter)
                .setObjectToLongConverter(objectConverter)
                .setRegistry(registry).setType(type);
    }

    /**
     * @return the arraySize
     */
    public int arraySize() {
        return arraySize;
    }

    /**
     * @return the childProps
     */
    @SuppressWarnings("null")
    public ChildProperties childProps() {
        return childProps;
    }

    /**
     * @return the fieldProps
     */
    @SuppressWarnings("null")
    public FieldProperties fieldProps() {
        return fieldProps;
    }

    /**
     * @return the isChildArray
     */
    public boolean isChildArray() {
        return isChildArray;
    }

    /**
     * @return the isList
     */
    public boolean isList() {
        return isList;
    }

    /**
     * @return the name
     */
    @SuppressWarnings("null")
    public String name() {
        return name;
    }

    /**
     * @return the objectToByteConverter
     */
    @SuppressWarnings({ "null", "rawtypes" })
    public Class<? extends Converter<?>> objectToByteConverter() {
        return objectToByteConverter;
    }

    /**
     * @return the objectToLongConverter
     */
    @SuppressWarnings({ "null", "rawtypes" })
    public Class<? extends LongConverter> objectToLongConverter() {
        return objectConverter;
    }

    @SuppressWarnings("null")
    public FieldInfo setChildProps(final ChildProperties theChildProps) {
        return new FieldInfo(getBuilder().setChildProps(theChildProps));
    }

    @SuppressWarnings("null")
    public FieldInfo setFieldProps(final FieldProperties theFieldProps) {
        return new FieldInfo(getBuilder().setFieldProps(theFieldProps));
    }

    @SuppressWarnings("null")
    public FieldInfo setInherited(final boolean isInherited) {
        return new FieldInfo(getBuilder().setInherited(isInherited));
    }

    @SuppressWarnings("null")
    public FieldInfo setName(final String theName) {
        return new FieldInfo(getBuilder().setName(theName));
    }

    @Override
    public String toString() {
        return "FieldInfo [arraySize=" + arraySize + ", childProps="
                + childProps + ", fieldProps=" + fieldProps + ", getterMethod="
                + getterMethod + ", hasGetter=" + hasGetter + ", hasSetter="
                + hasSetter + ", inherited=" + inherited + ", interfaceName="
                + interfaceName + ", isChildArray=" + isChildArray
                + ", isList=" + isList + ", isObjectField=" + isObjectField
                + ", isPrimitiveArray=" + isPrimitiveArray + ", listInterface="
                + listInterface + ", name=" + name + ", objectToByteConverter="
                + objectToByteConverter + ", objectToLongConverter="
                + objectConverter + ", registry=" + registry
                + ", setterMethod=" + setterMethod + ", simpleName="
                + simpleName + ", type=" + type + ", typeClass=" + typeClass
                + "]";
    }

    /**
     * @return the type
     */
    @SuppressWarnings("null")
    public CtClass type() {
        return type;
    }
}
