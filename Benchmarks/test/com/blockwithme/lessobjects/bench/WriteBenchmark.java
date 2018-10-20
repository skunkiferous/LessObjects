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
import static com.blockwithme.lessobjects.bench.Constants.ELEMENTS_WRITTEN;
import static com.blockwithme.lessobjects.bench.Constants.PACKED;
import static com.blockwithme.lessobjects.bench.Constants._64K;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.blockwithme.lessobjects.Compiler;
import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

//CHECKSTYLE IGNORE FOR NEXT 600 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public abstract class WriteBenchmark extends AbstractBenchmark {

    protected int numberOfIterations;

    protected StorageUtil util = null;

    @Rule
    public TestName name = new TestName();

    /** Sets the benchmark properties, for stats and graphics generation. */
    static {
        System.setProperty("jub.consumers", "CONSOLE,H2");
        System.setProperty("jub.db.file", "benchmarks");
        System.setProperty("jub.charts.dir", "benchmarks/charts");
    }

    protected void init(final boolean recordChanges,
            final int theNumberOfIterations) {
        final String methodName = name.getMethodName();
        final Compiler compiler = methodName.contains("Aligned64") ? ALIGNED64
                : PACKED;
        final boolean prePopulate = methodName.contains("reads");
        final boolean createStorage = !methodName.contains("Proxy");
        final boolean createProxy = methodName.contains("Proxy");
        final boolean createBean = methodName.contains("Bean");
        final boolean createWrapper = methodName.contains("Wrapper");
        final boolean createWrapper2L = methodName.contains("Wrapper2L");

        util = new StorageUtil(compiler, _64K, recordChanges, prePopulate,
                createStorage, createProxy, createBean, createWrapper,
                createWrapper2L, false);

        numberOfIterations = theNumberOfIterations;

    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Random() {
        util.randomWrites(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Random_Proxy() {
        util.randomWritesProxy(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Sequential() {
        util.sequentialWrites(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Sequential_Proxy() {
        util.sequentialWritesProxy(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Bean() {
        util.writeBean(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Random() {
        util.randomWrites(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Random_Proxy() {
        util.randomWritesProxy(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Sequential() {
        util.sequentialWrites(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Sequential_Proxy() {
        util.sequentialWritesProxy(ELEMENTS_WRITTEN, numberOfIterations);
    }

}
