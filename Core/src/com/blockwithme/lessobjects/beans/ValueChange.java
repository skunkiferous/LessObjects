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
/**
 *
 */
package com.blockwithme.lessobjects.beans;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.storage.Storage;

/** An object of this class contains change event details like Field object,
 * old value, new value, Struct index etc.
 *
 * @param <E> the data type
 * @see com.blockwithme.lessobjects.storage.TransactionManager
 * @see com.blockwithme.lessobjects.storage.ChangeRecords
 * @see com.blockwithme.lessobjects.ValueChangeListener
 * @see com.blockwithme.lessobjects.storage.ActionSet
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public interface ValueChange<E> {

    /** Apply this change on a Storage object.
     *
     * @param theStorage the storage
     * */
    void applyChange(final Storage theStorage);

    /** Field that was modified
     *
     * @return the field */
    <F extends Field<E, F>> Field<E, F> field();

    /** New value, Each implementation class also has a corresponding primitive
     * type method to avoid auto-boxing.
     *
     * @return the new value */
    E newValue();

    /** Old value, Each implementation class also has a corresponding primitive
     * type method to avoid auto-boxing.
     *
     * @return the old value */
    E oldValue();

    /** Reverse the change.
     *
     * @param theStorage the storage */
    void reverseChange(final Storage theStorage);

    /** Structure index
     *
     * @return the int */
    int structureIndex();
}
