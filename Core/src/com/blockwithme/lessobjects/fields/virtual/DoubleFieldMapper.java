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
 * The Interface DoubleFieldConverter for virtual fields of double type
 *
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface DoubleFieldMapper {
    /** Method clears the value of double virtual field */
    void clear(final Struct theParent, final Storage theStorage);

    /** Method reads the value of double virtual field */
    double read(final Struct theParent, final Storage theStorage);

    /** Method writes the value of double virtual field. */
    void write(final double theValue, final Struct theParent,
            final Storage theStorage);
}
