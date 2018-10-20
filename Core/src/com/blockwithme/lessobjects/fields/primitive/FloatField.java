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
package com.blockwithme.lessobjects.fields.primitive;

import static com.blockwithme.lessobjects.util.StructConstants.FLOAT_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.prim.FloatConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Float field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class FloatField<E, F extends FloatField<E, F>> extends Field<E, F> {

    /** Instantiates a new float field.
     *
     * @param theProperties the properties
    */
    @SuppressWarnings("boxing")
    protected FloatField(final FieldProperties theProperties) {
        super(validate(theProperties, FloatConverter.class,
                FloatConverter.DEFAULT_BITS));
    }

    /** Instantiates a new float field.
     *
     * @param theName the name
     *  */
    @SuppressWarnings("boxing")
    protected FloatField(final FloatConverter<E> theConverter,
            final String theName, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, isGlobal, isOptional, isVirtual);
    }

    /** Instantiates a new float field.
     *
     * @param theName the name
     * */
    @SuppressWarnings("boxing")
    public FloatField(final FloatConverter<E> theConverter, final String theName) {
        super(theConverter, theName);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public FloatField(final String theName) {
        super((FloatConverter) FloatConverter.DEFAULT, theName);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return FLOAT_BITS;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeFloat(theStorage, theBuffer, 0);
    }

    /** Returns the converter. */
    @SuppressWarnings("null")
    @Override
    public final FloatConverter<E> converter() {
        return (FloatConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new FloatField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((FloatField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    @SuppressWarnings("unchecked")
    public float readFloat(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readFloat((F) this);
    }

    /** Read float value from Storage wrapper buffer if found other wise
         * reads the value from storage.*/
    public float readFloat(final Storage theStorage,
            final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return Float.intBitsToFloat((int) buffer.lget());
        }
        return theStorage.read(this);

    }

    /** {@inheritDoc} */
    @SuppressWarnings("boxing")
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        theStorage.write(this, converter().fromObject(theValue));
    }

    /** {@inheritDoc} */
    @Override
    public void writeAnyLong(final long theValue, final Storage theStorage) {
        theStorage.write(this, Float.intBitsToFloat((int) theValue));
    }

    @SuppressWarnings({ "null", "unchecked" })
    public void writeFloat(final Storage theStorage, final float theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final float oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.FLOAT_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Writes the float value to Storage wrapper buffer.*/
    public void writeFloat(final Storage theStorage,
            final StorageBuffer theBuffer, final float theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, Float.floatToIntBits(theValue));
    }
}
