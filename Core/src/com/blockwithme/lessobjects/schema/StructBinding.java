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
package com.blockwithme.lessobjects.schema;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldCategory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.StructInfo;
import com.blockwithme.lessobjects.beans.FieldGroups;
import com.blockwithme.lessobjects.schema.StructSchema.JSONViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * An internal class used at the time of conversion to JSON/XML format.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
@JsonPropertyOrder(alphabetic = true)
public class StructBinding implements Comparable<StructBinding> {

    /** The Constant for field bindings empty array. */
    private static final FieldBinding[] FIELD_BINDINGS_EMPTY_ARRAY = new FieldBinding[0];

    /** The Constant for struct bindings empty array. */
    private static final StructBinding[] STRUCT_BINDINGS_EMPTY_ARRAY = new StructBinding[0];

    /** All the children. */
    @JsonProperty
    @JsonView(JSONViews.SignatureView.class)
    private StructBinding[] allChildren;

    /** The constant fields. */
    @JsonProperty
    @JsonView(JSONViews.SignatureView.class)
    private FieldBinding[] allFields;

    /** The children. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private StructBinding[] children;

    /** The normal fields. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private FieldBinding[] fields;

    /** The global fields. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private FieldBinding[] globalFields;

    /** The list children. */
    @JsonProperty
    @JsonView({ JSONViews.SignatureView.class, JSONViews.CompleteView.class })
    private StructBinding[] listChildren;

    /** The name. */
    @JsonProperty
    @JsonView({ JSONViews.SignatureView.class, JSONViews.CompleteView.class })
    private String name;

    /** The object fields. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private FieldBinding[] objectFields;

    /** The optional children. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private StructBinding[] optionalChildren;

    /** The optional fields. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private FieldBinding[] optionalFields;

    /** The union flag. */
    @JsonProperty
    @JsonView({ JSONViews.SignatureView.class, JSONViews.CompleteView.class })
    private boolean union;

    /** The virtual fields. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private FieldBinding[] virtualFields;

    /** The optional flag */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    boolean optional;

    /** Converts a Struct Child array into StructBinding array */
    @SuppressWarnings("null")
    private static StructBinding[] convertChildren(
            @Nullable final Struct[] theChildren) {
        final StructBinding[] sbArray;
        if (theChildren != null && theChildren.length > 0) {
            Arrays.sort(theChildren);
            sbArray = new StructBinding[theChildren.length];
            for (int i = 0; i < theChildren.length; i++) {
                sbArray[i] = new StructBinding(theChildren[i]);
            }
        } else {
            sbArray = STRUCT_BINDINGS_EMPTY_ARRAY;
        }
        return sbArray;
    }

    /** Converts a Field array into FieldBindings array */
    @SuppressWarnings("null")
    private static FieldBinding[] convertFields(
            @Nullable final Field<?, ?>[] theFields) {
        FieldBinding[] fbs;
        if (theFields != null && theFields.length > 0) {
            Arrays.sort(theFields);
            fbs = new FieldBinding[theFields.length];
            for (int i = 0; i < theFields.length; i++) {
                fbs[i] = new FieldBinding(theFields[i]);
            }
        } else {
            fbs = FIELD_BINDINGS_EMPTY_ARRAY;
        }
        return fbs;
    }

    /** Creates Actual Field class from FieldBinding */
    @SuppressWarnings("null")
    private static void createFields(final FieldBinding[] theArray,
            final FieldCategory theCategory,
            final List<Field<?, ?>> theFieldList) {
        if (theArray == null || theArray.length == 0) {
            return;
        }
        for (final FieldBinding binding : theArray) {
            theFieldList.add(binding.create(theCategory));
        }
    }

    /** Instantiates a new struct binding. */
    public StructBinding() {
        // NOP
    }

    /** Instantiates a new struct binding. */
    public StructBinding(final Struct theStruct) {
        name = theStruct.name();
        union = theStruct.union();
        final List<FieldBinding> allFieldList = new ArrayList<>();

        final FieldGroups fieldGroups = theStruct.fieldGroups();
        fields = convertFields(fieldGroups.fields());
        allFieldList.addAll(asList(fields));
        globalFields = convertFields(fieldGroups.globalFields());
        allFieldList.addAll(asList(globalFields));
        objectFields = convertFields(fieldGroups.objectFields());
        allFieldList.addAll(asList(objectFields));
        optionalFields = convertFields(fieldGroups.optionalFields());
        allFieldList.addAll(asList(optionalFields));
        virtualFields = convertFields(fieldGroups.virtualFields());
        allFieldList.addAll(asList(virtualFields));

        allFields = allFieldList.toArray(new FieldBinding[allFieldList.size()]);
        Arrays.sort(allFields);

        final List<StructBinding> allChildrenList = new ArrayList<>();
        children = convertChildren(theStruct.structChildren());
        allChildrenList.addAll(Arrays.asList(children));
        optionalChildren = convertChildren(theStruct.optionalChildren());
        allChildrenList.addAll(Arrays.asList(optionalChildren));
        allChildren = allChildrenList.toArray(new StructBinding[allChildrenList
                .size()]);

        Arrays.sort(allChildren);
        listChildren = convertChildren(theStruct.allListChildren());

    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(@Nullable final StructBinding theOther) {
        if (theOther != null) {
            return name.compareTo(theOther.name);
        }
        return 1;
    }

    /** The children. */
    @SuppressWarnings("null")
    public StructBinding[] getChildren() {
        return children;
    }

    /** The fields. */
    @SuppressWarnings("null")
    public FieldBinding[] getFields() {
        return fields;
    }

    /** The global fields. */
    @SuppressWarnings("null")
    public FieldBinding[] getGlobalFields() {
        return globalFields;
    }

    /** The list children. */
    @SuppressWarnings("null")
    public StructBinding[] getListChildren() {
        return listChildren;
    }

    /** The name. */
    @SuppressWarnings("null")
    public String getName() {
        return name;
    }

    /** The object fields. */
    @SuppressWarnings("null")
    public FieldBinding[] getObjectFields() {
        return objectFields;
    }

    /** The optional children. */
    @SuppressWarnings("null")
    public StructBinding[] getOptionalChildren() {
        return optionalChildren;
    }

    /** The optional fields. */
    @SuppressWarnings("null")
    public FieldBinding[] getOptionalFields() {
        return optionalFields;
    }

    /** The virtual fields. */
    @SuppressWarnings("null")
    public FieldBinding[] getVirtualFields() {
        return virtualFields;
    }

    /** The optional flag. */
    @SuppressWarnings("null")
    public boolean isOptional() {
        return optional;
    }

    /** Is union ? */
    @SuppressWarnings("null")
    public boolean isUnion() {
        return union;
    }

    /** The children. */
    public void setChildren(final StructBinding[] theChildren) {
        children = theChildren;
    }

    /** The fields. */
    public void setFields(final FieldBinding[] theFields) {
        fields = theFields;
    }

    /** The global fields. */
    public void setGlobalFields(final FieldBinding[] theGlobalFields) {
        globalFields = theGlobalFields;
    }

    /** The list children. */
    public void setListChildren(final StructBinding[] theListChildren) {
        listChildren = theListChildren;
    }

    /** The name. */
    public void setName(final String theName) {
        name = theName;
    }

    /** The object fields. */
    public void setObjectFields(final FieldBinding[] theObjectFields) {
        objectFields = theObjectFields;
    }

    /** Is optional ? */
    public void setOptional(final boolean isOptional) {
        optional = isOptional;
    }

    /** The optional children of this struct. */
    public void setOptionalChildren(final StructBinding[] theOptionalChildren) {
        optionalChildren = theOptionalChildren;
    }

    /** The optional fields of this struct. */
    public void setOptionalFields(final FieldBinding[] theOptionalFields) {
        optionalFields = theOptionalFields;
    }

    /** Set true if this is a union ? */
    public void setUnion(final boolean isUnion) {
        union = isUnion;
    }

    /** The virtual fields. */
    public void setVirtualFields(final FieldBinding[] theVirtualFields) {
        virtualFields = theVirtualFields;
    }

    /**
     * Converts to a real Struct object.
     */
    @SuppressWarnings("null")
    public Struct toStruct(final StructInfo theStructInfo) {
        final List<Field<?, ?>> fieldList = new ArrayList<>();
        createFields(fields, FieldCategory.NORMAL, fieldList);
        createFields(globalFields, FieldCategory.GLOBAL, fieldList);
        createFields(objectFields, FieldCategory.NORMAL, fieldList);
        createFields(optionalFields, FieldCategory.OPTIONAL, fieldList);
        createFields(virtualFields, FieldCategory.VIRTUAL, fieldList);

        final List<Struct> childrenList = new ArrayList<>();
        if (children != null && children.length > 0) {
            for (final StructBinding sBinding : children) {
                childrenList.add(sBinding.toStruct(null));
            }
        }

        if (listChildren != null && listChildren.length > 0) {
            for (final StructBinding sBinding : listChildren) {
                childrenList.add(sBinding.toStruct(null).setList(true));
            }
        }

        if (optionalChildren != null && optionalChildren.length > 0) {
            for (final StructBinding sBinding : optionalChildren) {
                childrenList.add(sBinding.toStruct(null).setOptional(true));
            }
        }
        return new Struct(name, union, theStructInfo,
                childrenList.toArray(new Struct[childrenList.size()]),
                fieldList.toArray(new Field[fieldList.size()]));
    }
}
