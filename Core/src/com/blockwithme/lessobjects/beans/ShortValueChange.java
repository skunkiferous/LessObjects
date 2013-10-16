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

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.fields.global.ShortGlobalField;
import com.blockwithme.lessobjects.fields.optional.ShortOptionalField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.Storage;

import edu.umd.cs.findbugs.annotations.NonNull;

/** The Class Short Value Change holder
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class ShortValueChange implements ValueChange<Short> {

    /** The field. */
    @NonNull
    private Field<Short, ? extends Field<Short, ?>> field;

    /** The new value. */
    private short newValue;

    /** The old value. */
    private short oldValue;

    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((ShortOptionalField) field, newValue);
        } else if (field.global()) {
            theStorage.write((ShortGlobalField) field, newValue);
        } else {
            theStorage.write((ShortField) field, newValue);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public <F extends Field<Short, F>> F field() {
        return (F) field;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Short newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Short oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((ShortOptionalField) field, oldValue);
        } else if (field.global()) {
            theStorage.write((ShortGlobalField) field, oldValue);
        } else {
            theStorage.write((ShortField) field, oldValue);
        }
    }

    /** New value, use this method to avoid auto-boxing. */
    public short shortNewValue() {
        return newValue;
    }

    /** Old value, use this method to avoid auto-boxing. */
    public short shortOldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public int structureIndex() {
        return structIndex;
    }

    /** updates short value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final ShortField theField,
            final short theOldValue, final short theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }

    /** updates short value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex,
            final ShortGlobalField theField, final short theOldValue,
            final short theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }

    /** updates short value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final ShortOptionalField theField,
            final short theOldValue, final short theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }
}
