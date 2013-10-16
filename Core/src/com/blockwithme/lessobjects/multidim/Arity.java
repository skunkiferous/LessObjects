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

/**
 * This enum indicates if an Storage is Single Dimensional or MultiDimensional and it's dimensions.
 * The term Arity comes from words like unary, binary, ternary, etc.
 *
 * @author tarung
 */
public enum Arity {

    /** Single/First dimension. */
    ONE_D,

    /** The Second dimension. */
    TWO_D,

    /** The third dimension. */
    THREE_D,

    /** The fourth dimension. */
    FOUR_D;
}
