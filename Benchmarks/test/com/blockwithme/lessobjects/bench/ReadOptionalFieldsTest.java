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
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "ReadOptionalFieldsTest")
public class ReadOptionalFieldsTest extends AbstractBenchmark {

    private byte probability;

    protected StorageUtil util = null;

    @Rule
    public TestName name = new TestName();

    /** Sets the benchmark properties, for stats and graphics generation. */
    static {
        System.setProperty("jub.consumers", "CONSOLE,H2");
        System.setProperty("jub.db.file", "benchmarks");
        System.setProperty("jub.charts.dir", "benchmarks/charts");
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Random() {
        util.randomOptionalReads(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Random_Proxy() {
        util.randomOptionalReadsProxy(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Sequential() {
        util.sequentialOptionalReads(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Sequential_Proxy() {
        util.sequentialOptionalReadsProxy(ELEMENTS_READ, READ_ITERATIONS);
    }

    @Before
    public void _setUp() {
        final String methodName = name.getMethodName();
        // any compiler should be okay since there is no storage field for this
        // test
        final Compiler compiler = ALIGNED64;
        final boolean createStorage = !methodName.contains("Proxy");
        final boolean createProxy = methodName.contains("Proxy");

        util = new StorageUtil(compiler, _64K, false, true, createStorage,
                createProxy, false, false, false, true);
    }
}
