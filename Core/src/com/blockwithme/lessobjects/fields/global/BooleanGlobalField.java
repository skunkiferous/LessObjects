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
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.BooleanConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Boolean Global field.
 *
 * @param <E> the element type
 * @param <F> the field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class BooleanGlobalField<E, F extends BooleanGlobalField<E, F>> extends
        BooleanField<E, F> {

    /** Instantiates a new boolean global field.
     *
     * @param theProperties the properties.
     * */
    @SuppressWarnings("boxing")
    private BooleanGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Public constructor */
    public BooleanGlobalField(final BooleanConverter<E> theConverter,
            final String theName) {
        super(theConverter, theName, true, false, false);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    public BooleanGlobalField(final String theName) {
        this((BooleanConverter) BooleanConverter.DEFAULT, theName);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new BooleanGlobalField(theProperties);
    }

    @SuppressWarnings({ "null", "rawtypes", "unchecked" })
    @Override
    public boolean readBoolean(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        checkNotNull(storage,
                "Global storage not found in the current Storage!");
        storage.checkAccess(this);
        return storage.readBoolean((BooleanField) this);
    }

    @Override
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void writeBoolean(final Storage theStorage, final boolean theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final boolean oldValue = globalStorage.writeImpl((BooleanField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((BooleanField) this,
                    ChangeType.BOOLEAN_GLOBAL, theValue, oldValue, -1);
        }
    }
}
