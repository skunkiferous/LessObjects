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

import static com.blockwithme.lessobjects.util.StructConstants.SHORT_BITS;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.ShortConverter;

/**
 * The Class Virtual Field of short type
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ShortVirtualField<E, F extends ShortVirtualField<E, F>> extends
        ShortField<E, F> implements VirtualField<ShortFieldMapper> {

    /** The mapper. */
    @Nonnull
    private final ShortFieldMapper mapper;

    /**
     * Instantiates a new short virtual field.
     *
     * @param theMapper the mapper
     * @param theProperties the properties
    */
    private ShortVirtualField(final ShortFieldMapper theMapper,
            final FieldProperties theProperties) {
        super(theProperties);
        mapper = theMapper;
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortVirtualField(final ShortConverter<E> theConverter,
            final String theName, final int theBits,
            final ShortFieldMapper theMapper) {
        super(theConverter, theName, theBits, false, false, true);
        mapper = theMapper;
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortVirtualField(final ShortConverter<E> theConverter,
            final String theName, final ShortFieldMapper theMapper) {
        this(theConverter, theName, theConverter.bits(), theMapper);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes", "null" })
    public ShortVirtualField(final String theName, final int theBits,
            final ShortFieldMapper theMapper) {
        this((ShortConverter) ShortConverter.DEFAULT, theName, theBits,
                theMapper);
    }

    /** Constructor */
    public ShortVirtualField(final String theName,
            final ShortFieldMapper theMapper) {
        this(theName, SHORT_BITS, theMapper);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void clear(final Storage theStorage) {
        mapper.clear(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ShortVirtualField(mapper(), theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((ShortVirtualField) theDestinationField,
                theSource.read(this));
    }

    /**
     * @return the mapper
     */
    @Override
    @SuppressWarnings("null")
    public ShortFieldMapper mapper() {
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
    public short readShort(final Storage theStorage) {
        return mapper.read(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        mapper().write(converter().fromObject(theValue), root(), theStorage);
    }

    @Override
    public void writeShort(final Storage theStorage, final short theValue) {
        mapper().write(theValue, root(), theStorage);
    }

}
