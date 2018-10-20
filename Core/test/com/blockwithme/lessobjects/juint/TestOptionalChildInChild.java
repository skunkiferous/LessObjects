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
public class TestOptionalChildInChild extends TestData {

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

        final IntField intField1 = struct1.field("NormalChild.intField");
        final LongField longField1 = struct1.field("NormalChild.longField");
        final ObjectField<String, ?> str1 = struct1
                .field("NormalChild.stringField1");

        final IntField intField2 = struct1
                .field("NormalChild.OptionalChild.intField");
        final LongField longField2 = struct1
                .field("NormalChild.OptionalChild.longField");
        final ObjectField<String, ?> str2 = struct1
                .field("NormalChild.OptionalChild.stringField1");

        for (int i = 0; i < _CAPACITY; i++) {
            storage1.selectStructure(i);
            storage1.write(intField1, INTS[i]);
            storage1.write(longField1, LONGS[i]);
            storage1.write(str1, "test" + INTS[i]);
            final int j = _CAPACITY - i - 1;
            storage1.write(intField2, INTS[j]);
            storage1.write(longField2, LONGS[j]);
            storage1.write(str2, "test" + INTS[j]);
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
            assertEquals(message, storage1.read(str2), "test" + INTS[j]);
        }
    }

    private Struct struct() {
        Struct optionalChild = new Struct("OptionalChild", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });
        optionalChild = optionalChild.setOptional(true);

        final Struct normalChild = new Struct("NormalChild",
                new Struct[] { optionalChild }, new Field<?, ?>[] {
                        FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newStringField("stringField1") });

        final Struct bs = new Struct("Struct", new Struct[] { normalChild },
                new Field<?, ?>[] { FACTORY.newIntField("intField") });

        return bs;
    }

    /** Copies data from Aligned64Storage to PackedStorage*/
    @Test
    public void testOptionalInsideOptional() {
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
