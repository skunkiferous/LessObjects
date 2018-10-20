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
package com.blockwithme.lessobjects.juint;

import static com.blockwithme.lessobjects.juint.Constants.COMPILERS;
import static com.blockwithme.lessobjects.juint.Constants.DELTA;
import static com.blockwithme.lessobjects.juint.Constants.FACTORY;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
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

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
//$codepro.audit.disable
@SuppressWarnings({ "PMD", "all" })
public class TestStructOptionalChildren extends TestData {

    /** The factory. */
    private CompiledStorage[] COMPILED;

    @Before
    public void setup() {

        final byte[] randomBytes = new byte[1];
        rand.nextBytes(randomBytes);

        Struct o1 = new Struct("OptionalChild", new Struct[] {},
                FACTORY.newIntField("o1_intField"),
                FACTORY.newLongField("o1_longField"),
                FACTORY.newByteField("o1_byteField"),
                FACTORY.newShortField("o1_shortField"),
                FACTORY.newFloatField("o1_floatField"),
                FACTORY.newDoubleField("o1_doubleField"),
                FACTORY.newCharField("o1_charField"),
                FACTORY.newBooleanField("o1_booleanField1"),
                FACTORY.newBooleanField("o1_booleanField2"),
                FACTORY.newIntField("o1_intField2"));

        o1 = o1.setOptional(true);

        final Struct tmp = new Struct("OptionalChildrenTest",
                new Struct[] { o1 }, FACTORY.newIntField("intField"),
                FACTORY.newLongField("longField"),
                FACTORY.newByteField("byteField"),
                FACTORY.newShortField("shortField"),
                FACTORY.newFloatField("floatField"),
                FACTORY.newDoubleField("doubleField"),
                FACTORY.newCharField("charField"),
                FACTORY.newBooleanField("booleanField1"),
                FACTORY.newBooleanField("booleanField2"),
                FACTORY.newIntField("intField2"));

        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new com.blockwithme.lessobjects.juint.CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(tmp);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, _CAPACITY);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testClearChild() {

        final boolean[] BOOLEANS = booleans();
        final byte[] BYTES = bytes();
        final char[] CHARS = chars();
        final double[] DOUBLES = doubles();
        final float[] FLOATS = floats();
        final int[] INTS = ints();
        final long[] LONGS = longs();
        final short[] SHORTS = shorts();
        final String[] STRINGS = strings();

        for (final CompiledStorage strg : COMPILED) {
            final Struct struct = strg.compiledStructs;
            final Struct child = struct.child("OptionalChild");
            final BooleanField bool1 = struct.field("booleanField1");
            final BooleanField bool2 = struct
                    .field("OptionalChild.o1_booleanField1");
            final CharField charField = struct.field("charField");
            final CharField charField2 = struct
                    .field("OptionalChild.o1_charField");
            final FloatField floatField = struct.field("floatField");
            final FloatField floatField2 = struct
                    .field("OptionalChild.o1_floatField");
            final ByteField byteField = struct.field("byteField");
            final ByteField byteField2 = struct
                    .field("OptionalChild.o1_byteField");
            final IntField intField = struct.field("intField");
            final IntField intField2 = struct
                    .field("OptionalChild.o1_intField");
            final LongField longField = struct.field("longField");
            final LongField longField2 = struct
                    .field("OptionalChild.o1_longField");
            final ShortField shortField = struct.field("shortField");
            final ShortField shortField2 = struct
                    .field("OptionalChild.o1_shortField");
            final DoubleField doubleField = struct.field("doubleField");
            final DoubleField doubleField2 = struct
                    .field("OptionalChild.o1_doubleField");
            final Storage st = strg.storage;

            for (int i = 0; i < _CAPACITY; i++) {
                st.selectStructure(i);
                st.write(bool1, BOOLEANS[i]);
                st.write(bool2, BOOLEANS[i]);
                st.write(byteField, BYTES[i]);
                st.write(byteField2, BYTES[i]);
                st.write(charField, CHARS[i]);
                st.write(charField2, CHARS[i]);
                st.write(doubleField, DOUBLES[i]);
                st.write(doubleField2, DOUBLES[i]);
                st.write(intField, INTS[i]);
                st.write(longField, LONGS[i]);
                st.write(intField, INTS[i]);
                st.write(intField2, INTS[i]);
                st.write(longField, LONGS[i]);
                st.write(longField2, LONGS[i]);
                st.write(shortField, SHORTS[i]);
                st.write(shortField2, SHORTS[i]);
            }

            for (int i = 0; i < _CAPACITY; i++) {
                st.selectStructure(i);
                assertEquals(BYTES[i], st.read(byteField));
                assertEquals(BYTES[i], st.read(byteField2));
                assertEquals(CHARS[i], st.read(charField));
                assertEquals(CHARS[i], st.read(charField2));
                assertEquals(DOUBLES[i], st.read(doubleField), DELTA);
                assertEquals(DOUBLES[i], st.read(doubleField2), DELTA);
                assertEquals(INTS[i], st.read(intField));
                assertEquals(LONGS[i], st.read(longField));
                assertEquals(INTS[i], st.read(intField));
                assertEquals(INTS[i], st.read(intField2));
                assertEquals(LONGS[i], st.read(longField));
                assertEquals(LONGS[i], st.read(longField2));
                assertEquals(SHORTS[i], st.read(shortField));
                assertEquals(SHORTS[i], st.read(shortField2));
            }

            for (int i = 0; i < _CAPACITY; i++) {
                st.selectStructure(i);
                st.clearChild(child);
                assertEquals(0, st.read(byteField2));
                assertEquals(0, st.read(charField2));
                assertEquals(0, st.read(doubleField2), DELTA);
                assertEquals(0, st.read(intField2));
                assertEquals(0, st.read(longField2));
                assertEquals(0, st.read(shortField2));
            }
        }
    }

    @Test
    public void testModifyBoolean() {

        final boolean[] BOOLEANS = booleans();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final BooleanField bool1 = (BooleanField) strg.compiledStructs
                    .field("booleanField1");
            final BooleanField bool2 = (BooleanField) strg.compiledStructs
                    .field("OptionalChild.o1_booleanField1");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, BOOLEANS[i]);
                strg.storage.write(bool2, BOOLEANS[i]);
                assertEquals(message, BOOLEANS[i], strg.storage.read(bool1));
                assertEquals(message, BOOLEANS[i], strg.storage.read(bool2));
            }
        }
    }

    @Test
    public void testModifyByte() {

        final byte[] BYTES = bytes();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Byte Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) strg.compiledStructs
                    .field("byteField");
            final ByteField byteField2 = (ByteField) strg.compiledStructs
                    .field("OptionalChild.o1_byteField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(byteField, BYTES[i]);
                strg.storage.write(byteField2, BYTES[i]);
                assertEquals(message, BYTES[i], strg.storage.read(byteField));
                assertEquals(message, BYTES[i], strg.storage.read(byteField2));
            }
        }
    }

    @Test
    public void testModifyChar() {

        final char[] CHARS = chars();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) strg.compiledStructs
                    .field("charField");
            final CharField charField2 = (CharField) strg.compiledStructs
                    .field("OptionalChild.o1_charField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, CHARS[i]);
                strg.storage.write(charField2, CHARS[i]);
                assertEquals(message, CHARS[i], strg.storage.read(charField));
                assertEquals(message, CHARS[i], strg.storage.read(charField2));
            }
        }
    }

    @Test
    public void testModifyDouble() {

        final double[] DOUBLES = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Double Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) strg.compiledStructs
                    .field("doubleField");
            final DoubleField doubleField2 = (DoubleField) strg.compiledStructs
                    .field("OptionalChild.o1_doubleField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, DOUBLES[i]);
                strg.storage.write(doubleField2, DOUBLES[i]);
                assertEquals(message, DOUBLES[i],
                        strg.storage.read(doubleField), DELTA);
                assertEquals(message, DOUBLES[i],
                        strg.storage.read(doubleField2), DELTA);
            }
        }
    }

    @Test
    public void testModifyFloat() {

        final float[] FLOATS = floats();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Float Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final FloatField floatField = (FloatField) strg.compiledStructs
                    .field("floatField");
            final FloatField floatField2 = (FloatField) strg.compiledStructs
                    .field("OptionalChild.o1_floatField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, FLOATS[i]);
                assertEquals(message, FLOATS[i], strg.storage.read(floatField),
                        DELTA);
                assertEquals(message, 0, strg.storage.read(floatField2), DELTA);
                strg.storage.write(floatField2, FLOATS[i]);
                assertEquals(message, FLOATS[i],
                        strg.storage.read(floatField2), DELTA);
            }
        }
    }

    @Test
    public void testModifyInt() {

        final int[] INTS = ints();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Int Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) strg.compiledStructs
                    .field("intField");
            final IntField intField2 = (IntField) strg.compiledStructs
                    .field("OptionalChild.o1_intField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, INTS[i]);

                assertEquals(message, INTS[i], strg.storage.read(intField));
                assertEquals(message, 0, strg.storage.read(intField2));

                strg.storage.write(intField2, INTS[i]);
                assertEquals(message, INTS[i], strg.storage.read(intField2));
            }
        }
    }

    @Test
    public void testModifyLong() {

        final long[] LONGS = longs();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Long Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) strg.compiledStructs
                    .field("longField");
            final LongField longField2 = (LongField) strg.compiledStructs
                    .field("OptionalChild.o1_longField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, LONGS[i]);
                assertEquals(message, LONGS[i], strg.storage.read(longField));
                assertEquals(message, 0, strg.storage.read(longField2));
                strg.storage.write(longField2, LONGS[i]);
                assertEquals(message, LONGS[i], strg.storage.read(longField2));
            }
        }
    }

    @Test
    public void testModifyShort() {

        final short[] SHORTS = shorts();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Short Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");
            final ShortField shortField2 = (ShortField) strg.compiledStructs
                    .field("OptionalChild.o1_shortField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, SHORTS[i]);
                strg.storage.write(shortField2, SHORTS[i]);
                assertEquals(message, SHORTS[i], strg.storage.read(shortField));
                assertEquals(message, SHORTS[i], strg.storage.read(shortField2));
            }
        }
    }

    @Test
    public void testSchema() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct compiledStructs = strg.compiledStructs;
            TestData.checkSchema(compiledStructs);
        }
    }
}
