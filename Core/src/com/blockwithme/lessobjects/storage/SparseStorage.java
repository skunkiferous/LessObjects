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

/**
 * A Sparse is an automatically resizable storage that can grow or shrink based on the number
 * of elements stored. The storage grows when selectStructure method is invoked but space is not
 * available in the storage, and shrinks when clear method is invoked. Re-Sizing strategy for a sparse
 * storage can be defined by passing an instance of StorageSizingStrategy.
 *
 * @author tarung
 */

// (tarung) Could we merge this interface into the normal Storage interface ?
// We discussed this and decided that its not needed atm.
@ParametersAreNonnullByDefault
public interface SparseStorage extends Storage {

    /**
     * Checks if an element index is present in this Storage.
     *
     * @param theIndex the index
     * @return true, if present
     */
    boolean contains(final int theIndex);

    /**
     * The current size of this storage.
     *
     * @return the current size.
     */
    int currentSize();

}
