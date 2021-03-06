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
 * The Interface IntFieldMapper for virtual fields of int type
 *
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface IntFieldMapper {
    /** Method clears the value of int virtual field */
    void clear(final Struct theParent, final Storage theStorage);

    /** Method reads the value of int virtual field */
    int read(final Struct theParent, final Storage theStorage);

    /** Method writes the value of int virtual field */
    void write(final int theValue, final Struct theParent,
            final Storage theStorage);
}
