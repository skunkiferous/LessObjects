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
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.CharConverter;

//CHECKSTYLE.OFF: IllegalType
/** Represents a Char global field */
@ParametersAreNonnullByDefault
public class CharGlobalField<E, F extends CharGlobalField<E, F>> extends
        CharField<E, F> {

    /**
     * Instantiates a new char global field.
     *
     * @param theProperties the properties.
     * */
    @SuppressWarnings("boxing")
    private CharGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Constructor */
    public CharGlobalField(final CharConverter<E> theConverter,
            final String theName) {
        this(theConverter, theName, theConverter.bits());
    }

    /** Constructor */
    public CharGlobalField(final CharConverter<E> theConverter,
            final String theName, final int theBits) {
        super(theConverter, theName, theBits, true, false, false);
    }

    /** Constructor */
    public CharGlobalField(final String theName) {
        this(theName, CharConverter.DEFAULT_BITS);
    }

    /** Constructor */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CharGlobalField(final String theName, final int theBits) {
        this((CharConverter) CharConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new CharGlobalField(theProperties);
    }

    @SuppressWarnings("null")
    @Override
    public char readChar(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        checkNotNull(storage,
                "Global storage not found in the current Storage!");
        storage.checkAccess(this);
        return storage.readChar((CharField) this);
    }

    @Override
    @SuppressWarnings("null")
    public void writeChar(final Storage theStorage, final char theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final char oldValue = globalStorage.writeImpl((CharField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((CharField) this, ChangeType.CHAR_GLOBAL,
                    theValue, oldValue, -1);
        }
    }
}
