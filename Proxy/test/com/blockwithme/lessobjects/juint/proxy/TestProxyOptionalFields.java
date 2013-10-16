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
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.juint.beans.ProxyOptionalFields;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
/** The Class TestCoreClasses. */
@SuppressWarnings({ "PMD", "all" })
public class TestProxyOptionalFields extends TestProxyData {

    private CompiledProxy[] proxies;

    @Test
    public void defaultValues() {
        for (final CompiledProxy proxy : proxies) {
            final String message = "Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyOptionalFields b = proxy.cursor.getInstance(i,
                        ProxyOptionalFields.class);

                assertEquals(message, false, b.isBooleanField());
                assertEquals(message, 0, b.getByteField());
                assertEquals(message, 0, b.getCharField());
                assertEquals(message, 0d, b.getDoubleField(), DELTA);
                assertEquals(message, 0f, b.getFloatField(), DELTA);
                assertEquals(message, 0, b.getIntField());
                assertEquals(message, 0l, b.getLongField());
                assertEquals(message, (short) 0, b.getShortField());
            }
        }
    }

    @Test
    public void modifyValues() {

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
            final String message = "Proxy Test failed for Compiler -"
                    + proxy.compiler.compilerName();

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyOptionalFields b = proxy.cursor.getInstance(i,
                        ProxyOptionalFields.class);
                b.setBooleanField(booleans[i]);
                b.setByteField(bytes[i]);
                b.setCharField(chars[i]);
                b.setDoubleField(doubles[i]);
                b.setFloatField(floats[i]);
                b.setIntField(ints[i]);
                b.setLongField(longs[i]);
                b.setShortField(shorts[i]);

            }

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyOptionalFields b = proxy.cursor.getInstance(i,
                        ProxyOptionalFields.class);
                assertEquals(message, booleans[i], b.isBooleanField());
                assertEquals(message, bytes[i], b.getByteField());
                assertEquals(message, chars[i], b.getCharField());
                assertEquals(message, doubles[i], b.getDoubleField(), DELTA);
                assertEquals(message, floats[i], b.getFloatField(), DELTA);
                assertEquals(message, ints[i], b.getIntField());
                assertEquals(message, longs[i], b.getLongField());
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
                    new Class<?>[] { ProxyOptionalFields.class }, cmplr,
                    _CAPACITY);
            proxies[count].compiler = cmplr;
            count++;
        }
    }
}
