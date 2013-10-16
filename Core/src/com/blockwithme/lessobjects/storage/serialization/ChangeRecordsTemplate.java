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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.blockwithme.lessobjects.storage.ChangeRecordsImpl;
import com.blockwithme.lessobjects.storage.ChangeStorage;
import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.ObjectUnpacker;
import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.Unpacker;
import com.blockwithme.msgpack.templates.AbstractTemplate;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.UnpackerContext;

/**
 * The Template class for serialization of ChangeRecords Class.
 *
 * @author tarung
 */
public class ChangeRecordsTemplate extends AbstractTemplate<ChangeRecordsImpl> {

    protected ChangeRecordsTemplate() {
        super(null, ChangeRecordsImpl.class, 1, ARRAY, EQUALITY, -1);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final ChangeRecordsImpl theStorage) {
        int count = 2;
        final Set<ChangeRecordsImpl> children = theStorage.children();
        if (children != null) {
            for (final ChangeRecordsImpl child : children) {
                count += getSpaceRequired(theContext, child);
            }
        }
        return count;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public ChangeRecordsImpl readData(final UnpackerContext theContext,
            final ChangeRecordsImpl thePreCreated, final int theSize)
            throws IOException {
        final ObjectUnpacker objPacker = theContext.objectUnpacker;
        final Unpacker up = theContext.unpacker;

        final ChangeStorage changeStorage = (ChangeStorage) objPacker
                .readObject();
        final int childerCount = up.readInt();
        Set<ChangeRecordsImpl> children = Collections.EMPTY_SET;
        if (childerCount > 0) {
            children = new HashSet<>();
            for (int count = 0; count < childerCount; count++) {
                children.add(readData(theContext, null, -1));
            }
        }
        return new ChangeRecordsImpl(changeStorage, children);
    }

    /** {@inheritDoc} */
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final ChangeRecordsImpl theValue) throws IOException {
        final ObjectPacker objPacker = theContext.objectPacker;
        final Packer p = theContext.packer;
        objPacker.writeObject(theValue.changeStorage());
        final Set<ChangeRecordsImpl> children = theValue.children();
        @SuppressWarnings("null")
        final int count = children == null ? 0 : children.size();
        p.writeInt(count);
        for (final ChangeRecordsImpl child : children) {
            writeData(theContext, -1, child);
        }
    }
}
