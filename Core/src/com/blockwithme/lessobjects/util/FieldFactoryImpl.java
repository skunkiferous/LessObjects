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
package com.blockwithme.lessobjects.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EnumSet;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.FieldType;
import com.blockwithme.lessobjects.fields.global.BooleanGlobalField;
import com.blockwithme.lessobjects.fields.global.ByteGlobalField;
import com.blockwithme.lessobjects.fields.global.CharGlobalField;
import com.blockwithme.lessobjects.fields.global.DoubleGlobalField;
import com.blockwithme.lessobjects.fields.global.FloatGlobalField;
import com.blockwithme.lessobjects.fields.global.IntGlobalField;
import com.blockwithme.lessobjects.fields.global.LongGlobalField;
import com.blockwithme.lessobjects.fields.global.ShortGlobalField;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.object.StringPseudoConverter;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
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
import com.blockwithme.lessobjects.fields.virtual.BooleanFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.BooleanVirtualField;
import com.blockwithme.lessobjects.fields.virtual.ByteFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ByteVirtualField;
import com.blockwithme.lessobjects.fields.virtual.CharFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.CharVirtualField;
import com.blockwithme.lessobjects.fields.virtual.DoubleFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.DoubleVirtualField;
import com.blockwithme.lessobjects.fields.virtual.FloatFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.FloatVirtualField;
import com.blockwithme.lessobjects.fields.virtual.IntFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.IntVirtualField;
import com.blockwithme.lessobjects.fields.virtual.LongFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.LongVirtualField;
import com.blockwithme.lessobjects.fields.virtual.ShortFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ShortVirtualField;
import com.blockwithme.lessobjects.fields.virtual.ObjectFieldMapper;
import com.blockwithme.prim.BooleanConverter;
import com.blockwithme.prim.ByteConverter;
import com.blockwithme.prim.CharConverter;
import com.blockwithme.prim.Converter;
import com.blockwithme.prim.DoubleConverter;
import com.blockwithme.prim.EnumByteConverter;
import com.blockwithme.prim.EnumSetConverter;
import com.blockwithme.prim.FloatConverter;
import com.blockwithme.prim.IntConverter;
import com.blockwithme.prim.LongConverter;
import com.blockwithme.prim.ShortConverter;

/**
 * Default implementation of the factory interface.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class FieldFactoryImpl implements FieldFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends BooleanField<E, F>> F newBooleanField(
            final BooleanConverter<E> theConverter, final String theName) {
        return (F) new BooleanField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends BooleanField<Boolean, F>> F newBooleanField(
            final String theName) {
        return (F) new BooleanField<Boolean, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends BooleanGlobalField<E, F>> F newBooleanGlobalField(
            final BooleanConverter<E> theConverter, final String theName) {
        return (F) new BooleanGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends BooleanGlobalField<Boolean, F>> F newBooleanGlobalField(
            final String theName) {
        return (F) new BooleanGlobalField<Boolean, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends BooleanOptionalField<E, F>> F newBooleanOptional(
            final BooleanConverter<E> theConverter, final String theName) {
        return (F) new BooleanOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends BooleanOptionalField<Boolean, F>> F newBooleanOptional(
            final String theName) {
        return (F) new BooleanOptionalField<Boolean, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends BooleanVirtualField<E, F>> F newBooleanVirtualField(
            final BooleanConverter<E> theConverter, final String theName,
            final BooleanFieldMapper theMapper) {
        return (F) new BooleanVirtualField<E, F>(theConverter, theName,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends BooleanVirtualField<Boolean, F>> F newBooleanVirtualField(
            final String theName, final BooleanFieldMapper theMapper) {
        return (F) new BooleanVirtualField<Boolean, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteField<E, F>> F newByteField(
            final ByteConverter<E> theConverter, final String theName) {
        return (F) new ByteField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteField<E, F>> F newByteField(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new ByteField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteField<Byte, F>> F newByteField(final String theName) {
        return (F) new ByteField<Byte, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteField<Byte, F>> F newByteField(final String theName,
            final int theBits) {
        return (F) new ByteField<Byte, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteGlobalField<E, F>> F newByteGlobalField(
            final ByteConverter<E> theConverter, final String theName) {
        return (F) new ByteGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteGlobalField<E, F>> F newByteGlobalField(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new ByteGlobalField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteGlobalField<Byte, F>> F newByteGlobalField(
            final String theName) {
        return (F) new ByteGlobalField<Byte, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteGlobalField<Byte, F>> F newByteGlobalField(
            final String theName, final int theBits) {
        return (F) new ByteGlobalField<Byte, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteOptionalField<E, F>> F newByteOptional(
            final ByteConverter<E> theConverter, final String theName) {
        return (F) new ByteOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteOptionalField<E, F>> F newByteOptional(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new ByteOptionalField<E, F>(theConverter, theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteOptionalField<Byte, F>> F newByteOptional(
            final String theName) {
        return (F) new ByteOptionalField<Byte, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteOptionalField<Byte, F>> F newByteOptional(
            final String theName, final int theBits) {
        return (F) new ByteOptionalField<Byte, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteVirtualField<E, F>> F newByteVirtualField(
            final ByteConverter<E> theConverter, final String theName,
            final ByteFieldMapper theMapper) {
        return (F) new ByteVirtualField<E, F>(theConverter, theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ByteVirtualField<E, F>> F newByteVirtualField(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits, final ByteFieldMapper theMapper) {
        return (F) new ByteVirtualField<E, F>(theConverter, theName, theBits,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteVirtualField<Byte, F>> F newByteVirtualField(
            final String theName, final ByteFieldMapper theMapper) {
        return (F) new ByteVirtualField<Byte, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ByteVirtualField<Byte, F>> F newByteVirtualField(
            final String theName, final int theBits,
            final ByteFieldMapper theMapper) {
        return (F) new ByteVirtualField<Byte, F>(theName, theBits, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharField<E, F>> F newCharField(
            final CharConverter<E> theConverter, final String theName) {
        return (F) new CharField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharField<E, F>> F newCharField(
            final CharConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new CharField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharField<Character, F>> F newCharField(
            final String theName) {
        return (F) new CharField<Character, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharField<Character, F>> F newCharField(
            final String theName, final int theBits) {
        return (F) new CharField<Character, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharGlobalField<E, F>> F newCharGlobalField(
            final CharConverter<E> theConverter, final String theName) {
        return (F) new CharGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharGlobalField<E, F>> F newCharGlobalField(
            final CharConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new CharGlobalField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharGlobalField<Character, F>> F newCharGlobalField(
            final String theName) {
        return (F) new CharGlobalField<Character, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharGlobalField<Character, F>> F newCharGlobalField(
            final String theName, final int theBits) {
        return (F) new CharGlobalField<Character, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharOptionalField<E, F>> F newCharOptional(
            final CharConverter<E> theConverter, final String theName) {
        return (F) new CharOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharOptionalField<E, F>> F newCharOptional(
            final CharConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new CharOptionalField<E, F>(theConverter, theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharOptionalField<Character, F>> F newCharOptional(
            final String theName) {
        return (F) new CharOptionalField<Character, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharOptionalField<Character, F>> F newCharOptional(
            final String theName, final int theBits) {
        return (F) new CharOptionalField<Character, F>(theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharVirtualField<E, F>> F newCharVirtualField(
            final CharConverter<E> theConverter, final String theName,
            final CharFieldMapper theMapper) {
        return (F) new CharVirtualField<E, F>(theConverter, theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends CharVirtualField<E, F>> F newCharVirtualField(
            final CharConverter<E> theConverter, final String theName,
            final int theBits, final CharFieldMapper theMapper) {
        return (F) new CharVirtualField<E, F>(theConverter, theName, theBits,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharVirtualField<Character, F>> F newCharVirtualField(
            final String theName, final CharFieldMapper theMapper) {
        return (F) new CharVirtualField<Character, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends CharVirtualField<Character, F>> F newCharVirtualField(
            final String theName, final int theBits,
            final CharFieldMapper theMapper) {
        return (F) new CharVirtualField<Character, F>(theName, theBits,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends DoubleField<E, F>> F newDoubleField(
            final DoubleConverter<E> theConverter, final String theName) {
        return (F) new DoubleField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends DoubleField<Double, F>> F newDoubleField(
            final String theName) {
        return (F) new DoubleField<Double, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends DoubleGlobalField<E, F>> F newDoubleGlobalField(
            final DoubleConverter<E> theConverter, final String theName) {
        return (F) new DoubleGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends DoubleGlobalField<Double, F>> F newDoubleGlobalField(
            final String theName) {
        return (F) new DoubleGlobalField<Double, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends DoubleOptionalField<E, F>> F newDoubleOptional(
            final DoubleConverter<E> theConverter, final String theName) {
        return (F) new DoubleOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends DoubleOptionalField<Double, F>> F newDoubleOptional(
            final String theName) {
        return (F) new DoubleOptionalField<Double, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends DoubleVirtualField<E, F>> F newDoubleVirtualField(
            final DoubleConverter<E> theConverter, final String theName,
            final DoubleFieldMapper theMapper) {
        return (F) new DoubleVirtualField<E, F>(theConverter, theName,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends DoubleVirtualField<Double, F>> F newDoubleVirtualField(
            final String theName, final DoubleFieldMapper theMapper) {
        return (F) new DoubleVirtualField<Double, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends ByteField<E, F>> F newEnumField(
            final String theName, final Class<E> theEnumType) {
        return (F) new ByteField<>(new EnumByteConverter<>(theEnumType),
                theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends ByteGlobalField<E, F>> F newEnumGlobalField(
            final String theName, final Class<E> theEnumType) {
        return (F) new ByteGlobalField<>(new EnumByteConverter<>(theEnumType),
                theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends ByteOptionalField<E, F>> F newEnumOptional(
            final String theName, final Class<E> theEnumType) {
        return (F) new ByteOptionalField<>(new EnumByteConverter<>(theEnumType),
                theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends LongField<EnumSet<E>, F>> F newEnumSetField(
            final String theName, final Class<E> theEnumType) {
        return (F) new LongField<EnumSet<E>, F>(new EnumSetConverter<>(
                theEnumType), theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends LongGlobalField<EnumSet<E>, F>> F newEnumSetGlobalField(
            final String theName, final Class<E> theEnumType) {
        return (F) new LongGlobalField<EnumSet<E>, F>(new EnumSetConverter<>(
                theEnumType), theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends LongOptionalField<EnumSet<E>, F>> F newEnumSetOptional(
            final String theName, final Class<E> theEnumType) {
        return (F) new LongOptionalField<EnumSet<E>, F>(new EnumSetConverter<>(
                theEnumType), theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends LongVirtualField<EnumSet<E>, F>> F newEnumSetVirtualField(
            final String theName, final Class<E> theEnumType,
            final LongFieldMapper theMapper) {
        return (F) new LongVirtualField<EnumSet<E>, F>(new EnumSetConverter<>(
                theEnumType), theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>, F extends ByteVirtualField<E, F>> F newEnumVirtualField(
            final String theName, final Class<E> theEnumType,
            final ByteFieldMapper theMapper) {
        return (F) new ByteVirtualField<>(new EnumByteConverter<>(theEnumType),
                theName, theMapper);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Field<?, ?> newField(final FieldBuilder theBuilder) {
        checkNotNull(theBuilder);
        final FieldType fieldType = theBuilder.getFieldType();
        checkNotNull(fieldType);
        return fieldType.createField(theBuilder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends FloatField<E, F>> F newFloatField(
            final FloatConverter<E> theConverter, final String theName) {
        return (F) new FloatField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends FloatField<Float, F>> F newFloatField(final String theName) {
        return (F) new FloatField<Float, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends FloatGlobalField<E, F>> F newFloatGlobalField(
            final FloatConverter<E> theConverter, final String theName) {
        return (F) new FloatGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends FloatGlobalField<Float, F>> F newFloatGlobalField(
            final String theName) {
        return (F) new FloatGlobalField<Float, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends FloatOptionalField<E, F>> F newFloatOptional(
            final FloatConverter<E> theConverter, final String theName) {
        return (F) new FloatOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends FloatOptionalField<Float, F>> F newFloatOptional(
            final String theName) {
        return (F) new FloatOptionalField<Float, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends FloatVirtualField<E, F>> F newFloatVirtualField(
            final FloatConverter<E> theConverter, final String theName,
            final FloatFieldMapper theMapper) {
        return (F) new FloatVirtualField<E, F>(theConverter, theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends FloatVirtualField<Float, F>> F newFloatVirtualField(
            final String theName, final FloatFieldMapper theMapper) {
        return (F) new FloatVirtualField<Float, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntField<E, F>> F newIntField(
            final IntConverter<E> theConverter, final String theName) {
        return (F) new IntField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntField<E, F>> F newIntField(
            final IntConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new IntField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntField<Integer, F>> F newIntField(final String theName) {
        return (F) new IntField<Integer, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntField<Integer, F>> F newIntField(final String theName,
            final int theBits) {
        return (F) new IntField<Integer, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntGlobalField<E, F>> F newIntGlobalField(
            final IntConverter<E> theConverter, final String theName) {
        return (F) new IntGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntGlobalField<E, F>> F newIntGlobalField(
            final IntConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new IntGlobalField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntGlobalField<Integer, F>> F newIntGlobalField(
            final String theName) {
        return (F) new IntGlobalField<Integer, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntGlobalField<Integer, F>> F newIntGlobalField(
            final String theName, final int theBits) {
        return (F) new IntGlobalField<Integer, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntOptionalField<E, F>> F newIntOptional(
            final IntConverter<E> theConverter, final String theName) {
        return (F) new IntOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntOptionalField<E, F>> F newIntOptional(
            final IntConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new IntOptionalField<E, F>(theConverter, theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntOptionalField<Integer, F>> F newIntOptional(
            final String theName) {
        return (F) new IntOptionalField<Integer, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntOptionalField<Integer, F>> F newIntOptional(
            final String theName, final int theBits) {
        return (F) new IntOptionalField<Integer, F>(theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntVirtualField<E, F>> F newIntVirtualField(
            final IntConverter<E> theConverter, final String theName,
            final int theBits, final IntFieldMapper theMapper) {
        return (F) new IntVirtualField<E, F>(theConverter, theName, theBits,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends IntVirtualField<E, F>> F newIntVirtualField(
            final IntConverter<E> theConverter, final String theName,
            final IntFieldMapper theMapper) {
        return (F) new IntVirtualField<E, F>(theConverter, theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntVirtualField<Integer, F>> F newIntVirtualField(
            final String theName, final int theBits,
            final IntFieldMapper theMapper) {
        return (F) new IntVirtualField<Integer, F>(theName, theBits, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends IntVirtualField<Integer, F>> F newIntVirtualField(
            final String theName, final IntFieldMapper theMapper) {
        return (F) new IntVirtualField<Integer, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongField<E, F>> F newLongField(
            final LongConverter<E> theConverter, final String theName) {
        return (F) new LongField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongField<E, F>> F newLongField(
            final LongConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new LongField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongField<Long, F>> F newLongField(final String theName) {
        return (F) new LongField<Long, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongField<Long, F>> F newLongField(final String theName,
            final int theBits) {
        return (F) new LongField<Long, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongGlobalField<E, F>> F newLongGlobalField(
            final LongConverter<E> theConverter, final String theName) {
        return (F) new LongGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongGlobalField<E, F>> F newLongGlobalField(
            final LongConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new LongGlobalField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongGlobalField<Long, F>> F newLongGlobalField(
            final String theName) {
        return (F) new LongGlobalField<Long, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongGlobalField<Long, F>> F newLongGlobalField(
            final String theName, final int theBits) {
        return (F) new LongGlobalField<Long, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongOptionalField<E, F>> F newLongOptional(
            final LongConverter<E> theConverter, final String theName) {
        return (F) new LongOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongOptionalField<E, F>> F newLongOptional(
            final LongConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new LongOptionalField<E, F>(theConverter, theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongOptionalField<Long, F>> F newLongOptional(
            final String theName) {
        return (F) new LongOptionalField<Long, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongOptionalField<Long, F>> F newLongOptional(
            final String theName, final int theBits) {
        return (F) new LongOptionalField<Long, F>(theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongVirtualField<E, F>> F newLongVirtualField(
            final LongConverter<E> theConverter, final String theName,
            final int theBits, final LongFieldMapper theMapper) {
        return (F) new LongVirtualField<E, F>(theConverter, theName, theBits,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends LongVirtualField<E, F>> F newLongVirtualField(
            final LongConverter<E> theConverter, final String theName,
            final LongFieldMapper theMapper) {
        return (F) new LongVirtualField<E, F>(theConverter, theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongVirtualField<Long, F>> F newLongVirtualField(
            final String theName, final int theBits,
            final LongFieldMapper theMapper) {
        return (F) new LongVirtualField<Long, F>(theName, theBits, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends LongVirtualField<Long, F>> F newLongVirtualField(
            final String theName, final LongFieldMapper theMapper) {
        return (F) new LongVirtualField<Long, F>(theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ObjectField<E, F>> F newObjectField(
            final Converter<E> theConverter, final String theName) {
        return (F) new ObjectField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ObjectField<E, F>> F newObjectGlobalField(
            final Converter<E> theConverter, final String theName) {
        return (F) new ObjectField<E, F>(theConverter, theName, true, false,
                false, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ObjectField<E, F>> F newObjectOptional(
            final Converter<E> theConverter, final String theName) {
        return (F) new ObjectField<E, F>(theConverter, theName, false, true,
                false, null);
    }

    /** Creates a Virtual ObjectField. */
    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ObjectField<E, F>> F newObjectVirtualField(
            final Converter<E> theConverter, final String theName,
            final ObjectFieldMapper<E> theMapper) {
        return (F) new ObjectField<E, F>(theConverter, theName, false, false,
                true, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortField<E, F>> F newShortField(
            final ShortConverter<E> theConverter, final String theName) {
        return (F) new ShortField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortField<E, F>> F newShortField(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new ShortField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortField<Short, F>> F newShortField(final String theName) {
        return (F) new ShortField<Short, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortField<Short, F>> F newShortField(
            final String theName, final int theBits) {
        return (F) new ShortField<Short, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortGlobalField<E, F>> F newShortGlobalField(
            final ShortConverter<E> theConverter, final String theName) {
        return (F) new ShortGlobalField<E, F>(theConverter, theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortGlobalField<E, F>> F newShortGlobalField(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new ShortGlobalField<E, F>(theConverter, theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortGlobalField<Short, F>> F newShortGlobalField(
            final String theName) {
        return (F) new ShortGlobalField<Short, F>(theName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortGlobalField<Short, F>> F newShortGlobalField(
            final String theName, final int theBits) {
        return (F) new ShortGlobalField<Short, F>(theName, theBits);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortOptionalField<E, F>> F newShortOptional(
            final ShortConverter<E> theConverter, final String theName) {
        return (F) new ShortOptionalField<E, F>(theConverter, theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortOptionalField<E, F>> F newShortOptional(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits) {
        return (F) new ShortOptionalField<E, F>(theConverter, theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortOptionalField<Short, F>> F newShortOptional(
            final String theName) {
        return (F) new ShortOptionalField<Short, F>(theName, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortOptionalField<Short, F>> F newShortOptional(
            final String theName, final int theBits) {
        return (F) new ShortOptionalField<Short, F>(theName, theBits, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortVirtualField<E, F>> F newShortVirtualField(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits, final ShortFieldMapper theMapper) {
        return (F) new ShortVirtualField<E, F>(theConverter, theName, theBits,
                theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ShortVirtualField<E, F>> F newShortVirtualField(
            final ShortConverter<E> theConverter, final String theName,
            final ShortFieldMapper theMapper) {
        return (F) new ShortVirtualField<E, F>(theConverter, theName, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortVirtualField<Short, F>> F newShortVirtualField(
            final String theName, final int theBits,
            final ShortFieldMapper theMapper) {
        return (F) new ShortVirtualField<Short, F>(theName, theBits, theMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ShortVirtualField<Short, F>> F newShortVirtualField(
            final String theName, final ShortFieldMapper theMapper) {
        return (F) new ShortVirtualField<Short, F>(theName, theMapper);
    }

    @SuppressWarnings("null")
    @Override
    public <F extends ObjectField<String, F>> F newStringField(
            final String theName) {
        return newObjectField(StringPseudoConverter.INSTANCE, theName);
    }

    @SuppressWarnings("null")
    @Override
    public <F extends ObjectField<String, F>> F newStringGlobalField(
            final String theName) {
        return newObjectGlobalField(StringPseudoConverter.INSTANCE, theName);
    }

    @SuppressWarnings("null")
    @Override
    public <F extends ObjectField<String, F>> F newStringOptional(
            final String theName) {
        return newObjectOptional(StringPseudoConverter.INSTANCE, theName);
    }

    @SuppressWarnings("null")
    @Override
    public <F extends ObjectField<String, F>> F newStringVirtualField(
            final String theName, final ObjectFieldMapper<String> theMapper) {
        return newObjectVirtualField(StringPseudoConverter.INSTANCE, theName,
                theMapper);
    }
}
