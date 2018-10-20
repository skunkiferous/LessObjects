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

import java.util.Iterator;
import java.util.Random;

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
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 1000 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestCommitRollbackOptionalFields {

    private static final int _CAPACITY = 10;

    private static final boolean[] BOOLEANS = new boolean[_CAPACITY];

    private static final boolean[] BOOLEANS_NEW = new boolean[_CAPACITY];

    private static final byte[] BYTES = new byte[_CAPACITY];

    private static final byte[] BYTES_NEW = new byte[_CAPACITY];

    private static final char[] CHARS = new char[_CAPACITY];

    private static final char[] CHARS_NEW = new char[_CAPACITY];

    private static final double[] DOUBLES = new double[_CAPACITY];

    private static final double[] DOUBLES_NEW = new double[_CAPACITY];

    private static final float[] FLOATS = new float[_CAPACITY];

    private static final float[] FLOATS_NEW = new float[_CAPACITY];

    private static final int[] INTS = new int[_CAPACITY];

    private static final int[] INTS_NEW = new int[_CAPACITY];

    private static final long[] LONGS = new long[_CAPACITY];

    private static final long[] LONGS_NEW = new long[_CAPACITY];

    private static final short[] SHORTS = new short[_CAPACITY];

    private static final short[] SHORTS_NEW = new short[_CAPACITY];

    final static Random rand = new Random(System.currentTimeMillis());

    /** The factory. */
    private CompiledStorage[] COMPILED;

    @BeforeClass
    public static void setUpClass() {
        for (int i = 0; i < _CAPACITY; i++) {
            BYTES[i] = (byte) rand.nextInt();
            INTS[i] = rand.nextInt();
            SHORTS[i] = (short) rand.nextInt();
            LONGS[i] = rand.nextLong();
            DOUBLES[i] = rand.nextDouble();
            CHARS[i] = (char) rand.nextInt();
            FLOATS[i] = rand.nextFloat();
            BOOLEANS[i] = rand.nextBoolean();

            BYTES_NEW[i] = (byte) rand.nextInt();
            INTS_NEW[i] = rand.nextInt();
            SHORTS_NEW[i] = (short) rand.nextInt();
            LONGS_NEW[i] = rand.nextLong();
            DOUBLES_NEW[i] = rand.nextDouble();
            CHARS_NEW[i] = (char) rand.nextInt();
            FLOATS_NEW[i] = rand.nextFloat();
            BOOLEANS_NEW[i] = rand.nextBoolean();
        }
    }

    @Before
    public void setup() {
        final byte[] randomBytes = new byte[1];
        rand.nextBytes(randomBytes);
        final Struct tmp = new Struct("FieldTest", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("realField"),
                        FACTORY.newIntOptional("intField"),
                        FACTORY.newLongOptional("longField"),
                        FACTORY.newByteOptional("byteField"),
                        FACTORY.newShortOptional("shortField"),
                        FACTORY.newFloatOptional("floatField"),
                        FACTORY.newDoubleOptional("doubleField"),
                        FACTORY.newCharOptional("charField"),
                        FACTORY.newBooleanOptional("booleanField1"),
                        FACTORY.newBooleanOptional("booleanField2"),
                        FACTORY.newIntField("intField2") });

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
    public void testApplyChange() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct cStructs = strg.compiledStructs;
            final BooleanField bool1 = cStructs.field("booleanField1");
            final ByteField newByteField = cStructs.field("byteField");
            final CharField newCharField = cStructs.field("charField");
            final DoubleField newDoubleField = cStructs.field("doubleField");
            final FloatField newFloatField = cStructs.field("floatField");
            final IntField newIntField = cStructs.field("intField");
            final ShortField newShortField = cStructs.field("shortField");
            final LongField newLongField = cStructs.field("longField");

            for (int i = 0; i < _CAPACITY; i++) {
                final Storage storage = strg.storage;
                storage.selectStructure(i);

                storage.write(bool1, BOOLEANS[i]);
                storage.write(newByteField, BYTES[i]);
                storage.write(newCharField, CHARS[i]);
                storage.write(newDoubleField, DOUBLES[i]);
                storage.write(newFloatField, FLOATS[i]);
                storage.write(newIntField, INTS[i]);
                storage.write(newLongField, LONGS[i]);
                storage.write(newShortField, SHORTS[i]);

                ActionSet actions = storage.transactionManager().commit();
                storage.clear();
                storage.transactionManager().commit();

                final Iterator<ValueChange<?>> changes = actions
                        .changeRecords().changes(strg.storage.rootStruct());

                if (BOOLEANS[i]) {
                    final BooleanValueChange booleanchange = (BooleanValueChange) changes
                            .next();
                    booleanchange.applyChange(storage);
                }

                if (BYTES[i] != 0) {
                    final ByteValueChange bytechange = (ByteValueChange) changes
                            .next();
                    bytechange.applyChange(storage);
                }
                if (CHARS[i] != 0) {
                    final CharValueChange charchange = (CharValueChange) changes
                            .next();
                    charchange.applyChange(storage);
                }
                if (DOUBLES[i] != 0) {
                    final DoubleValueChange doublechange = (DoubleValueChange) changes
                            .next();
                    doublechange.applyChange(storage);
                }
                if (FLOATS[i] != 0) {
                    final FloatValueChange floatchange = (FloatValueChange) changes
                            .next();
                    floatchange.applyChange(storage);
                }
                if (INTS[i] != 0) {
                    final IntValueChange intChange = (IntValueChange) changes
                            .next();
                    intChange.applyChange(storage);
                }
                if (LONGS[i] != 0) {
                    final LongValueChange longchange = (LongValueChange) changes
                            .next();
                    longchange.applyChange(storage);
                }
                if (SHORTS[i] != 0) {
                    final ShortValueChange shortchange = (ShortValueChange) changes
                            .next();
                    shortchange.applyChange(storage);
                }

                actions = storage.transactionManager().commit();
                assertEquals(BOOLEANS[i], storage.read(bool1));
                assertEquals(BYTES[i], storage.read(newByteField));
                assertEquals(CHARS[i], storage.read(newCharField));
                assertEquals(DOUBLES[i], storage.read(newDoubleField), DELTA);
                assertEquals(FLOATS[i], storage.read(newFloatField), DELTA);
                assertEquals(INTS[i], storage.read(newIntField));
                assertEquals(LONGS[i], storage.read(newLongField));
                assertEquals(SHORTS[i], storage.read(newShortField));
            }
        }
    }

    @Test
    public void testClear() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct cStructs = strg.compiledStructs;
            final BooleanField bool1 = cStructs.field("booleanField1");
            final ByteField newByteField = cStructs.field("byteField");
            final CharField newCharField = cStructs.field("charField");
            final DoubleField newDoubleField = cStructs.field("doubleField");
            final FloatField newFloatField = cStructs.field("floatField");
            final IntField newIntField = cStructs.field("intField");
            final ShortField newShortField = cStructs.field("shortField");
            final LongField newLongField = cStructs.field("longField");

            final Storage storage = strg.storage;
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(bool1, BOOLEANS[i]);
                storage.write(newByteField, BYTES[i]);
                storage.write(newCharField, CHARS[i]);
                storage.write(newDoubleField, DOUBLES[i]);
                storage.write(newFloatField, FLOATS[i]);
                storage.write(newIntField, INTS[i]);
                storage.write(newShortField, SHORTS[i]);
                storage.write(newLongField, LONGS[i]);
            }
            ActionSet actions = storage.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(BOOLEANS[i], storage.read(bool1));
                assertEquals(BYTES[i], storage.read(newByteField));
                assertEquals(CHARS[i], storage.read(newCharField));
                assertEquals(DOUBLES[i], storage.read(newDoubleField), DELTA);
                assertEquals(FLOATS[i], storage.read(newFloatField), DELTA);
                assertEquals(INTS[i], storage.read(newIntField));
                assertEquals(LONGS[i], storage.read(newLongField));
                assertEquals(SHORTS[i], storage.read(newShortField));
            }

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.clear();
                actions = storage.transactionManager().commit();

                final Iterator<ValueChange<?>> changes = actions
                        .changeRecords().changes(strg.storage.rootStruct());

                if (BOOLEANS[i]) {
                    final BooleanValueChange booleanchange = (BooleanValueChange) changes
                            .next();
                    assertEquals(bool1, booleanchange.field());
                    assertEquals(i, booleanchange.structureIndex());
                    assertEquals(true, booleanchange.booleanOldValue());
                    assertEquals(false, booleanchange.booleanNewValue());
                    assertEquals(true, booleanchange.oldValue());
                    assertEquals(false, booleanchange.newValue());
                }

                if (BYTES[i] != 0) {
                    final ByteValueChange bytechange = (ByteValueChange) changes
                            .next();
                    assertEquals(newByteField, bytechange.field());
                    assertEquals(i, bytechange.structureIndex());
                    assertEquals(BYTES[i], bytechange.byteOldValue());
                    assertEquals(0, bytechange.byteNewValue());
                    assertEquals(Byte.valueOf(BYTES[i]), bytechange.oldValue());
                    assertEquals(Byte.valueOf((byte) 0), bytechange.newValue());
                }
                if (CHARS[i] != 0) {
                    final CharValueChange charchange = (CharValueChange) changes
                            .next();
                    assertEquals(newCharField, charchange.field());
                    assertEquals(i, charchange.structureIndex());
                    assertEquals(CHARS[i], charchange.charOldValue());
                    assertEquals(0, charchange.charNewValue());
                    assertEquals(Character.valueOf(CHARS[i]),
                            charchange.oldValue());
                    assertEquals(Character.valueOf((char) 0),
                            charchange.newValue());
                }
                if (DOUBLES[i] != 0) {
                    final DoubleValueChange doublechange = (DoubleValueChange) changes
                            .next();
                    assertEquals(newDoubleField, doublechange.field());
                    assertEquals(i, doublechange.structureIndex());
                    assertEquals(DOUBLES[i], doublechange.doubleOldValue(),
                            DELTA);
                    assertEquals(0, doublechange.doubleNewValue(), DELTA);
                    assertEquals(Double.valueOf(DOUBLES[i]),
                            doublechange.oldValue());
                    assertEquals(Double.valueOf(0d), doublechange.newValue());
                }
                if (FLOATS[i] != 0) {
                    final FloatValueChange floatchange = (FloatValueChange) changes
                            .next();
                    assertEquals(newFloatField, floatchange.field());
                    assertEquals(i, floatchange.structureIndex());
                    assertEquals(FLOATS[i], floatchange.floatOldValue(), DELTA);
                    assertEquals(0, floatchange.floatNewValue(), DELTA);
                    assertEquals(Float.valueOf(FLOATS[i]),
                            floatchange.oldValue());
                    assertEquals(Float.valueOf(0f), floatchange.newValue());
                }
                if (INTS[i] != 0) {
                    final IntValueChange intChange = (IntValueChange) changes
                            .next();
                    assertEquals(newIntField, intChange.field());
                    assertEquals(i, intChange.structureIndex());
                    assertEquals(INTS[i], intChange.intOldValue());
                    assertEquals(0, intChange.intNewValue());
                    assertEquals(Integer.valueOf(INTS[i]), intChange.oldValue());
                    assertEquals(Integer.valueOf(0), intChange.newValue());
                }
                if (LONGS[i] != 0) {
                    final LongValueChange longchange = (LongValueChange) changes
                            .next();
                    assertEquals(newLongField, longchange.field());
                    assertEquals(i, longchange.structureIndex());
                    assertEquals(LONGS[i], longchange.longOldValue());
                    assertEquals(0, longchange.longNewValue());
                    assertEquals(Long.valueOf(LONGS[i]), longchange.oldValue());
                    assertEquals(Long.valueOf(0l), longchange.newValue());
                }
                if (SHORTS[i] != 0) {
                    final ShortValueChange shortchange = (ShortValueChange) changes
                            .next();
                    assertEquals(newShortField, shortchange.field());
                    assertEquals(i, shortchange.structureIndex());
                    assertEquals(SHORTS[i], shortchange.shortOldValue());
                    assertEquals(0, shortchange.shortNewValue());
                    assertEquals(Short.valueOf(SHORTS[i]),
                            shortchange.oldValue());
                    assertEquals(Short.valueOf((short) 0),
                            shortchange.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyBoolean() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, BOOLEANS_NEW[i]);
            }
            // now rollback
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed Asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Boolean field -"
                    + bool1.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], strg.storage.read(bool1));
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed Asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " Boolean field -"
                    + bool1.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (BOOLEANS[count] != BOOLEANS_NEW[count]) {
                    final BooleanValueChange change = (BooleanValueChange) changes
                            .next();
                    assertEquals(message, bool1, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", BOOLEANS[count],
                            change.booleanOldValue());
                    assertEquals(message + " new value -", BOOLEANS_NEW[count],
                            change.booleanNewValue());
                    assertEquals(message + " old value -", BOOLEANS[count],
                            change.oldValue());
                    assertEquals(message + " new value -", BOOLEANS_NEW[count],
                            change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyByte() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(byteField, BYTES_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Byte field -"
                    + byteField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, BYTES[i], strg.storage.read(byteField));
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + byteField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (BYTES[count] != BYTES_NEW[count]) {
                    final ByteValueChange change = (ByteValueChange) changes
                            .next();
                    assertEquals(message, byteField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", BYTES[count],
                            change.byteOldValue());
                    assertEquals(message + " new value -", BYTES_NEW[count],
                            change.byteNewValue());
                    assertEquals(message + " old value -",
                            Byte.valueOf(BYTES[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Byte.valueOf(BYTES_NEW[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyChar() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, CHARS_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Char field -"
                    + charField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, CHARS[i], strg.storage.read(charField));
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + charField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (CHARS[count] != CHARS_NEW[count]) {
                    final CharValueChange change = (CharValueChange) changes
                            .next();
                    assertEquals(message, charField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", CHARS[count],
                            change.charOldValue());
                    assertEquals(message + " new value -", CHARS_NEW[count],
                            change.charNewValue());
                    assertEquals(message + " old value -",
                            Character.valueOf(CHARS[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Character.valueOf(CHARS_NEW[count]),
                            change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyDouble() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, DOUBLES_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Double field -"
                    + doubleField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, DOUBLES[i],
                        strg.storage.read(doubleField), DELTA);
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + doubleField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (DOUBLES[count] != DOUBLES_NEW[count]) {
                    final DoubleValueChange change = (DoubleValueChange) changes
                            .next();
                    assertEquals(message, doubleField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", DOUBLES[count],
                            change.doubleOldValue(), DELTA);
                    assertEquals(message + " new value -", DOUBLES_NEW[count],
                            change.doubleNewValue(), DELTA);
                    assertEquals(message + " old value -",
                            Double.valueOf(DOUBLES[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Double.valueOf(DOUBLES_NEW[count]),
                            change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyFloat() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, FLOATS_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Double field -"
                    + floatField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, FLOATS[i], strg.storage.read(floatField),
                        DELTA);
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + floatField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (FLOATS[count] != FLOATS_NEW[count]) {
                    final FloatValueChange change = (FloatValueChange) changes
                            .next();
                    assertEquals(message, floatField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", FLOATS[count],
                            change.floatOldValue(), DELTA);
                    assertEquals(message + " new value -", FLOATS_NEW[count],
                            change.floatNewValue(), DELTA);
                    assertEquals(message + " old value -",
                            Float.valueOf(FLOATS[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Float.valueOf(FLOATS_NEW[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyInt() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, INTS_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Int field -"
                    + intField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, INTS[i], strg.storage.read(intField));
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + intField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (INTS[count] != INTS_NEW[count]) {
                    final IntValueChange change = (IntValueChange) changes
                            .next();
                    assertEquals(message, intField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", INTS[count],
                            change.intOldValue());
                    assertEquals(message + " new value -", INTS_NEW[count],
                            change.intNewValue());
                    assertEquals(message + " old value -",
                            Integer.valueOf(INTS[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Integer.valueOf(INTS_NEW[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyLong() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, LONGS_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Long field -"
                    + longField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, LONGS[i], strg.storage.read(longField));
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + longField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (LONGS[count] != LONGS_NEW[count]) {
                    final LongValueChange change = (LongValueChange) changes
                            .next();
                    assertEquals(message, longField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", LONGS[count],
                            change.longOldValue());
                    assertEquals(message + " new value -", LONGS_NEW[count],
                            change.longNewValue());
                    assertEquals(message + " old value -",
                            Long.valueOf(LONGS[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Long.valueOf(LONGS_NEW[count]), change.newValue());
                }
            }
        }
    }

    @Test
    public void testModifyShort() {
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

            Iterator<ValueChange<?>> changes = actions.changeRecords().changes(
                    strg.storage.rootStruct());
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

            // Modify values and set it from _new array.
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, SHORTS_NEW[i]);
            }
            // now rollback the transaction
            final ActionSet roll_actions = strg.storage.transactionManager()
                    .rollback();
            // values should match with old array values.
            message = "Failed asserting rollback values to old values for compiler -"
                    + strg.compiler.compilerName()
                    + " Short field -"
                    + shortField.name();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, SHORTS[i], strg.storage.read(shortField));
            }

            // Results should have 'changes' where old value is not equal to
            // new.
            changes = roll_actions.changeRecords().changes(
                    strg.storage.rootStruct());
            message = "Failed asserting rollback actions for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + shortField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (SHORTS[count] != SHORTS_NEW[count]) {
                    final ShortValueChange change = (ShortValueChange) changes
                            .next();
                    assertEquals(message, shortField, change.field());
                    assertEquals(message + " Struct index -", count,
                            change.structureIndex());
                    assertEquals(message + " old value -", SHORTS[count],
                            change.shortOldValue());
                    assertEquals(message + " new value -", SHORTS_NEW[count],
                            change.shortNewValue());
                    assertEquals(message + " old value -",
                            Short.valueOf(SHORTS[count]), change.oldValue());
                    assertEquals(message + " new value -",
                            Short.valueOf(SHORTS_NEW[count]), change.newValue());
                }
            }
        }
    }
}
