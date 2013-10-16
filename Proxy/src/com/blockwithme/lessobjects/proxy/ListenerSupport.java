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

/**
 * The Interface ListenerSupport is extended by generated proxy
 * to provide an interface to add listeners.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ListenerSupport {

    /**
     * Adds the listener.
     *
     * @param theListener the listener
     */
    void addListener(final ProxyValueChangeListener theListener);

    /**
     * Adds the listener only to the field(s) specified by theFieldName,
     * field name search is performed on the proxy, if exact match for the
     * field name is found that is used for attaching the listener, otherwise if a
     * child is found with the name passed all the fields of that child are
     * used for attaching the listener.
     *
     *  @throws IllegalStateException if no fields are found with the name specified.
     *  */
    void addListener(final String theFieldName,
            final ProxyValueChangeListener theListener);

    /**
     * Adds a post-commit listener.
     *
     * @param theListener the listener
     */
    void addPostCommitListener(final ProxyTransactionListener theListener);

    /**
     * Adds a pre-commit listener.
     *
     * @param theListener the listener
     */
    void addPreCommitListener(final ProxyTransactionListener theListener);

    /**
     * Removes a listener, if the listener is associated with multiple fields
     * all of them are removed. Listener's equals method is used to check
     * the equality
     *
     * @param theListener the Listener
     */
    public void removeListener(ProxyValueChangeListener theListener);

    /** Removes a listener only from the field(s) specified by theFieldName,
     * field name search is performed on the proxy, if exact match for the
     * field name is found that is used for detaching the listener, else if a
     * child is found with the name passed all the fields of that child are
     * used for detaching the listener.
     *
     *  @throws IllegalStateException if no fields are found with the name specified.
     * */
    public void removeListener(String theFieldName,
            ProxyValueChangeListener theListener);

    /**
     * Removes the post commit listener.
     *
     * @param theListener the theListener
     */
    public void removePostCommitListener(ProxyTransactionListener theListener);

    /**
     * Removes the pre commit listener.
     *
     * @param theListener the the listener
     */
    public void removePreCommitListener(ProxyTransactionListener theListener);

}
