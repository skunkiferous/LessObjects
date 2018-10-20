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
package com.blockwithme.lessobjects.juint.proxy;

import static com.blockwithme.lessobjects.juint.proxy.Constants.COMPILERS;
import static com.blockwithme.lessobjects.juint.proxy.Constants.DELTA;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.juint.beans.Proxy;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 500 LINES
@SuppressWarnings({ "PMD", "all" })
@ParametersAreNonnullByDefault
public class TestCopyProxy extends TestProxy {

    /** {@inheritDoc} */
    @Before
    @Override
    public void setUp() {
        super.setUp();
        // We create the storage in super class setup method.
        // call the 'copy' method here and perform read write operations in
        // the individual test methods.
        for (int i = 0; i < COMPILERS.length; i++) {
            proxies[i].cursor = proxies[i].cursor.copy();
        }
    }

    @SuppressWarnings("boxing")
    @Test
    /**
     * To test if proxy-copy has same values after copy is made.
     */
    public void testCopy() {

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
            final String message = "TestCopyProxy.testCopy() :-"
                    + proxy.compiler.compilerName();
            final ProxyCursor pCursor = proxy.cursor;

            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b1 = pCursor.getInstance(i, Proxy.class);
                final int j = _CAPACITY - i - 1;

                /* set primitives */
                b1.setBooleanField(booleans[i]);
                b1.setByteField(bytes[i]);
                b1.setCharField(chars[i]);
                b1.setDoubleField(doubles[i]);
                b1.setFloatField(floats[i]);
                b1.setIntField(ints[i]);
                b1.setLongField(longs[i]);
                b1.setShortField(shorts[i]);

                /* set objects */
                b1.setObject(objects[i]);

                /* set arrays */
                b1.setIntArray(ints);
                b1.setBooleanArray(booleans);
                b1.setByteArray(bytes);
                b1.setCharArray(chars);
                b1.setDoubleArray(doubles);
                b1.setFloatArray(floats);
                b1.setFullLongArray(longs);
                b1.setLongArray(int_longs);
                b1.setShortArray(shorts);
                b1.setObjectArray(objects);

            }

            // can't create copy if there is uncommitted data in the storage.
            pCursor.transactionManager().commit();
            final ProxyCursor pCursorCopy = pCursor.copy();

            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b1 = pCursor.getInstance(i, Proxy.class);
                final Proxy b2 = pCursorCopy.getInstance(i, Proxy.class);

                /* get primitives */
                assertEquals(message, booleans[i], b1.isBooleanField());
                assertEquals(message, booleans[i], b2.isBooleanField());
                assertEquals(message, bytes[i], b1.getByteField());
                assertEquals(message, bytes[i], b2.getByteField());
                assertEquals(message, chars[i], b1.getCharField());
                assertEquals(message, chars[i], b2.getCharField());
                assertEquals(message, doubles[i], b1.getDoubleField(), DELTA);
                assertEquals(message, doubles[i], b2.getDoubleField(), DELTA);
                assertEquals(message, floats[i], b1.getFloatField(), DELTA);
                assertEquals(message, floats[i], b2.getFloatField(), DELTA);
                assertEquals(message, ints[i], b1.getIntField());
                assertEquals(message, ints[i], b2.getIntField());
                assertEquals(message, longs[i], b1.getLongField());
                assertEquals(message, longs[i], b2.getLongField());
                assertEquals(message, shorts[i], b1.getShortField());
                assertEquals(message, shorts[i], b2.getShortField());

                /* get objects */
                assertEquals(message, objects[i], b1.getObject());
                assertEquals(message, objects[i], b2.getObject());

                /* get arrays */
                assertTrue(message,
                        Arrays.equals(booleans, b1.getBooleanArray()));
                assertTrue(message,
                        Arrays.equals(booleans, b2.getBooleanArray()));
                assertArrayEquals(message, bytes, b1.getByteArray());
                assertArrayEquals(message, bytes, b2.getByteArray());
                assertArrayEquals(message, chars, b1.getCharArray());
                assertArrayEquals(message, chars, b2.getCharArray());
                assertArrayEquals(message, doubles, b1.getDoubleArray(), DELTA);
                assertArrayEquals(message, doubles, b2.getDoubleArray(), DELTA);
                assertArrayEquals(message, floats, b1.getFloatArray(), DELTA);
                assertArrayEquals(message, floats, b2.getFloatArray(), DELTA);
                assertArrayEquals(message, ints, b1.getIntArray());
                assertArrayEquals(message, ints, b2.getIntArray());
                assertArrayEquals(message, longs, b1.getFullLongArray());
                assertArrayEquals(message, longs, b2.getFullLongArray());
                assertArrayEquals(message, shorts, b1.getShortArray());
                assertArrayEquals(message, shorts, b2.getShortArray());

            }
        }
    }

    /**
     * To test if proxy-copy and source store different values.
     */
    @SuppressWarnings("boxing")
    @Test
    public void testModifyCopy() {

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
            final String message = "TestCopyProxy.testCopy() :-"
                    + proxy.compiler.compilerName();

            final ProxyCursor pCursor = proxy.cursor;
            final ProxyCursor pCursorCopy = pCursor.copy();

            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b1 = pCursor.getInstance(i, Proxy.class);
                final Proxy b2 = pCursorCopy.getInstance(i, Proxy.class);

                final int j = _CAPACITY - i - 1;

                /* set primitives */
                b1.setBooleanField(booleans[i]);
                b2.setBooleanField(booleans[j]);

                b1.setByteField(bytes[i]);
                b2.setByteField(bytes[j]);

                b1.setCharField(chars[i]);
                b2.setCharField(chars[j]);

                b1.setDoubleField(doubles[i]);
                b2.setDoubleField(doubles[j]);

                b1.setFloatField(floats[i]);
                b2.setFloatField(floats[j]);

                b1.setIntField(ints[i]);
                b2.setIntField(ints[j]);

                b1.setLongField(longs[i]);
                b2.setLongField(longs[j]);

                b1.setShortField(shorts[i]);
                b2.setShortField(shorts[j]);

                /* set objects */
                b1.setObject(objects[i]);
                b2.setObject(objects[j]);

                /* set arrays */
                b1.setIntArray(ints);
                b2.setIntArray(ints);

                b1.setBooleanArray(booleans);
                b2.setBooleanArray(booleans);

                b1.setByteArray(bytes);
                b2.setByteArray(bytes);

                b1.setCharArray(chars);
                b2.setCharArray(chars);

                b1.setDoubleArray(doubles);
                b2.setDoubleArray(doubles);

                b1.setFloatArray(floats);
                b2.setFloatArray(floats);

                b1.setFullLongArray(longs);
                b2.setFullLongArray(longs);

                b1.setLongArray(int_longs);
                b2.setLongArray(int_longs);

                b1.setShortArray(shorts);
                b2.setShortArray(shorts);

                b1.setObjectArray(objects);
                b2.setObjectArray(objects);
            }

            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b1 = pCursor.getInstance(i, Proxy.class);
                final Proxy b2 = pCursorCopy.getInstance(i, Proxy.class);

                final int j = _CAPACITY - i - 1;

                /* get primitives */
                assertEquals(message, booleans[i], b1.isBooleanField());
                assertEquals(message, booleans[j], b2.isBooleanField());
                assertEquals(message, bytes[i], b1.getByteField());
                assertEquals(message, bytes[j], b2.getByteField());
                assertEquals(message, chars[i], b1.getCharField());
                assertEquals(message, chars[j], b2.getCharField());
                assertEquals(message, doubles[i], b1.getDoubleField(), DELTA);
                assertEquals(message, doubles[j], b2.getDoubleField(), DELTA);
                assertEquals(message, floats[i], b1.getFloatField(), DELTA);
                assertEquals(message, floats[j], b2.getFloatField(), DELTA);
                assertEquals(message, ints[i], b1.getIntField());
                assertEquals(message, ints[j], b2.getIntField());
                assertEquals(message, longs[i], b1.getLongField());
                assertEquals(message, longs[j], b2.getLongField());
                assertEquals(message, shorts[i], b1.getShortField());
                assertEquals(message, shorts[j], b2.getShortField());

                /* get objects */
                assertEquals(message, objects[i], b1.getObject());
                assertEquals(message, objects[j], b2.getObject());

                /* get arrays */
                assertTrue(message,
                        Arrays.equals(booleans, b1.getBooleanArray()));
                assertTrue(message,
                        Arrays.equals(booleans, b2.getBooleanArray()));
                assertArrayEquals(message, bytes, b1.getByteArray());
                assertArrayEquals(message, bytes, b2.getByteArray());
                assertArrayEquals(message, chars, b1.getCharArray());
                assertArrayEquals(message, chars, b2.getCharArray());
                assertArrayEquals(message, doubles, b1.getDoubleArray(), DELTA);
                assertArrayEquals(message, doubles, b2.getDoubleArray(), DELTA);
                assertArrayEquals(message, floats, b1.getFloatArray(), DELTA);
                assertArrayEquals(message, floats, b2.getFloatArray(), DELTA);
                assertArrayEquals(message, ints, b1.getIntArray());
                assertArrayEquals(message, ints, b2.getIntArray());
                assertArrayEquals(message, longs, b1.getFullLongArray());
                assertArrayEquals(message, longs, b2.getFullLongArray());
                assertArrayEquals(message, shorts, b1.getShortArray());
                assertArrayEquals(message, shorts, b2.getShortArray());
            }
        }
    }
}
