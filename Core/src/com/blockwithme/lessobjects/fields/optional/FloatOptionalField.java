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
package com.blockwithme.lessobjects.fields.optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.FloatConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Float Optional field. Optional fields are the fields where the
 * field value is expected to be a default value most of the times
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class FloatOptionalField<E, F extends FloatField<E, F>> extends
        FloatField<E, F> implements OptionalField<E, F> {

    /** The optional field index. */
    private final int optionalFieldIndex;

    /**
     * Instantiates a new float optional.
     *
     * @param theOptionalFieldIndex the optional field index
     * @param theProperties the properties
     * */
    @SuppressWarnings("boxing")
    private FloatOptionalField(final int theOptionalFieldIndex,
            final FieldProperties theProperties) {
        super(theProperties);
        optionalFieldIndex = theOptionalFieldIndex;
    }

    /**
     * Instantiates a new float field.
     *
     * @param theName the name
     * */
    @SuppressWarnings("boxing")
    public FloatOptionalField(final FloatConverter<E> theConverter,
            final String theName, final int theOptionalFieldIndex) {
        super(theConverter, theName, false, true, false);
        optionalFieldIndex = theOptionalFieldIndex;
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public FloatOptionalField(final String theName, final int theOptionalFieldIndex) {
        this((FloatConverter) FloatConverter.DEFAULT, theName,
                theOptionalFieldIndex);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(final Storage theStorage) {
        ((AbstractStorage) theStorage).clearOptional(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copy(final int theNewIndex) {
        return (F) new FloatOptionalField(theNewIndex, properties());
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new FloatOptionalField(optionalFieldIndex(), theProperties);
    }

    /** {@inheritDoc} */
    @Override
    public int optionalFieldIndex() {
        return optionalFieldIndex;
    }

    @Override
    public float readFloat(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readOptional(this);
    }

    @Override
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void writeFloat(final Storage theStorage, final float theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final float oldValue = storage.writeOptional(this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((FloatField) this, ChangeType.FLOAT_OPTIONAL,
                    theValue, oldValue, storage.getSelectedStructure());
        }
    }
}
