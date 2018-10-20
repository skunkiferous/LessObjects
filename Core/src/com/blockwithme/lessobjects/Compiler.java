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
package com.blockwithme.lessobjects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.Storage;

/** To achieve the smallest memory footprint and/or the fastest access speed, we
 * use a compiler that converts a "naively" created structure into an optimized
 * structure. As this might require specific implementation of field objects,
 * additional data in those fields, and a specific implementation of the
 * Storage, we use the compiler as the facade over all that specific code.
 *
 * @author monster
 * */
@ParametersAreNonnullByDefault
public interface Compiler {
    /** Compiles a "naively" created structure to it's optimized form. */
    Struct compile(final Struct theStruct);

    /** Gets the compiler name. */
    String compilerName();

    /** Returns the field factory. It must be used while creating the initial
     * structure. */
    FieldFactory getFieldFactory();

    /**
     * Initializes and creates a new storage object for a compiled structure.
     * The storage is initialized with 'transactional' property enabled and the returned
     * instance will *not* be auto-resizable.
     *
     * @see {@link #initStorage(Struct, int, boolean, boolean)}
     *
     * @param theStruct the structure instance, the structure must have been
     *        compiled with this.compile().
     * @param theInitialCapacity the initial capacity of this storage.
     * @return the storage instance.
     */
    Storage initStorage(final Struct theStruct, final int theInitialCapacity);

    /**
     * Initializes and creates a new storage object for a compiled structure.
     * The storage is initialized with 'transactional' property enabled and the returned
     * instance will *not* be auto-resizable.
     *
     * @see {@link #initStorage(Struct, int, boolean, boolean)}
     *
     * @param theStruct the structure instance, the structure must have been
     *        compiled with this.compile().
     * @param theInitialCapacity the initial capacity of this storage.
     * @param theArity the arity or dimensionality in case multidimensional access is needed.
     * @return the storage instance.
     */
    Storage initStorage(final Struct theStruct, final int theInitialCapacity,
            Arity theArity);

    /**
     * Initializes and creates a new storage object for a compiled structure.
     *
     * @param theStruct the structure instance, the structure must have been
     *        compiled with this.compile().
     * @param theInitialCapacity the initial capacity of this storage.
     * @param theAutoResize true if the storage needs to grow itself automatically when the element position is 'selected'
     *        and shrink if needed when the 'clear' method is called, Growing and Shrinking are expensive operations
     *        hence this should be avoided when the Storage size is known to be fixed, or grow/shrink operations can be
     *        minimized by manually invoking {@link Storage#resizeStorage(int)}
     * @param theTransactionalFlag if the transactional flag is off the storage will *not* record any change history,
     *        roll-backs cannot be performed and change Field Change listeners will not be invoked.
     * @param theArity the arity or dimensionality in case multidimensional access is needed.
     * @return the storage instance.
     */
    Storage initStorage(final Struct theStruct, final int theInitialCapacity,
            Arity theArity, final boolean theAutoResize,
            final boolean theTransactionalFlag);

    /**
     * Initializes and creates a new storage object for a compiled structure.
     * The storage is initialized with 'transactional' property enabled.
     *
     * @see {@link #initStorage(Struct, int, boolean, boolean)}
     *
     * @param theStruct the structure instance, the structure must have been
     *        compiled with this.compile().
     * @param theInitialCapacity the initial capacity of this storage.
     * @param theAutoResize if the storage needs to grow itself automatically when the element position is 'selected'
     *        and shrink if needed when the 'clear' method is called, Growing and Shrinking are expensive operations
     *        hence this should be avoided when the Storage size is known to be fixed, or grow/shrink operations can be
     *        minimized by manually invoking {@link Storage#resizeStorage(int)}
     * @return the storage instance.
     */
    Storage initStorage(final Struct theStruct, final int theInitialCapacity,
            final boolean theAutoResize);

    /**
     * Initializes and creates a new storage object for a compiled structure.
     *
     * @param theStruct the structure instance, the structure must have been
     *        compiled with this.compile().
     * @param theInitialCapacity the initial capacity of this storage.
     * @param theAutoResize true if the storage needs to grow itself automatically when the element position is 'selected'
     *        and shrink if needed when the 'clear' method is called, Growing and Shrinking are expensive operations
     *        hence this should be avoided when the Storage size is known to be fixed, or grow/shrink operations can be
     *        minimized by manually invoking {@link Storage#resizeStorage(int)}
     * @param theTransactionalFlag if the transactional flag switched off the storage will *not* record any change history,
     *        roll-backs cannot be performed and change Field Change listeners will not be invoked.
     * @return the storage instance.
     */
    Storage initStorage(final Struct theStruct, final int theInitialCapacity,
            final boolean theAutoResize, final boolean theTransactionalFlag);

}
