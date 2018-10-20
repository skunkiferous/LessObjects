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
package com.blockwithme.lessobjects.proxy;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.ChangeInfo;

/**
 * The listener interface for receiving change events.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ProxyValueChangeListener {

    /**
     * This method is invoked when a value is changed.
     *
     * @param theChange the object that wraps change information.
     * @param theSource the source storage object
     * @param theResultEvents any additional objects to be added in the list of events, these
     *
     */
    void onChange(final ChangeInfo theChange, final ProxyCursor theSource,
            final List<Object> theResultEvents);

}
