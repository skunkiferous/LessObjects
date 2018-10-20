/**
 *
 */
package com.blockwithme.lessobjects.juint;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blockwithme.lessobjects.beans.MultiDimensionalSupport;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.multidim.MultiDimensionSupportImpl;
import com.blockwithme.lessobjects.multidim.Point1D;
import com.blockwithme.lessobjects.multidim.Point2D;
import com.blockwithme.lessobjects.multidim.Point3D;
import com.blockwithme.lessobjects.multidim.Point4D;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestMultiDimensionalSupport {
    private static void checkGetIndex1D(final int size, final int x) {
        final String message = "Test getIndex (1D) x: " + x + " size: " + size;
        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.ONE_D);
        int index = mdSupport.getIndex(x);
        assertEquals(message, x, index);
        index = mdSupport.getIndex(new Point1D(x));
        assertEquals(message, x, index);
    }

    private static void checkGetIndex2D(final int size, final int x, final int y) {
        final String message = "Test getIndex (2D) x: " + x + " y: " + y
                + " size: " + size;
        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.TWO_D);
        int index = mdSupport.getIndex(x, y);
        assertEquals(message, size * y + x, index);
        index = mdSupport.getIndex(new Point2D(x, y));
        assertEquals(message, size * y + x, index);
    }

    private static void checkGetIndex3D(final int size, final int x,
            final int y, final int z) {
        final String message = "Test getIndex (3D) x: " + x + " y: " + y
                + " z: " + z + " size: " + size;
        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.THREE_D);
        int index = mdSupport.getIndex(x, y, z);
        assertEquals(message, size * size * z + size * y + x, index);
        index = mdSupport.getIndex(new Point3D(x, y, z));
        assertEquals(message, size * size * z + size * y + x, index);
    }

    private static void checkGetIndex4D(final int size, final int x,
            final int y, final int z, final int w) {
        final String message = "Test getIndex (4D) x: " + x + " y: " + y
                + " z: " + z + " w: " + w + " size:" + size;
        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.FOUR_D);
        int index = mdSupport.getIndex(x, y, z, w);
        assertEquals(message, size * size * size * w + size * size * z + size
                * y + x, index);
        index = mdSupport.getIndex(new Point4D(x, y, z, w));
        assertEquals(message, size * size * size * w + size * size * z + size
                * y + x, index);
    }

    private void checkToPoint1D(final int x, final int size) {
        final String message = "Test checkToPoint1D  x: " + x + " size: "
                + size;
        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.ONE_D);
        final int index = mdSupport.getIndex(x);
        final Point1D pt = (Point1D) mdSupport.toPoint(index);
        assertEquals(message, Arity.ONE_D, pt.arity());
        assertEquals(message, x, pt.getX());

    }

    private void checkToPoint2D(final int x, final int y, final int size) {
        final String message = "Test checkToPoint2D  x: " + x + " y: " + y
                + " size: " + size;
        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.TWO_D);
        final int index = mdSupport.getIndex(x, y);
        final Point2D pt = (Point2D) mdSupport.toPoint(index);

        assertEquals(message, Arity.TWO_D, pt.arity());
        assertEquals(message, x, pt.getX());
        assertEquals(message, y, pt.getY());

    }

    private void checkToPoint3D(final int x, final int y, final int z,
            final int size) {
        final String message = "Test checkToPoint3D  x: " + x + " y: " + y
                + " z: " + z + " size: " + size;

        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.THREE_D);
        final int index = mdSupport.getIndex(x, y, z);
        final Point3D pt = (Point3D) mdSupport.toPoint(index);

        assertEquals(message, Arity.THREE_D, pt.arity());
        assertEquals(message, x, pt.getX());
        assertEquals(message, y, pt.getY());
        assertEquals(message, z, pt.getZ());
    }

    private void checkToPoint4D(final int x, final int y, final int z,
            final int w, final int size) {
        final String message = "Test checkToPoint4D (4D) x: " + x + " y: " + y
                + " z: " + z + " w: " + w + " size: " + size;

        final MultiDimensionalSupport mdSupport = new MultiDimensionSupportImpl(
                size, Arity.FOUR_D);
        final int index = mdSupport.getIndex(x, y, z, w);
        final Point4D pt = (Point4D) mdSupport.toPoint(index);

        assertEquals(message, Arity.FOUR_D, pt.arity());
        assertEquals(message, x, pt.getX());
        assertEquals(message, y, pt.getY());
        assertEquals(message, z, pt.getZ());
        assertEquals(message, w, pt.getW());
    }

    @Test
    public void testGetIndex1D() {
        checkGetIndex1D(1, 0);

        checkGetIndex1D(10, 0);
        checkGetIndex1D(10, 1);
        checkGetIndex1D(10, 9);

        checkGetIndex1D(100, 0);
        checkGetIndex1D(100, 1);
        checkGetIndex1D(100, 10);
        checkGetIndex1D(100, 99);

        checkGetIndex1D(1000, 0);
        checkGetIndex1D(1000, 1);
        checkGetIndex1D(1000, 100);
        checkGetIndex1D(1000, 999);
    }

    @Test
    public void testGetIndex2D() {
        checkGetIndex2D(1, 0, 0);

        checkGetIndex2D(10, 0, 0);
        checkGetIndex2D(10, 1, 1);
        checkGetIndex2D(10, 1, 2);
        checkGetIndex2D(10, 9, 9);

        checkGetIndex2D(100, 0, 0);
        checkGetIndex2D(100, 1, 1);
        checkGetIndex2D(100, 10, 20);
        checkGetIndex2D(100, 99, 99);

        checkGetIndex2D(1000, 0, 0);
        checkGetIndex2D(1000, 1, 1);
        checkGetIndex2D(1000, 100, 200);
        checkGetIndex2D(1000, 999, 999);
    }

    @Test
    public void testGetIndex3D() {
        checkGetIndex3D(1, 0, 0, 0);

        checkGetIndex3D(10, 0, 0, 0);
        checkGetIndex3D(10, 1, 1, 1);
        checkGetIndex3D(10, 1, 2, 3);
        checkGetIndex3D(10, 9, 9, 9);

        checkGetIndex3D(100, 0, 0, 0);
        checkGetIndex3D(100, 1, 1, 1);
        checkGetIndex3D(100, 10, 20, 30);
        checkGetIndex3D(100, 99, 99, 99);

        checkGetIndex3D(1000, 0, 0, 0);
        checkGetIndex3D(1000, 1, 1, 1);
        checkGetIndex3D(1000, 100, 200, 300);
        checkGetIndex3D(1000, 999, 999, 999);
    }

    @Test
    public void testGetIndex4D() {
        checkGetIndex4D(1, 0, 0, 0, 0);

        checkGetIndex4D(10, 0, 0, 0, 0);
        checkGetIndex4D(10, 1, 1, 1, 1);
        checkGetIndex4D(10, 1, 2, 3, 4);
        checkGetIndex4D(10, 9, 9, 9, 9);

        checkGetIndex4D(100, 0, 0, 0, 0);
        checkGetIndex4D(100, 1, 1, 1, 1);
        checkGetIndex4D(100, 10, 20, 30, 40);
        checkGetIndex4D(100, 99, 99, 99, 99);

        checkGetIndex4D(1000, 0, 0, 0, 0);
        checkGetIndex4D(1000, 1, 1, 1, 1);
        checkGetIndex4D(1000, 100, 200, 300, 400);
        checkGetIndex4D(1000, 999, 999, 999, 999);
    }

    @Test
    public void testToPoint1D() {
        checkToPoint1D(0, 1);

        checkToPoint1D(0, 10);
        checkToPoint1D(1, 10);
        checkToPoint1D(2, 10);
        checkToPoint1D(9, 10);
        checkToPoint1D(0, 100);
        checkToPoint1D(1, 100);
        checkToPoint1D(10, 100);
        checkToPoint1D(99, 100);

        checkToPoint1D(0, 101);
        checkToPoint1D(1, 101);
        checkToPoint1D(11, 101);
        checkToPoint1D(100, 101);
        checkToPoint1D(100, 101);
    }

    @Test
    public void testToPoint2D() {
        checkToPoint2D(0, 0, 1);

        checkToPoint2D(0, 0, 10);
        checkToPoint2D(1, 1, 10);
        checkToPoint2D(1, 2, 10);
        checkToPoint2D(9, 9, 10);
        checkToPoint2D(0, 0, 100);
        checkToPoint2D(1, 1, 100);
        checkToPoint2D(10, 20, 100);
        checkToPoint2D(99, 99, 100);

        checkToPoint2D(0, 0, 101);
        checkToPoint2D(1, 1, 101);
        checkToPoint2D(11, 21, 101);
        checkToPoint2D(100, 100, 101);
        checkToPoint2D(100, 11, 101);
    }

    @Test
    public void testToPoint3D() {
        checkToPoint3D(0, 0, 0, 1);

        checkToPoint3D(0, 0, 0, 10);
        checkToPoint3D(1, 1, 1, 10);
        checkToPoint3D(1, 2, 3, 10);
        checkToPoint3D(9, 9, 9, 10);

        checkToPoint3D(0, 0, 0, 100);
        checkToPoint3D(1, 1, 1, 100);
        checkToPoint3D(10, 20, 30, 100);
        checkToPoint3D(99, 99, 99, 100);

        checkToPoint3D(0, 0, 0, 101);
        checkToPoint3D(1, 1, 1, 101);
        checkToPoint3D(11, 21, 31, 101);
        checkToPoint3D(100, 100, 100, 101);
        checkToPoint3D(100, 11, 1, 101);
    }

    @Test
    public void testToPoint4D() {
        checkToPoint4D(0, 0, 0, 0, 1);

        checkToPoint4D(0, 0, 0, 0, 10);
        checkToPoint4D(1, 1, 1, 1, 10);
        checkToPoint4D(1, 2, 3, 4, 10);
        checkToPoint4D(9, 9, 9, 9, 10);

        checkToPoint4D(0, 0, 0, 0, 100);
        checkToPoint4D(1, 1, 1, 1, 100);
        checkToPoint4D(10, 20, 30, 40, 100);
        checkToPoint4D(99, 99, 99, 99, 100);

        checkToPoint4D(0, 0, 0, 0, 101);
        checkToPoint4D(1, 1, 1, 1, 101);
        checkToPoint4D(11, 21, 31, 41, 101);
        checkToPoint4D(100, 100, 100, 100, 101);
        checkToPoint4D(100, 11, 1, 100, 101);
    }
}
