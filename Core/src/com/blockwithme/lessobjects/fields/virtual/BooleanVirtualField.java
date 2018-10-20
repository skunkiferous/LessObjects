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
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.BooleanConverter;

/**
 * The Class Virtual Field of boolean type
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @see BooleanFieldMapper
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class BooleanVirtualField<E, F extends BooleanVirtualField<E, F>>
        extends BooleanField<E, F> implements VirtualField<BooleanFieldMapper> {

    /** The mapper. */
    @Nonnull
    private final BooleanFieldMapper mapper;

    /**
     * Instantiates a new boolean virtual field.
     *
     * @param theMapper the mapper
     * @param theProperties the properties
     * */
    private BooleanVirtualField(final BooleanFieldMapper theMapper,
            final FieldProperties theProperties) {
        super(theProperties);
        mapper = theMapper;
    }

    /** Public constructor */
    public BooleanVirtualField(final BooleanConverter<E> theConverter,
            final String theName, final BooleanFieldMapper theMapper) {
        super(theConverter, theName, false, false, true);
        mapper = theMapper;
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public BooleanVirtualField(final String theName,
            final BooleanFieldMapper theMapper) {
        this((BooleanConverter) BooleanConverter.DEFAULT, theName, theMapper);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        mapper().clear(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new BooleanVirtualField(mapper(), theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((BooleanVirtualField) theDestinationField,
                theSource.read(this));
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public BooleanFieldMapper mapper() {
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
    public boolean readBoolean(final Storage theStorage) {
        return mapper.read(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        mapper().write(converter().fromObject(theValue), root(), theStorage);
    }

    @Override
    public void writeBoolean(final Storage theStorage, final boolean theValue) {
        mapper().write(theValue, root(), theStorage);
    }
}
