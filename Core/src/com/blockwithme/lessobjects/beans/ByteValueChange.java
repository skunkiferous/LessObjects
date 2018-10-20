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
import com.blockwithme.lessobjects.fields.global.ByteGlobalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.storage.Storage;

/** The Byte Value Change holder
 *
 * @author tarung
 *
 * */
@ParametersAreNonnullByDefault
public class ByteValueChange implements ValueChange<Byte> {

    /** The field. */
    @Nonnull
    private Field<Byte, ? extends Field<Byte, ?>> field;

    /** The new value. */
    private byte newValue;

    /** The old value. */
    private byte oldValue;

    /** The struct index. */
    private int structIndex;

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void applyChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((ByteOptionalField) field, newValue);
        } else if (field.global()) {
            theStorage.write((ByteGlobalField) field, newValue);
        } else {
            theStorage.write((ByteField) field, newValue);
        }

    }

    /** New value, use this method to avoid auto-boxing. */
    public byte byteNewValue() {
        return newValue;
    }

    /** Old value, use this method to avoid auto-boxing. */
    public byte byteOldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked" })
    @Override
    public <F extends Field<Byte, F>> F field() {
        return (F) field;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Byte newValue() {
        return newValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public Byte oldValue() {
        return oldValue;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "rawtypes" })
    @Override
    public void reverseChange(final Storage theStorage) {
        theStorage.selectStructure(structIndex);
        if (field.isOptional()) {
            theStorage.write((ByteOptionalField) field, oldValue);
        } else if (field.global()) {
            theStorage.write((ByteGlobalField) field, oldValue);
        } else {
            theStorage.write((ByteField) field, oldValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int structureIndex() {
        return structIndex;
    }

    /** updates Byte Value Change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void update(final int theStructIndex, final ByteField theField,
            final byte theOldValue, final byte theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldValue = theOldValue;
        newValue = theNewValue;
    }

    /** updates Byte Value Change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex,
            final ByteGlobalField theField, final byte theOldValue,
            final byte theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldValue = theOldValue;
        newValue = theNewValue;
    }

    /** updates Byte Value Change holder.
     *
     * @param theStructIndex the struct index
     * @param theField the field
     * @param theOldValue the old value
     * @param theNewValue the new value */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void update(final int theStructIndex, final ByteOptionalField theField,
            final byte theOldValue, final byte theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldValue = theOldValue;
        newValue = theNewValue;
    }
}
