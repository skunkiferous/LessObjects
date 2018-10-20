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
/**
 *
 */
package com.blockwithme.lessobjects.juint.proxy;

import static com.blockwithme.lessobjects.juint.proxy.Constants.COMPILERS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.juint.beans.ListChild;
import com.blockwithme.lessobjects.juint.beans.ProxyWithIterableField;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 500 LINES
/** The Class TestCoreClasses. */

@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class TestProxyWithIterableField extends TestProxyData {
    private CompiledProxy[] proxies;

    @Test
    public void modifyTwoLists() {

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
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                bean.setIntField(ints[i]);
                final Iterator<ListChild> itr = bean.getList(10);
                final Iterator<ListChild> itr2 = bean.getSecondList(10);

                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    final ListChild c2 = itr2.next();
                    c.setIntField(ints[j]);
                    c.setLongField(longs[j]);
                    c2.setIntField(ints[10 - j - 1]);
                    c2.setLongField(longs[10 - j - 1]);
                    j++;
                }
            }
            proxy.cursor.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                assertEquals(ints[i], bean.getIntField());
                final Iterator<ListChild> itr = bean.getList(10);
                final Iterator<ListChild> itr2 = bean.getSecondList(10);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    final ListChild c2 = itr2.next();
                    assertEquals(ints[j], c.getIntField());
                    assertEquals(longs[j], c.getLongField());
                    assertEquals(ints[10 - j - 1], c2.getIntField());
                    assertEquals(longs[10 - j - 1], c2.getLongField());
                    j++;
                }
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
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                bean.setIntField(ints[i]);
                final Iterator<ListChild> itr = bean.getList(10);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    c.setIntField(ints[j]);
                    c.setLongField(longs[j]);
                    j++;
                }

            }
            proxy.cursor.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                assertEquals(ints[i], bean.getIntField());
                final Iterator<ListChild> itr = bean.getList(10);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    assertEquals(ints[j], c.getIntField());
                    assertEquals(longs[j], c.getLongField());
                    j++;
                }
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
                    new Class<?>[] { ProxyWithIterableField.class }, cmplr,
                    _CAPACITY);
            proxies[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testClearList() {

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
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                bean.setIntField(ints[i]);
                final Iterator<ListChild> itr = bean.getList(10);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    c.setIntField(ints[j]);
                    c.setLongField(longs[j]);
                    j++;
                }
            }

            proxy.cursor.transactionManager().commit();
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                assertEquals(ints[i], bean.getIntField());
                // negative value indicates that we want to clear the collection
                final Iterator<ListChild> itr = bean.getList(-1);
                assertFalse(itr.hasNext());
            }
        }
    }

    @Test
    public void testDecreaseSize() {

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
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                bean.setIntField(ints[i]);
                final Iterator<ListChild> itr = bean.getList(10);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    c.setIntField(ints[j]);
                    c.setLongField(longs[j]);
                    j++;
                }
            }
            proxy.cursor.transactionManager().commit();
            // get the list with size = 5 and check if remaining values are
            // retained.
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                assertEquals(ints[i], bean.getIntField());
                // a different number indicates that we want to change the
                // collection size.
                final Iterator<ListChild> itr = bean.getList(5);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    assertEquals(ints[j], c.getIntField());
                    assertEquals(longs[j], c.getLongField());
                    j++;
                }
                assertEquals(5, j);
            }
        }
    }

    @Test
    public void testIncreaseSize() {

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
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                bean.setIntField(ints[i]);
                final Iterator<ListChild> itr = bean.getList(10);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    c.setIntField(ints[j]);
                    c.setLongField(longs[j]);
                    j++;
                }
            }
            proxy.cursor.transactionManager().commit();
            // Now change the size to 20
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                final Iterator<ListChild> itr = bean.getList(20);
                int j = 0;
                while (itr.hasNext()) {
                    itr.next();
                    j++;
                }
                assertEquals(20, j);
            }

            // populate all 20 elements.
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                bean.setIntField(ints[i]);
                // zero indicates that we want the collection with existing
                // size.
                final Iterator<ListChild> itr = bean.getList(0);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    c.setIntField(ints[j % 10]);
                    c.setLongField(longs[j % 10]);
                    j++;
                }
                assertEquals(20, j);
            }
            proxy.cursor.transactionManager().commit();

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithIterableField bean = proxy.cursor.getInstance(i,
                        ProxyWithIterableField.class);
                assertEquals(ints[i], bean.getIntField());
                // zero indicates that we want the collection with existing
                // size.
                final Iterator<ListChild> itr = bean.getList(0);
                int j = 0;
                while (itr.hasNext()) {
                    final ListChild c = itr.next();
                    assertEquals(ints[j % 10], c.getIntField());
                    assertEquals(longs[j % 10], c.getLongField());
                    j++;
                }
            }
        }
    }

}
