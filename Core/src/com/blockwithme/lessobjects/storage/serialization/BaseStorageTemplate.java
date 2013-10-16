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
package com.blockwithme.lessobjects.storage.serialization;

import static com.blockwithme.msgpack.templates.ObjectType.ARRAY;
import static com.blockwithme.msgpack.templates.TrackingType.EQUALITY;

import java.io.IOException;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageBuilder;
import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.ObjectUnpacker;
import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.Unpacker;
import com.blockwithme.msgpack.templates.AbstractTemplate;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.UnpackerContext;

/**
 * The Class Base class for all Storage Template classes.
 *
 * @param <T> the storage type.
 */
public abstract class BaseStorageTemplate<T extends Storage> extends
        AbstractTemplate<T> {

    /** The Constant ARITY_VALUES. */
    private static final Arity[] ARITY_VALUES = Arity.values();

    /** Instantiates a new storage base template. */
    protected BaseStorageTemplate(final Class<T> theClass) {
        super(null, theClass, 1, ARRAY, EQUALITY, -1);
    }

    /** Creates the base storage builder. */
    @SuppressWarnings({ "null", "static-method", "unused" })
    protected StorageBuilder createBaseStorageBuilder(
            final UnpackerContext theContext, final Storage thePreCreated,
            final int theSize) throws IOException {

        final ObjectUnpacker objPacker = theContext.objectUnpacker;
        final Unpacker unpacker = theContext.unpacker;
        final StorageBuilder builder = new StorageBuilder();

        builder.setCapacity(unpacker.readInt());
        builder.setTransactionsDisabled(unpacker.readBoolean());
        builder.setSecondary(unpacker.readBoolean());
        builder.setArity(ARITY_VALUES[unpacker.readByte()]);

        final Struct struct = (Struct) objPacker.readObject();
        builder.setStruct(struct);

        final boolean hasBaseStorage = unpacker.readBoolean();
        if (hasBaseStorage) {
            builder.setBaseStorage((Storage) objPacker.readObject());
        }
        final boolean hasGlobalStorage = unpacker.readBoolean();
        if (hasGlobalStorage) {
            builder.setGlobalFieldsStorage((AbstractStorage) objPacker
                    .readObject());
        }
        final int objectCount = struct.objectCount();
        if (objectCount > 0) {
            builder.setObjectStorageObjects((Object[]) objPacker.readObject());
        }

        builder.setArray((long[]) objPacker.readObject());
        builder.setStructurePosition(unpacker.readInt());

        return builder;

    }

    /** Read from storage builder. */
    @SuppressWarnings({ "null", "static-method", "unused" })
    protected void readFromStorageBuilder(final PackerContext theContext,
            final StorageBuilder theBuilder) throws IOException {
        final ObjectPacker objPacker = theContext.objectPacker;
        final Packer packer = theContext.packer;

        packer.writeInt(theBuilder.getCapacity());
        packer.writeBoolean(theBuilder.isTransactionsDisabled());
        packer.writeBoolean(theBuilder.isSecondary());
        // TODO ordinal() is evil, and should never be used for serialization
        packer.writeByte((byte) theBuilder.getArity().ordinal());

        final Struct struct = theBuilder.getStruct();
        objPacker.writeObject(struct);

        final Storage baseStorage = theBuilder.getBaseStorage();
        final boolean hasBaseStorage = baseStorage != null;
        packer.writeBoolean(hasBaseStorage);
        if (hasBaseStorage) {
            objPacker.writeObject(baseStorage);
        }

        final AbstractStorage globalStorage = theBuilder
                .getGlobalFieldsStorage();
        final boolean hasGlobalStorage = globalStorage != null;
        packer.writeBoolean(hasGlobalStorage);
        if (hasGlobalStorage) {
            objPacker.writeObject(globalStorage);
        }

        final int objectCount = struct.objectCount();
        if (objectCount > 0) {
            objPacker.writeObject(theBuilder.getObjectStorageObjects());
        }

        objPacker.writeObject(theBuilder.getArray());
        packer.writeLong(theBuilder.getStructurePosition());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("static-method")
    public final int getSpaceRequired(final StorageBuilder theBuilder) {

        // Storage-Capacity, TransactionsDisabled flag, is Secondary flag,
        // Arity, Struct, hasBaseStorage, hasGlobalStorage, Array,
        // StructurePosition
        int count = 9;

        final Storage baseStorage = theBuilder.getBaseStorage();
        final boolean hasBaseStorage = baseStorage != null;
        if (hasBaseStorage) {
            count++;
        }
        final AbstractStorage globalStorage = theBuilder
                .getGlobalFieldsStorage();
        final boolean hasGlobalStorage = globalStorage != null;
        if (hasGlobalStorage) {
            count++;
        }
        final Struct struct = theBuilder.getStruct();
        final int objectCount = struct.objectCount();
        if (objectCount > 0) {
            count++;
        }
        return count;
    }
}
