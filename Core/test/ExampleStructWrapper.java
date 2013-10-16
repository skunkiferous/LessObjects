
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
import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Factory;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.storage.Storage;
//Do not delete these classes as this is a dummy class used by the design diagrams
//CHECKSTYLE IGNORE FOR NEXT 50 LINES

@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
/**
 * An Example a class that uses Struct.
 */
public class ExampleStructWrapper {

    /** Fields can be initialized with 'Default Value'*/
//    private static final int intDefaultValue = 10;

    /** The Field Factory static instance. */
    public static final FieldFactory FACTORY = new Factory().fieldFactory();

    /** The size of 'intField' in bits, indicates the number of
     * bits to be allocated in the storage object*/
    public static final int INT_FIELD_BITS = 24;

    /** Use this if the structure is a Struct. */
    public static final boolean STRUCT = false;

    /** Use this if the structure is an Union. */
    public static final boolean UNION = true;

    /** The boolean field instance. */
    private BooleanField booleanField;

    /** The compiler. */
    private final Compiler compiler;

    /** The int field instance */
    private IntField intField;

    /** The struct. */
    private final Struct struct;

    public static void main(final String[] args) {
        final ExampleStructWrapper wrapper = new ExampleStructWrapper(
                new Aligned64Compiler());
    }

    /**
    * The public constructor
    */
    public ExampleStructWrapper(final Compiler theCompiler) {

        struct = theCompiler.compile(new Struct("ExampleStruct", STRUCT,
                new Struct[] {}, new Field<?, ?>[] {
                        FACTORY.newBooleanField("testBoolean"),
                        FACTORY.newIntField("intField", INT_FIELD_BITS) }));

        booleanField = struct.field("testBoolean");
        intField = struct.field("intField");
        compiler = theCompiler;
    }

    public Storage createStroage(final int capacity) {
        return compiler.initStorage(struct, capacity);
    }

    /**
     * @return the booleanField
     */
    public BooleanField getBooleanField() {
        return booleanField;
    }

    /**
     * @return the intField
     */
    public IntField getIntField() {
        return intField;
    }

    /**
     * @param booleanField the booleanField to set
     */
    public void setBooleanField(final BooleanField booleanField) {
        this.booleanField = booleanField;
    }

    /**
     * @param intField the intField to set
     */
    public void setIntField(final IntField intField) {
        this.intField = intField;
    }

}
