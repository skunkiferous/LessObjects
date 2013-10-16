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

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.fields.virtual.BooleanFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.BooleanVirtualField;
import com.blockwithme.lessobjects.fields.virtual.ByteFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ByteVirtualField;
import com.blockwithme.lessobjects.fields.virtual.CharFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.CharVirtualField;
import com.blockwithme.lessobjects.fields.virtual.DoubleFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.DoubleVirtualField;
import com.blockwithme.lessobjects.fields.virtual.FloatFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.FloatVirtualField;
import com.blockwithme.lessobjects.fields.virtual.IntFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.IntVirtualField;
import com.blockwithme.lessobjects.fields.virtual.LongFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.LongVirtualField;
import com.blockwithme.lessobjects.fields.virtual.ShortFieldMapper;
import com.blockwithme.lessobjects.fields.virtual.ShortVirtualField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 900 LINES
/** The Class TestVirtualFields. */
@SuppressWarnings({ "PMD", "all" })
public class TestVirtualFields extends TestData {

    /** The Class VirtualBooleanConverter. */
    public static final class VirtualBooleanConverter implements
            BooleanFieldMapper {
        BooleanField f;

        private void init(final Struct theParent) {
            // TODO Doing this can get expensive. Since the virtual field
            // converter should be immutable,
            // because it is part of a Struct, it cannot have a mutable state,
            // and therefore this. But
            // since the data we need to cache is also part of the struct, this
            // could be optimized. We
            // need to add a "phase", before finalizing a compiled Struct, where
            // virtual converters can
            // query the struct and cache some struct data.
            f = theParent.field("booleanField");
        }

        /** {@inheritDoc} */
        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        /** {@inheritDoc} */
        @Override
        public boolean read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final boolean theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    /** The Class VirtualCharConverter. */
    public static final class VirtualCharConverter implements CharFieldMapper {
        CharField f;

        private void init(final Struct theParent) {
            f = theParent.field("charField");
        }

        /** {@inheritDoc} */
        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        /** {@inheritDoc} */
        @Override
        public char read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final char theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    /** The Class VirtualDoubleConverter. */
    public static final class VirtualDoubleConverter implements
            DoubleFieldMapper {
        DoubleField f;

        private void init(final Struct theParent) {
            f = theParent.field("doubleField");
        }

        /** {@inheritDoc} */
        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        /** {@inheritDoc} */
        @Override
        public double read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final double theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    public static final class VirtualFloatConverter implements FloatFieldMapper {
        FloatField f;

        private void init(final Struct theParent) {
            f = theParent.field("floatField");
        }

        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        @Override
        public float read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        @Override
        public void write(final float theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    public static final class VirtualIntConverter implements IntFieldMapper {
        IntField f;

        private void init(final Struct theParent) {
            f = theParent.field("intField");
        }

        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        @Override
        public int read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        @Override
        public void write(final int theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    /** The Class VirtualLongConverter. */
    public static final class VirtualLongConverter implements LongFieldMapper {
        LongField f;

        private void init(final Struct theParent) {
            f = theParent.field("longField");
        }

        /** {@inheritDoc} */
        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear();
        }

        /** {@inheritDoc} */
        @Override
        public long read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final long theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    /** The Class VirtualShortConverter. */
    public static final class VirtualShortConverter implements ShortFieldMapper {
        ShortField f;

        private void init(final Struct theParent) {
            f = theParent.field("shortField");
        }

        /** {@inheritDoc} */
        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        /** {@inheritDoc} */
        @Override
        public short read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final short theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    public static final class VirutalByteConverter implements ByteFieldMapper {
        ByteField f;

        private void init(final Struct theParent) {
            f = theParent.field("byteField");
        }

        @Override
        public void clear(final Struct theParent, final Storage theStorage) {
            init(theParent);
            theStorage.clear(f);
        }

        @Override
        public byte read(final Struct theParent, final Storage theStorage) {
            init(theParent);
            return theStorage.read(f);
        }

        @Override
        public void write(final byte theValue, final Struct theParent,
                final Storage theStorage) {
            init(theParent);
            theStorage.write(f, theValue);
        }
    }

    /** The factory. */
    public CompiledStorage[] COMPILED;

    /** The Struct. */
    public Struct struct;

    @Test
    public void booleanReadWrite() {

        final boolean[] BOOLEANS = booleans();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.booleanReadWrite() -"
                    + strg.compiler.compilerName();
            final BooleanVirtualField bool1 = strg.compiledStructs
                    .field("booleanVField");
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                bool1.writeAny(BOOLEANS[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], bool1.readAny(storage));
            }
        }
    }

    @Test
    public void byteReadWrite() {

        final byte[] BYTES = bytes();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.byteReadWrite() -"
                    + strg.compiler.compilerName();
            final ByteVirtualField byteField = strg.compiledStructs
                    .field("byteVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                byteField.writeAny(BYTES[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, BYTES[i],
                        (byte) byteField.readAny(storage));
            }
        }
    }

    @Test
    public void charReadWrite() {

        final char[] CHARS = chars();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.charReadWrite() -"
                    + strg.compiler.compilerName();
            final CharVirtualField charField = strg.compiledStructs
                    .field("charVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                charField.writeAny(CHARS[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, CHARS[i],
                        (char) charField.readAny(storage));
            }
        }
    }

    @Test
    public void doubleReadWrite() {

        final double[] DOUBLES = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.doubleReadWrite() -"
                    + strg.compiler.compilerName();
            final DoubleVirtualField doubleField = strg.compiledStructs
                    .field("doubleVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                doubleField.writeAny(DOUBLES[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, DOUBLES[i],
                        (double) doubleField.readAny(storage), DELTA);
            }
        }
    }

    @Test
    public void floatReadWrite() {

        final float[] FLOATS = floats();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.floatReadWrite() -"
                    + strg.compiler.compilerName();
            final FloatVirtualField floatField = strg.compiledStructs
                    .field("floatVField");
            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                floatField.writeAny(FLOATS[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, FLOATS[i],
                        (float) floatField.readAny(storage), DELTA);
            }
        }
    }

    public void init() {

        final IntFieldMapper intConverter = new VirtualIntConverter();
        final LongFieldMapper longConverter = new VirtualLongConverter();
        final ByteFieldMapper byteConverter = new VirutalByteConverter();
        final ShortFieldMapper shortConverter = new VirtualShortConverter();
        final FloatFieldMapper floatConverter = new VirtualFloatConverter();
        final DoubleFieldMapper doubleConverter = new VirtualDoubleConverter();
        final CharFieldMapper charConverter = new VirtualCharConverter();
        final BooleanFieldMapper booleanConverter = new VirtualBooleanConverter();

        struct = new Struct("VirtualFieldTest", new Struct[] {},
                FACTORY.newIntField("intField"), FACTORY.newIntVirtualField(
                        "intVField", intConverter),
                FACTORY.newLongField("longField"), FACTORY.newLongVirtualField(
                        "longVField", longConverter),
                FACTORY.newByteField("byteField"), FACTORY.newByteVirtualField(
                        "byteVField", byteConverter),
                FACTORY.newShortField("shortField"),
                FACTORY.newShortVirtualField("shortVField", shortConverter),
                FACTORY.newFloatField("floatField"),
                FACTORY.newFloatVirtualField("floatVField", floatConverter),
                FACTORY.newDoubleField("doubleField"),
                FACTORY.newDoubleVirtualField("doubleVField", doubleConverter),
                FACTORY.newCharField("charField"), FACTORY.newCharVirtualField(
                        "charVField", charConverter),
                FACTORY.newBooleanField("booleanField"),
                FACTORY.newBooleanVirtualField("booleanVField",
                        booleanConverter));

        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(struct);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, _CAPACITY);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void intReadWrite() {

        final int[] INTS = ints();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.intReadWrite() -"
                    + strg.compiler.compilerName();
            final IntVirtualField intField = strg.compiledStructs
                    .field("intVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                intField.writeAny(INTS[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, INTS[i], (int) intField.readAny(storage));
            }
        }
    }

    @Test
    public void longReadWrite() {

        final long[] LONGS = longs();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.longReadWrite() -"
                    + strg.compiler.compilerName();
            final LongVirtualField longField = strg.compiledStructs
                    .field("longVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                longField.writeAny(LONGS[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, LONGS[i],
                        (long) longField.readAny(storage));
            }
        }
    }

    @Before
    public void setup() {
        init();
    }

    @Test
    public void shortReadWrite() {

        final short[] SHORTS = shorts();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.shortReadWrite() -"
                    + strg.compiler.compilerName();
            final ShortVirtualField shortField = strg.compiledStructs
                    .field("shortVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                shortField.writeAny(SHORTS[i], storage);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, SHORTS[i],
                        (short) shortField.readAny(storage));
            }
        }
    }

    /** Test modify boolean. */
    @Test
    public void testModifyBoolean() {

        final boolean[] BOOLEANS = booleans();

        for (final CompiledStorage strg : COMPILED) {
            final Storage storage = strg.storage;

            final String message = "TestVirtualFields.testModifyBoolean() -"
                    + strg.compiler.compilerName();
            final BooleanVirtualField bool1 = strg.compiledStructs
                    .field("booleanVField");

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                storage.write(bool1, BOOLEANS[i]);
            }
            final ActionSet actions = storage.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                storage.selectStructure(i);
                assertEquals(message, BOOLEANS[i], storage.read(bool1));
            }
        }
    }

    /** Test modify byte. */
    @Test
    public void testModifyByte() {

        final byte[] BYTES = bytes();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestVirtualFields.testModifyByte() -"
                    + strg.compiler.compilerName();
            final ByteVirtualField byteField = strg.compiledStructs
                    .field("byteVField");

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
        }
    }

    /** Test modify char. */
    @Test
    public void testModifyChar() {

        final char[] CHARS = chars();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestVirtualFields.testModifyChar() -"
                    + strg.compiler.compilerName();
            final CharVirtualField charField = strg.compiledStructs
                    .field("charVField");

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
        }
    }

    /** Test modify double. */
    @Test
    public void testModifyDouble() {

        final double[] DOUBLES = doubles();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestVirtualFields.testModifyDouble() -"
                    + strg.compiler.compilerName();
            final DoubleVirtualField doubleField = strg.compiledStructs
                    .field("doubleVField");

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
        }
    }

    /** Test modify float. */
    @Test
    public void testModifyFloat() {

        final float[] FLOATS = floats();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestVirtualFields.testModifyFloat() -"
                    + strg.compiler.compilerName();
            final FloatVirtualField floatField = strg.compiledStructs
                    .field("floatVField");

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
        }
    }

    /** Test modify int. */
    @Test
    public void testModifyInt() {

        final int[] INTS = ints();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestVirtualFields.testModifyInt() -"
                    + strg.compiler.compilerName();
            final IntVirtualField intField = strg.compiledStructs
                    .field("intVField");

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
        }
    }

    /** Test modify long. */
    @Test
    public void testModifyLong() {

        final long[] LONGS = longs();

        for (final CompiledStorage strg : COMPILED) {
            final String message = "TestVirtualFields.testModifyLong() -"
                    + strg.compiler.compilerName();
            final LongVirtualField longField = strg.compiledStructs
                    .field("longVField");

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
        }
    }

    /** Test modify short. */
    @Test
    public void testModifyShort() {

        final short[] SHORTS = shorts();

        for (final CompiledStorage strg : COMPILED) {
            final ShortVirtualField shortField = strg.compiledStructs
                    .field("shortVField");
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                strg.storage.write(shortField, SHORTS[i]);
            }
            final ActionSet actions = strg.storage.transactionManager()
                    .commit();
            for (int i = 0; i < _CAPACITY; i++) {
                strg.storage.selectStructure(i);
                assertEquals(SHORTS[i], strg.storage.read(shortField));
            }
        }
    }

    /** Test schema. */
    @Test
    public void testSchema() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct compiledStructs = strg.compiledStructs;
            TestData.checkSchema(compiledStructs);
        }
    }
}
