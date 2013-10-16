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
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.fields.global.IntGlobalField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.storage.Storage;

/** The Class Int Value Change holder
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class IntValueChange implements ValueChange<Integer> {

    /** The field. */
    @Nonnull
    private Field<Integer, ? extends Field<Integer, ?>> field;

    /** The new value. */
    private int newValue;

    /** The old value. */
    private int oldValue;

    /** The storage reference. */
    @Nonnull
    // private Storage storageReference;
    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((IntOptionalField) field, newValue);
        } else if (field.global()) {
            theStorage.write((IntGlobalField) field, newValue);
        } else {
            theStorage.write((IntField) field, newValue);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public <F extends Field<Integer, F>> F field() {
        return (F) field;
    }

    /** New value, use this method to avoid auto-boxing. */
    public long intNewValue() {
        return newValue;
    }

    /** Old value, use this method to avoid auto-boxing. */
    public long intOldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Integer newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Integer oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((IntOptionalField) field, oldValue);
        } else if (field.global()) {
            theStorage.write((IntGlobalField) field, oldValue);
        } else {
            theStorage.write((IntField) field, oldValue);
        }
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final IntField theField,
            final int theOldValue, final int theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }

    /** Instantiates a new long value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final IntGlobalField theField,
            final int theOldValue, final int theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;

    }

    /** Instantiates a new long value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final IntOptionalField theField,
            final int theOldValue, final int theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }
}
