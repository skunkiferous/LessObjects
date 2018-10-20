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
import com.carrotsearch.hppc.IntStack;
import com.carrotsearch.hppc.cursors.IntCursor;

/** Serialization Template class for IntOpenHashSet. */
@SuppressWarnings("rawtypes")
public class IntStackTemplate extends AbstractTemplate<IntStack> {

    /** Instantiates a new int open hash set template. */
    protected IntStackTemplate() {
        super(null, IntStack.class, 1, ObjectType.ARRAY, false);
    }

    /** Computes the size; (key+value) counts as *1*. */
    @Override
    public final int getSpaceRequired(final PackerContext theContext,
            final IntStack theValue) {
        if (theValue != null) {
            return theValue.size();
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public IntStack preCreate(final int theSize) {
        return new IntStack(theSize);
    }

    /** {@inheritDoc} */
    @Override
    public IntStack readData(final UnpackerContext theContext,
            final IntStack thePreCreated, final int theSize) throws IOException {

        final Unpacker u = theContext.unpacker;
        for (int i = 0; i < theSize; i++) {
            final int val = u.readInt();
            thePreCreated.add(val);
        }
        return thePreCreated;
    }

    /** {@inheritDoc} */
    @Override
    public void writeData(final PackerContext theContext, final int theSize,
            final IntStack theValue) throws IOException {
        final Packer p = theContext.packer;
        for (final IntCursor k : theValue) {
            p.writeInt(k.value);
        }
    }
}