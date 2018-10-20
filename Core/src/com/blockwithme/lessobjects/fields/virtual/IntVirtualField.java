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

import static com.blockwithme.lessobjects.util.StructConstants.INT_BITS;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.Util;
import com.blockwithme.prim.IntConverter;

/**
 * The Class Virtual Field int type
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class IntVirtualField<E, F extends IntVirtualField<E, F>> extends
        IntField<E, F> implements VirtualField<IntFieldMapper> {

    /** The mapper. */
    @Nonnull
    private final IntFieldMapper mapper;

    /**
     * Instantiates a new int virtual field.
     *
     * @param theMapper the mapper
     * @param theProperties the properties
    */
    private IntVirtualField(final IntFieldMapper theMapper,
            final FieldProperties theProperties) {
        super(theProperties);
        mapper = theMapper;
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    public IntVirtualField(final IntConverter<E> theConverter,
            final String theName, final int theBits,
            final IntFieldMapper theMapper) {
        super(theConverter, theName, theBits, false, false, true);
        mapper = theMapper;
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    public IntVirtualField(final IntConverter<E> theConverter,
            final String theName, final IntFieldMapper theMapper) {
        this(theConverter, theName, theConverter.bits(), theMapper);
    }

    /** Public constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes", "null" })
    public IntVirtualField(final String theName, final int theBits,
            final IntFieldMapper theMapper) {
        this((IntConverter) IntConverter.DEFAULT, theName, theBits, theMapper);
    }

    /** Public constructor */
    public IntVirtualField(final String theName, final IntFieldMapper theMapper) {
        this(theName, INT_BITS, theMapper);
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
        return (F) new IntVirtualField(mapper(), theProperties);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("rawtypes")
    @Override
    public void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination) {
        theDestination.write((IntVirtualField) theDestinationField,
                theSource.read(this));
    }

    /**
     * @return the mapper
     */
    @Override
    @SuppressWarnings("null")
    public IntFieldMapper mapper() {
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

    /** {@inheritDoc} */
    @Override
    public int readInt(final Storage theStorage) {
        return mapper.read(root(), theStorage);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "boxing", "null" })
    @Override
    public void writeAny(final E theValue, final Storage theStorage) {
        mapper().write(converter().fromObject(theValue), root(), theStorage);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(final Storage theStorage, final int theValue) {
        mapper().write(theValue, root(), theStorage);
    }
}
