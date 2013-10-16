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
package com.blockwithme.lessobjects;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Fixed-size arrays are represented by a structure where the children are
 * generated dynamically, to represent the array elements.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public final class ChildrenArray {

    /** The array struct. */
    private final Struct arrayStruct;

    /** Clones the given element field, change it's name in every copy. */
    private static Struct[] elements(final Struct theElement,
            final int theArraySize) {
        final Struct[] result = new Struct[theArraySize];
        final String name = theElement.name();
        for (int i = 0; i < result.length; i++) {
            final String elementName = name + "[" + i + "]";
            result[i] = theElement.copy(
                    theElement.childProperties().setName(elementName), false);
        }
        return result;
    }

    /** Creates a struct with arraySize Fields of the same type as element, and
     * named element[0], element[1], ... */
    public ChildrenArray(final String theName, final Struct theElement,
            final int theSize) {
        arrayStruct = new Struct(theName, elements(theElement, theSize));
    }

    @SuppressWarnings("null")
    public Struct arrayStruct() {
        return arrayStruct;
    }

}
