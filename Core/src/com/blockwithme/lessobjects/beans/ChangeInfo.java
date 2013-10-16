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
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;

/**
 * The Value Change holder; a master object that can contain any type of change.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChangeInfo {

    /** The new double value. */
    private double newDoubleValue;

    /** The new long value. */
    private long newLongValue;

    /** The new object value. */
    private Object newObjectValue;

    /** The old double value. */
    private double oldDoubleValue;

    /** The old long value. */
    private long oldLongValue;

    /** The old object value. */
    private Object oldObjectValue;

    /** The struct index. */
    private int structIndex;

    /** The field. */
    @Nonnull
    private Field<?, ?> field;

    /** @return the field */
    @SuppressWarnings("null")
    public Field<?, ?> field() {
        return field;
    }

    /** Returns the field name. */
    public String getFieldName() {
        return field.name();
    }

    /** @return the structIndex */
    public int getStructIndex() {
        return structIndex;
    }

    /** @return the newBooleanValue */
    public boolean newBooleanValue() {
        return newLongValue != 0;
    }

    /** @return the newByteValue */
    public byte newByteValue() {
        return (byte) newLongValue;
    }

    /** @return the newCharValue */
    public char newCharValue() {
        return (char) newLongValue;
    }

    /** @return the newDoubleValue */
    public double newDoubleValue() {
        return newDoubleValue;
    }

    /** @return the newFloatValue */
    public float newFloatValue() {
        return (float) newDoubleValue;
    }

    /** @return the newIntValue */
    public int newIntValue() {
        return (int) newLongValue;
    }

    /** @return the newLongValue */
    public long newLongValue() {
        return newLongValue;
    }

    /** @return the newObjectValue */
    @Nullable
    public Object newObjectValue() {
        return newObjectValue;
    }

    /** @return the newShortValue */
    public short newShortValue() {
        return (short) newLongValue;
    }

    /** @return the oldBooleanValue */
    public boolean oldBooleanValue() {
        return oldLongValue != 0;
    }

    /** @return the oldByteValue */
    public byte oldByteValue() {
        return (byte) oldLongValue;
    }

    /** @return the oldCharValue */
    public char oldCharValue() {
        return (char) oldLongValue;
    }

    /** @return the oldDoubleValue */
    public double oldDoubleValue() {
        return oldDoubleValue;
    }

    /** @return the oldFloatValue */
    public float oldFloatValue() {
        return (float) oldDoubleValue;
    }

    /** @return the oldIntValue */
    public int oldIntValue() {
        return (int) oldLongValue;
    }

    /** @return the oldLongValue */
    public long oldLongValue() {
        return oldLongValue;
    }

    /** @return the oldObjectValue */
    @Nullable
    public Object oldObjectValue() {
        return oldObjectValue;
    }

    /** @return the oldShortValue */
    public short oldShortValue() {
        return (short) oldLongValue;
    }

    /** Boolean update method. */
    public void update(final int theStructIndex,
            final BooleanField<?, ?> theField, final boolean theOldValue,
            final boolean theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldLongValue = theOldValue ? 1 : 0;
        newLongValue = theNewValue ? 1 : 0;
    }

    /** Byte update method. */
    public void update(final int theStructIndex,
            final ByteField<?, ?> theField, final byte theOldValue,
            final byte theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldLongValue = theOldValue;
        newLongValue = theNewValue;
    }

    /** Char update method. */
    public void update(final int theStructIndex,
            final CharField<?, ?> theField, final char theOldValue,
            final char theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldLongValue = theOldValue;
        newLongValue = theNewValue;
    }

    /** Double update method. */
    public void update(final int theStructIndex,
            final DoubleField<?, ?> theField, final double theOldValue,
            final double theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldDoubleValue = theOldValue;
        newDoubleValue = theNewValue;
    }

    /** Float update method. */
    public void update(final int theStructIndex,
            final FloatField<?, ?> theField, final float theOldValue,
            final float theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldDoubleValue = theOldValue;
        newDoubleValue = theNewValue;
    }

    /** Integer update method. */
    public void update(final int theStructIndex, final IntField<?, ?> theField,
            final int theOldValue, final int theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldLongValue = theOldValue;
        newLongValue = theNewValue;
    }

    /** Long update method. */
    public void update(final int theStructIndex,
            final LongField<?, ?> theField, final long theOldValue,
            final long theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldLongValue = theOldValue;
        newLongValue = theNewValue;
    }

    /** Object update method. */
    public void update(final int theStructIndex,
            final ObjectField<?, ?> theField,
            @Nullable final Object theOldValue,
            @Nullable final Object theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldObjectValue = theOldValue;
        newObjectValue = theNewValue;
    }

    /** Short update method. */
    public void update(final int theStructIndex,
            final ShortField<?, ?> theField, final short theOldValue,
            final short theNewValue) {
        structIndex = theStructIndex;
        field = theField;
        oldLongValue = theOldValue;
        newLongValue = theNewValue;
    }
}
