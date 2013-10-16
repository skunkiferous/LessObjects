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

import static com.blockwithme.lessobjects.juint.Constants.DELTA;
import static com.blockwithme.lessobjects.juint.Constants.FACTORY;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.Test;

import com.blockwithme.lessobjects.Field;
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
import com.blockwithme.lessobjects.storage.aligned64.Aligned64Storage;
import com.blockwithme.lessobjects.storage.packed.PackedStorage;

/**
 * The Class TestCopyStruct.
 */
// CHECKSTYLE IGNORE FOR NEXT 300 LINES
@SuppressWarnings({ "PMD", "all" })
@ParametersAreNonnullByDefault
public class TestCopyStructWithListChildren extends TestData {

    final static Random rand = new Random(System.currentTimeMillis());

    private void compareResults(final String message, final Struct struct1,
            final Storage storage1, final Struct struct2, final Storage storage2) {

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

        final BooleanField bool1 = struct1
                .field("CopyStructListTest.booleanField1");
        final ByteField byteField1 = struct1
                .field("CopyStructListTest.byteField");
        final CharField charField1 = struct1
                .field("CopyStructListTest.charField");
        final DoubleField doubleField1 = struct1
                .field("CopyStructListTest.doubleField");
        final FloatField floatField1 = struct1
                .field("CopyStructListTest.floatField");
        final IntField intField1 = struct1.field("CopyStructListTest.intField");
        final LongField longField1 = struct1
                .field("CopyStructListTest.longField");
        final ShortField shortField1 = struct1
                .field("CopyStructListTest.shortField");
        final ObjectField<String, ?> str1 = struct1
                .field("CopyStructListTest.stringField1");

        final BooleanField bool2 = struct2
                .field("CopyStructListTest.booleanField1");
        final ByteField byteField2 = struct2
                .field("CopyStructListTest.byteField");
        final CharField charField2 = struct2
                .field("CopyStructListTest.charField");
        final DoubleField doubleField2 = struct2
                .field("CopyStructListTest.doubleField");
        final FloatField floatField2 = struct2
                .field("CopyStructListTest.floatField");
        final IntField intField2 = struct2.field("CopyStructListTest.intField");
        final LongField longField2 = struct2
                .field("CopyStructListTest.longField");
        final ShortField shortField2 = struct2
                .field("CopyStructListTest.shortField");
        final ObjectField<String, ?> str2 = struct2
                .field("CopyStructListTest.stringField1");

        final Struct listChild = struct1.child("CopyStructListTest");
        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            final Storage listStorage = storage1.createOrClearList(listChild);
            for (int j = 0; j < 10; j++) {
                listStorage.write(bool1, booleans[i]);
                listStorage.write(byteField1, bytes[i]);
                listStorage.write(charField1, chars[i]);
                listStorage.write(doubleField1, doubles[i]);
                listStorage.write(floatField1, floats[i]);
                listStorage.write(intField1, ints[i]);
                listStorage.write(longField1, longs[i]);
                listStorage.write(shortField1, shorts[i]);
                listStorage.write(str1, "test" + ints[i]);
            }

        }

        final ActionSet actions = storage1.transactionManager().commit();
        storage1.copyStorage(storage2);

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            storage2.selectStructure(i);

            final Storage listStorage1 = storage1.list(listChild);
            final Storage listStorage2 = storage2.list(listChild);

            for (int j = 0; j < 10; j++) {
                assertEquals(message, listStorage1.read(bool1),
                        listStorage2.read(bool2));
                assertEquals(message, listStorage1.read(byteField1),
                        listStorage2.read(byteField2));
                assertEquals(message, listStorage1.read(charField1),
                        listStorage2.read(charField2));
                assertEquals(message, listStorage1.read(doubleField1),
                        listStorage2.read(doubleField2), DELTA);
                assertEquals(message, listStorage1.read(floatField1),
                        listStorage2.read(floatField2), DELTA);
                assertEquals(message, listStorage1.read(intField1),
                        listStorage2.read(intField2));
                assertEquals(message, listStorage1.read(longField1),
                        listStorage2.read(longField2));
                assertEquals(message, listStorage1.read(shortField1),
                        listStorage2.read(shortField2));
                assertEquals(message, listStorage1.read(str1),
                        listStorage2.read(str2));
            }
        }
    }

    private Struct struct() {
        Struct bs_child = new Struct("CopyStructListTest", new Struct[] {},
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
        bs_child = bs_child.setList(true);

        final Struct bs = new Struct("BaseStruct", new Struct[] { bs_child },
                new Field<?, ?>[] { FACTORY.newIntField("intField") });

        return bs;
    }

    /* Copies data from Aligned64Storage to PackedStorage*/
    @Test
    public void testCopyDifferentTypes1() {
        final String message = "testCopyDifferentTypes1() :-";
        final Aligned64Compiler aligned64Compiler = new Aligned64Compiler();
        final CompilerBase packedCompiler = new PackedCompiler();
        final Struct bs = struct();
        final Struct struct1 = aligned64Compiler.compile(bs);
        final Aligned64Storage storage1 = (Aligned64Storage) aligned64Compiler
                .initStorage(struct1, _CAPACITY);
        final Struct struct2 = packedCompiler.compile(bs);
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
        final Struct bs = struct();
        final Struct struct1 = packedCompiler.compile(bs);
        final PackedStorage storage1 = (PackedStorage) packedCompiler
                .initStorage(struct1, _CAPACITY);
        final Struct struct2 = aligned64Compiler.compile(bs);
        final Aligned64Storage storage2 = (Aligned64Storage) aligned64Compiler
                .initStorage(struct2, _CAPACITY);
        compareResults(message, struct1, storage1, struct2, storage2);
    }
}
