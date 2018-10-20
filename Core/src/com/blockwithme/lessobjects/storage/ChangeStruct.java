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
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.util.StructConstants.FACTORY;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.IntField;

/**
 * The ChangeStruct class defines a generic structure for recording the changes in a
 * particular storage. This class wraps a Struct instance that is used by {@link ChangeStorage}
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChangeStruct {

    /** The fields. */
    private static final Field<?, ?>[] FIELDS = {

    FACTORY.newStringField("fieldName"),
            FACTORY.newEnumField("changeType", ChangeType.class),
            FACTORY.newIntField("structureIndex"),
            FACTORY.newIntField("storageKey") };

    /** The _new. */
    @Nonnull
    private final ChangeChildUnion _new; // $codepro.audit.disable

    /** The _old. */
    @Nonnull
    private final ChangeChildUnion _old; // $codepro.audit.disable

    /** The field type. */
    @Nonnull
    private final ByteField<ChangeType, ?> changeType;

    /** The field name. */
    @Nonnull
    private final ObjectField<String, ?> fieldName;

    /** The index. */
    @Nonnull
    private final IntField<?, ?> index;

    /** The change struct. */
    private final Struct struct;

    @SuppressWarnings("null")
    public ChangeStruct(final Compiler theCompiler) {

        final Struct newChildStruct = new Struct("newValue", true,
                ChangeChildUnion.CHILDREN, ChangeChildUnion.FIELDS);
        final Struct oldchildStruct = new Struct("oldValue", true,
                ChangeChildUnion.CHILDREN, ChangeChildUnion.FIELDS);

        final Struct theStruct = new Struct("ChangeStruct", new Struct[] {
                newChildStruct, oldchildStruct }, FIELDS);

        struct = theCompiler.compile(theStruct);
        index = struct.field("structureIndex");
        changeType = struct.field("changeType");
        fieldName = struct.field("fieldName");

        _new = new ChangeChildUnion(struct.child("newValue"));
        _old = new ChangeChildUnion(struct.child("oldValue"));

    }

    /**
     * @return the changeType
     */
    @SuppressWarnings("null")
    public ByteField<ChangeType, ?> changeType() {
        return changeType;
    }

    /**
     * @return the fieldName
     */
    @SuppressWarnings("null")
    public ObjectField<String, ?> fieldName() {
        return fieldName;
    }

    /**
     * @return the _new
     */
    @SuppressWarnings("null")
    public ChangeChildUnion getNew() {
        return _new;
    }

    /**
     * @return the _old
     */
    @SuppressWarnings("null")
    public ChangeChildUnion getOld() {
        return _old;
    }

    /**
     * @return the index
     */
    @SuppressWarnings("null")
    public IntField<?, ?> index() {
        return index;
    }

    /**
     * @return the changeStruct
     */
    @SuppressWarnings("null")
    public Struct struct() {
        return struct;
    }

}
