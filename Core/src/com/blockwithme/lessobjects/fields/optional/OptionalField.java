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

package com.blockwithme.lessobjects.fields.optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;

//CHECKSTYLE.OFF: IllegalType
/**
 * Represents a Abstract Optional field type. Optional fields are the fields
 * where the field value is expected to be a default value most of the times
 *
 * @param <E> the data type
 * @param <F> the Field type
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface OptionalField<E, F extends Field<E, F>> {

    /**
     * Sets optional field index and returns the modified copy.
     *
     * @param theNewIndex the new index
     * @return the f */
    public F copy(final int theNewIndex);

    /**
     * @return the optionalFieldIndex
     */
    public int optionalFieldIndex();

}
