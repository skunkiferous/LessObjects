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
import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.cursors.IntDoubleCursor;

/** Serialization Template class for IntDoubleOpenHashMap. */
public class IntDoubleOpenHashMapTemplate extends
        AbstractTemplate<IntDoubleOpenHashMap> {

    /** Instantiates a new int double open hash map template. */
    protected IntDoubleOpenHashMapTemplate() {
        super(null, IntDoubleOpenHashMap.class, 1, ObjectType.MAP, false);
    }

    /** Computes the size; (key+value) counts as *1*. */
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final IntDoubleOpenHashMap theValue) {
        return theValue.size();
    }

    /** {@inheritDoc} */
    @Override
    public IntDoubleOpenHashMap preCreate(final int theSize) {
        return new IntDoubleOpenHashMap(theSize);
    }

    /** {@inheritDoc} */
    @Override
    public IntDoubleOpenHashMap readData(final UnpackerContext theContext,
            final IntDoubleOpenHashMap thePreCreated, final int theSize)
            throws IOException {
        readHeaderValue(theContext, thePreCreated, theSize);
        final Unpacker u = theContext.unpacker;
        for (int i = 0; i < theSize; i++) {
            final int key = u.readInt();
            final double value = u.readDouble();
            thePreCreated.put(key, value);
        }
        return thePreCreated;

    }

    /** {@inheritDoc} */
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final IntDoubleOpenHashMap theValue) throws IOException {
        final Packer p = theContext.packer;
        for (final IntDoubleCursor k : theValue) {
            p.writeInt(k.key);
            p.writeDouble(k.value);
        }
    }
}