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
package com.blockwithme.lessobjects.storage.serialization;

import static com.blockwithme.lessobjects.FieldCategory.VIRTUAL;
import static com.blockwithme.lessobjects.FieldType.OBJECT;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldCategory;
import com.blockwithme.lessobjects.FieldType;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.virtual.VirtualField;
import com.blockwithme.lessobjects.util.FieldBuilder;
import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.ObjectUnpacker;
import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.Unpacker;
import com.blockwithme.msgpack.templates.AbstractTemplate;
import com.blockwithme.msgpack.templates.ObjectType;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.TrackingType;
import com.blockwithme.msgpack.templates.UnpackerContext;
import com.blockwithme.prim.ConfiguredConverter;
import com.blockwithme.prim.Converter;

/**
 * The class StructTemplate serializes a Struct.
 *
 * @author tarung
 */
public class StructTemplate extends AbstractTemplate<Struct> {

    /** The all field categories. */
    private static FieldCategory[] ALL_FIELD_CATEGORIES = FieldCategory
            .values();

    /** The all field types. */
    private static FieldType[] ALL_FIELD_TYPES = FieldType.values();

    /** Instantiates a new struct template. */
    protected StructTemplate() {
        super(null, Struct.class, 1, ObjectType.ARRAY, TrackingType.EQUALITY,
                -1);
    }

    /** {@inheritDoc} */
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final Struct theStruct) {

        // Struct-Name, Struct-IsUnion, Struct-IsOptional, Struct-IsList,
        // Struct-Field-Count
        int count = 7;
        for (final Field<?, ?> f : theStruct.fields()) {
            // Field-Name, Field-Type, Field-Category, Field-Converter
            count += 4;
            final Converter<?> converter = f.converter();
            if (converter instanceof ConfiguredConverter) {
                // Converter configuration String
                count++;
            }
            if (f.fieldType() != OBJECT && f.fieldCategory() != VIRTUAL) {
                // Field-Bits, Field-Compiled
                count += 2;
                if (f.compiled()) {
                    // Field-Index, Field-Offset
                    count += 2;
                }
            } else if (f.fieldCategory() == VIRTUAL) {
                // Field-Converter-Class
                count++;
            }
        }
        for (final Struct child : theStruct.children()) {
            count = count + getSpaceRequired(theContext, child);
        }
        if (theStruct.bits() > 0) {
            count++;
        }
        return count;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Struct readData(final UnpackerContext theContext,
            final Struct thePreCreated, final int theSize) throws IOException {

        // Struct is immutable so 'thePreCreated' will be null.
        final Unpacker u = theContext.unpacker;
        final ObjectUnpacker objUnpacker = theContext.objectUnpacker;
        final String structName = objUnpacker.readString();

        final boolean isUnion = u.readBoolean();
        final boolean isOptional = u.readBoolean();
        final boolean isList = u.readBoolean();
        final int fieldArrayLength = u.readInt();

        final Field<?, ?>[] fields = new Field<?, ?>[fieldArrayLength];
        final FieldBuilder builder = new FieldBuilder();

        for (int i = 0; i < fields.length; i++) {

            final String fName = objUnpacker.readString();
            final byte fieldType = u.readByte();
            final byte fieldCategory = u.readByte();

            final FieldType ftype = ALL_FIELD_TYPES[fieldType];
            final FieldCategory fCategory = ALL_FIELD_CATEGORIES[fieldCategory];

            builder.setFieldName(fName);
            builder.setFieldType(ftype);
            builder.setFieldCategory(fCategory);

            final Class<?> converterClass = objUnpacker.readClass();
            Converter<?> converter = Converter.DEFAULTS.get(converterClass
                    .getName());

            if (converter == null) {
                if (ConfiguredConverter.class.isAssignableFrom(converterClass)) {
                    try {
                        final String config = objUnpacker.readString();
                        final Constructor<?> ctr = converterClass
                                .getConstructor(String.class);
                        converter = (Converter<?>) ctr.newInstance(config);
                    } catch (NoSuchMethodException | InstantiationException
                            | IllegalAccessException | SecurityException
                            | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new IOException(e.getMessage(), e);
                    }
                } else {
                    try {
                        converter = (Converter<?>) converterClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new IOException(e.getMessage(), e);
                    }
                }
            }
            builder.setConverter(converter);

            if (ftype != OBJECT && fCategory != VIRTUAL) {

                builder.setBits(u.readByte());
                final boolean compiled = u.readBoolean();
                builder.setCompiled(compiled);
                if (compiled) {
                    builder.setIndex(u.readInt());
                    builder.setOffset(u.readInt());
                }
            } else if (fCategory == VIRTUAL) {
                final Class<?> mapperClass = objUnpacker.readClass();
                try {
                    builder.setMapper(mapperClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IOException(e.getMessage(), e);
                }
                builder.setBits(0);
            }
            fields[i] = ftype.createField(builder);
        }

        final boolean isCompiled = u.readBoolean();
        final int size = isCompiled ? u.readInt() : -1;

        final int childrenCount = u.readInt();
        final Struct[] children = new Struct[childrenCount];
        if (childrenCount > 0) {
            for (int i = 0; i < childrenCount; i++) {
                children[i] = readData(theContext, null, -1);
            }
        }

        Struct result = new Struct(structName, isUnion, children, fields);
        if (isList) {
            result = result.setList(true);
        }
        if (isOptional) {
            result = result.setOptional(isOptional);
        }
        if (isCompiled) {
            result = result.copy(result.childProperties().setBits(size), false);
        }
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final Struct theStruct) throws IOException {
        final Packer p = theContext.packer;
        final ObjectPacker objPacker = theContext.objectPacker;
        final Field<?, ?>[] allFields = theStruct.fields();

        objPacker.writeObject(theStruct.name());

        p.writeBoolean(theStruct.union());
        p.writeBoolean(theStruct.isOptional());
        p.writeBoolean(theStruct.list());
        p.writeInt(allFields.length);

        for (final Field<?, ?> f : allFields) {
            final FieldType fType = f.fieldType();
            final FieldCategory fCategory = f.fieldCategory();

            // TODO ordinal() is evil, and should never be used for
            // serialization
            final byte fieldType = (byte) fType.ordinal();
            // TODO ordinal() is evil, and should never be used for
            // serialization
            final byte fieldCategory = (byte) fCategory.ordinal();

            final boolean compiled = f.compiled();

            objPacker.writeObject(f.name());
            p.writeByte(fieldType);
            p.writeByte(fieldCategory);
            final Converter<?> fieldConverter = f.converter();
            objPacker.writeObject(fieldConverter.getClass());
            if (fieldConverter instanceof ConfiguredConverter) {
                objPacker.writeObject(((ConfiguredConverter<?>) fieldConverter)
                        .getConfiguration());
            }

            if (fType != OBJECT && fCategory != VIRTUAL) {
                p.writeByte((byte) f.bits());
                p.writeBoolean(compiled);
                if (compiled) {
                    p.writeInt(f.index());
                    p.writeInt(f.offset());
                }
            } else if (fCategory == VIRTUAL) {
                final VirtualField<?> vf = (VirtualField<?>) f;
                objPacker.writeObject(vf.mapper().getClass());
            }
        }
        final boolean isCompiled = theStruct.bits() > -1;
        p.writeBoolean(isCompiled);
        if (isCompiled) {
            p.writeInt(theStruct.bits());
        }

        final Struct[] children = theStruct.children();
        p.writeInt(children.length);
        for (final Struct child : children) {
            writeData(theContext, -1, child);
        }
    }
}
