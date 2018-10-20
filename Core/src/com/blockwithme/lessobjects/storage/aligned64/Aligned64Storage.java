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
package com.blockwithme.lessobjects.storage.aligned64;

import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_OBJ_ARR;

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
import com.blockwithme.lessobjects.util.StructConstants;

/**
 * Implements the storage wrapper for the aligned 64 compiler.
 *
 * TODO: This implementation should work, but it could be MUCH faster. We need
 * to take advantage of the fact that the position of a field within a long is
 * always the same, so is the bit shift and the mask, and that full-bits longs
 * and double need neither bit shift nor masking. This is most likely achieved
 * by extending the Field classes with additional attributes, which contain
 * pre-computed results.
 *
 * For a typical field type, like int, we do 3 methods call to the field class
 * on read, and 6 methods call on write (could be reduced to 4). And the only
 * data that we access is structureIndex and array. So moving the read/write
 * logic into the fields themselves, passing both structureIndex and array to
 * them, would reduce the number of calls to 1 in both case, which would speed
 * up things, in particular of that call could be made "final".
 *
 * @author monster, tarung
 */
// CHECKSTYLE.OFF: IllegalType
@ParametersAreNonnullByDefault
public class Aligned64Storage extends BaseLongStorage {

    /** The init defaults. */
    private final boolean initDefaults;

    /** Computes the array size, given a structure size, and a number of
     * structure. */
    public static long[] createArray(final int theCount, final long theSize) {

        final long adjustedSize = adjustStructSize(theSize);
        final long bits = adjustedSize * theCount;
        return new long[(int) (bits / StructConstants.LONG_BITS)];
    }

    /** The Actual constructor with private level access, also used by at the time of de-serialization. */
    private Aligned64Storage(final Struct theStruct, final long[] theArray,
            final long theSize, final int theCapacity,
            @Nullable final Storage theBaseStorage,
            final boolean theTransactionalFlag,
            final boolean theDefaultInitializationFlag, final Arity theArity) {

        super(theStruct, theArray, adjustStructSize(theSize), theCapacity,
                theBaseStorage, theTransactionalFlag, theArity);
        initDefaults = theDefaultInitializationFlag;
    }

    /** Constructor which creates the array itself, and initializes default
     * values if needed */
    protected Aligned64Storage(final Struct theStruct,
            final int theStorageSize, @Nullable final Storage theBaseStorage,
            final boolean isTransactional,
            final boolean theDefaultInitializationFlag, final Arity theArity) {

        this(theStruct, createArray(theStorageSize, theStruct.bits()),
                theStruct.bits(), theStorageSize, theBaseStorage,
                isTransactional, theDefaultInitializationFlag, theArity);
        if (theDefaultInitializationFlag) {
            // initDefaults();
        }
    }

    /** Constructor uses a builder object to create the storage. */
    public Aligned64Storage(final StorageBuilder theBuilder) {

        super(theBuilder);
        initDefaults = false;
    }

    /** Constructor which creates the array itself */
    public Aligned64Storage(final Struct theStruct, final int theStorageSize,
            @Nullable final Storage theParent, final boolean isTransactional,
            final Arity theArity) {

        this(theStruct, theStorageSize, theParent, isTransactional, true,
                theArity);
    }

    /** {@inheritDoc} */
    @Override
    protected Storage blankCopy() {

        @SuppressWarnings("null")
        final Aligned64Storage result = new Aligned64Storage(struct, capacity,
                isSecondary ? baseStorage : null, transactionsEnabled(), false,
                arity);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    protected void clearAllChildren() {
        // do nothing
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    protected AbstractStorage getSingleStorage(final Struct theGlobalStruct) {
        return new Aligned64Storage(theGlobalStruct, 1, this,
                !transactionsDisabled, Arity.ONE_D);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {
        if (isDifferent(theOther)) {
            // call AbstractStorage.copyStorage method.
            super.copyStorage(theOther, theSchemaMigrator);
        } else {
            final Aligned64Storage otherStorage = (Aligned64Storage) theOther;
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
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return (array[arrayIndex] & theField.mask64()) != 0;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ByteField<?, F>> byte readByte(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return (byte) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends CharField<?, F>> char readChar(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return (char) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends DoubleField<?, F>> double readDouble(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return Double.longBitsToDouble(array[arrayIndex]);
    }

    /** {@inheritDoc} */
    @Override
    public <F extends FloatField<?, F>> float readFloat(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return Float.intBitsToFloat((int) ((array[arrayIndex] & theField
                .mask64()) >> theField.offsetMod64()));
    }

    /** {@inheritDoc} */
    @Override
    public <F extends IntField<?, F>> int readInt(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return (int) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
    }

    /** {@inheritDoc} */
    @Override
    public <F extends LongField<?, F>> long readLong(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return (array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64();
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ShortField<?, F>> short readShort(final F theField) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        return (short) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public void resizeStorage(final int theNewCapacity) {
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
                newObjectStore = EMPTY_OBJ_ARR;
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
                // select last element. here
                selectStructure(theNewCapacity - 1);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public <F extends BooleanField<?, F>> boolean writeImpl(final F theField,
            final boolean theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final boolean oldValue = (array[arrayIndex] & theField.mask64()) != 0;
        if (theValue) {
            array[arrayIndex] |= theField.mask64();
        } else {
            array[arrayIndex] &= theField.negMask64();
        }
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ByteField<?, F>> byte writeImpl(final F theField,
            final byte theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final byte oldValue = (byte) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
        final long tmp = array[arrayIndex] & theField.negMask64();
        array[arrayIndex] = tmp | (long) theValue << theField.offsetMod64()
                & theField.mask64();
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends CharField<?, F>> char writeImpl(final F theField,
            final char theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final char oldValue = (char) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
        final long tmp = array[arrayIndex] & theField.negMask64();
        array[arrayIndex] = tmp | (long) theValue << theField.offsetMod64()
                & theField.mask64();
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends DoubleField<?, F>> double writeImpl(final F theField,
            final double theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final double oldValue = Double.longBitsToDouble(array[arrayIndex]);
        array[arrayIndex] = Double.doubleToRawLongBits(theValue);
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends FloatField<?, F>> float writeImpl(final F theField,
            final float theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final float oldValue = Float
                .intBitsToFloat((int) ((array[arrayIndex] & theField.mask64()) >> theField
                        .offsetMod64()));
        final long tmp = array[arrayIndex] & theField.negMask64();
        array[arrayIndex] = tmp
                | (long) Float.floatToRawIntBits(theValue) << theField
                        .offsetMod64() & theField.mask64();
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends IntField<?, F>> int writeImpl(final F theField,
            final int theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final int oldValue = (int) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
        final long tmp = array[arrayIndex] & theField.negMask64();
        array[arrayIndex] = tmp | (long) theValue << theField.offsetMod64()
                & theField.mask64();
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends LongField<?, F>> long writeImpl(final F theField,
            final long theValue) {
        if (theField.fullBits()) {
            final int arrayIndex = structureIndex + theField.offsetDiv64();
            final long oldValue = array[arrayIndex];
            array[arrayIndex] = theValue;
            return oldValue;
        }
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final long oldValue = (int) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
        final long tmp = array[arrayIndex] & theField.negMask64();
        array[arrayIndex] = tmp | theValue << theField.offsetMod64()
                & theField.mask64();
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public <F extends ShortField<?, F>> short writeImpl(final F theField,
            final short theValue) {
        final int arrayIndex = structureIndex + theField.offsetDiv64();
        final short oldValue = (short) ((array[arrayIndex] & theField.mask64()) >> theField
                .offsetMod64());
        final long tmp = array[arrayIndex] & theField.negMask64();
        array[arrayIndex] = tmp | (long) theValue << theField.offsetMod64()
                & theField.mask64();
        return oldValue;
    }
}
