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

import static com.blockwithme.lessobjects.util.StructConstants.INT_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.prim.IntConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Int field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class IntField<E, F extends IntField<E, F>> extends Field<E, F> {

    /**
     * Instantiates a new int field.
     *
     * @param theProperties the properties
     */
    @SuppressWarnings("boxing")
    protected IntField(final FieldProperties theProperties) {
        super(validate(theProperties, IntConverter.class,
                IntConverter.DEFAULT_BITS));
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    protected IntField(final IntConverter<E> theConverter,
            final String theName, final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, theBits, isGlobal, isOptional, isVirtual);
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    public IntField(final IntConverter<E> theConverter, final String theName) {
        super(theConverter, theName);
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    public IntField(final IntConverter<E> theConverter, final String theName,
            final int theBits) {
        super(theConverter, theName, theBits);
    }

    /** Public constructor */
    public IntField(final String theName) {
        this(theName, INT_BITS);
    }

    /** Public constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes", "null" })
    public IntField(final String theName, final int theBits) {
        super((IntConverter) IntConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return INT_BITS;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeInt(theStorage, theBuffer, 0);
    }

    /** Returns the converter. */
    @SuppressWarnings("null")
    @Override
    public final IntConverter<E> converter() {
        return (IntConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new IntField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((IntField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    @SuppressWarnings("unchecked")
    public int readInt(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readInt((F) this);
    }

    /** Read int value from Storage wrapper buffer if found other wise
     * reads the value from storage. */
    public int readInt(final Storage theStorage, final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return (int) buffer.lget();
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
        theStorage.write(this, (int) theValue);
    }

    /** Write int value to the storage. */
    @SuppressWarnings({ "null", "unchecked" })
    public void writeInt(final Storage theStorage, final int theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final int oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.INT_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Writes the int value to Storage wrapper buffer.*/
    public void writeInt(final Storage theStorage,
            final StorageBuffer theBuffer, final int theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, theValue);
    }
}
