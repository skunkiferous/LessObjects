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
package com.blockwithme.lessobjects.fields.virtual;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.storage.Storage;

/**
 * Converts a virtual fields (Those fields can be read and/or written, but they
 * use some calculation internally, which might be based on the value of other
 * fields.) value to and from actual values.
 * It is assumed that the converter is stateless, and therefore thread-safe.
 *
 * @param <F> the generic type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface ObjectFieldMapper<F> {

    /** Clears the Field Value. */
    void clear(final Struct theParent, final Storage theStorage);

    /** Reads the Field Value */
    F read(final Struct theParent, final Storage theStorage);

    /** Type of Field */
    Class<F> type();

    /** Writes the value of this field */
    void write(final F theValue, final Struct theParent,
            final Storage theStorage);
}
