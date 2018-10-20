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

import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.Unpacker;
import com.blockwithme.msgpack.templates.AbstractTemplate;
import com.blockwithme.msgpack.templates.ObjectType;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.UnpackerContext;
import com.carrotsearch.hppc.IntByteOpenHashMap;
import com.carrotsearch.hppc.cursors.IntByteCursor;

/** Serialization Template class for IntByteOpenHashMap. */
public class IntByteOpenHashMapTemplate extends
        AbstractTemplate<IntByteOpenHashMap> {

    /**Instantiates a new int byte open hash map template. */
    protected IntByteOpenHashMapTemplate() {
        super(null, IntByteOpenHashMap.class, 1, ObjectType.MAP, false);
    }

    /** Computes the size; (key+value) counts as *1*. */
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final IntByteOpenHashMap theValue) {
        return theValue.size();
    }

    /** {@inheritDoc} */
    @Override
    public IntByteOpenHashMap preCreate(final int theSize) {
        return new IntByteOpenHashMap(theSize);
    }

    /** {@inheritDoc} */
    @Override
    public IntByteOpenHashMap readData(final UnpackerContext theContext,
            final IntByteOpenHashMap thePreCreated, final int theSize)
            throws IOException {
        readHeaderValue(theContext, thePreCreated, theSize);
        final Unpacker u = theContext.unpacker;
        for (int i = 0; i < theSize; i++) {
            final int key = u.readInt();
            final byte value = u.readByte();
            thePreCreated.put(key, value);
        }
        return thePreCreated;

    }

    /** {@inheritDoc} */
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final IntByteOpenHashMap theValue) throws IOException {
        final Packer p = theContext.packer;
        for (final IntByteCursor k : theValue) {
            p.writeInt(k.key);
            p.writeByte(k.value);
        }
    }
}