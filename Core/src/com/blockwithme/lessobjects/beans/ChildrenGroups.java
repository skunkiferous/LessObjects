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
package com.blockwithme.lessobjects.beans;

import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_CHILDREN;
import static com.blockwithme.lessobjects.util.StructConstants.EMPTY_FIELDS;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct;

/**
 * This class segregates different types of children and encapsulates them,
 * also encapsulates a group of fields
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChildrenGroups {

    /** The children. */
    @Nonnull
    private final Struct[] children;

    /** The field groups. */
    @Nonnull
    private FieldGroups fieldGroups;

    /** The list children. */
    @Nonnull
    private final Struct[] listChildren;

    /** The object fields count. */
    private final int objectFieldsCount;

    /** The optional children. */
    @Nonnull
    private final Struct[] optionalChildren;

    /** The optional fields count. */
    private final int optionalFieldsCount;

    /** The total fields count includes that for
     * children, optional children and list children. */
    private final int totalFieldsCount;

    private static int countAll(@Nullable final FieldGroups theFieldGroups,
            @Nullable final Struct[] theChildren,
            @Nullable final Struct[] theOptionalChildren,
            @Nullable final Struct[] theListChildren) {

        int result = 0;
        if (theFieldGroups != null) {
            result += theFieldGroups.fields().length;
            result += theFieldGroups.globalFields().length;
            result += theFieldGroups.objectFields().length;
            result += theFieldGroups.optionalFields().length;
        }
        if (theChildren != null) {
            for (final Struct child : theChildren) {
                result += child.fieldCount();
            }
        }
        if (theOptionalChildren != null) {
            for (final Struct child : theOptionalChildren) {
                result += child.fieldCount();
            }
        }
        if (theListChildren != null) {
            for (final Struct child : theListChildren) {
                result += child.fieldCount();
            }
        }
        return result;
    }

    private static int countObjectFields(
            @Nullable final FieldGroups theFieldGroups,
            @Nullable final Struct[] theChildren) {

        int result = 0;
        if (theFieldGroups != null) {
            result = theFieldGroups.objectFields().length;
        }
        if (theChildren != null) {
            for (final Struct child : theChildren) {
                result += child.objectCount();
            }
        }
        return result;
    }

    private static int countOptionalFields(
            @Nullable final FieldGroups theFieldGroups,
            @Nullable final Struct[] theChildren) {

        int result = 0;
        if (theFieldGroups != null) {
            result = theFieldGroups.optionalFields().length;
        }
        if (theChildren != null) {
            for (final Struct child : theChildren) {
                result += child.optionalFieldsCount();
            }
        }
        return result;
    }

    /** The main constructor that segregates different type of children.*/
    public ChildrenGroups(@Nullable final FieldGroups theFieldGroups,
            @Nullable final Struct[] theChildren) {

        fieldGroups = theFieldGroups == null ? new FieldGroups(EMPTY_FIELDS)
                : theFieldGroups;

        if (theChildren != null) {
            final List<Struct> childrenList = new ArrayList<>();
            final List<Struct> optionalList = new ArrayList<>();
            final List<Struct> lists = new ArrayList<>();
            for (final Struct child : theChildren) {
                if (child.isOptional()) {
                    optionalList.add(child);
                } else if (child.structProperties().isList()) {
                    lists.add(child);
                } else {
                    childrenList.add(child);
                }
            }
            children = childrenList.toArray(new Struct[childrenList.size()]);
            optionalChildren = optionalList.toArray(new Struct[optionalList
                    .size()]);
            listChildren = lists.toArray(new Struct[lists.size()]);

        } else {
            children = EMPTY_CHILDREN;
            optionalChildren = EMPTY_CHILDREN;
            listChildren = EMPTY_CHILDREN;
        }

        objectFieldsCount = countObjectFields(fieldGroups, children);
        optionalFieldsCount = countOptionalFields(fieldGroups, children);
        totalFieldsCount = countAll(fieldGroups, children, optionalChildren,
                listChildren);

    }

    public ChildrenGroups(@Nullable final FieldGroups theFieldGroups,
            @Nullable final Struct[] theChildren,
            @Nullable final Struct[] theOptionalChildren,
            @Nullable final Struct[] theListChildren) {

        fieldGroups = theFieldGroups == null ? new FieldGroups(EMPTY_FIELDS)
                : theFieldGroups;
        children = theChildren == null ? EMPTY_CHILDREN : theChildren;
        optionalChildren = theOptionalChildren == null ? EMPTY_CHILDREN
                : theOptionalChildren;
        listChildren = theListChildren == null ? EMPTY_CHILDREN
                : theListChildren;

        objectFieldsCount = countObjectFields(fieldGroups, children);
        optionalFieldsCount = countOptionalFields(fieldGroups, children);
        totalFieldsCount = countAll(fieldGroups, children, optionalChildren,
                listChildren);
    }

    /**
     * @return the children, empty array if no children
     */
    @SuppressWarnings("null")
    public Struct[] children() {
        return children;
    }

    /**
     * @return the fieldGroups, field group if no fields
     */
    @SuppressWarnings("null")
    public FieldGroups fieldGroups() {
        return fieldGroups;
    }

    /**
     * @param theFieldGroups the fieldGroups to set
     */
    public void fieldGroups(@Nullable final FieldGroups theFieldGroups) {
        fieldGroups = theFieldGroups == null ? new FieldGroups(EMPTY_FIELDS)
                : theFieldGroups;
    }

    /**
     * @return the listChildren, empty array if no list children
     */
    @SuppressWarnings("null")
    public Struct[] listChildren() {
        return listChildren;
    }

    /**
     * @return the objectFieldsCount
     */
    public int objectFieldsCount() {
        return objectFieldsCount;
    }

    /**
     * @return the optionalChildren, empty array if no optional children
     */
    @SuppressWarnings("null")
    public Struct[] optionalChildren() {
        return optionalChildren;
    }

    /**
     * @return the optionalFieldsCount
     */
    public int optionalFieldsCount() {
        return optionalFieldsCount;
    }

    /** The total fields count includes that for
     * children, optional children and list children. */
    public int totalFieldsCount() {
        return totalFieldsCount;
    }

}
