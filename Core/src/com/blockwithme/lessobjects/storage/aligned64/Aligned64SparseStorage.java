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

import static com.google.common.base.Preconditions.checkState;

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
 * Implements the storage for the aligned 64 compiler which is Automatically
 * Resizable.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Aligned64SparseStorage extends Aligned64CompositeStorage implements
        SparseStorage {

    /** The index map. */
    private transient IntIntOpenHashMap indexMap;

    /** The last inserted. */
    // TODO Do we really need a map AND a stack?
    private transient IntStack lastInserted;

    /** The primary index. */
    private int primaryIndex;

    /** The is selected. */
    private boolean selected;

    /** The Resizer. */
    private transient StorageSizingStrategy strategy = new SimpleSizingStrategy();

    /** Instantiates a new aligned64 auto resizable storage. */
    Aligned64SparseStorage(final Struct theStruct,
            final int theInitialCapacity,
            @Nullable final Storage theBaseStorage,
            final boolean theTransactionalFlag, final Arity theArity) {
        super(theStruct, theInitialCapacity, theBaseStorage,
                theTransactionalFlag, theArity);
        indexMap = new IntIntOpenHashMap();
        lastInserted = new IntStack();
    }

    /** Constructor */
    public Aligned64SparseStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
        indexMap = theBuilder.getIndexMap();
        selected = theBuilder.isSelected();
        lastInserted = theBuilder.getLastInserted();
        primaryIndex = theBuilder.getPrimaryIndex();
    }

    /** Instantiates a new aligned64 auto resizable storage. */
    public Aligned64SparseStorage(final Struct theStruct,
            final int theInitialCapacity, final boolean isTransactional,
            final Arity theArity) {
        this(theStruct, theInitialCapacity, null, isTransactional, theArity);
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
    @Override
    protected Storage blankCopy() {
        @SuppressWarnings("null")
        final Aligned64SparseStorage result = new Aligned64SparseStorage(
                struct, capacity, isSecondary ? baseStorage : null,
                !transactionsDisabled, arity);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void checkAccess(final Field<?, ?> theField) {
        checkState(selected,
                "Error while reading sparse storage, element not selected");
        super.checkAccess(theField);
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {

        assert selected;

        super.clear();

        final int lastIndex = lastInserted.pop();
        final int lastStructure = indexMap.get(lastIndex);
        if (structure < lastStructure) {
            // replacing Values from last index into the current slots.
            final int end = structureIndex
                    + (int) (structSize / StructConstants.LONG_BITS);
            final long lastIndexPosition = lastStructure * structSize;
            final int lastStructureIndex = (int) (lastIndexPosition / StructConstants.LONG_BITS);
            for (int i = structureIndex, j = 0; i < end; i++, j++) {
                array[i] = array[lastStructureIndex + j];
                array[lastStructureIndex + j] = 0;
            }
            indexMap.put(lastIndex, structure);
        }
        indexMap.remove(primaryIndex);
        final int shrinkSize = strategy.shrink(capacity, indexMap.size());
        if (shrinkSize > 0) {
            resizeStorage(capacity - shrinkSize);
        }
        selected = false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final int theStructure) {
        return indexMap.containsKey(theStructure);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {
        if (isDifferent(theOther)) {
            // Call AbstractStorage.copyStorage method.
            super.copyStorage(theOther, theSchemaMigrator);
        } else {
            // calls Aligned64Storage.copyStorage first then copies
            // the index maps etc..
            super.copyStorage(theOther, theSchemaMigrator);
            final Aligned64SparseStorage otheStorage = (Aligned64SparseStorage) theOther;
            otheStorage.indexMap.putAll(indexMap);
            otheStorage.lastInserted.pushAll(lastInserted);
            otheStorage.primaryIndex = primaryIndex;
            otheStorage.selected = selected;
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
        builder.setSelected(selected);
        return builder;
    }

    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return strategy.max();
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
        selected = true;
        return true;
    }
}
