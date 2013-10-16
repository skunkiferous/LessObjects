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
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.DoubleConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Double global field.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class DoubleGlobalField<E, F extends DoubleGlobalField<E, F>> extends
        DoubleField<E, F> {

    /**
     * Instantiates a new double global field.
     *
     * @param theProperties the properties.
     */
    @SuppressWarnings("boxing")
    private DoubleGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Constructor */
    public DoubleGlobalField(final DoubleConverter<E> theConverter,
            final String theName) {
        super(theConverter, theName, true, false, false);
    }

    /** Constructor */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DoubleGlobalField(final String theName) {
        this((DoubleConverter) DoubleConverter.DEFAULT, theName);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new DoubleGlobalField(theProperties);
    }

    @Override
    public double readDouble(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        if (storage != null) {
            storage.checkAccess(this);
            return storage.readDouble((DoubleField) this);
        }
        throw new IllegalArgumentException(
                "Global storage not found in the current Storage!");
    }

    @Override
    @SuppressWarnings("null")
    public void writeDouble(final Storage theStorage, final double theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final double oldValue = globalStorage.writeImpl((DoubleField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((DoubleField) this, ChangeType.DOUBLE_GLOBAL,
                    theValue, oldValue, -1);
        }
    }
}
