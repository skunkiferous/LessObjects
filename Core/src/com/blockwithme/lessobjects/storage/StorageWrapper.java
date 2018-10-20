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
 * Storage Wrapper wraps a Storage Object and provides an implementation of Storage interface.
 * The Storage wrapper stores all changes done in a buffer space instead of actually writing
 * to the storage. When reading  from it, it first check it's own data, and if the
 * current field of the current element wasn't changed, it passes the read call to the base storage.
 * Multiple layers of buffer can be added to an existing StorageWrapper, each layer is identified using
 * a long identifier called 'cycle'.  At every cycle, the current storage is wrapped with one more layer.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface StorageWrapper extends Storage {

    /** Adds a new layer to this Storage Wrapper. the cycle can be any long value except zero. */
    public void addLayer(long theCycle);

    /** Returns current cycle, returns zero if all existing layers have been merged/written to the actual storage. */
    public long currentCycle();

    /** Writes buffered data for a particular cycle to the previous cycle.
     * if the previous cycle is the actual storage then it writes to the actual storage.
     * Buffered data for this cycle is removed after this operation. Throws exception
     * if no layers were found corresponding to the the cycle number.*/
    @SuppressWarnings("null")
    public Storage mergeLayer(final long theCycle);

    /**
     * Writes values in the actual storage for a particular cycle and all the layers
     * previous to it starting from oldest layer. Throws exception
     * if no layers were found corresponding to the the cycle number.
     *
     * @param theCycle the the cycle
     */
    public Storage writeLayer(final long theCycle);

    /**
     * Apply/merge the buffered data for all the layers starting from oldest layer.
     * After this operation, all buffered data has been merged into the base storage.
     */
    public Storage writeAllLayers();

    /**
     * Replaces the actual buffer for a particular cycle and base storage.
     * The storage parameter is required, as a storage might itself have children storages.
     * Throws exception if no layers were found corresponding to the the cycle number.
     */
    public void replaceStorage(final long theCycle, final Storage theStorage);
}
