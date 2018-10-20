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

import static com.blockwithme.lessobjects.util.StructConstants.INT_BITS;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.IntConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Int global field.
 *
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class IntGlobalField<E, F extends IntGlobalField<E, F>> extends
        IntField<E, F> {

    /**
     * Instantiates a new int global field.
     *
     * @param theProperties the properties.
     *
     * */
    @SuppressWarnings("boxing")
    private IntGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    public IntGlobalField(final IntConverter<E> theConverter,
            final String theName) {
        this(theConverter, theName, theConverter.bits());
    }

    /** Public constructor */
    @SuppressWarnings("boxing")
    public IntGlobalField(final IntConverter<E> theConverter,
            final String theName, final int theBits) {
        super(theConverter, theName, theBits, true, false, false);
    }

    /** Public constructor */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public IntGlobalField(final String theName) {
        this((IntConverter) IntConverter.DEFAULT, theName, INT_BITS);
    }

    /** Public constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public IntGlobalField(final String theName, final int theBits) {
        this((IntConverter) IntConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new IntGlobalField(theProperties);
    }

    @Override
    public int readInt(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        if (storage != null) {
            storage.checkAccess(this);
            return storage.readInt((IntField) this);
        }
        throw new IllegalArgumentException(
                "Global storage not found in the current Storage!");
    }

    @Override
    @SuppressWarnings("null")
    public void writeInt(final Storage theStorage, final int theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final int oldValue = globalStorage.writeImpl((IntField) this, theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((IntField) this, ChangeType.INT_GLOBAL,
                    theValue, oldValue, -1);
        }
    }

}
