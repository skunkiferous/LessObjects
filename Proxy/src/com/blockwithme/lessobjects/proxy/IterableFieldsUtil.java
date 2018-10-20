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

import java.util.Collections;
import java.util.Iterator;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * The Util class used by the *generated* code to delegate control
 * and create an appropriate iterator for Iterable Fields
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class IterableFieldsUtil {

    /** The Constant NULL_ITERATOR. */
    @SuppressWarnings("rawtypes")
    private static final Iterator EMPTY_ITERATOR = Collections.EMPTY_LIST
            .iterator();

    /** Creates iterator for the list field. This method is invoked from the
     * generated proxy code. If the size of iterator is negative this method
     * just clears pre-existing data if any, zero indicates that the user wants
     * the list with current size or null-iterator if not initialized
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public static Iterator createIterator(final Storage theStorage,
            final Object theBean, final String theListName, final int theSize) {

        final Struct listChild = theStorage.rootStruct().structChildren()[0]
                .child(theListName);
        Storage tempStorage = null;

        if (theSize < 0) {
            theStorage.clearChild(listChild);
        } else {
            tempStorage = theStorage.list(listChild);
            // zero indicates that the user wants the list with
            // current size or null-iterator if not initialized
            if (theSize != 0) {
                if (tempStorage == null) {
                    tempStorage = theStorage.createOrClearList(listChild,
                            theSize);
                }
                if (tempStorage.getCapacity() != theSize) {
                    tempStorage.resizeStorage(theSize);
                }
            }
        }
        final Storage listStorage = tempStorage;
        final Object copy = ((CopyBean) theBean).copy(tempStorage);

        if (listStorage == null) {
            return EMPTY_ITERATOR;
        }
        return new Iterator() {
            int count = -1;

            @Override
            public boolean hasNext() {
                return count + 1 < listStorage.getCapacity();
            }

            @Override
            public Object next() {
                if (!hasNext()) {
                    throw new ArrayIndexOutOfBoundsException(
                            "There are no more elements in "
                                    + copy.getClass().getName() + " : " + count);
                }
                listStorage.selectStructure(++count);
                return copy;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "Remove operation not supported for this iterator");

            }
        };
    }
}
