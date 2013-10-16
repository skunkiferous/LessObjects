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
package com.blockwithme.lessobjects.storage;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The Class SimpleSizingStrategy provides a basic StorageSizingStrategy implementation.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class SimpleSizingStrategy implements StorageSizingStrategy {

    /** Grow by GROW_RATIO times the number of allocated elements */
    private static final double GROW_RATIO = 1.5;

    /** Shrink to SHRINK_RATIO times the number of allocated elements */
    private static final double SHRINK_RATIO = 1.5;

    /** Shrink when the ratio of total size to allocated elements is more or
     * equal to this ratio */
    private static final int SHRINK_THRESHOLD_RATIO = 2;

    /** The Constant MIN_SIZE. */
    public static final int MIN_SIZE = 10;

    /** {@inheritDoc} */
    @Override
    public int grow(final int theCurrentCapacity, final int theMinIncrement) {
        int newSize = (int) (theCurrentCapacity * (GROW_RATIO - 1));
        if (theCurrentCapacity + newSize > max()) {
        	// Don't grow past maximum size
            newSize = max() - theCurrentCapacity;
        }
        return newSize;
    }

    /** {@inheritDoc} */
    @Override
    public int max() {
        return Integer.MAX_VALUE;
    }

    /** {@inheritDoc} */
    @Override
    public int min() {
        return MIN_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public int shrink(final int theFullCapacity, final int theAllocatedElements) {
        int reducedSize = 0;
        if (theFullCapacity >= theAllocatedElements * SHRINK_THRESHOLD_RATIO) {
            reducedSize = (int) (theAllocatedElements * (SHRINK_RATIO - 1));
            final int min = min();
            if (theFullCapacity - reducedSize < min) {
                reducedSize = theFullCapacity - min;
            }
        }
        return reducedSize;
    }

}
