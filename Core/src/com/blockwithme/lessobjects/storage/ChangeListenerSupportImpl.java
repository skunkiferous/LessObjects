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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Array;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.TransactionListener;
import com.blockwithme.lessobjects.ValueChangeListener;
import com.blockwithme.lessobjects.beans.ChangeInfo;

/**
 * The Class Change Listener Support Implementation class
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChangeListenerSupportImpl implements ChangeListenerSupport {

    /** The Constant NO_LISTENER. */
    private static final ValueChangeListener[] NO_LISTENER = new ValueChangeListener[0];

    /** The Constant NO_LISTENER. */
    private static final TransactionListener[] NO_TRNS_LISTENER = new TransactionListener[0];

    /** The Base storage. */
    @Nonnull
    private final Storage baseStorage;

    /** The Results. */
    @Nullable
    private List<Object> eventList;

    /** The Global listeners */
    @Nonnull
    private ValueChangeListener[] globalListeners;

    /** The Listeners associated with fields */
    @Nonnull
    private final ValueChangeListener[][] listeners;

    /** The post commit Listeners. */
    @Nonnull
    private TransactionListener[] postCommitListeners;

    /** The pre commit Listeners. */
    @Nonnull
    private TransactionListener[] preCommitListeners;

    /** Add a new change listener to array of listeners also performs some validation.    */
    private static <T> T[] addArrayElement(final T theListener,
            @Nullable final T[] theArray, final String theListName) {
        T[] result;
        if (theArray == null || theArray.length == 0) {
            result = (T[]) Array.newInstance(theListener.getClass(), 1);
            result[0] = theListener;
            return result;
        }
        for (final T l : theArray) {
            if (l.equals(theListener)) {
                throw new IllegalArgumentException(
                        "The listener already exists in " + theListName);
            }
        }
        final int length = theArray.length;
        result = (T[]) Array.newInstance(theListener.getClass(), length + 1);
        System.arraycopy(theArray, 0, result, 0, length);
        result[length] = theListener;
        return result;
    }

    /** Removes ChangenListener from the array and returns the new array. */
    @SuppressWarnings("null")
    private static <T> T[] remove(final T theListener,
            @Nullable final T[] theArray, final boolean theSilentFlag) {
        checkState(theArray != null && theArray.length > 0,
                "Cannot remove listeners, no listeners were found.");
        final int length = theArray.length;
        int index = -1;
        int count = 0;
        for (final T ls : theArray) {
            if (ls.equals(theListener)) {
                index = count;
                break;
            }
            count++;
        }
        checkState(theSilentFlag || index != -1,
                "Cannot remove listeners, no listeners were found.");

        if (length == 1) {
            return (T[]) Array.newInstance(theListener.getClass(), 0);
        }
        final T[] newArray = (T[]) Array.newInstance(theListener.getClass(),
                length - 1);
        if (index > 0) {
            System.arraycopy(theArray, 0, newArray, 0, index);
        }
        if (index < length - 1) {
            System.arraycopy(theArray, index + 1, newArray, index, length
                    - index - 1);
        }
        return newArray;
    }

    /** Constructor. */
    public ChangeListenerSupportImpl(final Struct theStructure,
            final Storage theBaseStorage) {
        listeners = new ValueChangeListener[theStructure.fieldCount()][];
        baseStorage = theBaseStorage;
        for (int i = 0; i < listeners.length; i++) {
            listeners[i] = NO_LISTENER;
        }
        globalListeners = NO_LISTENER;
        preCommitListeners = NO_TRNS_LISTENER;
        postCommitListeners = NO_TRNS_LISTENER;
    }

    /** The baseStorage. */
    @SuppressWarnings("null")
    protected Storage baseStorage() {
        return baseStorage;
    }

    /** {@inheritDoc} */
    @Override
    public void addListener(final Field<?, ?> theField,
            final ValueChangeListener theListener) {
        checkNotNull(theField);
        checkNotNull(theListener);
        // baseStorage.checkAccess(theField);
        final int indx = theField.uniqueIndex();
        listeners[indx] = addArrayElement(theListener, listeners[indx],
                "Listeners Associated with " + theField.name());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void addListener(final ValueChangeListener theListener) {
        checkNotNull(theListener,
                "Null arguments passed to addListener method.");
        globalListeners = addArrayElement(theListener, globalListeners,
                "Global Listeners");
    }

    /** {@inheritDoc} */
    @Override
    public void addPostCommitListener(final TransactionListener theListener) {
        checkNotNull(theListener,
                "Null arguments passed to addPostCommitListener method.");
        postCommitListeners = addArrayElement(theListener, postCommitListeners,
                "Post Commit Listeners");
    }

    /** {@inheritDoc} */
    @Override
    public void addPreCommitListener(final TransactionListener theListener) {
        checkNotNull(theListener,
                "Null arguments passed to addPreCommitListener method.");
        preCommitListeners = addArrayElement(theListener, preCommitListeners,
                "Pre Commit Listeners");
    }

    /**
     * @return the eventList
     */
    @SuppressWarnings("null")
    public List<Object> eventList() {
        return eventList;
    }

    /**
     * @param theEventList the eventList to set.
     */
    public void eventList(@Nullable final List<Object> theEventList) {
        eventList = theEventList;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void fireValueChange(final ChangeInfo theChange) {
        for (final ValueChangeListener listener : listeners[theChange.field()
                .uniqueIndex()]) {
            listener.onChange(theChange, baseStorage, eventList);
        }
        if (globalListeners.length > 0) {
            for (final ValueChangeListener listener : globalListeners) {
                listener.onChange(theChange, baseStorage(), eventList());
            }
        }
    }

    @SuppressWarnings("null")
    @Override
    public void postCommit(final ActionSet theActions, final boolean isCommit) {
        for (final TransactionListener listener : postCommitListeners) {
            listener.postCommit(theActions, baseStorage, isCommit);
        }
    }

    @SuppressWarnings("null")
    @Override
    public void preCommit(final ActionSet theActions, final boolean isCommit) {
        for (final TransactionListener listener : preCommitListeners) {
            listener.preCommit(theActions, baseStorage, isCommit);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeListener(final Field<?, ?> theField,
            final ValueChangeListener theListener) {
        checkNotNull(theField);
        checkNotNull(theListener);
        final int indx = theField.uniqueIndex();
        listeners[indx] = remove(theListener, listeners[indx], false);
    }

    /** {@inheritDoc} */
    @Override
    public void removeListener(final ValueChangeListener theListener) {
        checkNotNull(theListener,
                "Null arguments passed to removeListener method.");
        globalListeners = remove(theListener, globalListeners, false);
    }

    /** {@inheritDoc} */
    @Override
    public void removePostCommitListener(final TransactionListener theListener) {
        checkNotNull(theListener,
                "Null arguments passed to removePostCommitListener method.");
        postCommitListeners = remove(theListener, postCommitListeners, false);
    }

    /** {@inheritDoc} */
    @Override
    public void removePreCommitListener(final TransactionListener theListener) {
        checkNotNull(theListener,
                "Null arguments passed to removePreCommitListener method.");
        preCommitListeners = remove(theListener, preCommitListeners, false);
    }
}
