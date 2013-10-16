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

import javassist.CtClass;

import com.blockwithme.lessobjects.beans.ChildProperties;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.prim.Converter;
import com.blockwithme.prim.ConverterRegistry;
import com.blockwithme.prim.LongConverter;

/**
 * The Field Info Builder Object
 *
 * @author tarung
 */
public class FieldInfoBuilder {

    /** The array size. */
    private int arraySize;

    /** The child props. */
    private ChildProperties childProps;

    /** The field props. */
    private FieldProperties fieldProps;

    /** The getter method name */
    private String getterMethod;

    /** if field is accessed by a getter method in the interface */
    private boolean hasGetter;

    /** if field is accessed by a setter method in the interface */
    private boolean hasSetter;

    /** The is inherited flag. */
    private boolean inherited;

    /** The interface name. */
    private String interfaceName;

    /** The field name. */
    private String name;

    /** The object to byte converter. */
    @SuppressWarnings("rawtypes")
    private Class<? extends Converter<?>> objectToByteConverter;

    /** The object to byte converter. */
    @SuppressWarnings("rawtypes")
    private Class<? extends LongConverter> objectToLongConverter;

    /** The registry. */
    private ConverterRegistry registry;

    /** The setter method name */
    private String setterMethod;

    /** The field name. */
    private String simpleName;

    /** The field class type can be primitive or child interfaces. */
    private CtClass type;

    /**
     * @return the arraySize
     */
    public int getArraySize() {
        return arraySize;
    }

    /**
     * @return the childProps
     */
    public ChildProperties getChildProps() {
        return childProps;
    }

    /**
     * @return the fieldProps
     */
    public FieldProperties getFieldProps() {
        return fieldProps;
    }

    /**
     * @return the getterMethod
     */
    public String getGetterMethod() {
        return getterMethod;
    }

    /**
     * @return the interfaceName
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the objectToByteConverter
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends Converter<?>> getObjectToByteConverter() {
        return objectToByteConverter;
    }

    /**
     * @return the objectToLongConverter
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends LongConverter> getObjectToLongConverter() {
        return objectToLongConverter;
    }

    /**
     * @return the registry
     */
    public ConverterRegistry getRegistry() {
        return registry;
    }

    /**
     * @return the setterMethod
     */
    public String getSetterMethod() {
        return setterMethod;
    }

    /**
     * @return the simpleName
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * @return the type
     */
    public CtClass getType() {
        return type;
    }

    /**
     * @return the hasGetter
     */
    public boolean hasGetter() {
        return hasGetter;
    }

    /**
     * @return the hasSetter
     */
    public boolean hasSetter() {
        return hasSetter;
    }

    /**
     * @return the inherited
     */
    public boolean isInherited() {
        return inherited;
    }

    /**
     * @return the hasSetter
     */
    public boolean isSetter() {
        return hasSetter();
    }

    /**
     * @param theArraySize the arraySize to set
     */
    public FieldInfoBuilder setArraySize(final int theArraySize) {
        arraySize = theArraySize;
        return this;
    }

    /**
     * @param theChildProps the childProps to set
     */
    public FieldInfoBuilder setChildProps(final ChildProperties theChildProps) {
        childProps = theChildProps;
        return this;
    }

    /**
     * @param theFieldProps the fieldProps to set
     */
    public FieldInfoBuilder setFieldProps(final FieldProperties theFieldProps) {
        fieldProps = theFieldProps;
        return this;
    }

    /**
     * @param theGetterMethod the getterMethod to set
     */
    public FieldInfoBuilder setGetterMethod(final String theGetterMethod) {
        getterMethod = theGetterMethod;
        return this;
    }

    /**
     * @param isGetter the hasGetter to set
     */
    public FieldInfoBuilder setHasGetter(final boolean isGetter) {
        hasGetter = isGetter;
        return this;
    }

    /**
     * @param isSetter the hasSetter to set
     */
    public FieldInfoBuilder setHasSetter(final boolean isSetter) {
        hasSetter = isSetter;
        return this;
    }

    /**
     * @param isInherited the inherited to set
     */
    public FieldInfoBuilder setInherited(final boolean isInherited) {
        inherited = isInherited;
        return this;
    }

    /**
     * @param theInterfaceName the interfaceName to set
     */
    public FieldInfoBuilder setInterfaceName(final String theInterfaceName) {
        interfaceName = theInterfaceName;
        return this;
    }

    /**
     * @param theName the name to set
     */
    public FieldInfoBuilder setName(final String theName) {
        name = theName;
        return this;
    }

    /**
     * @param theObjectToByteConverter the objectToByteConverter to set
     */
    @SuppressWarnings("rawtypes")
    public FieldInfoBuilder setObjectToByteConverter(
            final Class<? extends Converter<?>> theObjectToByteConverter) {
        objectToByteConverter = theObjectToByteConverter;
        return this;
    }

    /**
     * @param theObjectToLongConverter the objectToLongConverter to set
     */
    @SuppressWarnings("rawtypes")
    public FieldInfoBuilder setObjectToLongConverter(
            final Class<? extends LongConverter> theObjectToLongConverter) {
        objectToLongConverter = theObjectToLongConverter;
        return this;
    }

    /**
     * @param theRegistry the registry to set
     */
    public FieldInfoBuilder setRegistry(final ConverterRegistry theRegistry) {
        registry = theRegistry;
        return this;
    }

    /**
     * @param theSetterMethod the setterMethod to set
     */
    public FieldInfoBuilder setSetterMethod(final String theSetterMethod) {
        setterMethod = theSetterMethod;
        return this;
    }

    /**
     * @param theSimpleName the simpleName to set
     */
    public FieldInfoBuilder setSimpleName(final String theSimpleName) {
        simpleName = theSimpleName;
        return this;
    }

    /**
     * @param theType the type to set
     */
    public FieldInfoBuilder setType(final CtClass theType) {
        type = theType;
        return this;
    }

}
