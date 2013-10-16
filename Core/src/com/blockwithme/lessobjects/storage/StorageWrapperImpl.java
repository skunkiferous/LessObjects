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
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.util.StructConstants.BYTE_MASK;
import static com.blockwithme.lessobjects.util.StructConstants.INDEX_FIELD_INDEX;
import static com.blockwithme.lessobjects.util.StructConstants.LIST_INITIAL_SIZE;
import static com.blockwithme.lessobjects.util.Util.intsToLong;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.UnionDiscriminatorValueMapping;
import com.blockwithme.lessobjects.beans.MultiDimensionalSupport;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.multidim.Point;
import com.blockwithme.lessobjects.util.Util;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.LongLongOpenHashMap;
import com.carrotsearch.hppc.LongObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectStack;
import com.carrotsearch.hppc.cursors.LongLongCursor;
import com.carrotsearch.hppc.cursors.LongObjectCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;

/**
 * Implementation of #{@link StorageWrapper}.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class StorageWrapperImpl implements StorageWrapper {

    /** The Storage Buffer class used to store the buffer data. */
    public static final class StorageBuffer {

        /** The primitive fields storage buffer. */
        @Nonnull
        private LongLongOpenHashMap buffer;

        /** The cycle. */
        private final long cycle;

        /** The Object field storage buffer. */
        @Nonnull
        private LongObjectOpenHashMap<Object> objectBuffer;

        /** Instantiates a new storage buffer. */
        StorageBuffer(final long theCycle) {
            buffer = new LongLongOpenHashMap();
            objectBuffer = new LongObjectOpenHashMap<>();
            cycle = theCycle;
        }

        /** The primitive fields storage buffer. */
        @SuppressWarnings("null")
        public LongLongOpenHashMap getBuffer() {
            return buffer;
        }

        /** The object fields storage buffer. */
        @SuppressWarnings("null")
        public LongObjectOpenHashMap<Object> getObjectBuffer() {
            return objectBuffer;
        }

        public void reset() {
            buffer = new LongLongOpenHashMap();
            objectBuffer = new LongObjectOpenHashMap<>();
        }
    }

    /** The actual storage. */
    @Nonnull
    private Storage actualStorage;

    /** The buffer storage. */
    @Nonnull
    private final ObjectArrayList<StorageBuffer> bufferList;

    /** The children storage wrappers. */
    private final LongObjectOpenHashMap<StorageWrapperImpl> childrenStorageWrappers;

    /** The cycle count */
    private long cycle;

    /** The unique index map. */
    private final IntObjectOpenHashMap<Field<?, ?>> uniqueIndexMap;

    /** Returns the field map, for a given Struct. */
    @SuppressWarnings("null")
    private static IntObjectOpenHashMap<Field<?, ?>> fieldMap(
            final Struct theStruct) {
        final IntObjectOpenHashMap<Field<?, ?>> theUniqueIndexMap = new IntObjectOpenHashMap<>();
        final Struct[] oChildren = theStruct.allOptionalChildren();
        final List<Field<?, ?>> allStorageFields = theStruct.allStorageFields();
        for (final Field<?, ?> field : allStorageFields) {
            theUniqueIndexMap.put(field.uniqueIndex(), field);
        }
        for (final Struct oChild : oChildren) {
            theUniqueIndexMap.putAll(fieldMap(oChild));
        }
        return theUniqueIndexMap;
    }

    /** Instantiates a new storage wrapper.
     *
     * @param theStorage the original storage instance that is being wrapped.
     */
    @SuppressWarnings("null")
    public StorageWrapperImpl(final Storage theStorage, final long theCycle) {
        checkArgument(theCycle != 0, "Invalid cycle number !");
        checkArgument(theStorage != null);
        cycle = theCycle;
        actualStorage = theStorage;
        bufferList = new ObjectStack<>();
        bufferList.add(new StorageBuffer(cycle));
        childrenStorageWrappers = new LongObjectOpenHashMap<>();
        uniqueIndexMap = fieldMap(theStorage.struct());
    }

    /** Returns the current buffer */
    @SuppressWarnings("null")
    private StorageBuffer buffer() {
        if (cycle != 0) {
            return bufferList.get(bufferList.size() - 1);
        }
        throw new IllegalStateException(
                "No buffer layers in the current Storage Wrapper.");
    }

    /** Check if the value of the union descriptors "match" */
    @SuppressWarnings("null")
    private boolean checkUnionDescriptor(final Field<?, ?> theChild,
            final Struct theStruct) {
        final UnionDiscriminatorValueMapping[] udMap = theStruct
                .uDMapping(theChild);
        for (final UnionDiscriminatorValueMapping udField : udMap) {
            if (udField.value() != read(udField.field())) {
                return false;
            }
        }
        return true;
    }

    /** Writes the buffer to the storage */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    private void writeBuffer(final StorageBuffer theBuffer,
            final Storage theStorage) {
        final int[] ints = new int[2];
        for (final LongLongCursor cursor : theBuffer.getBuffer()) {
            Util.longToInts(cursor.key, ints);
            theStorage.selectStructure(ints[0]);
            final Field<?, ?> f = uniqueIndexMap.get(ints[1]);
            // ignore the field value if the union position doesn't match.
            if (checkUnionDescriptor(f, f.root())) {
                f.writeAnyLong(cursor.value, theStorage);
            }
        }

        final LongObjectOpenHashMap<Object> objectBuffer = theBuffer.objectBuffer;
        for (final LongObjectCursor<Object> cursor : objectBuffer) {
            Util.longToInts(cursor.key, ints);
            theStorage.selectStructure(ints[0]);
            final ObjectField f = (ObjectField) uniqueIndexMap.get(ints[1]);
            // ignore the field value if the union position doesn't match.
            if (checkUnionDescriptor(f, f.parent())) {
                f.writeAny(cursor.value, theStorage);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addLayer(final long theCycle) {
        for (int i = bufferList.size() - 1; i >= 0; i--) {
            checkArgument(bufferList.get(i).cycle != theCycle,
                    "Cycle number - " + theCycle
                            + " already present in the Storage Wrapper.");
        }
        bufferList.add(new StorageBuffer(theCycle));
        cycle = theCycle;
        for (final LongObjectCursor<StorageWrapperImpl> cursor : childrenStorageWrappers) {
            cursor.value.addLayer(theCycle);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ChangeListenerSupport changeListenerSupport() {
        return actualStorage.changeListenerSupport();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void checkAccess(final Field<?, ?> theChild) {
        final Struct struct = actualStorage.struct();
        checkNotNull(theChild);
        checkState(theChild.compiled(), "The field " + theChild.name()
                + " is not a compiled field. ");
        checkState(
                uniqueIndexMap.get(theChild.uniqueIndex()).name()
                        .equals(theChild.name()),
                "The field " + theChild.name()
                        + " does not belong to the struct which is backed "
                        + "by the current storage!");
        checkState(checkUnionDescriptor(theChild, struct), "Cannot read/write "
                + theChild.name() + " Union position is not set appropriately.");
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void clear() {
        final List<Field<?, ?>> allStorageFields = actualStorage.struct()
                .allStorageFields();
        final StorageBuffer buffer = buffer();
        for (final Field<?, ?> field : allStorageFields) {
            field.clear(actualStorage, buffer);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void clear(final Field<?, ?> theField) {
        theField.clear(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void clearChild(final Struct theChild) {
        final List<Field<?, ?>> allStorageFields = theChild.allStorageFields();
        final StorageBuffer buffer = buffer();
        for (final Field<?, ?> field : allStorageFields) {
            field.clear(actualStorage, buffer);
        }
    }

    /** This operation is not supported on Storage wrapper */
    @Override
    public Storage copy() {
        throw new UnsupportedOperationException();
    }

    /** This operation is not supported on Storage wrapper */
    @Override
    public void copyStorage(final Storage theOther) {
        throw new UnsupportedOperationException();
    }

    /** This operation is not supported on Storage wrapper */
    @Override
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Storage createOrClearList(final Struct theListChild) {
        return createOrClearList(theListChild, LIST_INITIAL_SIZE);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage createOrClearList(final Struct theListChild,
            final int theInitialSize) {
        final Storage list = actualStorage.createOrClearList(theListChild);
        // start from the latest element.
        StorageWrapperImpl wrapper = null;
        for (final ObjectCursor<StorageBuffer> cursor : bufferList) {
            final long currentCycle = cursor.value.cycle;
            if (wrapper == null) {
                wrapper = new StorageWrapperImpl(list, currentCycle);
            } else {
                wrapper.addLayer(currentCycle);
            }
        }
        final long key = intsToLong(actualStorage.getSelectedStructure(),
                theListChild.storageKey().getKey());
        childrenStorageWrappers.put(key, wrapper);
        return wrapper;
    }

    /** {@inheritDoc} */
    @Override
    public long currentCycle() {
        return cycle;
    }

    /** This operation is not supported on Storage wrapper */
    @Override
    public void enableTransactions(final boolean theEnableFlag) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public int getCapacity() {
        return actualStorage.getCapacity();
    }

    /** The children storage wrappers. */
    @SuppressWarnings("null")
    public LongObjectOpenHashMap<StorageWrapperImpl> getChildrenStorageWrappers() {
        return childrenStorageWrappers;
    }

    /** {@inheritDoc} */
    @Override
    public Point getSelectedPoint() {
        return actualStorage.getSelectedPoint();
    }

    /** {@inheritDoc} */
    @Override
    public int getSelectedStructure() {
        return actualStorage.getSelectedStructure();
    }

    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return actualStorage.getSize();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    @Nullable
    public Storage list(final Struct theListChild) {
        final long key = intsToLong(actualStorage.getSelectedStructure(),
                theListChild.storageKey().getKey());
        if (childrenStorageWrappers.containsKey(key)) {
            return childrenStorageWrappers.lget();
        }
        final Storage list = actualStorage.list(theListChild);
        if (list != null) {
            StorageWrapperImpl wrapper = null;
            for (final ObjectCursor<StorageBuffer> cursor : bufferList) {
                final long currentCycle = cursor.value.cycle;
                if (wrapper == null) {
                    wrapper = new StorageWrapperImpl(list, currentCycle);
                } else {
                    wrapper.addLayer(currentCycle);
                }
            }
            childrenStorageWrappers.put(key, wrapper);
            return wrapper;
        }
        return null;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage mergeLayer(final long theCycle) {
        StorageBuffer sBuffer = null;
        int index = -1;
        final long currentCycle = cycle;
        // start from the latest element.
        for (int i = bufferList.size() - 1; i >= 0; i--) {
            final StorageBuffer b = bufferList.get(i);
            if (b.cycle == theCycle) {
                sBuffer = b;
                index = i;
            }
        }
        checkArgument(sBuffer != null, "Could not find cycle - " + theCycle);
        Storage result;
        if (index == 0) {
            writeBuffer(sBuffer, actualStorage);
            cycle = 0;
            result = actualStorage;
        } else {
            cycle = bufferList.get(index - 1).cycle;
            writeBuffer(bufferList.remove(index), this);
            if (cycle != index) {
                cycle = currentCycle;
            }
            result = this;
        }
        for (final LongObjectCursor<StorageWrapperImpl> cursor : childrenStorageWrappers) {
            cursor.value.mergeLayer(theCycle);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public MultiDimensionalSupport multiDimensionalSupport() {
        return actualStorage.multiDimensionalSupport();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public boolean read(final BooleanField<?, ?> theField) {
        checkAccess(theField);
        return theField.readBoolean(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public byte read(final ByteField<?, ?> theField) {
        checkAccess(theField);
        return theField.readByte(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public char read(final CharField<?, ?> theField) {
        checkAccess(theField);
        return theField.readChar(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public double read(final DoubleField<?, ?> theField) {
        checkAccess(theField);
        return theField.readDouble(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public float read(final FloatField<?, ?> theField) {
        checkAccess(theField);
        return theField.readFloat(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public int read(final IntField<?, ?> theField) {
        checkAccess(theField);
        return theField.readInt(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public long read(final LongField<?, ?> theField) {
        checkAccess(theField);
        return theField.readLong(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    @Nullable
    public <E, F extends ObjectField<E, F>> E read(
            final ObjectField<E, F> theField) {
        checkAccess(theField);
        return theField.readObject(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public short read(final ShortField<?, ?> theField) {
        checkAccess(theField);
        return theField.readShort(actualStorage, buffer());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void replaceStorage(final long theCycle, final Storage theStorage) {
        StorageBuffer sBuffer = null;
        int index = -1;
        // start from the latest element.
        for (int i = bufferList.size() - 1; i >= 0; i--) {
            final StorageBuffer b = bufferList.get(i);
            if (b.cycle == theCycle) {
                sBuffer = b;
                index = i;
            }
        }
        checkArgument(sBuffer != null, "Could not find cycle -" + theCycle);
        // start from the first element and remove all child cycles.
        for (int i = 0; i <= index - 1; i++) {
            bufferList.remove(i);
        }
        sBuffer.reset();
        actualStorage = theStorage;
    }

    /** This operation is not supported on Storage wrapper.*/
    @Override
    public void resizeStorage(final int theNewCapacity) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Struct rootStruct() {
        return actualStorage.rootStruct();
    }

    /** {@inheritDoc} */
    @Override
    public boolean selectPoint(final Point thePoint) {
        return actualStorage.selectPoint(thePoint);
    }

    /** {@inheritDoc} */
    @Override
    public boolean selectStructure(final int theStructure) {
        return actualStorage.selectStructure(theStructure);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void selectUnionPosition(final Field<?, ?> theField) {
        selectUnionPosition(theField.parent(), theField.localIndex());
    }

    /** {@inheritDoc} */
    @Override
    public void selectUnionPosition(final Struct theStruct,
            final int thePosition) {
        // Get localIndex of the currently selected child.
        final CharField<?, ?> theField = (CharField<?, ?>) theStruct
                .structFields()[INDEX_FIELD_INDEX];
        if (theField != null) {
            final char localFieldIndex = read(theField);
            final int localIndex = localFieldIndex & BYTE_MASK;
            if (localIndex != thePosition) {
                clearChild(theStruct);
            }
            write(theField, (char) thePosition);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Struct struct() {
        return actualStorage.struct();
    }

    /** This operation is not supported on Storage wrapper. */
    @Override
    public TransactionManager transactionManager() {
        throw new UnsupportedOperationException(
                "Transaction processing not supported on wrappers. ");
    }

    /** {@inheritDoc} */
    @Override
    public boolean transactionsEnabled() {
        return actualStorage.transactionsEnabled();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final BooleanField<?, ?> theField, final boolean theValue) {
        checkAccess(theField);
        theField.writeBoolean(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final ByteField<?, ?> theField, final byte theValue) {
        checkAccess(theField);
        theField.writeByte(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final CharField<?, ?> theField, final char theValue) {
        checkAccess(theField);
        theField.writeChar(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final DoubleField<?, ?> theField, final double theValue) {
        checkAccess(theField);
        theField.writeDouble(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final FloatField<?, ?> theField, final float theValue) {
        checkAccess(theField);
        theField.writeFloat(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final IntField<?, ?> theField, final int theValue) {
        checkAccess(theField);
        theField.writeInt(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final LongField<?, ?> theField, final long theValue) {
        checkAccess(theField);
        theField.writeLong(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public <E, F extends ObjectField<E, F>> void write(
            final ObjectField<E, F> theField, @Nullable final E theValue) {
        checkAccess(theField);
        theField.writeObject(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final ShortField<?, ?> theField, final short theValue) {
        checkAccess(theField);
        theField.writeShort(actualStorage, buffer(), theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage writeAllLayers() {
        return writeLayer(cycle);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public Storage writeLayer(final long theCycle) {
        checkArgument(theCycle != 0);
        StorageBuffer sBuffer = null;
        int index = -1;
        // start from the latest element.
        for (int i = bufferList.size() - 1; i >= 0; i--) {
            final StorageBuffer b = bufferList.get(i);
            if (b.cycle == theCycle) {
                sBuffer = b;
                index = i;
            }
        }
        checkArgument(sBuffer != null, "Could not find cycle -" + theCycle);
        // start from the first element.
        for (int i = 0; i <= index; i++) {
            writeBuffer(bufferList.get(i), actualStorage);
            bufferList.remove(i);
        }
        if (bufferList.size() == 0) {
            cycle = 0;
            return actualStorage;
        }
        cycle = bufferList.get(index + 1).cycle;
        for (final LongObjectCursor<StorageWrapperImpl> cursor : childrenStorageWrappers) {
            cursor.value.writeLayer(theCycle);
        }
        return this;
    }
}
