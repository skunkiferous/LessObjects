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
package com.blockwithme.lessobjects.storage.packed;

import static com.blockwithme.lessobjects.util.StructConstants.LONG_BITS;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.BaseLongStorage;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageBuilder;

//CHECKSTYLE.OFF: IllegalType
/**
 * Implements the storage wrapper for the packed compiler.
 *
 * @author monster, tarung
 */
@ParametersAreNonnullByDefault
public class PackedStorage extends BaseLongStorage {

    /** The init defaults flag. */
    private final boolean initDefaults;

    /** Computes the array size, given a structure size, and a number of
     * structure. */
    public static long[] createArray(final int theCount,
            final long theStructSize) {
        final long bits = theStructSize * theCount;
        final long rest = bits % LONG_BITS;
        final long padding = rest == 0 ? 0 : LONG_BITS - rest;
        return new long[(int) ((bits + padding) / LONG_BITS)];
    }

    /** Constructor */
    private PackedStorage(final Struct theStruct, final long[] theArray,
            final long theStructSize, final int theCapacity,
            @Nullable final Storage theBaseStorage,
            final boolean isTransactional, final boolean theInitDefaultsFlag,
            final Arity theArity) {

        super(theStruct, theArray, theStructSize, theCapacity, theBaseStorage,
                isTransactional, theArity);
        initDefaults = theInitDefaultsFlag;
    }

    /** Constructor which creates the array itself */
    PackedStorage(final Struct theStruct, final int theStorageSize,
            @Nullable final Storage theBaseStorage,
            final boolean isTransactional, final boolean theInitDefaultsFlag,
            final Arity theArity) {
        this(theStruct, createArray(theStorageSize, theStruct.bits()),
                theStruct.bits(), theStorageSize, theBaseStorage,
                isTransactional, theInitDefaultsFlag, theArity);
        if (theInitDefaultsFlag) {
            // initDefaults();
        }
    }

    /** Constructor uses a builder object to create the storage. */
    public PackedStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
        initDefaults = false;
    }

    /** Constructor which creates the array itself */
    public PackedStorage(final Struct theStruct, final int theStorageSize,
            @Nullable final Storage theBaseStorage,
            final boolean isTransactional, final Arity theArity) {
        this(theStruct, theStorageSize, theBaseStorage, isTransactional, true,
                theArity);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    protected Storage blankCopy() {
        return new PackedStorage(struct, capacity, isSecondary ? baseStorage
                : null, transactionsEnabled(), false, arity);
    }

    /** {@inheritDoc} */
    @Override
    protected void clearAllChildren() {
        // do nothing.
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    protected AbstractStorage getSingleStorage(final Struct theGlobalStruct) {
        return new PackedStorage(theGlobalStruct, 1, this,
                !transactionsDisabled, Arity.ONE_D);
    }

    /** Reads some bits, at the give position. */
    protected long read(long theOffsetInBits, final int theBitsCount) {

        theOffsetInBits += structurePosition;
        final int arrayIndex = (int) (theOffsetInBits / LONG_BITS);
        final int bitIndex = (int) (theOffsetInBits % LONG_BITS);
        final int rest = LONG_BITS - bitIndex;
        final long valueMask = mask(Math.min(theBitsCount, rest));
        final long arrayValue = array[arrayIndex];
        final long shiftedArrayValue = arrayValue >>> bitIndex;
        long value = shiftedArrayValue & valueMask;
        if (theBitsCount > rest) {
            final long arrayValue2 = array[arrayIndex + 1];
            final long valueMask2 = mask(theBitsCount - rest);
            final long shiftedArrayValue2 = arrayValue2 & valueMask2;
            value |= shiftedArrayValue2 << rest;
        }
        return value;

    }

    /** Writes some bits, at the give position. */
    protected void write(long theOffsetInBits, final int theBitsCount,
            final long theValue) {

        theOffsetInBits += structurePosition;
        final int arrayIndex = (int) (theOffsetInBits / LONG_BITS);
        final int bitIndex = (int) (theOffsetInBits % LONG_BITS);
        final int rest = LONG_BITS - bitIndex;
        final long valueMask = mask(Math.min(theBitsCount, rest));
        final long beforeValue = array[arrayIndex];
        final long shiftedMask = valueMask << bitIndex;
        final long maskedBeforeValue = beforeValue & ~shiftedMask;
        final long maskedValue = theValue & valueMask;
        final long shiftedValue = maskedValue << bitIndex;
        array[arrayIndex] = maskedBeforeValue | shiftedValue;
        if (theBitsCount > rest) {
            final long beforeValue2 = array[arrayIndex + 1];
            final long valueMask2 = mask(theBitsCount - rest);
            final long maskedBeforeValue2 = beforeValue2 & ~valueMask2;
            final long shiftedValue2 = theValue >>> rest;
            final long newValue2 = shiftedValue2 & valueMask2;
            array[arrayIndex + 1] = maskedBeforeValue2 | newValue2;
        }

    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {
        if (isDifferent(theOther)) {
            super.copyStorage(theOther, theSchemaMigrator);
        } else {
            final PackedStorage otherStorage = (PackedStorage) theOther;
            System.arraycopy(array, 0, otherStorage.array, 0, array.length);
            if (objectStorageObjects != null && objectStorageObjects.length > 0) {
                copyObjectStorage(struct(), objectStorageObjects,
                        otherStorage.objectStorageObjects);
            }
            otherStorage.selectStructure(getSelectedStructure());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Storage createOrClearList(final Struct theListChild) {
        throw new IllegalStateException(
                "This storage does not have any list-type children");
    }

    /** {@inheritDoc} */
    @Override
    public Storage createOrClearList(final Struct theListChild,
            final int theInitialSize) {
        throw new IllegalStateException(
                "This storage does not have any list-type children");

    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public Storage list(final Struct theListChild) {
        throw new IllegalStateException(
                "This storage does not have any list-type children");
    }

    /** {@inheritDoc} */
    @Override
    public <F extends BooleanField<?, F>> boolean readBoolean(final F theField) {
        final long offsetInBits = structurePosition + theField.offset();
        final int arrayIndex = (int) (offsetInBits / LONG_BITS);
        final int bitIndex = (int) (offsetInBits % LONG_BITS);
        final long mask = 1L << bitIndex;
        return (array[arrayIndex] & mask) != 0;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ByteField<?, F>> byte readByte(final F theField) {
        return (byte) read(theField.offset(), theField.bits());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends CharField<?, F>> char readChar(final F theField) {
        return (char) read(theField.offset(), theField.bits());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends DoubleField<?, F>> double readDouble(final F theField) {
        return Double
                .longBitsToDouble(read(theField.offset(), theField.bits()));
    }

    /** {@inheritDoc} */
    @Override
    public <F extends FloatField<?, F>> float readFloat(final F theField) {
        return Float.intBitsToFloat((int) read(theField.offset(),
                theField.bits()));
    }

    /** {@inheritDoc} */
    @Override
    public <F extends IntField<?, F>> int readInt(final F theField) {
        return (int) read(theField.offset(), theField.bits());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends LongField<?, F>> long readLong(final F theField) {
        return read(theField.offset(), theField.bits());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ShortField<?, F>> short readShort(final F theField) {
        return (short) read(theField.offset(), theField.bits());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public final void resizeStorage(final int theNewCapacity) {
        if (capacity != theNewCapacity) {
            final long[] newArray = createArray(theNewCapacity, struct.bits());
            final int len = Math.min(array.length, newArray.length);
            System.arraycopy(array, 0, newArray, 0, len);
            Object[] newObjectStore;
            final int oldCapacity = capacity;
            array = newArray;
            capacity = theNewCapacity;
            final int currentIndex = getSelectedStructure();

            if (objectStorageObjects.length > 0) {
                newObjectStore = new Object[objectStorageObjects.length];
            } else {
                newObjectStore = new Object[0];
            }

            reInitObjectStorage(struct(), newObjectStore);
            copyObjectStorage(struct(), objectStorageObjects, newObjectStore);
            objectStorageObjects = newObjectStore;

            if (capacity > oldCapacity && initDefaults) {
                for (int i = oldCapacity; i < capacity; i++) {
                    selectStructure(i);
                    clear();
                }
            }
            if (currentIndex < theNewCapacity) {
                selectStructure(currentIndex);
            } else {
                selectStructure(theNewCapacity - 1);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public <F extends BooleanField<?, F>> boolean writeImpl(final F theField,
            final boolean theValue) {
        final long offsetInBits = structurePosition + theField.offset();
        final int arrayIndex = (int) (offsetInBits / LONG_BITS);
        final int bitIndex = (int) (offsetInBits % LONG_BITS);
        final long mask = 1L << bitIndex;
        final boolean oldValue = (array[arrayIndex] & mask) != 0;
        if (theValue) {
            array[arrayIndex] |= mask;
        } else {
            array[arrayIndex] &= ~mask;
        }
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ByteField<?, F>> byte writeImpl(final F theField,
            final byte theValue) {
        final byte oldValue = (byte) read(theField.offset(), theField.bits());
        write(theField.offset(), theField.bits(), theValue);
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends CharField<?, F>> char writeImpl(final F theField,
            final char theValue) {
        final char oldValue = (char) read(theField.offset(), theField.bits());
        write(theField.offset(), theField.bits(), theValue);
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends DoubleField<?, F>> double writeImpl(final F theField,
            final double theValue) {
        final double oldValue = Double.longBitsToDouble(read(theField.offset(),
                theField.bits()));
        write(theField.offset(), theField.bits(),
                Double.doubleToRawLongBits(theValue));
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends FloatField<?, F>> float writeImpl(final F theField,
            final float theValue) {
        final float oldValue = Float.intBitsToFloat((int) read(
                theField.offset(), theField.bits()));
        write(theField.offset(), theField.bits(),
                Float.floatToRawIntBits(theValue));
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends IntField<?, F>> int writeImpl(final F theField,
            final int theValue) {
        final int oldValue = (int) read(theField.offset(), theField.bits());
        write(theField.offset(), theField.bits(), theValue);
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends LongField<?, F>> long writeImpl(final F theField,
            final long theValue) {
        final long oldValue = read(theField.offset(), theField.bits());
        write(theField.offset(), theField.bits(), theValue);
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ShortField<?, F>> short writeImpl(final F theField,
            final short theValue) {
        final short oldValue = (short) read(theField.offset(), theField.bits());
        write(theField.offset(), theField.bits(), theValue);
        return oldValue;
    }
}
