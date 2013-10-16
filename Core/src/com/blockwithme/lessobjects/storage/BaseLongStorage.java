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
// $codepro.audit.disable com.instantiations.assist.eclipse.arrayIsStoredWithoutCopying
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.util.StructConstants.BYTE_MASK;
import static com.blockwithme.lessobjects.util.StructConstants.INDEX_FIELD_INDEX;
import static com.blockwithme.lessobjects.util.StructConstants.MAX_NUMBER_OF_CHILDREN;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Child;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.util.StructConstants;
import com.blockwithme.msgpack.ObjectPacker;
import com.blockwithme.msgpack.Packer;
import com.blockwithme.msgpack.templates.PackerContext;

/**
 * Implements the base storage implementation for the Packed and
 * Aligned64 compilers.
 *
 * @author monster, tarung
 */
@ParametersAreNonnullByDefault
public abstract class BaseLongStorage extends AbstractStorage {

    /** The long array. */
    @Nonnull
    protected long[] array;

    /** The set defaults flag. */
    protected boolean setDefaults;

    /** struct size in bits. */
    protected final long structSize;

    /** The position of the current struct, as an array Index. */
    protected int structureIndex;

    /** The position of the current struct, in bits. */
    protected long structurePosition;

    /** Adjusts the structure size in bits to a multiple of 64. */
    protected static long adjustStructSize(long theSize) {
        final long structRest = theSize % StructConstants.LONG_BITS;
        if (structRest != 0) {
            theSize += StructConstants.LONG_BITS - structRest;
        }
        return theSize;
    }

    /** Computes a bitmask */
    protected static long mask(final int theBitsPerValue) {
        return -1L >>> StructConstants.LONG_BITS - theBitsPerValue;
    }

    /** Constructor */
    protected BaseLongStorage(final StorageBuilder theBuilder) {
        super(theBuilder);
        array = theBuilder.getArray();
        structSize = adjustStructSize(theBuilder.getStruct().bits());
        capacity = theBuilder.getCapacity();
        structurePosition = theBuilder.getStructurePosition();
    }

    /** Constructor */
    protected BaseLongStorage(final Struct theStruct, final long[] theArray,
            final long theSize, final int theCapacity,
            @Nullable final Storage theBaseStorage,
            final boolean theTransactionalFlag, final Arity theArity) {
        super(theStruct, theBaseStorage, theCapacity, theTransactionalFlag,
                theArity);
        array = theArray;
        structSize = theSize;
        capacity = theCapacity;
        structurePosition = 0;

    }

    /** Delegates clear() call to all feilds and children. */
    private void clearInternal(final Struct theChild) {
        for (final Field<?, ?> f : theChild.allStorageFields()) {
            f.clear(this);
        }
        for (final Struct c : theChild.children()) {
            if (c != null) {
                clearChild(c);
            }
        }
    }

    /** Serialize self. */
    protected void writeData(final PackerContext theContext) throws IOException {
        final ObjectPacker objPacker = theContext.objectPacker;
        final Packer packer = theContext.packer;
        packer.writeLong(structSize);
        packer.writeInt(capacity);
        packer.writeInt(structure);
        objPacker.writeObject(array);
    }

    /** Clear (set to 0/false) one field of the selected structure. */
    @Override
    public void clear(final Field<?, ?> theField) {
        theField.clear(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "null" })
    @Override
    public void clearChild(final Struct theChild) {
        checkState(!theChild.isOptional()
                && !theChild.structProperties().isList(),
                "Something is wrong !! " + "Optional/List child should "
                        + "get cleared only in Sparse storages");
        if (theChild.union()) {
            // Get localIndex of the currently selected child.
            final CharField theField = (CharField) theChild.structFields()[INDEX_FIELD_INDEX];
            final char localFieldIndex = read(theField);
            final int localIndex = localFieldIndex & StructConstants.BYTE_MASK;
            final Child chld = theChild.child(localIndex);
            if (chld.isField()) {
                ((Field) chld).clear(this);
            } else {
                clearChild((Struct) chld);
            }
        } else {
            clearInternal(theChild);
        }
    }

    /** Returns the long array. */
    @SuppressWarnings("null")
    public final long[] getArray() {
        return array;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public StorageBuilder getBuilder() {
        final StorageBuilder builder = super.getBuilder();
        builder.setArray(array);
        builder.setStructureIndex(structureIndex);
        builder.setStructurePosition(structurePosition);
        return builder;
    }

    /** {@inheritDoc} */
    @Override
    public final int getCapacity() {
        return capacity;
    }

    /** {@inheritDoc} */
    @Override
    public int getSelectedStructure() {
        return structure;
    }

    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return capacity;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Struct rootStruct() {
        return struct;
    }

    /** {@inheritDoc} */
    @Override
    public boolean selectStructure(final int theStructure) {
        checkArgument(theStructure < getCapacity(),
                "Storage capacity is less then the element index passed!");
        structure = theStructure;
        structurePosition = theStructure * structSize;
        structureIndex = (int) (structurePosition / StructConstants.LONG_BITS);
        return true;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void selectUnionPosition(final Field<?, ?> theField) {
        selectUnionPosition(theField.parent(), theField.localIndex());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void selectUnionPosition(final Struct theStruct,
            final int theChildIndex) {
        checkArgument(theChildIndex >= 0,
                "Invalid value of child index passed: " + theChildIndex);
        checkArgument(theChildIndex < MAX_NUMBER_OF_CHILDREN,
                "Invalid value of child index passed: " + theChildIndex);
        checkArgument(theStruct.union(),
                "The current structure is not a union.");
        final boolean equals = struct.equals(theStruct);
        checkArgument(
                equals || struct.child(theStruct.qualifiedName()) != null,
                "The struct passed is not backed by the current storage.");
        // Get localIndex of the currently selected child.
        final CharField<?, ?> theField = (CharField<?, ?>) theStruct
                .structFields()[INDEX_FIELD_INDEX];
        if (theField != null) {
            final char localFieldIndex = read(theField);
            final int localIndex = localFieldIndex & BYTE_MASK;
            if (localIndex != theChildIndex) {
                clearChild(theStruct);
            }
            write(theField, (char) theChildIndex);
        }
    }
}
