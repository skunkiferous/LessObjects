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

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.BooleanValueChange;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.IntValueChange;
import com.blockwithme.lessobjects.beans.LongValueChange;
import com.blockwithme.lessobjects.beans.ShortValueChange;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.ActionSet;

//CHECKSTYLE IGNORE FOR NEXT 700 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestStructFields extends TestData {

    protected Struct basestruct;

    /** The factory. */
    protected CompiledStorage[] COMPILED;

    @Before
    public void setup() {

        basestruct = new Struct("FieldTest", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newByteField("byteField"),
                        FACTORY.newShortField("shortField"),
                        FACTORY.newFloatField("floatField"),
                        FACTORY.newDoubleField("doubleField"),
                        FACTORY.newCharField("charField"),
                        FACTORY.newBooleanField("booleanField1"),
                        FACTORY.newBooleanField("booleanField2"),
                        FACTORY.newIntField("intField2"),
                        FACTORY.newStringField("stringField1") });
        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(basestruct);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, _CAPACITY);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testModifyBoolean() {

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
            String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final BooleanField bool1 = (BooleanField) strg.compiledStructs
                    .field("booleanField1");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, BOOLEANS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], strg.storage.read(bool1));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Boolean field -"
                    + bool1.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (BOOLEANS[count]) {
                    final BooleanValueChange change = (BooleanValueChange) changes
                            .next();
                    assertEquals(message, bool1, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", false,
                            change.booleanOldValue());
                    assertEquals(message + " new value -", BOOLEANS[count],
                            change.booleanNewValue());
                    assertEquals(message + " old value -", false,
                            change.oldValue());
                    assertEquals(message + " new value -", BOOLEANS[count],
                            change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyByte() {

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
            String message = "Byte Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) strg.compiledStructs
                    .field("byteField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(byteField, BYTES[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BYTES[i], strg.storage.read(byteField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Byte field -"
                    + byteField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (BYTES[count] != 0) {
                    final ByteValueChange change = (ByteValueChange) changes
                            .next();
                    assertEquals(message, byteField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.byteOldValue());
                    assertEquals(message + " new value -", BYTES[count],
                            change.byteNewValue());
                    assertEquals(message + " old value -",
                            Byte.valueOf((byte) 0), change.oldValue());
                    assertEquals(message + " new value -",
                            Byte.valueOf(BYTES[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyChar() {

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
            String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) strg.compiledStructs
                    .field("charField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, CHARS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, CHARS[i], strg.storage.read(charField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Char field -"
                    + charField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (CHARS[count] != 0) {
                    final CharValueChange change = (CharValueChange) changes
                            .next();
                    assertEquals(message, charField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.charOldValue());
                    assertEquals(message + " new value -", CHARS[count],
                            change.charNewValue());
                    assertEquals(message + " old value -",
                            Character.valueOf((char) 0), change.oldValue());
                    assertEquals(message + " new value -",
                            Character.valueOf(CHARS[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyDouble() {

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

            String message = "Double Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) strg.compiledStructs
                    .field("doubleField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, DOUBLES[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, DOUBLES[i],
                        strg.storage.read(doubleField), DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Double field -"
                    + doubleField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (DOUBLES[count] != 0) {
                    final DoubleValueChange change = (DoubleValueChange) changes
                            .next();
                    assertEquals(message, doubleField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.doubleOldValue(), DELTA);
                    assertEquals(message + " new value -", DOUBLES[count],
                            change.doubleNewValue(), DELTA);
                    assertEquals(message + " old value -", Double.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Double.valueOf(DOUBLES[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyFloat() {

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

            String message = "Float Field Test failed for Compiler -"
                    + strg.compiler.compilerName();

            final FloatField floatField = (FloatField) strg.compiledStructs
                    .field("floatField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, FLOATS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, FLOATS[i], strg.storage.read(floatField),
                        DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Float field -"
                    + floatField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (FLOATS[count] != 0) {
                    final FloatValueChange change = (FloatValueChange) changes
                            .next();
                    assertEquals(message, floatField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.floatOldValue(), DELTA);
                    assertEquals(message + " new value -", FLOATS[count],
                            change.floatNewValue(), DELTA);
                    assertEquals(message + " old value -", Float.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Float.valueOf(FLOATS[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyInt() {

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

            String message = "Int Field Test failed for Compiler -"
                    + strg.compiler.compilerName();

            final IntField intField = (IntField) strg.compiledStructs
                    .field("intField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, INTS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, INTS[i], strg.storage.read(intField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Int field -"
                    + intField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (INTS[count] != 0) {
                    final IntValueChange change = (IntValueChange) changes
                            .next();
                    assertEquals(message, intField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.intOldValue());
                    assertEquals(message + " new value -", INTS[count],
                            change.intNewValue());
                    assertEquals(message + " old value -", Integer.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Integer.valueOf(INTS[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyLong() {

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

            String message = "Long Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) strg.compiledStructs
                    .field("longField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, LONGS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, LONGS[i], strg.storage.read(longField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Int field -"
                    + longField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (LONGS[count] != 0) {
                    final LongValueChange change = (LongValueChange) changes
                            .next();
                    assertEquals(message, longField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.longOldValue());
                    assertEquals(message + " new value -", LONGS[count],
                            change.longNewValue());
                    assertEquals(message + " old value -", Long.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Long.valueOf(LONGS[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyShort() {

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
            String message = "Short Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, SHORTS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, SHORTS[i], strg.storage.read(shortField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " short field -"
                    + shortField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (SHORTS[count] != 0) {
                    final ShortValueChange change = (ShortValueChange) changes
                            .next();
                    assertEquals(message, shortField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.shortOldValue());
                    assertEquals(message + " new value -", SHORTS[count],
                            change.shortNewValue());
                    assertEquals(message + " old value -",
                            Short.valueOf((short) 0), change.oldValue());
                    assertEquals(message + " new value -",
                            Short.valueOf(SHORTS[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyString() {

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
            String message = "String Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ObjectField<String, ?> str1 = (ObjectField<String, ?>) strg.compiledStructs
                    .field("stringField1");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(str1, "test" + INTS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, "test" + INTS[i], strg.storage.read(str1));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " String field -"
                    + str1.name();
            for (int count = 0; count < _CAPACITY; count++) {
                final ValueChange change = changes.next();
                assertEquals(message, str1, change.field());
                assertEquals(message + " Struct index -", count,
                        change.structureIndex());
                assertEquals(message + " old value -", null, change.oldValue());
                assertEquals(message + " new value -", "test" + INTS[count],
                        change.newValue());
            }
        }
    }

    @Test
    public void testReadWriteAnyBoolean() {

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
            final String message = "TestStructFields.testReadWriteAnyBoolean() -"
                    + strg.compiler.compilerName();

            final BooleanField bool1 = (BooleanField) strg.compiledStructs
                    .field("booleanField1");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                bool1.writeAny(BOOLEANS[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], bool1.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyByte() {

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
            final String message = "TestStructFields.testReadWriteAnyByte() -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) strg.compiledStructs
                    .field("byteField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                byteField.writeAny(BYTES[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BYTES[i],
                        (byte) byteField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyChar() {

        final char[] CHARS = chars();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructFields.testReadWriteAnyChar() -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) strg.compiledStructs
                    .field("charField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                charField.writeAny(CHARS[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, CHARS[i],
                        (char) charField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyDouble() {

        final double[] DOUBLES = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructFields.testReadWriteAnyDouble() -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) strg.compiledStructs
                    .field("doubleField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                doubleField.writeAny(DOUBLES[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, DOUBLES[i],
                        (double) doubleField.readAny(strg.storage), DELTA);
            }
        }
    }

    @Test
    public void testReadWriteAnyFloat() {

        final float[] FLOATS = floats();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructFields.testReadWriteAnyFloat() -"
                    + strg.compiler.compilerName();
            final FloatField floatField = (FloatField) strg.compiledStructs
                    .field("floatField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                floatField.writeAny(FLOATS[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, FLOATS[i],
                        (float) floatField.readAny(strg.storage), DELTA);
            }
        }
    }

    @Test
    public void testReadWriteAnyInt() {

        final int[] INTS = ints();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructFields.testReadWriteAnyInt() -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) strg.compiledStructs
                    .field("intField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                intField.writeAny(INTS[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, INTS[i],
                        (int) intField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyLong() {

        final long[] LONGS = longs();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructFields.testReadWriteAnyLong() -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) strg.compiledStructs
                    .field("longField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                longField.writeAny(LONGS[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, LONGS[i],
                        (long) longField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyShort() {

        final short[] SHORTS = shorts();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructFields.testReadWriteAnyShort() -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                shortField.writeAny(SHORTS[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, SHORTS[i],
                        (short) shortField.readAny(strg.storage));
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
