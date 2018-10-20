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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/** Fixed-size arrays are represented by a structure where the fields are
 * generated dynamically, to represent the array elements.
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public final class Array<E, F extends Field<E, F>> {

    /** The child struct. */
    private final Struct childStruct;

    /** The field class. */
    @Nonnull
    private final Class<? extends Field<?, ?>> fieldClass;

    /** Clones the given element field, change it's name in every copy. */
    private static Field<?, ?>[] elements(final Field<?, ?> theElement,
            final int theArraySize) {
        final Field<?, ?>[] result = new Field<?, ?>[theArraySize];
        final String name = theElement.name();
        for (int i = 0; i < result.length; i++) {
            result[i] = theElement.copy(name + "[" + i + "]");
        }
        return result;
    }

    /** Passes everything to the base class. */
    private Array(final String theName, final Field<?, ?>... theFields) {
        childStruct = new Struct(theName, (Struct[]) null, theFields);
        fieldClass = (Class<? extends Field<?, ?>>) theFields[0].getClass();
    }

    /** Creates a struct with arraySize Fields of the same type as element, and
     * named element[0], element[1], ... */
    public Array(final String theName, final F theElement, final int theSize) {
        this(theName, elements(theElement, theSize));
    }

    /**
     * The generated array struct.
     */
    @SuppressWarnings("null")
    public Struct arrayStruct() {
        return childStruct;
    }

    /**
     * @return the field Class
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public Class<? extends Field> fieldClass() {
        return fieldClass;
    }
}
