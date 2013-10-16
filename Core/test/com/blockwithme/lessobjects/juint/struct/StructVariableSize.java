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
package com.blockwithme.lessobjects.juint.struct;

import static com.blockwithme.lessobjects.juint.Constants.FACTORY;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;

/** The Class StructVariableSize.  */
@SuppressWarnings("all")
public class StructVariableSize {

    /** The struct. */
    private final Struct struct;

    /** Field array. */
    private static Field<?, ?>[] fieldArray(final int theNumber) {
        final Field<?, ?>[] array = new Field<?, ?>[theNumber];
        for (int i = 0; i < theNumber; i++) {
            array[i] = FACTORY.newByteField("byteField" + i);
        }
        return array;
    }

    /** Instantiates a new base struct. */
    public StructVariableSize(final String theName, final int theNumber,
            final boolean isUnion, final Compiler theCompiler) {
        struct = theCompiler.compile(new Struct(theName, isUnion,
                new Struct[] {}, fieldArray(theNumber)));
    }

    /**
     * The struct object.
     */
    public Struct struct() {
        return struct;
    }
}
