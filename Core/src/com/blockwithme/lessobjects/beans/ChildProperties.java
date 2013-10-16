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

package com.blockwithme.lessobjects.beans;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Child;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.StorageKey;

/**
 * The ChildProperties class wraps all the properties of a particular Child,
 * i.e. generic properties of a Field or a Struct-child)
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChildProperties {

    /** The size in bits. */
    private final int bits;

    /** The index of the child inside parent the struct */
    private final int localIndex;

    /** The struct name. */
    @Nonnull
    private final String name;

    /** The optional flag */
    private final boolean optional;

    /** The reference to the 'parent' of this child, null if this represents root
     * struct */
    private final Struct parent;

    /** The struct fully qualified name. */
    private final String qualifiedName;

    /** The reference to the super most 'parent' of this child, null if this
     * represents root struct */
    private final Struct root;

    /** The storage key property, used to indicate that a particular field/child
     * points to a secondary storage. */
    @Nullable
    private final StorageKey storageKey;

    /** Is it a global Struct */
    private final boolean global;

    @SuppressWarnings("null")
    protected static boolean objectEquals(final Object theO1, final Object theO2) {
        if (theO1 == theO2) {
            return true;
        }
        if (theO1 != null && theO2 != null && theO1.equals(theO2)) {
            return true;
        }
        return false;
    }

    /**
     * Internal constructor Instantiates a new child properties.
     *
     * @param theName the name
     * @param theQName the qualified name
     * @param theBits the size in bits
     * @param isOptional is optional child ?
     * @param theLocalIndex index of this child within a Struct, this value is
     *        used by Union field index.
     * @param theParent the parent struct, should be null if this is a root
     *        struct
     * @param theRoot the super most parent, should be null if this is a root
     *        struct
     * @param theStorageKey the storage key of optional parent or list parent,
     *        used to indicate that a particular field/child belongs to an optional
     *        parent.
     */
    ChildProperties(final String theName, @Nullable final String theQName,
            final int theBits, final boolean isOptional,
            final int theLocalIndex, @Nullable final Struct theParent,
            @Nullable final Struct theRoot,
            @Nullable final StorageKey theStorageKey, final boolean isGlobal) {

        name = theName;
        qualifiedName = theQName;
        bits = theBits;
        optional = isOptional;
        localIndex = theLocalIndex;
        parent = theParent;
        root = theRoot;
        storageKey = theStorageKey;
        global = isGlobal;
    }

    /**
     * Instantiates a new child properties by copying all
     * the properties from an existing child. *
     *
     * @param theChild source child object where the values are copied from */
    public ChildProperties(final Child theChild) {

        name = theChild.name();
        qualifiedName = theChild.qualifiedName();
        bits = theChild.bits();
        optional = theChild.isOptional();
        localIndex = theChild.localIndex();
        parent = theChild.parent();
        root = theChild.root();
        storageKey = theChild.storageKey();
        global = theChild.global();

    }

    /**
     * Instantiates a new child properties with name and
     * size, and default values for rest of the fields
     *
     * @param theName the name property
     * @param theBits the size property in bits
     * */
    public ChildProperties(final String theName, final int theBits) {

        name = theName;
        qualifiedName = theName;
        bits = theBits;
        optional = false;
        localIndex = -1;
        parent = null;
        root = null;
        storageKey = null;
        global = false;
    }

    @SuppressWarnings("static-method")
    protected ChildProperties setChildFields(final String theName,
            @Nullable final String theQName, final int theBits,
            final boolean isOptional, final int theLocalIndex,
            @Nullable final Struct theParent, @Nullable final Struct theRoot,
            @Nullable final StorageKey theStorageKey, final boolean isGlobal) {

        return new ChildProperties(theName, theQName, theBits, isOptional,
                theLocalIndex, theParent, theRoot, theStorageKey, isGlobal);
    }

    /**
     * @return the bits
     */
    public int bits() {
        return bits;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public boolean equals(@Nullable final Object theObj) {
        if (this == theObj) {
            return true;
        }
        if (theObj == null) {
            return false;
        }
        if (getClass() != theObj.getClass()) {
            return false;
        }
        final ChildProperties other = (ChildProperties) theObj;
        if (bits != other.bits) {
            return false;
        }
        if (localIndex != other.localIndex) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (optional != other.optional) {
            return false;
        }
        if (storageKey != other.storageKey) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        if (qualifiedName == null) {
            if (other.qualifiedName != null) {
                return false;
            }
        } else if (!qualifiedName.equals(other.qualifiedName)) {
            return false;
        }
        if (root == null) {
            if (other.root != null) {
                return false;
            }
        } else if (!root.equals(other.root)) {
            return false;
        }
        return true;
    }

    /** @return the size bits */
    public int getBits() {
        return bits;
    }

    /** @return the name of this child */
    @SuppressWarnings("null")
    public String getName() {
        return name;
    }

    /** Is it a global Struct */
    public boolean global() {
        return global;
    }

    /** Is it an optional field/child ? */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Index of child within same parent, the value is used as
     * union-discriminator.
     * */
    public int localIndex() {
        return localIndex;
    }

    /** The name. */
    @SuppressWarnings("null")
    public String name() {
        return name;
    }

    /** The parent.  */
    @Nullable
    public Struct parent() {
        return parent;
    }

    /** The fully qualified name includes all parents separated by a '.' */
    @Nullable
    public String qualifiedName() {
        return qualifiedName;
    }

    /** Reference to the root struct. */
    @Nullable
    public Struct root() {
        return root;
    }

    /**
     * Sets the size in bits and returns a new modified instance,
     * (Doesn't modify 'this' instance)
     *
     * @param theBits the new size in bits.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setBits(final int theBits) {
        if (bits != theBits) {
            return setChildFields(name, qualifiedName, theBits, optional,
                    localIndex, parent, root, storageKey, global);
        }
        return this;
    }

    /**
     * Sets global flag (Doesn't modify 'this' instance)
     *
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setGlobal(final boolean isGlobal) {

        if (global != isGlobal) {
            return setChildFields(name, qualifiedName, bits, optional,
                    localIndex, parent, root, storageKey, isGlobal);
        }
        return this;
    }

    /**
     * Internal method, Sets theLocalIndex property and returns a new modified
     * instance,
     * (Doesn't modify 'this' instance)
     *
     * @param theLocalIndex the local index.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setLocalIndex(final int theLocalIndex) {
        if (localIndex != theLocalIndex) {
            return setChildFields(name, qualifiedName, bits, optional,
                    theLocalIndex, parent, root, storageKey, global);
        }
        return this;
    }

    /**
     * Sets a new name and returns a new modified instance,
     * (Doesn't modify 'this' instance)
     *
     * @param theName the new name.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setName(final String theName) {
        if (!objectEquals(name, theName)) {
            return setChildFields(theName, qualifiedName, bits, optional,
                    localIndex, parent, root, storageKey, global);
        }
        return this;
    }

    /**
     * Sets the OptionalFlag property and returns a new modified instance,
     * (Doesn't modify 'this' instance)
     *
     * @param theOptionalFlag the new optional flag .
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setOptional(final boolean theOptionalFlag) {
        if (theOptionalFlag != optional) {
            return setChildFields(name, qualifiedName, bits, theOptionalFlag,
                    localIndex, parent, root, storageKey, global);
        }
        return this;
    }

    /**
     * Internal method, Sets the Parent reference and returns a new modified
     * instance.
     * (Doesn't modify 'this' instance)
     *
     * @param theParent the new parent object.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setParent(@Nullable final Struct theParent) {
        if (!objectEquals(parent, theParent)) {
            return setChildFields(name, qualifiedName, bits, optional,
                    localIndex, theParent, root, storageKey, global);
        }
        return this;
    }
    /**
     * Internal method, Sets the fully qualified name and returns a new modified
     * instance. (Doesn't modify 'this' instance)
     *
     * @param theQualifiedName the new qualified name.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setQualifiedName(final String theQualifiedName) {
        if (!objectEquals(theQualifiedName, qualifiedName)) {
            return setChildFields(name, theQualifiedName, bits, optional,
                    localIndex, parent, root, storageKey, global);
        }
        return this;
    }

    /**
     * Internal method, Sets the root object and returns a new modified
     * instance, (Doesn't modify 'this' instance)
     *
     * @param theRoot the new root struct object.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setRoot(@Nullable final Struct theRoot) {
        if (!objectEquals(theRoot, root)) {
            return setChildFields(name, qualifiedName, bits, optional,
                    localIndex, parent, theRoot, storageKey, global);
        }
        return this;
    }

    /**
     * Internal method, Sets the optional parent name and returns a new modified
     * instance, (Doesn't modify 'this' instance)
     *
     * @param theStorageKey property used to identify which storage this field
     *        belongs to, null if it belongs to the base storage.
     * @return the modified instance */
    @SuppressWarnings("null")
    public ChildProperties setStorageKey(
            @Nullable final StorageKey theStorageKey) {

        if (!objectEquals(theStorageKey, storageKey)) {
            return setChildFields(name, qualifiedName, bits, optional,
                    localIndex, parent, root, theStorageKey, global);
        }
        return this;
    }

    /**
     * The storageKey to identify storage used for this child,
     * in case of list/optional child this property is used to resolve the
     * exact storage instance used for a child. All fields of a particular child
     * will have same storage key. */
    @Nullable
    public StorageKey storageKey() {
        return storageKey;
    }

}
