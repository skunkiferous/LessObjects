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
package com.blockwithme.lessobjects.juint;

import static com.blockwithme.lessobjects.juint.Constants.COMPILERS;
import static com.blockwithme.lessobjects.juint.Constants.DELTA;
import static com.blockwithme.lessobjects.juint.Constants.FACTORY;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapper;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl;

//CHECKSTYLE IGNORE FOR NEXT 700 LINES
/** The Class TestStorageWrapper. */
@SuppressWarnings("all")
public class TestStorageWrapper extends TestData {

    protected Struct basestruct;

    /** The factory. */
    protected CompiledStorage[] COMPILED;

    final static boolean[] BOOLEANS = booleans();
    final static byte[] BYTES = bytes();
    final static char[] CHARS = chars();
    final static double[] DOUBLES = doubles();
    final static float[] FLOATS = floats();
    final static int[] INTS = ints();
    final static long[] LONGS = longs();
    final static short[] SHORTS = shorts();
    final static String[] STRINGS = strings();

    private void fieldsCheck(final Storage sto, final BooleanField bool1,
            final ByteField byteField, final CharField charField,
            final DoubleField doubleField, final FloatField floatField,
            final IntField intField, final LongField longField,
            final ShortField shortField, final ObjectField<String, ?> str1,
            final boolean doWrite) {

        if (doWrite) {
            for (int i = 0; i < _CAPACITY; i++) {
                sto.selectStructure(i);
                sto.write(bool1, BOOLEANS[i]);
                sto.write(byteField, BYTES[i]);
                sto.write(charField, CHARS[i]);
                sto.write(doubleField, DOUBLES[i]);
                sto.write(floatField, FLOATS[i]);
                sto.write(intField, INTS[i]);
                sto.write(longField, LONGS[i]);
                sto.write(shortField, SHORTS[i]);
                sto.write(str1, "test" + INTS[i]);
            }
        }
        for (int i = 0; i < _CAPACITY; i++) {
            sto.selectStructure(i);
            assertEquals(BOOLEANS[i], sto.read(bool1));
            assertEquals(BYTES[i], sto.read(byteField));
            assertEquals(CHARS[i], sto.read(charField));
            assertEquals(DOUBLES[i], sto.read(doubleField), DELTA);
            assertEquals(FLOATS[i], sto.read(floatField), DELTA);
            assertEquals(INTS[i], sto.read(intField));
            assertEquals(LONGS[i], sto.read(longField));
            assertEquals(SHORTS[i], sto.read(shortField));
            assertEquals("test" + INTS[i], sto.read(str1));
        }
    }

    private void fieldsCheckReverse(final Storage sto,
            final BooleanField bool1, final ByteField byteField,
            final CharField charField, final DoubleField doubleField,
            final FloatField floatField, final IntField intField,
            final LongField longField, final ShortField shortField,
            final ObjectField<String, ?> str1, final boolean doWrite) {
        if (doWrite) {
            for (int i = 1; i <= _CAPACITY; i++) {
                sto.selectStructure(i - 1);
                final int j = _CAPACITY - i;
                sto.write(bool1, BOOLEANS[j]);
                sto.write(byteField, BYTES[j]);
                sto.write(charField, CHARS[j]);
                sto.write(doubleField, DOUBLES[j]);
                sto.write(floatField, FLOATS[j]);
                sto.write(intField, INTS[j]);
                sto.write(longField, LONGS[j]);
                sto.write(shortField, SHORTS[j]);
                sto.write(str1, "test" + INTS[j]);
            }
        }
        for (int i = 1; i <= _CAPACITY; i++) {
            sto.selectStructure(i - 1);
            final int j = _CAPACITY - i;
            assertEquals(BOOLEANS[j], sto.read(bool1));
            assertEquals(BYTES[j], sto.read(byteField));
            assertEquals(CHARS[j], sto.read(charField));
            assertEquals(DOUBLES[j], sto.read(doubleField), DELTA);
            assertEquals(FLOATS[j], sto.read(floatField), DELTA);
            assertEquals(INTS[j], sto.read(intField));
            assertEquals(LONGS[j], sto.read(longField));
            assertEquals(SHORTS[j], sto.read(shortField));
            assertEquals("test" + INTS[j], sto.read(str1));
        }
    }

    private void testAllFields(final String message, final Storage storage,
            final Struct strct, final boolean doWrite, final boolean testClear) {
        final BooleanField bool1 = strct.field("booleanField1");
        final ByteField byteField = strct.field("byteField");
        final CharField charField = strct.field("charField");
        final DoubleField doubleField = strct.field("doubleField");
        final FloatField floatField = strct.field("floatField");
        final IntField intField = strct.field("intField");
        final LongField longField = strct.field("longField");
        final ShortField shortField = strct.field("shortField");
        final ObjectField<String, ?> str1 = strct.field("stringField1");

        if (doWrite) {
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(bool1, BOOLEANS[i]);
                storage.write(byteField, BYTES[i]);
                storage.write(charField, CHARS[i]);
                storage.write(doubleField, DOUBLES[i]);
                storage.write(floatField, FLOATS[i]);
                storage.write(intField, INTS[i]);
                storage.write(longField, LONGS[i]);
                storage.write(shortField, SHORTS[i]);
                storage.write(str1, "test" + INTS[i]);
            }
        }
        for (int i = 0; i < _CAPACITY; i++) {
            storage.selectStructure(i);
            assertEquals(message, BOOLEANS[i], storage.read(bool1));
            assertEquals(message, BYTES[i], storage.read(byteField));
            assertEquals(message, CHARS[i], storage.read(charField));
            assertEquals(message, DOUBLES[i], storage.read(doubleField), DELTA);
            assertEquals(message, FLOATS[i], storage.read(floatField), DELTA);
            assertEquals(message, INTS[i], storage.read(intField));
            assertEquals(message, LONGS[i], storage.read(longField));
            assertEquals(message, SHORTS[i], storage.read(shortField));
            assertEquals(message, "test" + INTS[i], storage.read(str1));
        }
        if (testClear) {
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.clear();
            }
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, false, storage.read(bool1));
                assertEquals(message, (byte) 0, storage.read(byteField));
                assertEquals(message, (char) 0, storage.read(charField));
                assertEquals(message, 0d, storage.read(doubleField), DELTA);
                assertEquals(message, 0f, storage.read(floatField), DELTA);
                assertEquals(message, 0, storage.read(intField));
                assertEquals(message, 0l, storage.read(longField));
                assertEquals(message, (short) 0, storage.read(shortField));
                assertEquals(message, null, storage.read(str1));
            }
        }
    }

    private void testAllFieldsAny(final String message, final Storage storage,
            final Struct strct) {
        final BooleanField bool1 = strct.field("booleanField1");
        final ByteField byteField = strct.field("byteField");
        final CharField charField = strct.field("charField");
        final DoubleField doubleField = strct.field("doubleField");
        final FloatField floatField = strct.field("floatField");
        final IntField intField = strct.field("intField");
        final LongField longField = strct.field("longField");
        final ShortField shortField = strct.field("shortField");
        final ObjectField<String, ?> str1 = strct.field("stringField1");

        for (int i = 0; i < _CAPACITY; i++) {
            storage.selectStructure(i);
            bool1.writeAny(BOOLEANS[i], storage);
            byteField.writeAny(BYTES[i], storage);
            charField.writeAny(CHARS[i], storage);
            doubleField.writeAny(DOUBLES[i], storage);
            floatField.writeAny(FLOATS[i], storage);
            intField.writeAny(INTS[i], storage);
            longField.writeAny(LONGS[i], storage);
            shortField.writeAny(SHORTS[i], storage);
            str1.writeAny("test" + INTS[i], storage);
        }
        for (int i = 0; i < _CAPACITY; i++) {
            storage.selectStructure(i);
            assertEquals(message, BOOLEANS[i], bool1.readAny(storage));
            assertEquals(message, BYTES[i], (byte) byteField.readAny(storage));
            assertEquals(message, CHARS[i], (char) charField.readAny(storage));
            assertEquals(message, DOUBLES[i],
                    (double) doubleField.readAny(storage), DELTA);
            assertEquals(message, FLOATS[i],
                    (float) floatField.readAny(storage), DELTA);
            assertEquals(message, INTS[i], (int) intField.readAny(storage));
            assertEquals(message, LONGS[i], (long) longField.readAny(storage));
            assertEquals(message, SHORTS[i],
                    (short) shortField.readAny(storage));
            assertEquals(message, "test" + INTS[i], str1.readAny(storage));
        }
    }

    protected void doTestChildren(final boolean optional) {
        for (final Compiler compiler : COMPILERS) {
            Struct child = new Struct("TestChild", new Struct[] {},
                    new Field[] { FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), });

            if (optional) {
                child = child.setOptional(true);
            }
            final Struct struct = compiler.compile(new Struct("Test",
                    new Struct[] { child }, new Field[] {
                            FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), }));

            final IntField iF = struct.field("TestChild.tesInt");
            final ByteField bF = struct.field("TestChild.tesByte");
            final BooleanField boolF = struct.field("TestChild.testBoolean");
            final StorageWrapper wrapper = new StorageWrapperImpl(
                    compiler.initStorage(struct, _CAPACITY), 1);

            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                wrapper.write(iF, INTS[i]);
                wrapper.write(bF, BYTES[i]);
                wrapper.write(boolF, BOOLEANS[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                assertEquals(INTS[i], wrapper.read(iF));
                assertEquals(BYTES[i], wrapper.read(bF));
                assertEquals(BOOLEANS[i], wrapper.read(boolF));
            }
            final Storage s = wrapper.writeLayer(1);
            for (int i = 0; i < _CAPACITY; i++) {
                s.selectStructure(i);
                assertEquals(INTS[i], s.read(iF));
                assertEquals(BYTES[i], s.read(bF));
                assertEquals(BOOLEANS[i], s.read(boolF));
            }
        }
    }

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
            COMPILED[count].storage = new StorageWrapperImpl(cmplr.initStorage(
                    COMPILED[count].compiledStructs, _CAPACITY), 1);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testAddLayer() {
        for (final CompiledStorage strg : COMPILED) {
            final StorageWrapper sto = (StorageWrapper) strg.storage;

            final Struct strct = strg.compiledStructs;
            final BooleanField bool1 = strct.field("booleanField1");
            final ByteField byteField = strct.field("byteField");
            final CharField charField = strct.field("charField");
            final DoubleField doubleField = strct.field("doubleField");
            final FloatField floatField = strct.field("floatField");
            final IntField intField = strct.field("intField");
            final LongField longField = strct.field("longField");
            final ShortField shortField = strct.field("shortField");
            final ObjectField<String, ?> str1 = strct.field("stringField1");

            fieldsCheck(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);
            sto.addLayer(2);

            // modify and check in reverse order.
            fieldsCheckReverse(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);
            sto.addLayer(3);

            fieldsCheck(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);
            sto.addLayer(4);

            // modify and check in reverse order.
            fieldsCheckReverse(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);
        }
    }

    @Test
    public void testApplyAllBuffers() {
        final String message = "TestStorageWrapper.testApplyAllBuffers() - ";
        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;
            final Struct strct = strg.compiledStructs;
            testAllFields(message, storage, strct, true, false);
            final Storage actualStorage = ((StorageWrapper) strg.storage)
                    .writeAllLayers();

            testAllFields(message, actualStorage, strct, false, false);
        }
    }

    @Test
    public void testApplyBuffer() {
        final String message = "TestStorageWrapper.testApplyBuffer() - ";
        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;
            final Struct strct = strg.compiledStructs;
            testAllFields(message, storage, strct, true, false);
            final Storage actualStorage = ((StorageWrapper) strg.storage)
                    .writeLayer(1);
            testAllFields(message, actualStorage, strct, false, false);
        }
    }

    @Test
    public void testChildren() {
        doTestChildren(false);
    }

    @Test
    public void testList() {
        for (final Compiler compiler : COMPILERS) {
            Struct child = new Struct("TestChild", new Struct[] {},
                    new Field[] { FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), });
            child = child.setList(true);

            final Struct struct = compiler.compile(new Struct("Test",
                    new Struct[] { child }, new Field[] {
                            FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), }));

            final Struct lc = struct.child("TestChild");
            final IntField iF = lc.field("tesInt");
            final ByteField bF = lc.field("tesByte");
            final BooleanField boolF = lc.field("testBoolean");

            final StorageWrapper wrapper = new StorageWrapperImpl(
                    compiler.initStorage(struct, _CAPACITY), 1);

            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                final Storage s = wrapper.createOrClearList(lc);
                for (int j = 0; j < _CAPACITY; j++) {
                    s.selectStructure(j);
                    s.write(iF, INTS[j]);
                    s.write(bF, BYTES[j]);
                    s.write(boolF, BOOLEANS[j]);
                }
            }
            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                final Storage s = wrapper.list(lc);
                for (int j = 0; j < _CAPACITY; j++) {
                    s.selectStructure(j);
                    assertEquals(INTS[j], s.read(iF));
                    assertEquals(BYTES[j], s.read(bF));
                    assertEquals(BOOLEANS[j], s.read(boolF));
                }
            }
            final Storage strg = wrapper.mergeLayer(1);
            for (int i = 0; i < _CAPACITY; i++) {
                strg.selectStructure(i);
                final Storage s = strg.list(lc);
                for (int j = 0; j < _CAPACITY; j++) {
                    s.selectStructure(j);
                    assertEquals(INTS[j], s.read(iF));
                    assertEquals(BYTES[j], s.read(bF));
                    assertEquals(BOOLEANS[j], s.read(boolF));
                }
            }
        }
    }

    @Test
    public void testList2() {

        for (final Compiler compiler : COMPILERS) {

            Struct child = new Struct("TestChild", new Struct[] {},
                    new Field[] { FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), });
            child = child.setList(true);
            final Struct struct = compiler.compile(new Struct("Test",
                    new Struct[] { child }, new Field[] {
                            FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), }));

            final Struct lc = struct.child("TestChild");
            final IntField iF = lc.field("tesInt");
            final ByteField bF = lc.field("tesByte");
            final BooleanField boolF = lc.field("testBoolean");

            final Storage initStorage = compiler.initStorage(struct, _CAPACITY);
            final StorageWrapper wrapper = new StorageWrapperImpl(initStorage,
                    1);

            for (int i = 0; i < _CAPACITY; i++) {
                initStorage.selectStructure(i);
                final Storage s = initStorage.createOrClearList(lc);
                for (int j = 0; j < _CAPACITY; j++) {
                    s.selectStructure(j);
                    s.write(iF, INTS[j]);
                    s.write(bF, BYTES[j]);
                    s.write(boolF, BOOLEANS[j]);
                }
            }
            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                final Storage s = wrapper.list(lc);
                for (int j = 0; j < _CAPACITY; j++) {
                    s.selectStructure(j);
                    assertEquals(INTS[j], s.read(iF));
                    assertEquals(BYTES[j], s.read(bF));
                    assertEquals(BOOLEANS[j], s.read(boolF));
                }
            }
            final Storage strg = wrapper.mergeLayer(1);
            for (int i = 0; i < _CAPACITY; i++) {
                strg.selectStructure(i);
                final Storage s = strg.list(lc);
                for (int j = 0; j < _CAPACITY; j++) {
                    s.selectStructure(j);
                    assertEquals(INTS[j], s.read(iF));
                    assertEquals(BYTES[j], s.read(bF));
                    assertEquals(BOOLEANS[j], s.read(boolF));
                }
            }
        }
    }

    @Test
    public void testMergeLayer() {
        for (final CompiledStorage strg : COMPILED) {
            final StorageWrapper sto = (StorageWrapper) strg.storage;

            final Struct strct = strg.compiledStructs;
            final BooleanField bool1 = strct.field("booleanField1");
            final ByteField byteField = strct.field("byteField");
            final CharField charField = strct.field("charField");
            final DoubleField doubleField = strct.field("doubleField");
            final FloatField floatField = strct.field("floatField");
            final IntField intField = strct.field("intField");
            final LongField longField = strct.field("longField");
            final ShortField shortField = strct.field("shortField");
            final ObjectField<String, ?> str1 = strct.field("stringField1");

            fieldsCheck(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);
            sto.addLayer(2);

            // modify and check in reverse order.
            fieldsCheckReverse(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);

            sto.addLayer(3);

            fieldsCheck(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);

            sto.addLayer(4);

            // modify and check in reverse order.
            fieldsCheckReverse(sto, bool1, byteField, charField, doubleField,
                    floatField, intField, longField, shortField, str1, true);

            // merge last layer with previous layer.
            StorageWrapper merged = (StorageWrapper) sto.mergeLayer(4);

            // modify and check in reverse order.
            fieldsCheckReverse(merged, bool1, byteField, charField,
                    doubleField, floatField, intField, longField, shortField,
                    str1, false);
            assertEquals(3, merged.currentCycle());

            // merge last layer with previous layer.
            merged = (StorageWrapper) sto.mergeLayer(3);

            // modify and check in reverse order.
            fieldsCheckReverse(merged, bool1, byteField, charField,
                    doubleField, floatField, intField, longField, shortField,
                    str1, false);
            assertEquals(2, merged.currentCycle());

            // merge last layer with previous layer.
            merged = (StorageWrapper) sto.mergeLayer(2);

            // modify and check in reverse order.
            fieldsCheckReverse(merged, bool1, byteField, charField,
                    doubleField, floatField, intField, longField, shortField,
                    str1, false);
            assertEquals(1, merged.currentCycle());

            // merge last layer with previous layer.
            final Storage storage = sto.mergeLayer(1);

            // modify and check in reverse order.
            fieldsCheckReverse(storage, bool1, byteField, charField,
                    doubleField, floatField, intField, longField, shortField,
                    str1, false);
            assertEquals(0, merged.currentCycle());
        }
    }

    @Test
    public void testOptionalChildren() {
        doTestChildren(true);
    }

    @Test
    public void testReplaceStorage() {
        final String message = "TestStorageWrapper.testReplaceStorage() - ";
        for (final CompiledStorage strg : COMPILED) {
            final StorageWrapper storage = (StorageWrapper) strg.storage;
            final Struct strct = strg.compiledStructs;

            testAllFields(message, storage, strct, true, true);
            final Storage newStorage = strg.compiler.initStorage(
                    strg.compiledStructs, _CAPACITY);
            storage.replaceStorage(1, newStorage);
            testAllFields(message, storage, strct, true, true);
        }
    }

    @Test
    public void testSchema() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct compiledStructs = strg.compiledStructs;
            TestData.checkSchema(compiledStructs);
        }
    }

    @Test
    public void testUnion() {
        for (final Compiler compiler : COMPILERS) {
            final Struct union = compiler.compile(new Struct("Test", true,
                    new Struct[] {}, new Field[] {
                            FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), }));

            final IntField iF = union.field("tesInt");
            final ByteField bF = union.field("tesByte");
            final BooleanField boolF = union.field("testBoolean");
            final StorageWrapper wrapper = new StorageWrapperImpl(
                    compiler.initStorage(union, _CAPACITY), 1);

            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                wrapper.selectUnionPosition(iF);
                wrapper.write(iF, INTS[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                assertEquals(INTS[i], wrapper.read(iF));
            }

        }
    }

    @Test
    public void testUnion2() {
        for (final Compiler compiler : COMPILERS) {
            final Struct union = compiler.compile(new Struct("Test", true,
                    new Struct[] {}, new Field[] {
                            FACTORY.newBooleanField("testBoolean"),
                            FACTORY.newIntField("tesInt"),
                            FACTORY.newByteField("tesByte"), }));

            final IntField iF = union.field("tesInt");
            final ByteField bF = union.field("tesByte");
            final BooleanField boolF = union.field("testBoolean");
            final StorageWrapper wrapper = new StorageWrapperImpl(
                    compiler.initStorage(union, _CAPACITY), 1);

            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                wrapper.selectUnionPosition(iF);
                wrapper.write(iF, INTS[i]);
                wrapper.selectUnionPosition(bF);
                wrapper.write(bF, BYTES[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                assertEquals(BYTES[i], wrapper.read(bF));
            }
            final Storage s = wrapper.writeAllLayers();
            for (int i = 0; i < _CAPACITY; i++) {
                wrapper.selectStructure(i);
                assertEquals(BYTES[i], s.read(bF));
            }
        }
    }

    @Test
    public void testWrapperReadWrite() {
        final String message = "TestStorageWrapper.testWrapperReadWrite() - ";
        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;
            final Struct strct = strg.compiledStructs;
            testAllFields(message, storage, strct, true, true);
        }
    }

    @Test
    public void testWrapperReadWriteAny() {
        final String message = "TestStorageWrapper.testWrapperReadWriteAny() - ";
        for (final CompiledStorage strg : COMPILED) {
            final StorageWrapper storage = (StorageWrapper) strg.storage;
            final Struct strct = strg.compiledStructs;
            testAllFieldsAny(message, storage, strct);
            assertEquals(1l, storage.currentCycle());
        }
    }

}
