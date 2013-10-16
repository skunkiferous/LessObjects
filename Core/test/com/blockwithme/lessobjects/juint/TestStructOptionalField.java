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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
import com.blockwithme.lessobjects.fields.optional.FloatOptionalField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
import com.blockwithme.lessobjects.fields.optional.LongOptionalField;
import com.blockwithme.lessobjects.fields.optional.ShortOptionalField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.util.FieldFactoryImpl;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestStructOptionalField {

    private static final int INITIAL_CAPACITY = 10;

    /** The factory. */
    static FieldFactory FACTORY = new FieldFactoryImpl();

    private CompiledStorage[] COMPILED;

    final Random rand = new Random(System.currentTimeMillis());

    @Test
    public void booleanOptional() {

        for (final CompiledStorage strg : COMPILED) {
            final String message = "Boolean Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final boolean b1 = strg.storage
                    .read((BooleanOptionalField) strg.compiledStructs
                            .field("booleanField1"));
            final boolean b2 = strg.storage
                    .read((BooleanOptionalField) strg.compiledStructs
                            .field("booleanField2"));
            assertFalse(message, b1);
            assertFalse(message, b2);
        }
    }

    @Test
    public void booleanReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.modifyBooleanReadWriteAny() -"
                    + strg.compiler.compilerName();
            final BooleanOptionalField<Boolean, ?> bol1 = (BooleanOptionalField<Boolean, ?>) strg.compiledStructs
                    .field("booleanField1");
            final BooleanOptionalField<Boolean, ?> bol2 = (BooleanOptionalField<Boolean, ?>) strg.compiledStructs
                    .field("booleanField2");
            bol1.writeAny(false, strg.storage);
            bol2.writeAny(true, strg.storage);
            assertFalse(message, bol1.readAny(strg.storage));
            assertTrue(message, bol2.readAny(strg.storage));
        }
    }

    @Test
    public void byteOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Byte Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ByteOptionalField byteField = (ByteOptionalField) strg.compiledStructs
                    .field("byteField");
            assertEquals(message, 0, strg.storage.read(byteField));
        }
    }

    @Test
    public void byteReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.byteReadWriteAny() -"
                    + strg.compiler.compilerName();
            final Byte byteValue = 10;
            final ByteOptionalField byteField = (ByteOptionalField) strg.compiledStructs
                    .field("byteField");
            byteField.writeAny(byteValue, strg.storage);
            assertEquals(message, byteValue, byteField.readAny(strg.storage));
        }
    }

    @Test
    public void charOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Char Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final char charValue = 100;
            final CharOptionalField charField = (CharOptionalField) strg.compiledStructs
                    .field("charField");
            strg.storage.write(charField, charValue);
            assertEquals(message, charValue, strg.storage.read(charField));
        }
    }

    @Test
    public void charReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.byteReadWriteAny() -"
                    + strg.compiler.compilerName();
            final Character charValue = 100;
            final CharOptionalField charField = (CharOptionalField) strg.compiledStructs
                    .field("charField");
            charField.writeAny(charValue, strg.storage);
            assertEquals(message, charValue, charField.readAny(strg.storage));
        }
    }

    @Test
    public void doubleOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Double Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final DoubleOptionalField doubleField = (DoubleOptionalField) strg.compiledStructs
                    .field("doubleField");
            assertEquals(message, 0, strg.storage.read(doubleField), DELTA);
        }
    }

    @Test
    public void doubleReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.doubleReadWriteAny() -"
                    + strg.compiler.compilerName();
            final double doubleValue = 100.1d;
            final DoubleOptionalField doubleField = (DoubleOptionalField) strg.compiledStructs
                    .field("doubleField");
            doubleField.writeAny(doubleValue, strg.storage);
            assertEquals(message, doubleValue,
                    (double) doubleField.readAny(strg.storage), DELTA);
        }
    }

    @Test
    public void floatOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Float Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final FloatOptionalField floatField = (FloatOptionalField) strg.compiledStructs
                    .field("floatField");
            assertEquals(message, 0, strg.storage.read(floatField), DELTA);
        }
    }

    @Test
    public void floatReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.floatReadWriteAny() -"
                    + strg.compiler.compilerName();
            final float floatValue = 100.1f;
            final FloatOptionalField floatField = (FloatOptionalField) strg.compiledStructs
                    .field("floatField");

            floatField.writeAny(floatValue, strg.storage);
            assertEquals(message, floatValue,
                    (float) floatField.readAny(strg.storage), DELTA);
        }
    }

    @Test
    public void intOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Int Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final IntOptionalField intField = (IntOptionalField) strg.compiledStructs
                    .field("intField");
            assertEquals(message, 0, strg.storage.read(intField));
        }
    }

    @Test
    public void intReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.intReadWriteAny() -"
                    + strg.compiler.compilerName();
            final Integer intValue = 100;
            final IntOptionalField intField = (IntOptionalField) strg.compiledStructs
                    .field("intField");
            intField.writeAny(intValue, strg.storage);
            assertEquals(message, intValue, intField.readAny(strg.storage));
        }
    }

    @Test
    public void longOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Long Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final LongOptionalField longField = (LongOptionalField) strg.compiledStructs
                    .field("longField");
            assertEquals(message, 0, strg.storage.read(longField));
        }
    }

    @Test
    public void longReadWriteAny() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.longReadWriteAny() -"
                    + strg.compiler.compilerName();
            final Long longValue = 100l;
            final LongOptionalField longField = (LongOptionalField) strg.compiledStructs
                    .field("longField");
            longField.writeAny(longValue, strg.storage);
            assertEquals(message, longValue, longField.readAny(strg.storage));
        }
    }

    @Test
    public void modifyBooleanOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Boolean Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final BooleanOptionalField bol1 = (BooleanOptionalField) strg.compiledStructs
                    .field("booleanField1");
            final BooleanOptionalField bol2 = (BooleanOptionalField) strg.compiledStructs
                    .field("booleanField2");
            strg.storage.write(bol1, false);
            strg.storage.write(bol2, true);
            assertFalse(message, strg.storage.read(bol1));
            assertTrue(message, strg.storage.read(bol2));
        }
    }

    @Test
    public void modifyByteOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Byte Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final byte byteValue = 10;
            final ByteOptionalField byteField = (ByteOptionalField) strg.compiledStructs
                    .field("byteField");
            strg.storage.write(byteField, byteValue);
            assertEquals(message, byteValue, strg.storage.read(byteField));
        }
    }

    @Test
    public void modifyCharOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "char Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final char charValue = 100;
            final CharOptionalField charField = (CharOptionalField) strg.compiledStructs
                    .field("charField");
            strg.storage.write(charField, charValue);
            assertEquals(message, charValue, strg.storage.read(charField));
        }
    }

    @Test
    public void modifyDoubleOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Double Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();

            final double doubleValue = 100.1d;
            final DoubleOptionalField doubleField = (DoubleOptionalField) strg.compiledStructs
                    .field("doubleField");
            strg.storage.write(doubleField, doubleValue);
            assertEquals(message, doubleValue, strg.storage.read(doubleField),
                    DELTA);
        }
    }

    @Test
    public void modifyFloatOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Float Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final float floatValue = 100.1f;
            final FloatOptionalField floatField = (FloatOptionalField) strg.compiledStructs
                    .field("floatField");
            strg.storage.write(floatField, floatValue);
            assertEquals(message, floatValue, strg.storage.read(floatField),
                    DELTA);
        }
    }

    @Test
    public void modifyIntOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Int Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final int intValue = 100;
            final IntOptionalField intField = (IntOptionalField) strg.compiledStructs
                    .field("intField");

            strg.storage.write(intField, intValue);
            assertEquals(message, intValue, strg.storage.read(intField));
        }
    }

    @Test
    public void modifyLongOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Long Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final long longValue = 100l;
            final LongOptionalField longField = (LongOptionalField) strg.compiledStructs
                    .field("longField");
            strg.storage.write(longField, longValue);
            assertEquals(message, longValue, strg.storage.read(longField));
        }
    }

    @Test
    public void modifyShortOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Short Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final short shortValue = 100;
            final ShortOptionalField shortField = (ShortOptionalField) strg.compiledStructs
                    .field("shortField");
            strg.storage.write(shortField, shortValue);
            assertEquals(message, shortValue, strg.storage.read(shortField));
        }
    }

    @Before
    public void setup() {
        final Struct tmp = new Struct("child1_child", new Struct[] {},
                FACTORY.newIntOptional("intField"),
                FACTORY.newLongOptional("longField"),
                FACTORY.newByteOptional("byteField"),
                FACTORY.newCharOptional("charField"),
                FACTORY.newShortOptional("shortField"),
                FACTORY.newFloatOptional("floatField"),
                FACTORY.newDoubleOptional("doubleField"),
                FACTORY.newBooleanOptional("booleanField1"),
                FACTORY.newBooleanOptional("booleanField2"),
                FACTORY.newIntField("intField2"),
                FACTORY.newStringOptional("stringOptional1"));

        final Struct tmp1 = new Struct("child1", new Struct[] { tmp },
                FACTORY.newIntOptional("intField"),
                FACTORY.newLongOptional("longField"),
                FACTORY.newByteOptional("byteField"),
                FACTORY.newCharOptional("charField"),
                FACTORY.newShortOptional("shortField"),
                FACTORY.newFloatOptional("floatField"),
                FACTORY.newDoubleOptional("doubleField"),
                FACTORY.newBooleanOptional("booleanField1"),
                FACTORY.newBooleanOptional("booleanField2"),
                FACTORY.newIntField("intField2"),
                FACTORY.newStringOptional("stringOptional2"));

        final Struct tmp2 = new Struct("child2", new Struct[] {},
                FACTORY.newIntOptional("intField"),
                FACTORY.newLongOptional("longField"),
                FACTORY.newByteOptional("byteField"),
                FACTORY.newCharOptional("charField"),
                FACTORY.newShortOptional("shortField"),
                FACTORY.newFloatOptional("floatField"),
                FACTORY.newDoubleOptional("doubleField"),
                FACTORY.newBooleanOptional("booleanField1"),
                FACTORY.newBooleanOptional("booleanField2"),
                FACTORY.newIntField("intField2"),
                FACTORY.newStringOptional("stringOptional3"));

        final Struct tmp3 = new Struct("base", new Struct[] { tmp1, tmp2 },
                FACTORY.newIntOptional("intField"),
                FACTORY.newLongOptional("longField"),
                FACTORY.newByteOptional("byteField"),
                FACTORY.newCharOptional("charField"),
                FACTORY.newShortOptional("shortField"),
                FACTORY.newFloatOptional("floatField"),
                FACTORY.newDoubleOptional("doubleField"),
                FACTORY.newBooleanOptional("booleanField1"),
                FACTORY.newBooleanOptional("booleanField2"),
                FACTORY.newIntField("intField2"),
                FACTORY.newStringOptional("stringOptional4"));

        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(tmp3);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, INITIAL_CAPACITY);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void shortOptional() {
        for (final CompiledStorage strg : COMPILED) {
            final String message = "Short Optional Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final short shortValue = 100;
            final ShortOptionalField shortField = (ShortOptionalField) strg.compiledStructs
                    .field("shortField");
            strg.storage.write(shortField, shortValue);
            assertEquals(message, shortValue, strg.storage.read(shortField));
        }
    }

    @Test
    public void shortReadWriteAny() {

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestStructOptionalField.shortReadWriteAny() -"
                    + strg.compiler.compilerName();
            final short shortValue = 100;
            final ShortOptionalField shortField = (ShortOptionalField) strg.compiledStructs
                    .field("shortField");
            shortField.writeAny(shortValue, strg.storage);
            assertEquals(message, shortValue,
                    (short) shortField.readAny(strg.storage));
        }
    }

    @Test
    public void testSchema() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct compiledStructs = strg.compiledStructs;
            TestData.checkSchema(compiledStructs);
        }
    }

    public void testStringDefault(final String theName) {
        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;
            final Struct struct = strg.compiledStructs;
            final ObjectField<String, ?> str = struct.field(theName);

            storage.selectStructure(0);
            assertNull(storage.read(str));
            storage.write(str, "test");

            final ActionSet actions = storage.transactionManager().commit();
            assertEquals("test", storage.read(str));
            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            final ValueChange change = changes.next();
            assertEquals(str, change.field());
            assertEquals(0, change.structureIndex());
            assertNull(change.oldValue());
            assertEquals("test", change.newValue());
            storage.clear(str);
            storage.transactionManager().commit();
        }
    }

    @Test
    public void testStringDefaultFeilds() {
        testStringDefault("base.child1.child1_child.stringOptional1");
        testStringDefault("base.child1.stringOptional2");
        testStringDefault("base.child2.stringOptional3");
        testStringDefault("base.stringOptional4");
    }
}
