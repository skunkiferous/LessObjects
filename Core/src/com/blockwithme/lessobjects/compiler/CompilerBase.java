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

import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_FIELDS;
import static com.blockwithme.lessobjects.util.StructConstants.INDEX_FIELD_NAME;
import static com.blockwithme.lessobjects.util.StructConstants.MAX_STORAGE_CAPACITY;
import static com.blockwithme.lessobjects.util.StructConstants.PARENT_PREFIX;
import static com.blockwithme.lessobjects.util.Util.childrenArray;
import static com.blockwithme.lessobjects.util.Util.fieldArray;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Child;
import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.FieldGroups;
import com.blockwithme.lessobjects.beans.StructProperties;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.OptionalField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.StructConstants;
import com.google.common.base.Preconditions;

/**
 * Base class for compilers. A "compiler" takes a "naively" defined structure,
 * and move fields around, to achieve a near optimal usage of space.
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public abstract class CompilerBase implements Compiler {

    /** Separate all fields, including from children, from union children. */
    protected static final class AllFields {
        /** All non-union fields, including from children. */
        @Nonnull
        final List<Field<?, ?>> fields = new ArrayList<>();

        /** The union children (direct or indirect). */
        @Nonnull
        final List<Struct> unions = new ArrayList<>();
    }
    /** Bucket used to sort the fields. */
    protected static final class Bucket {
        /** All fields in this bucket. */
        @Nonnull
        final List<Child> children = new ArrayList<>();

        /** Total padding in bits. */
        int padding;

        /** Total size in bits. */
        int size;

        /** Adds a child, and add up it's size. */
        public void add(final Child theChild, final int theBits) {
            children.add(theChild);
            size += theBits;
        }

        /** Returns true if the field was added to this bucket. */
        public boolean addIfFits(final Child theChild, final int theBits) {
            if (size + theBits <= StructConstants.LONG_BITS) {
                add(theChild, theBits);
                return true;
            }
            return false;
        }
    }

    /** Used to build the info needed to create the compiled structures. Those
     * are the data needed for the call to Field.copy() */
    protected static class FieldData {

        /** The no fields. */
        static final FieldData NO_FIELDS = new FieldData();

        /** The field. */
        @Nonnull
        Field<?, ?> field;

        /** The index. */
        int index;

        /** The offset. */
        int offset;

        /** The struct. */
        @Nonnull
        Struct struct;

    }

    /** Compares children by size. Biggest first, and then by name. */
    protected static class SizeComparator implements Comparator<Child> {

        /** The compiled children structure/unions. */
        @Nonnull
        private final Map<Struct, Struct> compiled;

        /** Takes the compiled children as parameter. */
        protected SizeComparator(final Map<Struct, Struct> theCompiled) {
            compiled = theCompiled;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("null")
        @Override
        public int compare(final Child theChild1, final Child theChild2) {

            final int size1 = sizeOf(theChild1, compiled);
            final int size2 = sizeOf(theChild2, compiled);
            // Biggest first
            final int cmpBits = size2 - size1;
            if (cmpBits == 0) {
                // But name in normal order ...
                final String n1 = theChild1.name();
                final String n2 = theChild2.name();
                // Both array elements?
                if (n1.endsWith("]") && n2.endsWith("]")) {
                    final int i1 = n1.indexOf('[');
                    final int i2 = n2.indexOf('[');
                    final String p1 = n1.substring(0, i1);
                    final String p2 = n2.substring(0, i2);
                    // Same array?
                    if (p1.equals(p2)) {
                        // Compare index numerically.
                        final int idx1 = Integer.parseInt(n1.substring(i1 + 1,
                                n1.length() - 1));
                        final int idx2 = Integer.parseInt(n2.substring(i2 + 1,
                                n2.length() - 1));
                        return idx1 - idx2;
                    }
                    // Only compare field name
                    return p1.compareTo(p2);
                }
                // Compare names normally
                return n1.compareTo(n2);
            }
            return cmpBits;
        }
    }

    /** The FieldFactory */
    private final FieldFactory fieldFactory;

    /**
     * Append index field.
     *
     * @param theFields the fields
     * @return the list */
    @SuppressWarnings("rawtypes")
    protected static List<Field<?, ?>> appendIndexField(
            @Nullable final List<Field<?, ?>> theFields, final int theUnionSize) {

        final List<Field<?, ?>> result = new ArrayList<>(theFields == null ? 1
                : 1 + theFields.size());
        final int bits = Integer.toBinaryString(theUnionSize + 1).length();
        final CharField charField = new CharField(INDEX_FIELD_NAME, bits);

        result.add(charField);

        if (theFields != null) {
            for (final Field<?, ?> f : theFields) {
                checkArgument(!f.name().equals(INDEX_FIELD_NAME),
                        "The array of fields already contains a field by name "
                                + INDEX_FIELD_NAME
                                + " which is an internal name and not allowed!");
                result.add(f);
            }
        }
        return result;
    }

    protected static int checkCapacity(final int theCapacity) {
        checkArgument(theCapacity > 0, "Capacity cannot be negative or zero: "
                + theCapacity);
        checkArgument(theCapacity <= MAX_STORAGE_CAPACITY,
                "Maximum capacity exceeded: " + theCapacity);
        return theCapacity;
    }

    /** Really return all fields, even of unions. */
    protected static List<Field<?, ?>> getAllFields(final Struct theStruct) {
        final List<Field<?, ?>> fields = new ArrayList<>(1024);
        fields.addAll(Arrays.asList(theStruct.structFields()));
        final Struct[] structChildren = theStruct.structChildren();
        for (final Struct child : structChildren) {
            if (child != null) {
                fields.addAll(getAllFields(child));
            }
        }
        return fields;
    }

    /** Returns AllFields. */
    protected static void getAllFields(final Struct theStruct,
            final AllFields theFields) {
        final Struct[] structChildren = theStruct.structChildren();
        for (final Struct child : structChildren) {
            if (child.union()) {
                theFields.unions.add(child);
            } else {
                getAllFields(child, theFields);
            }
        }
        for (final Field<?, ?> f : theStruct.structFields()) {
            theFields.fields.add(f);
        }
    }

    /** Map the old structure to new ones. We cannot convert a struct before it's
     * children are converted, so we just iterate over the same map repeatedly,
     * converting at each iteration, the structs whose children list was
     * completed in the previous iteration. */
    @SuppressWarnings("null")
    protected static Struct oldStructToNew(final Struct theStruct,
            final int theBits, final Map<Struct, List<FieldData>> theFieldsData) {
        final Map<Struct, Struct> oldStructToNew = new IdentityHashMap<>();
        while (!theFieldsData.isEmpty()) {
            boolean foundOne = false;
            for (final Struct parent : theFieldsData.keySet().toArray(
                    new Struct[theFieldsData.size()])) {
                boolean missing = false;
                for (final Struct child : parent.structChildren()) {
                    if (child.nativeFieldCounts() > 0) {
                        // A child with no fields but only containing children
                        // will not be present in the map. So if all
                        // the child's children are already present in the map,
                        // just create a copy and put it in the map.
                        if (child.structFields().length == 0
                                && child.structChildren().length > 0) {
                            boolean childrenAreCompiled = true;
                            for (final Struct subChild : child.structChildren()) {
                                if (!oldStructToNew.containsKey(subChild)) {
                                    childrenAreCompiled = false;
                                    break;
                                }
                            }
                            if (childrenAreCompiled) {
                                final List<Struct> children = new ArrayList<>();
                                for (final Struct subChild : child
                                        .structChildren()) {
                                    children.add(oldStructToNew.get(subChild));
                                }
                                oldStructToNew.put(child, child.copy(
                                        child.name(), theBits, child.union(),
                                        childrenArray(children), EMPTY_FIELDS,
                                        false, child.structInfo()));
                            }
                        }
                        if (!oldStructToNew.containsKey(child)) {
                            missing = true;
                            break;
                        }
                    } else {
                        // children that have nothing to be compiled !
                        if (!oldStructToNew.containsKey(child)) {
                            oldStructToNew.put(child, child);
                        }
                    }
                }
                if (!missing) {
                    // No children, or all children processed ...
                    foundOne = true;
                    final List<FieldData> list = theFieldsData.remove(parent);
                    final Field<?, ?>[] newFields = new Field<?, ?>[list.size()];
                    for (int i = 0; i < newFields.length; i++) {
                        final FieldData fd = list.get(i);
                        newFields[i] = fd.field.copy(fd.field.name(), fd.index,
                                fd.offset);
                        newFields[i] = newFields[i].copy(newFields[i]
                                .properties().setOriginal(fd.field));
                    }
                    final Struct[] newChildren = new Struct[parent
                            .structChildren().length];
                    for (int i = 0; i < newChildren.length; i++) {
                        if (oldStructToNew.get(parent.structChildren()[i]) != null) {
                            newChildren[i] = oldStructToNew.get(parent
                                    .structChildren()[i]);
                        } else {
                            newChildren[i] = parent.structChildren()[i];
                        }
                    }
                    final int bits2 = parent == theStruct ? theBits : -1;
                    Arrays.sort(newFields);
                    oldStructToNew.put(parent, parent.copy(parent.name(),
                            bits2, parent.union(), newChildren, newFields,
                            false, parent.structInfo()));
                }
            }
            if (!foundOne) {
                throw new IllegalStateException("Infinite loop!");
            }
        }

        if (oldStructToNew.get(theStruct) == null) {
            final List<Struct> children = new ArrayList<>();
            for (final Struct child : theStruct.structChildren()) {
                children.add(oldStructToNew.get(child));
            }
            oldStructToNew.put(theStruct, theStruct.copy(theStruct.name(),
                    theBits, theStruct.union(), childrenArray(children),
                    EMPTY_FIELDS, false, theStruct.structInfo()));
        }

        return oldStructToNew.get(theStruct);
    }

    /** Recreates all fields, based on the Bucket configuration */
    protected static Map<Child, FieldData> processBuckets(
            final Struct theStruct, final List<Bucket> theBuckets,
            final Map<Struct, Struct> theCompiled,
            @Nullable final int[] theTotal) {

        final Map<Child, FieldData> fieldsData = new IdentityHashMap<>();
        // Create the FieldData mapping, and initialize indexAligned64 and
        // offsetAligned64
        int index = 0;
        int offset = 0;
        for (final Bucket bucket : theBuckets) {
            for (final Child c : bucket.children) {
                if (c.isField()) {
                    final Field<?, ?> f = (Field<?, ?>) c;
                    final FieldData fieldData = new FieldData();
                    fieldData.field = f;
                    fieldData.struct = f.parent();
                    fieldData.index = index++;
                    fieldData.offset = offset;
                    fieldsData.put(f, fieldData);
                    if (!theStruct.union()) {
                        offset += f.bits();
                    }
                } else {
                    // A union. We keep the values of the fields already
                    // compiled, but just offset them by our current position.
                    final Struct oldU = (Struct) c;
                    final Struct newU = theCompiled.get(oldU);
                    if (newU == null) {
                        throw new IllegalStateException(
                                "Null compiled structure for :" + oldU);
                    }
                    int count = 0;
                    for (final Field<?, ?> f : getAllFields(newU)) {
                        final FieldData fieldData = new FieldData();
                        final Field<?, ?> original = f.original();
                        fieldData.field = original;
                        if (original == null) {
                            throw new IllegalStateException(
                                    "Null original field for :" + fieldData);
                        }
                        fieldData.struct = original.parent();
                        fieldData.index = f.index() + index;
                        fieldData.offset = f.offset() + offset;
                        fieldsData.put(original, fieldData);
                        count++;
                    }
                    // Now update our position atomically
                    index += count;
                    if (!theStruct.union()) {
                        offset += newU.bits();
                    }
                }
            }
            offset += bucket.padding;
        }
        if (theTotal != null) {
            theTotal[0] = offset;
        }
        return fieldsData;
    }

    /** Returns the size of a child in bits. */
    protected static int sizeOf(final Child theChild,
            final Map<Struct, Struct> theCompiled) {
        final Child cc = theCompiled.get(theChild);
        return cc == null ? theChild.bits() : cc.bits();
    }

    /** Separate the FieldData according to owning structure */
    protected static Map<Struct, List<FieldData>> structToFieldsData(
            final Map<Child, FieldData> theFieldsData) {
        final Map<Struct, List<FieldData>> result = new IdentityHashMap<>();
        for (final FieldData fieldData : theFieldsData.values()) {
            final Struct s = fieldData.struct;
            List<FieldData> list = result.get(s);
            if (list == null) {
                list = new ArrayList<>();
                result.put(s, list);
            }
            list.add(fieldData);
        }
        return result;
    }

    /** Constructor */
    protected CompilerBase(final FieldFactory theFieldFactory) {
        fieldFactory = theFieldFactory;
    }

    /**
     * Extract meta data.
     *
     * If struct is a union remove all meta-data fields from original struct
     * and create a new structure by breaking it into parent and child
     * structs, all meta-data fields will be a part of 'parent' which is a not a
     * union and other fields will be in a union struct which will be added as a
     * child.
     * @param theStruct the struct
     * @return the e */
    @SuppressWarnings("unchecked")
    private Struct extractMetaData(final Struct theStruct) {

        List<Field<?, ?>> metaFields = null;
        final Field<?, ?>[] structFields = theStruct.structFields();
        final List<Field<?, ?>> allFields = new ArrayList<>();
        allFields.addAll(Arrays.asList(structFields));

        boolean metaDataExtracted = false;
        if (theStruct.union()) {
            metaFields = new ArrayList<>();
            if (!allFields.isEmpty()) {
                for (final Field<?, ?> f : allFields) {
                    if (f.metadata()) {
                        metaFields.add(f);
                    }
                }
                allFields.removeAll(metaFields);
            }
            metaFields = appendIndexField(metaFields, allFields.size()
                    + theStruct.children().length);
            metaDataExtracted = true;
        }

        /* We need to do the same recursively for all the children struct as well in case
         * there are union as part of child structs. */
        final List<Struct> children = new ArrayList<>();

        final Struct[] structChildren = theStruct.structChildren();
        for (final Struct c : structChildren) {
            if (c != null) {
                children.add(extractMetaData(c));
            }
        }
        final Struct child = theStruct.copy(theStruct.childProperties(),
                theStruct.structProperties().setMetaDataExtracted(false),
                childrenArray(children), fieldArray(allFields));

        if (metaFields == null) {
            return child;
        }

        final String theQName = PARENT_PREFIX + theStruct.name();

        final Struct result = child.copy(
                child.childProperties().setName(theQName)
                        .setQualifiedName(theQName), new StructProperties(
                        metaDataExtracted, false, false, false),
                new Struct[] { child }, fieldArray(metaFields));

        return result;
    }

    /**
     * Merge meta data.
     *
     * Here we do the reverse of what we did in extractMetaData method.
     *
     * @param theStruct the struct
     * @return the e */
    @SuppressWarnings("unchecked")
    private Struct mergeMetaData(final Struct theStruct) {

        final List<Field<?, ?>> allFields = new ArrayList<>();
        final List<Struct> allChildren = new ArrayList<>();

        final Struct original = theStruct.structProperties()
                .isMetaDataExtracted() ? theStruct.structChildren()[0]
                : theStruct;

        if (theStruct.structProperties().isMetaDataExtracted()) {
            allFields.addAll(Arrays.asList(theStruct.structFields()));
        }
        allFields.addAll(Arrays.asList(original.structFields()));

        for (final Struct child : original.structChildren()) {
            if (child != null) {
                final Struct mergedChild = mergeMetaData(child);
                allChildren.add(mergedChild);
            }
        }

        final Struct[] childrenArray = childrenArray(allChildren);
        for (int i = 0; i < childrenArray.length; i++) {
            childrenArray[i] = childrenArray[i].copy(childrenArray[i]
                    .childProperties().setParent(null).setRoot(null), true);
        }
        final Field<?, ?>[] fArray = fieldArray(allFields);
        for (int i = 0; i < fArray.length; i++) {
            fArray[i] = fArray[i].copy(fArray[i].properties().setCompiled(true)
                    .setParent(null).setRoot(null));
        }

        final Struct merged = original.copy(
                original.childProperties().setBits(theStruct.bits())
                        .setRoot(null).setParent(null), original
                        .structProperties().setMetaDataExtracted(false),
                childrenArray, fArray);

        return merged;
    }

    /** Compile internally. */
    protected abstract Struct compileInternaly(final Struct theStruct);

    protected void processChildren(final Struct theStruct,
            final List<Child> theChildren,
            final Map<Struct, Struct> theCompiledMap) {

        theChildren.addAll(Arrays.asList(theStruct.structFields()));
        theChildren.addAll(Arrays.asList(theStruct.structChildren()));
        for (final Struct c : theStruct.structChildren()) {
            if (c != null) {
                if (!theCompiledMap.containsKey(c)) {
                    theCompiledMap.put(c, compileInternaly(c));
                }
            }
        }
    }

    /** Compiles this bit field by correctly initializing all values of all
     * fields. The Compiler parameter is optional. */
    @Override
    public final Struct compile(final Struct theStruct) {
        Struct resturctured = extractMetaData(theStruct);
        // Compile all the optional, list children separately.
        if (resturctured.allListChildren().length > 0
                || resturctured.allOptionalChildren().length > 0) {
            resturctured = resturctured.compileDetachedChildren(this,
                    resturctured.storageKey());
        }
        Struct result;
        if (resturctured.nativeFieldCounts() > 0) {
            result = compileInternaly(resturctured);
        } else {
            // nothing compiled.
            result = resturctured;
        }
        result = mergeMetaData(result);
        Struct globalStruct = result.globalStruct();

        if (globalStruct != null) {
            globalStruct = compile(globalStruct);
            final FieldGroups fg = new FieldGroups(result.structFields(),
                    globalStruct.fields(), result.structOptionalFields(),
                    result.structVirtualFields(), result.structObjectFields());
            // this will replace the global fields with compiled global fields.
            result = result.copy(result.childProperties(),
                    result.structProperties(), result.structChildren(), fg,
                    true);
        }
        result = result.copy(result.structProperties().setCompiled(true));
        for (final Field<?, ?> field : result.allStorageFields()) {
            Objects.requireNonNull(field.converterClassName(), "Field " + field
                    + " has no converter");
            final int objIndex = field.properties().objectFieldIndex();
            if (field instanceof ObjectField) {
                Preconditions.checkState(objIndex >= 0, "objectFieldIndex("
                        + objIndex + ") of " + field + " < 0");
            } else {
                Preconditions.checkState(objIndex < 0, "objectFieldIndex("
                        + objIndex + ") of " + field + " >= 0");
            }
            if (field instanceof OptionalField && !field.object()) {
                @SuppressWarnings("rawtypes")
                final int optIndex = ((OptionalField) field)
                        .optionalFieldIndex();
                Preconditions.checkState(optIndex >= 0, "optionalFieldIndex("
                        + optIndex + ") of " + field + " < 0");
            }
        }
        return result;
    }

    /** Returns the field factory. It must be used while creating the initial
     * structure. */
    @SuppressWarnings("null")
    @Override
    public final FieldFactory getFieldFactory() {
        return fieldFactory;
    }

    public abstract Storage initStorage(final Struct theStruct,
            final int theInitialCapacity, final Storage theParent,
            final boolean isTransactional, Arity theArity);
}
