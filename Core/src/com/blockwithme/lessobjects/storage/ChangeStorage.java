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
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.multidim.Arity.ONE_D;
import static com.blockwithme.lessobjects.storage.ActionSetImpl.INITIAL_CAPACITY;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.storage.packed.PackedSparseStorage;

/** The storage class used to store Changes made to another storage. */
@ParametersAreNonnullByDefault
public class ChangeStorage extends PackedSparseStorage {

    @SuppressWarnings("null")
    public ChangeStorage() {
        super(STRUCT.struct(), INITIAL_CAPACITY, false, ONE_D);
    }

    /**
     * Constructor used by the Template class.
     *
     * @param theBuilder the the builder
     */
    public ChangeStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
    }

    /** {@inheritDoc} */
    @Override
    public void checkAccess(final Field<?, ?> theField) {
        // NOP
        // we don't want to perform checks as
        // this is an internal storage.
    }
}