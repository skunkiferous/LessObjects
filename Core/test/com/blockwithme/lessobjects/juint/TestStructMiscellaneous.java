/**
 *
 */
// $codepro.audit.disable
package com.blockwithme.lessobjects.juint;

import static com.blockwithme.lessobjects.util.StructConstants.FACTORY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blockwithme.lessobjects.Array;
import com.blockwithme.lessobjects.ChildrenArray;
import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
/** The Class TestCoreClasses. */
@SuppressWarnings({ "PMD", "all" })
public class TestStructMiscellaneous extends TestData {
    private void testResize(final Storage strg, final int theNewCapacity) {
        final ByteField bf = strg.struct().field("byte1");
        final IntOptionalField intOptional = strg.struct().field("intOptional");
        final ObjectField<String, ?> strField = strg.struct().field(
                "stringField");

        strg.resizeStorage(theNewCapacity);

        for (int i = 0; i < theNewCapacity; i++) {
            strg.selectStructure(i);
            strg.write(bf, (byte) i);
            strg.write(intOptional, i);
            strg.write(strField, "String" + i);
        }
        for (int i = 0; i < theNewCapacity; i++) {
            strg.selectStructure(i);
            assertEquals((byte) i, strg.read(bf));
            assertEquals(i, strg.read(intOptional));
            assertEquals("String" + i, strg.read(strField));
        }
    }

    /**
     * @return
     */
    private Struct testStruct() {
        final Struct testStruct = new Struct("Test", false, null, new Field[] {
                FACTORY.newBooleanField("dummyBoolean"),
                FACTORY.newIntField("int1"), FACTORY.newByteField("byte1"),
                FACTORY.newIntOptional("intOptional"),
                FACTORY.newByteOptional("byteOptional"),
                FACTORY.newCharOptional("charOptional"),
                FACTORY.newFloatOptional("floatOptional"),
                FACTORY.newStringField("stringField") });
        return testStruct;
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateChildNames() {
        final Struct c1 = new Struct("Test", false, null,
                new Field[] { FACTORY.newStringField("myString") });
        final Struct c2 = new Struct("Test", false, null,
                new Field[] { FACTORY.newStringField("someString") });
        final Struct s = new Struct("Parent", false, null, new Struct[] { c1,
                c2 }, new Field[] { FACTORY.newStringField("someString") });

    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateFieldNames() {
        final Struct testStruct = new Struct("Test", false, null,
                new Field[] { FACTORY.newStringField("myName"),
                        FACTORY.newIntField("myName") });
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidFieldNames() {
        final Struct testStruct = new Struct("Test", false, null,
                new Field[] { FACTORY.newStringField("myNam!@#!@#e") });
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidName1() {
        final Struct c1 = new Struct("Test#@!@#!", false, null,
                new Field[] { FACTORY.newStringField("myString") });
    }

    @Test
    public void testChidrenArray() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct testStruct = new Struct("arrayTest", (Struct[]) null,
                    new Field[] { FACTORY.newIntField("int1"),
                            FACTORY.newByteField("byte1") });
            final ChildrenArray array = new ChildrenArray("TestArray",
                    testStruct, 10);
            cmplr.initStorage(cmplr.compile(array.arrayStruct()), 10);
        }
    }

    @Test
    public void testStorageResize() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct testStruct = new Struct("Test", false, null,
                    new Field[] { FACTORY.newBooleanField("dummyBoolean"),
                            FACTORY.newIntField("int1"),
                            FACTORY.newByteField("byte1"),
                            FACTORY.newIntOptional("intOptional"),
                            FACTORY.newStringField("stringField") });

            final Struct compiled = cmplr.compile(testStruct);
            final Storage strg = cmplr.initStorage(compiled, 10);
            testResize(strg, 100);
            testResize(strg, 1000);
            testResize(strg, 10);
            testResize(strg, 100);
        }
    }

    @Test
    public void testStringArrayOnly() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct testStruct = new Struct("Test", false, null,
                    new Field[] { FACTORY.newStringField("myString") });
            final ChildrenArray array = new ChildrenArray("TestArray",
                    testStruct, 10);
            cmplr.initStorage(cmplr.compile(array.arrayStruct()), 10);
        }
    }

    @Test
    public void testStringOnly() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            Struct testStruct = new Struct("Test", false, null,
                    new Field[] { FACTORY.newStringField("myString") });
            testStruct = cmplr.compile(testStruct);
            cmplr.initStorage(testStruct, 10);
        }
    }

    @Test
    public void testStringStringArray() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            Struct testStruct = new Struct("Test", false,
                    new Struct[] { new Array("stringarray", FACTORY
                            .newStringField("myString"), 10).arrayStruct() },
                    new Field[] { FACTORY.newStringField("myString1") });
            testStruct = cmplr.compile(testStruct);
            cmplr.initStorage(testStruct, 10);
        }
    }

    @Test
    public void testStructWithChildren() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct struct1 = new Struct("one", (Struct[]) null,
                    new Field[] { FACTORY.newIntField("int1"),
                            FACTORY.newByteField("byte1") });
            final Struct struct2 = new Struct("two", (Struct[]) null,
                    new Field[] { FACTORY.newIntField("int1"),
                            FACTORY.newByteField("byte1") });
            Struct testStruct = new Struct("Test", false, new Struct[] {
                    struct1, struct2 }, null);
            testStruct = cmplr.compile(testStruct);
            cmplr.initStorage(testStruct, 10);
        }
    }

    @Test
    public void testUnionWithChildren() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct struct1 = new Struct("one", (Struct[]) null,
                    new Field[] { FACTORY.newIntField("int1"),
                            FACTORY.newByteField("byte1") });
            final Struct struct2 = new Struct("two", (Struct[]) null,
                    new Field[] { FACTORY.newIntField("int1"),
                            FACTORY.newByteField("byte1") });

            Struct testStruct = new Struct("Test", true, new Struct[] {
                    struct1, struct2 }, null);
            testStruct = cmplr.compile(testStruct);
            cmplr.initStorage(testStruct, 10);
        }
    }

    @Test
    public void testWithStorageSize() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct testStruct = testStruct();
            final Struct compiled = cmplr.compile(testStruct);
            final Storage strg = cmplr.initStorage(compiled,
                    Character.MAX_VALUE);
        }
    }

    @Test
    public void testWithStorageSize2() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct testStruct = testStruct();
            final Struct compiled = cmplr.compile(testStruct);
            final Storage strg2 = cmplr.initStorage(compiled,
                    Character.MAX_VALUE + 2);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStorageSize4() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final Struct testStruct = testStruct();
            final Struct compiled = cmplr.compile(testStruct);
            final Storage strg3 = cmplr.initStorage(compiled,
                    1024 * 1024 * 1024 + 1);
        }
    }
}
