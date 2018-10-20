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

import java.io.IOException;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.storage.StorageBuilder;
import com.blockwithme.lessobjects.storage.aligned64.Aligned64SparseStorage;
import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.ObjectUnpacker;
import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.Unpacker;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.UnpackerContext;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntStack;

/**
 * Aligned64 sparse storage template.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Aligned64SparseStorageTemplate extends
        BaseStorageTemplate<Aligned64SparseStorage> {

    /** Instantiates a new aligned64 sparse storage template. */
    protected Aligned64SparseStorageTemplate() {
        super(Aligned64SparseStorage.class);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public final int getSpaceRequired(final PackerContext theContext,
            final Aligned64SparseStorage theStorage) {
        final StorageBuilder builder = theStorage.getBuilder();
        return 4 + super.getSpaceRequired(builder);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Aligned64SparseStorage readData(final UnpackerContext theContext,
            final Aligned64SparseStorage thePreCreated, final int theSize)
            throws IOException {
        final ObjectUnpacker objPacker = theContext.objectUnpacker;
        final Unpacker up = theContext.unpacker;
        final StorageBuilder builder = super.createBaseStorageBuilder(
                theContext, thePreCreated, theSize);

        builder.setIndexMap((IntIntOpenHashMap) objPacker.readObject());
        builder.setSelected(up.readBoolean());
        builder.setLastInserted((IntStack) objPacker.readObject());
        builder.setPrimaryIndex(up.readInt());
        return new Aligned64SparseStorage(builder);

    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final Aligned64SparseStorage theValue) throws IOException {
        final StorageBuilder builder = theValue.getBuilder();
        final ObjectPacker objPacker = theContext.objectPacker;
        final Packer packer = theContext.packer;
        super.readFromStorageBuilder(theContext, builder);
        objPacker.writeObject(builder.getIndexMap());
        packer.writeBoolean(builder.isSelected());
        objPacker.writeObject(builder.getLastInserted());
        packer.writeInt(builder.getPrimaryIndex());
    }
}
