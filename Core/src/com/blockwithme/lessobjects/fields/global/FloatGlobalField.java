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
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.FloatConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Float global field.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class FloatGlobalField<E, F extends FloatField<E, F>> extends
        FloatField<E, F> {

    /**
     * Instantiates a new float global field.
     *
     * @param theProperties the properties.
     */
    @SuppressWarnings("boxing")
    private FloatGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Instantiates a new float field.
     *
     * @param theName the name
     *  */
    @SuppressWarnings("boxing")
    public FloatGlobalField(final FloatConverter<E> theConverter,
            final String theName) {
        super(theConverter, theName, true, false, false);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FloatGlobalField(final String theName) {
        this((FloatConverter) FloatConverter.DEFAULT, theName);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new FloatGlobalField(theProperties);
    }

    @Override
    public float readFloat(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        if (storage != null) {
            storage.checkAccess(this);
            return storage.readFloat((FloatField) this);
        }
        throw new IllegalArgumentException(
                "Global storage not found in the current Storage!");
    }

    @Override
    @SuppressWarnings("null")
    public void writeFloat(final Storage theStorage, final float theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final float oldValue = globalStorage.writeImpl((FloatField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((FloatField) this, ChangeType.FLOAT_GLOBAL,
                    theValue, oldValue, -1);
        }
    }

}
