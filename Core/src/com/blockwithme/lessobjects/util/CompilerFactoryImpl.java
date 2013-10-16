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
package com.blockwithme.lessobjects.util;

import static com.blockwithme.lessobjects.CompilerType.ALIGNED64;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.CompilerFactory;
import com.blockwithme.lessobjects.CompilerType;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.compiler.PackedCompiler;

/**
 * A factory for creating Compiler objects.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class CompilerFactoryImpl implements CompilerFactory {

    /** The aligned64 compiler. */
    private static Aligned64Compiler ALIGNED64_COMPILER = new Aligned64Compiler();

    /** The aligned64 compiler. */
    private static PackedCompiler PACKED_COMPILER = new PackedCompiler();

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Compiler createCompiler(final CompilerType theCompilerType) {
        return theCompilerType == ALIGNED64 ? ALIGNED64_COMPILER
                : PACKED_COMPILER;
    }
}
