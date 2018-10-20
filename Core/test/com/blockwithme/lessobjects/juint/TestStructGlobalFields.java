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
import com.blockwithme.lessobjects.fields.global.BooleanGlobalField;
import com.blockwithme.lessobjects.fields.global.ByteGlobalField;
import com.blockwithme.lessobjects.fields.global.CharGlobalField;
import com.blockwithme.lessobjects.fields.global.DoubleGlobalField;
import com.blockwithme.lessobjects.fields.global.FloatGlobalField;
import com.blockwithme.lessobjects.fields.global.IntGlobalField;
import com.blockwithme.lessobjects.fields.global.LongGlobalField;
import com.blockwithme.lessobjects.fields.global.ShortGlobalField;
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
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 800 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestStructGlobalFields extends TestData {

    /** The factory. */
    private CompiledStorage[] COMPILED;

    @Before
    public void setup() {

        final byte[] randomBytes = new byte[1];
        rand.nextBytes(randomBytes);
        final Struct tmp = new Struct("GlobalFieldTest", new Struct[] {},
                FACTORY.newIntField("intField"),
                FACTORY.newLongField("longField"),
                FACTORY.newByteField("byteField"),
                FACTORY.newShortField("shortField"),
                FACTORY.newFloatField("floatField"),
                FACTORY.newDoubleField("doubleField"),
                FACTORY.newCharField("charField"),
                FACTORY.newBooleanField("booleanField1"),
                FACTORY.newBooleanField("booleanField2"),
                FACTORY.newIntField("intField2"),
                FACTORY.newIntGlobalField("G_intField"),
                FACTORY.newLongGlobalField("G_longField"),
                FACTORY.newByteGlobalField("G_byteField"),
                FACTORY.newShortGlobalField("G_shortField"),
                FACTORY.newFloatGlobalField("G_floatField"),
                FACTORY.newDoubleGlobalField("G_doubleField"),
                FACTORY.newCharGlobalField("G_charField"),
                FACTORY.newBooleanGlobalField("G_boolean"),
                FACTORY.newStringGlobalField("G_String"));

        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(tmp);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, _CAPACITY);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testModifyBoolean() {

        final boolean[] BOOLEANS = booleans();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final BooleanField bool1 = (BooleanField) strg.compiledStructs
                    .field("booleanField1");
            final BooleanGlobalField bool_global = strg.compiledStructs
                    .field("G_boolean");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, BOOLEANS[i]);
            }
            strg.storage.write(bool_global, true);

            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], strg.storage.read(bool1));
            }
            assertEquals(message, true, strg.storage.read(bool_global));

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

            final BooleanValueChange change = (BooleanValueChange) changes
                    .next();
            assertEquals(message, bool_global, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", false,
                    change.booleanOldValue());
            assertEquals(message + " new value -", true,
                    change.booleanNewValue());
            assertEquals(message + " old value -", false, change.oldValue());
            assertEquals(message + " new value -", true, change.newValue());
        }
    }

    @Test
    public void testModifyByte() {

        final byte[] BYTES = bytes();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Byte Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) strg.compiledStructs
                    .field("byteField");
            final ByteGlobalField byte_global = strg.compiledStructs
                    .field("G_byteField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(byteField, BYTES[i]);
            }
            strg.storage.write(byte_global, (byte) 10);
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BYTES[i], strg.storage.read(byteField));
            }
            assertEquals(message, 10, strg.storage.read(byte_global));
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

            final ByteValueChange change = (ByteValueChange) changes.next();
            assertEquals(message, byte_global, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.byteOldValue());
            assertEquals(message + " new value -", 10, change.byteNewValue());
            assertEquals(message + " old value -", Byte.valueOf((byte) 0),
                    change.oldValue());
            assertEquals(message + " new value -", Byte.valueOf((byte) 10),
                    change.newValue());
        }
    }

    @Test
    public void testModifyChar() {

        final char[] CHARS = chars();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final CharField charField = strg.compiledStructs.field("charField");
            final CharGlobalField char_global = strg.compiledStructs
                    .field("G_charField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, CHARS[i]);
            }
            strg.storage.write(char_global, 'a');
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, CHARS[i], strg.storage.read(charField));
            }
            assertEquals(message, 'a', strg.storage.read(char_global));
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
            final CharValueChange change = (CharValueChange) changes.next();
            assertEquals(message, char_global, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.charOldValue());
            assertEquals(message + " new value -", 'a', change.charNewValue());
            assertEquals(message + " old value -", Character.valueOf((char) 0),
                    change.oldValue());
            assertEquals(message + " new value -", Character.valueOf('a'),
                    change.newValue());
        }
    }

    @Test
    public void testModifyDouble() {

        final double[] DOUBLES = doubles();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Double Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) strg.compiledStructs
                    .field("doubleField");
            final DoubleGlobalField global_double = strg.compiledStructs
                    .field("G_doubleField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, DOUBLES[i]);
            }
            strg.storage.write(global_double, 10.1d);

            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, DOUBLES[i],
                        strg.storage.read(doubleField), DELTA);
            }
            assertEquals(message, 10.1, strg.storage.read(global_double), DELTA);

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
            final DoubleValueChange change = (DoubleValueChange) changes.next();
            assertEquals(message, global_double, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.doubleOldValue(),
                    DELTA);
            assertEquals(message + " new value -", 10.1,
                    change.doubleNewValue(), DELTA);
            assertEquals(message + " old value -", Double.valueOf(0),
                    change.oldValue());
            assertEquals(message + " new value -", Double.valueOf(10.1),
                    change.newValue());
        }
    }

    @Test
    public void testModifyFloat() {

        final float[] FLOATS = floats();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Float Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final FloatField floatField = strg.compiledStructs
                    .field("floatField");
            final FloatGlobalField global_float = strg.compiledStructs
                    .field("G_floatField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, FLOATS[i]);
            }
            strg.storage.write(global_float, 10.1f);

            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, FLOATS[i], strg.storage.read(floatField),
                        DELTA);
            }
            assertEquals(message, 10.1f, strg.storage.read(global_float), DELTA);

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
            final FloatValueChange change = (FloatValueChange) changes.next();
            assertEquals(message, global_float, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.floatOldValue(),
                    DELTA);
            assertEquals(message + " new value -", 10.1f,
                    change.floatNewValue(), DELTA);
            assertEquals(message + " old value -", Float.valueOf(0),
                    change.oldValue());
            assertEquals(message + " new value -", Float.valueOf(10.1f),
                    change.newValue());

        }
    }

    @Test
    public void testModifyInt() {

        final int[] INTS = ints();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Int Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) strg.compiledStructs
                    .field("intField");
            final IntGlobalField global_int = strg.compiledStructs
                    .field("G_intField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, INTS[i]);
            }
            strg.storage.write(global_int, 10);
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, INTS[i], strg.storage.read(intField));
            }
            assertEquals(message, 10, strg.storage.read(global_int));

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
            final IntValueChange change = (IntValueChange) changes.next();
            assertEquals(message, global_int, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.intOldValue());
            assertEquals(message + " new value -", 10, change.intNewValue());
            assertEquals(message + " old value -", Integer.valueOf(0),
                    change.oldValue());
            assertEquals(message + " new value -", Integer.valueOf(10),
                    change.newValue());
        }
    }

    @Test
    public void testModifyLong() {

        final long[] LONGS = longs();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Long Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) strg.compiledStructs
                    .field("longField");
            final LongGlobalField global_long = strg.compiledStructs
                    .field("G_longField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, LONGS[i]);
            }
            strg.storage.write(global_long, 10l);
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, LONGS[i], strg.storage.read(longField));
            }
            assertEquals(message, 10l, strg.storage.read(global_long));
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

            final LongValueChange change = (LongValueChange) changes.next();
            assertEquals(message, global_long, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.longOldValue());
            assertEquals(message + " new value -", 10, change.longNewValue());
            assertEquals(message + " old value -", Long.valueOf(0),
                    change.oldValue());
            assertEquals(message + " new value -", Long.valueOf(10),
                    change.newValue());
        }

    }

    @Test
    public void testModifyShort() {

        final short[] SHORTS = shorts();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Short Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");
            final ShortGlobalField global_short = strg.compiledStructs
                    .field("G_shortField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, SHORTS[i]);
            }
            strg.storage.write(global_short, (short) 10);

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
            final ShortValueChange change = (ShortValueChange) changes.next();
            assertEquals(message, global_short, change.field());
            assertEquals(message + " Struct index -", -1,
                    change.structureIndex());
            assertEquals(message + " old value -", 0, change.shortOldValue());
            assertEquals(message + " new value -", (short) 10,
                    change.shortNewValue());
            assertEquals(message + " old value -", Short.valueOf((short) 0),
                    change.oldValue());
            assertEquals(message + " new value -", Short.valueOf((short) 10),
                    change.newValue());
        }
    }

    @Test
    public void testModifyString() {

        final int[] INTS = ints();

        for (final CompiledStorage strg : COMPILED) {

            final String message = "TestStructGlobalFields.testModifyString() -"
                    + strg.compiler.compilerName();

            final ObjectField<String, ?> stringField = strg.compiledStructs
                    .field("G_String");

            final String testStr = "Str" + INTS[0];
            strg.storage.write(stringField, testStr);
            assertEquals(testStr, strg.storage.read(stringField));
        }
    }

    @Test
    public void testModifyString2() {

        final int[] INTS = ints();

        final Struct tmp = new Struct("GlobalFieldTest", new Struct[] {},
                FACTORY.newIntField("intField"),
                FACTORY.newStringGlobalField("G_String"));

        for (final Compiler cmplr : COMPILERS) {

            final Struct compiled = cmplr.compile(tmp);
            final ObjectField<String, ?> stringField = compiled
                    .field("G_String");

            final Storage strg = cmplr.initStorage(compiled, 10);
            final String testStr = "Str" + INTS[0];
            strg.write(stringField, testStr);
            assertEquals(testStr, strg.read(stringField));
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
