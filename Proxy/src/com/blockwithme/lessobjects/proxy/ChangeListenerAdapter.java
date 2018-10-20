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

package com.blockwithme.lessobjects.proxy;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.TransactionListener;
import com.blockwithme.lessobjects.ValueChangeListener;
import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * The Class ChangeListnerAdapter wraps a TransactionListener, while offering
 * a ValueChangeListener interface.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChangeListenerAdapter implements ValueChangeListener,
        TransactionListener {

    /** The listener. */
    @Nullable
    private final com.blockwithme.lessobjects.proxy.ProxyValueChangeListener listener;

    /** The proxy. */
    @Nonnull
    private final ProxyCursor proxy;

    /** The transaction listener. */
    @Nullable
    private final com.blockwithme.lessobjects.proxy.ProxyTransactionListener transListener;

    /**
     * Instantiates a new change listener adapter.
     *
     * @param theTransListener the transactional listener
     * @param theProxyCursor the proxy cursor
     */
    public ChangeListenerAdapter(
            final com.blockwithme.lessobjects.proxy.ProxyTransactionListener theTransListener,
            final ProxyCursor theProxyCursor) {

        listener = null;
        proxy = theProxyCursor;
        transListener = theTransListener;
    }

    /** Instantiates a new change listener adapter.
     *
     * @param theListener the listener
     * @param theProxyCursor the proxy cursor */
    public ChangeListenerAdapter(
            final com.blockwithme.lessobjects.proxy.ProxyValueChangeListener theListener,
            final com.blockwithme.lessobjects.proxy.ProxyTransactionListener theTransListener,
            final ProxyCursor theProxyCursor) {

        listener = theListener;
        proxy = theProxyCursor;
        transListener = theTransListener;
    }

    /**
     * Instantiates a new change listener adapter.
     *
     * @param theListener the listener
     * @param theProxyCursor the proxy cursor
     */
    public ChangeListenerAdapter(
            final com.blockwithme.lessobjects.proxy.ProxyValueChangeListener theListener,
            final ProxyCursor theProxyCursor) {

        listener = theListener;
        proxy = theProxyCursor;
        transListener = null;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unused", "null" })
    @Override
    public boolean equals(final Object theObj) {
        if (this == theObj) {
            return true;
        }
        if (theObj == null) {
            return false;
        }
        if (getClass() != theObj.getClass()) {
            return false;
        }
        final ChangeListenerAdapter other = (ChangeListenerAdapter) theObj;
        if (listener == null) {
            if (other.listener != null) {
                return false;
            }
        } else if (!listener.equals(other.listener)) {
            return false;
        }
        if (transListener == null) {
            if (other.transListener != null) {
                return false;
            }
        } else if (!transListener.equals(other.transListener)) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (listener == null ? 0 : listener.hashCode());
        result = prime * result
                + (transListener == null ? 0 : transListener.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void onChange(final ChangeInfo theChange, final Storage theSource,
            final List<Object> theResultEvents) {
        checkNotNull(listener, "Value change listener was not set.");
        listener.onChange(theChange, proxy, theResultEvents);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void postCommit(final ActionSet theActions, final Storage theSource,
            final boolean isCommit) {
        checkNotNull(transListener, "Transaction listener was not set.");
        transListener.postCommit(theActions, proxy, isCommit);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void preCommit(final ActionSet theActions, final Storage theSource,
            final boolean isCommit) {
        checkNotNull(transListener, "Transaction listener was not set.");
        transListener.preCommit(theActions, proxy, isCommit);
    }
}
