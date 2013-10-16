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
package com.blockwithme.lessobjects.fields.global;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.LongConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a long global field
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class LongGlobalField<E, F extends LongGlobalField<E, F>> extends
        LongField<E, F> {

    /**
     * Instantiates a new long global field.
     *
     * @param theProperties the properties
     *
     * */
    @SuppressWarnings("boxing")
    private LongGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** constructor */
    @SuppressWarnings("boxing")
    public LongGlobalField(final LongConverter<E> theConverter,
            final String theName) {
        this(theConverter, theName, theConverter.bits());
    }

    /** constructor */
    @SuppressWarnings("boxing")
    public LongGlobalField(final LongConverter<E> theConverter,
            final String theName, final int theBits) {
        super(theConverter, theName, theBits, true, false, false);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LongGlobalField(final String theName) {
        this(theName, LongConverter.DEFAULT_BITS);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LongGlobalField(final String theName, final int theBits) {
        this((LongConverter) LongConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new LongGlobalField(theProperties);
    }

    @Override
    public long readLong(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        if (storage != null) {
            storage.checkAccess(this);
            return storage.readLong((LongField) this);
        }
        throw new IllegalArgumentException(
                "Global storage not found in the current Storage!");
    }

    @Override
    @SuppressWarnings("null")
    public void writeLong(final Storage theStorage, final long theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final long oldValue = globalStorage.writeImpl((LongField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((LongField) this, ChangeType.LONG_GLOBAL,
                    theValue, oldValue, -1);
        }
    }
}
