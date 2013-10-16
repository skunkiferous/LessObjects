/**
 *
 */
package com.blockwithme.lessobjects.beans;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.multidim.Point;
import com.blockwithme.lessobjects.multidim.Point1D;
import com.blockwithme.lessobjects.multidim.Point2D;
import com.blockwithme.lessobjects.multidim.Point3D;
import com.blockwithme.lessobjects.multidim.Point4D;

//CHECKSTYLE.OFF: ParameterName

/**
 * This Class provides support for multi-dimensional access in Storage objects.
 * Provides APIs to convert multi-dimensional coordinates to/from storage index.
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public interface MultiDimensionalSupport {

    /** Arity (Dimensionality)
     *  Returns if the Point is 1D, 2D, 3D or 4D */
    public Arity arity();

    /**
     * Computes index for a one dimensional coordinates .
     *
     * @param x the x coordinates
     * @return the calculated index
     */
    public abstract int getIndex(int x);

    /**
     * Computes index for a two dimensional coordinates .
     *
     * @param x the x coordinates
     * @param y the y coordinates
     * @return the calculated index
     */
    public abstract int getIndex(int x, int y);

    /**
     * Computes index for a three dimensional coordinates .
     *
     * @param x the x coordinates
     * @param y the y coordinates
     * @param z the z coordinates
     * @return the calculated index
     */
    public abstract int getIndex(int x, int y, int z);

    /**
     * Computes index for a four dimensional coordinates .
     *
     * @param x the x coordinates
     * @param y the y coordinates
     * @param z the z coordinates
     * @param w the w coordinates
     * @return the calculated index
     */
    public abstract int getIndex(int x, int y, int z, int w);

    /**
     * Computes index for a generic Point object.
     *
     * @return the calculated index
     */
    public abstract int getIndex(Point thePoint);

    /**
     * Computes index for a single dimensional Point.
     *
     * @return the calculated index
     */
    public abstract int getIndex(Point1D thePoint);

    /**
     * Computes index for a two dimensional Point.
     *
     * @return the calculated index
     */
    public abstract int getIndex(Point2D thePoint);

    /**
     * Computes index for a three dimensional Point.
     *
     * @return the calculated index
     */
    public abstract int getIndex(Point3D thePoint);

    /**
     * Computes index for a four dimensional Point.
     *
     * @return the calculated index
     */
    public abstract int getIndex(Point4D thePoint);

    /**
     * Converts an index to a Point object.
     *
     * @param theIndex the index
     * @return the calculated value of point.
     */
    public abstract Point toPoint(int theIndex);

}