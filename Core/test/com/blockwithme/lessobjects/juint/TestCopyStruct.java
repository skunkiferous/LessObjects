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
import static org.junit.Assert.assertEquals;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.compiler.CompilerBase;
import com.blockwithme.lessobjects.compiler.PackedCompiler;
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
import com.blockwithme.lessobjects.storage.aligned64.Aligned64SparseStorage;
import com.blockwithme.lessobjects.storage.aligned64.Aligned64Storage;
import com.blockwithme.lessobjects.storage.packed.PackedSparseStorage;
import com.blockwithme.lessobjects.storage.packed.PackedStorage;

/** The Class TestCopyStruct. */
// CHECKSTYLE IGNORE FOR NEXT 100 LINES
@SuppressWarnings({ "PMD", "all" })
@ParametersAreNonnullByDefault
public class TestCopyStruct extends TestStructFields {

    final static boolean[] booleans = booleans();
    final static byte[] bytes = bytes();
    final static char[] chars = chars();
    final static double[] doubles = doubles();
    final static float[] floats = floats();
    final static long[] int_longs = int_longs();
    final static int[] ints = ints();
    final static long[] longs = longs();
    final static short[] shorts = shorts();
    final static String[] strings = strings();

    private void compareResults(final String message, final Struct struct1,
            final Storage storage1, final Struct struct2, final Storage storage2) {

        final ObjectField<String, ?> str1 = struct1.field("stringField1");
        final IntField intField1 = struct1.field("intField");
        final BooleanField bool1 = struct1.field("booleanField1");
        final ByteField byteField1 = struct1.field("byteField");
        final CharField charField1 = struct1.field("charField");
        final FloatField floatField1 = struct1.field("floatField");
        final ShortField shortField1 = struct1.field("shortField");
        final DoubleField doubleField1 = struct1.field("doubleField");
        final LongField longField1 = struct1.field("longField");

        final ObjectField<String, ?> str2 = struct2.field("stringField1");
        final BooleanField bool2 = struct2.field("booleanField1");
        final IntField intField2 = struct2.field("intField");
        final ByteField byteField2 = struct2.field("byteField");
        final CharField charField2 = struct2.field("charField");
        final FloatField floatField2 = struct2.field("floatField");
        final ShortField shortField2 = struct2.field("shortField");
        final DoubleField doubleField2 = struct2.field("doubleField");
        final LongField longField2 = struct2.field("longField");

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            storage1.write(bool1, booleans[i]);
            storage1.write(byteField1, bytes[i]);
            storage1.write(charField1, chars[i]);
            storage1.write(doubleField1, doubles[i]);
            storage1.write(floatField1, floats[i]);
            storage1.write(intField1, ints[i]);
            storage1.write(longField1, longs[i]);
            storage1.write(shortField1, shorts[i]);
            storage1.write(str1, "test" + ints[i]);
        }

        final ActionSet actions = storage1.transactionManager().commit();
        storage1.copyStorage(storage2);

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            storage2.selectStructure(i);

            assertEquals(message, storage1.read(bool1), storage2.read(bool2));

            assertEquals(message, storage1.read(byteField1),
                    storage2.read(byteField2));
            assertEquals(message, storage1.read(charField1),
                    storage2.read(charField2));
            assertEquals(message, storage1.read(doubleField1),
                    storage2.read(doubleField2), DELTA);
            assertEquals(message, storage1.read(floatField1),
                    storage2.read(floatField2), DELTA);
            assertEquals(message, storage1.read(intField1),
                    storage2.read(intField2));
            assertEquals(message, storage1.read(longField1),
                    storage2.read(longField2));
            assertEquals(message, storage1.read(shortField1),
                    storage2.read(shortField2));
            assertEquals(message, storage1.read(str1), storage2.read(str2));
        }
    }

    /** {@inheritDoc} */
    @Before
    @Override
    public void setup() {
        super.setup();
        for (int i = 0; i < COMPILERS.length; i++) {
            COMPILED[i].storage = COMPILED[i].storage.copy();
        }
    }

    @Test
    public void testCopy() {
        for (final CompiledStorage strg : COMPILED) {
            final Struct struct1 = strg.compiledStructs;
            final Storage s1 = strg.storage;
            final String msg = "TestCopyStruct.testCopy() :-";
            final BooleanField bool1 = struct1.field("booleanField1");
            final ByteField byField1 = struct1.field("byteField");
            final CharField cField1 = struct1.field("charField");
            final DoubleField dField1 = struct1.field("doubleField");
            final FloatField floatField1 = struct1.field("floatField");
            final IntField intField1 = struct1.field("intField");
            final LongField longField1 = struct1.field("longField");
            final ShortField shortField1 = struct1.field("shortField");
            final ObjectField<String, ?> str1 = struct1.field("stringField1");
            for (int i = 0; i < _CAPACITY; i++) {
                s1.selectStructure(i);
                s1.write(bool1, booleans[i]);
                s1.write(byField1, bytes[i]);
                s1.write(cField1, chars[i]);
                s1.write(dField1, doubles[i]);
                s1.write(floatField1, floats[i]);
                s1.write(intField1, ints[i]);
                s1.write(longField1, longs[i]);
                s1.write(shortField1, shorts[i]);
                s1.write(str1, "test" + ints[i]);
            }

            final ActionSet actions = s1.transactionManager().commit();
            final Storage s2 = s1.copy();
            for (int i = 0; i < _CAPACITY; i++) {
                s1.selectStructure(i);
                s2.selectStructure(i);
                assertEquals(msg, s1.read(bool1), s2.read(bool1));
                assertEquals(msg, s1.read(byField1), s2.read(byField1));
                assertEquals(msg, s1.read(cField1), s2.read(cField1));
                assertEquals(msg, s1.read(dField1), s2.read(dField1), DELTA);
                assertEquals(msg, s1.read(floatField1), s2.read(floatField1),
                        DELTA);
                assertEquals(msg, s1.read(intField1), s2.read(intField1));
                assertEquals(msg, s1.read(longField1), s2.read(longField1));
                assertEquals(msg, s1.read(shortField1), s2.read(shortField1));
                assertEquals(msg, s1.read(str1), s2.read(str1));
            }
        }
    }

    /* Copies data from Aligned64Storage to PackedStorage*/
    @Test
    public void testCopyDifferentTypes1() {
        final String message = "testCopyDifferentTypes1() :-";
        final Aligned64Compiler aligned64Compiler = new Aligned64Compiler();
        final CompilerBase packedCompiler = new PackedCompiler();
        final Struct struct1 = aligned64Compiler.compile(basestruct);
        final Aligned64Storage storage1 = (Aligned64Storage) aligned64Compiler
                .initStorage(struct1, _CAPACITY);
        final Struct struct2 = packedCompiler.compile(basestruct);
        final PackedStorage storage2 = (PackedStorage) packedCompiler
                .initStorage(struct2, _CAPACITY);
        compareResults(message, struct1, storage1, struct2, storage2);
    }

    /* Copies data from PackedStorage to Aligned64Storage*/
    @Test
    public void testCopyDifferentTypes2() {
        final String message = "testCopyDifferentTypes2() :-";
        final Aligned64Compiler aligned64Compiler = new Aligned64Compiler();
        final CompilerBase packedCompiler = new PackedCompiler();
        final Struct struct1 = packedCompiler.compile(basestruct);
        final PackedStorage storage1 = (PackedStorage) packedCompiler
                .initStorage(struct1, _CAPACITY);
        final Struct struct2 = aligned64Compiler.compile(basestruct);
        final Aligned64Storage storage2 = (Aligned64Storage) aligned64Compiler
                .initStorage(struct2, _CAPACITY);
        compareResults(message, struct1, storage1, struct2, storage2);
    }

    /* Copies data from PackedStorage to Aligned64Storage*/
    @Test
    public void testCopyDifferentTypes3() {
        final String message = "testCopyDifferentTypes2() :-";
        final Aligned64Compiler aligned64Compiler = new Aligned64Compiler();
        final CompilerBase packedCompiler = new PackedCompiler();
        final Struct struct1 = packedCompiler.compile(basestruct);
        final PackedSparseStorage storage1 = (PackedSparseStorage) packedCompiler
                .initStorage(struct1, _CAPACITY, true);
        final Struct struct2 = aligned64Compiler.compile(basestruct);
        final Aligned64SparseStorage storage2 = (Aligned64SparseStorage) aligned64Compiler
                .initStorage(struct2, _CAPACITY, true);
        compareResults(message, struct1, storage1, struct2, storage2);
    }
}
