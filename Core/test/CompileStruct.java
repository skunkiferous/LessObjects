
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
import com.blockwithme.lessobjects.CompilerType;
import com.blockwithme.lessobjects.Factory;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * This example shows how to compile a Struct.
 */
// Do not delete these classes as this is a dummy class used by the design
// diagrams
// CHECKSTYLE IGNORE FOR NEXT 700 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class CompileStruct {

    /** The factory. */
    public static Factory _FACTORY = new Factory();

    /** The Aligned64 compiler instance. */
    public static Compiler ALIGNED64_COMPILER = _FACTORY.compilerFactory()
            .createCompiler(CompilerType.ALIGNED64);

    /** The Packed Compiler instance. */
    public static Compiler PACKED_COMPILER = _FACTORY.compilerFactory()
            .createCompiler(CompilerType.PACKED);

    public static void main(final String[] args) {

        final FieldFactory factory = new Factory().fieldFactory();
        final Compiler compiler = new Aligned64Compiler();
        final int intFieldSize = 20;
        //final int intFieldDefaultValue = 100;

        Struct struct = compiler.compile(new Struct("ExampleStruct", true,
                new Struct[] {}, new Field<?, ?>[] {
                        factory.newBooleanField("testBoolean"),
                        factory.newIntField("intField", intFieldSize) }));

        // Pass it to the compiler and use the return value as compiled copy.
        struct = compiler.compile(struct);

        // creating a 1K fixed size storage.
        final Storage storage = compiler.initStorage(struct, 1024);
    }
}
