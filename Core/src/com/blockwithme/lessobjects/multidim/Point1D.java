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
 * The single dimensional Point class used by single dimensional storages.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Point1D implements Point {

    /** The x coordinate */
    private final int x;

    /** Instantiates a new single dimensional Point object. */
    public Point1D(final int theX) {
        x = theX;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Arity arity() {
        return Arity.ONE_D;
    }

    /** The x coordinate */
    public int getX() {
        return x;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + x + ")";
    }
}
