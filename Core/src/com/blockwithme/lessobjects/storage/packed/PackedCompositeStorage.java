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

import static com.blockwithme.lessobjects.util.StructConstants.LIST_INITIAL_SIZE;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.StorageKey;
import com.blockwithme.lessobjects.compiler.CompilerBase;
import com.blockwithme.lessobjects.compiler.PackedCompiler;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.OptionalObjectStore;
import com.blockwithme.lessobjects.storage.SparseStorage;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageBuilder;

/**
 * Implements the storage wrapper for the packed compiler.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class PackedCompositeStorage extends PackedStorage {

    /** The cached StorageCreator. */
    private static final OptionalObjectStore.StorageCreator STORAGE_CREATOR = new OptionalObjectStore.StorageCreator() {
        @SuppressWarnings("null")
        @Override
        public SparseStorage createSparseStorage(final Struct theStruct,
                final int theInitialCapacity,
                @Nullable final Storage theBaseStorage,
                final boolean theTransactionalFlag) {
            // passing SINGLE_D as arity for the child storage.
            return new PackedSparseStorage(theStruct, theInitialCapacity,
                    theBaseStorage, theTransactionalFlag, Arity.ONE_D);
        }

        @SuppressWarnings({ "unchecked", "rawtypes", "null" })
        @Override
        public Storage createStorage(final Struct theStruct,
                final int theInitialCapacity,
                @Nullable final Storage theBaseStorage,
                final boolean theTransactionalFlag) {
            // passing SINGLE_D as arity for the child storage.
            return THE_COMPILER.initStorage(theStruct, theInitialCapacity,
                    theBaseStorage, theTransactionalFlag, Arity.ONE_D);
        }
    };

    /** The cached PackedCompiler. */
    private static final CompilerBase THE_COMPILER = new PackedCompiler();

    /** The optional storage. */
    @Nonnull
    private final transient OptionalObjectStore optionalStorage;

    /** The storage key. */
    @Nonnull
    private final StorageKey storageKey;

    /** Constructor */
    PackedCompositeStorage(final Struct theStruct, final int theSize,
            final boolean isTransactional, final Arity theArity) {
        this(theStruct, theSize, null, isTransactional, theArity);
    }

    /** Instantiates a new packed composite storage. */
    public PackedCompositeStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
        optionalStorage = theBuilder.getOptionalStorage();
        storageKey = theBuilder.getStruct().storageKey();
    }

    /** Constructor */
    @SuppressWarnings("null")
    public PackedCompositeStorage(final Struct theStruct, final int theSize,
            @Nullable final Storage theParent, final boolean isTransactional,
            final Arity theArity) {

        super(theStruct, theSize, theParent, isTransactional, false, theArity);
        storageKey = theStruct.storageKey();
        optionalStorage = new OptionalObjectStore(theStruct, STORAGE_CREATOR,
                this, isTransactional);

    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    protected Storage blankCopy() {
        return new PackedCompositeStorage(struct, capacity,
                isSecondary ? baseStorage : null, !transactionsDisabled, arity);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    protected void clearAllChildren() {
        for (final Struct oChild : struct.allOptionalChildren()) {
            clearChild(oChild);
        }
        for (final Struct lChild : struct.allListChildren()) {
            clearChild(lChild);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearChild(final Struct theChild) {
        if (!theChild.isOptional() && !theChild.list()) {
            super.clearChild(theChild);
            return;
        }
        optionalStorage.clearChild(structure, theChild);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {
        if (isDifferent(theOther)) {
            // Call AbstractStorage.copyStorage method.
            super.copyStorage(theOther, theSchemaMigrator);
        } else {
            // calls PackedStorage.copyStorage
            super.copyStorage(theOther, theSchemaMigrator);
            final PackedCompositeStorage otherStorage = (PackedCompositeStorage) theOther;
            optionalStorage.copyTo(otherStorage.optionalStorage);
        }

    }

    /** Creates the list and returns the Storage.
     *
     * @param theListChild the list child
     * @return the storage */
    @Override
    public Storage createOrClearList(final Struct theListChild) {
        return createOrClearList(theListChild, LIST_INITIAL_SIZE);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage createOrClearList(final Struct theListChild,
            final int theInitialSize) {
        return optionalStorage.createList(structure, theListChild,
                theInitialSize, this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public StorageBuilder getBuilder() {
        final StorageBuilder builder = super.getBuilder();
        builder.setOptionalStorage(optionalStorage);
        return builder;
    }

    /** If the struct backed by this storage contains a list child, this
     * method returns the storage corresponding to the list child. Returns null
     * if the list has no elements.
     *
     * @param theListChild reference to the list child.
     * @return the list specific storage */
    @Override
    @Nullable
    public Storage list(final Struct theListChild) {
        return optionalStorage.list(structure, theListChild);
    }

    /** {@inheritDoc} */
    @Override
    public boolean read(final BooleanField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public byte read(final ByteField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public char read(final CharField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public double read(final DoubleField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public float read(final FloatField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public int read(final IntField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public long read(final LongField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public <E, F extends ObjectField<E, F>> E read(
            final ObjectField<E, F> theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public short read(final ShortField<?, ?> theField) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final BooleanField<?, ?> theField, final boolean theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final ByteField<?, ?> theField, final byte theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final CharField<?, ?> theField, final char theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final DoubleField<?, ?> theField, final double theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final FloatField<?, ?> theField, final float theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final IntField<?, ?> theField, final int theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final LongField<?, ?> theField, final long theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <E, F extends ObjectField<E, F>> void write(
            final ObjectField<E, F> theField, @Nullable final E theValue) {
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);

    }

    /** {@inheritDoc} */
    @Override
    public void write(final ShortField<?, ?> theField, final short theValue) {
        checkReadOnly();
        final StorageKey sk = theField.storageKey();
        if (sk != null && !sk.equals(storageKey)) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }
}
