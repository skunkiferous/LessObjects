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
// $codepro.audit.disable
/**
 *
 */
package com.blockwithme.lessobjects.juint;

import static com.blockwithme.lessobjects.juint.Constants.COMPILERS;
import static com.blockwithme.lessobjects.juint.Constants.DELTA;
import static com.blockwithme.lessobjects.juint.Constants.FACTORY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
@SuppressWarnings({ "PMD", "all" })
/** The Class TestCoreClasses. */
public class TestStructUnion extends TestData {

    /** The children. */
    static final Struct[] CHILDREN = {
            new Struct("one", new Struct[0], getFields()),
            new Struct("two", new Struct[0], getFields()),
            new Struct("three", new Struct[0], getFields()) };

    @SuppressWarnings("unchecked")
    private static <T> T field(final Struct struct, final String childName,
            final String fieldName, final Class<T> fieldClass) {
        final Struct child = struct.child(childName);
        return (T) child.field(fieldName);
    }

    static Field<?, ?>[] getFields() {
        return new Field<?, ?>[] { FACTORY.newIntField("intField"),
                FACTORY.newByteField("byteField"),
                FACTORY.newShortField("shortField"),
                FACTORY.newCharField("charField"),
                FACTORY.newLongField("longField"),
                FACTORY.newFloatField("floatField"),
                FACTORY.newDoubleField("doubleField"),
                FACTORY.newBooleanField("booleanField") };
    }

    /**
     * @param cmplr
     * @return
     */
    private Struct createStruct(final Compiler cmplr) {
        final Struct struct = cmplr.compile(new Struct("SingleStruct",
                new Struct[0], getFields()));
        return struct;
    }

    /**
     * @param cmplr
     * @return
     */
    private Struct createUnion(final Compiler cmplr) {
        final Struct union = cmplr.compile(new Struct("SimpleUnion", true,
                CHILDREN, new Field<?, ?>[] { FACTORY.newByteField("test") }));
        return union;
    }

    @Test
    public void struct() {

        final boolean[] BOOLEANS = booleans();
        final byte[] BYTES = bytes();
        final char[] CHARS = chars();
        final double[] DOUBLES = doubles();
        final float[] FLOATS = floats();
        final int[] INTS = ints();
        final long[] LONGS = longs();
        final short[] SHORTS = shorts();
        final String[] STRINGS = strings();

        for (final Compiler cmplr : COMPILERS) {
            final Struct struct = createStruct(cmplr);
            final IntField intF = struct.field("intField");
            final ByteField byteF = struct.field("byteField");
            final ShortField shortF = struct.field("shortField");
            final CharField charF = struct.field("charField");
            final LongField longF = struct.field("longField");
            final FloatField floatF = struct.field("floatField");
            final DoubleField doubleF = struct.field("doubleField");
            final BooleanField booleanF = struct.field("booleanField");

            final Storage data = cmplr.initStorage(struct, _CAPACITY);

            for (int i = 0; i < _CAPACITY; i++) {
                data.selectStructure(i);
                data.write(booleanF, BOOLEANS[i]);
                data.write(byteF, BYTES[i]);
                data.write(charF, CHARS[i]);
                data.write(doubleF, DOUBLES[i]);
                data.write(floatF, FLOATS[i]);
                data.write(intF, INTS[i]);
                data.write(longF, LONGS[i]);
                data.write(shortF, SHORTS[i]);

            }
            for (int i = 0; i < _CAPACITY; i++) {
                data.selectStructure(i);
                assertEquals(BOOLEANS[i], data.read(booleanF));
                assertEquals(BYTES[i], data.read(byteF));
                assertEquals(CHARS[i], data.read(charF));
                assertEquals(DOUBLES[i], data.read(doubleF), DELTA);
                assertEquals(FLOATS[i], data.read(floatF), DELTA);
                assertEquals(INTS[i], data.read(intF));
                assertEquals(LONGS[i], data.read(longF));
                assertEquals(SHORTS[i], data.read(shortF));
            }
        }
    }

    @Test
    public void testSchema() {
        for (final Compiler cmplr : COMPILERS) {
            TestData.checkSchema(createStruct(cmplr));
            TestData.checkSchema(createUnion(cmplr));
        }
    }

    @Test
    public void union() {

        final boolean[] BOOLEANS = booleans();
        final byte[] BYTES = bytes();
        final char[] CHARS = chars();
        final double[] DOUBLES = doubles();
        final float[] FLOATS = floats();
        final int[] INTS = ints();
        final long[] LONGS = longs();
        final short[] SHORTS = shorts();
        final String[] STRINGS = strings();

        for (final Compiler cmplr : COMPILERS) {
            final Struct union = createUnion(cmplr);
            final Storage data = cmplr.initStorage(union, _CAPACITY);

            for (int i = 0; i < _CAPACITY; i++) {
                data.selectStructure(i);
                data.selectUnionPosition(union, union.childIndex("one"));

                final BooleanField child1boolean = field(union, "one",
                        "booleanField", BooleanField.class);
                final ByteField child1Byte = field(union, "one", "byteField",
                        ByteField.class);
                final CharField child1Char = field(union, "one", "charField",
                        CharField.class);
                final DoubleField child1Double = field(union, "one",
                        "doubleField", DoubleField.class);
                final FloatField child1Float = field(union, "one",
                        "floatField", FloatField.class);
                final ShortField child1Short = field(union, "one",
                        "shortField", ShortField.class);
                final IntField child1Int = field(union, "one", "intField",
                        IntField.class);
                final LongField child1Long = field(union, "one", "longField",
                        LongField.class);

                data.write(child1boolean, BOOLEANS[i]);
                data.write(child1Byte, BYTES[i]);
                data.write(child1Char, CHARS[i]);
                data.write(child1Double, DOUBLES[i]);
                data.write(child1Float, FLOATS[i]);
                data.write(child1Int, INTS[i]);
                data.write(child1Long, LONGS[i]);
                data.write(child1Short, SHORTS[i]);

                assertEquals(BOOLEANS[i], data.read(child1boolean));
                assertEquals(BYTES[i], data.read(child1Byte));
                assertEquals(CHARS[i], data.read(child1Char));
                assertEquals(DOUBLES[i], data.read(child1Double), DELTA);
                assertEquals(FLOATS[i], data.read(child1Float), DELTA);
                assertEquals(INTS[i], data.read(child1Int));
                assertEquals(LONGS[i], data.read(child1Long));
                assertEquals(SHORTS[i], data.read(child1Short));

                final BooleanField child2boolean = field(union, "two",
                        "booleanField", BooleanField.class);
                final ByteField child2Byte = field(union, "two", "byteField",
                        ByteField.class);
                final CharField child2Char = field(union, "two", "charField",
                        CharField.class);
                final DoubleField child2Double = field(union, "two",
                        "doubleField", DoubleField.class);
                final FloatField child2Float = field(union, "two",
                        "floatField", FloatField.class);
                final ShortField child2Short = field(union, "two",
                        "shortField", ShortField.class);
                final IntField child2Int = field(union, "two", "intField",
                        IntField.class);
                final LongField child2Long = field(union, "two", "longField",
                        LongField.class);

                data.selectUnionPosition(union, union.childIndex("two"));

                data.write(child2boolean, BOOLEANS[i]);
                data.write(child2Byte, BYTES[i]);
                data.write(child2Char, CHARS[i]);
                data.write(child2Double, DOUBLES[i]);
                data.write(child2Float, FLOATS[i]);
                data.write(child2Int, INTS[i]);
                data.write(child2Long, LONGS[i]);
                data.write(child2Short, SHORTS[i]);

                assertEquals(BOOLEANS[i], data.read(child2boolean));
                assertEquals(BYTES[i], data.read(child2Byte));
                assertEquals(CHARS[i], data.read(child2Char));
                assertEquals(DOUBLES[i], data.read(child2Double), DELTA);
                assertEquals(FLOATS[i], data.read(child2Float), DELTA);
                assertEquals(INTS[i], data.read(child2Int));
                assertEquals(LONGS[i], data.read(child2Long));
                assertEquals(SHORTS[i], data.read(child2Short));

                data.selectUnionPosition(union, union.childIndex("three"));

                final BooleanField child3boolean = field(union, "three",
                        "booleanField", BooleanField.class);
                final ByteField child3Byte = field(union, "three", "byteField",
                        ByteField.class);
                final CharField child3Char = field(union, "three", "charField",
                        CharField.class);
                final DoubleField child3Double = field(union, "three",
                        "doubleField", DoubleField.class);
                final FloatField child3Float = field(union, "three",
                        "floatField", FloatField.class);
                final ShortField child3Short = field(union, "three",
                        "shortField", ShortField.class);
                final IntField child3Int = field(union, "three", "intField",
                        IntField.class);
                final LongField child3Long = field(union, "three", "longField",
                        LongField.class);

                data.write(child3boolean, BOOLEANS[i]);
                data.write(child3Byte, BYTES[i]);
                data.write(child3Char, CHARS[i]);
                data.write(child3Double, DOUBLES[i]);
                data.write(child3Float, FLOATS[i]);
                data.write(child3Int, INTS[i]);
                data.write(child3Long, LONGS[i]);
                data.write(child3Short, SHORTS[i]);

                assertEquals(BOOLEANS[i], data.read(child3boolean));
                assertEquals(BYTES[i], data.read(child3Byte));
                assertEquals(CHARS[i], data.read(child3Char));
                assertEquals(DOUBLES[i], data.read(child3Double), DELTA);
                assertEquals(FLOATS[i], data.read(child3Float), DELTA);
                assertEquals(INTS[i], data.read(child3Int));
                assertEquals(LONGS[i], data.read(child3Long));
                assertEquals(SHORTS[i], data.read(child3Short));

            }
        }
    }
}
