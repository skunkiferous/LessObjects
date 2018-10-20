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
package com.blockwithme.lessobjects.util;

import java.util.Iterator;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.collections.CollectionFactory;
import com.blockwithme.lessobjects.storage.collections.CollectionFactoryImpl;

/**
 * An Interface containing all the common constants used within this project.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface StructConstants {

    /** The alpha numeric regex pattern. */
    String ALPHA_NUMERIC_PATTERN = "^[A-Za-z0-9_\\[\\]]*$";

    /** The primitive collection factory. */
    CollectionFactory COLL_FACTORY = new CollectionFactoryImpl();

    /** number of bits in five bytes */
    int B_FIVE_BYTES = 40;

    /** Max index value beyond which int based maps will be used. */
    int MAX_CHAR_INDEX = Character.MAX_VALUE + 1;

    /** The max storage capacity. */
    int MAX_STORAGE_CAPACITY = 1024 * 1024 * 1024;

    /** number of bits in four bytes */
    int B_FOUR_BYTES = 32;

    /** number of bits in one byte */
    int B_ONE_BYTE = 8;

    /** number of bits in seven bytes */
    int B_SEVEN_BYTES = 56;

    /** number of bits in six bytes */
    int B_SIX_BYTES = 48;

    /** number of bits in three bytes */
    int B_THREE_BYTES = 24;

    /** number of bits in two bytes */
    int B_TWO_BYTES = 16;

    /** Number of bits in byte */
    int BYTE_BITS = 8;

    /** To mask everything except last one byte */
    int BYTE_MASK = 255;

    /** number of bits in char */
    int CHAR_BITS = 16;

    /** Number of bytes in char */
    int CHAR_BYTES = 2;

    /** The default value string - an empty string used as a default value
     * if field default value is not specified. */
    String DEFAULT_VALUE_STR = "";

    /** number of bits in Double */
    int DOUBLE_BITS = 64;

    /** Number of bytes in double */
    int DOUBLE_BYTES = 8;

    /** The Constant EMPTY_BYTES. */
    byte[] EMPTY_BYTES = new byte[0];

    /** Empty array of struct children. */
    Struct[] EMPTY_CHILDREN = new Struct[0];

    /** Empty array of struct fields. */
    Field<?, ?>[] EMPTY_FIELDS = new Field<?, ?>[0];

    /** The empty obj arr. */
    final Object[] EMPTY_OBJ_ARR = new Object[0];

    /** Empty array of struct object fields. */
    ObjectField<?, ?>[] EMPTY_OBJECTS = new ObjectField<?, ?>[0];

    /** The field factory. */
    FieldFactory FACTORY = new FieldFactoryImpl();

    /** The Constant FIELD_CLASS_NAME. */
    String FIELD_CLASS_NAME = Util.getClassName(Field.class);

    /** number of bits in float */
    int FLOAT_BITS = 32;

    /** Number of bytes in float */
    int FLOAT_BYTES = 4;

    /** The index of '_fieldIndex' */
    int INDEX_FIELD_INDEX = 0;

    /** The index field name. */
    String INDEX_FIELD_NAME = "_fieldIndex";

    /** Number of bits in int */
    int INT_BITS = 32;

    /** Number of bytes in int */
    int INT_BYTES = 4;

    /** The iterator class name. */
    String ITERATOR_CLASS_NAME = Util.getClassName(Iterator.class);

    /** Used as the initial list size if the size is not specified. */
    int LIST_INITIAL_SIZE = 10;

    /** Number of bits in long */
    int LONG_BITS = 64;

    /** The long bits l. */
    long LONG_BITS_L = 64L;

    /** Number of bytes in long */
    int LONG_BYTES = 8;

    /** Number of bytes in long into 2 */
    int LONG_BYTES_X_2 = LONG_BYTES * 2;

    /** The max number of children. */
    int MAX_NUMBER_OF_CHILDREN = Character.MAX_VALUE;

    /** Prefix used name the parent struct in case of union */
    String PARENT_PREFIX = "_parent_";

    /** number of bits in short */
    int SHORT_BITS = 16;

    /** Number of bytes in short */
    int SHORT_BYTES = 2;

    /** To mask everything except last one short */
    int SHORT_MASK = 0xFFFF;

    /** The storage class name. */
    String STORAGE_CLASS_NAME = Util.getClassName(Storage.class);

    /** Class name of String Class used in code generation */
    String STRING_CLASS_NAME = Util.getClassName(String.class);

    /** The struct class name. */
    String STRUCT_CLASS_NAME = Util.getClassName(Struct.class);

}
