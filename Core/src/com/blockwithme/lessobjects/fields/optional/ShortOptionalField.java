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

import static com.blockwithme.lessobjects.util.StructConstants.SHORT_BITS;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.ShortConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Short Optional field. Optional fields are the fields where the
 * field value is expected to be a default value most of the times.
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ShortOptionalField<E, F extends ShortOptionalField<E, F>> extends
        ShortField<E, F> implements OptionalField<E, F> {

    /** The optional field index. */
    private final int optionalFieldIndex;

    /** Private constructor */
    @SuppressWarnings("boxing")
    private ShortOptionalField(final int theOptionalFieldIndex,
            final FieldProperties theProperties) {
        super(theProperties);
        optionalFieldIndex = theOptionalFieldIndex;
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortOptionalField(final ShortConverter<E> theConverter,
            final String theName, final int theOptionalFieldIndex) {
        this(theConverter, theName, theConverter.bits(), theOptionalFieldIndex);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortOptionalField(final ShortConverter<E> theConverter,
            final String theName, final int theBits,
            final int theOptionalFieldIndex) {
        super(theConverter, theName, theBits, false, true, false);
        optionalFieldIndex = theOptionalFieldIndex;
    }

    /** Constructor */
    public ShortOptionalField(final String theName, final int theOptionalFieldIndex) {
        this(theName, SHORT_BITS, theOptionalFieldIndex);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes", "null" })
    public ShortOptionalField(final String theName, final int theBits,
            final int theOptionalFieldIndex) {
        this((ShortConverter) ShortConverter.DEFAULT, theName, theBits,
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
        return (F) new ShortOptionalField(theNewIndex, properties());
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ShortOptionalField(optionalFieldIndex(), theProperties);
    }

    /** {@inheritDoc} */
    @Override
    public int optionalFieldIndex() {
        return optionalFieldIndex;
    }

    @Override
    public short readShort(final Storage theStorage) {
        theStorage.checkAccess(this);
        return ((AbstractStorage) theStorage).readOptional(this);
    }

    @Override
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void writeShort(final Storage theStorage, final short theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        storage.checkReadOnly();
        storage.checkAccess(this);
        final short oldValue = storage.writeOptional(this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((ShortField) this, ChangeType.SHORT_OPTIONAL,
                    theValue, oldValue, storage.getSelectedStructure());
        }
    }
}
