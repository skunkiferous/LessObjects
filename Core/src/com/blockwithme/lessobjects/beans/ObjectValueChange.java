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
/**
 *
 */
package com.blockwithme.lessobjects.beans;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.storage.Storage;

/** The Class ObjectValueChange.
 *
 * @param <E> the element type
 * @param <F> the generic type
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class ObjectValueChange<E, F extends Field<E, F>> implements
        ValueChange<E> {

    /** The field. */
    @Nonnull
    private F field;

    /** The new value. */
    private E newValue;

    /** The old value. */
    private E oldValue;

    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        field.writeAny(newValue(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public F field() {
        return field;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public E newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public E oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        field.writeAny(oldValue(), theStorage);
    }

    /** {@inheritDoc} */
    @Override
    public int structureIndex() {
        return structIndex;
    }

    /** Instantiates a new long value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    public void update(final int theStructIndex, final F theField,
            @Nullable final E theOldValue, @Nullable final E theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;

    }
}
