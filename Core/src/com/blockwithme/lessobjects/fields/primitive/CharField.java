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

import static com.blockwithme.lessobjects.util.StructConstants.CHAR_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.CharConverter;
import com.carrotsearch.hppc.LongLongOpenHashMap;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Char field
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class CharField<E, F extends CharField<E, F>> extends Field<E, F> {

    /** Constructor */
    protected CharField(final CharConverter<E> theConverter,
            final String theName, final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        super(theConverter, theName, theBits, isGlobal, isOptional, isVirtual);
    }

    /** Instantiates a new char field.
     *
     * @param theProperties the properties
     *  */
    @SuppressWarnings("boxing")
    protected CharField(final FieldProperties theProperties) {
        super(validate(theProperties, CharConverter.class,
                CharConverter.DEFAULT_BITS));
    }

    /** Constructor */
    public CharField(final CharConverter<E> theConverter, final String theName) {
        super(theConverter, theName);
    }

    /** Constructor */
    public CharField(final CharConverter<E> theConverter, final String theName,
            final int theBits) {
        super(theConverter, theName, theBits);
    }

    /** Constructor */
    public CharField(final String theName) {
        this(theName, CHAR_BITS);
    }

    /** Constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public CharField(final String theName, final int theBits) {
        super((CharConverter) CharConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return CHAR_BITS;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, (char) 0);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeChar(theStorage, theBuffer, (char) 0);
    }

    /** Returns the converter */
    @SuppressWarnings("null")
    @Override
    public final CharConverter<E> converter() {
        return (CharConverter<E>) converter;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new CharField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((CharField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(theStorage.read(this));
    }

    @SuppressWarnings("unchecked")
    public char readChar(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readChar((F) this);
    }

    /** Read byte value from Storage wrapper buffer if found other wise
     * reads the value from storage.*/
    public char readChar(final Storage theStorage, final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = Util.intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        if (buffer.containsKey(key)) {
            return (char) buffer.lget();
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
        theStorage.write(this, (char) theValue);
    }

    @SuppressWarnings({ "null", "unchecked" })
    public void writeChar(final Storage theStorage, final char theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final char oldValue = storage.writeImpl((F) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((F) this, ChangeType.CHAR_FIELD, theValue,
                    oldValue, storage.getSelectedStructure());
        }
    }

    /** Writes the byte value to Storage wrapper buffer.*/
    public void writeChar(final Storage theStorage,
            final StorageBuffer theBuffer, final char theValue) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongLongOpenHashMap buffer = theBuffer.getBuffer();
        buffer.put(key, theValue);
    }
}
