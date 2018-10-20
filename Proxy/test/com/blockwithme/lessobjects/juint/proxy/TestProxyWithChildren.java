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
import com.blockwithme.lessobjects.juint.beans.ProxyChild1;
import com.blockwithme.lessobjects.juint.beans.ProxyChild2;
import com.blockwithme.lessobjects.juint.beans.ProxyChild4;
import com.blockwithme.lessobjects.juint.beans.ProxyWithChildren;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
/** The Class TestCoreClasses. */
@SuppressWarnings({ "PMD", "all" })
public class TestProxyWithChildren extends TestProxyData {

    private CompiledProxy[] proxies;

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
                final ProxyWithChildren b = proxy.cursor.getInstance(i,
                        ProxyWithChildren.class);
                b.setBooleanField(booleans[i]);
                b.setByteField(bytes[i]);
                b.setCharField(chars[i]);
                b.setDoubleField(doubles[i]);
                b.setFloatField(floats[i]);
                b.setIntField(ints[i]);
                b.setLongField(longs[i]);
                b.setShortField(shorts[i]);

                final int j = _CAPACITY - 1 - i;
                final ProxyChild1 child1 = b.getChild1();
                child1.setBooleanField(booleans[j]);
                child1.setByteField(bytes[j]);
                child1.setCharField(chars[j]);
                child1.setDoubleField(doubles[j]);
                child1.setFloatField(floats[j]);
                child1.setIntField(ints[j]);
                child1.setLongField(longs[j]);
                child1.setShortField(shorts[j]);

                final ProxyChild1 child3 = b.getChild3();
                child3.setBooleanField(booleans[j]);
                child3.setByteField(bytes[j]);
                child3.setCharField(chars[j]);
                child3.setDoubleField(doubles[j]);
                child3.setFloatField(floats[j]);
                child3.setIntField(ints[j]);
                child3.setLongField(longs[j]);
                child3.setShortField(shorts[j]);

                final ProxyChild2 child2 = b.getChild2();
                child2.setBooleanField(booleans[j]);
                child2.setByteField(bytes[j]);
                child2.setCharField(chars[j]);
                child2.setDoubleField(doubles[j]);
                child2.setFloatField(floats[j]);
                child2.setIntField(ints[j]);
                child2.setLongField(longs[j]);
                child2.setShortField(shorts[j]);

                final ProxyChild4 child4 = b.getChild4();
                child4.setBooleanField(booleans[j]);
                child4.setByteField(bytes[j]);
                child4.setCharField(chars[j]);
                child4.setDoubleField(doubles[j]);
                child4.setFloatField(floats[j]);
                child4.setIntField(ints[j]);
                child4.setLongField(longs[j]);
                child4.setShortField(shorts[j]);

            }

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithChildren b = proxy.cursor.getInstance(i,
                        ProxyWithChildren.class);

                assertEquals(message, booleans[i], b.isBooleanField());
                assertEquals(message, bytes[i], b.getByteField());
                assertEquals(message, chars[i], b.getCharField());
                assertEquals(message, doubles[i], b.getDoubleField(), DELTA);
                assertEquals(message, floats[i], b.getFloatField(), DELTA);
                assertEquals(message, ints[i], b.getIntField());
                assertEquals(message, longs[i], b.getLongField());
                assertEquals(message, shorts[i], b.getShortField());

                final ProxyChild1 child = b.getChild1();
                final int j = _CAPACITY - 1 - i;
                assertEquals(message, booleans[j], child.isBooleanField());
                assertEquals(message, bytes[j], child.getByteField());
                assertEquals(message, chars[j], child.getCharField());
                assertEquals(message, doubles[j], child.getDoubleField(), DELTA);
                assertEquals(message, floats[j], child.getFloatField(), DELTA);
                assertEquals(message, ints[j], child.getIntField());
                assertEquals(message, longs[j], child.getLongField());
                assertEquals(message, shorts[j], child.getShortField());

                final ProxyChild1 child3 = b.getChild3();
                assertEquals(message, booleans[j], child3.isBooleanField());
                assertEquals(message, bytes[j], child3.getByteField());
                assertEquals(message, chars[j], child3.getCharField());
                assertEquals(message, doubles[j], child3.getDoubleField(),
                        DELTA);
                assertEquals(message, floats[j], child3.getFloatField(), DELTA);
                assertEquals(message, ints[j], child3.getIntField());
                assertEquals(message, longs[j], child3.getLongField());
                assertEquals(message, shorts[j], child3.getShortField());

                final ProxyChild2 child2 = b.getChild2();
                assertEquals(message, booleans[j], child2.isBooleanField());
                assertEquals(message, bytes[j], child2.getByteField());
                assertEquals(message, chars[j], child2.getCharField());
                assertEquals(message, doubles[j], child2.getDoubleField(),
                        DELTA);
                assertEquals(message, floats[j], child2.getFloatField(), DELTA);
                assertEquals(message, ints[j], child2.getIntField());
                assertEquals(message, longs[j], child2.getLongField());
                assertEquals(message, shorts[j], child2.getShortField());

                final ProxyChild4 child4 = b.getChild4();
                assertEquals(message, booleans[j], child4.isBooleanField());
                assertEquals(message, bytes[j], child4.getByteField());
                assertEquals(message, chars[j], child4.getCharField());
                assertEquals(message, doubles[j], child4.getDoubleField(),
                        DELTA);
                assertEquals(message, floats[j], child4.getFloatField(), DELTA);
                assertEquals(message, ints[j], child4.getIntField());
                assertEquals(message, longs[j], child4.getLongField());
                assertEquals(message, shorts[j], child4.getShortField());

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
                    new Class<?>[] { ProxyWithChildren.class }, cmplr,
                    _CAPACITY);
            proxies[count].compiler = cmplr;
            count++;
        }
    }
}
