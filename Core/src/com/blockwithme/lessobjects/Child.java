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

import static com.blockwithme.lessobjects.util.StructConstants.ALPHA_NUMERIC_PATTERN;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct.StorageKey;
import com.blockwithme.lessobjects.beans.ChildProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base class of all Struct children.
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public abstract class Child implements Comparable<Child> {

    /** The size in bits. */
    private final int bits;

    /** The child is a field. */
    private final boolean isField;

    /** The index of the child inside parent the struct */
    private final int localIndex;

    /** The name of this child. */
    @Nonnull
    private final String name;

    /** The reference to the 'parent' of this child, null if this represents root
     * struct */
    @Nullable
    private final Struct parent;

    /** Fully qualified name of the child */
    private final String qualifiedName;

    /**
     * We have to keep this for sparse storage.
     * StorageKey is used by the read and write operations of Composite Storage
     * to resolve the Storage object for a Field
     */
    @Nullable
    private final StorageKey storageKey;

    /** Is it a global Struct */
    final boolean global;

    /** The optional flag */
    protected final boolean optional;

    /** The root. */
    @Nullable
    protected final Struct root;

    /** Constructor */
    protected Child(final ChildProperties theChildProperties,
            final boolean isChildAField) {

        checkNotNull(theChildProperties);

        isField = isChildAField;
        bits = theChildProperties.bits();
        name = checkNotNull(theChildProperties.name());
        checkArgument(name.matches(ALPHA_NUMERIC_PATTERN), "Invalid name :"
                + name
                + ", name can cotain only alphabets, digits, '_', '[' and ']' ");
        optional = theChildProperties.isOptional();
        qualifiedName = theChildProperties.qualifiedName();
        localIndex = theChildProperties.localIndex();
        parent = theChildProperties.parent();
        root = theChildProperties.root();
        storageKey = theChildProperties.storageKey();
        global = theChildProperties.global();
    }
    /** size in bits */
    public int bits() {
        return bits;
    }

    /** @return the index of the child inside the struct, returns -1 if the struct
     *         is not yet compiled */
    public int childIndex() {
        return localIndex();
    }

    /** Child properties.
     *
     * @return the child properties */
    public ChildProperties childProperties() {
        return new ChildProperties(this);
    }

    /** Compare to other Child based on size. Biggest comes *first* */
    @Override
    public final int compareTo(@Nullable final Child theOther) {
        if (theOther != null) {
            // TODO I thought we were meant to compare by *size*! Is the doc
            // wrong?
            return name().compareTo(theOther.name());
        }
        return 1;
    }

    /** Is it a global Struct */
    public boolean global() {
        return global;
    }

    /**
     * @return the isField
     */
    @JsonIgnore
    public boolean isField() {
        return isField;
    }

    /**
     * @return the optional
     */
    @JsonIgnore
    public boolean isOptional() {
        return optional;
    }

    /**
     * @return the localIndex
     */
    public int localIndex() {
        return localIndex;
    }

    /** Child name */
    @SuppressWarnings("null")
    public String name() {
        return name;
    }

    /** @return the parent */
    @Nullable
    public Struct parent() {
        return parent;
    }

    /**
     * @return the qualifiedName
     */
    @Nullable
    public String qualifiedName() {
        return qualifiedName;
    }

    /**
     * @return the root
     */
    @Nullable
    public Struct root() {
        return root;
    }

    /** @return the storageKey */
    @Nullable
    public StorageKey storageKey() {
        return storageKey;
    }

}
