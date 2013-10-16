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

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.TransactionListener;
import com.blockwithme.lessobjects.ValueChangeListener;
import com.blockwithme.lessobjects.beans.ChangeInfo;

/**
 * The Interface Event Listener Support. An instance of this interface is
 * associated with every storage instance.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ChangeListenerSupport {

    /** Adds the a listener to a particular field
     *
     * @param theField the field
     * @param theListener the listener */
    void addListener(final Field<?, ?> theField,
            final ValueChangeListener theListener);

    /** Adds a global listener which listens all the changes.
     *
     * @param theListener the listener */
    void addListener(final ValueChangeListener theListener);

    /**
     * Adds an event listener to be invoked after a transaction is committed.
     *
     * @param theListener the listener
     */
    void addPostCommitListener(final TransactionListener theListener);

    /**
     * Adds an event listener to be invoked just before any transaction is committed.
     *
     * @param theListener the listener
     */
    void addPreCommitListener(final TransactionListener theListener);

    /** Removes the listener, invokes equals method on the listener object passed
     * to compare with the existing listeners.
     *
     * @param theField the field associated with the listener.
     * @param theListener the listener to be removed.
     * @throws IllegalStateException if the listener was not found */
    void removeListener(final Field<?, ?> theField,
            final ValueChangeListener theListener);

    /** Removes a global listener, invokes equals method on the listener object
     * passed to compare with the existing listeners.
     *
     * @param theListener the listener
     * @throws IllegalStateException if the listener was not found */
    void removeListener(final ValueChangeListener theListener);

    /** Removes a post commit listener, invokes equals method on the listener object passed
     * to compare with the existing listeners.
     *
     * @param theListener the listener to be removed.
     * @throws IllegalStateException if the listener was not found */
    void removePostCommitListener(final TransactionListener theListener);

    /** Removes a pre commit listener, invokes equals method on the listener object passed
     * to compare with the existing listeners.
     *
     * @param theListener the listener to be removed.
     * @throws IllegalStateException if the listener was not found */
    void removePreCommitListener(final TransactionListener theListener);

    /** Fire value change.
     *
     * @param theChange the change */
    public void fireValueChange(final ChangeInfo theChange);

    /** Post commit method informs all listeners after a commit/rollback. */
    public void postCommit(final ActionSet theActions, final boolean isCommit);

    /** Pre commit method informs all listeners before a commit/rollback. */
    public void preCommit(final ActionSet theActions, final boolean isCommit);
}
