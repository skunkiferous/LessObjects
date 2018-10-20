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

import static com.blockwithme.lessobjects.bench.Constants.ALIGNED64;
import static com.blockwithme.lessobjects.bench.Constants.ELEMENTS_READ;
import static com.blockwithme.lessobjects.bench.Constants.PACKED;
import static com.blockwithme.lessobjects.bench.Constants.READ_ITERATIONS;
import static com.blockwithme.lessobjects.bench.Constants._64K;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.blockwithme.lessobjects.Compiler;
import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

//CHECKSTYLE IGNORE FOR NEXT 600 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
@AxisRange(min = 0, max = 3)
@BenchmarkMethodChart(filePrefix = "ReadOptionalChildrenTest")
public class ReadOptionalChildrenTest extends AbstractBenchmark {

    protected StorageUtilOptionalChildren util = null;

    @Rule
    public TestName name = new TestName();

    /** Sets the benchmark properties, for stats and graphics generation. */
    static {
        System.setProperty("jub.consumers", "CONSOLE,H2");
        System.setProperty("jub.db.file", "benchmarks");
        System.setProperty("jub.charts.dir", "benchmarks/charts");
    }

    @Before
    public void _setUp() {
        final String methodName = name.getMethodName();
        final Compiler compiler = methodName.contains("Aligned64") ? ALIGNED64
                : PACKED;
        util = new StorageUtilOptionalChildren(compiler, _64K,
                false, true);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Random() {
        util.randomReads(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Sequential() {
        util.sequentialReads(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Random() {
        util.randomReads(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Sequential() {
        util.sequentialReads(ELEMENTS_READ, READ_ITERATIONS);
    }
}
