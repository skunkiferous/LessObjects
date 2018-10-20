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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.SimpleSizingStrategy;
import com.blockwithme.lessobjects.storage.SparseStorage;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageBuilder;
import com.blockwithme.lessobjects.storage.StorageSizingStrategy;
import com.blockwithme.lessobjects.util.StructConstants;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntStack;

/**
 *  A Sparse is an automatically resizable storage that can grow or shrink based on the number
 *  of elements stored. The storage grows when selectStructure method is invoked but space is not
 *  available in the storage, and shrinks when clear method is invoked. Re-Sizing strategy for a
 *  sparse storage can be defined by passing an instance of StorageSizingStrategy.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class PackedSparseStorage extends PackedCompositeStorage implements
        SparseStorage {

    /** The index map. */
    private transient IntIntOpenHashMap indexMap;

    /** The last inserted. */
    // TODO Do we really need a map AND a stack?
    private transient IntStack lastInserted;

    /** The selected flag. */
    private boolean isSelected;

    /** The primary index. */
    private int primaryIndex;

    /** The Strategy. */
    private transient StorageSizingStrategy strategy = new SimpleSizingStrategy();

    /** Constructor */
    PackedSparseStorage(final Struct theStruct, final int theInitialCapacity,
            @Nullable final Storage theBaseStorage,
            final boolean isTransactional, final Arity theArity) {
        super(theStruct, theInitialCapacity, theBaseStorage, isTransactional,
                theArity);
        indexMap = new IntIntOpenHashMap();
        lastInserted = new IntStack();
    }

    /** Constructor */
    public PackedSparseStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
        indexMap = theBuilder.getIndexMap();
        isSelected = theBuilder.isSelected();
        lastInserted = theBuilder.getLastInserted();
        primaryIndex = theBuilder.getPrimaryIndex();

    }

    /** Constructor */
    public PackedSparseStorage(final Struct theStruct,
            final int theInitialCapacity, final boolean isTransactional,
            final Arity theArity) {
        this(theStruct, theInitialCapacity, (Storage) null, isTransactional,
                theArity);
    }

    /** Allocates space for a new element. */
    private int selectNew(final int theStructure) {
        final int secondaryIndex = indexMap.size() > 0 ? indexMap
                .get(lastInserted.peek()) + 1 : 0;
        if (secondaryIndex >= capacity) {
            if (secondaryIndex <= strategy.max()) {
                final int growBy = strategy.grow(capacity, 1);
                resizeStorage(capacity + growBy);
            } else {
                throw new IllegalStateException(
                        "Storage cannot be contain more than " + strategy.max()
                                + " elements");
            }
        }
        indexMap.put(theStructure, secondaryIndex);
        lastInserted.push(theStructure);
        return secondaryIndex;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    protected Storage blankCopy() {
        return new PackedSparseStorage(struct, capacity,
                isSecondary ? baseStorage : null, !transactionsDisabled, arity);
    }

    /** {@inheritDoc} */
    @Override
    public void checkAccess(final Field<?, ?> theField) {
        if (!isSelected) {
            throw new IllegalStateException(
                    "Error while readin sparse storage, "
                            + "element not selected");
        }
        super.checkAccess(theField);
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        assert isSelected;
        super.clear();

        final int lastIndex = lastInserted.pop();
        final int lastStructure = indexMap.get(lastIndex);
        if (structure < lastStructure) {
            long bits = structSize;
            long offsetInBits = 0;
            final long offset = (lastStructure - structure) * bits;
            while (bits > StructConstants.LONG_BITS) {
                write(offsetInBits, StructConstants.LONG_BITS,
                        read(offset + offsetInBits, StructConstants.LONG_BITS));
                bits -= StructConstants.LONG_BITS;
                offsetInBits += StructConstants.LONG_BITS;
            }
            write(offsetInBits, (int) bits,
                    read(offset + offsetInBits, (int) bits));
            indexMap.put(lastIndex, structure);
        }
        indexMap.remove(primaryIndex);
        final int shrinkSize = strategy.shrink(capacity, indexMap.size());
        if (shrinkSize > 0) {
            resizeStorage(capacity - shrinkSize);
        }
        isSelected = false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final int theIndex) {
        return indexMap.containsKey(theIndex);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {
        if (isDifferent(theOther)) {
            super.copyStorage(theOther, theSchemaMigrator);
        } else {
            super.copyStorage(theOther, theSchemaMigrator);
            final PackedSparseStorage otheStorage = (PackedSparseStorage) theOther;
            otheStorage.indexMap.putAll(indexMap);
            otheStorage.lastInserted.pushAll(lastInserted);
            otheStorage.primaryIndex = primaryIndex;
            otheStorage.isSelected = isSelected;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int currentSize() {
        return indexMap.size();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public StorageBuilder getBuilder() {
        final StorageBuilder builder = super.getBuilder();
        builder.setIndexMap(indexMap);
        builder.setLastInserted(lastInserted);
        builder.setPrimaryIndex(primaryIndex);
        builder.setSelected(isSelected);
        return builder;
    }

    /** {@inheritDoc} */
    @Override
    public boolean selectStructure(final int theStructure) {
        int secondaryIndex;
        if (indexMap.containsKey(theStructure)) {
            secondaryIndex = indexMap.lget();
        } else {
            secondaryIndex = selectNew(theStructure);
        }
        primaryIndex = theStructure;
        structure = secondaryIndex;
        structurePosition = secondaryIndex * structSize;
        structureIndex = (int) (structurePosition / StructConstants.LONG_BITS);
        isSelected = true;
        return true;
    }
}
