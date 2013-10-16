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
import com.blockwithme.lessobjects.fields.global.BooleanGlobalField;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.storage.Storage;

/** The Boolean Value Change holder.
 *
 * @author tarung
 *
 *  */
@ParametersAreNonnullByDefault
public class BooleanValueChange implements ValueChange<Boolean> {

    /** The field. */
    @Nonnull
    private Field<Boolean, ? extends Field<Boolean, ?>> field;

    /** The new value. */
    private boolean newValue;

    /** The old value. */
    private boolean oldValue;

    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((BooleanOptionalField) field, newValue);
        } else if (field.global()) {
            theStorage.write((BooleanGlobalField) field, newValue);
        } else {
            theStorage.write((BooleanField) field, newValue);
        }
    }

    /** Boolean new value, use this method to avoid auto-boxing. */
    public boolean booleanNewValue() {
        return newValue;
    }

    /** Boolean old value, use this method to avoid auto-boxing. */
    public boolean booleanOldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public <F extends Field<Boolean, F>> F field() {
        return (F) field;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Boolean newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Boolean oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((BooleanOptionalField) field, oldValue);
        } else if (field.global()) {
            theStorage.write((BooleanGlobalField) field, oldValue);
        } else {
            theStorage.write((BooleanField) field, oldValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int structureIndex() {
        return structIndex;
    }

    /** updates boolean value change.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final BooleanField theField,
            final boolean theOldValue, final boolean theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldValue = theOldValue;
        newValue = theNewValue;
    }

    /** updates boolean value change.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex,
            final BooleanGlobalField theField, final boolean theOldValue,
            final boolean theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldValue = theOldValue;
        newValue = theNewValue;

    }

    /** updates boolean value change.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex,
            final BooleanOptionalField theField, final boolean theOldValue,
            final boolean theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldValue = theOldValue;
        newValue = theNewValue;
    }
}
