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

import static com.blockwithme.lessobjects.util.StructConstants.LIST_INITIAL_SIZE;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.StorageKey;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
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
 * A Composite Storage that consists of at least one optional/list child Storage.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Aligned64CompositeStorage extends Aligned64Storage {

    /** The StorageCreator cached instance. */
    private static final OptionalObjectStore.StorageCreator STORAGE_CREATOR = new OptionalObjectStore.StorageCreator() {
        @SuppressWarnings("null")
        @Override
        public SparseStorage createSparseStorage(final Struct theStruct,
                final int theInitialCapacity,
                @Nullable final Storage theBaseStorage,
                final boolean theTransactionalFlag) {

            return new Aligned64SparseStorage(theStruct, theInitialCapacity,
                    theBaseStorage, theTransactionalFlag, Arity.ONE_D);
        }

        @SuppressWarnings({ "unchecked", "rawtypes", "null" })
        @Override
        public Storage createStorage(final Struct theStruct,
                final int theInitialCapacity,
                @Nullable final Storage theBaseStorage,
                final boolean theTransactionalFlag) {
            return THE_COMPILER.initStorage(theStruct, theInitialCapacity,
                    theBaseStorage, theTransactionalFlag, Arity.ONE_D);
        }
    };

    /** The Aligned64Compiler cached instance. */
    private static final Aligned64Compiler THE_COMPILER = new Aligned64Compiler();

    /** The optional storage. */
    @Nonnull
    private final transient OptionalObjectStore optionalStorage;

    /** The storage key. */
    @Nonnull
    private final StorageKey storageKey;

    /** @param theStruct Struct backed by this storage.
     * @param theSize storage size */
    Aligned64CompositeStorage(final Struct theStruct, final int theSize,
            final boolean isTransactional, final Arity theArity) {
        this(theStruct, theSize, null, isTransactional, theArity);
    }

    /** Instantiates a new aligned 64 composite storage. */
    public Aligned64CompositeStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
        optionalStorage = theBuilder.getOptionalStorage();
        storageKey = theBuilder.getStruct().storageKey();
    }

    /** @param theStruct Struct backed by this storage.
     * @param theSize storage size */
    @SuppressWarnings({ "hiding", "null" })
    public Aligned64CompositeStorage(final Struct theStruct, final int theSize,
            @Nullable final Storage theParent, final boolean isTransactional,
            final Arity theArity) {

        super(theStruct, theSize, theParent, isTransactional, false, theArity);
        storageKey = theStruct.storageKey();
        optionalStorage = new OptionalObjectStore(theStruct, STORAGE_CREATOR,
                this, isTransactional);

    }

    /** {@inheritDoc} */
    @Override
    protected Storage blankCopy() {
        @SuppressWarnings("null")
        final Aligned64CompositeStorage result = new Aligned64CompositeStorage(
                struct, capacity, isSecondary ? baseStorage : null,
                !transactionsDisabled, arity);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
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
        if (!theChild.isOptional() && !theChild.structProperties().isList()) {
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
            super.copyStorage(theOther, theSchemaMigrator);
        } else {
            // calls Aligned64Storage.copyStorage first then copies
            // optionalStorage.
            super.copyStorage(theOther, theSchemaMigrator);
            final Aligned64CompositeStorage otherStorage = (Aligned64CompositeStorage) theOther;
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
    @Override
    @SuppressWarnings("null")
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
    public boolean read(final BooleanField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public byte read(final ByteField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public char read(final CharField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public double read(final DoubleField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public float read(final FloatField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public int read(final IntField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public long read(final LongField theField) {
        if (theField.storageKey() != storageKey) {
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
    public short read(final ShortField theField) {
        if (theField.storageKey() != storageKey) {
            return optionalStorage.readSparse(structure, theField);
        }
        return super.read(theField);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final BooleanField theField, final boolean theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final ByteField theField, final byte theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final CharField theField, final char theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final DoubleField theField, final double theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final FloatField theField, final float theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final IntField theField, final int theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final LongField theField, final long theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
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
    public void write(final ShortField theField, final short theValue) {
        checkReadOnly();
        if (theField.storageKey() != storageKey) {
            optionalStorage.writeSparse(structure, theField, theValue);
            return;
        }
        super.write(theField, theValue);
    }
}
