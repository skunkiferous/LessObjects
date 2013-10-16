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

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.Test;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.compiler.CompilerBase;
import com.blockwithme.lessobjects.compiler.PackedCompiler;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * The Class TestCopyStruct.
 */
// CHECKSTYLE IGNORE FOR NEXT 300 LINES
@SuppressWarnings({ "PMD", "all" })
@ParametersAreNonnullByDefault
public class TestOptionalChildInsideOptionalChild extends TestData {

    final static boolean[] BOOLEANS = booleans();
    final static byte[] BYTES = bytes();
    final static char[] CHARS = chars();
    final static double[] DOUBLES = doubles();
    final static float[] FLOATS = floats();
    final static int[] INTS = ints();
    final static long[] LONGS = longs();
    final static short[] SHORTS = shorts();
    final static String[] STRINGS = strings();

    private void compareResults(final String message, final Struct struct1,
            final Storage storage1, final Storage storage2) {

        final IntField intField1 = struct1.field("OptionalParent.intField");
        final LongField longField1 = struct1.field("OptionalParent.longField");
        final ObjectField<String, ?> str1 = struct1
                .field("OptionalParent.stringField1");

        final IntField intField2 = struct1
                .field("OptionalParent.OptionalChild.intField");
        final LongField longField2 = struct1
                .field("OptionalParent.OptionalChild.longField");
        final ObjectField<String, ?> str2 = struct1
                .field("OptionalParent.OptionalChild.stringField1");

        final LongField lField = struct1
                .field("OptionalParent.OptionalChild.lField");
        final CharField cField = struct1
                .field("OptionalParent.OptionalChild.cField");
        final ByteField bField = struct1
                .field("OptionalParent.OptionalChild.bField");
        final BooleanField booleanField = struct1
                .field("OptionalParent.OptionalChild.booleanField");
        final FloatField floatField = struct1
                .field("OptionalParent.OptionalChild.floatField");
        final ShortField shortField = struct1
                .field("OptionalParent.OptionalChild.shortField");

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            storage1.write(intField1, INTS[i]);
            storage1.write(longField1, LONGS[i]);
            storage1.write(str1, "test" + INTS[i]);
            final int j = _CAPACITY - i - 1;
            storage1.write(intField2, INTS[j]);
            storage1.write(longField2, LONGS[j]);
            storage1.write(lField, Math.abs(INTS[j]));
            storage1.write(cField, CHARS[j]);
            storage1.write(bField, BYTES[j]);
            storage1.write(booleanField, BOOLEANS[j]);
            storage1.write(floatField, FLOATS[j]);
            storage1.write(shortField, SHORTS[j]);
        }

        final ActionSet actions = storage1.transactionManager().commit();

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            assertEquals(message, storage1.read(intField1), INTS[i]);
            assertEquals(message, storage1.read(longField1), LONGS[i]);
            assertEquals(message, storage1.read(str1), "test" + INTS[i]);
            final int j = _CAPACITY - i - 1;
            assertEquals(message, storage1.read(intField2), INTS[j]);
            assertEquals(message, storage1.read(longField2), LONGS[j]);
            assertEquals(storage1.read(lField), Math.abs(INTS[j]));
            assertEquals(storage1.read(cField), CHARS[j]);
            assertEquals(storage1.read(bField), BYTES[j]);
            assertEquals(storage1.read(booleanField), BOOLEANS[j]);
            assertEquals(storage1.read(floatField), FLOATS[j], DELTA);
            assertEquals(storage1.read(shortField), SHORTS[j]);
        }
        storage1.copyStorage(storage2);
        for (int i = 0; i < _CAPACITY; i++) {
            storage2.selectStructure(i);
            assertEquals(storage2.read(intField1), INTS[i]);
            assertEquals(storage2.read(longField1), LONGS[i]);
            assertEquals(storage2.read(str1), "test" + INTS[i]);
            final int j = _CAPACITY - i - 1;
            assertEquals(storage2.read(intField2), INTS[j]);
            assertEquals(storage2.read(longField2), LONGS[j]);
            assertEquals(storage2.read(lField), Math.abs(INTS[j]));
            assertEquals(storage2.read(cField), CHARS[j]);
            assertEquals(storage2.read(bField), BYTES[j]);
            assertEquals(storage2.read(booleanField), BOOLEANS[j]);
            assertEquals(storage2.read(floatField), FLOATS[j], DELTA);
            assertEquals(storage2.read(shortField), SHORTS[j]);
        }

    }

    private Struct struct() {

        Struct optionalChild = new Struct("OptionalChild", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newLongField("lField"),
                        FACTORY.newCharField("cField"),
                        FACTORY.newByteField("bField"),
                        FACTORY.newBooleanField("booleanField"),
                        FACTORY.newFloatField("floatField"),
                        FACTORY.newShortField("shortField"),
                        FACTORY.newStringField("stringField1") });
        optionalChild = optionalChild.setOptional(true);

        Struct optionalParent = new Struct("OptionalParent",
                new Struct[] { optionalChild }, new Field<?, ?>[] {
                        FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });
        optionalParent = optionalParent.setOptional(true);

        final Struct bs = new Struct("Struct", new Struct[] { optionalParent },
                new Field<?, ?>[] { FACTORY.newIntField("intField") });
        return bs;
    }

    /* Copies data from Aligned64Storage to PackedStorage*/
    @Test
    public void testOptionalInsideOptional() {
        for (final com.blockwithme.lessobjects.Compiler cmplr : Constants.COMPILERS) {
            final String message = "testListInsideList() :-";
            final CompilerBase packedCompiler = new PackedCompiler();
            final Struct bs = struct();
            final Struct struct1 = cmplr.compile(bs);
            final Storage storage1 = cmplr.initStorage(struct1, _CAPACITY);
            final Storage storage2 = cmplr.initStorage(struct1, _CAPACITY);
            compareResults(message, struct1, storage1, storage2);
        }
    }
}
