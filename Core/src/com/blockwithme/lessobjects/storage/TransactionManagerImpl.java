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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.ValueChange;

//CHECKSTYLE.OFF: IllegalType
/**
 * The TransactionManager Implementation class.
 *
 * @author tarung
 */
// TODO Validate that this code is actually transactional
public class TransactionManagerImpl implements TransactionManager {

    /** The empty change records. */
    @SuppressWarnings("all")
    private static ChangeRecords EMPTY_CHANGE_RECORDS = new ChangeRecords() {

        @Override
        public Iterator<ValueChange<?>> changes(final Struct theStruct) {
            return Collections.EMPTY_LIST.iterator();
        }

        @Override
        public Set<? extends ChangeRecords> children() {
            return Collections.EMPTY_SET;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    /** The empty ActionSet. */
    @SuppressWarnings("all")
    private static ActionSet EMPTY_SET = new ActionSet() {

        @Override
        public ChangeRecords changeRecords() {
            return EMPTY_CHANGE_RECORDS;
        }

        @Override
        @Nullable
        public ChangeStorage currentChanges() {
            return null;
        }

        @Override
        public List<Object> events() {
            return Collections.EMPTY_LIST;
        }

    };

    /** The action set. */
    @Nullable
    private ActionSetImpl actionSet;

    /** The children. */
    @Nonnull
    private final Set<TransactionManagerImpl> children;

    /** The parent TransactionManagerImpl. */
    @Nullable
    private final TransactionManagerImpl parent;

    /** The storage. */
    @Nonnull
    private final AbstractStorage storage;

    /** Instantiates a new transaction manager impl.
     * @param theStorage the storage instance */
    @SuppressWarnings("null")
    TransactionManagerImpl(final AbstractStorage theStorage,
            @Nullable final TransactionManagerImpl theParent) {
        storage = theStorage;
        children = new HashSet<>();
        parent = theParent;
        if (theParent != null) {
            theParent.children.add(this);
        }
    }

    /**
     * Post commit processing
     */
    @SuppressWarnings("null")
    private void postCommit(final ActionSetImpl theActionSet) {
        // Calling post-commit listeners.
        storage.changeListenerSupport().postCommit(theActionSet, true);
    }

    /**
     * Post rollback processing
     */
    @SuppressWarnings("null")
    private void postRollback() {
        // Calling pre-commit listeners.
        storage.changeListenerSupport().postCommit(actionSet, false);
    }

    /**
     * Pre commit processing
     */
    @SuppressWarnings("null")
    private void preCommit() {

        // if this is a child transaction manager we don't want to invoke
        // global listeners from here because the listeners will get invoked
        // multiple times.

        // Calling pre-commit listeners.
        final boolean oldReadOnly = storage.transactionsEnabled();
        storage.enableTransactionsInternal(false);
        try {
            storage.changeListenerSupport().preCommit(actionSet, true);
        } catch (final Exception exp) {
            // to rollback the transaction the storage should be writable.
            storage.enableTransactionsInternal(true);
            rollback();
            throw new IllegalStateException(
                    "Error occurred while invoking change listeners, the TRANSACTION WAS ROLLED BACK!",
                    exp);
        } finally {
            storage.enableTransactionsInternal(oldReadOnly);
        }
    }

    /**
     * Pre rollback processing
     */
    @SuppressWarnings("null")
    private void preRollback() {
        // Calling pre-commit listeners.
        storage.changeListenerSupport().preCommit(actionSet, false);
    }

    protected ActionSet commitImpl(final boolean theInvokeListenersFlag) {
        if (actionSet != null) {
            // we don't want pre commit and post commit listeners to be invoked
            // multiple times.
            if (theInvokeListenersFlag) {
                preCommit();
            }
            final ActionSetImpl oldActionSet = actionSet;
            actionSet = null;
            storage.listenerSupport.eventList(null);
            if (!children.isEmpty()) {
                for (final TransactionManagerImpl childTm : children) {
                    childTm.commitImpl(false);
                }
            }
            // we don't want pre commit and post commit listeners to be invoked
            // multiple times.
            if (theInvokeListenersFlag) {
                postCommit(oldActionSet);
            }
            return oldActionSet;
        }
        return EMPTY_SET;
    }

    /** The current Data set for un-committed transactions.*/
    @SuppressWarnings("null")
    public ActionSet actionSet() {

        if (actionSet == null) {

            actionSet = new ActionSetImpl();
            storage.listenerSupport.eventList(actionSet.eventList);

            if (parent != null) {
                ((ActionSetImpl) parent.actionSet()).addChild(actionSet);
            }
        }
        return actionSet;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public ActionSet commit() {
        return commitImpl(true);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isUncommitted() {
        if (actionSet == null) {
            return false;
        }
        return !actionSet.changeRecords.isEmpty();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public ActionSet rollback() {
        preRollback();

        final ActionSet oldActionSet;

        if (!children.isEmpty()) {
            for (final TransactionManagerImpl childTM : children) {
                childTM.rollback();
            }
        }

        if (actionSet != null) {
            oldActionSet = actionSet;
            final Iterator<ValueChange<?>> itr = oldActionSet.changeRecords()
                    .changes(storage.rootStruct());
            final boolean oldFlagVlaue = storage.transactionsDisabled;
            storage.transactionsDisabled = true;
            while (itr.hasNext()) {
                final ValueChange<?> vc = itr.next();
                vc.reverseChange(storage());
            }
            storage.transactionsDisabled = oldFlagVlaue;
            actionSet = null;
            storage.listenerSupport.eventList(null);
        } else {
            oldActionSet = EMPTY_SET;
        }
        postRollback();
        return oldActionSet;
    }

    /**
     * @return the storage
     */
    @SuppressWarnings("null")
    public Storage storage() {
        return storage;
    }
}
