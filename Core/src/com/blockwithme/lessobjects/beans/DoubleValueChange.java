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
import com.blockwithme.lessobjects.fields.global.DoubleGlobalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.storage.Storage;

/** The Class Double Value Change holder
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class DoubleValueChange implements ValueChange<Double> {

    /** The field. */
    @Nonnull
    private Field<Double, ? extends Field<Double, ?>> field;

    /** The new value. */
    private double newValue;

    /** The old value. */
    private double oldValue;

    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((DoubleOptionalField) field, newValue);
        } else if (field.global()) {
            theStorage.write((DoubleGlobalField) field, newValue);
        } else {
            theStorage.write((DoubleField) field, newValue);
        }
    }

    /** New value, use this method to avoid auto-boxing. */
    public double doubleNewValue() {
        return newValue;
    }

    /** Old value, use this method to avoid auto-boxing. */
    public double doubleOldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public <F extends Field<Double, F>> F field() {
        return (F) field;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Double newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Double oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((DoubleOptionalField) field, oldValue);
        } else if (field.global()) {
            theStorage.write((DoubleGlobalField) field, oldValue);
        } else {
            theStorage.write((DoubleField) field, oldValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int structureIndex() {
        return structIndex;
    }

    /** updates double value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final DoubleField theField,
            final double theOldValue, final double theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }

    /** updates double value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex,
            final DoubleGlobalField theField, final double theOldValue,
            final double theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }

    /** updates double value change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final DoubleOptionalField theField,
            final double theOldValue, final double theNewValue) {
        structIndex = theStructIndex;
        oldValue = theOldValue;
        newValue = theNewValue;
        field = theField;
    }
}
