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
 * The Point Interface implemented by Point1D, Point2D, Point3D, and Point4D classes.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface Point {
    /**
     * Arity (Dimensionality)
     *  Returns if the Point is 1D, 2D, 3D or 4D
     */
    public Arity arity();
}
