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

import com.blockwithme.lessobjects.storage.OptionalObjectStore;
import com.blockwithme.lessobjects.storage.StorageBuilder;
import com.blockwithme.lessobjects.storage.aligned64.Aligned64CompositeStorage;
import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.ObjectUnpacker;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.UnpackerContext;

/**
 * Aligned64 composite template.
 * 
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Aligned64CompositeStorageTemplate extends
        BaseStorageTemplate<Aligned64CompositeStorage> {

    /** Instantiates a new aligned64 storage template. */
    protected Aligned64CompositeStorageTemplate() {
        super(Aligned64CompositeStorage.class);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public final int getSpaceRequired(final PackerContext theContext,
            final Aligned64CompositeStorage theStorage) {
        final StorageBuilder builder = theStorage.getBuilder();
        return 1 + super.getSpaceRequired(builder);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Aligned64CompositeStorage readData(final UnpackerContext theContext,
            final Aligned64CompositeStorage thePreCreated, final int theSize)
            throws IOException {
        final ObjectUnpacker objPacker = theContext.objectUnpacker;
        final StorageBuilder builder = super.createBaseStorageBuilder(
                theContext, thePreCreated, theSize);
        builder.setOptionalStorage((OptionalObjectStore) objPacker.readObject());
        return new Aligned64CompositeStorage(builder);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final Aligned64CompositeStorage theValue) throws IOException {
        final StorageBuilder builder = theValue.getBuilder();
        super.readFromStorageBuilder(theContext, builder);
        final ObjectPacker objPacker = theContext.objectPacker;
        objPacker.writeObject(builder.getOptionalStorage());
    }
}
