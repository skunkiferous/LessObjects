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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.StorageKey;
import com.blockwithme.prim.Converter;

/**
 * The Field level Properties bean class.
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("hashcode")
public final class FieldProperties extends ChildProperties {

    /** The field index (first, second, ...) in that structure. */
    private final int index;

    /** Is a meta data field ? */
    private final boolean metadata;

    /** The offset. */
    private final int offset;

    /** The original. */
    /** HACK! We keep a temporary reference to the original field, when
     * compiling. */
    // TODO Can we somehow do without?
    private final Field<?, ?> original;

    /** The unique Child index, inside the struct hierarchy */
    private final int uniqueIndex;

    /** Is Virtual ? */
    private final boolean virtual;

    /** Is already compiled ? */
    protected final boolean compiled;

    /** Indicates if this field is an object field */
    protected final boolean object;

    /** The Converter; every field has one, even the primitive fields. */
    protected final Converter<?> converter;

    /** The *object* field index. */
    protected final int objectFieldIndex;

    /** Creates Field Properties */
    private FieldProperties(final boolean isMetadata, final boolean isVirtual,
            final boolean isObject, final int theOffSet, final int theIndex,
            final int theUniqueIndex,
            @Nullable final Field<?, ?> theOriginalField,
            final boolean isCompiled,
            @Nullable final Converter<?> theConverter,
            final int theObjectFieldIndex, final int theBits,
            final String theName, @Nullable final String theQName,
            final boolean isOptional, final int theLocalIndex,
            @Nullable final Struct theParent, @Nullable final Struct theRoot,
            @Nullable final StorageKey theStorageKey, final boolean isGlobal) {

        super(theName, theQName, theBits, isOptional, theLocalIndex, theParent,
                theRoot, theStorageKey, isGlobal);

        metadata = isMetadata;
        virtual = isVirtual;
        uniqueIndex = theUniqueIndex;
        index = theIndex;
        offset = theOffSet;
        original = theOriginalField;
        object = isObject;
        compiled = isCompiled;
        converter = theConverter;
        objectFieldIndex = theObjectFieldIndex;
    }

    /** Instantiates a new field properties. */
    public FieldProperties(final String theName, final int theBits) {

        super(theName, theBits);
        metadata = false;
        virtual = false;
        uniqueIndex = -1;
        index = -1;
        offset = -1;
        original = null;
        object = false;
        compiled = false;
        converter = null;
        objectFieldIndex = -1;
    }

    @Override
    @SuppressWarnings({ "static-method", "null" })
    protected ChildProperties setChildFields(final String theName,
            @Nullable final String theQName, final int theBits,
            final boolean isOptional, final int theLocalIndex,
            @Nullable final Struct theParent, @Nullable final Struct theRoot,
            @Nullable final StorageKey theStorageKey, final boolean isGlobal) {

        return new FieldProperties(metadata, virtual, object, offset, index,
                uniqueIndex, original, compiled, converter, objectFieldIndex,
                theBits, theName, theQName, isOptional, theLocalIndex,
                theParent, theRoot, theStorageKey, isGlobal);
    }
    /** Is already compiled ? */
    public boolean compiled() {
        return compiled;
    }

    /**
     * @return the converter
     */
    @SuppressWarnings("null")
    public Converter<?> converter() {
        return converter;
    }

    @SuppressWarnings({ "unused", "null" })
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
        final FieldProperties other = (FieldProperties) theObj;
        if (compiled != other.compiled) {
            return false;
        }
        final Class<?> cls = converter == null ? null : converter.getClass();
        final Class<?> otherCls = other.converter == null ? null
                : other.converter.getClass();
        if (cls == null) {
            if (otherCls != null) {
                return false;
            }
        } else if (!cls.equals(otherCls)) {
            return false;
        }
        if (index != other.index) {
            return false;
        }
        if (metadata != other.metadata) {
            return false;
        }
        if (object != other.object) {
            return false;
        }
        if (objectFieldIndex != other.objectFieldIndex) {
            return false;
        }
        if (offset != other.offset) {
            return false;
        }
        if (original == null) {
            if (other.original != null) {
                return false;
            }
        } else if (!original.equals(other.original)) {
            return false;
        }
        if (uniqueIndex != other.uniqueIndex) {
            return false;
        }
        if (virtual != other.virtual) {
            return false;
        }
        return true;
    }

    public boolean hasDefaultValues() {

        if (isOptional() || global() || virtual || object) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (compiled ? 1231 : 1237);
        final Class<?> cls = converter == null ? null : converter.getClass();
        result = prime * result + (cls == null ? 0 : cls.hashCode());
        result = prime * result + index;
        result = prime * result + (metadata ? 1231 : 1237);
        result = prime * result + (object ? 1231 : 1237);
        result = prime * result + objectFieldIndex;
        result = prime * result + offset;
        result = prime * result + (original == null ? 0 : original.hashCode());
        result = prime * result + uniqueIndex;
        result = prime * result + (virtual ? 1231 : 1237);
        return result;
    }

    /**
     * @return the index
     */
    public int index() {
        return index;
    }

    /**
     * @return the meta-data flag
     */
    public boolean metadata() {
        return metadata;
    }

    /** Is field an object type */
    public boolean object() {
        return object;
    }

    /** @return the objectFieldIndex */
    public int objectFieldIndex() {
        return objectFieldIndex;
    }

    /**
     * @return the offset
     */
    public int offset() {
        return offset;
    }

    /**
     * @return the original field
     */
    @Nullable
    public Field<?, ?> original() {
        return original;
    }

    @SuppressWarnings("null")
    public FieldProperties setChildFields(final ChildProperties theCP) {
        return new FieldProperties(metadata, virtual, object, offset, index,
                uniqueIndex, original, compiled, converter, objectFieldIndex,
                theCP.bits(), theCP.name(), theCP.qualifiedName(),
                theCP.isOptional(), theCP.localIndex(), theCP.parent(),
                theCP.root(), theCP.storageKey(), theCP.global());

    }
    /** Sets isCompiled flag. */
    public FieldProperties setCompiled(final boolean isCompiled) {
        if (isCompiled != compiled) {
            return new FieldProperties(metadata, virtual, object, offset,
                    index, uniqueIndex, original, isCompiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the converter. */
    @SuppressWarnings("null")
    public FieldProperties setConverter(final Converter<?> theConverter) {
        if (!objectEquals(theConverter, converter)) {
            return new FieldProperties(metadata, virtual, object, offset,
                    index, uniqueIndex, original, compiled, theConverter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the index.
     *
     * @param theIndex the index
     * @return the field properties */
    public FieldProperties setIndex(final int theIndex) {
        if (theIndex != index) {
            return new FieldProperties(metadata, virtual, object, offset,
                    theIndex, uniqueIndex, original, compiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the is-object flag.
    *
    * @return the field properties new instance */
    public FieldProperties setObject(final boolean theObject) {
        if (theObject != object) {
            return new FieldProperties(metadata, virtual, theObject, offset,
                    index, uniqueIndex, original, compiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the object field index. */
    public FieldProperties setObjectFieldIndex(final int theObjectFieldIndex) {

        if (theObjectFieldIndex != objectFieldIndex) {
            return new FieldProperties(metadata, virtual, object, offset,
                    index, uniqueIndex, original, compiled, converter,
                    theObjectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the offset.
     *
     * @param theOffset the offset
     * @return the field properties */
    public FieldProperties setOffset(final int theOffset) {

        if (theOffset != offset) {
            return new FieldProperties(metadata, virtual, object, theOffset,
                    index, uniqueIndex, original, compiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the original field.
     *
     * @param theOriginalField the original field instance before compilation.
     * @return the field properties */
    @SuppressWarnings("null")
    public FieldProperties setOriginal(final Field<?, ?> theOriginalField) {

        if (!objectEquals(theOriginalField, original)) {
            return new FieldProperties(metadata, virtual, object, offset,
                    index, uniqueIndex, theOriginalField, compiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    /** Sets the unique index.
     *
     * @param theUniqueIndex the unique index
     * @return the field properties */
    public FieldProperties setUniqueIndex(final int theUniqueIndex) {
        if (theUniqueIndex != uniqueIndex) {
            return new FieldProperties(metadata, virtual, object, offset,
                    index, theUniqueIndex, original, compiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }
    /** Sets the virtual flag.

     * @param theVirtualFlag the virtual flag
     * @return the field properties */
    public FieldProperties setVirtual(final boolean theVirtualFlag) {
        if (theVirtualFlag != virtual) {
            return new FieldProperties(metadata, theVirtualFlag, object,
                    offset, index, uniqueIndex, original, compiled, converter,
                    objectFieldIndex, bits(), name(), qualifiedName(),
                    isOptional(), localIndex(), parent(), root(), storageKey(),
                    global());
        }
        return this;
    }

    @Override
    public String toString() {
        return "FieldProperties [global=" + global() + ", index=" + index
                + ", metadata=" + metadata + ", offset=" + offset
                + ", original=" + original + ", uniqueIndex=" + uniqueIndex
                + ", virtual=" + virtual + ", compiled=" + compiled
                + ", object=" + object + ", optional=" + isOptional()
                + ", converter=" + converter + ", objectFieldIndex="
                + objectFieldIndex + "]";
    }

    /**
     * @return the uniqueIndex
     */
    public int uniqueIndex() {
        return uniqueIndex;
    }

    /**
     * @return the virtual
     */
    public boolean virtual() {
        return virtual;
    }

}
