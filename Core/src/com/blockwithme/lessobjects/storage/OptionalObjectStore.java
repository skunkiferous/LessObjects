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

import static com.google.common.base.Preconditions.checkState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Child;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.StorageKey;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * The Class Optional Object Store simplifies dealing with optional fields.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class OptionalObjectStore {

    /** The Interface StorageCreator. */
    public interface StorageCreator {

        public SparseStorage createSparseStorage(final Struct theStruct,
                final int theInitialCapacity,
                @Nullable final Storage theBaseStorage,
                final boolean theTransactionalFlag);

        public Storage createStorage(final Struct theStruct,
                final int theInitialCapacity,
                @Nullable final Storage theBaseStorage,
                final boolean theTransactionalFlag);

    }

    /** The initial size. */
    private static final int INITIAL_SIZE = 10;

    /** The list store. */
    @Nonnull
    private final IntObjectOpenHashMap<Storage[]> listStore = new IntObjectOpenHashMap<>();

    /** The optional store. */
    @Nonnull
    private final IntObjectOpenHashMap<Storage> optionalStore = new IntObjectOpenHashMap<>();

    /** The storage creator. */
    @Nonnull
    private final StorageCreator storageCreator;

    /** The struct. */
    @Nonnull
    private final Struct struct;

    /** Instantiates a new optional object store. */
    @SuppressWarnings("null")
    public OptionalObjectStore(final Struct theStruct,
            final StorageCreator theStorageCreator,
            @Nullable final Storage theParent, final boolean isTransactional) {
        struct = theStruct;
        storageCreator = theStorageCreator;
        for (final Struct optionalChild : theStruct.allOptionalChildren()) {
            optionalStore.put(optionalChild.storageKey().getKey(),
                    theStorageCreator.createSparseStorage(optionalChild,
                            INITIAL_SIZE, theParent, isTransactional));
        }
        for (final Struct listChild : theStruct.allListChildren()) {
            final Storage[] sotrageArray = new Storage[INITIAL_SIZE];
            // The actual storage will be created lazily at the time when
            // createList method is invoked.
            listStore.put(listChild.storageKey().getKey(), sotrageArray);
        }
    }

    /** Performs look up for appropriate storage */
    @SuppressWarnings("null")
    @Nullable
    private Storage getStorageForRead(final int theStructure,
            final Field<?, ?> theField) {
        final Storage storage = optionalStore.get(theField.storageKey()
                .getKey());
        if (storage == null) {
            final Storage childStorage = childStorage(theField);
            childStorage.selectStructure(theStructure);
            return childStorage;
        }
        final SparseStorage resizableStorage = (SparseStorage) storage;
        if (!resizableStorage.contains(theStructure)) {
            return null;
        }
        resizableStorage.selectStructure(theStructure);
        return resizableStorage;
    }

    /** Performs look up for appropriate storage */
    private Storage getStorageForWrite(final int theStructure,
            final Field<?, ?> theField) {
        @SuppressWarnings("null")
        Storage storage = optionalStore.get(theField.storageKey().getKey());
        if (storage == null) {
            storage = childStorage(theField);
        }
        storage.selectStructure(theStructure);
        return storage;
    }

    /** Returns the storage of a Child. */
    @SuppressWarnings("null")
    public Storage childStorage(final Child theChild) {
        StorageKey k = theChild.storageKey().getParentKey();
        Storage childStorage = null;
        while (k != null) {
            if (optionalStore.containsKey(k.getKey())) {
                childStorage = optionalStore.lget();
                break;
            }
            k = k.getParentKey();
        }
        checkState(childStorage != null, "The child :" + theChild.name()
                + " does not belong to this Storage.");
        return childStorage;
    }

    /** Clear Child. */
    @SuppressWarnings("null")
    public void clearChild(final int theStructure, final Struct theChild) {
        if (theChild.isOptional()) {
            if (optionalStore.containsKey(theChild.storageKey().getKey())) {
                final Storage storage = optionalStore.lget();
                storage.selectStructure(theStructure);
                storage.clear();
            } else {
                // Optional child is inside another optional child.
                final Storage childStorage = childStorage(theChild);
                childStorage.selectStructure(theStructure);
                childStorage.clearChild(theChild);
            }
        } else if (theChild.structProperties().isList()) {
            if (listStore.containsKey(theChild.storageKey().getKey())) {
                final Storage[] storageArray = listStore.lget();
                storageArray[theStructure] = null;
            } else {
                // List child is inside an optional child
                final Storage childStorage = childStorage(theChild);
                childStorage.selectStructure(theStructure);
                childStorage.clearChild(theChild);
            }
        }
    }

    /**
     * Copies the data to other Optional object Storage.
     *
     * @param theOtherStore the other Optional object storage
     */
    @SuppressWarnings("null")
    public void copyTo(final OptionalObjectStore theOtherStore) {
        if (optionalStore.size() > 0) {
            for (final IntCursor key : optionalStore.keys()) {
                final Storage sourceStorage = optionalStore.get(key.value);
                final Storage destStorage = theOtherStore.optionalStore
                        .get(key.value);
                if (sourceStorage != null && destStorage != null) {
                    sourceStorage.copyStorage(destStorage);
                }
            }
        }
        if (listStore.size() > 0) {
            for (final IntCursor key : listStore.keys()) {
                final Storage[] sotrageArray = listStore.get(key.value);
                final Storage[] otherSotrageArray = theOtherStore.listStore
                        .get(key.value);
                for (int i = 0; i < sotrageArray.length; i++) {
                    if (sotrageArray[i] != null && otherSotrageArray != null) {
                        sotrageArray[i].copyStorage(otherSotrageArray[i]);
                    }
                }
            }
        }
    }

    /** Performs look up for appropriate storage and creates list storage*/
    @SuppressWarnings("null")
    public Storage createList(final int theStructure,
            final Struct theListChild, final int theInitialSize,
            final Storage theBaseStorage) {
        final Storage[] listStorageArray = listStore.get(theListChild
                .storageKey().getKey());
        if (listStorageArray == null) {
            final Storage childStorage = childStorage(theListChild);
            childStorage.selectStructure(theStructure);
            return childStorage.createOrClearList(theListChild, theInitialSize);
        }
        final Storage oldStorage = listStorageArray[theStructure];
        // before discarding the old storage clear all the elements so that
        // changes are recorded.
        if (oldStorage != null) {
            for (int i = 0; i < oldStorage.getCapacity(); i++) {
                oldStorage.selectStructure(i);
                oldStorage.clear();
            }
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Storage s = storageCreator.createStorage(theListChild,
                theInitialSize, theBaseStorage, true);

        listStorageArray[theStructure] = s;
        return listStorageArray[theStructure];
    }

    /** Performs look up for appropriate storage and gets the list storage from it */
    @SuppressWarnings("null")
    public Storage list(final int theStructure, final Struct theListChild) {
        final Storage[] listStorageArray = listStore.get(theListChild
                .storageKey().getKey());
        if (listStorageArray == null) {
            final Storage childStorage = childStorage(theListChild);
            childStorage.selectStructure(theStructure);
            return childStorage.list(theListChild);
        }
        return listStorageArray[theStructure];
    }

    /** Performs look up for appropriate storage and reads Boolean from it */
    @SuppressWarnings("null")
    public boolean readSparse(final int theStructure,
            final BooleanField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? false : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Byte from it*/
    public byte readSparse(final int theStructure,
            final ByteField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Char from it */
    public char readSparse(final int theStructure,
            final CharField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Double from it */
    public double readSparse(final int theStructure,
            final DoubleField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Float from it */
    public float readSparse(final int theStructure,
            final FloatField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Int from it */
    public int readSparse(final int theStructure, final IntField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Long from it */
    public long readSparse(final int theStructure,
            final LongField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and reads Object from it */
    @Nullable
    public <E, F extends ObjectField<E, F>> E readSparse(
            final int theStructure, final ObjectField<E, F> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? null : storage.read(theField);
    }
    /** Performs look up for appropriate storage and reads Short from it */
    public short readSparse(final int theStructure,
            final ShortField<?, ?> theField) {
        final Storage storage = getStorageForRead(theStructure, theField);
        return storage == null ? 0 : storage.read(theField);
    }

    /** Performs look up for appropriate storage and writes Boolean to it */
    public void writeSparse(final int theStructure,
            final BooleanField<?, ?> theField, final boolean theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes Byte to it */
    public void writeSparse(final int theStructure,
            final ByteField<?, ?> theField, final byte theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes char to it */
    public void writeSparse(final int theStructure,
            final CharField<?, ?> theField, final char theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes double to it */
    public void writeSparse(final int theStructure,
            final DoubleField<?, ?> theField, final double theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes Float to it */
    public void writeSparse(final int theStructure,
            final FloatField<?, ?> theField, final float theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes int to it */
    public void writeSparse(final int theStructure,
            final IntField<?, ?> theField, final int theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes long to it */
    public void writeSparse(final int theStructure,
            final LongField<?, ?> theField, final long theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes Object to it */
    public <E, F extends ObjectField<E, F>> void writeSparse(
            final int theStructure, final ObjectField<E, F> theField,
            @Nullable final E theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }

    /** Performs look up for appropriate storage and writes short to it */
    public void writeSparse(final int theStructure,
            final ShortField<?, ?> theField, final short theValue) {
        getStorageForWrite(theStructure, theField).write(theField, theValue);
    }
}
