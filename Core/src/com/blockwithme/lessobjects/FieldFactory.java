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
package com.blockwithme.lessobjects;

import java.util.EnumSet;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.fields.global.BooleanGlobalField;
import com.blockwithme.lessobjects.fields.global.ByteGlobalField;
import com.blockwithme.lessobjects.fields.global.CharGlobalField;
import com.blockwithme.lessobjects.fields.global.DoubleGlobalField;
import com.blockwithme.lessobjects.fields.global.FloatGlobalField;
import com.blockwithme.lessobjects.fields.global.IntGlobalField;
import com.blockwithme.lessobjects.fields.global.LongGlobalField;
import com.blockwithme.lessobjects.fields.global.ShortGlobalField;
import com.blockwithme.lessobjects.fields.object.ObjectField;
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
 * Creates the fields and field data objects.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface FieldFactory {

    /** Creates a BooleanField. */
    <E, F extends BooleanField<E, F>> F newBooleanField(
            final BooleanConverter<E> theConverter, final String theName);

    /** Creates a BooleanField. */
    <F extends BooleanField<Boolean, F>> F newBooleanField(final String theName);

    /** Creates a BooleanGlobalField. */
    <E, F extends BooleanGlobalField<E, F>> F newBooleanGlobalField(
            final BooleanConverter<E> theConverter, final String theName);

    /** Creates a BooleanGlobalField. */
    <F extends BooleanGlobalField<Boolean, F>> F newBooleanGlobalField(
            final String theName);

    /** Creates a BooleanOptional. */
    <E, F extends BooleanOptionalField<E, F>> F newBooleanOptional(
            final BooleanConverter<E> theConverter, final String theName);

    /** Creates a BooleanOptional. */
    <F extends BooleanOptionalField<Boolean, F>> F newBooleanOptional(
            final String theName);

    /** Creates a Boolean Virtual Field. */
    <E, F extends BooleanVirtualField<E, F>> F newBooleanVirtualField(
            final BooleanConverter<E> theConverter, final String theName,
            BooleanFieldMapper theMapper);

    /** Creates a Boolean Virtual Field. */
    <F extends BooleanVirtualField<Boolean, F>> F newBooleanVirtualField(
            final String theName, BooleanFieldMapper theMapper);

    /** Creates a ByteField. */
    <E, F extends ByteField<E, F>> F newByteField(
            final ByteConverter<E> theConverter, final String theName);

    /** Creates a ByteField with specified size. */
    <E, F extends ByteField<E, F>> F newByteField(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a ByteField. */
    <F extends ByteField<Byte, F>> F newByteField(final String theName);

    /** Creates a ByteField with specified size. */
    <F extends ByteField<Byte, F>> F newByteField(final String theName,
            final int theBits);

    /** Creates a ByteGlobalField. */
    <E, F extends ByteGlobalField<E, F>> F newByteGlobalField(
            final ByteConverter<E> theConverter, final String theName);

    /** Creates a ByteGlobalField with specified size. */
    <E, F extends ByteGlobalField<E, F>> F newByteGlobalField(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a ByteGlobalField. */
    <F extends ByteGlobalField<Byte, F>> F newByteGlobalField(
            final String theName);

    /** Creates a ByteGlobalField with specified size. */
    <F extends ByteGlobalField<Byte, F>> F newByteGlobalField(
            final String theName, final int theBits);

    /** Creates a ByteOptional. */
    <E, F extends ByteOptionalField<E, F>> F newByteOptional(
            final ByteConverter<E> theConverter, final String theName);

    /** Creates a ByteOptional with specified size. */
    <E, F extends ByteOptionalField<E, F>> F newByteOptional(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a ByteOptional. */
    <F extends ByteOptionalField<Byte, F>> F newByteOptional(final String theName);

    /** Creates a ByteOptional with specified size. */
    <F extends ByteOptionalField<Byte, F>> F newByteOptional(final String theName,
            final int theBits);

    /** Creates a Byte Virtual Field. */
    <E, F extends ByteVirtualField<E, F>> F newByteVirtualField(
            final ByteConverter<E> theConverter, final String theName,
            ByteFieldMapper theMapper);

    /** Creates a Byte Virtual Field with specified size. */
    <E, F extends ByteVirtualField<E, F>> F newByteVirtualField(
            final ByteConverter<E> theConverter, final String theName,
            final int theBits, ByteFieldMapper theMapper);

    /** Creates a Byte Virtual Field. */
    <F extends ByteVirtualField<Byte, F>> F newByteVirtualField(
            final String theName, ByteFieldMapper theMapper);

    /** Creates a Byte Virtual Field with specified size. */
    <F extends ByteVirtualField<Byte, F>> F newByteVirtualField(
            final String theName, final int theBits, ByteFieldMapper theMapper);

    /** Creates a CharField. */
    <E, F extends CharField<E, F>> F newCharField(
            final CharConverter<E> theConverter, final String theName);

    /** Creates a CharField with specified size. */
    <E, F extends CharField<E, F>> F newCharField(
            final CharConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a CharField. */
    <F extends CharField<Character, F>> F newCharField(final String theName);

    /** Creates a CharField with specified size. */
    <F extends CharField<Character, F>> F newCharField(final String theName,
            final int theBits);

    /** Creates a CharGlobalField. */
    <E, F extends CharGlobalField<E, F>> F newCharGlobalField(
            final CharConverter<E> theConverter, final String theName);

    /** Creates a CharGlobalField with specified size. */
    <E, F extends CharGlobalField<E, F>> F newCharGlobalField(
            final CharConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a CharGlobalField. */
    <F extends CharGlobalField<Character, F>> F newCharGlobalField(
            final String theName);

    /** Creates a CharGlobalField with specified size. */
    <F extends CharGlobalField<Character, F>> F newCharGlobalField(
            final String theName, final int theBits);

    /** Creates a CharOptional. */
    <E, F extends CharOptionalField<E, F>> F newCharOptional(
            final CharConverter<E> theConverter, final String theName);

    /** Creates a CharOptional with specified size. */
    <E, F extends CharOptionalField<E, F>> F newCharOptional(
            final CharConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a CharOptional. */
    <F extends CharOptionalField<Character, F>> F newCharOptional(
            final String theName);

    /** Creates a CharOptional with specified size. */
    <F extends CharOptionalField<Character, F>> F newCharOptional(
            final String theName, final int theBits);

    /** Creates a Char Virtual Field. */
    <E, F extends CharVirtualField<E, F>> F newCharVirtualField(
            final CharConverter<E> theConverter, final String theName,
            CharFieldMapper theMapper);

    /** Creates a Char Virtual Field with specified size. */
    <E, F extends CharVirtualField<E, F>> F newCharVirtualField(
            final CharConverter<E> theConverter, final String theName,
            final int theBits, CharFieldMapper theMapper);

    /** Creates a Char Virtual Field. */
    <F extends CharVirtualField<Character, F>> F newCharVirtualField(
            final String theName, CharFieldMapper theMapper);

    /** Creates a Char Virtual Field with specified size. */
    <F extends CharVirtualField<Character, F>> F newCharVirtualField(
            final String theName, final int theBits, CharFieldMapper theMapper);

    /** Creates a DoubleField. */
    <E, F extends DoubleField<E, F>> F newDoubleField(
            final DoubleConverter<E> theConverter, final String theName);

    /** Creates a DoubleField. */
    <F extends DoubleField<Double, F>> F newDoubleField(final String theName);

    /** Creates a DoubleGlobalField. */
    <E, F extends DoubleGlobalField<E, F>> F newDoubleGlobalField(
            final DoubleConverter<E> theConverter, final String theName);

    /** Creates a DoubleGlobalField. */
    <F extends DoubleGlobalField<Double, F>> F newDoubleGlobalField(
            final String theName);

    /** Creates a DoubleOptional. */
    <E, F extends DoubleOptionalField<E, F>> F newDoubleOptional(
            final DoubleConverter<E> theConverter, final String theName);

    /** Creates a DoubleOptional. */
    <F extends DoubleOptionalField<Double, F>> F newDoubleOptional(
            final String theName);

    /** Creates a Double Virtual Field. */
    <E, F extends DoubleVirtualField<E, F>> F newDoubleVirtualField(
            final DoubleConverter<E> theConverter, final String theName,
            DoubleFieldMapper theMapper);

    /** Creates a Double Virtual Field. */
    <F extends DoubleVirtualField<Double, F>> F newDoubleVirtualField(
            final String theName, DoubleFieldMapper theMapper);

    /** Creates a EnumField. */
    <E extends Enum<E>, F extends ByteField<E, F>> F newEnumField(
            final String theName, final Class<E> theEnumType);

    /** Creates a EnumGlobalField. */
    <E extends Enum<E>, F extends ByteGlobalField<E, F>> F newEnumGlobalField(
            final String theName, final Class<E> theEnumType);

    /** Creates a EnumOptional. */
    <E extends Enum<E>, F extends ByteOptionalField<E, F>> F newEnumOptional(
            final String theName, final Class<E> theEnumType);

    /** Creates a EnumSetField. */
    <E extends Enum<E>, F extends LongField<EnumSet<E>, F>> F newEnumSetField(
            final String theName, final Class<E> theEnumType);

    /** Creates a EnumGlobalField. */
    <E extends Enum<E>, F extends LongGlobalField<EnumSet<E>, F>> F newEnumSetGlobalField(
            final String theName, final Class<E> theEnumType);

    /** Creates a EnumOptional. */
    <E extends Enum<E>, F extends LongOptionalField<EnumSet<E>, F>> F newEnumSetOptional(
            final String theName, final Class<E> theEnumType);

    /** Creates a Enum Virtual Field. */
    <E extends Enum<E>, F extends LongVirtualField<EnumSet<E>, F>> F newEnumSetVirtualField(
            final String theName, final Class<E> theEnumType,
            LongFieldMapper theMapper);

    /** Creates a Enum Virtual Field. */
    <E extends Enum<E>, F extends ByteVirtualField<E, F>> F newEnumVirtualField(
            final String theName, final Class<E> theEnumType,
            ByteFieldMapper theMapper);

    /** Creates a new field object using the Builder objects. */
    Field<?, ?> newField(FieldBuilder theBuilder);

    /** Creates a FloatField. */
    <E, F extends FloatField<E, F>> F newFloatField(
            final FloatConverter<E> theConverter, final String theName);

    /** Creates a FloatField. */
    <F extends FloatField<Float, F>> F newFloatField(final String theName);

    /** Creates a FloatGlobalField. */
    <E, F extends FloatGlobalField<E, F>> F newFloatGlobalField(
            final FloatConverter<E> theConverter, final String theName);

    /** Creates a FloatGlobalField. */
    <F extends FloatGlobalField<Float, F>> F newFloatGlobalField(
            final String theName);

    /** Creates a FloatOptional. */
    <E, F extends FloatOptionalField<E, F>> F newFloatOptional(
            final FloatConverter<E> theConverter, final String theName);

    /** Creates a FloatOptional. */
    <F extends FloatOptionalField<Float, F>> F newFloatOptional(final String theName);

    /** Creates a Float Virtual Field. */
    <E, F extends FloatVirtualField<E, F>> F newFloatVirtualField(
            final FloatConverter<E> theConverter, final String theName,
            FloatFieldMapper theMapper);

    /** Creates a Float Virtual Field. */
    <F extends FloatVirtualField<Float, F>> F newFloatVirtualField(
            final String theName, FloatFieldMapper theMapper);

    /** Creates a 32 bit int Field. */
    <E, F extends IntField<E, F>> F newIntField(
            final IntConverter<E> theConverter, final String theName);

    /** Creates a IntField with size specified by 'theBits'. */
    <E, F extends IntField<E, F>> F newIntField(
            final IntConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a 32 bit int Field. */
    <F extends IntField<Integer, F>> F newIntField(final String theName);

    /** Creates a IntField with size specified by 'theBits'. */
    <F extends IntField<Integer, F>> F newIntField(final String theName,
            final int theBits);

    /** Creates a IntGlobalField. */
    <E, F extends IntGlobalField<E, F>> F newIntGlobalField(
            final IntConverter<E> theConverter, final String theName);

    /** Creates a IntGlobalField with size specified by 'theBits'. */
    <E, F extends IntGlobalField<E, F>> F newIntGlobalField(
            final IntConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a IntGlobalField. */
    <F extends IntGlobalField<Integer, F>> F newIntGlobalField(
            final String theName);

    /** Creates a IntGlobalField with size specified by 'theBits'. */
    <F extends IntGlobalField<Integer, F>> F newIntGlobalField(
            final String theName, final int theBits);

    /** Creates a IntOptional. */
    <E, F extends IntOptionalField<E, F>> F newIntOptional(
            final IntConverter<E> theConverter, final String theName);

    /** Creates a IntOptional with size specified by 'theBits'. */
    <E, F extends IntOptionalField<E, F>> F newIntOptional(
            final IntConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a IntOptional. */
    <F extends IntOptionalField<Integer, F>> F newIntOptional(final String theName);

    /** Creates a IntOptional with size specified by 'theBits'. */
    <F extends IntOptionalField<Integer, F>> F newIntOptional(final String theName,
            final int theBits);

    /** Creates a Int Virtual Field with size specified by 'theBits'. */
    <E, F extends IntVirtualField<E, F>> F newIntVirtualField(
            final IntConverter<E> theConverter, final String theName,
            final int theBits, IntFieldMapper theMapper);

    /** Creates a Int Virtual Field. */
    <E, F extends IntVirtualField<E, F>> F newIntVirtualField(
            final IntConverter<E> theConverter, final String theName,
            IntFieldMapper theMapper);

    /** Creates a Int Virtual Field with size specified by 'theBits'. */
    <F extends IntVirtualField<Integer, F>> F newIntVirtualField(
            final String theName, final int theBits, IntFieldMapper theMapper);

    /** Creates a Int Virtual Field. */
    <F extends IntVirtualField<Integer, F>> F newIntVirtualField(
            final String theName, IntFieldMapper theMapper);

    /** Creates a LongField. */
    <E, F extends LongField<E, F>> F newLongField(
            final LongConverter<E> theConverter, final String theName);

    /** Creates a LongField with size specified by 'theBits'. */
    <E, F extends LongField<E, F>> F newLongField(
            final LongConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a LongField. */
    <F extends LongField<Long, F>> F newLongField(final String theName);

    /** Creates a LongField with size specified by 'theBits'. */
    <F extends LongField<Long, F>> F newLongField(final String theName,
            final int theBits);

    /** Creates a Global Long Field. */
    <E, F extends LongGlobalField<E, F>> F newLongGlobalField(
            final LongConverter<E> theConverter, final String theName);

    /** Creates a Global Long Field with size specified by 'theBits'. */
    <E, F extends LongGlobalField<E, F>> F newLongGlobalField(
            final LongConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a Global Long Field. */
    <F extends LongGlobalField<Long, F>> F newLongGlobalField(
            final String theName);

    /** Creates a Global Long Field with size specified by 'theBits'. */
    <F extends LongGlobalField<Long, F>> F newLongGlobalField(
            final String theName, final int theBits);

    /** Creates a LongOptional. */
    <E, F extends LongOptionalField<E, F>> F newLongOptional(
            final LongConverter<E> theConverter, final String theName);

    /** Creates a LongOptional with size specified by 'theBits'. */
    <E, F extends LongOptionalField<E, F>> F newLongOptional(
            final LongConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a LongOptional. */
    <F extends LongOptionalField<Long, F>> F newLongOptional(final String theName);

    /** Creates a LongOptional with size specified by 'theBits'. */
    <F extends LongOptionalField<Long, F>> F newLongOptional(final String theName,
            final int theBits);

    /** Creates a Long Virtual Field with size specified by 'theBits'. */
    <E, F extends LongVirtualField<E, F>> F newLongVirtualField(
            final LongConverter<E> theConverter, final String theName,
            final int theBits, LongFieldMapper theMapper);

    /** Creates a Long Virtual Field. */
    <E, F extends LongVirtualField<E, F>> F newLongVirtualField(
            final LongConverter<E> theConverter, final String theName,
            LongFieldMapper theMapper);

    /** Creates a Long Virtual Field with size specified by 'theBits'. */
    <F extends LongVirtualField<Long, F>> F newLongVirtualField(
            final String theName, final int theBits, LongFieldMapper theMapper);

    /** Creates a Long Virtual Field. */
    <F extends LongVirtualField<Long, F>> F newLongVirtualField(
            final String theName, LongFieldMapper theMapper);

    /** Creates a new ObjectField. */
    <E, F extends ObjectField<E, F>> F newObjectField(
            final Converter<E> theConverter, final String theName);

    /** Creates a new Global ObjectField. */
    <E, F extends ObjectField<E, F>> F newObjectGlobalField(
            final Converter<E> theConverter, final String theName);

    /** Creates a new Optional ObjectField. */
    <E, F extends ObjectField<E, F>> F newObjectOptional(
            final Converter<E> theConverter, final String theName);

    /** Creates a Virtual ObjectField. */
    <E, F extends ObjectField<E, F>> F newObjectVirtualField(
            final Converter<E> theConverter, final String theName,
            ObjectFieldMapper<E> theMapper);

    /** Creates a ShortField. */
    <E, F extends ShortField<E, F>> F newShortField(
            final ShortConverter<E> theConverter, final String theName);

    /** Creates a ShortField with size specified by 'theBits'. */
    <E, F extends ShortField<E, F>> F newShortField(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a ShortField. */
    <F extends ShortField<Short, F>> F newShortField(final String theName);

    /** Creates a ShortField with size specified by 'theBits'. */
    <F extends ShortField<Short, F>> F newShortField(final String theName,
            final int theBits);

    /** Creates a ShortGlobalField. */
    <E, F extends ShortGlobalField<E, F>> F newShortGlobalField(
            final ShortConverter<E> theConverter, final String theName);

    /** Creates a ShortGlobalField with size specified by 'theBits'. */
    <E, F extends ShortGlobalField<E, F>> F newShortGlobalField(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a ShortGlobalField. */
    <F extends ShortGlobalField<Short, F>> F newShortGlobalField(
            final String theName);

    /** Creates a ShortGlobalField with size specified by 'theBits'. */
    <F extends ShortGlobalField<Short, F>> F newShortGlobalField(
            final String theName, final int theBits);

    /** Creates a ShortOptional. */
    <E, F extends ShortOptionalField<E, F>> F newShortOptional(
            final ShortConverter<E> theConverter, final String theName);

    /** Creates a ShortOptional with size specified by 'theBits'. */
    <E, F extends ShortOptionalField<E, F>> F newShortOptional(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits);

    /** Creates a ShortOptional. */
    <F extends ShortOptionalField<Short, F>> F newShortOptional(final String theName);

    /** Creates a ShortOptional with size specified by 'theBits'. */
    <F extends ShortOptionalField<Short, F>> F newShortOptional(
            final String theName, final int theBits);

    /** Creates a Short Virtual Field with size specified by 'theBits'. */
    <E, F extends ShortVirtualField<E, F>> F newShortVirtualField(
            final ShortConverter<E> theConverter, final String theName,
            final int theBits, ShortFieldMapper theMapper);

    /** Creates a Short Virtual Field. */
    <E, F extends ShortVirtualField<E, F>> F newShortVirtualField(
            final ShortConverter<E> theConverter, final String theName,
            ShortFieldMapper theMapper);

    /** Creates a Short Virtual Field with size specified by 'theBits'. */
    <F extends ShortVirtualField<Short, F>> F newShortVirtualField(
            final String theName, final int theBits, ShortFieldMapper theMapper);

    /** Creates a Short Virtual Field. */
    <F extends ShortVirtualField<Short, F>> F newShortVirtualField(
            final String theName, ShortFieldMapper theMapper);

    /** Creates a new String Field. */
    <F extends ObjectField<String, F>> F newStringField(final String theName);

    /** Creates a new String Global Field. */
    <F extends ObjectField<String, F>> F newStringGlobalField(
            final String theName);

    /** Creates a new String Optional Field. */
    <F extends ObjectField<String, F>> F newStringOptional(final String theName);

    /** Creates a new Virtual Global Field. */
    <F extends ObjectField<String, F>> F newStringVirtualField(
            final String theName, ObjectFieldMapper<String> theMapper);
}
