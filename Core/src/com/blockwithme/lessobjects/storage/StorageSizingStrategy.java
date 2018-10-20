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
 * Re-Sizing strategy for a sparse storage can be defined by passing
 * an instance of StorageSizingStrategy.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface StorageSizingStrategy {

    /** Elements to be added to grow the storage automatically. */
    int grow(final int theCurrentCapacity, final int theMinIncrement);

    /** Max storage size beyond which the storage should not grow any further */
    int max();

    /** Minimum storage size under which the storage should not shrink any further */
    int min();

    /** Number Elements to be removed to shrink the storage automatically. */
    int shrink(final int theFullCapacity, final int theAllocatedElements);

}
