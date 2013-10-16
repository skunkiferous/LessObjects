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
package com.blockwithme.lessobjects.bench;

import com.blockwithme.lessobjects.FieldFactory;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.compiler.CompilerBase;
import com.blockwithme.lessobjects.compiler.PackedCompiler;
import com.blockwithme.lessobjects.util.FieldFactoryImpl;

//CHECKSTYLE IGNORE FOR NEXT 50 LINES

/**
 * The Constants.
 *
 * @author tarung
 */
public interface Constants {

    /** The factory. */
    FieldFactory FACTORY = new FieldFactoryImpl();

    /** The 64 K chunk size */
    int _64K = 2 * 2 * 2;// 40 * 40 * 40;

    /** The Aligned 64 compiler */
    Aligned64Compiler ALIGNED64 = new Aligned64Compiler();

    /** Number of Elements to be READ */
    int ELEMENTS_READ = _64K;

    /** Number of Elements to be Written */
    int ELEMENTS_WRITTEN = _64K;

    /** The packed compiler*/
    CompilerBase PACKED = new PackedCompiler();

    /** The * iterations. */
    int _ITERATIONS = 50; // 20;

    /** The read iterations. */
    int READ_ITERATIONS = _ITERATIONS;

    /** The read union iterations. */
    int READ_ITERATIONS_UNION = _ITERATIONS;

    /** The write iterations non-transactional. */
    int WRITE_ITERATIONS_NON_TRANSACTIONAL = _ITERATIONS;

    /** The write iterations transactional. */
    int WRITE_ITERATIONS_TRANSACTIONAL = _ITERATIONS;

    /** The write union iterations non-transactional. */
    int WRITE_ITERATIONS_UNION_NON_TRANSACTIONAL = _ITERATIONS;

    /** The write union iterations transactional. */
    int WRITE_ITERATIONS_UNION_TRANSACTIONAL = _ITERATIONS;

}
