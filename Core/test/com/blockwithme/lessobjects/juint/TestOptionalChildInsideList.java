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

import static com.blockwithme.lessobjects.juint.Constants.FACTORY;
import static org.junit.Assert.assertEquals;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.Test;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.compiler.CompilerBase;
import com.blockwithme.lessobjects.compiler.PackedCompiler;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * The Class TestCopyStruct.
 */
// CHECKSTYLE IGNORE FOR NEXT 300 LINES
@SuppressWarnings({ "PMD", "all" })
@ParametersAreNonnullByDefault
public class TestOptionalChildInsideList extends TestData {

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
            final Storage storage1) {

        final IntField intField1 = struct1.field("ListChild.intField");
        final LongField longField1 = struct1.field("ListChild.longField");
        final ObjectField<String, ?> str1 = struct1
                .field("ListChild.stringField1");

        final IntField intField2 = struct1
                .field("ListChild.OptionalChild.intField");
        final LongField longField2 = struct1
                .field("ListChild.OptionalChild.longField");
        final ObjectField<String, ?> str2 = struct1
                .field("ListChild.OptionalChild.stringField1");

        final Struct listChild = struct1.child("ListChild");

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            final Storage list = storage1.createOrClearList(listChild);

            for (int j = 0; j < 10; j++) {
                list.selectStructure(j);

                list.write(intField1, INTS[j]);
                list.write(longField1, LONGS[j]);
                list.write(str1, "test" + INTS[j]);

                list.write(intField2, INTS[j]);
                list.write(longField2, LONGS[j]);
                list.write(str2, "test" + INTS[j]);
            }
        }

        final ActionSet actions = storage1.transactionManager().commit();
        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);

            for (int j = 0; j < 10; j++) {
                final Storage list = storage1.list(listChild);

                list.selectStructure(j);
                assertEquals(message, list.read(intField1), INTS[j]);
                assertEquals(message, list.read(longField1), LONGS[j]);
                assertEquals(message, list.read(str1), "test" + INTS[j]);
                assertEquals(message, list.read(intField2), INTS[j]);
                assertEquals(message, list.read(longField2), LONGS[j]);
                assertEquals(message, list.read(str2), "test" + INTS[j]);

            }
        }
    }

    private Struct struct() {
        Struct optionalChild = new Struct("OptionalChild", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });
        optionalChild = optionalChild.setOptional(true);

        Struct listChild = new Struct("ListChild",
                new Struct[] { optionalChild }, new Field<?, ?>[] {
                        FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });
        listChild = listChild.setList(true);

        final Struct bs = new Struct("BaseStruct", new Struct[] { listChild },
                new Field<?, ?>[] { FACTORY.newIntField("intField") });
        return bs;
    }

    /* Copies data from Aligned64Storage to PackedStorage*/
    @Test
    public void testListInsideList() {
        for (final com.blockwithme.lessobjects.Compiler cmplr : Constants.COMPILERS) {
            final String message = "testListInsideList() :-";
            final CompilerBase packedCompiler = new PackedCompiler();
            final Struct bs = struct();
            final Struct struct1 = cmplr.compile(bs);
            final Storage storage1 = cmplr.initStorage(struct1, _CAPACITY);
            compareResults(message, struct1, storage1);
        }
    }

    @Test
    public void testSchema() {
        checkSchema(struct());
    }
}
