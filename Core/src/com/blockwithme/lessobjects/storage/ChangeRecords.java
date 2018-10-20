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
package com.blockwithme.lessobjects.storage;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.ValueChange;

/**
 * The ChangeRecords interface, wraps all the changes made to a storage object for
 * a particular transaction
 *
 * @see com.blockwithme.lessobjects.storage.TransactionManager
 * @see com.blockwithme.lessobjects.storage.ActionSet
 * @see ValueChange
 * @see ProxyValueChangeListener
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ChangeRecords {

    /** Iterator over the collection of changes,
     * the 'ValueChange' instances returned by this method are valid only till the next call to Iterator.next().*/
    Iterator<ValueChange<?>> changes(Struct theStruct);

    /** The Children ChangeRecord Objects */
    public Set<? extends ChangeRecords> children();

    /**
     * Checks if ChangeRecords object is empty.
     *
     * @return true, if empty
     */
    public boolean isEmpty();

}
