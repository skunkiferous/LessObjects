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

import javax.annotation.Nullable;

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
import com.blockwithme.lessobjects.util.StructConstants;
import com.blockwithme.prim.LongConverter;

//CHECKSTYLE IGNORE FOR NEXT 700 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestFieldsInsideOptionalChild extends TestData {

    public static class TestSimpleObjectConverter implements
            LongConverter<String> {
        @Override
        public int bits() {
            return StructConstants.LONG_BITS;
        }

        @Override
        public long fromObject(final String theObject) {
            if (theObject != null) {
                return Long.valueOf(theObject);
            }
            return 0;
        }

        @Override
        @Nullable
        public String toObject(final long theValue) {
            return Long.valueOf(theValue).toString();
        }

        @Override
        public Class<String> type() {
            return String.class;
        }
    }

    protected Struct basestruct;

    /** The factory. */
    protected CompiledStorage[] COMPILED;

    private Struct childStruct(final CompiledStorage strg) {
        final Struct compiledStructs = strg.compiledStructs.child("Child");
        return compiledStructs;
    }

    @Before
    public void setup() {
        Struct child = new Struct("Child", new Struct[] {}, new Field<?, ?>[] {
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
                FACTORY.newStringField("stringField1") });
        child = child.setOptional(true);

        basestruct = new Struct("Child", new Struct[] { child },
                new Field<?, ?>[] { FACTORY.newIntField("intField") });

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

        final boolean[] booleans = booleans();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyBoolean() -"
                    + strg.compiler.compilerName();

            final BooleanField bool1 = (BooleanField) childStruct(strg).field(
                    "booleanField1");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, booleans[i]);

            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, booleans[i], strg.storage.read(bool1));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Boolean field -"
                    + bool1.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (booleans[count]) {
                    final BooleanValueChange change = (BooleanValueChange) changes
                            .next();
                    assertEquals(message, bool1, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", false,
                            change.booleanOldValue());
                    assertEquals(message + " new value -", booleans[count],
                            change.booleanNewValue());
                    assertEquals(message + " old value -", false,
                            change.oldValue());
                    assertEquals(message + " new value -", booleans[count],
                            change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyByte() {

        final byte[] bytes = bytes();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyByte() -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) childStruct(strg).field(
                    "byteField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(byteField, bytes[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, bytes[i], strg.storage.read(byteField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Byte field -"
                    + byteField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (bytes[count] != 0) {
                    final ByteValueChange change = (ByteValueChange) changes
                            .next();
                    assertEquals(message, byteField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.byteOldValue());
                    assertEquals(message + " new value -", bytes[count],
                            change.byteNewValue());
                    assertEquals(message + " old value -",
                            Byte.valueOf((byte) 0), change.oldValue());
                    assertEquals(message + " new value -",
                            Byte.valueOf(bytes[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyChar() {

        final char[] chars = chars();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyChar() -"
                    + strg.compiler.compilerName();

            final CharField charField = (CharField) childStruct(strg).field(
                    "charField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, chars[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, chars[i], strg.storage.read(charField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Char field -"
                    + charField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (chars[count] != 0) {
                    final CharValueChange change = (CharValueChange) changes
                            .next();
                    assertEquals(message, charField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.charOldValue());
                    assertEquals(message + " new value -", chars[count],
                            change.charNewValue());
                    assertEquals(message + " old value -",
                            Character.valueOf((char) 0), change.oldValue());
                    assertEquals(message + " new value -",
                            Character.valueOf(chars[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyDouble() {

        final double[] doubles = doubles();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyDouble() -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) childStruct(strg)
                    .field("doubleField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, doubles[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, doubles[i],
                        strg.storage.read(doubleField), DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Double field -"
                    + doubleField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (doubles[count] != 0) {
                    final DoubleValueChange change = (DoubleValueChange) changes
                            .next();
                    assertEquals(message, doubleField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.doubleOldValue(), DELTA);
                    assertEquals(message + " new value -", doubles[count],
                            change.doubleNewValue(), DELTA);
                    assertEquals(message + " old value -", Double.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Double.valueOf(doubles[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyFloat() {

        final float[] floats = floats();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyFloat() -"
                    + strg.compiler.compilerName();
            final FloatField floatField = (FloatField) childStruct(strg).field(
                    "floatField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, floats[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, floats[i], strg.storage.read(floatField),
                        DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Float field -"
                    + floatField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (floats[count] != 0) {
                    final FloatValueChange change = (FloatValueChange) changes
                            .next();
                    assertEquals(message, floatField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.floatOldValue(), DELTA);
                    assertEquals(message + " new value -", floats[count],
                            change.floatNewValue(), DELTA);
                    assertEquals(message + " old value -", Float.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Float.valueOf(floats[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyInt() {

        final int[] ints = ints();

        for (final CompiledStorage strg : COMPILED) {
            String message = "TestFieldsInsideOptionalChild.testModifyInt() -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) childStruct(strg).field(
                    "intField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, ints[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, ints[i], strg.storage.read(intField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Int field -"
                    + intField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (ints[count] != 0) {
                    final IntValueChange change = (IntValueChange) changes
                            .next();
                    assertEquals(message, intField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.intOldValue());
                    assertEquals(message + " new value -", ints[count],
                            change.intNewValue());
                    assertEquals(message + " old value -", Integer.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Integer.valueOf(ints[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyLong() {

        final long[] longs = longs();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyLong() -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) childStruct(strg).field(
                    "longField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, longs[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, longs[i], strg.storage.read(longField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Int field -"
                    + longField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (longs[count] != 0) {
                    final LongValueChange change = (LongValueChange) changes
                            .next();
                    assertEquals(message, longField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.longOldValue());
                    assertEquals(message + " new value -", longs[count],
                            change.longNewValue());
                    assertEquals(message + " old value -", Long.valueOf(0),
                            change.oldValue());
                    assertEquals(message + " new value -",
                            Long.valueOf(longs[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyShort() {

        final short[] shorts = shorts();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyShort() -"
                    + strg.compiler.compilerName();

            final ShortField shortField = (ShortField) childStruct(strg).field(
                    "shortField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, shorts[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, shorts[i], strg.storage.read(shortField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " short field -"
                    + shortField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (shorts[count] != 0) {
                    final ShortValueChange change = (ShortValueChange) changes
                            .next();
                    assertEquals(message, shortField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", 0,
                            change.shortOldValue());
                    assertEquals(message + " new value -", shorts[count],
                            change.shortNewValue());
                    assertEquals(message + " old value -",
                            Short.valueOf((short) 0), change.oldValue());
                    assertEquals(message + " new value -",
                            Short.valueOf(shorts[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyString() {

        final int[] ints = ints();

        for (final CompiledStorage strg : COMPILED) {

            String message = "TestFieldsInsideOptionalChild.testModifyString() -"
                    + strg.compiler.compilerName();

            final ObjectField<String, ?> str1 = (ObjectField<String, ?>) childStruct(
                    strg).field("stringField1");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(str1, "test" + ints[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, "test" + ints[i], strg.storage.read(str1));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(childStruct(strg));
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " String field -"
                    + str1.name();
            for (int count = 0; count < _CAPACITY; count++) {
                final ValueChange change = changes.next();
                assertEquals(message, str1, change.field());
                assertEquals(message + " Struct index -", count,
                        change.structureIndex());
                assertEquals(message + " old value -", null, change.oldValue());
                assertEquals(message + " new value -", "test" + ints[count],
                        change.newValue());
            }
        }
    }

    @Test
    public void testReadWriteAnyBoolean() {

        final boolean[] booleans = booleans();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyBoolean() -"
                    + strg.compiler.compilerName();

            final BooleanField bool1 = (BooleanField) childStruct(strg).field(
                    "booleanField1");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                bool1.writeAny(booleans[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, booleans[i], bool1.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyByte() {

        final byte[] bytes = bytes();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyByte() -"
                    + strg.compiler.compilerName();

            final ByteField byteField = (ByteField) childStruct(strg).field(
                    "byteField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                byteField.writeAny(bytes[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, bytes[i],
                        (byte) byteField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyChar() {

        final char[] chars = chars();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyChar() -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) childStruct(strg).field(
                    "charField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                charField.writeAny(chars[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, chars[i],
                        (char) charField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyDouble() {

        final double[] doubles = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyDouble() -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) childStruct(strg)
                    .field("doubleField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                doubleField.writeAny(doubles[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, doubles[i],
                        (double) doubleField.readAny(strg.storage), DELTA);
            }
        }
    }

    @Test
    public void testReadWriteAnyFloat() {

        final float[] floats = floats();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyFloat() -"
                    + strg.compiler.compilerName();
            final FloatField floatField = (FloatField) childStruct(strg).field(
                    "floatField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                floatField.writeAny(floats[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, floats[i],
                        (float) floatField.readAny(strg.storage), DELTA);
            }
        }
    }

    @Test
    public void testReadWriteAnyInt() {

        final int[] ints = ints();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyInt() -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) childStruct(strg).field(
                    "intField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                intField.writeAny(ints[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, ints[i],
                        (int) intField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyLong() {

        final long[] longs = longs();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyLong() -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) childStruct(strg).field(
                    "longField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                longField.writeAny(longs[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, longs[i],
                        (long) longField.readAny(strg.storage));
            }
        }
    }

    @Test
    public void testReadWriteAnyShort() {

        final short[] shorts = shorts();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestFieldsInsideOptionalChild.testReadWriteAnyShort() -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) childStruct(strg).field(
                    "shortField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                shortField.writeAny(shorts[i], strg.storage);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, shorts[i],
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
