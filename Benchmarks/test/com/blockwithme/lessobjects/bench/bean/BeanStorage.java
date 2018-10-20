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
package com.blockwithme.lessobjects.bench.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.ChangeRecords;
import com.blockwithme.lessobjects.storage.ChangeStorage;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class BeanStorage<T extends BeanStorageEntity> {

    private static final int COMMIT_LOG_INIT_CAPACITY = 512;

    private IntObjectMap<T> commitLog;

    private final T[] store;

    private final boolean transactional;

    public BeanStorage(final Class<T> clazz, final int storageSize,
            final Factory<T> factory, final boolean isTransactional) {
        store = (T[]) Array.newInstance(clazz, storageSize);
        for (int i = 0; i < storageSize; i++) {
            store[i] = factory.createNewInstance(i);
        }
        commitLog = new IntObjectOpenHashMap<>(COMMIT_LOG_INIT_CAPACITY);
        transactional = isTransactional;
    }

    public ActionSet commit() {

        if (!transactional) {
            throw new IllegalStateException("Storage not transactional.");
        }

        final List<ValueChange<?>> changeList = new ArrayList<>();
        for (final IntCursor i : commitLog.keys()) {
            final T initial = commitLog.get(i.value);
            final T current = store[i.value];
            current.processChanges(i.index, initial, changeList);
        }

        final ActionSet actions = new ActionSet() {
            ChangeRecords changes;
            // constructor
            {
                changes = new ChangeRecords() {
                    List<ValueChange<?>> changes;
                    // constructor
                    {
                        changes = changeList;
                    }

                    void addChange(final ValueChange<?> change) {
                        changes.add(change);
                    }

                    /** {@inheritDoc} */
                    @Override
                    public Iterator<ValueChange<?>> changes(
                            final Struct theStruct) {
                        return changes.iterator();
                    }

                    @Override
                    public Set<? extends ChangeRecords> children() {
                        return null;
                    }

                    @Override
                    public boolean isEmpty() {
                        return changes.isEmpty();
                    }

                };
            }

            public ChangeRecords changeRecords() {
                return changes;
            }

            @Override
            @Nullable
            public ChangeStorage currentChanges() {
                throw new UnsupportedOperationException(
                        "This currentChanges is not supported for impl class :"
                                + this.getClass());
            }

            /** {@inheritDoc} */
            @Override
            public List<Object> events() {
                // NOP
                return null;
            }
        };

        commitLog = new IntObjectOpenHashMap<>(COMMIT_LOG_INIT_CAPACITY);
        return actions;
    }

    public T instanceAt(final int elementIndex) {
        try {
            // add initial state to the commit log.
            if (transactional && !commitLog.containsKey(elementIndex)) {
                commitLog.put(elementIndex, (T) store[elementIndex].clone());
            }
            return store[elementIndex];
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean transactionsEnabled() {
        return this.transactional;
    }
}
