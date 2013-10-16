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
package com.blockwithme.lessobjects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * The listener interface for receiving transaction events.
 *
 * @author tarung.
 */
@ParametersAreNonnullByDefault
public interface TransactionListener {

    /**
     * This method is invoked before the commit/rollback is performed.
     *
     * @param theSource the source storage object
     * @param theActions the action set object, containing changes and action events.
     * @param isCommit true is passed if the transaction is being committed, false in case its being rolled back
     */
    void preCommit(ActionSet theActions, final Storage theSource,
            boolean isCommit);

    /**
     * This method is invoked after the commit/rollback is performed.
     *
     * @param theSource the source storage object
     * @param theActions that action set object, containing changes and action events.
     * @param isCommit true is passed if the transaction is being committed, false in case its being rolled back
     */
    void postCommit(ActionSet theActions, final Storage theSource,
            boolean isCommit);

}
