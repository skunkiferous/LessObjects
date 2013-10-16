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
import static com.blockwithme.lessobjects.util.StructConstants.INDEX_FIELD_NAME;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.GenericPseudoConverter;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;

/**
 * The Class ChangeChildUnion caches value needed to quickly access a
 * "change child union" storage.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChangeChildUnion {

    /** The children. */
    static final Struct[] CHILDREN = {};

    /** The fields. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static final Field<?, ?>[] FIELDS = {
            FACTORY.newBooleanField("booleanField"),
            FACTORY.newByteField("byteField"),
            FACTORY.newCharField("charField"),
            FACTORY.newDoubleField("doubleField"),
            FACTORY.newFloatField("floatField"),
            FACTORY.newIntField("intField"),
            FACTORY.newLongField("longField"),
            FACTORY.newShortField("shortField"),
            FACTORY.newObjectField(new GenericPseudoConverter(Object.class),
                    "objectField") };

    /** The new boolean field. */
    @Nonnull
    private final BooleanField<?, ?> booleanField;

    /** The new value byte field. */
    @Nonnull
    private final ByteField<?, ?> byteField;

    /** The new value char field. */
    @Nonnull
    private final CharField<?, ?> charField;

    /** The change struct. */
    private final Struct childStruct;

    /** The new double field. */
    @Nonnull
    private final DoubleField<?, ?> doubleField;

    /** The new value float field. */
    @Nonnull
    private final FloatField<?, ?> floatField;

    /** The index field. */
    @Nonnull
    private final CharField<?, ?> indexField;

    /** The new value int field. */
    @Nonnull
    private final IntField<?, ?> intField;

    /** The new value long field. */
    @Nonnull
    private final LongField<?, ?> longField;

    /** The new value object field. */
    @SuppressWarnings("rawtypes")
    @Nonnull
    private final ObjectField objectField;

    /** The new value short field. */
    @Nonnull
    private final ShortField<?, ?> shortField;

    /** Instantiates a new change child union.*/
    public ChangeChildUnion(final Struct theChildStruct) {
        childStruct = theChildStruct;
        booleanField = childStruct.field("booleanField");
        byteField = childStruct.field("byteField");
        charField = childStruct.field("charField");
        doubleField = childStruct.field("doubleField");
        floatField = childStruct.field("floatField");
        intField = childStruct.field("intField");
        longField = childStruct.field("longField");
        shortField = childStruct.field("shortField");
        objectField = childStruct.field("objectField");
        indexField = getIndexField();
    }

    @Nullable
    private CharField<?, ?> getIndexField() {
        try {
            // index fields will not be present initially
            // because index field is created at the time of compilation.
            // so we have to catch this exception and ignore it.
            return childStruct.field(INDEX_FIELD_NAME);
        } catch (final IllegalStateException exp) {
            // ignore this exception.
            return null;
        }
    }

    /**
     * @return the booleanField
     */
    @SuppressWarnings("null")
    public BooleanField<?, ?> booleanField() {
        return booleanField;
    }

    /**
     * @return the byteField
     */
    @SuppressWarnings("null")
    public ByteField<?, ?> byteField() {
        return byteField;
    }

    /**
     * @return the charField
     */
    @SuppressWarnings("null")
    public CharField<?, ?> charField() {
        return charField;
    }

    /**
     * The Actual Change struct object.
     *
     * @return the struct
     */
    @SuppressWarnings("null")
    public Struct childStruct() {
        return childStruct;
    }

    /**
     * @return the doubleField
     */
    @SuppressWarnings("null")
    public DoubleField<?, ?> doubleField() {
        return doubleField;
    }

    /**
     * @return the floatField
     */
    @SuppressWarnings("null")
    public FloatField<?, ?> floatField() {
        return floatField;
    }

    /**
     * @return the indexField
     */
    @SuppressWarnings("null")
    public CharField<?, ?> indexField() {
        return indexField;
    }

    /**
     * @return the intField
     */
    @SuppressWarnings("null")
    public IntField<?, ?> intField() {
        return intField;
    }

    /**
     * @return the longField
     */
    @SuppressWarnings("null")
    public LongField<?, ?> longField() {
        return longField;
    }

    /**
     * @return the objectField
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public ObjectField objectField() {
        return objectField;
    }

    /**
     * @return the shortField
     */
    @SuppressWarnings("null")
    public ShortField<?, ?> shortField() {
        return shortField;
    }
}
