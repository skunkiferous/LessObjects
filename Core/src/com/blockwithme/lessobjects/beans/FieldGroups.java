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
/**
 *
 */
package com.blockwithme.lessobjects.beans;

import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_FIELDS;
import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_OBJECTS;
import static com.blockwithme.lessobjects.util.Util.fieldArray;
import static com.blockwithme.lessobjects.util.Util.objectArray;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.ObjectField;

/**
 * FieldGroups class separates fields based on field types.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class FieldGroups {

    /** The struct storage fields. */
    @Nonnull
    private final Field<?, ?>[] fields;

    /** The global fields. */
    @Nonnull
    private final Field<?, ?>[] globalFields;

    /** The object fields. */
    @Nonnull
    private final ObjectField<?, ?>[] objectFields;

    /** The optional fields. */
    @Nonnull
    private final Field<?, ?>[] optionalFields;

    /** The virtual fields. */
    @Nonnull
    private final Field<?, ?>[] virtualFields;

    /** Checks if a field exists in the list of fields, this method uses field
     * name for the comparison */
    private static boolean checkIfExists(final List<Field<?, ?>> theList,
            final Field<?, ?> theField) {
        for (final Field<?, ?> f : theList) {
            if (f.name().equals(theField.name())) {
                return true;
            }
        }
        return false;
    }

    /** @param theGlobalStruct */
    private static Field<?, ?>[] globalFields(
            @Nullable final Struct theGlobalStruct) {
        return theGlobalStruct == null ? new Field<?, ?>[0] : theGlobalStruct
                .fields();
    }

    /** Returns all fields as an array of fields, this method also returns
     * meta data fields added by the struct-compiler */
    public static Field<?, ?>[] allFields(final Struct theStruct) {
        final int totalCount = theStruct.structFields().length
                + theStruct.structOptionalFields().length
                + theStruct.structGlobalFields().length
                + theStruct.structVirtualFields().length
                + theStruct.structObjectFields().length;

        final Field<?, ?>[] allFields = new Field<?, ?>[totalCount];

        int count = theStruct.structFields().length;
        int startIndex = 0;
        if (count > 0) {
            System.arraycopy(theStruct.structFields(), 0, allFields,
                    startIndex, count);
        }
        startIndex += count;
        count = theStruct.structOptionalFields().length;
        if (count > 0) {
            System.arraycopy(theStruct.structOptionalFields(), 0, allFields,
                    startIndex, count);
        }
        startIndex += count;
        count = theStruct.structGlobalFields().length;
        if (count > 0) {
            System.arraycopy(theStruct.structGlobalFields(), 0, allFields,
                    startIndex, count);
        }
        startIndex += count;
        count = theStruct.structVirtualFields().length;
        if (count > 0) {
            System.arraycopy(theStruct.structVirtualFields(), 0, allFields,
                    startIndex, count);
        }
        startIndex += count;
        count = theStruct.structObjectFields().length;
        if (count > 0) {
            System.arraycopy(theStruct.structObjectFields(), 0, allFields,
                    startIndex, count);
        }
        return allFields;
    }

    /** Returns an array of global fields without including the children global
     * fields. */
    @SuppressWarnings("null")
    public static Field<?, ?>[] globalFields(
            @Nullable final Field<?, ?>[] theFields) {

        if (theFields == null || theFields.length == 0) {
            return EMPTY_FIELDS;
        }

        final List<Field<?, ?>> resultList = new ArrayList<>();
        for (final Field<?, ?> f : theFields) {
            if (f.global() && !checkIfExists(resultList, f)) {
                resultList.add(f);
            }
        }
        return fieldArray(resultList);
    }

    /** Returns all the fields that are not global. */
    @SuppressWarnings("null")
    public static Field<?, ?>[] normalOnly(
            @Nullable final Field<?, ?>[] theFields,
            final boolean isGlobalIncluded) {

        if (theFields == null || theFields.length == 0) {
            return EMPTY_FIELDS;
        }
        final List<Field<?, ?>> resultList = new ArrayList<>();
        for (final Field<?, ?> f : theFields) {
            if (f.storageField()) {
                if (isGlobalIncluded || !f.global()) {
                    resultList.add(f);
                }
            }
        }
        return fieldArray(resultList);
    }

    @SuppressWarnings("null")
    public static ObjectField<?, ?>[] objectOnly(
            @Nullable final Field<?, ?>[] theFields,
            final boolean isGlobalIncluded) {

        if (theFields == null || theFields.length == 0) {
            return EMPTY_OBJECTS;
        }
        final List<ObjectField<?, ?>> resultList = new ArrayList<>();
        for (final Field<?, ?> f : theFields) {
            if (f.object()) {
                if (isGlobalIncluded || !f.global()) {
                    resultList.add((ObjectField<?, ?>) f);
                }
            }
        }
        return objectArray(resultList);
    }

    /** Returns all the fields that are optional. */
    @SuppressWarnings("null")
    public static Field<?, ?>[] optionalOnly(
            @Nullable final Field<?, ?>[] theFields) {

        if (theFields == null || theFields.length == 0) {
            return EMPTY_FIELDS;
        }
        final List<Field<?, ?>> resultList = new ArrayList<>();
        for (final Field<?, ?> f : theFields) {
            if (f.isOptional() && !f.object()) {
                resultList.add(f);
            }
        }
        return fieldArray(resultList);
    }

    /** Returns all the fields that are optional. */
    @SuppressWarnings("null")
    public static Field<?, ?>[] virtualOnly(
            @Nullable final Field<?, ?>[] theFields) {

        if (theFields == null || theFields.length == 0) {
            return EMPTY_FIELDS;
        }
        final List<Field<?, ?>> resultList = new ArrayList<>();
        for (final Field<?, ?> f : theFields) {
            if (f.virtual()) {
                resultList.add(f);
            }
        }
        return fieldArray(resultList);
    }

    /** Instantiates a new field groups.
     *
     * @param theAllFields the all fields
     */
    public FieldGroups(@Nullable final Field<?, ?>[] theAllFields) {
        this(normalOnly(theAllFields, false), globalFields(theAllFields),
                optionalOnly(theAllFields), virtualOnly(theAllFields),
                objectOnly(theAllFields, false));
    }

    /** Instantiates a new field groups.
     *
     * @param theFields the normal storage fields
     * @param theGlobalFields the global fields
     * @param theOptionalFields the optional fields
     * @param theVirtualFields the virtual fields
     * @param theObjectFields the object fields
     */
    public FieldGroups(@Nullable final Field<?, ?>[] theFields,
            @Nullable final Field<?, ?>[] theGlobalFields,
            @Nullable final Field<?, ?>[] theOptionalFields,
            @Nullable final Field<?, ?>[] theVirtualFields,
            @Nullable final ObjectField<?, ?>[] theObjectFields) {

        fields = theFields == null ? EMPTY_FIELDS : theFields;
        optionalFields = theOptionalFields == null ? EMPTY_FIELDS
                : theOptionalFields;
        globalFields = theGlobalFields == null ? EMPTY_FIELDS : theGlobalFields;
        virtualFields = theVirtualFields == null ? EMPTY_FIELDS
                : theVirtualFields;
        objectFields = theObjectFields == null ? EMPTY_OBJECTS
                : theObjectFields;

    }

    /** Instantiates a new field groups.
     *
     * @param theFields the fields
     * @param theGlobalStruct the global struct
     * @param theOptionalFields the optional fields
     * @param theVirtualFields the virtual fields
     * @param theObjectFields the object fields
     */
    public FieldGroups(@Nullable final Field<?, ?>[] theFields,
            @Nullable final Struct theGlobalStruct,
            @Nullable final Field<?, ?>[] theOptionalFields,
            @Nullable final Field<?, ?>[] theVirtualFields,
            @Nullable final ObjectField<?, ?>[] theObjectFields) {

        this(theFields, globalFields(theGlobalStruct), theOptionalFields,
                theVirtualFields, theObjectFields);
    }

    /** Instantiates a new grouped fields.
     *
     * @param theStruct the struct */
    public FieldGroups(final Struct theStruct) {
        this(theStruct.structFields(), theStruct.structGlobalFields(),
                theStruct.structOptionalFields(), theStruct
                        .structVirtualFields(), theStruct.structObjectFields());
    }

    /** Gets the all fields in a 2 dimensional array, modifications to the array will modify this object.
     *
     * @return the a 2 dimensional array of fields, optionalFields,
                constantFields, globalFields, virtualFields and objectFields in this same order */
    public Field<?, ?>[][] allFields() {
        final Field<?, ?>[][] allFields = { fields, optionalFields,
                globalFields, virtualFields, objectFields() };
        return allFields;
    }

    /**
     * @return the fields, empty array if there are no fields
     */
    @SuppressWarnings("null")
    public Field<?, ?>[] fields() {
        return fields;
    }

    /**
     * @return the globalFields, empty array if there are no global fields
     */
    @SuppressWarnings("null")
    public Field<?, ?>[] globalFields() {
        return globalFields;
    }

    /**
     * @return the objectFields, empty array if there are no object fields
     */
    @SuppressWarnings("null")
    public ObjectField<?, ?>[] objectFields() {
        return objectFields;
    }

    /**
     * @return the optionalFields, empty array if there are no optional fields
     */
    @SuppressWarnings("null")
    public Field<?, ?>[] optionalFields() {
        return optionalFields;
    }

    /**
     * @return the virtualFields, empty array if there are no virtual fields
     */
    @SuppressWarnings("null")
    public Field<?, ?>[] virtualFields() {
        return virtualFields;
    }
}
