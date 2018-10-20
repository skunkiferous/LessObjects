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
package com.blockwithme.lessobjects.multidim;

//CHECKSTYLE.OFF: ParameterName
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.MultiDimensionalSupport;

/**
 * This Class provides support for multi-dimensional access in Storage objects.
 * Provides APIs to convert multi-dimensional coordinates to/from storage index.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class MultiDimensionSupportImpl implements MultiDimensionalSupport {

    /** The Arity (dimensionality). */
    private final Arity arity;

    /** The factor of w. */
    private final int fw;

    /** The factor of x. */
    private final int fx;

    /** The factor of y. */
    private final int fy;

    /** The factor of z. */
    private final int fz;

    /** The size. */
    private final int size;

    /**
     * Instantiates a new multi dimension support.
     *
     * @param theSize the size
     * @param theArity the dimensionality
     */
    @SuppressWarnings("null")
    public MultiDimensionSupportImpl(final int theSize, final Arity theArity) {
        checkArgument(theArity != null,
                "The theArity (dimensionality) must not be null ");
        checkArgument(theSize > 0, "The size must greater than 0 !");
        arity = theArity;
        size = theSize;

        fx = 1;
        switch (arity) {
        case ONE_D:
            fy = 0;
            fz = 0;
            fw = 0;
            break;
        case TWO_D:
            fy = size;
            fz = 0;
            fw = 0;
            break;
        case THREE_D:
            fy = size;
            fz = fy * size;
            fw = 0;
            break;
        case FOUR_D:
            fy = size;
            fz = fy * size;
            fw = fz * size;
            break;
        default:
            // This will not happen.
            throw new IllegalStateException();
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Arity arity() {
        return arity;
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final int x) {
        checkArgument(x < size, "The x coordinate must be less than the size.");
        return fx * x;
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final int x, final int y) {
        checkState(arity == Arity.TWO_D,
                "Method getIndex(int x, int y), applies to two dimensional Storages");
        checkArgument(x < size, "The x coordinate must be less than the size.");
        checkArgument(y < size, "The y coordinate must be less than the size.");
        return fx * x + fy * y;
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final int x, final int y, final int z) {
        checkState(arity == Arity.THREE_D,
                "Method getIndex(int x, int y, int z), applies to three dimensional Storages");
        checkArgument(x < size, "The x coordinate must be less than the size.");
        checkArgument(y < size, "The y coordinate must be less than the size.");
        checkArgument(z < size, "The z coordinate must be less than the size.");
        return fx * x + fy * y + fz * z;
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final int x, final int y, final int z, final int w) {
        checkState(
                arity == Arity.FOUR_D,
                "Method getIndex(int x, int y, int z, int w), applies to four dimensional Storages");
        checkArgument(x < size, "The x coordinate must be less than the size.");
        checkArgument(y < size, "The y coordinate must be less than the size.");
        checkArgument(z < size, "The z coordinate must be less than the size.");
        checkArgument(w < size, "The w coordinate must be less than the size.");
        return fx * x + fy * y + fz * z + fw * w;
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final Point thePoint) {
        switch (thePoint.arity()) {
        case ONE_D:
            return getIndex((Point1D) thePoint);
        case TWO_D:
            return getIndex((Point2D) thePoint);
        case THREE_D:
            return getIndex((Point3D) thePoint);
        case FOUR_D:
            return getIndex((Point4D) thePoint);
        default:
            // This will not happen
            throw new IllegalArgumentException();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final Point1D thePoint) {
        checkState(arity == Arity.ONE_D,
                "Method getIndex(Point1D thePoint), applies to single dimensional Storages");
        return getIndex(thePoint.getX());
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final Point2D thePoint) {
        checkState(arity == Arity.TWO_D,
                "Method getIndex(Point2D thePoint), applies to two dimensional Storages");
        return getIndex(thePoint.getX(), thePoint.getY());
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final Point3D thePoint) {
        checkState(arity == Arity.THREE_D,
                "Method getIndex(Point3D thePoint), applies to three dimensional Storages");
        return getIndex(thePoint.getX(), thePoint.getY(), thePoint.getZ());
    }

    /** {@inheritDoc} */
    @Override
    public int getIndex(final Point4D thePoint) {
        checkState(arity == Arity.FOUR_D,
                "Method getIndex(Point4D thePoint), applies to four dimensional Storages");
        return getIndex(thePoint.getX(), thePoint.getY(), thePoint.getZ(),
                thePoint.getW());
    }

    /** {@inheritDoc} */
    @Override
    public Point toPoint(final int theIndex) {
        checkArgument(theIndex >= 0, "The index must be a non negative integer");
        int x;
        int y;
        int z;
        int w;
        switch (arity) {
        // Omitted 'fx' as its always 1.
        case ONE_D:
            return new Point1D(theIndex);
        case TWO_D:
            y = theIndex / fy;
            x = theIndex % fy;
            return new Point2D(x, y);
        case THREE_D:
            z = theIndex / fz;
            y = theIndex % fz / fy;
            x = theIndex % fz % fy;
            return new Point3D(x, y, z);
        case FOUR_D:
            w = theIndex / fw;
            z = theIndex % fw / fz;
            y = theIndex % fw % fz / fy;
            x = theIndex % fw % fz % fy;
            return new Point4D(x, y, z, w);
        default:
            // This will not happen.
            throw new IllegalStateException();
        }
    }
}
