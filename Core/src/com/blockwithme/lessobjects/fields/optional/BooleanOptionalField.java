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
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.BooleanConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Boolean Optional field. Optional fields are the fields where the
 * field value is expected to be a default value most of the times.
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class BooleanOptionalField<E, F extends BooleanOptionalField<E, F>> extends
        BooleanField<E, F> implements OptionalField<E, F> {

    /** The optional field index. */
    private final int optionalFieldIndex;

    /**
     * Instantiates a new boolean optional.
     *
     * @param theOptionalFieldIndex the optional field index
     * */
    @SuppressWarnings("boxing")
    private BooleanOptionalField(final FieldProperties theProperties,
            final int theOptionalFieldIndex) {
        super(theProperties);
        optionalFieldIndex = theOptionalFieldIndex;
    }

    /** Public constructor */
    public BooleanOptionalField(final BooleanConverter<E> theConverter,
            final String theName, final int theOptionalFieldIndex) {
        super(theConverter, theName, false, true, false);
        optionalFieldIndex = theOptionalFieldIndex;
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public BooleanOptionalField(final String theName, final int theOptionalFieldIndex) {
        this((BooleanConverter) BooleanConverter.DEFAULT, theName,
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
        return (F) new BooleanOptionalField(properties(), theNewIndex);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new BooleanOptionalField(theProperties, optionalFieldIndex());
    }

    /** {@inheritDoc} */
    @Override
    public int optionalFieldIndex() {
        return optionalFieldIndex;
    }

    /** {@inheritDoc} */
    @Override
    public boolean readBoolean(final Storage theStorage) {

        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readOptional(this);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void writeBoolean(final Storage theStorage, final boolean theValue) {

        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final boolean oldValue = storage.writeOptional(this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((BooleanField) this,
                    ChangeType.BOOLEAN_OPTIONAL, theValue, oldValue,
                    storage.getSelectedStructure());
        }
    }
}
