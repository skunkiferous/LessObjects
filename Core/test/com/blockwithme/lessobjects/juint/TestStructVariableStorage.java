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

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestStructVariableStorage {

    private static final int _MAX_CAPACITY = 100;

    private static final int INITIAL_CAPACITY = 10;

    final static Random rand = new Random(System.currentTimeMillis());

    private static final boolean[] BOOLEANS = new boolean[_MAX_CAPACITY];

    private static final byte[] BYTES = new byte[_MAX_CAPACITY];

    private static final char[] CHARS = new char[_MAX_CAPACITY];

    private static final double[] DOUBLES = new double[_MAX_CAPACITY];

    private static final float[] FLOATS = new float[_MAX_CAPACITY];

    private static final int[] INTS = new int[_MAX_CAPACITY];

    private static final long[] LONGS = new long[_MAX_CAPACITY];

    private static final short[] SHORTS = new short[_MAX_CAPACITY];

    /** The factory. */
    private CompiledStorage[] COMPILED;

    @BeforeClass
    public static void setUpClass() {
        for (int i = 0; i < _MAX_CAPACITY; i++) {
            BYTES[i] = (byte) rand.nextInt();
            INTS[i] = rand.nextInt();
            SHORTS[i] = (short) rand.nextInt();
            LONGS[i] = rand.nextLong();
            DOUBLES[i] = rand.nextDouble();
            CHARS[i] = (char) rand.nextInt();
            FLOATS[i] = rand.nextFloat();
            BOOLEANS[i] = rand.nextBoolean();
        }
    }

    @Before
    public void setup() {
        final byte[] randomBytes = new byte[1];
        rand.nextBytes(randomBytes);
        final Struct tmp = new Struct("OptionalFieldTest", new Struct[] {},
                FACTORY.newIntField("intField"),
                FACTORY.newLongField("longField"),
                FACTORY.newByteField("byteField"),
                FACTORY.newShortField("shortField"),
                FACTORY.newFloatField("floatField"),
                FACTORY.newDoubleField("doubleField"),
                FACTORY.newCharField("charField"),
                FACTORY.newBooleanField("booleanField1"),
                FACTORY.newBooleanField("booleanField2"),
                FACTORY.newIntField("intField2"));

        COMPILED = new CompiledStorage[COMPILERS.length];
        int count = 0;
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(tmp);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, INITIAL_CAPACITY, true);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testModifyBoolean() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final BooleanField bool1 = (BooleanField) strg.compiledStructs
                    .field("booleanField1");
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, BOOLEANS[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], strg.storage.read(bool1));
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, BOOLEANS[i + 1],
                            strg.storage.read(bool1));
                }
            }
        }
    }

    @Test
    public void testModifyByte() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Byte Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) strg.compiledStructs
                    .field("byteField");
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(byteField, BYTES[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BYTES[i], strg.storage.read(byteField));
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, BYTES[i + 1],
                            strg.storage.read(byteField));
                }
            }
        }
    }

    @Test
    public void testModifyChar() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) strg.compiledStructs
                    .field("charField");
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, CHARS[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, CHARS[i], strg.storage.read(charField));
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, CHARS[i + 1],
                            strg.storage.read(charField));
                }
            }
        }
    }

    @Test
    public void testModifyDouble() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Double Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) strg.compiledStructs
                    .field("doubleField");

            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, DOUBLES[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, DOUBLES[i],
                        strg.storage.read(doubleField), DELTA);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, DOUBLES[i + 1],
                            strg.storage.read(doubleField), DELTA);
                }
            }
        }
    }

    @Test
    public void testModifyFloat() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Float Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final FloatField floatField = (FloatField) strg.compiledStructs
                    .field("floatField");
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, FLOATS[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, FLOATS[i], strg.storage.read(floatField),
                        DELTA);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, FLOATS[i + 1],
                            strg.storage.read(floatField), DELTA);
                }
            }
        }
    }

    @Test
    public void testModifyInt() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Int Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) strg.compiledStructs
                    .field("intField");
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, INTS[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, INTS[i], strg.storage.read(intField));
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, INTS[i + 1],
                            strg.storage.read(intField));
                }
            }
        }
    }

    @Test
    public void testModifyLong() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Long Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) strg.compiledStructs
                    .field("longField");

            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, LONGS[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, LONGS[i], strg.storage.read(longField));
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, LONGS[i + 1],
                            strg.storage.read(longField));
                }
            }
        }
    }

    @Test
    public void testModifyShort() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Short Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, SHORTS[i]);
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, SHORTS[i], strg.storage.read(shortField));
            }
            for (int i = 0; i < _MAX_CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.clear();
                if (i + 1 < _MAX_CAPACITY) {
                    strg.storage.selectStructure(i + 1);
                    assertEquals(message, SHORTS[i + 1],
                            strg.storage.read(shortField));
                }
            }
        }
    }
}
