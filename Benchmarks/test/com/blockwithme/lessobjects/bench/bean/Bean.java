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
package com.blockwithme.lessobjects.bench.bean;

import java.util.List;

import com.blockwithme.lessobjects.beans.BooleanValueChange;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.IntValueChange;
import com.blockwithme.lessobjects.beans.LongValueChange;
import com.blockwithme.lessobjects.beans.ShortValueChange;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class Bean implements BeanStorageEntity {

    private boolean booleanField;

    private byte byteField;

    private char charField;

    private double doubleField;

    private float floatField;

    private int intField;

    private long longField;

    private short shortField;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return the byteField
     */
    public byte getByteField() {
        return byteField;
    }

    /**
     * @return the charField
     */
    public char getCharField() {
        return charField;
    }

    /**
     * @return the doubleField
     */
    public double getDoubleField() {
        return doubleField;
    }

    /**
     * @return the floatField
     */
    public float getFloatField() {
        return floatField;
    }

    /**
     * @return the intField
     */
    public int getIntField() {
        return intField;
    }

    /**
     * @return the longField
     */
    public long getLongField() {
        return longField;
    }

    /**
     * @return the shortField
     */
    public short getShortField() {
        return shortField;
    }

    /**
     * @return the booleanField
     */
    public boolean isBooleanField() {
        return booleanField;
    }

    /** {@inheritDoc} */
    @Override
    public void processChanges(final int elementIndex,
            final BeanStorageEntity initialState,
            final List<ValueChange<?>> changeList) {

        final Bean initial = (Bean) initialState;
        if (initial.booleanField != booleanField) {
            final BooleanValueChange booleanValueChange = new BooleanValueChange();
            booleanValueChange.update(elementIndex, (BooleanField) null,
                    ((Bean) initialState).booleanField, booleanField);
            changeList.add(booleanValueChange);
        }
        if (initial.byteField != byteField) {
            final ByteValueChange byteValueChange = new ByteValueChange();
            byteValueChange.update(elementIndex, (ByteField) null,
                    ((Bean) initialState).byteField, byteField);
            changeList.add(byteValueChange);
        }
        if (initial.charField != charField) {
            final CharValueChange charValueChange = new CharValueChange();
            charValueChange.update(elementIndex, (CharField) null,
                    ((Bean) initialState).charField, charField);
            changeList.add(charValueChange);
        }
        if (initial.doubleField != doubleField) {
            final DoubleValueChange doubleValueChange = new DoubleValueChange();
            doubleValueChange.update(elementIndex, (DoubleField) null,
                    ((Bean) initialState).doubleField, doubleField);
            changeList.add(doubleValueChange);
        }
        if (initial.floatField != floatField) {
            final FloatValueChange floatValueChange = new FloatValueChange();
            floatValueChange.update(elementIndex, (FloatField) null,
                    ((Bean) initialState).floatField, floatField);
            changeList.add(floatValueChange);
        }
        if (initial.intField != intField) {
            final IntValueChange intValueChange = new IntValueChange();
            intValueChange.update(elementIndex, (IntField) null,
                    ((Bean) initialState).intField, intField);
            changeList.add(intValueChange);
        }
        if (initial.longField != longField) {
            final LongValueChange longValueChange = new LongValueChange();
            longValueChange.update(elementIndex, (LongField) null,
                    ((Bean) initialState).longField, longField);
            changeList.add(longValueChange);
        }
        if (initial.shortField != shortField) {
            final ShortValueChange shortValueChange = new ShortValueChange();
            shortValueChange.update(elementIndex, (ShortField) null,
                    ((Bean) initialState).shortField, shortField);
            changeList.add(shortValueChange);
        }
    }

    /**
     * @param booleanField the booleanField to set
     */
    public void setBooleanField(final boolean booleanField) {
        this.booleanField = booleanField;
    }

    /**
     * @param byteField the byteField to set
     */
    public void setByteField(final byte byteField) {
        this.byteField = byteField;
    }

    /**
     * @param charField the charField to set
     */
    public void setCharField(final char charField) {
        this.charField = charField;
    }

    /**
     * @param doubleField the doubleField to set
     */
    public void setDoubleField(final double doubleField) {
        this.doubleField = doubleField;
    }

    /**
     * @param floatField the floatField to set
     */
    public void setFloatField(final float floatField) {
        this.floatField = floatField;
    }

    /**
     * @param intField the intField to set
     */
    public void setIntField(final int intField) {
        this.intField = intField;
    }

    /**
     * @param longField the longField to set
     */
    public void setLongField(final long longField) {
        this.longField = longField;
    }

    /**
     * @param shortField the shortField to set
     */
    public void setShortField(final short shortField) {
        this.shortField = shortField;
    }

}
