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

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The TransactionManager instance associated with a Storage object.
 * The storage objects are transactional in nature, one can commit or rollback
 * a list of changes performed on a Storage object.
 *
 * @see ProxyValueChangeListener
 * @see ActionSet
 * @see com.blockwithme.lessobjects.storage.ChangeRecords
 * @see com.blockwithme.lessobjects.beans.ValueChange
 * @see Storage
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface TransactionManager {

    /**
     * Commit all the changes on the associated storage object, since the last
     * commit or rollback
     *
     * @return the list of 'events' and 'changes' generated since last
     *         commit/rollback
     */
    ActionSet commit();

    /**
     * Checks if the Transaction is uncommitted.
     *
     * @return true, if uncommitted
     */
    boolean isUncommitted();

    /**
     * Rollback all the changes on the associated storage object, since the last
     * commit or rollback.
     *
     * @return the list of 'events' and 'changes' generated since last
     *         commit/rollback
     */
    ActionSet rollback();
}
