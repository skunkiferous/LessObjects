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

import static com.blockwithme.lessobjects.bench.Constants.WRITE_ITERATIONS_NON_TRANSACTIONAL;

import java.util.Random;

//CHECKSTYLE IGNORE FOR NEXT 700 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class TestData {

    static final int MAX_CAPACITY = 64 * 64 * 64;

    public static final boolean[] _100_PERCENT_TRUE;

    public static final boolean[] _25_PERCENT_TRUE;

    public static final boolean[] _50_PERCENT_TRUE;

    public static final boolean[] _75_PERCENT_TRUE;

    public static final boolean[] BOOLEANS;

    public static final boolean[] BOOLEANS_READ;

    public static final byte[] BYTES;

    public static final byte[] BYTES_READ;

    public static final char[] CHARS;

    public static final char[] CHARS_READ;

    public static final double[] DOUBLES;

    public static final double[] DOUBLES_READ;

    public static final float[] FLOATS;

    public static final float[] FLOATS_READ;

    public static final int[] INTS;

    public static final int[] INTS_READ;

    public static final long[] LONGS;

    public static final long[] LONGS_READ;

    public static int NUMBER_OF_CHANGES;

    public static Random RAND = new Random(System.currentTimeMillis());

    public static final int[] RANDOM_INDEX_ARRAY;

    public static final short[] SHORTS;

    public static final short[] SHORTS_READ;

    static {

        final int arraySize = MAX_CAPACITY * WRITE_ITERATIONS_NON_TRANSACTIONAL;

        BYTES = new byte[arraySize];
        INTS = new int[arraySize];
        SHORTS = new short[arraySize];
        LONGS = new long[arraySize];
        DOUBLES = new double[arraySize];
        CHARS = new char[arraySize];
        FLOATS = new float[arraySize];
        BOOLEANS = new boolean[arraySize];
        RANDOM_INDEX_ARRAY = new int[arraySize];

        BYTES_READ = new byte[arraySize];
        INTS_READ = new int[arraySize];
        SHORTS_READ = new short[arraySize];
        LONGS_READ = new long[arraySize];
        DOUBLES_READ = new double[arraySize];
        CHARS_READ = new char[arraySize];
        FLOATS_READ = new float[arraySize];
        BOOLEANS_READ = new boolean[arraySize];

        _25_PERCENT_TRUE = new boolean[arraySize];
        _50_PERCENT_TRUE = new boolean[arraySize];
        _75_PERCENT_TRUE = new boolean[arraySize];
        _100_PERCENT_TRUE = new boolean[arraySize];

        for (int i = 0; i < arraySize; i++) {

            BYTES[i] = (byte) RAND.nextInt();
            INTS[i] = RAND.nextInt();
            SHORTS[i] = (short) RAND.nextInt();
            LONGS[i] = RAND.nextLong();
            DOUBLES[i] = RAND.nextDouble();
            CHARS[i] = (char) RAND.nextInt();
            FLOATS[i] = RAND.nextFloat();
            BOOLEANS[i] = RAND.nextBoolean();

            // 1/4 probability of being true
            _25_PERCENT_TRUE[i] = RAND.nextInt(4) == 0;
            // 1/2 probability of being true
            _50_PERCENT_TRUE[i] = RAND.nextBoolean();
            // 3/4 probability of being true
            _75_PERCENT_TRUE[i] = RAND.nextInt(4) != 0;
            _100_PERCENT_TRUE[i] = true;

        }
    }
}
