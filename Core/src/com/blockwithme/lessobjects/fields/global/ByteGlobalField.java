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
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.storage.AbstractStorage;
import com.blockwithme.lessobjects.storage.ChangeType;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.ByteConverter;

//CHECKSTYLE.OFF: IllegalType

/** Represents a Byte Global field */
@ParametersAreNonnullByDefault
public class ByteGlobalField<E, F extends ByteGlobalField<E, F>> extends
        ByteField<E, F> {

    /**
     * Instantiates a new byte global field.
     *
     * @param theProperties the properties
     * */
    @SuppressWarnings("boxing")
    private ByteGlobalField(final FieldProperties theProperties) {
        super(theProperties);
    }

    /** Constructor */
    @SuppressWarnings("boxing")
    public ByteGlobalField(final ByteConverter<E> theConverter,
            final String theName) {
        this(theConverter, theName, theConverter.bits());
    }

    /** Constructor */
    @SuppressWarnings("boxing")
    public ByteGlobalField(final ByteConverter<E> theConverter,
            final String theName, final int theBits) {
        super(theConverter, theName, theBits, true, false, false);
    }

    /** Constructor */
    public ByteGlobalField(final String theName) {
        this(theName, ByteConverter.DEFAULT_BITS);
    }

    /** Constructor */
    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public ByteGlobalField(final String theName, final int theBits) {
        this((ByteConverter) ByteConverter.DEFAULT, theName, theBits);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public F copyInternal(final FieldProperties theProperties) {
        return (F) new ByteGlobalField(theProperties);
    }

    @SuppressWarnings("null")
    @Override
    public byte readByte(final Storage theStorage) {
        final AbstractStorage storage = ((AbstractStorage) theStorage)
                .globalFieldsStorage();
        checkNotNull(storage,
                "Global storage not found in the current Storage!");
        storage.checkAccess(this);
        return storage.readByte((ByteField) this);
    }

    @Override
    @SuppressWarnings("null")
    public void writeByte(final Storage theStorage, final byte theValue) {
        final AbstractStorage storage = (AbstractStorage) theStorage;
        final AbstractStorage globalStorage = storage.globalFieldsStorage();
        checkNotNull(globalStorage,
                "Global storage not found in the current Storage!");
        globalStorage.checkReadOnly();
        globalStorage.checkAccess(this);
        final byte oldValue = globalStorage.writeImpl((ByteField) this,
                theValue);
        if (storage.transactionsEnabled() && oldValue != theValue) {
            storage.publishChange((ByteField) this, ChangeType.BYTE_GLOBAL,
                    theValue, oldValue, -1);
        }
    }
}
