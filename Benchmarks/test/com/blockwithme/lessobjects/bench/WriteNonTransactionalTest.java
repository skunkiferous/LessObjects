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

import static com.blockwithme.lessobjects.bench.Constants.ELEMENTS_WRITTEN;
import static com.blockwithme.lessobjects.bench.Constants.WRITE_ITERATIONS_NON_TRANSACTIONAL;

import org.junit.Before;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

//CHECKSTYLE IGNORE FOR NEXT 600 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "WriteNonTransactionalTest")
public class WriteNonTransactionalTest extends WriteBenchmark {

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Random_Wrapper() {
        util.randomWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Random_Wrapper2L() {
        util.randomWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Sequential_Wrapper() {
        util.sequentialWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Aligned64_Sequential_Wrapper2L() {
        util.sequentialWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Random_Wrapper() {
        util.randomWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Random_Wrapper2L() {
        util.randomWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Sequential_Wrapper() {
        util.sequentialWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 2)
    public void _64K_Packed_Sequential_Wrapper2L() {
        util.sequentialWritesWrapper(ELEMENTS_WRITTEN, numberOfIterations);
    }

    @Before
    public void _setUp() {
        init(false, WRITE_ITERATIONS_NON_TRANSACTIONAL);
    }
}
