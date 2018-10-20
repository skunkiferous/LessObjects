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
/**
 *
 */
package com.blockwithme.lessobjects;

import static com.blockwithme.lessobjects.util.StructConstants.FACTORY;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.virtual.BooleanFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ByteFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.CharFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.DoubleFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.FloatFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.IntFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.LongFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ShortFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ObjectFieldMapper;
import com.blockwithme.lessobjects.util.FieldBuilder;
import com.blockwithme.prim.BooleanConverter;
import com.blockwithme.prim.ByteConverter;
import com.blockwithme.prim.CharConverter;
import com.blockwithme.prim.Converter;
import com.blockwithme.prim.DoubleConverter;
import com.blockwithme.prim.FloatConverter;
import com.blockwithme.prim.IntConverter;
import com.blockwithme.prim.LongConverter;
import com.blockwithme.prim.ShortConverter;

/**
 * The FieldType ENUM
 *
 * @author tarung
 */
public enum FieldType {

    /** The boolean. */
    BOOLEAN {

        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final String name = theBuilder.getFieldName();
            final BooleanConverter<?> converter = (BooleanConverter<?>) theBuilder
                    .getConverter();
            Objects.requireNonNull(converter, "converter of " + name
                    + " is null");
            Field<?, ?> field;
            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newBooleanGlobalField(converter, name);
                break;

            case NORMAL:
                field = FACTORY.newBooleanField(converter, name);
                break;

            case OPTIONAL:
                field = FACTORY.newBooleanOptional(converter, name);
                break;

            case VIRTUAL:
                final BooleanFieldMapper mapper = (BooleanFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newBooleanVirtualField(converter, name, mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }

            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The byte type. */
    BYTE {

        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final String name = theBuilder.getFieldName();
            final ByteConverter<?> converter = (ByteConverter<?>) theBuilder
                    .getConverter();
            final int bits = theBuilder.getBits();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newByteGlobalField(converter, name, bits);
                break;

            case NORMAL:
                field = FACTORY.newByteField(converter, name, bits);
                break;

            case OPTIONAL:
                field = FACTORY.newByteOptional(converter, name, bits);
                break;

            case VIRTUAL:
                final ByteFieldMapper mapper = (ByteFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newByteVirtualField(converter, name, bits,
                        mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }
            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The char type. */
    CHAR {

        @SuppressWarnings({ "boxing", "null" })
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final String name = theBuilder.getFieldName();
            final CharConverter<?> converter = (CharConverter<?>) theBuilder
                    .getConverter();
            final int bits = theBuilder.getBits();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newCharGlobalField(converter, name, bits);
                break;

            case NORMAL:
                field = FACTORY.newCharField(converter, name, bits);
                break;

            case OPTIONAL:
                field = FACTORY.newCharOptional(converter, name, bits);
                break;

            case VIRTUAL:
                final CharFieldMapper mapper = (CharFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newCharVirtualField(converter, name, bits,
                        mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }
            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The double type. */
    DOUBLE {

        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final String name = theBuilder.getFieldName();
            final DoubleConverter<?> converter = (DoubleConverter<?>) theBuilder
                    .getConverter();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newDoubleGlobalField(converter, name);
                break;

            case NORMAL:
                field = FACTORY.newDoubleField(converter, name);
                break;

            case OPTIONAL:
                field = FACTORY.newDoubleOptional(converter, name);
                break;

            case VIRTUAL:
                final DoubleFieldMapper mapper = (DoubleFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newDoubleVirtualField(converter, name, mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }

            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The float type. */
    FLOAT {
        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final String name = theBuilder.getFieldName();
            final FloatConverter<?> converter = (FloatConverter<?>) theBuilder
                    .getConverter();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newFloatGlobalField(converter, name);
                break;

            case NORMAL:
                field = FACTORY.newFloatField(converter, name);
                break;

            case OPTIONAL:
                field = FACTORY.newFloatOptional(converter, name);
                break;

            case VIRTUAL:
                final FloatFieldMapper mapper = (FloatFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newFloatVirtualField(converter, name, mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }
            return updateCompiledInfo(field, theBuilder);

        }
    },

    /** The int type. */
    INT {
        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final int bits = theBuilder.getBits();
            final String name = theBuilder.getFieldName();
            final IntConverter<?> converter = (IntConverter<?>) theBuilder
                    .getConverter();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newIntGlobalField(converter, name, bits);
                break;

            case NORMAL:
                field = FACTORY.newIntField(converter, name, bits);
                break;

            case OPTIONAL:
                field = FACTORY.newIntOptional(converter, name, bits);
                break;

            case VIRTUAL:
                final IntFieldMapper mapper = (IntFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newIntVirtualField(converter, name, bits,
                        mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }
            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The long type. */
    LONG {
        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final int bits = theBuilder.getBits();
            final String name = theBuilder.getFieldName();
            final LongConverter<?> converter = (LongConverter<?>) theBuilder
                    .getConverter();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newLongGlobalField(converter, name, bits);
                break;

            case NORMAL:
                field = FACTORY.newLongField(converter, name, bits);
                break;

            case OPTIONAL:
                field = FACTORY.newLongOptional(converter, name, bits);
                break;

            case VIRTUAL:
                final LongFieldMapper mapper = (LongFieldMapper) theBuilder
                        .getMapper();
                field = FACTORY.newLongVirtualField(converter, name, bits,
                        mapper);
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }
            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The short type. */
    SHORT {
        @SuppressWarnings("null")
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final String fieldName = theBuilder.getFieldName();
            final int bits = theBuilder.getBits();
            final ShortConverter<?> converter = (ShortConverter<?>) theBuilder
                    .getConverter();
            Field<?, ?> field;

            switch (theBuilder.getFieldCategory()) {

            case GLOBAL:
                field = FACTORY.newShortGlobalField(converter, fieldName, bits);
                break;

            case NORMAL:
                field = FACTORY.newShortField(converter, fieldName, bits);
                break;

            case OPTIONAL:
                field = FACTORY.newShortOptional(converter, fieldName, bits);
                break;

            case VIRTUAL:
                field = FACTORY.newShortVirtualField(converter, fieldName,
                        bits, (ShortFieldMapper) theBuilder.getMapper());
                break;

            default:
                throw new IllegalStateException("Unkonwn type!");
            }
            return updateCompiledInfo(field, theBuilder);
        }
    },

    /** The object. */
    OBJECT {

        @SuppressWarnings({ "null", "unchecked", "boxing", "rawtypes" })
        @Override
        public Field<?, ?> createField(final FieldBuilder theBuilder) {
            final Converter<?> converter = theBuilder.getConverter();

            final String name = theBuilder.getFieldName();
            final boolean optional = theBuilder.isOptional();
            final boolean global = theBuilder.getFieldCategory().equals(
                    FieldCategory.GLOBAL);
            final boolean virtual = theBuilder.getFieldCategory().equals(
                    FieldCategory.VIRTUAL);
            if (optional) {
                return FACTORY.newObjectOptional(converter, name);
            }
            if (global) {
                return FACTORY.newObjectGlobalField(converter, name);
            }
            if (virtual) {
                return FACTORY.newObjectVirtualField(converter, name,
                        (ObjectFieldMapper) theBuilder.getMapper());
            }

            return FACTORY.newObjectField(converter, name);
        }
    };

    /** The type map. */
    private static Map<String, FieldType> TYPE_MAP = new HashMap<>();

    /** Helper */
    private static String classToString(final Class<?> theClazz) {
        return theClazz.getName().toUpperCase();
    }

    /** If the field is a compiled field this method updates compiled info. */
    private static Field<?, ?> updateCompiledInfo(final Field<?, ?> theField,
            final FieldBuilder theFieldBuilder) {
        if (theFieldBuilder.isCompiled()) {
            final FieldProperties properties = theField.properties()
                    .setOffset(theFieldBuilder.getOffset())
                    .setIndex(theFieldBuilder.getIndex());
            return theField.copy(properties);
        }
        return theField;
    }

    /** Gets type from class name string.
    *
    * @param theClass the class instance
    * @return the field type
    *  */
    public static FieldType from(final Class<?> theClass) {
        return from(theClass.getName());
    }

    /** Gets type from class name string.
     *
     * @param theClass the fully qualified class name
     * @return the field type */
    public static FieldType from(final String theClass) {

        if (TYPE_MAP.size() == 0) {

            TYPE_MAP.put(classToString(Boolean.class), BOOLEAN);
            TYPE_MAP.put(classToString(boolean.class), BOOLEAN);

            TYPE_MAP.put(classToString(Byte.class), BYTE);
            TYPE_MAP.put(classToString(byte.class), BYTE);

            TYPE_MAP.put(classToString(Character.class), CHAR);
            TYPE_MAP.put(classToString(char.class), CHAR);

            TYPE_MAP.put(classToString(Double.class), DOUBLE);
            TYPE_MAP.put(classToString(double.class), DOUBLE);

            TYPE_MAP.put(classToString(Float.class), FLOAT);
            TYPE_MAP.put(classToString(float.class), FLOAT);

            TYPE_MAP.put(classToString(Integer.class), INT);
            TYPE_MAP.put(classToString(int.class), INT);

            TYPE_MAP.put(classToString(Long.class), LONG);
            TYPE_MAP.put(classToString(long.class), LONG);

            TYPE_MAP.put(classToString(Short.class), SHORT);
            TYPE_MAP.put(classToString(short.class), SHORT);

        }

        final FieldType key = TYPE_MAP.get(theClass.toUpperCase());
        // TODO Could/Should we check the converter registry as well?
        if (key == null) {
            return OBJECT;
        }

        return key;

    }

    /** Gets the type based on Class.
    *
    * @param theClass the class
    * @return the type */
    public static FieldType getType(final Class<?> theClass) {

        if (theClass.equals(Boolean.class) || theClass.equals(Boolean.TYPE)) {
            return BOOLEAN;
        }
        if (theClass.equals(Byte.class) || theClass.equals(Byte.TYPE)) {
            return BYTE;
        }
        if (theClass.equals(Character.class) || theClass.equals(Character.TYPE)) {
            return CHAR;
        }
        if (theClass.equals(Double.class) || theClass.equals(Double.TYPE)) {
            return DOUBLE;
        }
        if (theClass.equals(Float.class) || theClass.equals(Float.TYPE)) {
            return FLOAT;
        }
        if (theClass.equals(Integer.class) || theClass.equals(Integer.TYPE)) {
            return INT;
        }
        if (theClass.equals(Long.class) || theClass.equals(Long.TYPE)) {
            return LONG;
        }
        if (theClass.equals(Short.class) || theClass.equals(Short.TYPE)) {
            return SHORT;
        }
        return OBJECT;
    }

    /** Creates a Field instance using a FieldBuilder. */
    public abstract Field<?, ?> createField(FieldBuilder theBuilder);

}
