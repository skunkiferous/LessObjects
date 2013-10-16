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
import com.carrotsearch.hppc.IntFloatOpenHashMap;
import com.carrotsearch.hppc.cursors.IntFloatCursor;

/** Serialization Template class for IntFloatOpenHashMap. */
public class IntFloatOpenHashMapTemplate extends
        AbstractTemplate<IntFloatOpenHashMap> {

    /** Instantiates a new int float open hash map template. */
    protected IntFloatOpenHashMapTemplate() {
        super(null, IntFloatOpenHashMap.class, 1, ObjectType.MAP, false);
    }

    /** Computes the size; (key+value) counts as *1*. */
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final IntFloatOpenHashMap theValue) {
        return theValue.size();
    }

    /** {@inheritDoc} */
    @Override
    public IntFloatOpenHashMap preCreate(final int theSize) {
        return new IntFloatOpenHashMap(theSize);
    }

    /** {@inheritDoc} */
    @Override
    public IntFloatOpenHashMap readData(final UnpackerContext theContext,
            final IntFloatOpenHashMap thePreCreated, final int theSize)
            throws IOException {
        readHeaderValue(theContext, thePreCreated, theSize);
        final Unpacker u = theContext.unpacker;
        for (int i = 0; i < theSize; i++) {
            final int key = u.readInt();
            final float value = u.readFloat();
            thePreCreated.put(key, value);
        }
        return thePreCreated;

    }

    /** {@inheritDoc} */
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final IntFloatOpenHashMap theValue) throws IOException {
        final Packer p = theContext.packer;
        for (final IntFloatCursor k : theValue) {
            p.writeInt(k.key);
            p.writeFloat(k.value);
        }
    }
}