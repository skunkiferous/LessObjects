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

import static com.blockwithme.lessobjects.util.StructConstants.SHORT_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.lessobjects.util.StructConstants;
import com.blockwithme.prim.ShortConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Short field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ShortField<E, F extends ShortField<E, F>> extends Field<E, F> {

    /** Instantiates a new short field.
     *
     * @param theProperties the properties
      */
    @SuppressWarnings("boxing")
    protected ShortField(final FieldProperties theProperties) {
        super(validate(theProperties, ShortConverter.class,
                ShortConverter.DEFAULT_BITS));
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    protected ShortField(final ShortConverter<E> theConverter,
            final String theName, final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, theBits, isGlobal, isOptional, isVirtual);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortField(final ShortConverter<E> theConverter, final String theName) {
        super(theConverter, theName);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortField(final ShortConverter<E> theConverter,
            final String theName, final int theBits) {
        super(theConverter, theName, theBits);
    }

    /** Constructor */
    public ShortField(final String theName) {
        this(theName, SHORT_BITS);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes", "null" })
    public ShortField(final String theName, final int theBits) {
        super((ShortConverter) ShortConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return StructConstants.B_TWO_BYTES;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, (short) 0);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeShort(theStorage, theBuffer, (short) 0);
    }

    /** Return the converter. */
    @SuppressWarnings("null")
    @Override
    public final ShortConverter<E> converter() {
        return (ShortConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ShortField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((ShortField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    /** Reads and returns short value at current index from the Storage. */
    @SuppressWarnings("unchecked")
    public short readShort(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readShort((F) this);
    }

    /** Read short value from Storage wrapper buffer if found other wise
      * reads the value from storage.*/
    public short readShort(final Storage theStorage,
            final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return (short) buffer.lget();
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
        theStorage.write(this, (short) theValue);
    }

    /** Writes the short value at current index to the Storage. */
    @SuppressWarnings({ "null", "unchecked" })
    public void writeShort(final Storage theStorage, final short theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final short oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.SHORT_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Writes the short value to Storage wrapper buffer.*/
    public void writeShort(final Storage theStorage,
            final StorageBuffer theBuffer, final short theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, theValue);
    }
}
