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

import static com.blockwithme.lessobjects.util.StructConstants.DOUBLE_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.prim.DoubleConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Double field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class DoubleField<E, F extends DoubleField<E, F>> extends Field<E, F> {

    /** Constructor */
    protected DoubleField(final DoubleConverter<E> theConverter,
            final String theName, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, isGlobal, isOptional, isVirtual);
    }

    /**
     * Instantiates a new double field.
     *
     * @param theProperties the properties
    */
    @SuppressWarnings("boxing")
    protected DoubleField(final FieldProperties theProperties) {
        super(validate(theProperties, DoubleConverter.class,
                DoubleConverter.DEFAULT_BITS));
    }

    /** Constructor */
    public DoubleField(final DoubleConverter<E> theConverter,
            final String theName) {
        super(theConverter, theName);
    }

    /** Constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public DoubleField(final String theName) {
        super((DoubleConverter) DoubleConverter.DEFAULT, theName);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return DOUBLE_BITS;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeDouble(theStorage, theBuffer, 0);
    }

    /** Returns the converter. */
    @SuppressWarnings("null")
    @Override
    public final DoubleConverter<E> converter() {
        return (DoubleConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new DoubleField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((DoubleField) theDestinationField,
                theSource.read(this));

    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    /** Read double. */
    @SuppressWarnings("unchecked")
    public double readDouble(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readDouble((F) this);
    }

    /** Read double value from Storage wrapper buffer if found other wise
     * reads the value from storage.*/
    public double readDouble(final Storage theStorage,
            final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return Double.longBitsToDouble(buffer.lget());
        }
        return theStorage.read(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "unchecked" })
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        theStorage.write(this, converter().fromObject(theValue));
    }

    /** {@inheritDoc} */
    @Override
    public void writeAnyLong(final long theValue, final Storage theStorage) {
        theStorage.write(this, Double.longBitsToDouble(theValue));
    }

    /** Write double. */
    @SuppressWarnings({ "null", "unchecked" })
    public void writeDouble(final Storage theStorage, final double theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final double oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.DOUBLE_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Writes the byte value to Storage wrapper buffer.*/
    public void writeDouble(final Storage theStorage,
            final StorageBuffer theBuffer, final double theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, Double.doubleToLongBits(theValue));
    }
}
