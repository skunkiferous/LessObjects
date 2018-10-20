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
package com.blockwithme.lessobjects.fields.object;

import static com.blockwithme.lessobjects.util.Util.intsToLong;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.optional.OptionalField;
import com.blockwithme.lessobjects.fields.virtual.VirtualField;
import com.blockwithme.lessobjects.fields.virtual.ObjectFieldMapper;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.prim.Converter;
import com.carrotsearch.hppc.LongObjectOpenHashMap;

/**
 * Represents an object field for Objects that have a Serializer that
 * converts them to byte[]. This type of Field will have 2 storage implementation -
 * 1. Normal implementation where we expect most of the values to be non-null.
 * 2. Optional Object fields where we except most of the values to be null.
 *
 * @param <E> the data type
 * @param <F> the field type
 *
 * @author tarung
 */
// TODO We need to use a MP template, for serialization.
@ParametersAreNonnullByDefault
public class ObjectField<E, F extends ObjectField<E, F>> extends Field<E, F>
        implements OptionalField<E, F>, VirtualField<ObjectFieldMapper<E>> {

    /** The optional field index. */
    private final int optionalFieldIndex;

    /** The mapper. */
    private final ObjectFieldMapper<E> mapper;

    /**
     * Instantiates a new object field.
     *
     * @param theProperties the properties
     */
    @SuppressWarnings("unchecked")
    protected ObjectField(final FieldProperties theProperties) {
        super(theProperties);
        optionalFieldIndex = -1;
        mapper = null;
    }

    @SuppressWarnings("unchecked")
    protected ObjectField(final FieldProperties theProperties,
            final int theOptionalFieldIndex,
            final ObjectFieldMapper<E> theMapper) {
        super(theProperties);
        optionalFieldIndex = theOptionalFieldIndex;
        mapper = theMapper;
    }

    /** Public constructor */
    public ObjectField(final Converter<E> theConverter, final String theName) {
        super(theConverter, theName, true);
        optionalFieldIndex = -1;
        mapper = null;
    }

    /**
     * Instantiates a new object field.
     *
     * @param theConverter the converter
     * @param theName the name*/
    public ObjectField(final Converter<E> theConverter, final String theName,
            final boolean isGlobal, final boolean isOptional,
            final boolean isVirtual,
            @Nullable final ObjectFieldMapper<E> theMapper) {

        super(theConverter, theName, theConverter.bits(), isGlobal, isOptional,
                isVirtual, true);
        optionalFieldIndex = -1;
        mapper = theMapper;
    }

    /** {@inheritDoc} */
    @Override
    protected int maxBits() {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        theStorage.write(this, null);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage, final StorageBuffer theBuffer) {
        writeObject(theStorage, theBuffer, null);
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public String converterClassName() {
        return converterClassName;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    @Override
    public F copy(final int theNewIndex) {
        return (F) new ObjectField(properties, theNewIndex, this.mapper);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ObjectField(theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((ObjectField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public ObjectFieldMapper<E> mapper() {
        return this.mapper;
    }

    /** {@inheritDoc} */
    @Override
    public int optionalFieldIndex() {
        return optionalFieldIndex;
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public E readAny(final Storage theStorage) {
        return theStorage.read(this);
    }

    /**
     * Read object.
     *
     * @param theStorage the storage
     * @return the stored object or null.
     */
    // CHECKSTYLE.OFF: IllegalType
    @Nullable
    public E readObject(final Storage theStorage) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkAccess(this);
        return storage.read(this);
    }

    /** Read long value from Storage wrapper buffer if found other wise
     *  reads the value from storage.*/
    @Nullable
    public E readObject(final Storage theStorage, final StorageBuffer theBuffer) {
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongObjectOpenHashMap<Object> buffer = theBuffer
                .getObjectBuffer();
        if (buffer.containsKey(key)) {
            return (E) buffer.lget();
        }
        return theStorage.read(this);
    }

    /** Sets theObjectFieldIndex and returns the modified instance.
     *
     * @param theObjectFieldIndex the object field index
     * @return the modified instance. */
    public F setObjectIndex(final int theObjectFieldIndex) {
        return copyInternal(properties.setObjectFieldIndex(theObjectFieldIndex));
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        theStorage.write(this, theValue);
    }

    @Override
    public void writeAnyLong(final long theValue, final Storage theStorage) {
        throw new UnsupportedOperationException(
                "ObjectField cannot be expressed as a long");
    }

    /** Writes the long value to Storage wrapper buffer.*/
    public void writeObject(final Storage theStorage,
            final StorageBuffer theBuffer, @Nullable final E theValue) {
        theStorage.checkAccess(this);
        final int selectedStructure = theStorage.getSelectedStructure();
        final long key = intsToLong(selectedStructure, uniqueIndex());
        final LongObjectOpenHashMap<Object> buffer = theBuffer
                .getObjectBuffer();
        buffer.put(key, theValue);
    }
}
