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
 * The two dimensional Point class used by two dimensional Storages.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Point2D implements Point {

    /** The x coordinate */
    private final int x;

    /** The y coordinate */
    private final int y;

    /** Instantiates a new two dimensional Point object. */
    public Point2D(final int theX, final int theY) {
        x = theX;
        y = theY;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Arity arity() {
        return Arity.TWO_D;
    }

    /** The x coordinate */
    public int getX() {
        return x;
    }

    /** The y coordinate */
    public int getY() {
        return y;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
