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

package com.blockwithme.lessobjects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.blockwithme.prim.Converter;

/** The Field annotation
 *
 * @author tarung
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface StructField {
    class DEFAULT_CONVERTER implements Converter<Object> {
        @Override
        public int bits() {
            throw new UnsupportedOperationException();
        }
        @Override
        public Class<Object> type() {
            throw new UnsupportedOperationException();
        }
    }

    /** Array size annotation property applicable for array fields */
    int arraySize() default 0;

    /** Field size in bits. */
    int bits() default 0;

    /** The converter class. */
    Class<? extends Converter<?>> converter() default DEFAULT_CONVERTER.class;

    /** Is a Global field ? */
    boolean global() default false;

    /** Is an Optional field/child ? */
    boolean optional() default false;

}
