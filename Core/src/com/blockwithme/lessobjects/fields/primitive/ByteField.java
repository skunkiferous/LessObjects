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

import static com.blockwithme.lessobjects.storage.ChangeType.BYTE_FIELD;
import static com.blockwithme.lessobjects.util.StructConstants.BYTE_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.ByteConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Byte field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class ByteField<E, F extends ByteField<E, F>> extends Field<E, F> {

    /** Constructor */
    @SuppressWarnings("boxing")
    protected ByteField(final ByteConverter<E> theConverter,
            final String theName, final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, theBits, isGlobal, isOptional, isVirtual);
    }

    /**
     * Instantiates a new byte field.
     *
     * @param theProperties the properties
     * */
    @SuppressWarnings("boxing")
    protected ByteField(final FieldProperties theProperties) {
        super(validate(theProperties, ByteConverter.class,
                ByteConverter.DEFAULT_BITS));
    }

    /** Constructor */
    @SuppressWarnings("boxing")
    public ByteField(final ByteConverter<E> theConverter, final String theName) {
        super(theConverter, theName, theConverter.bits());
    }

    /** Constructor */
    @SuppressWarnings("boxing")
    public ByteField(final ByteConverter<E> theConverter, final String theName,
            final int theBits) {
        super(theConverter, theName, theBits);
    }

    /** Constructor */
    public ByteField(final String theName) {
        this(theName, BYTE_BITS);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes", "null" })
    public ByteField(final String theName, final int theBits) {
        super((ByteConverter) ByteConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return BYTE_BITS;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, (byte) 0);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeByte(theStorage, theBuffer, (byte) 0);
    }

    /** Returns the converter. */
    @SuppressWarnings("null")
    @Override
    public final ByteConverter<E> converter() {
        return (ByteConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ByteField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((ByteField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    /** Reads byte value of this field from the storage.*/
    @SuppressWarnings("unchecked")
    public byte readByte(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readByte((F) this);
    }

    /** Read byte value from Storage wrapper buffer if found other wise
     * reads the value from storage.*/
    public byte readByte(final Storage theStorage, final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = Util.intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return (byte) buffer.lget();
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
        theStorage.write(this, (byte) theValue);
    }

    /**
     * Write byte.
     *
     * @param theStorage the the storage
     * @param theValue the the value
     */
    @SuppressWarnings({ "null", "unchecked" })
    public void writeByte(final Storage theStorage, final byte theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final byte oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, BYTE_FIELD, theValue, oldValue,
                    storage.getSelectedStructure());
        }
    }

    /** Writes the byte value to Storage wrapper buffer.*/
    public void writeByte(final Storage theStorage,
            final StorageBuffer theBuffer, final byte theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, theValue);
    }
}
