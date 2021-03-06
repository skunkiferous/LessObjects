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

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The Interface ActionSet, wraps the list of events generated by all the change
 * listeners and List of "ValueChange" objects. An instance of this interface is
 * returned at the time of commit or roll-back performed on a storage object.
 *
 * @see TransactionManager
 * @see ChangeRecords
 * @see com.blockwithme.lessobjects.ValueChangeListener
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ActionSet {

    /** @return the change list */
    ChangeRecords changeRecords();

    /** Returns the current changes in form of Change Storage, so that the changes can be serialized.
    *
    * @return the change storage, null if there are no changes.
    */
    @Nullable
    ChangeStorage currentChanges();

    /**
     * Events are additional "actions" recorded by the change listeners,
     * to represent changes that cannot be expressed as simple value changes.
     *
     * @return the list */
    List<Object> events();

}
