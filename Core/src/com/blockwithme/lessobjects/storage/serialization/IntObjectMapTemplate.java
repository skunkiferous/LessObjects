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
import java.util.Iterator;

import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.ObjectUnpacker;
import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.Unpacker;
import com.blockwithme.msgpack.templates.AbstractTemplate;
import com.blockwithme.msgpack.templates.ObjectType;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.UnpackerContext;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;

/** Serialization Template class for IntLongOpenHashMap. */
@SuppressWarnings("rawtypes")
public class IntObjectMapTemplate extends
        AbstractTemplate<IntObjectOpenHashMap> {

    /** Instantiates a new int object map template. */
    protected IntObjectMapTemplate() {
        super(null, IntObjectOpenHashMap.class, 1, ObjectType.MAP, false);
    }

    /** Computes the size; (key+value) counts as *1*. */
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final IntObjectOpenHashMap theValue) {
        return theValue.size();
    }

    /** {@inheritDoc} */
    @Override
    public IntObjectOpenHashMap preCreate(final int theSize) {
        return new IntObjectOpenHashMap(theSize);
    }

    /** {@inheritDoc} */
    @Override
    public IntObjectOpenHashMap readData(final UnpackerContext theContext,
            final IntObjectOpenHashMap thePreCreated, final int theSize)
            throws IOException {
        readHeaderValue(theContext, thePreCreated, theSize);
        final ObjectUnpacker ou = theContext.objectUnpacker;
        final Unpacker u = theContext.unpacker;

        for (int i = 0; i < theSize; i++) {
            final int key = u.readInt();
            final Object value = ou.readObject();
            thePreCreated.put(key, value);
        }
        return thePreCreated;
    }

    /** {@inheritDoc} */
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final IntObjectOpenHashMap theValue) throws IOException {
        final ObjectPacker op = theContext.objectPacker;
        final Packer p = theContext.packer;
        final Iterator<IntObjectCursor<?>> iterator = theValue.iterator();
        while (iterator.hasNext()) {
            final IntObjectCursor<?> k = iterator.next();
            p.writeInt(k.key);
            op.writeObject(k.value);
        }
    }
}