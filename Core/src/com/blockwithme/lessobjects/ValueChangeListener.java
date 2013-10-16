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

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * The listener interface for receiving change events.
 *
 * @author tarung
 *
 * */
@ParametersAreNonnullByDefault
public interface ValueChangeListener {

    /**
     * This method is invoked when a storage value is changed.
     *
     * @param theChange the object that wraps change information.
     * @param theSource Storage instance in read-only mode; any changes performed on this instance will result into IllegalStateException.
     * @param theResultEvents list can be used to store and pass around objects between the listeners and these values will be available at the time when commit or roll back is invoked.
     * A new theResultEvents list is created every time a commit or rollback is performed.
     */
    void onChange(final ChangeInfo theChange, final Storage theSource,
            final List<Object> theResultEvents);

}
