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

import static com.blockwithme.lessobjects.util.StructConstants.LONG_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.prim.LongConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Long field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class LongField<E, F extends LongField<E, F>> extends Field<E, F> {

    /** LONG_BITS - 1, because LONG_BITS is a special case worth optimizing */
    private static final int MAX_BITS = LONG_BITS;

    /** Instantiates a new long field.
     *
     * @param theProperties the properties
     * */
    @SuppressWarnings("boxing")
    protected LongField(final FieldProperties theProperties) {
        super(validate(theProperties, LongConverter.class,
                LongConverter.DEFAULT_BITS));
    }

    /** constructor */
    @SuppressWarnings("boxing")
    protected LongField(final LongConverter<E> theConverter,
            final String theName, final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, theBits, isGlobal, isOptional, isVirtual);
    }

    /** constructor */
    @SuppressWarnings("boxing")
    public LongField(final LongConverter<E> theConverter, final String theName) {
        super(theConverter, theName);
    }

    /** constructor */
    @SuppressWarnings("boxing")
    public LongField(final LongConverter<E> theConverter, final String theName,
            final int theBits) {
        super(theConverter, theName, theBits);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public LongField(final String theName) {
        super((LongConverter) LongConverter.DEFAULT, theName);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public LongField(final String theName, final int theBits) {
        super((LongConverter) LongConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return MAX_BITS;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, 0L);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeLong(theStorage, theBuffer, 0);
    }

    /** Returns the converter. */
    @SuppressWarnings("null")
    @Override
    public final LongConverter<E> converter() {
        return (LongConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new LongField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((LongField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    @SuppressWarnings("unchecked")
    public long readLong(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readLong((F) this);
    }

    /** Read long value from Storage wrapper buffer if found other wise
         * reads the value from storage.*/
    public long readLong(final Storage theStorage, final StorageBuffer theBuffer) {
        theStorage.checkAccess(this);
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return buffer.lget();
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
        theStorage.write(this, theValue);
    }

    @SuppressWarnings({ "null", "unchecked" })
    public void writeLong(final Storage theStorage, final long theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final long oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.LONG_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Writes the long value to Storage wrapper buffer.*/
    public void writeLong(final Storage theStorage,
            final StorageBuffer theBuffer, final long theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, theValue);
    }
}
