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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Child;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.aligned64.Aligned64CompositeStorage;
import com.blockwithme.lessobjects.storage.aligned64.Aligned64SparseStorage;
import com.blockwithme.lessobjects.storage.aligned64.Aligned64Storage;
import com.blockwithme.lessobjects.util.FieldFactoryImpl;
import com.blockwithme.lessobjects.util.StructConstants;

/**
 * Aligned64 Compiler compiles the structure in order to be stored in Aligned64
 * Storage which uses an array of 'long' as an underlying storage. Each long in
 * the array is treated as a 64 bit bucket and space for each field in is allocated
 * based on First Fit Decreasing (FFD) strategy. This compiler will try to use
 * the least possible amount of space, while never positioning any field to
 * lie across a boundary.
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public class Aligned64Compiler extends CompilerBase {

    private static List<Struct> sortUnions(final AllFields theFields,
            final List<Child> theChildren,
            final Map<Struct, Struct> theCompiledMap) {
        // Sort unions by size
        final List<Struct> largeUnions = new ArrayList<>();

        for (final Struct u : theFields.unions) {
            if (theCompiledMap.get(u).bits() > StructConstants.LONG_BITS) {
                largeUnions.add(u);
            } else {
                theChildren.add(u);
            }
        }
        return largeUnions;
    }

    /** Constructor */
    public Aligned64Compiler() {
        super(new FieldFactoryImpl());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Struct compileInternaly(final Struct theStruct) {

        // We treat all unions, even the indirect unions, as atomic, like
        // fields. All other fields of all children are just absorbed.
        final AllFields allFields = new AllFields();
        getAllFields(theStruct, allFields);
        final List<Child> allChildren = new ArrayList<>();
        // We create a temporary compilation for every union, as if it was
        // "stand-alone". This helps us filling the gaps we leave for them
        // later.
        final Map<Struct, Struct> compiled = new IdentityHashMap<>();
        for (final Struct u : allFields.unions) {
            if (u != null) {
                compiled.put(u, compileInternaly(u));
            }
        }

        final List<Bucket> buckets = new ArrayList<>();
        final int bitsAligned64;

        if (theStruct.union()) {
            processChildren(theStruct, allChildren, compiled);
            // Biggest children first.
            Collections.sort(allChildren, new SizeComparator(compiled));

            int max = 0;
            for (final Child child : allChildren) {
                if (child != null) {
                    max = Math.max(max, sizeOf(child, compiled));
                }
            }
            // if union size is one bucket.
            if (max <= StructConstants.LONG_BITS) {
                final Bucket bucket = new Bucket();
                bucket.children.addAll(allChildren);
                buckets.add(bucket);
                bitsAligned64 = bucket.size = max;
            } else {
                int allocate = max;
                final Bucket bucket = new Bucket();
                buckets.add(bucket);
                bucket.children.addAll(allChildren);
                bucket.size = StructConstants.LONG_BITS;
                allocate -= StructConstants.LONG_BITS;
                // Add dummy buckets, to "reserve" the required space.
                while (allocate > StructConstants.LONG_BITS) {
                    final Bucket dummy = new Bucket();
                    dummy.size = StructConstants.LONG_BITS;
                    allocate -= StructConstants.LONG_BITS;
                    buckets.add(dummy);
                }
                final Bucket dummy = new Bucket();
                dummy.size = allocate;
                buckets.add(dummy);
                bitsAligned64 = (buckets.size() - 1)
                        * StructConstants.LONG_BITS
                        + buckets.get(buckets.size() - 1).size;
            }

        } else {

            allChildren.addAll(allFields.fields);
            final List<Struct> largeUnions = sortUnions(allFields, allChildren,
                    compiled);

            // Biggest children first.
            Collections.sort(allChildren, new SizeComparator(compiled));

            // Decreasing First-Fit heuristic for fully aligned fields.
            // Fast, but rarely optimal.
            for (final Struct u : largeUnions) {
                final Bucket bucket = new Bucket();
                buckets.add(bucket);
                // We use the compiled union, so that we know how many buckets
                // it needs.
                int allocate = compiled.get(u).bits();
                bucket.children.add(u);
                bucket.size = StructConstants.LONG_BITS;
                allocate -= StructConstants.LONG_BITS;
                // Add dummy buckets, to "reserve" the required space.
                while (allocate > StructConstants.LONG_BITS) {
                    final Bucket dummy = new Bucket();
                    dummy.size = StructConstants.LONG_BITS;
                    allocate -= StructConstants.LONG_BITS;
                    buckets.add(dummy);
                }
                final Bucket dummy = new Bucket();
                dummy.size = allocate;
                buckets.add(dummy);
            }
            for (final Child child : allChildren) {
                if (child != null) {
                    boolean added = false;
                    final int bits = sizeOf(child, compiled);
                    for (final Bucket bucket : buckets) {
                        if (bucket.addIfFits(child, bits)) {
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        final Bucket bucket = new Bucket();
                        buckets.add(bucket);
                        // Must fit in new bucket!
                        bucket.add(child, bits);
                    }
                }
            }
            // When aligned, every bucket takes 64 bits, so the size is the
            // number of buckets * 64
            bitsAligned64 = (buckets.size() - 1) * StructConstants.LONG_BITS
                    + buckets.get(buckets.size() - 1).size;
        }

        // At this point, we have found a near optimal location for all fields,
        // and unions, or at least as good as possible, without using a very
        // complicated heuristic.
        for (final Bucket bucket : buckets) {
            checkState(bucket.size <= StructConstants.LONG_BITS,
                    "Bucket size is " + bucket.size);
            bucket.padding = StructConstants.LONG_BITS - bucket.size;
        }

        final Map<Child, FieldData> fieldsData = processBuckets(theStruct,
                buckets, compiled, null);
        final Map<Struct, List<FieldData>> structToFieldsData = structToFieldsData(fieldsData);
        return oldStructToNew(theStruct, bitsAligned64, structToFieldsData);

    }

    /** {@inheritDoc} */
    @Override
    public String compilerName() {
        return "Aligned64Complier";
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

        checkNotNull(theStruct);
        checkCapacity(theInitialCapacity);
        if (theAutoResize) {
            return new Aligned64SparseStorage(theStruct, theInitialCapacity,
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

    /** {@inheritDoc} */
    @Override
    public Storage initStorage(final Struct theStruct,
            final int theInitialCapacity, @Nullable final Storage theParent,
            final boolean isTransactional, final Arity theArity) {

        checkNotNull(theStruct);
        checkCapacity(theInitialCapacity);
        Storage result;
        if (theStruct.allListChildren().length > 0
                || theStruct.allOptionalChildren().length > 0) {
            result = new Aligned64CompositeStorage(theStruct,
                    theInitialCapacity, theParent, isTransactional, theArity);
        } else {
            result = new Aligned64Storage(theStruct, theInitialCapacity,
                    theParent, isTransactional, theArity);
        }
        return result;
    }
}
