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
import com.blockwithme.lessobjects.juint.beans.Proxy;
import com.blockwithme.lessobjects.juint.beans.ProxyCopy1;
import com.blockwithme.lessobjects.juint.beans.ProxyCopy2;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
/** The Class TestCoreClasses. */
@SuppressWarnings({ "PMD", "all" })
public class TestProxyUnion extends TestProxyData {

    private CompiledProxy[] proxies;

    @Test
    public void proxyUnion() {

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
            for (int i = 0; i < _CAPACITY; i++) {
                final Proxy b = proxy.cursor.getInstance(i, Proxy.class);
                b.setBooleanField(booleans[i]);
                b.setByteField(bytes[i]);
                b.setCharField(chars[i]);
                b.setDoubleField(doubles[i]);
                b.setFloatField(floats[i]);
                b.setIntField(ints[i]);
                b.setLongField(longs[i]);
                b.setShortField(shorts[i]);

                String message = "Proxy Test failed for Compiler -"
                        + proxy.compiler.compilerName() + " proxy class - "
                        + b.getClass().getName();

                assertEquals(message, booleans[i], b.isBooleanField());
                assertEquals(message, bytes[i], b.getByteField());
                assertEquals(message, chars[i], b.getCharField());
                assertEquals(message, doubles[i], b.getDoubleField(), DELTA);
                assertEquals(message, floats[i], b.getFloatField(), DELTA);
                assertEquals(message, ints[i], b.getIntField());
                assertEquals(message, longs[i], b.getLongField());
                assertEquals(message, shorts[i], b.getShortField());

                proxy.cursor.selectType(i, ProxyCopy1.class);

                final ProxyCopy1 c = proxy.cursor.getInstance(i,
                        ProxyCopy1.class);

                message = "Proxy Test failed for Compiler -"
                        + proxy.compiler.compilerName() + " proxy class - "
                        + c.getClass().getName();

                c.setBooleanField(booleans[i]);
                c.setByteField(bytes[i]);
                c.setCharField(chars[i]);
                c.setDoubleField(doubles[i]);
                c.setFloatField(floats[i]);
                c.setIntField(ints[i]);
                c.setLongField(longs[i]);
                c.setShortField(shorts[i]);

                assertEquals(message, booleans[i], c.isBooleanField());
                assertEquals(message, bytes[i], c.getByteField());
                assertEquals(message, chars[i], c.getCharField());
                assertEquals(message, doubles[i], c.getDoubleField(), DELTA);
                assertEquals(message, floats[i], c.getFloatField(), DELTA);
                assertEquals(message, ints[i], c.getIntField());
                assertEquals(message, longs[i], c.getLongField());
                assertEquals(message, shorts[i], c.getShortField());

                proxy.cursor.selectType(i, ProxyCopy2.class);
                final ProxyCopy2 d = proxy.cursor.getInstance(i,
                        ProxyCopy2.class);

                message = "Proxy Test failed for Compiler -"
                        + proxy.compiler.compilerName() + " proxy class - "
                        + d.getClass().getName();

                d.setBooleanField(booleans[i]);
                d.setByteField(bytes[i]);
                d.setCharField(chars[i]);
                d.setDoubleField(doubles[i]);
                d.setFloatField(floats[i]);
                d.setIntField(ints[i]);
                d.setLongField(longs[i]);
                d.setShortField(shorts[i]);

                assertEquals(message, booleans[i], d.isBooleanField());
                assertEquals(message, bytes[i], d.getByteField());
                assertEquals(message, chars[i], d.getCharField());
                assertEquals(message, doubles[i], d.getDoubleField(), DELTA);
                assertEquals(message, floats[i], d.getFloatField(), DELTA);
                assertEquals(message, ints[i], d.getIntField());
                assertEquals(message, longs[i], d.getLongField());
                assertEquals(message, shorts[i], d.getShortField());
            }
        }
    }

    @Before
    public void setUp() {
        proxies = new CompiledProxy[COMPILERS.length];
        int count = 0;
        for (final Compiler cmplr : COMPILERS) {
            proxies[count] = new CompiledProxy();
            proxies[count].cursor = new ProxyCursor(new Class<?>[] {
                    Proxy.class, ProxyCopy1.class, ProxyCopy2.class }, cmplr,
                    _CAPACITY, registry);
            proxies[count].compiler = cmplr;
            count++;
        }
    }
}
