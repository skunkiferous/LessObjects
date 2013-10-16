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
import com.blockwithme.lessobjects.fields.global.LongGlobalField;
import com.blockwithme.lessobjects.fields.optional.LongOptionalField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.storage.Storage;

/** The Class Long Value Change holder
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class LongValueChange implements ValueChange<Long> {

    /** The field. */
    @Nonnull
    private Field<Long, ? extends Field<Long, ?>> field;

    /** The new value. */
    private long newValue;

    /** The old value. */
    private long oldValue;

    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((LongOptionalField) field, newValue);
        } else if (field.global()) {
            theStorage.write((LongGlobalField) field, newValue);
        } else {
            theStorage.write((LongField) field, newValue);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public <F extends Field<Long, F>> F field() {
        return (F) field;
    }

    /** New value, use this method to avoid auto-boxing. */
    public long longNewValue() {
        return newValue;
    }

    /** Old value, use this method to avoid auto-boxing. */
    public long longOldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Long newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Long oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((LongOptionalField) field, oldValue);
        } else if (field.global()) {
            theStorage.write((LongGlobalField) field, oldValue);
        } else {
            theStorage.write((LongField) field, oldValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int structureIndex() {
        return structIndex;
    }

    /** updates long value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final LongField theField,
            final long theOldValue, final long theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }

    /** updates long value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex,
            final LongGlobalField theField, final long theOldValue,
            final long theNewValue) {
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
    public void update(final int theStructIndex, final LongOptionalField theField,
            final long theOldValue, final long theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }
}
