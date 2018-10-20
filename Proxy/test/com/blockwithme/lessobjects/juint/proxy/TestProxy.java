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
// $codepro.audit.disable
package com.blockwithme.lessobjects.juint.proxy;

import static com.blockwithme.lessobjects.juint.proxy.Constants.COMPILERS;
import static com.blockwithme.lessobjects.juint.proxy.Constants.DELTA;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.juint.beans.Proxy;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 600 LINES
/** The Class TestCoreClasses. */

@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class TestProxy extends TestProxyData {

    protected CompiledProxy[] proxies;

    @Test
    public void modifyAllValues() {

        final boolean[] booleans = booleans();
        final byte[] bytes = bytes();
        final char[] chars = chars();
        final double[] doubles = doubles();
        final float[] floats = floats();
        final long[] int_longs = int_longs();
        final int[] ints = ints();
        final long[] longs = longs();
        final short[] shorts = shorts();
        final TestObject[] objects = objects();
        final String[] strings = strings();

        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyValues() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                /* set primitives */
                b.setBooleanField(booleans[i]);
                b.setByteField(bytes[i]);
                b.setCharField(chars[i]);
                b.setDoubleField(doubles[i]);
                b.setFloatField(floats[i]);
                b.setIntField(ints[i]);
                b.setLongField(longs[i]);
                b.setShortField(shorts[i]);

                /* set objects */
                b.setObject(objects[i]);
                /* set arrays */
                b.setIntArray(ints);

                b.setBooleanArray(booleans);
                b.setByteArray(bytes);
                b.setCharArray(chars);
                b.setDoubleArray(doubles);
                b.setFloatArray(floats);
                b.setFullLongArray(longs);
                b.setLongArray(int_longs);
                b.setShortArray(shorts);
                b.setObjectArray(objects);
                b.setMyStrings(strings);

            }

            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                /* get primitives */
                assertEquals(message, booleans[i], b.isBooleanField());
                assertEquals(message, bytes[i], b.getByteField());
                assertEquals(message, chars[i], b.getCharField());
                assertEquals(message, doubles[i], b.getDoubleField(), DELTA);
                assertEquals(message, floats[i], b.getFloatField(), DELTA);
                assertEquals(message, ints[i], b.getIntField());
                assertEquals(message, longs[i], b.getLongField());
                assertEquals(message, shorts[i], b.getShortField());
                /* get objects */
                assertEquals(message, objects[i], b.getObject());
                /* get arrays */
                assertTrue(message,
                        Arrays.equals(booleans, b.getBooleanArray()));
                assertArrayEquals(message, bytes, b.getByteArray());
                assertArrayEquals(message, chars, b.getCharArray());
                assertArrayEquals(message, doubles, b.getDoubleArray(), DELTA);
                assertArrayEquals(message, floats, b.getFloatArray(), DELTA);
                assertArrayEquals(message, ints, b.getIntArray());
                assertArrayEquals(message, longs, b.getFullLongArray());
                assertArrayEquals(message, shorts, b.getShortArray());
                assertTrue(Arrays.equals(int_longs, b.getLongArray()));
                assertArrayEquals(message, strings, b.getMyStrings());
                assertArrayEquals(message, objects, b.getObjectArray());
            }
        }
    }

    @Test
    public void modifyBooleanArray() {

        final boolean[] booleans = booleans();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyBooleanArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setBooleanArray(booleans);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertTrue(message,
                        Arrays.equals(booleans, b.getBooleanArray()));
            }
        }
    }

    @Test
    public void modifyBooleanValues() {

        final boolean[] booleans = booleans();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyValues() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setBooleanField(booleans[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, booleans[i], b.isBooleanField());
            }
        }
    }

    @Test
    public void modifyByteArray() {

        final byte[] bytes = bytes();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyByteArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setByteArray(bytes);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, bytes, b.getByteArray());
            }
        }
    }

    @Test
    public void modifyByteValues() {

        final byte[] bytes = bytes();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyByteValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setByteField(bytes[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, bytes[i], b.getByteField());
            }
        }
    }

    @Test
    public void modifyCharArray() {

        final char[] chars = chars();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyCharArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setCharArray(chars);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, chars, b.getCharArray());
            }
        }
    }

    @Test
    public void modifyCharValues() {

        final char[] chars = chars();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyCharValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setCharField(chars[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, chars[i], b.getCharField());
            }
        }
    }

    @Test
    public void modifyDoubleArray() {

        final double[] doubles = doubles();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyDoubleArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setDoubleArray(doubles);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, doubles, b.getDoubleArray(), DELTA);
            }
        }
    }

    @Test
    public void modifyDoubleValues() {

        final double[] doubles = doubles();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyDoubleValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setDoubleField(doubles[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, doubles[i], b.getDoubleField(), DELTA);
            }
        }
    }

    @Test
    public void modifyFloatArray() {

        final float[] floats = floats();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyFloatArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setFloatArray(floats);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, floats, b.getFloatArray(), DELTA);
            }
        }
    }

    @Test
    public void modifyFloatValues() {

        final float[] floats = floats();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyFloatValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setFloatField(floats[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, floats[i], b.getFloatField(), DELTA);
            }
        }
    }

    @Test
    public void modifyFullLongArray() {

        final long[] longs = longs();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyLongArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setFullLongArray(longs);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, longs, b.getFullLongArray());
            }
        }
    }

    @Test
    public void modifyIntArray() {

        final int[] ints = ints();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyIntArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setIntArray(ints);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, ints, b.getIntArray());
            }
        }
    }

    @Test
    public void modifyIntValues() {

        final int[] ints = ints();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyIntValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setIntField(ints[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, ints[i], b.getIntField());
            }
        }
    }

    @Test
    public void modifyLongArray() {

        final long[] int_longs = int_longs();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyLongArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setLongArray(int_longs);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, int_longs, b.getLongArray());
            }
        }
    }

    @Test
    public void modifyLongValues() {

        final long[] longs = longs();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyLongValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setLongField(longs[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, longs[i], b.getLongField());
            }
        }
    }

    @Test
    public void modifyObjectValues() {

        final TestObject[] objects = objects();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyObjectValues() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setObject(objects[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, objects[i], b.getObject());
            }
        }
    }
    @Test
    public void modifyShortArray() {

        final short[] shorts = shorts();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyShortArray() : Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setShortArray(shorts);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertArrayEquals(message, shorts, b.getShortArray());
            }
        }
    }

    @Test
    public void modifyShortValues() {

        final short[] shorts = shorts();
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestProxy.modifyShortValues() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setShortField(shorts[i]);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                assertEquals(message, shorts[i], b.getShortField());
            }
        }
    }

    @Before
    public void setUp() {

        proxies = new CompiledProxy[COMPILERS.length];
        int count = 0;
        for (final Compiler cmplr : COMPILERS) {
            proxies[count] = new CompiledProxy();
            proxies[count].cursor = new ProxyCursor(
                    new Class<?>[] { Proxy.class }, cmplr, _CAPACITY, registry);
            proxies[count].compiler = cmplr;
            count++;
        }

    }
}
