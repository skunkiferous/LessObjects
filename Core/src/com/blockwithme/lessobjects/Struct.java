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
// $codepro.audit.disable stringConcatenationInLoop
package com.blockwithme.lessobjects;

import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_CHILDREN;
import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_FIELDS;
import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_OBJECTS;
import static com.blockwithme.lessobjects.util.StructConstants.INDEX_FIELD_INDEX;
import static com.blockwithme.lessobjects.util.StructConstants.MAX_NUMBER_OF_CHILDREN;
import static com.blockwithme.lessobjects.util.StructConstants.PARENT_PREFIX;
import static com.blockwithme.lessobjects.util.Util.childrenArray;
import static com.blockwithme.lessobjects.util.Util.fieldArray;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.ChildProperties;
import com.blockwithme.lessobjects.beans.ChildrenGroups;
import com.blockwithme.lessobjects.beans.FieldGroups;
import com.blockwithme.lessobjects.beans.StructProperties;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.OptionalField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.schema.StructSchema;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.JSONParser;
import com.blockwithme.lessobjects.util.StructConstants;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * Represents a bit field structure *schema*. This class does not actually contain
 * the data, but rather knows how to read and write it. You instantiate this class.
 * Then you call the compile method, so that an optimized version gets created.
 * Since the purpose is to represent large arrays of bit-field structures
 * (and unions), we cannot represent fields of varying size. The size
 * of a union will be the size of it's biggest field.
 *
 * @author monster, tarung
 */
@ParametersAreNonnullByDefault
public final class Struct extends Child {

    /**
     * The DefaultSchemaMapper class provides default implementation of SchemaMigrator interface.
     */
    private static class DefaultSchemaMigrator implements SchemaMigrator {

        /** {@inheritDoc} */
        @SuppressWarnings("null")
        @Override
        public Map<Field<?, ?>, Field<?, ?>> mapSchema(
                final Struct theNewStruct, final Struct theOldStruct) {

            final Map<Field<?, ?>, Field<?, ?>> result = new HashMap<>();
            final Map<String, Field<?, ?>> oldNameMap = theOldStruct.qualifiedNameFieldMap;
            final Map<String, Field<?, ?>> newNameMap = theNewStruct.qualifiedNameFieldMap;
            for (final Field<?, ?> newField : newNameMap.values()) {
                result.put(newField, oldNameMap.get(newField.qualifiedName()));
            }
            return Collections.unmodifiableMap(result);
        }

        /** {@inheritDoc} */
        @Override
        public void postProcessor(final Storage theSource,
                final Storage theDestination, final int theElementIndex,
                final int theDestinationIndex) {
            // NOP
        }
    }

    /**
     *  A Class used as a temporary structure to store Indexed Fields.
     */
    private static class IndexedFields {

        /** The last index. */
        int lastIndex;

        /** The new fields. */
        Field<?, ?>[] newFields;

        /**  Instantiates a new indexed fields. */
        IndexedFields(final int theLastIndex, final Field<?, ?>[] theNewFields) {
            lastIndex = theLastIndex;
            newFields = theNewFields;
        }
    }

    /**
     * An internal Class used for indexing a Field
     * instance based on the current index values.
     */
    private static class Indexer {

        /** The object index. */
        int objectIndex;

        /** The optional index. */
        int optionalIndex;

        /** The unique index. */
        int uniqueIndex;

        Indexer(final int theUniqueIndex, final int theObjectIndex,
                final int theOptionalIndex) {
            uniqueIndex = theUniqueIndex;
            objectIndex = theObjectIndex;
            optionalIndex = theOptionalIndex;
        }

        @SuppressWarnings("unused")
        Field<?, ?> index(final Field<?, ?> theField) {
            Field<?, ?> field = theField;
            if (theField instanceof ObjectField) {
                field = ((ObjectField<?, ?>) field)
                        .setObjectIndex(objectIndex++);
            } else if (theField.isOptional()) {
                field = ((OptionalField<?, ?>) field).copy(optionalIndex++);
            }
            field = field.copy(field.properties.setUniqueIndex(uniqueIndex++));
            return field;
        }
    }

    /**
     *  In case of Composite Storages i.e. {@link com.blockwithme.lessobjects.storage.aligned64.Aligned64CompositeStorage}
     *  and {@link com.blockwithme.lessobjects.storage.packed.PackedCompositeStorage},
     *  each child storage object is identified by a 'StorageKey'. StorageKey is used by the read and write operations
     *  of Composite Storage to resolve the Storage object for a Field, i.e. When a Struct contains optional children,
     *  A Field that belongs to an optional child is stored in a child sparse storage object. The Base Composite Storage
     *  object uses StorageKey to resolve which child object should be used to read/write Field value.
     */
    public static class StorageKey {

        /** The key. */
        private final int key;

        /** The parent key. */
        private final StorageKey parentKey;

        /**
         * Instantiates a new storage key.
         */
        public StorageKey(final int theKey,
                @Nullable final StorageKey theParentKey) {
            key = theKey;
            parentKey = theParentKey;
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(@Nullable final Object theOther) {
            if (this == theOther) {
                return true;
            }
            if (theOther == null) {
                return false;
            }
            if (getClass() != theOther.getClass()) {
                return false;
            }
            final StorageKey other = (StorageKey) theOther;
            if (key != other.key) {
                return false;
            }
            if (parentKey == null) {
                if (other.parentKey != null) {
                    return false;
                }
            } else if (!parentKey.equals(other.parentKey)) {
                return false;
            }
            return true;
        }

        /** The storage key value. */
        public int getKey() {
            return key;
        }

        /** Parent Storage key reference.*/
        @Nullable
        public StorageKey getParentKey() {
            return parentKey;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            // TODO Should this be recorded as final instead?
            final int prime = 31;
            int result = 1;
            result = prime * result + key;
            result = prime * result
                    + (parentKey == null ? 0 : parentKey.hashCode());
            return result;
        }
    }

    /**
     * The Class used to represent mapping between a field and it's
     * UD (Union discriminator) field and expected value.
     *
     */
    public static class UnionDiscriminatorValueMapping {

        /** Union discriminator field. */
        @Nonnull
        private final CharField<?, ?> field;

        /** Union discriminator value. */
        private final char value;

        public UnionDiscriminatorValueMapping(final CharField<?, ?> theField,
                final char theValue) {
            value = theValue;
            field = theField;
        }

        /** Union discriminator field. */
        @SuppressWarnings("null")
        public CharField<?, ?> field() {
            return field;
        }

        /** Union discriminator value. */
        public char value() {
            return value;
        }

    }

    /** The empty UD Mapping Array. */
    private static final UnionDiscriminatorValueMapping[] EMPTY_UD_MAPPING_ARRAY = new UnionDiscriminatorValueMapping[0];

    /** The default schema migrator. */
    public static final DefaultSchemaMigrator DEFAULT_SCHEMA_MIGRATOR = new DefaultSchemaMigrator();

    /** Set of all the fields with that need to be reset, including that of all the children */
    @Nonnull
    private final List<Field<?, ?>> allStorageFields;

    /** The children structures embedded in this structure. */
    @Nonnull
    private final Struct[] children;

    /** The total field count. */
    private final int fieldCount;

    /** The fields belonging to this structure. */
    @Nonnull
    private final Field<?, ?>[] fields;

    /** The global fields. */
    @Nonnull
    private final Field<?, ?>[] globalFields;

    /** The struct that contains all global fields */
    @Nullable
    private final Struct globalStruct;

    /** The list children. */
    @Nonnull
    private final Struct[] listChildren;

    /** The local index map. */
    @Nonnull
    private final transient IntObjectOpenHashMap<Child> localIndexMap;

    /** The children belonging to this structure, mapped by name. */
    private final Map<String, Struct> nameChildMap;

    /** The fields belonging to this structure, mapped by name. */
    private final Map<String, Field<?, ?>> nameFieldMap;

    /** The name prefix, used to search name */
    @Nonnull
    private final String namePrefix;

    /** The count of all the 'normal' fields including that of children struct. */
    private final int nativeFieldCounts;

    /** The object count. */
    private final int objectCount;

    /** The object fields. */
    @Nonnull
    private final ObjectField<?, ?>[] objectFields;

    /** The optional children. */
    @Nonnull
    private final Struct[] optionalChildren;

    /** The optional fields. */
    @Nonnull
    private final Field<?, ?>[] optionalFields;

    /** The optional field count. */
    private final int optionalFieldsCount;

    /** The fields belonging to this structure, mapped by qualified name. */
    private final Map<String, Field<?, ?>> qualifiedNameFieldMap;

    /** The schema. */
    private final StructSchema schema;

    /** The struct info object containing struct meta data. */
    private final StructInfo structInfo;

    /** The struct properties. */
    @Nonnull
    private final StructProperties structProperties;

    /** toString */
    @Nonnull
    private final String toString;

    /** The union discriminator map. */
    private final Map<Field<?, ?>, UnionDiscriminatorValueMapping[]> unionDiscriminatorMap;

    /** The virtual fields. */
    @Nonnull
    private final Field<?, ?>[] virtualFields;

    /** Checks if a field exists in the list of fields, this method uses field
        name for the comparison. */
    private static boolean checkIfExists(final List<Field<?, ?>> theList,
            final Field<?, ?> theField) {
        for (final Field<?, ?> f : theList) {
            if (f.name().equals(theField.name())) {
                return true;
            }
        }
        return false;
    }

    /** Returns a Combined group of fields and children after indexing. */
    private static ChildrenGroups combineAndIndexChildren(
            final ChildrenGroups theChildrenGroups, final boolean theReIndexFlag) {
        if (theReIndexFlag) {
            return indexChildren(theChildrenGroups);
        }
        return theChildrenGroups;
    }

    /** Returns InternalGlobalStruct with global fields including the children
     * global fields. */
    @Nullable
    private static Struct globalStruct(final String theName,
            final Field<?, ?>[] theFields,
            @Nullable final Struct[] theChildren, final boolean isCompiled) {

        final List<Field<?, ?>> resultList = new ArrayList<>();
        for (final Field<?, ?> f : theFields) {
            if (f.global()) {
                resultList.add(f);
            }
        }
        if (theChildren != null) {
            for (final Struct c : theChildren) {
                // no need to make recursive calls since the globalStructs will
                // contain global fields from all the children below it.
                if (c.globalStruct != null) {
                    for (final Field<?, ?> f : c.globalStruct.structFields()) {
                        if (f != null) {
                            // Check here if field already exists in the list.
                            // the Global Fields array will contain global
                            // fields from children, in case the constructor is
                            // being called through copy method.
                            if (!checkIfExists(resultList, f)) {
                                resultList.add(f);
                            }
                        }
                    }
                }
            }
        }
        if (resultList.size() == 0) {
            return null;
        }
        // Calculate struct size here.
        int size = 0;
        for (final Field<?, ?> f : resultList) {
            size += f.bits();
        }

        final Field<?, ?>[] fArray = fieldArray(resultList);

        final StructProperties structProperties = new StructProperties(false,
                false, false, isCompiled);
        final ChildrenGroups theChildrenGroups = new ChildrenGroups(
                new FieldGroups(FieldGroups.normalOnly(fArray, true),
                        EMPTY_FIELDS, EMPTY_FIELDS, EMPTY_FIELDS,
                        FieldGroups.objectOnly(fArray, true)), EMPTY_CHILDREN);

        return new Struct(new ChildProperties(theName, size).setGlobal(true),
                structProperties, theChildrenGroups, (StructInfo) null, true);
    }

    /** Index children. */
    private static ChildrenGroups indexChildren(
            final ChildrenGroups theChildrenGroups) {

        /* Start setting 'localIndex'
         *
         * Setting local index - all direct children (Fields and Structs) are
         * indexed. local index is used as union discriminator. */
        int count = 0;

        final FieldGroups fieldGroups = theChildrenGroups.fieldGroups();

        final IndexedFields indexedFields = indexFields(count,
                fieldGroups.fields());
        final Field<?, ?>[] newFields = indexedFields.newFields;
        count += indexedFields.lastIndex;

        final IndexedFields indexedOptionalFields = indexFields(count,
                fieldGroups.optionalFields());
        final Field<?, ?>[] newOptionalFields = indexedOptionalFields.newFields;
        count += newOptionalFields.length;

        final IndexedFields indexedObjectFields = indexFields(count,
                fieldGroups.objectFields());
        final ObjectField<?, ?>[] newObjectFields = (ObjectField<?, ?>[]) indexedObjectFields.newFields;
        count += newObjectFields.length;

        final FieldGroups newGroup = new FieldGroups(newFields,
                fieldGroups.globalFields(), newOptionalFields,
                fieldGroups.virtualFields(), newObjectFields);

        final Struct[] childrenArray = theChildrenGroups.children();
        final Struct[] newChildrenArray = new Struct[childrenArray.length];
        count = updateChildrenIndex(count, childrenArray, newChildrenArray);

        final Struct[] oChildrenArray = theChildrenGroups.optionalChildren();
        final Struct[] newOChildren = new Struct[oChildrenArray.length];
        count = updateChildrenIndex(count, oChildrenArray, newOChildren);

        final Struct[] listChildrenArray = theChildrenGroups.listChildren();
        final Struct[] newLChildren = new Struct[listChildrenArray.length];
        count = updateChildrenIndex(count, listChildrenArray, newLChildren);

        /* End Setting localIndex */
        /* Start setting other indexes like index, optionalFieldIndex,
         * ObjectFieldIndex and storageKey. */
        final Indexer indexer = new Indexer(0, 0, 0);

        final Field<?, ?>[][] allFields = newGroup.allFields();
        final Struct[][] allChildren = new Struct[][] { newChildrenArray,
                newOChildren, newLChildren };
        indexFields(allFields, allChildren, indexer);

        final ChildrenGroups indexedChildren = new ChildrenGroups(newGroup,
                newChildrenArray, newOChildren, newLChildren);

        return indexedChildren;

    }

    /** Index normal fields. */
    @SuppressWarnings("null")
    private static void indexFields(@Nullable final Field<?, ?>[][] theFields,
            @Nullable final Struct[][] theChildren,
            @Nullable final Indexer theIndexer) {

        if (theFields != null) {
            for (final Field<?, ?>[] flds : theFields) {
                if (flds != null) {
                    for (int i = 0; i < flds.length; i++) {
                        flds[i] = theIndexer.index(flds[i]);
                    }
                }
            }
        }

        if (theChildren != null) {
            for (final Struct[] chld : theChildren) {
                if (chld != null) {
                    for (int i = 0; i < chld.length; i++) {

                        final Field<?, ?>[][] childsFields = { chld[i].fields,
                                chld[i].optionalFields, chld[i].globalFields,
                                chld[i].objectFields };

                        final Struct[][] childsChildren = new Struct[][] {
                                chld[i].children, chld[i].optionalChildren,
                                chld[i].listChildren };

                        Indexer childIndexer;
                        if (chld[i].isOptional() || chld[i].list()) {
                            childIndexer = new Indexer(theIndexer.uniqueIndex,
                                    0, 0);
                        } else {
                            childIndexer = theIndexer;
                        }
                        indexFields(childsFields, childsChildren, childIndexer);
                        theIndexer.uniqueIndex = childIndexer.uniqueIndex;
                    }
                }
            }
        }
    }

    /** Index fields. */
    private static IndexedFields indexFields(final int theCount,
            final Field<?, ?>[] theFieldArray) {

        final Field<?, ?>[] result = new Field<?, ?>[theFieldArray.length];
        int count = theCount;
        for (int i = 0; i < theFieldArray.length; i++) {
            checkState(count <= MAX_NUMBER_OF_CHILDREN,
                    "Total number  fields and children allowed for a Struct is "
                            + MAX_NUMBER_OF_CHILDREN);
            final Field<?, ?> f = theFieldArray[i];
            if (f.name().equals(StructConstants.INDEX_FIELD_NAME)) {
                result[i] = f.copy(f.childProperties().setLocalIndex(-1));
            } else {
                result[i] = f.copy(f.properties().setLocalIndex(count++));
            }
        }
        return new IndexedFields(count, result);
    }

    /** Index fields. */
    private static IndexedFields indexFields(int theCount,
            final ObjectField<?, ?>[] theFieldArray) {
        final ObjectField<?, ?>[] result = new ObjectField<?, ?>[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            checkState(theCount <= MAX_NUMBER_OF_CHILDREN,
                    "Total number  fields and children allowed for a Struct is "
                            + MAX_NUMBER_OF_CHILDREN);
            final ObjectField<?, ?> f = theFieldArray[i];
            result[i] = f.copy(f.childProperties().setLocalIndex(theCount++));
        }
        return new IndexedFields(theCount, result);
    }

    @SuppressWarnings("null")
    private static void populateDiscriminatorMap(
            final List<Field<?, ?>> theFields,
            final Map<Field<?, ?>, UnionDiscriminatorValueMapping[]> theUDMap) {

        for (final Field<?, ?> f : theFields) {

            final List<UnionDiscriminatorValueMapping> mappings = new ArrayList<>();
            Struct parent = f.parent();
            Child child = f;
            final Field<?, ?>[] structFields = parent.structFields();

            if (parent.union() && f == structFields[INDEX_FIELD_INDEX]) {
                // if the field is a UD field we need to check if
                // there is any union up the hierarchy, if present
                // insert the related union discriminator fields.
                child = parent;
                parent = parent.parent();
            }
            while (parent != null) {
                if (parent.union()) {
                    // this indicates that the child belongs to a Union.
                    final Field<?, ?>[] parentFields = parent.structFields();
                    mappings.add(new UnionDiscriminatorValueMapping(
                            (CharField<?, ?>) parentFields[INDEX_FIELD_INDEX],
                            (char) child.localIndex()));

                }
                child = parent;
                parent = parent.parent();
            }
            if (mappings.size() > 0) {
                theUDMap.put(f, mappings
                        .toArray(new UnionDiscriminatorValueMapping[mappings
                                .size()]));
            } else {
                theUDMap.put(f, EMPTY_UD_MAPPING_ARRAY);
            }
        }
    }

    /** Generates the QualifiedName by traversing up the hierarchy. */
    private static String qualifiedName(final Child theChild) {
        String qualifiedName = theChild.name();
        Struct prnt = theChild.parent();
        while (prnt != null) {
            qualifiedName = prnt.namePrefix + qualifiedName;
            prnt = prnt.parent();
        }
        return qualifiedName;
    }

    /** Update children index. */
    private static int updateChildrenIndex(final int theCount,
            final Struct[] theChildrenArray, final Struct[] theNewChildrenArray) {

        int count = theCount;
        if (theNewChildrenArray.length > 0) {
            for (int i = 0; i < theChildrenArray.length; i++) {
                checkState(count <= MAX_NUMBER_OF_CHILDREN,
                        "Total number  fields and children allowed for a Struct is "
                                + MAX_NUMBER_OF_CHILDREN);
                final Struct chld = theChildrenArray[i];
                theNewChildrenArray[i] = chld.copy(chld.childProperties()
                        .setLocalIndex(count++), false);
            }
        }
        return count;
    }

    /**
     * Creates a Struct instance from schema string.
     *
     * @param theSchema the schema.
     * @param theFormat the format.
     * @return the struct.
     */
    public static Struct fromSchemaString(final String theSchema,
            final SchemaFormat theFormat) {
        return new JSONParser(theSchema, theFormat).struct();
    }

    /** Private constructor - the actual constructor, assumes all fields are
     * indexed as needed. */
    @SuppressWarnings("null")
    private Struct(final ChildProperties theChildProperties,
            final StructProperties theStructProperties,
            final ChildrenGroups theChildren,
            @Nullable final StructInfo theStructInfo) {

        super(theChildProperties, false);
        checkNotNull(theChildren);

        structProperties = theStructProperties;
        namePrefix = name() + ".";
        structInfo = theStructInfo;
        localIndexMap = new IntObjectOpenHashMap<>();

        Struct rootStruct = root();
        rootStruct = rootStruct == null ? this : rootStruct;

        final Map<String, Struct> childMap = new TreeMap<>();
        final Map<String, Field<?, ?>> fieldMap = new TreeMap<>();
        final List<Field<?, ?>> storageFields = new ArrayList<>();

        final FieldGroups fg = theChildren.fieldGroups();
        final Field<?, ?>[] strtFields = fg.fields();
        final Field<?, ?>[] strtOptionalFields = fg.optionalFields();
        final Field<?, ?>[] strtVirtualFields = fg.virtualFields();
        final Field<?, ?>[] strtGlobalFields = fg.globalFields();
        final ObjectField<?, ?>[] strtObjectFields = fg.objectFields();

        int tempNativeFieldCounts = strtFields.length;

        String tmpToString = (structProperties().union() ? "union " : "struct ")
                + name() + " {\n";

        Field<?, ?>[] tempFields = EMPTY_FIELDS;
        Field<?, ?>[] tempOptionalFields = EMPTY_FIELDS;
        Field<?, ?>[] tempVirtualFields = EMPTY_FIELDS;
        Field<?, ?>[] tempGlobalFields = EMPTY_FIELDS;

        ObjectField<?, ?>[] tempObjectFields = EMPTY_OBJECTS;

        if (strtFields.length > 0) {
            tempFields = new Field<?, ?>[strtFields.length];
            int count = 0;
            for (final Field<?, ?> field : strtFields) {
                if (field != null) {
                    final Field<?, ?> updated = updateField(field, rootStruct);
                    checkDuplicate(fieldMap, updated);
                    fieldMap.put(updated.name(), updated);
                    tempFields[count++] = updated;
                    tmpToString += updated + ",\n";
                    storageFields.add(updated);
                    localIndexMap.put(updated.localIndex(), updated);
                }
            }
        }

        if (strtOptionalFields.length > 0) {
            int count = 0;
            tempOptionalFields = new Field<?, ?>[strtOptionalFields.length];
            for (final Field<?, ?> field : strtOptionalFields) {
                if (field != null) {
                    final Field<?, ?> updated = updateField(field, rootStruct);
                    checkDuplicate(fieldMap, updated);
                    fieldMap.put(updated.name(), updated);
                    tmpToString += updated + ",\n";
                    tempOptionalFields[count++] = updated;
                    storageFields.add(updated);
                    localIndexMap.put(updated.localIndex(), updated);
                }
            }
        }
        if (strtVirtualFields.length > 0) {
            int count = 0;
            tempVirtualFields = new Field<?, ?>[strtVirtualFields.length];
            for (final Field<?, ?> field : strtVirtualFields) {
                if (field != null) {
                    final Field<?, ?> updated = updateField(field, rootStruct);
                    checkDuplicate(fieldMap, updated);
                    fieldMap.put(updated.name(), updated);
                    tmpToString += updated + ",\n";
                    tempVirtualFields[count++] = updated;
                }
            }
        }

        if (strtGlobalFields.length > 0) {
            int count = 0;
            tempGlobalFields = new Field<?, ?>[strtGlobalFields.length];
            for (final Field<?, ?> field : strtGlobalFields) {
                if (field != null) {
                    final Field<?, ?> updated = updateField(field, rootStruct);
                    tmpToString += updated + ",\n";
                    tempGlobalFields[count++] = updated;
                    storageFields.add(updated);
                }
            }
        }
        if (strtObjectFields.length > 0) {
            int count = 0;
            tempObjectFields = new ObjectField<?, ?>[strtObjectFields.length];
            for (final ObjectField<?, ?> field : strtObjectFields) {
                if (field != null) {
                    final ObjectField<?, ?> updated = (ObjectField<?, ?>) updateField(
                            field, rootStruct);
                    checkDuplicate(fieldMap, updated);
                    fieldMap.put(updated.name(), updated);
                    tmpToString += updated + ",\n";
                    tempObjectFields[count++] = updated;
                    storageFields.add(updated);
                    localIndexMap.put(updated.localIndex(), updated);
                }
            }
        }

        final FieldGroups newGroup = new FieldGroups(tempFields,
                tempGlobalFields, tempOptionalFields, tempVirtualFields,
                tempObjectFields);

        final List<Struct> childrenList = new ArrayList<>();
        final List<Struct> optionalChildrenList = new ArrayList<>();
        final List<Struct> listChildrenList = new ArrayList<>();

        if (theChildren.children().length > 0) {
            for (final Struct s : theChildren.children()) {
                if (s != null) {
                    final Struct newChild = newChild(s, rootStruct, this);
                    checkArgument(!childMap.containsKey(newChild.name()),
                            "Duplicate Child name: " + newChild.name()
                                    + " found in Struct: " + name());
                    childMap.put(newChild.name(), newChild);
                    childrenList.add(newChild);
                    tmpToString += newChild + " [localIndx: "
                            + newChild.localIndex() + "] ,\n";
                    storageFields.addAll(newChild.allStorageFields());
                    tempNativeFieldCounts += newChild.nativeFieldCounts;
                    localIndexMap.put(newChild.localIndex(), newChild);
                }
            }
        }
        if (theChildren.optionalChildren().length > 0) {
            for (final Struct s : theChildren.optionalChildren()) {
                if (s != null) {
                    final Struct newChild = newChild(s, null, null);
                    checkState(!childMap.containsKey(newChild.name()),
                            "Duplicate Child name: " + newChild.name()
                                    + " found in Struct: " + name());
                    childMap.put(newChild.name(), newChild);
                    optionalChildrenList.add(newChild);
                    tmpToString += newChild + " [localIndx: "
                            + newChild.localIndex() + "] ,\n";
                    localIndexMap.put(newChild.localIndex(), newChild);
                }
            }
        }
        if (theChildren.listChildren().length > 0) {
            for (final Struct s : theChildren.listChildren()) {
                if (s != null) {
                    final Struct newChild = newChild(s, null, null);
                    checkState(!childMap.containsKey(newChild.name()),
                            "Duplicate Child name: " + newChild.name()
                                    + " found in Struct: " + name());
                    childMap.put(newChild.name(), newChild);
                    listChildrenList.add(newChild);
                    tmpToString += newChild + " [localIndx: "
                            + newChild.localIndex() + "] ,\n";
                    localIndexMap.put(newChild.localIndex(), newChild);
                }
            }
        }

        children = childrenArray(childrenList);
        optionalChildren = childrenArray(optionalChildrenList);
        listChildren = childrenArray(listChildrenList);
        fields = newGroup.fields();
        optionalFields = newGroup.optionalFields();
        virtualFields = newGroup.virtualFields();
        globalFields = newGroup.globalFields();
        objectFields = newGroup.objectFields();
        allStorageFields = Collections.unmodifiableList(storageFields);

        if (theStructProperties.compiled()) {
            unionDiscriminatorMap = new HashMap<>();
            populateDiscriminatorMap(storageFields, unionDiscriminatorMap);
            for (final Struct oChild : optionalChildren) {
                unionDiscriminatorMap.putAll(oChild.unionDiscriminatorMap);
            }

        } else {
            unionDiscriminatorMap = null;
        }
        toString = tmpToString + "} : " + theChildProperties.bits();

        if (newGroup.globalFields().length > 0) {
            globalStruct = globalStruct(name(), structGlobalFields(), children,
                    theStructProperties.compiled());
            final Struct gStruct = globalStruct;
            fieldMap.putAll(gStruct.nameFieldMap);
        } else {
            globalStruct = null;
        }

        nameChildMap = unmodifiableMap(childMap);
        nameFieldMap = unmodifiableMap(fieldMap);
        qualifiedNameFieldMap = new HashMap<>();

        for (final Field<?, ?> field : nameFieldMap.values()) {
            qualifiedNameFieldMap.put(field.qualifiedName(), field);
        }
        for (final Struct child : nameChildMap.values()) {
            qualifiedNameFieldMap.putAll(child.qualifiedNameFieldMap);
        }

        nativeFieldCounts = tempNativeFieldCounts;
        optionalFieldsCount = theChildren.optionalFieldsCount();
        objectCount = theChildren.objectFieldsCount();
        fieldCount = theChildren.totalFieldsCount();
        schema = new StructSchema(this);

    }

    /** Private constructor - copy method calls this constructor */
    private Struct(final ChildProperties theChildProperties,
            final StructProperties theStructProperties,
            final ChildrenGroups theChildrenGroups,
            @Nullable final StructInfo theStructInfo,
            final boolean theReIndexFlag) {
        this(theChildProperties, theStructProperties, combineAndIndexChildren(
                theChildrenGroups, theReIndexFlag), theStructInfo);
    }

    /**
     * Instantiates a new struct object.
     *
     * @param theName the Struct name
     * @param isUnion the is a union or a struct ?, true if union.
     * @param theChildren an array of children struct objects.
     * @param theFields an array of field objects.
     */
    public Struct(final String theName, final boolean isUnion,
            @Nullable final Struct[] theChildren,
            @Nullable final Field<?, ?>... theFields) {

        this(new ChildProperties(theName, -1), new StructProperties(false,
                isUnion, false, false), new ChildrenGroups(new FieldGroups(
                theFields), theChildren), (StructInfo) null, true);
    }

    /**
     * Instantiates a new struct object.
     *
     * @param theName the Struct name
     * @param isUnion the is a union or a struct ?, true if union.
     * @param theStructInfo the struct additional information, this
     * must be 'null' while creating children struct objects.
     * @param theChildren an array of children struct objects.
     * @param theFields an array of field objects.
     */
    public Struct(final String theName, final boolean isUnion,
            @Nullable final StructInfo theStructInfo,
            @Nullable final Struct[] theChildren,
            @Nullable final Field<?, ?>... theFields) {

        this(new ChildProperties(theName, -1), new StructProperties(false,
                isUnion, false, false), new ChildrenGroups(new FieldGroups(
                theFields), theChildren), theStructInfo, true);
    }

    /**
     * Instantiates a new Struct object.
     *
     * @param theName the name
     * @param theChildren the array of children structs
     * @param theFields the array of fields.
     */
    public Struct(final String theName, @Nullable final Struct[] theChildren,
            @Nullable final Field<?, ?>... theFields) {
        this(theName, false, theChildren, theFields);
    }

    /** Check if the field with same name already exists in theFieldMap,
     * If the field already exists, this method throws IllegalArgument exception. */
    private void checkDuplicate(final Map<String, Field<?, ?>> theFieldMap,
            final Field<?, ?> theField) {

        if (theFieldMap.containsKey(theField.name())) {
            throw new IllegalArgumentException(
                    "Duplicate Field Name found! field: " + theField.name()
                            + " Struct: " + name());
        }
    }

    /** Creates a new copy of this structure, internal method */
    private Struct newChild(final Struct theChild,
            @Nullable final Struct theRootStruct,
            @Nullable final Struct theParent) {

        ChildProperties childProps = new ChildProperties(theChild).setParent(
                theParent).setRoot(theRootStruct);
        childProps = childProps.setQualifiedName(qualifiedName(this) + "."
                + theChild.name());
        return theChild.copy(childProps, false);
    }

    /** Sets the storage key.
     * @param theStorageKey the storage key
     * @return the e */
    private Struct setStorageKey(final StorageKey theStorageKey) {
        return copy(childProperties().setStorageKey(theStorageKey), false);
    }

    /** Storage count.
     * @return the int */
    private int storageCount() {
        int result = listChildren.length + optionalChildren.length;
        for (final Struct child : children) {
            result += child.storageCount();
        }
        return result;
    }

    /** Updates field parent, root, and qName and puts in map. */
    private Field<?, ?> updateField(final Field<?, ?> theField,
            final Struct theRootStruct) {
        ChildProperties props = theField.childProperties().setParent(this)
                .setRoot(theRootStruct)
                .setQualifiedName(qualifiedName(this) + "." + theField.name());
        if (isOptional() || structProperties().isList()) {
            props = props.setStorageKey(storageKey());
        }
        return theField.copy(props);
    }

    /** All list children of this struct. */
    public Struct[] allListChildren() {
        final List<Struct> listChildrenList = new ArrayList<>();
        if (listChildren.length > 0) {
            listChildrenList.addAll(Arrays.asList(listChildren));
        }
        if (children.length > 0) {
            for (final Struct child : children) {
                listChildrenList.addAll(Arrays.asList(child.allListChildren()));
            }
        }
        return childrenArray(listChildrenList);
    }

    /** All optional children including that of children struct */
    public Struct[] allOptionalChildren() {
        final List<Struct> optionalChildrenList = new ArrayList<>();
        if (optionalChildren.length > 0) {
            optionalChildrenList.addAll(Arrays.asList(optionalChildren));
        }
        if (children.length > 0) {
            for (final Struct child : children) {
                optionalChildrenList.addAll(Arrays.asList(child
                        .allOptionalChildren()));
            }
        }
        return childrenArray(optionalChildrenList);
    }

    /** Returns all the real fields of this struct, does not include virtual fields,
     * includes global fields, also includes fields of all the children structs
     * (excluding optional children and list children) */
    @SuppressWarnings("null")
    public List<Field<?, ?>> allStorageFields() {
        return allStorageFields;
    }

    /** Returns child corresponding to a particular localIndex, Throws an Exception if not found */
    @SuppressWarnings("null")
    public Child child(final int theLocalIndex) {
        checkState(localIndexMap.containsKey(theLocalIndex),
                "Not found child with local index " + theLocalIndex);
        return localIndexMap.lget();
    }

    /** Searches for a child Struct by its name. Throws an Exception if not found. */
    @SuppressWarnings({ "null", "unchecked" })
    public final Struct child(final String theName) {
        if (globalStruct != null && theName.equals(globalStruct.name())) {
            return globalStruct;
        }

        final int dot = theName.indexOf('.');
        if (dot > 0) {
            final String childName = theName.substring(0, dot);
            final String rest = theName.substring(dot + 1);
            if (childName == null || childName.isEmpty() || rest == null
                    || rest.isEmpty()) {
                throw new IllegalStateException("Invalid qName passed: "
                        + childName);
            }
            if (globalStruct != null && childName.equals(globalStruct.name())) {
                return globalStruct;
            }
            final Struct base = name().equals(childName) ? this
                    : (Struct) child(childName);
            return base.child(rest);
        }
        Struct child = nameChildMap.get(theName);
        if (child == null) {
            final Struct unionChild = nameChildMap.get(PARENT_PREFIX + theName);
            if (unionChild != null) {
                child = unionChild.children[0];
            }
        }
        checkState(child != null, "Not found: " + theName);
        return child;
    }

    /** Searches a Child by name and returns its local index, this index is used as a Union discriminator. */
    public int childIndex(final String theName) {
        final Child c = child(theName);
        return c.localIndex();
    }

    /** Returns the direct children of this Struct (without including that of children).
    *
    * @return the array of struct children. */
    public Struct[] children() {
        final List<Struct> allChildren = new ArrayList<>();
        allChildren.addAll(Arrays.asList(children));
        allChildren.addAll(Arrays.asList(optionalChildren));
        allChildren.addAll(Arrays.asList(listChildren));
        return childrenArray(allChildren);
    }

    /** This is an internal method that compiles detached children (Optional and List children)
     * separately using a compiler instance passed to it, and returns the modified struct object.
     * This method is used internally while compiling to create a fully compiled struct schema. */
    @SuppressWarnings("unchecked")
    public final Struct compileDetachedChildren(final Compiler theCompiler,
            @Nullable final StorageKey theStorageKey) {

        Struct[] tempOptionalChildren;
        Struct[] tempListChildren;
        Struct[] tempChildren;
        int tempStorageKey = theStorageKey == null ? 0 : theStorageKey.key + 1;

        if (listChildren.length > 0) {
            tempListChildren = new Struct[listChildren.length];
            for (int i = 0; i < listChildren.length; i++) {
                @SuppressWarnings("rawtypes")
                final StorageKey key = new StorageKey(++tempStorageKey,
                        theStorageKey);
                final Struct chld = listChildren[i].setStorageKey(key);
                tempListChildren[i] = theCompiler.compile(chld);
                tempStorageKey += tempListChildren[i].storageCount();
            }
        } else {
            tempListChildren = EMPTY_CHILDREN;
        }

        if (optionalChildren.length > 0) {
            tempOptionalChildren = new Struct[optionalChildren.length];
            for (int i = 0; i < optionalChildren.length; i++) {
                final StorageKey key = new StorageKey(++tempStorageKey,
                        theStorageKey);
                @SuppressWarnings("rawtypes")
                final Struct optionalChild = optionalChildren[i]
                        .setStorageKey(key);
                tempOptionalChildren[i] = theCompiler.compile(optionalChild);
                tempStorageKey += tempOptionalChildren[i].storageCount();
            }
        } else {
            tempOptionalChildren = EMPTY_CHILDREN;
        }
        if (children.length > 0) {
            tempChildren = new Struct[children.length];
            for (int i = 0; i < children.length; i++) {
                tempChildren[i] = children[i].compileDetachedChildren(
                        theCompiler, theStorageKey);
                tempStorageKey += tempChildren[i].storageCount();
            }
        } else {
            tempChildren = EMPTY_CHILDREN;
        }
        return new Struct(childProperties(), structProperties(),
                new ChildrenGroups(fieldGroups(), tempChildren,
                        tempOptionalChildren, tempListChildren),
                (StructInfo) null, false);
    }

    /** Creates a new copy of this structure, with the given children and fields. */
    public final Struct copy(final ChildProperties theChildProperties,
            final boolean theReIndexFlag) {
        return copy(theChildProperties, structProperties(), structChildren(),
                fieldGroups(), theReIndexFlag);
    }

    /** Creates a new copy of this structure, with the given children and fields. */
    public final Struct copy(final ChildProperties theChildProperties,
            final StructProperties theStructProperties,
            final Struct[] theChildren, final Field<?, ?>[] theFields) {

        return copy(theChildProperties, theStructProperties, theChildren,
                new FieldGroups(theFields, globalStruct, optionalFields,
                        virtualFields, objectFields), true);
    }

    /** Creates a new copy of this structure, with the given children
     * and fields and new set of properties. */
    public final Struct copy(final ChildProperties theChildProperties,
            final StructProperties theStructProperties,
            final Struct[] theChildren, final FieldGroups theFieldGroups,
            final boolean theReIndexFlag) {
        return copy(theChildProperties, theStructProperties, theChildren,
                theFieldGroups, structInfo, theReIndexFlag);
    }

    /** Creates a new copy of this structure, with the given children and fields
     * and new set of properties. */
    public final Struct copy(final ChildProperties theChildProperties,
            final StructProperties theStructProperties,
            final Struct[] theChildren, final FieldGroups theFieldGroups,
            @Nullable final StructInfo theStructInfo,
            final boolean theReIndexFlag) {
        final ChildrenGroups childrenGroups = new ChildrenGroups(
                theFieldGroups, theChildren, optionalChildren, listChildren);
        return new Struct(theChildProperties, theStructProperties,
                childrenGroups, theStructInfo, theReIndexFlag);
    }

    /** Creates a new copy of this structure, with a new set of children and fields and properties.
     *  Since Struct class is immutable one can use 'copy' method to get a modified copy using
     *  this method. */
    public final Struct copy(final String theName, final int theBits,
            final boolean isUnion, final Struct[] theChildren,
            final Field<?, ?>[] theFields, final boolean isCompiled,
            @Nullable final StructInfo theStructInfo) {

        return copy(childProperties().setName(theName).setBits(theBits),
                new StructProperties(metaDataExtracted(), isUnion,
                        structProperties().isList(), isCompiled), theChildren,
                new FieldGroups(theFields, globalStruct, optionalFields,
                        virtualFields, objectFields), theStructInfo, true);
    }

    /** Creates a new copy of this structure using new set of properties,
     * no field-re-indexing. */
    public Struct copy(final StructProperties theStructProps) {
        return copy(childProperties(), theStructProps, structChildren(),
                fieldGroups(), false);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public boolean equals(final Object theOther) {
        if (theOther instanceof Struct) {
            final StructSchema otherSchema = ((Struct) theOther).schema;
            if (otherSchema != null && schema != null) {
                return otherSchema.fullHash() == schema.fullHash();
            }
        }
        return false;
    }

    /** Searches for a field. Throws an Exception if not found. */
    @SuppressWarnings({ "unchecked", "null", "rawtypes" })
    public final <F> F field(final String theName) {
        final int dot = theName.indexOf('.');
        if (dot > 0) {
            final String childName = theName.substring(0, dot);
            final String rest = theName.substring(dot + 1);
            if (childName == null || childName.isEmpty() || rest == null
                    || rest.isEmpty()) {
                throw new IllegalStateException(
                        "Invalid field qualified name passed: " + theName);
            }
            final Struct child = name().equals(childName) ? this
                    : (Struct) child(childName);
            return child.field(rest);
        }
        if (nameFieldMap.containsKey(theName)) {
            return (F) nameFieldMap.get(theName);
        }
        if (name().startsWith(PARENT_PREFIX)) {
            final F field = (F) children[0].nameFieldMap.get(theName);
            if (field != null) {
                return field;
            }
        }
        if (globalStruct != null
                && globalStruct.nameFieldMap.containsKey(theName)) {
            return (F) globalStruct.nameFieldMap.get(theName);
        }
        throw new IllegalStateException("Not found: " + theName);
    }

    /** The total fields count includes that for
     * children, optional children and list children. */
    public int fieldCount() {
        return fieldCount;
    }

    /** Fields getter. */
    public FieldGroups fieldGroups() {
        return new FieldGroups(this);
    }

    /** Fields getter */
    public Field<?, ?>[] fields() {
        return FieldGroups.allFields(this);
    }

    /** The Struct signature. */
    @SuppressWarnings("null")
    public long getSignature() {
        return schema.signature();
    }

    /**
     * @return the globalStruct
     */
    @Nullable
    public Struct globalStruct() {
        return globalStruct;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final long fullHash = schema.fullHash();
        return (int) (fullHash ^ fullHash >>> 32);
    }

    /**
     * Checks if schema of this Struct object is compatible with the other Struct object.
     * The match is performed without considering attributes like field default values,
     * field size in bits, optional, virtual, constant flags etc. The match is generated
     * using field names types only.
     */
    public boolean isCompatible(final Struct theOtherStruct) {
        checkNotNull(theOtherStruct);
        return theOtherStruct.signature() == signature();
    }

    /** Is it a list-child struct ? */
    public final boolean list() {
        return structProperties.isList();
    }

    /**
     * The meta data extracted flag; property used by the compiler to indicate an
     * intermediate state.
     */
    public boolean metaDataExtracted() {
        return structProperties.isMetaDataExtracted();
    }

    /** The count of all the 'normal' (excluding global, object, virtual fields etc.)
     * fields including that of children struct. */
    public int nativeFieldCounts() {
        return nativeFieldCounts;
    }

    /** The object fields count. */
    public int objectCount() {
        return objectCount;
    }

    /** Direct optional children of this struct. */
    @SuppressWarnings("null")
    public Struct[] optionalChildren() {
        return optionalChildren.clone();
    }

    /** The optional fields count. */
    public int optionalFieldsCount() {
        return optionalFieldsCount;
    }

    /**
     * Schema string for in a particular format, xml or json formats.
     *
     * @param theFormat the format
     */
    @SuppressWarnings("null")
    public String schemaString(final SchemaFormat theFormat) {
        final JSONParser builder = new JSONParser(new StructSchema(this));
        if (theFormat == SchemaFormat.JSON) {
            return builder.toJSONString();
        }
        return builder.toXMLString();
    }

    /** Searches a Field, field name is passed that is returned,
     * all the fields of a child are returned if child name is passed  */
    public Field<?, ?>[] searchFields(final String theName) {

        final Map<String, Field<?, ?>> fieldMap = name().startsWith(
                PARENT_PREFIX) ? children[0].nameFieldMap : nameFieldMap;
        if (fieldMap.containsKey(theName)) {
            return new Field<?, ?>[] { fieldMap.get(theName) };
        }
        final Struct gStruct = globalStruct;
        if (gStruct != null && gStruct.nameFieldMap.containsKey(theName)) {
            return new Field<?, ?>[] { gStruct.nameFieldMap.get(theName) };
        }
        final Map<String, Struct> childrenMap = name()
                .startsWith(PARENT_PREFIX) ? children[0].nameChildMap
                : nameChildMap;
        final Struct child = childrenMap.get(theName);
        if (child != null) {
            return child.structFields();
        }
        throw new IllegalStateException("Not found: " + theName);

    }

    /** Sets 'is list' property for this struct to store a variable size
     * collection of values. Storage space is allocated separately for this type
     * of children on-demand basis.
     *
     * @param isList the 'is list' property
     * @return the modified struct */
    public final Struct setList(final boolean isList) {
        return copy(structProperties.setList(isList));
    }

    /** Sets the optional-child flag to indicate that when this Struct is added
     * to a parent struct, it will be an optional child, i.e. fields in this
     * child will not have allocated space by default. Space will be allocated
     * in a separate storage on-demand basis.
     *
     * @param isOptional the is optional
     * @return the modified struct */
    public final Struct setOptional(final boolean isOptional) {
        return copy(childProperties().setOptional(isOptional), false);
    }

    /**
     * Signature is a system generated value using fields and children of this Struct objects. The
     * signature generation algorithm does not consider attributes like field default values,
     * field size in bits, optional, virtual, constant flags etc. The Signature is generated using
     * field names types only.
     */
    public long signature() {
        return schema.signature();
    }

    /** Array of Struct children. */
    @SuppressWarnings("null")
    public Struct[] structChildren() {
        return children;
    }

    /** Array of Struct fields (only normal fields; does not
     * include virtual, global, optional etc.). */
    @SuppressWarnings("null")
    public Field<?, ?>[] structFields() {
        return fields;
    }

    /** Array of Global fields. */
    @SuppressWarnings("null")
    public Field<?, ?>[] structGlobalFields() {
        return globalFields;
    }

    /** Struct info object containing meta-data fields like
     * created-by, created-on etc. returns 'null' if the value is not set.*/
    @Nullable
    public StructInfo structInfo() {
        return structInfo;
    }

    /** Array of Object fields. */
    @SuppressWarnings("null")
    public ObjectField<?, ?>[] structObjectFields() {
        return objectFields;
    }

    /** Array of Optional fields. */
    @SuppressWarnings("null")
    public Field<?, ?>[] structOptionalFields() {
        return optionalFields;
    }

    /** An object that wraps Properties of a this Struct object. */
    @SuppressWarnings("null")
    public StructProperties structProperties() {
        return structProperties;
    }

    /** Array of virtual fields. */
    @SuppressWarnings("null")
    public Field<?, ?>[] structVirtualFields() {
        return virtualFields;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public final String toString() {
        return toString;
    }

    /** Union discriminator field mapping for a field,
     * An internal structure used to validate if the field index are
     * set before modifying a field in a union. */
    @Nullable
    public UnionDiscriminatorValueMapping[] uDMapping(final Field<?, ?> theField) {
        return unionDiscriminatorMap.get(theField);
    }

    /** Is it a union ?*/
    public boolean union() {
        return structProperties.union();
    }

}
