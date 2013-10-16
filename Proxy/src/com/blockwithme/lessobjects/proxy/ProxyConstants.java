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

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.util.Util;

/**
 * An Interface containing the Proxy constants used within this project.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ProxyConstants {

    /** The change adapter class name. */
    String CHANGE_ADAPTER_CLASS_NAME = Util
            .getClassName(ChangeListenerAdapter.class);

    /** The change listener class name. */
    String CHANGE_LISTENER_CLASS_NAME = Util
            .getClassName(ProxyValueChangeListener.class);

    /** The copy bean class name. */
    String COPY_BEAN_CLASS_NAME = Util.getClassName(CopyBean.class);

    /** The listener interface class name. */
    String LISTENER_INTERFACE_CLASS_NAME = Util
            .getClassName(ListenerSupport.class);

    /** The proxy class name. */
    String PROXY_CLASS_NAME = Util.getClassName(ProxyCursor.class);

    /** The change listener class name. */
    String TRANS_CHANGE_LISTENER_CLASS_NAME = Util
            .getClassName(ProxyTransactionListener.class);

}
