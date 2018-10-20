/**
 *
 */
package com.blockwithme.lessobjects.juint.proxy;

import static com.blockwithme.lessobjects.juint.proxy.Constants.DELTA;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.CompilerType;
import com.blockwithme.lessobjects.juint.beans.Proxy;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;
import com.blockwithme.lessobjects.util.CompilerFactoryImpl;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class TestProxyRecompile extends TestProxyData {

    ProxyCursor cursor;

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

        for (int i = 0; i < _CAPACITY; i++) {
            final Proxy b = cursor.getInstance(i, Proxy.class);
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
            final Proxy b = cursor.getInstance(i, Proxy.class);
            /* get primitives */
            assertEquals(booleans[i], b.isBooleanField());
            assertEquals(bytes[i], b.getByteField());
            assertEquals(chars[i], b.getCharField());
            assertEquals(doubles[i], b.getDoubleField(), DELTA);
            assertEquals(floats[i], b.getFloatField(), DELTA);
            assertEquals(ints[i], b.getIntField());
            assertEquals(longs[i], b.getLongField());
            assertEquals(shorts[i], b.getShortField());
            /* get objects */
            assertEquals(objects[i], b.getObject());
            /* get arrays */
            assertTrue(Arrays.equals(booleans, b.getBooleanArray()));
            assertArrayEquals(bytes, b.getByteArray());
            assertArrayEquals(chars, b.getCharArray());
            assertArrayEquals(doubles, b.getDoubleArray(), DELTA);
            assertArrayEquals(floats, b.getFloatArray(), DELTA);
            assertArrayEquals(ints, b.getIntArray());
            assertArrayEquals(longs, b.getFullLongArray());
            assertArrayEquals(shorts, b.getShortArray());
            assertTrue(Arrays.equals(int_longs, b.getLongArray()));
            assertArrayEquals(strings, b.getMyStrings());
            assertArrayEquals(objects, b.getObjectArray());
        }
    }

    @Before
    public void setUp() {
        final Compiler cmplr = new CompilerFactoryImpl()
                .createCompiler(CompilerType.ALIGNED64);
        final Compiler otherCmplr = new CompilerFactoryImpl()
                .createCompiler(CompilerType.PACKED);
        cursor = new ProxyCursor(new Class<?>[] { Proxy.class }, cmplr,
                _CAPACITY, registry);
        cursor.reCompile(_CAPACITY, cmplr);
    }
}
