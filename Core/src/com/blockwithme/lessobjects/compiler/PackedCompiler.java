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
package com.blockwithme.lessobjects.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Child;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.packed.PackedCompositeStorage;
import com.blockwithme.lessobjects.storage.packed.PackedSparseStorage;
import com.blockwithme.lessobjects.storage.packed.PackedStorage;
import com.blockwithme.lessobjects.util.FieldFactoryImpl;
import com.blockwithme.lessobjects.util.StructConstants;

/**
 * Packed Compiler compiles the structure in order to be stored in Packed
 * Storage which uses an array of 'long' as an underlying storage. Each long in
 * the array is treated as a 64 bit bucket and space for each field in is allocated
 * based on First Fit Decreasing (FFD) strategy. This compilers tries not to put
 * fields across boundaries, but will do so, if required to prevent gaps.
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class PackedCompiler extends CompilerBase {

    /** Constructor */
    public PackedCompiler() {
        super(new FieldFactoryImpl());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Struct compileInternaly(final Struct theStruct) {

        // We treat all unions, even the indirect unions, as atomic, like
        // fields.
        // All other fields of all children are just absorbed.
        final AllFields allFields = new AllFields();
        getAllFields(theStruct, allFields);
        final List<Child> allChildren = new ArrayList<>();
        // We create a temporary compilation for every union, as if it was
        // "stand-alone"
        // This helps us filling the gaps we leave for them later.
        final Map<Struct, Struct> compiled = new HashMap<>();
        for (final Struct u : allFields.unions) {
            if (u != null) {
                compiled.put(u, compileInternaly(u));
            }
        }

        final List<Bucket> buckets = new ArrayList<>();

        int unionSize = -1;
        if (theStruct.union()) {
            processChildren(theStruct, allChildren, compiled);

            final Bucket bucket = new Bucket();
            bucket.children.addAll(allChildren);
            buckets.add(bucket);

            int max = 0;
            for (final Child child : allChildren) {
                if (child != null) {
                    max = Math.max(max, sizeOf(child, compiled));
                }
            }
            unionSize = max;
        } else {
            allChildren.addAll(allFields.fields);
            allChildren.addAll(allFields.unions);

            // Biggest children first.
            Collections.sort(allChildren, new SizeComparator(compiled));

            // Find the large unions
            final List<Struct> largeUnions = new ArrayList<>();
            while (!allChildren.isEmpty()
                    && allChildren.get(0).bits() > StructConstants.LONG_BITS) {
                largeUnions.add((Struct) allChildren.remove(0));
            }

            // The next heuristic is my (monster) own. It's a bit similar to
            // First-Fit, but without gaps.
            final List<Child> todo = new ArrayList<>(allChildren);
            // We remove the biggest first, so best put them at the end ...
            Collections.reverse(todo);
            buckets.add(new Bucket());
            int alignment = 0;
            while (!todo.isEmpty()) {
                Bucket bucket = buckets.get(buckets.size() - 1);
                if (bucket.size >= StructConstants.LONG_BITS) {
                    final int overflow = bucket.size
                            - StructConstants.LONG_BITS;
                    bucket = new Bucket();
                    bucket.size += overflow;
                    buckets.add(bucket);
                }
                if (alignment % StructConstants.LONG_BITS == 0
                        && !largeUnions.isEmpty()) {
                    // We try to put the large unions at alignment boundaries.
                    final Child c = largeUnions.remove(0);
                    alignment += c.bits();
                    bucket.add(c, c.bits());
                } else {
                    final int available = StructConstants.LONG_BITS
                            - bucket.size;
                    boolean found = false;
                    for (int i = todo.size() - 1; i >= 0; i--) {
                        final Child c = todo.get(i);
                        if (c.bits() <= available) {
                            bucket.add(c, c.bits());
                            todo.remove(i);
                            alignment += c.bits();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        // Could not find any perfect fit, so just take last one
                        // (biggest)
                        final Child c = todo.remove(todo.size() - 1);
                        alignment += c.bits();
                        bucket.add(c, c.bits());
                    }
                }
            }
            if (!largeUnions.isEmpty()) {
                // Once we are here, we don't care about alignment, bucket size,
                // ... anymore
                final Bucket bucket = buckets.get(buckets.size() - 1);
                bucket.children.addAll(largeUnions);
            }
        }

        // At this point, we have found a near optimal location for all fields,
        // and unions,
        // or at least as good as possible, without getting very complicated.
        final int[] total = new int[1];
        final Map<Child, FieldData> fieldsData = processBuckets(theStruct,
                buckets, compiled, total);
        final Map<Struct, List<FieldData>> structToFieldsData = structToFieldsData(fieldsData);
        return oldStructToNew(theStruct, theStruct.union() ? unionSize
                : total[0], structToFieldsData);
    }

    /** {@inheritDoc} */
    @Override
    public String compilerName() {
        return "PackedCompiler";
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theInitialCapacity) {
        checkCapacity(theInitialCapacity);
        return initStorage(theStruct, theInitialCapacity, null, true,
                Arity.ONE_D);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theInitialCapacity, final Arity theArity) {
        checkCapacity(theInitialCapacity);
        return initStorage(theStruct, theInitialCapacity, null, true, theArity);
    }

    /** {@inheritDoc} */
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theInitialCapacity, final Arity theArity,
            final boolean theAutoResize, final boolean isTransactional) {
        checkCapacity(theInitialCapacity);
        if (theAutoResize) {
            return new PackedSparseStorage(theStruct, theInitialCapacity,
                    isTransactional, theArity);
        }
        return initStorage(theStruct, theInitialCapacity, null,
                isTransactional, theArity);
    }

    /** {@inheritDoc} */
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theInitialCapacity, final boolean theAutoResize) {
        checkCapacity(theInitialCapacity);
        return initStorage(theStruct, theInitialCapacity, theAutoResize, true);
    }
    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theInitialCapacity, final boolean theAutoResize,
            final boolean isTransactional) {
        checkCapacity(theInitialCapacity);
        return initStorage(theStruct, theInitialCapacity, Arity.ONE_D,
                theAutoResize, isTransactional);
    }
    /** Actually initializes the Storage */
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theStorageSize, @Nullable final Storage theBaseStorage,
            final boolean isTransactional, final Arity theArity) {

        Storage result;
        checkCapacity(theStorageSize);
        if (theStruct.allOptionalChildren().length > 0
                || theStruct.allListChildren().length > 0) {
            result = new PackedCompositeStorage(theStruct, theStorageSize,
                    theBaseStorage, isTransactional, theArity);

        } else {
            result = new PackedStorage(theStruct, theStorageSize,
                    theBaseStorage, isTransactional, theArity);
        }
        return result;
    }
}
