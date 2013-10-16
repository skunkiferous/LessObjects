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
package com.blockwithme.lessobjects.fields.virtual;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.LongConverter;

/**
 * The Class Virtual Field of Long type
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class LongVirtualField<E, F extends LongVirtualField<E, F>> extends
        LongField<E, F> implements VirtualField<LongFieldMapper> {

    /** The mapper. */
    @Nonnull
    private final LongFieldMapper mapper;

    /**
     * Instantiates a new long virtual field.
     *
     * @param theMapper the mapper
     * @param theProperties the properties
     * */
    private LongVirtualField(final LongFieldMapper theMapper,
            final FieldProperties theProperties) {
        super(theProperties);
        mapper = theMapper;
    }

    /** constructor */
    @SuppressWarnings("boxing")
    public LongVirtualField(final LongConverter<E> theConverter,
            final String theName, final int theBits,
            final LongFieldMapper theMapper) {
        super(theConverter, theName, theBits, false, false, true);
        mapper = theMapper;
    }

    /** constructor */
    @SuppressWarnings("boxing")
    public LongVirtualField(final LongConverter<E> theConverter,
            final String theName, final LongFieldMapper theMapper) {
        this(theConverter, theName, theConverter.bits(), theMapper);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public LongVirtualField(final String theName, final int theBits,
            final LongFieldMapper theMapper) {
        this((LongConverter) LongConverter.DEFAULT, theName, theBits, theMapper);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public LongVirtualField(final String theName,
            final LongFieldMapper theMapper) {
        this((LongConverter) LongConverter.DEFAULT, theName, theMapper);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        mapper.clear(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new LongVirtualField(mapper(), theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((LongVirtualField) theDestinationField,
                theSource.read(this));
    }

    /**
     * @return the mapper
     */
    @Override
    @SuppressWarnings("null")
    public LongFieldMapper mapper() {
        return mapper;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public String mapperClassName() {
        return Util.getClassName(mapper.getClass());
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public E readAny(final Storage theStorage) {
        return converter().toObject(mapper.read(root(), theStorage));
    }

    @Override
    public long readLong(final Storage theStorage) {
        return mapper.read(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        mapper().write(converter().fromObject(theValue), root(), theStorage);
    }

    @Override
    public void writeLong(final Storage theStorage, final long theValue) {
        mapper().write(theValue, root(), theStorage);
    }
}
