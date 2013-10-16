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
public class TestListInsideList extends TestData {
    private void compareResults(final String message, final Struct struct1,
            final Storage storage1) {

        final boolean[] BOOLEANS = booleans();
        final byte[] BYTES = bytes();
        final char[] CHARS = chars();
        final double[] DOUBLES = doubles();
        final float[] FLOATS = floats();
        final int[] INTS = ints();
        final long[] LONGS = longs();
        final short[] SHORTS = shorts();
        final String[] STRINGS = strings();

        final IntField intField1 = struct1.field("ListParent.intField");
        final LongField longField1 = struct1.field("ListParent.longField");
        final ObjectField<String, ?> str1 = struct1
                .field("ListParent.stringField1");

        final IntField intField2 = struct1
                .field("ListParent.ListChild.intField");
        final LongField longField2 = struct1
                .field("ListParent.ListChild.longField");
        final ObjectField<String, ?> str2 = struct1
                .field("ListParent.ListChild.stringField1");

        final Struct listChild = struct1.child("ListParent");
        final Struct secondlevel = listChild.child("ListChild");

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            final Storage list1 = storage1.createOrClearList(listChild);

            for (int j = 0; j < 10; j++) {
                list1.selectStructure(j);
                list1.write(intField1, INTS[j]);
                list1.write(longField1, LONGS[j]);
                list1.write(str1, "test" + INTS[j]);
                final Storage list2 = list1.createOrClearList(secondlevel);
                for (int k = 0; k < 10; k++) {
                    list2.selectStructure(k);
                    list2.write(intField2, INTS[k]);
                    list2.write(longField2, LONGS[k]);
                    list2.write(str2, "test" + INTS[k]);
                }
            }

        }

        final ActionSet actions = storage1.transactionManager().commit();

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            final Storage list1 = storage1.list(listChild);

            for (int j = 0; j < 10; j++) {
                list1.selectStructure(j);
                assertEquals(message, list1.read(intField1), INTS[j]);
                assertEquals(message, list1.read(longField1), LONGS[j]);
                assertEquals(message, list1.read(str1), "test" + INTS[j]);

                final Storage list2 = list1.list(secondlevel);

                for (int k = 0; k < 10; k++) {
                    list2.selectStructure(k);
                    assertEquals(message, list2.read(intField2), INTS[k]);
                    assertEquals(message, list2.read(longField2), LONGS[k]);
                    assertEquals(message, list2.read(str2), "test" + INTS[k]);
                }
            }
        }
    }

    private Struct struct() {
        Struct listChild = new Struct("ListChild", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });
        listChild = listChild.setList(true);
        Struct listParent = new Struct("ListParent",
                new Struct[] { listChild }, new Field<?, ?>[] {
                        FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });
        listParent = listParent.setList(true);
        final Struct bs = new Struct("Struct", new Struct[] { listParent },
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
