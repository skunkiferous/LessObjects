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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.ValueChangeListener;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.ObjectValueChange;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
import com.blockwithme.lessobjects.fields.optional.FloatOptionalField;
import com.blockwithme.lessobjects.juint.struct.TestListener;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.ChangeListenerSupport;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 800 LINES
/**
 * Test class for testing the Change listeners.
 *
 * @author tarung
 */
@SuppressWarnings({ "PMD", "all" })
public class TestListenersOtherFields extends TestData {

    private static final Character ZERO_C = Character.valueOf((char) 0);

    private static final Byte ZERO_B = Byte.valueOf((byte) 0);

    private static final Double ZERO_D = Double.valueOf(0);

    private static final Float ZERO_F = Float.valueOf(0);

    @Before
    public void setup() {

        final FieldFactory f = FACTORY;

        ByteOptionalField byteField = f.newByteOptional("byteField");
        CharOptionalField charField = f.newCharOptional("charField");
        DoubleOptionalField doubleField = f.newDoubleOptional("doubleField");
        FloatOptionalField floatField = f.newFloatOptional("floatField");
        ObjectField<String, ?> strField = f.newStringField("strField");

        final Struct tmp = new Struct("OptionalFieldTest", new Struct[] {},
                new Field<?, ?>[] { byteField, floatField, doubleField,
                        charField, f.newIntField("intField2"), strField });

        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];

        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(tmp);
            final Struct cStructs = COMPILED[count].compiledStructs;
            COMPILED[count].storage = cmplr.initStorage(cStructs, _CAPACITY);

            COMPILED[count].compiler = cmplr;

            final ChangeListenerSupport cSupport = COMPILED[count].storage
                    .changeListenerSupport();

            byteField = cStructs.field("byteField");
            cSupport.addListener(byteField, new TestListener());

            charField = cStructs.field("charField");
            cSupport.addListener(charField, new TestListener());

            doubleField = cStructs.field("doubleField");
            cSupport.addListener(doubleField, new TestListener());

            floatField = cStructs.field("floatField");
            cSupport.addListener(floatField, new TestListener());

            strField = cStructs.field("strField");
            cSupport.addListener(strField, new TestListener());

            cSupport.addListener(new ValueChangeListener() {

                @Override
                public void onChange(final ChangeInfo theChange,
                        final Storage theSource,
                        final List<Object> theResultEvents) {

                    final String response = "global" + theChange.field().name()
                            + theSource.getSelectedStructure();
                    theResultEvents.add(response);
                }
            });
            count++;
        }
    }

    @Test
    public void testModifyByte() {

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
            final String message = "TestOptionalFieldsChangeListeners.testModifyByte() -"
                    + strg.compiler.compilerName();

            final Struct cStructs = strg.compiledStructs;
            final ByteOptionalField byteField = cStructs.field("byteField");

            final Storage storage = strg.storage;

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(byteField, bytes[i]);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, bytes[i], storage.read(byteField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                if (bytes[count] != (byte) 0) {
                    final ByteValueChange change = (ByteValueChange) changes
                            .next();
                    assertEquals(message, byteField, change.field());
                    assertEquals(message, count, change.structureIndex());
                    assertEquals(message, 0, change.byteOldValue());
                    assertEquals(message, bytes[count], change.byteNewValue());
                    assertEquals(message, ZERO_B, change.oldValue());
                    assertEquals(message, Byte.valueOf(bytes[count]),
                            change.newValue());
                }
            }
            final List<Object> events = actions.events();

            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (bytes[count] != 0) {
                    final String expected = byteField.name() + count;
                    final String expected2 = "global" + byteField.name()
                            + count;

                    final Object actual = events.get(changeCount++);
                    final Object actual2 = events.get(changeCount++);
                    assertEquals(message, expected, actual);
                    assertEquals(message, expected2, actual2);
                }
            }
        }
    }

    @Test
    public void testModifyChar() {

        final char[] chars = chars();

        for (final CompiledStorage strg : COMPILED) {
            String message = "TestOptionalFieldsChangeListeners.testModifyChar() -"
                    + strg.compiler.compilerName();

            final Struct cStructs = strg.compiledStructs;
            final CharOptionalField charField = cStructs.field("charField");

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
                    assertEquals(message, count, change.structureIndex());
                    assertEquals(message, 0, change.charOldValue());
                    assertEquals(message, chars[count], change.charNewValue());
                    assertEquals(message, ZERO_C, change.oldValue());
                    assertEquals(message, Character.valueOf(chars[count]),
                            change.newValue());
                }
            }

            final List<Object> events = actions.events();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (chars[count] != 0) {
                    final String expected = charField.name() + count;
                    final String expected2 = "global" + charField.name()
                            + count;

                    final Object actual = events.get(changeCount++);
                    final Object actual2 = events.get(changeCount++);

                    assertEquals(message, expected, actual);
                    assertEquals(message, expected2, actual2);
                }
            }
        }
    }

    @Test
    public void testModifyDouble() {

        final double[] doubles = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestOptionalFieldsChangeListeners.testModifyDouble() -"
                    + strg.compiler.compilerName();

            final DoubleOptionalField doubleField = strg.compiledStructs
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

            for (int count = 0; count < _CAPACITY; count++) {
                if (doubles[count] != 0) {
                    final DoubleValueChange change = (DoubleValueChange) changes
                            .next();
                    assertEquals(message, doubleField, change.field());
                    assertEquals(message, count, change.structureIndex());
                    assertEquals(message, 0d, change.doubleOldValue(), DELTA);
                    assertEquals(message, doubles[count],
                            change.doubleNewValue(), DELTA);
                    assertEquals(message, ZERO_D, change.oldValue());
                    assertEquals(message, Double.valueOf(doubles[count]),
                            change.newValue());
                }
            }

            final List<Object> events = actions.events();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (doubles[count] != 0) {
                    final String expected = doubleField.name() + count;
                    final String expected2 = "global" + doubleField.name()
                            + count;

                    final Object actual = events.get(changeCount++);
                    final Object actual2 = events.get(changeCount++);

                    assertEquals(message, expected, actual);
                    assertEquals(message, expected2, actual2);
                }
            }
        }
    }

    @Test
    public void testModifyFloat() {

        final float[] floats = floats();

        for (final CompiledStorage strg : COMPILED) {

            final String message = "TestOptionalFieldsChangeListeners.testModifyFloat() -"
                    + strg.compiler.compilerName();

            final FloatOptionalField floatField = strg.compiledStructs
                    .field("floatField");

            final Storage storage = strg.storage;

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(floatField, floats[i]);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, floats[i], storage.read(floatField),
                        DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                if (floats[count] != 0) {
                    final FloatValueChange change = (FloatValueChange) changes
                            .next();
                    assertEquals(message, floatField, change.field());
                    assertEquals(message, count, change.structureIndex());
                    assertEquals(message, 0f, change.floatOldValue(), DELTA);
                    assertEquals(message, floats[count],
                            change.floatNewValue(), DELTA);
                    assertEquals(message, ZERO_F, change.oldValue());
                    assertEquals(message, Float.valueOf(floats[count]),
                            change.newValue());

                }
            }
            final List<Object> events = actions.events();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (floats[count] != 0f) {
                    final String expected = floatField.name() + count;
                    final String expected2 = "global" + floatField.name()
                            + count;

                    final Object actual = events.get(changeCount++);
                    final Object actual2 = events.get(changeCount++);

                    assertEquals(message, expected, actual);
                    assertEquals(message, expected2, actual2);
                }
            }
        }
    }

    @Test
    public void testModifyString() {

        final byte[] bytes = bytes();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestOptionalFieldsChangeListeners.testModifyString() -"
                    + strg.compiler.compilerName();

            final Struct cStructs = strg.compiledStructs;
            final ObjectField<String, ?> strField = cStructs.field("strField");

            final Storage storage = strg.storage;

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(strField, "Str" + bytes[i]);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, "Str" + bytes[i], storage.read(strField));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                final ObjectValueChange change = (ObjectValueChange) changes
                        .next();
                assertEquals(message, strField, change.field());
                assertEquals(message, count, change.structureIndex());
                assertEquals(message, null, change.oldValue());
                assertEquals(message, "Str" + bytes[count], change.newValue());
            }
            final List<Object> events = actions.events();

            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                final String expected = strField.name() + count;
                final String expected2 = "global" + strField.name() + count;

                final Object actual = events.get(changeCount++);
                final Object actual2 = events.get(changeCount++);

                assertEquals(message, expected, actual);
                assertEquals(message, expected2, actual2);
            }
        }
    }
}
