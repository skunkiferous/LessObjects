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

/**
 * The Interface VirtualField. All virtual fields must implement this.
 *
 * @param <T> the generic type
 *
 * @author tarung
 */
public interface VirtualField<T> {

    /** Returns the mapper of this field. */
    T mapper();

    /** Returns the name of the mapper class of this field. */
    String mapperClassName();
}
