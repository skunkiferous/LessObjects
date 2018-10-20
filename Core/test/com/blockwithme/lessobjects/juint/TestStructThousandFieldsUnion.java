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
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.juint.struct.StructVariableSize;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestStructThousandFieldsUnion {

    private static final int CAPACITY = 10;

    private static final int fields = 1000;

    /** The Constant BYTES. */
    private static final byte[][] XBYTES = new byte[CAPACITY][fields];

    final static Random rand = new Random(System.currentTimeMillis());

    private CompiledStorage[] compiled;

    @BeforeClass
    public static void setUpClass() {
        for (int i = 0; i < CAPACITY; i++) {
            for (int j = 0; j < fields; j++) {
                XBYTES[i][j] = (byte) rand.nextInt();
            }
        }
    }

    @Before
    public void setup() {
        int count = 0;
        compiled = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            final StructVariableSize tmp = new StructVariableSize("Union",
                    fields, true, cmplr);
            compiled[count] = new CompiledStorage();
            compiled[count].compiledStructs = tmp.struct();
            compiled[count].storage = cmplr.initStorage(
                    compiled[count].compiledStructs, CAPACITY);
            compiled[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testMaxFields() {

        for (final CompiledStorage strg : compiled) {

            final String message = "TestStructMaxFields.testMaxFields() Test failed for Compiler -"
                    + strg.compiler.compilerName();

            final Field<?, ?>[] fields = strg.compiledStructs.fields();

            for (int i = 0; i < CAPACITY; i++) {
                strg.storage.selectStructure(i);
                for (int j = 1; j < fields.length; j++) {
                    strg.storage.clear();
                    strg.storage.selectUnionPosition(fields[j]);
                    strg.storage.write((ByteField) fields[j], XBYTES[i][j - 1]);
                    assertEquals(XBYTES[i][j - 1],
                            strg.storage.read((ByteField) fields[j]));
                }
            }
        }
    }

    @Test
    public void testSchema() {
        for (final CompiledStorage strg : compiled) {
            final Struct compiledStructs = strg.compiledStructs;
            TestData.checkSchema(compiledStructs);
        }
    }
}
