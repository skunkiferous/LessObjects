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

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The four dimensional Point class used by four dimensional Storages.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Point4D implements Point {

    /** The w coordinate */
    private final int w;

    /** The x coordinate */
    private final int x;

    /** The y coordinate */
    private final int y;

    /** The z coordinate */
    private final int z;

    /** Instantiates a new three dimensional Point object. */
    public Point4D(final int theX, final int theY, final int theZ,
            final int theW) {
        x = theX;
        y = theY;
        z = theZ;
        w = theW;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Arity arity() {
        return Arity.FOUR_D;
    }

    /** The w coordinate */
    public int getW() {
        return w;
    }

    /** The x coordinate */
    public int getX() {
        return x;
    }

    /** The y coordinate */
    public int getY() {
        return y;
    }

    /** The z coordinate */
    public int getZ() {
        return z;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + w + ")";
    }
}
