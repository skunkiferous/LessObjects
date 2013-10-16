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

import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.prim.BooleanConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Boolean field. fillBits is false, because a boolean should be
 * stored as one bit, but a real java boolean always takes 8 bits!
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class BooleanField<E, F extends BooleanField<E, F>> extends Field<E, F> {

    /** Public constructor */
    protected BooleanField(final BooleanConverter<E> theConverter,
            final String theName, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, isGlobal, isOptional, isVirtual);
    }

    /** Instantiates a new boolean field.
     *
     * @param theProperties the properties.
     *  */
    @SuppressWarnings("boxing")
    protected BooleanField(final FieldProperties theProperties) {
        super(validate(theProperties, BooleanConverter.class,
                BooleanConverter.DEFAULT_BITS));
    }

    /** Public constructor */
    public BooleanField(final BooleanConverter<E> theConverter,
            final String theName) {
        super(theConverter, theName);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public BooleanField(final String theName) {
        super((BooleanConverter) BooleanConverter.DEFAULT, theName);
    }

    /** {@inheritDoc} */
    @Override
    protected final int maxBits() {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, false);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeBoolean(theStorage, theBuffer, false);
    }

    /** Returns the converter. */
    @SuppressWarnings("null")
    @Override
    public final BooleanConverter<E> converter() {
        return (BooleanConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new BooleanField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((BooleanField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    @SuppressWarnings("unchecked")
    public boolean readBoolean(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readBoolean((F) this);
    }

    public boolean readBoolean(final Storage theStorage,
            final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return buffer.lget() != 0;
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
        theStorage.write(this, theValue != 0);
    }

    /** Write boolean to the storage. */
    @SuppressWarnings({ "null", "unchecked" })
    public void writeBoolean(final Storage theStorage, final boolean theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final boolean oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.BOOLEAN_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Write boolean to the storage wrapper buffer. */
    public void writeBoolean(final Storage theStorage,
            final StorageBuffer theBuffer, final boolean theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, theValue ? 1L : 0L);
    }
}
