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

import static com.blockwithme.lessobjects.util.StructConstants.CHAR_BITS;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.CharConverter;

/**
 * The Class Virtual Field of char type
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class CharVirtualField<E, F extends CharVirtualField<E, F>> extends
        CharField<E, F> implements VirtualField<CharFieldMapper> {

    /** The mapper. */
    @Nonnull
    private final CharFieldMapper mapper;

    /**
     * Instantiates a new char virtual field.
     *
     * @param theMapper the mapper
     * @param theProperties the properties
     * */
    private CharVirtualField(final CharFieldMapper theMapper,
            final FieldProperties theProperties) {
        super(theProperties);
        mapper = theMapper;
    }

    /** Constructor */
    public CharVirtualField(final CharConverter<E> theConverter,
            final String theName, final CharFieldMapper theMapper) {
        this(theConverter, theName, theConverter.bits(), theMapper);
    }

    /** Constructor */
    public CharVirtualField(final CharConverter<E> theConverter,
            final String theName, final int theBits,
            final CharFieldMapper theMapper) {
        super(theConverter, theName, theBits, false, false, true);
        mapper = theMapper;
    }

    /** Constructor */
    public CharVirtualField(final String theName,
            final CharFieldMapper theMapper) {
        this(theName, CHAR_BITS, theMapper);
    }

    /** Constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public CharVirtualField(final String theName, final int theBits,
            final CharFieldMapper theMapper) {
        this((CharConverter) CharConverter.DEFAULT, theName, theBits, theMapper);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void clear(final Storage theStorage) {
        mapper().clear(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new CharVirtualField(mapper(), theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((CharVirtualField) theDestinationField,
                theSource.read(this));
    }

    /**
     * @return the mapper
     */
    @Override
    @SuppressWarnings("null")
    public CharFieldMapper mapper() {
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
        return converter().toObject(mapper().read(root(), theStorage));
    }

    @Override
    public char readChar(final Storage theStorage) {
        return mapper().read(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        mapper().write(converter().fromObject(theValue), root(), theStorage);
    }

    @Override
    public void writeChar(final Storage theStorage, final char theValue) {
        mapper().write(theValue, root(), theStorage);
    }
}
