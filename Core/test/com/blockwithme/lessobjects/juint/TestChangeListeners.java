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
import com.blockwithme.lessobjects.TransactionListener;
import com.blockwithme.lessobjects.ValueChangeListener;
import com.blockwithme.lessobjects.beans.BooleanValueChange;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.IntValueChange;
import com.blockwithme.lessobjects.beans.LongValueChange;
import com.blockwithme.lessobjects.beans.ObjectValueChange;
import com.blockwithme.lessobjects.beans.ShortValueChange;
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
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.ChangeListenerSupport;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 2000 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestChangeListeners extends TestData {

    public static class TestListener implements ValueChangeListener,
            TransactionListener {
        /** {@inheritDoc} */
        @Override
        public void onChange(final ChangeInfo theChange,
                final Storage theSource, final List<Object> theResultEvents) {
            final String name = theChange.field().name();
            final String response = name + theSource.getSelectedStructure();

            theResultEvents.add(response);
            if (name.equals("booleanField1") || name.equals("booleanOptional")) {
                theResultEvents.add("old: " + theChange.oldBooleanValue());
                theResultEvents.add("new: " + theChange.newBooleanValue());
            } else if (name.equals("byteField") || name.equals("byteOptional")) {
                theResultEvents.add("old: " + theChange.oldByteValue());
                theResultEvents.add("new: " + theChange.newByteValue());
            } else if (name.equals("charField") || name.equals("charOptional")) {
                theResultEvents.add("old: " + theChange.oldCharValue());
                theResultEvents.add("new: " + theChange.newCharValue());
            } else if (name.equals("doubleField")
                    || name.equals("doubleOptional")) {
                theResultEvents.add("old: " + theChange.oldDoubleValue());
                theResultEvents.add("new: " + theChange.newDoubleValue());
            } else if (name.equals("floatField")
                    || name.equals("floatOptional")) {
                theResultEvents.add("old: " + theChange.oldFloatValue());
                theResultEvents.add("new: " + theChange.newFloatValue());
            } else if (name.equals("intField") || name.equals("intOptional")) {
                theResultEvents.add("old: " + theChange.oldIntValue());
                theResultEvents.add("new: " + theChange.newIntValue());
            } else if (name.equals("longField") || name.equals("longOptional")) {
                theResultEvents.add("old: " + theChange.oldLongValue());
                theResultEvents.add("new: " + theChange.newLongValue());
            } else if (name.equals("shortField")
                    || name.equals("shortOptional")) {
                theResultEvents.add("old: " + theChange.oldShortValue());
                theResultEvents.add("new: " + theChange.newShortValue());
            } else if (name.equals("stringField")) {
                theResultEvents.add("old: " + theChange.oldObjectValue());
                theResultEvents.add("new: " + theChange.newObjectValue());
            }
        }

        /** {@inheritDoc} */
        @Override
        public void postCommit(final ActionSet theActions,
                final Storage theSource, final boolean isCommit) {
            // NOP
        }

        /** {@inheritDoc} */
        @Override
        public void preCommit(final ActionSet theActions,
                final Storage theSource, final boolean isCommit) {
            // NOP
        }
    }

    public static class TestListener2 implements ValueChangeListener {
        /** {@inheritDoc} */
        @Override
        public void onChange(final ChangeInfo theChange,
                final Storage theSource, final List<Object> theResultEvents) {
            // This should throw an exception.
            theSource.clear();
        }
    }

    @Before
    public void setup() {

        final FieldFactory ff = FACTORY;

        BooleanField bField1 = ff.newBooleanField("booleanField1");
        ByteField byteField = ff.newByteField("byteField");
        CharField charField = ff.newCharField("charField");
        DoubleField doubleField = ff.newDoubleField("doubleField");
        FloatField floatField = ff.newFloatField("floatField");
        ShortField shortField = ff.newShortField("shortField");
        LongField LongField = ff.newLongField("longField");
        IntField newFullIntField = ff.newIntField("intField");
        BooleanOptionalField bOptional = ff.newBooleanOptional("booleanOptional");
        ByteOptionalField byteOptional = ff.newByteOptional("byteOptional");
        CharOptionalField cOptional = ff.newCharOptional("charOptional");
        DoubleOptionalField dOptional = ff.newDoubleOptional("doubleOptional");
        FloatOptionalField fOptional = ff.newFloatOptional("floatOptional");
        ShortOptionalField sOptional = ff.newShortOptional("shortOptional");
        LongOptionalField lOptional = ff.newLongOptional("longOptional");
        IntOptionalField iOptional = ff.newIntOptional("intOptional");

        ObjectField<String, ?> string = ff.newStringField("stringField");
        final Struct tmp = new Struct("ChangelistenerTest", new Struct[] {},
                new Field<?, ?>[] { newFullIntField, LongField, byteField,
                        shortField, floatField, doubleField, charField,
                        bField1, bOptional, byteOptional, cOptional, dOptional,
                        fOptional, sOptional, lOptional, iOptional,
                        FACTORY.newBooleanField("booleanField2"),
                        FACTORY.newIntField("intField2"), string });

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

            bField1 = cStructs.field("booleanField1");
            byteField = cStructs.field("byteField");
            charField = cStructs.field("charField");
            doubleField = cStructs.field("doubleField");
            floatField = cStructs.field("floatField");
            newFullIntField = cStructs.field("intField");
            LongField = cStructs.field("longField");
            shortField = cStructs.field("shortField");
            bOptional = cStructs.field("booleanOptional");
            byteOptional = cStructs.field("byteOptional");
            cOptional = cStructs.field("charOptional");
            dOptional = cStructs.field("doubleOptional");
            fOptional = cStructs.field("floatOptional");
            sOptional = cStructs.field("shortOptional");
            lOptional = cStructs.field("longOptional");
            iOptional = cStructs.field("intOptional");
            string = cStructs.field("stringField");

            cSupport.addListener(bField1, new TestListener());
            cSupport.addListener(byteField, new TestListener());
            cSupport.addListener(charField, new TestListener());
            cSupport.addListener(doubleField, new TestListener());
            cSupport.addListener(floatField, new TestListener());
            cSupport.addListener(newFullIntField, new TestListener());
            cSupport.addListener(LongField, new TestListener());
            cSupport.addListener(shortField, new TestListener());
            cSupport.addListener(bOptional, new TestListener());
            cSupport.addListener(byteOptional, new TestListener());
            cSupport.addListener(cOptional, new TestListener());
            cSupport.addListener(dOptional, new TestListener());
            cSupport.addListener(fOptional, new TestListener());
            cSupport.addListener(sOptional, new TestListener());
            cSupport.addListener(lOptional, new TestListener());
            cSupport.addListener(iOptional, new TestListener());
            cSupport.addListener(string, new TestListener());
            count++;
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testAddRemoveListeners() {

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
            final Struct cS = strg.compiledStructs;

            final BooleanField bField1 = cS.field("booleanField1");
            final ByteField byteField = cS.field("byteField");
            final CharField charField = cS.field("charField");
            final DoubleField doubleField = cS.field("doubleField");
            final FloatField floatField = cS.field("floatField");
            final IntField newFullIntField = cS.field("intField");
            final ShortField shortField = cS.field("shortField");
            final LongField LongField = cS.field("longField");

            final BooleanOptionalField bOptional = cS.field("booleanOptional");
            final ByteOptionalField byteOptional = cS.field("byteOptional");
            final CharOptionalField cOptional = cS.field("charOptional");
            final DoubleOptionalField dOptional = cS.field("doubleOptional");
            final FloatOptionalField fOptional = cS.field("floatOptional");
            final ShortOptionalField sOptional = cS.field("shortOptional");
            final LongOptionalField lOptional = cS.field("longOptional");

            final Storage storage = strg.storage;
            final ChangeListenerSupport cSupport = strg.storage
                    .changeListenerSupport();

            final TestListener globalListner = new TestListener();

            final TestListener listener1 = new TestListener();
            final TestListener listener2 = new TestListener();
            final TestListener listener3 = new TestListener();
            final TestListener listener4 = new TestListener();
            final TestListener listener5 = new TestListener();
            final TestListener listener6 = new TestListener();
            final TestListener listener7 = new TestListener();
            final TestListener listener8 = new TestListener();
            final TestListener listener9 = new TestListener();
            final TestListener listener10 = new TestListener();
            final TestListener listener11 = new TestListener();
            final TestListener listener12 = new TestListener();
            final TestListener listener13 = new TestListener();
            final TestListener listener14 = new TestListener();
            final TestListener listener15 = new TestListener();
            cSupport.addListener(globalListner);
            cSupport.addListener(bField1, listener1);
            cSupport.addListener(byteField, listener2);
            cSupport.addListener(charField, listener3);
            cSupport.addListener(doubleField, listener4);
            cSupport.addListener(floatField, listener5);
            cSupport.addListener(newFullIntField, listener6);
            cSupport.addListener(LongField, listener7);
            cSupport.addListener(shortField, listener8);
            cSupport.addListener(bOptional, listener9);
            cSupport.addListener(byteOptional, listener10);
            cSupport.addListener(cOptional, listener11);
            cSupport.addListener(dOptional, listener12);
            cSupport.addListener(fOptional, listener13);
            cSupport.addListener(sOptional, listener14);
            cSupport.addListener(lOptional, listener15);

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(bField1, booleans[i]);
                storage.write(byteField, bytes[i]);
                storage.write(charField, chars[i]);
                storage.write(doubleField, doubles[i]);
                storage.write(floatField, floats[i]);
                storage.write(newFullIntField, ints[i]);
                storage.write(shortField, shorts[i]);
                storage.write(LongField, longs[i]);

                storage.write(bOptional, booleans[i]);
                storage.write(byteOptional, bytes[i]);
                storage.write(cOptional, chars[i]);
                storage.write(dOptional, doubles[i]);
                storage.write(fOptional, floats[i]);
                storage.write(sOptional, shorts[i]);
                storage.write(lOptional, longs[i]);

            }
            ActionSet actions = storage.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);

                assertEquals(booleans[i], storage.read(bField1));
                assertEquals(bytes[i], storage.read(byteField));
                assertEquals(chars[i], storage.read(charField));
                assertEquals(doubles[i], storage.read(doubleField), DELTA);
                assertEquals(floats[i], storage.read(floatField), DELTA);
                assertEquals(ints[i], storage.read(newFullIntField));
                assertEquals(longs[i], storage.read(LongField));
                assertEquals(shorts[i], storage.read(shortField));
                assertEquals(booleans[i], storage.read(bOptional));
                assertEquals(bytes[i], storage.read(byteOptional));
                assertEquals(chars[i], storage.read(cOptional));
                assertEquals(doubles[i], storage.read(dOptional), DELTA);
                assertEquals(floats[i], storage.read(fOptional), DELTA);
                assertEquals(longs[i], storage.read(lOptional));
                assertEquals(shorts[i], storage.read(sOptional));

            }

            for (int i = 0; i < _CAPACITY; i++) {

                storage.selectStructure(i);
                storage.clear();
                actions = storage.transactionManager().commit();

                final List<Object> events = actions.events();
                int changeCount = 0;

                if (booleans[i]) {
                    changeCount = checkBooleanChange(bField1, i, events,
                            changeCount);
                    changeCount = checkBooleanChange(bField1, i, events,
                            changeCount);
                    changeCount = checkBooleanChange(bField1, i, events,
                            changeCount);
                }
                if (bytes[i] != 0) {
                    changeCount = checkByteChange(byteField, i, events,
                            changeCount, bytes);
                    changeCount = checkByteChange(byteField, i, events,
                            changeCount, bytes);
                    changeCount = checkByteChange(byteField, i, events,
                            changeCount, bytes);
                }
                if (chars[i] != 0) {
                    changeCount = checkCharChange(charField, i, events,
                            changeCount, chars);
                    changeCount = checkCharChange(charField, i, events,
                            changeCount, chars);
                    changeCount = checkCharChange(charField, i, events,
                            changeCount, chars);
                }
                if (doubles[i] != 0) {
                    changeCount = checkDoubleChange(doubleField, i, events,
                            changeCount, doubles);
                    changeCount = checkDoubleChange(doubleField, i, events,
                            changeCount, doubles);
                    changeCount = checkDoubleChange(doubleField, i, events,
                            changeCount, doubles);
                }
                if (floats[i] != 0) {
                    changeCount = checkFloatChange(floatField, i, events,
                            changeCount, floats);
                    changeCount = checkFloatChange(floatField, i, events,
                            changeCount, floats);
                    changeCount = checkFloatChange(floatField, i, events,
                            changeCount, floats);
                }
                if (ints[i] != 0) {
                    changeCount = checkIntChange(newFullIntField, i, events,
                            changeCount, ints);
                    changeCount = checkIntChange(newFullIntField, i, events,
                            changeCount, ints);
                    changeCount = checkIntChange(newFullIntField, i, events,
                            changeCount, ints);
                }
                if (longs[i] != 0L) {
                    changeCount = checkLongChange(LongField, i, events,
                            changeCount, longs);
                    changeCount = checkLongChange(LongField, i, events,
                            changeCount, longs);
                    changeCount = checkLongChange(LongField, i, events,
                            changeCount, longs);
                }
                if (shorts[i] != 0) {
                    changeCount = checkShortChange(shortField, i, events,
                            changeCount, shorts);
                    changeCount = checkShortChange(shortField, i, events,
                            changeCount, shorts);
                    changeCount = checkShortChange(shortField, i, events,
                            changeCount, shorts);
                }

                if (booleans[i]) {
                    changeCount = checkBooleanChange(bOptional, i, events,
                            changeCount);
                    changeCount = checkBooleanChange(bOptional, i, events,
                            changeCount);
                    changeCount = checkBooleanChange(bOptional, i, events,
                            changeCount);
                }
                if (bytes[i] != 0) {
                    changeCount = checkByteChange(byteOptional, i, events,
                            changeCount, bytes);
                    changeCount = checkByteChange(byteOptional, i, events,
                            changeCount, bytes);
                    changeCount = checkByteChange(byteOptional, i, events,
                            changeCount, bytes);
                }
                if (chars[i] != 0) {
                    changeCount = checkCharChange(cOptional, i, events,
                            changeCount, chars);
                    changeCount = checkCharChange(cOptional, i, events,
                            changeCount, chars);
                    changeCount = checkCharChange(cOptional, i, events,
                            changeCount, chars);

                }
                if (doubles[i] != 0) {
                    changeCount = checkDoubleChange(dOptional, i, events,
                            changeCount, doubles);
                    changeCount = checkDoubleChange(dOptional, i, events,
                            changeCount, doubles);
                    changeCount = checkDoubleChange(dOptional, i, events,
                            changeCount, doubles);
                }
                if (floats[i] != 0) {
                    changeCount = checkFloatChange(fOptional, i, events,
                            changeCount, floats);
                    changeCount = checkFloatChange(fOptional, i, events,
                            changeCount, floats);
                    changeCount = checkFloatChange(fOptional, i, events,
                            changeCount, floats);
                }
                if (longs[i] != 0L) {
                    changeCount = checkLongChange(lOptional, i, events,
                            changeCount, longs);
                    changeCount = checkLongChange(lOptional, i, events,
                            changeCount, longs);
                    changeCount = checkLongChange(lOptional, i, events,
                            changeCount, longs);
                }
                if (shorts[i] != 0) {
                    changeCount = checkShortChange(sOptional, i, events,
                            changeCount, shorts);
                    changeCount = checkShortChange(sOptional, i, events,
                            changeCount, shorts);
                    changeCount = checkShortChange(sOptional, i, events,
                            changeCount, shorts);
                    changeCount--;
                }
            }

            cSupport.removeListener(bField1, listener1);
            cSupport.removeListener(byteField, listener2);
            cSupport.removeListener(charField, listener3);
            cSupport.removeListener(doubleField, listener4);
            cSupport.removeListener(floatField, listener5);
            cSupport.removeListener(newFullIntField, listener6);
            cSupport.removeListener(LongField, listener7);
            cSupport.removeListener(shortField, listener8);
            cSupport.removeListener(bOptional, listener9);
            cSupport.removeListener(byteOptional, listener10);
            cSupport.removeListener(cOptional, listener11);
            cSupport.removeListener(dOptional, listener12);
            cSupport.removeListener(fOptional, listener13);
            cSupport.removeListener(sOptional, listener14);
            cSupport.removeListener(lOptional, listener15);
            cSupport.removeListener(globalListner);

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.clear();
            }
            storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(bField1, booleans[i]);
                storage.write(byteField, bytes[i]);
                storage.write(charField, chars[i]);
                storage.write(doubleField, doubles[i]);
                storage.write(floatField, floats[i]);
                storage.write(newFullIntField, ints[i]);
                storage.write(shortField, shorts[i]);
                storage.write(LongField, longs[i]);

                storage.write(bOptional, booleans[i]);
                storage.write(byteOptional, bytes[i]);
                storage.write(cOptional, chars[i]);
                storage.write(dOptional, doubles[i]);
                storage.write(fOptional, floats[i]);
                storage.write(sOptional, shorts[i]);
                storage.write(lOptional, longs[i]);

            }
            actions = storage.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);

                assertEquals(booleans[i], storage.read(bField1));
                assertEquals(bytes[i], storage.read(byteField));
                assertEquals(chars[i], storage.read(charField));
                assertEquals(doubles[i], storage.read(doubleField), DELTA);
                assertEquals(floats[i], storage.read(floatField), DELTA);
                assertEquals(ints[i], storage.read(newFullIntField));
                assertEquals(longs[i], storage.read(LongField));
                assertEquals(shorts[i], storage.read(shortField));
                assertEquals(booleans[i], storage.read(bOptional));
                assertEquals(bytes[i], storage.read(byteOptional));
                assertEquals(chars[i], storage.read(cOptional));
                assertEquals(doubles[i], storage.read(dOptional), DELTA);
                assertEquals(floats[i], storage.read(fOptional), DELTA);
                assertEquals(longs[i], storage.read(lOptional));
                assertEquals(shorts[i], storage.read(sOptional));

            }

            for (int i = 0; i < _CAPACITY; i++) {

                storage.selectStructure(i);
                storage.clear();
                actions = storage.transactionManager().commit();

                final List<Object> events = actions.events();
                int changeCount = 0;

                if (booleans[i]) {
                    changeCount = checkBooleanChange(bField1, i, events,
                            changeCount);
                }
                if (bytes[i] != 0) {
                    changeCount = checkByteChange(byteField, i, events,
                            changeCount, bytes);
                }
                if (chars[i] != 0) {
                    changeCount = checkCharChange(charField, i, events,
                            changeCount, chars);
                }
                if (doubles[i] != 0) {
                    changeCount = checkDoubleChange(doubleField, i, events,
                            changeCount, doubles);
                }
                if (floats[i] != 0) {
                    changeCount = checkFloatChange(floatField, i, events,
                            changeCount, floats);
                }
                if (ints[i] != 0) {
                    changeCount = checkIntChange(newFullIntField, i, events,
                            changeCount, ints);
                }
                if (longs[i] != 0L) {
                    changeCount = checkLongChange(LongField, i, events,
                            changeCount, longs);
                }
                if (shorts[i] != 0) {
                    changeCount = checkShortChange(shortField, i, events,
                            changeCount, shorts);
                }

                if (booleans[i]) {
                    changeCount = checkBooleanChange(bOptional, i, events,
                            changeCount);
                }
                if (bytes[i] != 0) {
                    changeCount = checkByteChange(byteOptional, i, events,
                            changeCount, bytes);
                }
                if (chars[i] != 0) {
                    changeCount = checkCharChange(cOptional, i, events,
                            changeCount, chars);

                }
                if (doubles[i] != 0) {
                    changeCount = checkDoubleChange(dOptional, i, events,
                            changeCount, doubles);
                }
                if (floats[i] != 0) {
                    changeCount = checkFloatChange(fOptional, i, events,
                            changeCount, floats);
                }
                if (longs[i] != 0L) {
                    changeCount = checkLongChange(lOptional, i, events,
                            changeCount, longs);
                }
                if (shorts[i] != 0) {
                    changeCount = checkShortChange(sOptional, i, events,
                            changeCount, shorts);
                    changeCount--;
                }
            }
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testClear() {

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
            final Struct cS = strg.compiledStructs;

            final BooleanField bool1 = cS.field("booleanField1");
            final ByteField byteField = cS.field("byteField");
            final CharField charField = cS.field("charField");
            final DoubleField doubleField = cS.field("doubleField");
            final FloatField floatField = cS.field("floatField");
            final IntField intField = cS.field("intField");
            final ShortField shortField = cS.field("shortField");
            final LongField LongField = cS.field("longField");

            final BooleanOptionalField booleanOptional = cS.field("booleanOptional");
            final ByteOptionalField byteOptional = cS.field("byteOptional");
            final CharOptionalField charOptional = cS.field("charOptional");
            final DoubleOptionalField doubleOptional = cS.field("doubleOptional");
            final FloatOptionalField floatOptional = cS.field("floatOptional");
            final ShortOptionalField shortOptional = cS.field("shortOptional");
            final LongOptionalField longOptional = cS.field("longOptional");

            final Storage storage = strg.storage;
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(bool1, booleans[i]);
                storage.write(byteField, bytes[i]);
                storage.write(charField, chars[i]);
                storage.write(doubleField, doubles[i]);
                storage.write(floatField, floats[i]);
                storage.write(intField, ints[i]);
                storage.write(shortField, shorts[i]);
                storage.write(LongField, longs[i]);

                storage.write(booleanOptional, booleans[i]);
                storage.write(byteOptional, bytes[i]);
                storage.write(charOptional, chars[i]);
                storage.write(doubleOptional, doubles[i]);
                storage.write(floatOptional, floats[i]);
                storage.write(shortOptional, shorts[i]);
                storage.write(longOptional, longs[i]);

            }
            ActionSet actions = storage.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);

                assertEquals(booleans[i], storage.read(bool1));
                assertEquals(bytes[i], storage.read(byteField));
                assertEquals(chars[i], storage.read(charField));
                assertEquals(doubles[i], storage.read(doubleField), DELTA);
                assertEquals(floats[i], storage.read(floatField), DELTA);
                assertEquals(ints[i], storage.read(intField));
                assertEquals(longs[i], storage.read(LongField));
                assertEquals(shorts[i], storage.read(shortField));

                assertEquals(booleans[i], storage.read(booleanOptional));
                assertEquals(bytes[i], storage.read(byteOptional));
                assertEquals(chars[i], storage.read(charOptional));
                assertEquals(doubles[i], storage.read(doubleOptional), DELTA);
                assertEquals(floats[i], storage.read(floatOptional), DELTA);
                assertEquals(longs[i], storage.read(longOptional));
                assertEquals(shorts[i], storage.read(shortOptional));

            }

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.clear();
                actions = storage.transactionManager().commit();

                final List<Object> events = actions.events();
                int changeCount = 0;

                if (booleans[i]) {
                    changeCount = checkBooleanChange(bool1, i, events,
                            changeCount);
                }
                if (bytes[i] != 0) {
                    changeCount = checkByteChange(byteField, i, events,
                            changeCount, bytes);
                }
                if (chars[i] != 0) {
                    changeCount = checkCharChange(charField, i, events,
                            changeCount, chars);
                }
                if (doubles[i] != 0) {
                    changeCount = checkDoubleChange(doubleField, i, events,
                            changeCount, doubles);
                }
                if (floats[i] != 0) {
                    changeCount = checkFloatChange(floatField, i, events,
                            changeCount, floats);
                }
                if (ints[i] != 0) {
                    changeCount = checkIntChange(intField, i, events,
                            changeCount, ints);
                }
                if (longs[i] != 0L) {
                    changeCount = checkLongChange(LongField, i, events,
                            changeCount, longs);

                }
                if (shorts[i] != 0) {
                    changeCount = checkShortChange(shortField, i, events,
                            changeCount, shorts);
                }

                if (booleans[i]) {
                    changeCount = checkBooleanChange(booleanOptional, i,
                            events, changeCount);

                }
                if (bytes[i] != 0) {
                    changeCount = checkByteChange(byteOptional, i, events,
                            changeCount, bytes);
                }
                if (chars[i] != 0) {
                    changeCount = checkCharChange(charOptional, i, events,
                            changeCount, chars);

                }
                if (doubles[i] != 0) {
                    changeCount = checkDoubleChange(doubleOptional, i, events,
                            changeCount, doubles);
                }
                if (floats[i] != 0) {
                    changeCount = checkFloatChange(floatOptional, i, events,
                            changeCount, floats);
                }
                if (longs[i] != 0L) {
                    changeCount = checkLongChange(longOptional, i, events,
                            changeCount, longs);
                }
                if (shorts[i] != 0) {
                    checkShortChange(shortOptional, i, events, changeCount,
                            shorts);
                }
            }
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

            final BooleanOptionalField booleanOptional = strg.compiledStructs
                    .field("booleanOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(bool1, booleans[i]);
                strg.storage.write(booleanOptional, booleans[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, booleans[i], strg.storage.read(bool1));
                assertEquals(message, booleans[i],
                        strg.storage.read(booleanOptional));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Boolean field -"
                    + bool1.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (booleans[count]) {
                    BooleanValueChange change = (BooleanValueChange) changes
                            .next();
                    assertEquals(message, bool1, change.field());
                    assertEquals(message, count, change.structureIndex());
                    assertEquals(message, false, change.booleanOldValue());
                    assertEquals(message, booleans[count],
                            change.booleanNewValue());
                    assertEquals(message, false, change.oldValue());
                    assertEquals(message, booleans[count], change.newValue());

                    change = (BooleanValueChange) changes.next();
                    assertEquals(message, booleanOptional, change.field());
                    assertEquals(message, count, change.structureIndex());
                    assertEquals(message, false, change.booleanOldValue());
                    assertEquals(message, booleans[count],
                            change.booleanNewValue());
                    assertEquals(message, false, change.oldValue());
                    assertEquals(message, booleans[count], change.newValue());
                }
            }

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -" + bool1.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (booleans[count]) {
                    assertEquals(message, bool1.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: false",
                            events.get(changeCount++));
                    assertEquals(message, "new: true",
                            events.get(changeCount++));
                    assertEquals(message, booleanOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: false",
                            events.get(changeCount++));
                    assertEquals(message, "new: true",
                            events.get(changeCount++));
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

            final ByteField byteField = strg.compiledStructs.field("byteField");
            final ByteOptionalField byteOptional = strg.compiledStructs
                    .field("byteOptional");

            final Storage storage = strg.storage;
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(byteField, bytes[i]);
                storage.write(byteOptional, bytes[i]);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, bytes[i], storage.read(byteField));
                assertEquals(message, bytes[i], storage.read(byteOptional));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Byte field -"
                    + byteField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (bytes[count] != 0) {
                    ByteValueChange change = (ByteValueChange) changes.next();
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

                    change = (ByteValueChange) changes.next();
                    assertEquals(message, byteOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + byteField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (bytes[count] != 0) {
                    assertEquals(message, byteField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + bytes[count],
                            events.get(changeCount++));
                    assertEquals(message, byteOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + bytes[count],
                            events.get(changeCount++));

                }
            }
        }
    }

    @Test
    public void testModifyChar() {

        final char[] chars = chars();

        for (final CompiledStorage strg : COMPILED) {

            String message = "Modify failed for Compiler -"
                    + strg.compiler.compilerName();
            final CharField charField = (CharField) strg.compiledStructs
                    .field("charField");

            final CharOptionalField charOptional = (CharOptionalField) strg.compiledStructs
                    .field("charOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(charField, chars[i]);
                strg.storage.write(charOptional, chars[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, chars[i], strg.storage.read(charField));
                assertEquals(message, chars[i], strg.storage.read(charOptional));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Char field -"
                    + charField.name();

            for (int count = 0; count < _CAPACITY; count++) {
                if (chars[count] != 0) {
                    CharValueChange change = (CharValueChange) changes.next();
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

                    change = (CharValueChange) changes.next();
                    assertEquals(message, charOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + charField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (chars[count] != 0) {
                    assertEquals(message, charField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: " + (char) 0,
                            events.get(changeCount++));
                    assertEquals(message, "new: " + chars[count],
                            events.get(changeCount++));
                    assertEquals(message, charOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: " + (char) 0,
                            events.get(changeCount++));
                    assertEquals(message, "new: " + chars[count],
                            events.get(changeCount++));

                }
            }
        }
    }

    @Test
    public void testModifyDouble() {

        final double[] doubles = doubles();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Double Field Test failed for Compiler -"
                    + strg.compiler.compilerName();

            final DoubleField doubleField = (DoubleField) strg.compiledStructs
                    .field("doubleField");
            final DoubleOptionalField doubleOptional = (DoubleOptionalField) strg.compiledStructs
                    .field("doubleOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(doubleField, doubles[i]);
                strg.storage.write(doubleOptional, doubles[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, doubles[i],
                        strg.storage.read(doubleField), DELTA);
                assertEquals(message, doubles[i],
                        strg.storage.read(doubleOptional), DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Double field -"
                    + doubleField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (doubles[count] != 0) {
                    DoubleValueChange change = (DoubleValueChange) changes
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

                    change = (DoubleValueChange) changes.next();
                    assertEquals(message, doubleOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + doubleField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (doubles[count] != 0) {
                    assertEquals(message, doubleField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: " + (double) 0,
                            events.get(changeCount++));
                    assertEquals(message, "new: " + doubles[count],
                            events.get(changeCount++));
                    assertEquals(message, doubleOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: " + (double) 0,
                            events.get(changeCount++));
                    assertEquals(message, "new: " + doubles[count],
                            events.get(changeCount++));
                }
            }
        }
    }

    @Test
    public void testModifyFloat() {

        final float[] floats = floats();

        for (final CompiledStorage strg : COMPILED) {

            String message = "Float Field Test failed for Compiler -"
                    + strg.compiler.compilerName();

            final FloatField floatField = strg.compiledStructs
                    .field("floatField");

            final FloatOptionalField floatOptional = strg.compiledStructs
                    .field("floatOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(floatField, floats[i]);
                strg.storage.write(floatOptional, floats[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, floats[i], strg.storage.read(floatField),
                        DELTA);
                assertEquals(message, floats[i],
                        strg.storage.read(floatOptional), DELTA);
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Float field -"
                    + floatField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (floats[count] != 0) {
                    FloatValueChange change = (FloatValueChange) changes.next();
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

                    change = (FloatValueChange) changes.next();
                    assertEquals(message, floatOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + floatField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (floats[count] != 0) {
                    assertEquals(message, floatField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: " + (float) 0,
                            events.get(changeCount++));
                    assertEquals(message, "new: " + floats[count],
                            events.get(changeCount++));
                    assertEquals(message, floatOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: " + (float) 0,
                            events.get(changeCount++));
                    assertEquals(message, "new: " + floats[count],
                            events.get(changeCount++));
                }
            }
        }
    }

    @Test
    public void testModifyInt() {

        final int[] ints = ints();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Int Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final IntField intField = strg.compiledStructs.field("intField");
            final IntOptionalField iOptional = strg.compiledStructs
                    .field("intOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(intField, ints[i]);
                strg.storage.write(iOptional, ints[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, ints[i], strg.storage.read(intField));
                assertEquals(message, ints[i], strg.storage.read(iOptional));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Int field -"
                    + intField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (ints[count] != 0) {
                    IntValueChange change = (IntValueChange) changes.next();
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

                    change = (IntValueChange) changes.next();
                    assertEquals(message, iOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + intField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (ints[count] != 0) {
                    assertEquals(message, intField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + ints[count],
                            events.get(changeCount++));

                    assertEquals(message, iOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + ints[count],
                            events.get(changeCount++));

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
            final LongOptionalField longOptional = (LongOptionalField) strg.compiledStructs
                    .field("longOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(longField, longs[i]);
                strg.storage.write(longOptional, longs[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, longs[i], strg.storage.read(longField));
                assertEquals(message, longs[i], strg.storage.read(longOptional));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " Int field -"
                    + longField.name();
            for (int count = 0; count < _CAPACITY; count++) {
                if (longs[count] != 0) {
                    LongValueChange change = (LongValueChange) changes.next();
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

                    change = (LongValueChange) changes.next();
                    assertEquals(message, longOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + longField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (longs[count] != 0) {
                    assertEquals(message, longField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + longs[count],
                            events.get(changeCount++));
                    assertEquals(message, longOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + longs[count],
                            events.get(changeCount++));
                }
            }
        }
    }

    @Test
    public void testModifyShort() {

        final short[] shorts = shorts();

        for (final CompiledStorage strg : COMPILED) {
            String message = "Short Field Test failed for Compiler -"
                    + strg.compiler.compilerName();
            final ShortField shortField = (ShortField) strg.compiledStructs
                    .field("shortField");
            final ShortOptionalField shortOptional = strg.compiledStructs
                    .field("shortOptional");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, shorts[i]);
                strg.storage.write(shortOptional, shorts[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(message, shorts[i], strg.storage.read(shortField));
                assertEquals(message, shorts[i],
                        strg.storage.read(shortOptional));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());
            message = "Failed Asserting 'change' list for Compiler -"
                    + strg.compiler.compilerName() + " short field -"
                    + shortField.name();

            for (int count = 0; count < _CAPACITY; count++) {
                if (shorts[count] != 0) {
                    ShortValueChange change = (ShortValueChange) changes.next();
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

                    change = (ShortValueChange) changes.next();
                    assertEquals(message, shortOptional, change.field());
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

            final List<Object> events = actions.events();
            message = "Failed Asserting listener response for Compiler -"
                    + strg.compiler.compilerName() + " field -"
                    + shortField.name();
            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                if (shorts[count] != 0) {
                    assertEquals(message, shortField.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + shorts[count],
                            events.get(changeCount++));
                    assertEquals(message, shortOptional.name() + count,
                            events.get(changeCount++));
                    assertEquals(message, "old: 0", events.get(changeCount++));
                    assertEquals(message, "new: " + shorts[count],
                            events.get(changeCount++));
                }
            }
        }
    }

    @Test
    public void testModifyString() {

        final int[] ints = ints();

        for (final CompiledStorage strg : COMPILED) {
            final ObjectField<String, ?> string = strg.compiledStructs
                    .field("stringField");

            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(string, "String" + ints[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals("String" + ints[i], strg.storage.read(string));
            }

            final Iterator<ValueChange<?>> changes = actions.changeRecords()
                    .changes(strg.storage.rootStruct());

            for (int count = 0; count < _CAPACITY; count++) {
                final ObjectValueChange<String, ?> change = (ObjectValueChange) changes
                        .next();
                assertEquals(string, change.field());
                assertEquals(count, change.structureIndex());
                assertEquals(null, change.oldValue());
                assertEquals("String" + ints[count], change.newValue());
            }

            final List<Object> events = actions.events();

            for (int count = 0, changeCount = 0; count < _CAPACITY; count++) {
                assertEquals(string.name() + count, events.get(changeCount++));
                assertEquals("old: null", events.get(changeCount++));
                assertEquals("new: String" + ints[count],
                        events.get(changeCount++));
            }
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testReadOnly() {

        for (final CompiledStorage strg : COMPILED) {
            final Struct cS = strg.compiledStructs;

            final BooleanField bool1 = cS.field("booleanField1");
            final ByteField byteField = cS.field("byteField");
            final CharField charField = cS.field("charField");
            final DoubleField doubleField = cS.field("doubleField");
            final FloatField floatField = cS.field("floatField");
            final IntField intField = cS.field("intField");
            final ShortField shortField = cS.field("shortField");
            final LongField LongField = cS.field("longField");

            final BooleanOptionalField booleanOptional = cS.field("booleanOptional");
            final ByteOptionalField byteOptional = cS.field("byteOptional");
            final CharOptionalField charOptional = cS.field("charOptional");
            final DoubleOptionalField doubleOptional = cS.field("doubleOptional");
            final FloatOptionalField floatOptional = cS.field("floatOptional");
            final ShortOptionalField shortOptional = cS.field("shortOptional");
            final LongOptionalField longOptional = cS.field("longOptional");

            final Storage storage = strg.storage;
            final ChangeListenerSupport cSupport = strg.storage
                    .changeListenerSupport();

            final TestListener2 globalListner = new TestListener2();
            cSupport.addListener(globalListner);

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                int j = 0;
                try {
                    storage.write(bool1, true);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(1, j);
                try {
                    storage.write(byteField, (byte) 1);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(2, j);

                try {
                    storage.write(charField, 'a');
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(3, j);

                try {
                    storage.write(doubleField, 10d);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(4, j);

                try {
                    storage.write(floatField, 10f);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(5, j);

                try {
                    storage.write(intField, 10);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(6, j);
                try {
                    storage.write(shortField, (short) 10);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(7, j);

                try {
                    storage.write(LongField, 10l);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(8, j);

                try {
                    storage.write(booleanOptional, true);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(9, j);

                try {
                    storage.write(byteOptional, (byte) 10);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(10, j);

                try {
                    storage.write(charOptional, 'a');
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(11, j);

                try {
                    storage.write(doubleOptional, 10d);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(12, j);
                try {
                    storage.write(floatOptional, 10f);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(13, j);
                try {
                    storage.write(shortOptional, (short) 10);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(14, j);
                try {
                    storage.write(longOptional, 10l);
                } catch (final Exception e) {
                    j++;
                }
                assertEquals(15, j);
            }
        }
    }
}
