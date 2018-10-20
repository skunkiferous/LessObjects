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
package com.blockwithme.lessobjects.schema;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldCategory;
import com.blockwithme.lessobjects.FieldType;
import com.blockwithme.lessobjects.schema.StructSchema.JSONViews;
import com.blockwithme.lessobjects.util.FieldBuilder;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.ConfiguredConverter;
import com.blockwithme.prim.Converter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * An internal class used at the time of conversion to JSON/XML format.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
@JsonPropertyOrder(alphabetic = true)
public class FieldBinding implements Comparable<FieldBinding> {

    /** The size in bits. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private int bits;

    /** The converter class name. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    // TODO Not safe to use simple class names in OSGi
    private String converterClassName;

    /** The converter configuration, if any. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private String converterConfig;

    /** The mapper class name. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    // TODO Not safe to use simple class names in OSGi
    private String mapperClassName;

    /** The name. */
    @JsonProperty
    @JsonView({ JSONViews.SignatureView.class, JSONViews.CompleteView.class })
    private String name;

    /** The optional flag. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private boolean optional;

    /** The type. */
    @JsonProperty
    @JsonView({ JSONViews.SignatureView.class, JSONViews.CompleteView.class })
    private String type;

    /** Instantiates a Field Binding */
    public FieldBinding() {
        // NOP
    }

    /** Instantiates a Field Binding */
    public FieldBinding(final Field<?, ?> theField) {
        bits = theField.bits();
        converterClassName = theField.converterClassName();
        final Converter<?> converter = theField.converter();
        if (converter instanceof ConfiguredConverter) {
            converterConfig = ((ConfiguredConverter<?>) converter)
                    .getConfiguration();
        }
        mapperClassName = theField.mapperClassName();
        name = theField.name();
        optional = theField.isOptional();
        type = Util.getClassName(theField.type());
    }

    /** Creates a Filed from a given category. */
    @SuppressWarnings("null")
    Field<?, ?> create(final FieldCategory theCategory) {
        try {
            final FieldBuilder builder = new FieldBuilder();
            builder.setFieldName(name);
            builder.setFieldCategory(theCategory);
            builder.setBits(bits);

            final FieldType fType = FieldType.from(type);
            Objects.requireNonNull(converterClassName, "converter class name");
            Converter<?> converter = Converter.DEFAULTS.get(converterClassName);
            if (converter == null) {
                final Class<?> converterClass = Util
                        .classForName(converterClassName);
                Objects.requireNonNull(converterClass, converterClassName
                        + " is not a valid converter class name");
                if (ConfiguredConverter.class.isAssignableFrom(converterClass)) {
                    Objects.requireNonNull(
                            converterConfig,
                            converterClassName
                                    + " is a ConfiguredConverter without converterConfig");
                    try {
                        final Constructor<?> ctr = converterClass
                                .getConstructor(String.class);
                        converter = (Converter<?>) ctr
                                .newInstance(converterConfig);
                    } catch (NoSuchMethodException | InstantiationException
                            | IllegalAccessException | SecurityException
                            | InvocationTargetException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                } else {
                    try {
                        converter = (Converter<?>) converterClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
            builder.setConverter(converter);
            if (mapperClassName != null) {
                final Class<?> mapperClass = Util.classForName(mapperClassName);
                final Object mapper = mapperClass.newInstance();
                builder.setMapper(mapper);
            }
            builder.setOptional(optional);
            return fType.createField(builder);
        } catch (final ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(@Nullable final FieldBinding theOther) {
        if (theOther != null) {
            return name.compareTo(theOther.name);
        }
        return 1;
    }

    /** The size. */
    public int getBits() {
        return bits;
    }

    /**  The Field Converter Class Name, If the field has a converter. */
    @SuppressWarnings("null")
    public String getConverterClassName() {
        return converterClassName;
    }

    /** The converterConfig */
    @SuppressWarnings("null")
    public String getConverterConfig() {
        return converterConfig;
    }

    /**  The Field Mapper Class Name, If the field has a mapper. */
    @SuppressWarnings("null")
    public String getMapperClassName() {
        return mapperClassName;
    }

    /** The name */
    @SuppressWarnings("null")
    public String getName() {
        return name;
    }

    /** The Field type (Class name as a string)*/
    @SuppressWarnings("null")
    public String getType() {
        return type;
    }

    /** Is an optional field ? */
    public boolean isOptional() {
        return optional;
    }

    /** The size in bits */
    public void setBits(final int theBits) {
        bits = theBits;
    }

    /**  The Field Converter Class Name, If the field has a converter. */
    public void setConverterClassName(final String theConverterClassName) {
        converterClassName = theConverterClassName;
    }

    /** The converterConfig */
    public void setConverterConfig(final String theConverterConfig) {
        converterConfig = theConverterConfig;
    }

    /**  The Field Mapper Class Name, If the field has a mapper. */
    public void setMapperClassName(final String theMapperClassName) {
        mapperClassName = theMapperClassName;
    }

    /** The name */
    public void setName(final String theName) {
        name = theName;
    }

    /** Is an Optional Field  ? */
    public void setOptional(final boolean isOptional) {
        optional = isOptional;
    }

    /** The Field type (Class name as a string)*/
    public void setType(final String theType) {
        type = theType;
    }
}
