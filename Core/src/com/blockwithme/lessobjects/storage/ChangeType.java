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
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.storage.AbstractStorage.STRUCT;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.BooleanValueChange;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.IntValueChange;
import com.blockwithme.lessobjects.beans.LongValueChange;
import com.blockwithme.lessobjects.beans.ObjectValueChange;
import com.blockwithme.lessobjects.beans.ShortValueChange;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.fields.global.BooleanGlobalField;
import com.blockwithme.lessobjects.fields.global.ByteGlobalField;
import com.blockwithme.lessobjects.fields.global.CharGlobalField;
import com.blockwithme.lessobjects.fields.global.DoubleGlobalField;
import com.blockwithme.lessobjects.fields.global.FloatGlobalField;
import com.blockwithme.lessobjects.fields.global.IntGlobalField;
import com.blockwithme.lessobjects.fields.global.LongGlobalField;
import com.blockwithme.lessobjects.fields.global.ShortGlobalField;
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
import com.blockwithme.lessobjects.storage.ChangeRecordsImpl.ValueChangeObjects;

/**
 * The Enum ChangeType defines all the types of Fields that can be "changed".
 * As well as a mean to extract the change data from the change storage.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public enum ChangeType {

    /** The Boolean field change Type. */
    BOOLEAN_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final boolean oldValue = theChangeStorage.read(STRUCT.getOld()
                    .booleanField());
            final boolean newValue = theChangeStorage.read(STRUCT.getNew()
                    .booleanField());
            final BooleanValueChange booleanValueChange = theChangeObjects
                    .booleanValueChange();
            booleanValueChange.update(theIndex, (BooleanField<?, ?>) theField,
                    oldValue, newValue);
            return booleanValueChange;
        }
    },

    /** The boolean global change Type . */
    BOOLEAN_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final boolean oldValue = theChangeStorage.read(STRUCT.getOld()
                    .booleanField());
            final boolean newValue = theChangeStorage.read(STRUCT.getNew()
                    .booleanField());
            final BooleanValueChange booleanValueChange = theChangeObjects
                    .booleanValueChange();
            booleanValueChange.update(theIndex,
                    (BooleanGlobalField<?, ?>) theField, oldValue, newValue);
            return booleanValueChange;
        }
    },

    /** The boolean optional field change Type . */
    BOOLEAN_OPTIONAL {
        @SuppressWarnings("null")
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final boolean oldValue = theChangeStorage.read(STRUCT.getOld()
                    .booleanField());
            final boolean newValue = theChangeStorage.read(STRUCT.getNew()
                    .booleanField());
            final BooleanValueChange booleanValueChange = theChangeObjects
                    .booleanValueChange();
            booleanValueChange.update(theIndex,
                    (BooleanOptionalField<?, ?>) theField, oldValue, newValue);
            return booleanValueChange;
        }
    },

    /** The byte field change Type . */
    BYTE_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final byte oldValue = theChangeStorage.read(STRUCT.getOld()
                    .byteField());
            final byte newValue = theChangeStorage.read(STRUCT.getNew()
                    .byteField());
            final ByteValueChange byteValueChange = theChangeObjects
                    .byteValueChange();
            byteValueChange.update(theIndex, (ByteField<?, ?>) theField,
                    oldValue, newValue);
            return byteValueChange;
        }
    },

    /** The byte global field change Type . */
    BYTE_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final byte oldValue = theChangeStorage.read(STRUCT.getOld()
                    .byteField());
            final byte newValue = theChangeStorage.read(STRUCT.getNew()
                    .byteField());
            final ByteValueChange byteValueChange = theChangeObjects
                    .byteValueChange();
            byteValueChange.update(theIndex, (ByteGlobalField<?, ?>) theField,
                    oldValue, newValue);
            return byteValueChange;
        }
    },

    /** The byte optional change Type . */
    BYTE_OPTIONAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final byte oldValue = theChangeStorage.read(STRUCT.getOld()
                    .byteField());
            final byte newValue = theChangeStorage.read(STRUCT.getNew()
                    .byteField());
            final ByteValueChange byteValueChange = theChangeObjects
                    .byteValueChange();
            byteValueChange.update(theIndex, (ByteOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return byteValueChange;
        }
    },

    /** The char field change Type . */
    CHAR_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final char oldValue = theChangeStorage.read(STRUCT.getOld()
                    .charField());
            final char newValue = theChangeStorage.read(STRUCT.getNew()
                    .charField());
            final CharValueChange charValueChange = theChangeObjects
                    .charValueChange();
            charValueChange.update(theIndex, (CharField<?, ?>) theField,
                    oldValue, newValue);
            return charValueChange;

        }
    },

    /** The char global field change Type . */
    CHAR_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final char oldValue = theChangeStorage.read(STRUCT.getOld()
                    .charField());
            final char newValue = theChangeStorage.read(STRUCT.getNew()
                    .charField());
            final CharValueChange charValueChange = theChangeObjects
                    .charValueChange();
            charValueChange.update(theIndex, (CharGlobalField<?, ?>) theField,
                    oldValue, newValue);
            return charValueChange;

        }
    },

    /** The char optional field change Type . */
    CHAR_OPTIONAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final char oldValue = theChangeStorage.read(STRUCT.getOld()
                    .charField());
            final char newValue = theChangeStorage.read(STRUCT.getNew()
                    .charField());
            final CharValueChange charValueChange = theChangeObjects
                    .charValueChange();
            charValueChange.update(theIndex, (CharOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return charValueChange;

        }
    },

    /** The double field change Type . */
    DOUBLE_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final double oldValue = theChangeStorage.read(STRUCT.getOld()
                    .doubleField());
            final double newValue = theChangeStorage.read(STRUCT.getNew()
                    .doubleField());
            final DoubleValueChange doubleValueChange = theChangeObjects
                    .doubleValueChange();
            doubleValueChange.update(theIndex, (DoubleField<?, ?>) theField,
                    oldValue, newValue);
            return doubleValueChange;
        }
    },

    /** The double global field change Type . */
    DOUBLE_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final double oldValue = theChangeStorage.read(STRUCT.getOld()
                    .doubleField());
            final double newValue = theChangeStorage.read(STRUCT.getNew()
                    .doubleField());

            final DoubleValueChange doubleValueChange = theChangeObjects
                    .doubleValueChange();
            doubleValueChange.update(theIndex,
                    (DoubleGlobalField<?, ?>) theField, oldValue, newValue);
            return doubleValueChange;

        }
    },

    /** The double optional field change Type . */
    DOUBLE_OPTIONAL {
        @Override
        @Nonnull
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final double oldValue = theChangeStorage.read(STRUCT.getOld()
                    .doubleField());
            final double newValue = theChangeStorage.read(STRUCT.getNew()
                    .doubleField());
            final DoubleValueChange doubleValueChange = theChangeObjects
                    .doubleValueChange();
            doubleValueChange.update(theIndex, (DoubleOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return doubleValueChange;

        }
    },

    /** The float field change Type . */
    FLOAT_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final float oldValue = theChangeStorage.read(STRUCT.getOld()
                    .floatField());
            final float newValue = theChangeStorage.read(STRUCT.getNew()
                    .floatField());
            final FloatValueChange floatValueChange = theChangeObjects
                    .floatValueChange();
            floatValueChange.update(theIndex, (FloatField<?, ?>) theField,
                    oldValue, newValue);
            return floatValueChange;
        }
    },

    /** The float global field change Type . */
    FLOAT_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {

            final float oldValue = theChangeStorage.read(STRUCT.getOld()
                    .floatField());
            final float newValue = theChangeStorage.read(STRUCT.getNew()
                    .floatField());
            final FloatValueChange floatValueChange = theChangeObjects
                    .floatValueChange();
            floatValueChange.update(theIndex,
                    (FloatGlobalField<?, ?>) theField, oldValue, newValue);
            return floatValueChange;

        }
    },

    /** The float optional change Type . */
    FLOAT_OPTIONAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final float oldValue = theChangeStorage.read(STRUCT.getOld()
                    .floatField());
            final float newValue = theChangeStorage.read(STRUCT.getNew()
                    .floatField());

            final FloatValueChange floatValueChange = theChangeObjects
                    .floatValueChange();
            floatValueChange.update(theIndex, (FloatOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return floatValueChange;
        }
    },

    /** The int field change Type . */
    INT_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final int oldValue = theChangeStorage.read(STRUCT.getOld()
                    .intField());
            final int newValue = theChangeStorage.read(STRUCT.getNew()
                    .intField());
            final IntValueChange intValueChange = theChangeObjects
                    .intValueChange();
            intValueChange.update(theIndex, (IntField<?, ?>) theField,
                    oldValue, newValue);
            return intValueChange;

        }
    },

    /** The int global field change Type . */
    INT_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final int oldValue = theChangeStorage.read(STRUCT.getOld()
                    .intField());
            final int newValue = theChangeStorage.read(STRUCT.getNew()
                    .intField());
            final IntValueChange intValueChange = theChangeObjects
                    .intValueChange();
            intValueChange.update(theIndex, (IntGlobalField<?, ?>) theField,
                    oldValue, newValue);
            return intValueChange;

        }
    },

    /** The int optional field change Type . */
    INT_OPTIONAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final int oldValue = theChangeStorage.read(STRUCT.getOld()
                    .intField());
            final int newValue = theChangeStorage.read(STRUCT.getNew()
                    .intField());
            final IntValueChange intValueChange = theChangeObjects
                    .intValueChange();
            intValueChange.update(theIndex, (IntOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return intValueChange;

        }
    },

    /** The long field change Type . */
    LONG_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final long oldValue = theChangeStorage.read(STRUCT.getOld()
                    .longField());
            final long newValue = theChangeStorage.read(STRUCT.getNew()
                    .longField());
            final LongValueChange longValueChange = theChangeObjects
                    .longValueChange();
            longValueChange.update(theIndex, (LongField<?, ?>) theField,
                    oldValue, newValue);
            return longValueChange;
        }
    },

    /** The long global field change Type . */
    LONG_GLOBAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final long oldValue = theChangeStorage.read(STRUCT.getOld()
                    .longField());
            final long newValue = theChangeStorage.read(STRUCT.getNew()
                    .longField());
            final LongValueChange longValueChange = theChangeObjects
                    .longValueChange();
            longValueChange.update(theIndex, (LongGlobalField<?, ?>) theField,
                    oldValue, newValue);
            return longValueChange;

        }
    },

    /** The long optional field change Type . */
    LONG_OPTIONAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final long oldValue = theChangeStorage.read(STRUCT.getOld()
                    .longField());
            final long newValue = theChangeStorage.read(STRUCT.getNew()
                    .longField());
            final LongValueChange longValueChange = theChangeObjects
                    .longValueChange();
            longValueChange.update(theIndex, (LongOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return longValueChange;

        }
    },

    /** The object field change Type . */
    OBJECT_FIELD {
        @SuppressWarnings({ "rawtypes", "unchecked", "null" })
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex, final Field theField,
                final ValueChangeObjects theChangeObjects) {

            final Object oldValue = ((AbstractStorage) theChangeStorage)
                    .readObject(STRUCT.getOld().objectField());
            final Object newValue = ((AbstractStorage) theChangeStorage)
                    .readObject(STRUCT.getNew().objectField());

            final ObjectValueChange objectValueChange = theChangeObjects
                    .objectValueChange();

            objectValueChange.update(theIndex, theField, oldValue, newValue);

            return objectValueChange;

        }
    },

    /** The short field change Type . */
    SHORT_FIELD {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final short oldValue = theChangeStorage.read(STRUCT.getOld()
                    .shortField());
            final short newValue = theChangeStorage.read(STRUCT.getNew()
                    .shortField());
            final ShortValueChange shortValueChange = theChangeObjects
                    .shortValueChange();
            shortValueChange.update(theIndex, (ShortField<?, ?>) theField,
                    oldValue, newValue);
            return shortValueChange;
        }
    },

    /** The short global field change Type . */
    SHORT_GLOBAL {
        @SuppressWarnings("null")
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final short oldValue = theChangeStorage.read(STRUCT.getOld()
                    .shortField());
            final short newValue = theChangeStorage.read(STRUCT.getNew()
                    .shortField());
            final ShortValueChange shortValueChange = theChangeObjects
                    .shortValueChange();
            shortValueChange.update(theIndex,
                    (ShortGlobalField<?, ?>) theField, oldValue, newValue);
            return shortValueChange;
        }
    },

    /** The short optional change Type . */
    SHORT_OPTIONAL {
        @Override
        ValueChange<?> loadFromStorage(final Storage theChangeStorage,
                final int theIndex,
                @SuppressWarnings("rawtypes") final Field theField,
                final ValueChangeObjects theChangeObjects) {
            final short oldValue = theChangeStorage.read(STRUCT.getOld()
                    .shortField());
            final short newValue = theChangeStorage.read(STRUCT.getNew()
                    .shortField());
            final ShortValueChange shortValueChange = theChangeObjects
                    .shortValueChange();
            shortValueChange.update(theIndex, (ShortOptionalField<?, ?>) theField,
                    oldValue, newValue);
            return shortValueChange;
        }
    };

    /** Builds a ValueChange instance by reading the data from the "change" Storage. */
    abstract ValueChange<?> loadFromStorage(final Storage theChangeStorage,
            final int theIndex,
            @SuppressWarnings("rawtypes") final Field theField,
            ValueChangeObjects theChangeObjects);
}
