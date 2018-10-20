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
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.FloatConverter;

/**
 * The Class Virtual Field of float type
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class FloatVirtualField<E, F extends FloatVirtualField<E, F>> extends
        FloatField<E, F> implements VirtualField<FloatFieldMapper> {

    /** The mapper. */
    @Nonnull
    private final FloatFieldMapper mapper;

    /**
     * Instantiates a new float virtual field.
     *
     * @param theMapper the mapper
     * @param theProperties the properties
     * */
    private FloatVirtualField(final FloatFieldMapper theMapper,
            final FieldProperties theProperties) {
        super(theProperties);
        mapper = theMapper;
    }

    /**
     * Instantiates a new float field.
     *
     * @param theName the name
     * */
    @SuppressWarnings("boxing")
    public FloatVirtualField(final FloatConverter<E> theConverter,
            final String theName, final FloatFieldMapper theMapper) {
        super(theConverter, theName, false, false, true);
        mapper = theMapper;
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public FloatVirtualField(final String theName,
            final FloatFieldMapper theMapper) {
        this((FloatConverter) FloatConverter.DEFAULT, theName, theMapper);
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
        return (F) new FloatVirtualField(mapper(), theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((FloatVirtualField) theDestinationField,
                theSource.read(this));
    }

    /**
     * @return the mapper
     */
    @Override
    @SuppressWarnings("null")
    public FloatFieldMapper mapper() {
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
    public float readFloat(final Storage theStorage) {
        return mapper.read(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("boxing")
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        mapper().write(converter().fromObject(theValue), root(), theStorage);
    }

    @Override
    public void writeFloat(final Storage theStorage, final float theValue) {
        mapper().write(theValue, root(), theStorage);
    }
}
