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

import static com.blockwithme.lessobjects.util.StructConstants.SHORT_BITS;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.ShortConverter;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Short global field
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ShortGlobalField<E, F extends ShortGlobalField<E, F>> extends
        ShortField<E, F> {

    /**
     * Instantiates a new short global field.
     *
     * @param theProperties the properties
     * */
    @SuppressWarnings("boxing")
    private ShortGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortGlobalField(final ShortConverter<E> theConverter,
            final String theName) {
        this(theConverter, theName, theConverter.bits());
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortGlobalField(final ShortConverter<E> theConverter,
            final String theName, final int theBits) {
        super(theConverter, theName, theBits, true, false, false);
    }

    /** Constructor */
    public ShortGlobalField(final String theName) {
        this(theName, SHORT_BITS);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ShortGlobalField(final String theName, final int theBits) {
        this((ShortConverter) ShortConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ShortGlobalField(theProperties);
    }

    @SuppressWarnings("null")
    @Override
    public short readShort(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        checkNotNull(storage,
                "Global storage not found in the current Storage!");
        storage.checkAccess(this);
        return storage.readShort((ShortField) this);
    }

    @Override
    @SuppressWarnings("null")
    public void writeShort(final Storage theStorage, final short theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final short oldValue = globalStorage.writeImpl((ShortField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((ShortField) this, ChangeType.SHORT_GLOBAL,
                    theValue, oldValue, -1);
        }
    }

}
