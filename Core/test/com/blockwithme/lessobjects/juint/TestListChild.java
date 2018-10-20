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
import org.junit.BeforeClass;
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
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestListChild extends TestData {

    /** The factory. */
    private CompiledStorage[] COMPILED;

    @BeforeClass
    public static void setUpClass() {
    }

    @Before
    public void setup() {

        Struct tmp1 = new Struct("ListStruct", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("listLong"),
                        FACTORY.newByteField("listByte"),
                        FACTORY.newShortField("listShort"),
                        FACTORY.newFloatField("listFloat"),
                        FACTORY.newDoubleField("listDouble"),
                        FACTORY.newCharField("listChar"),
                        FACTORY.newBooleanField("listBoolean"),
                        FACTORY.newIntField("listInt"),
                        FACTORY.newStringField("listString") });

        tmp1 = tmp1.setList(true);

        final Struct tmp = new Struct("BaseStruct", new Struct[] { tmp1 },
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
            COMPILED[count].compiledStructs = cmplr.compile(tmp);
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
            String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final BooleanField bool1 = (BooleanField) strg.compiledStructs
                    .field("booleanField1");
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
                    .changes(strg.storage.rootStruct());
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
            String message = "Byte Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ByteField byteField = (ByteField) strg.compiledStructs
                    .field("byteField");
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
                    .changes(strg.storage.rootStruct());
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

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {

            String message = "Boolean Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) strg.compiledStructs
                    .field("charField");
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
                    .changes(strg.storage.rootStruct());
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

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {

            String message = "Double Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final DoubleField doubleField = (DoubleField) strg.compiledStructs
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
                    .changes(strg.storage.rootStruct());
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

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Float Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final FloatField floatField = (FloatField) strg.compiledStructs
                    .field("floatField");
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
                    .changes(strg.storage.rootStruct());
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

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {

            String message = "Int Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final IntField intField = (IntField) strg.compiledStructs
                    .field("intField");
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
                    .changes(strg.storage.rootStruct());
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
    public void testModifyListBoolean() {

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {
            final Struct list = strg.compiledStructs.child("ListStruct");
            final BooleanField bool = list.field("listBoolean");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(bool, booleans[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(booleans[j], storage.read(bool));
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (booleans[j]) {
                        final BooleanValueChange change = (BooleanValueChange) changes
                                .next();
                        assertEquals(bool, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals(false, change.booleanOldValue());
                        assertEquals(booleans[j], change.booleanNewValue());
                        assertEquals(false, change.oldValue());
                        assertEquals(booleans[j], change.newValue());
                    }
                }
            }
        }
    }

    @Test
    public void testModifyListByte() {

        final byte[] bytes = bytes();

        for (final CompiledStorage strg : COMPILED) {
            final Struct list = strg.compiledStructs.child("ListStruct");
            final ByteField fByte = list.field("listByte");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(fByte, bytes[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(bytes[j], storage.read(fByte));
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (bytes[j] != 0) {
                        final ByteValueChange change = (ByteValueChange) changes
                                .next();
                        assertEquals(fByte, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals((byte) 0, change.byteOldValue());
                        assertEquals(bytes[j], change.byteNewValue());
                        assertEquals((byte) 0, (byte) change.oldValue());
                        assertEquals(bytes[j], (byte) change.newValue());
                    }
                }
            }
        }
    }

    @Test
    public void testModifyListChar() {

        final char[] chars = chars();

        for (final CompiledStorage strg : COMPILED) {
            final Struct list = strg.compiledStructs.child("ListStruct");
            final CharField fChar = list.field("listChar");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(fChar, chars[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(chars[j], storage.read(fChar));
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (chars[j] != 0) {
                        final CharValueChange change = (CharValueChange) changes
                                .next();
                        assertEquals(fChar, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals((char) 0, change.charOldValue());
                        assertEquals(chars[j], change.charNewValue());
                        assertEquals((char) 0, (char) change.oldValue());
                        assertEquals(chars[j], (char) change.newValue());
                    }
                }
            }
        }
    }

    @Test
    public void testModifyListDouble() {

        final double[] doubles = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final Struct list = strg.compiledStructs.child("ListStruct");
            final DoubleField fDouble = list.field("listDouble");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(fDouble, doubles[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(doubles[j], storage.read(fDouble), DELTA);
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (doubles[j] != 0) {
                        final DoubleValueChange change = (DoubleValueChange) changes
                                .next();
                        assertEquals(fDouble, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals(0D, change.doubleOldValue(), DELTA);
                        assertEquals(doubles[j], change.doubleNewValue(), DELTA);
                        assertEquals(0D, change.oldValue(), DELTA);
                        assertEquals(doubles[j], change.newValue(), DELTA);
                    }
                }
            }
        }
    }

    @Test
    public void testModifyListFloat() {

        final float[] floats = floats();

        for (final CompiledStorage strg : COMPILED) {
            final Struct list = strg.compiledStructs.child("ListStruct");
            final FloatField fFloat = list.field("listFloat");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(fFloat, floats[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(floats[j], storage.read(fFloat), DELTA);
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (floats[j] != 0) {
                        final FloatValueChange change = (FloatValueChange) changes
                                .next();
                        assertEquals(fFloat, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals(0f, change.floatOldValue(), DELTA);
                        assertEquals(floats[j], change.floatNewValue(), DELTA);
                        assertEquals(0f, change.oldValue(), DELTA);
                        assertEquals(floats[j], change.newValue(), DELTA);
                    }
                }
            }
        }
    }

    @Test
    public void testModifyListInt() {

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {
            final Struct list = strg.compiledStructs.child("ListStruct");
            final IntField fInt = list.field("listInt");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(fInt, ints[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(ints[j], storage.read(fInt));
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (ints[j] != 0) {
                        final IntValueChange change = (IntValueChange) changes
                                .next();
                        assertEquals(fInt, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals(0, change.intOldValue());
                        assertEquals(ints[j], change.intNewValue());
                        assertEquals(0, (int) change.oldValue());
                        assertEquals(ints[j], (int) change.newValue());
                    }
                }
            }
        }
    }

    @Test
    public void testModifyListLong() {

        final long[] longs = longs();

        for (final CompiledStorage strg : COMPILED) {

            final Struct list = strg.compiledStructs.child("ListStruct");
            final LongField fLong = list.field("listLong");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage
                        .createOrClearList(list, 10);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    storage.write(fLong, longs[j]);
                }
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                final Storage storage = strg.storage.list(list);
                for (int j = 0; j < _CAPACITY; j++) {
                    storage.selectStructure(j);
                    assertEquals(longs[j], storage.read(fLong));
                }
            }
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                for (int j = 0; j < _CAPACITY; j++) {
                    if (longs[j] != 0) {
                        final LongValueChange change = (LongValueChange) changes
                                .next();
                        assertEquals(fLong, change.field());
                        assertEquals(j, change.structureIndex());
                        assertEquals(0L, change.longOldValue());
                        assertEquals(longs[j], change.longNewValue());
                        assertEquals(0L, (long) change.oldValue());
                        assertEquals(longs[j], (long) change.newValue());
                    }
                }
            }
        }
    }

    @Test
    public void testModifyLong() {

        final long[] longs = longs();

        for (final CompiledStorage strg : COMPILED) {

            String message = "Long Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final LongField longField = (LongField) strg.compiledStructs
                    .field("longField");
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
                    .changes(strg.storage.rootStruct());
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

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final String[] strings = strings();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Short Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");
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
                    .changes(strg.storage.rootStruct());
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
            String message = "String Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ObjectField<String, ?> str1 = (ObjectField<String, ?>) strg.compiledStructs
                    .field("stringField1");
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
                assertEquals(message + " new value -", "test" + ints[count],
                        change.newValue());
            }
        }
    }

    @Test
    public void testSchema() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct compiledStructs = strg.compiledStructs;
            checkSchema(compiledStructs);
        }
    }
}
