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
/**
 *
 */
package com.blockwithme.lessobjects.proxy;

import static com.blockwithme.lessobjects.util.StructConstants.STRING_CLASS_NAME;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The Utility class, to be used within the package.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
class Util {

    /**
     * A FieldInfo is a field when the type is one of the following  :
     * (This distinction is basically because we can have setter methods only for
     * these types, other types like Child-Interfaces, Lists and ChildrenArrays
     * do not have setter methods, i.e. any modifications in the instances returned
     * by the getter methods, of non-field types will updates the values in the storage.)
     *
     *  1. Primitive.
     *  2. String.
     *  3. Enum
     *  4. Is an object type with Object-to-long or Object-to-byte-array converter.
     *  5. Is an array of primitive types.
     */
    static boolean isField(final FieldInfo theFieldInfo) {
        return theFieldInfo.type.isPrimitive()
                || theFieldInfo.type.getName().equals(STRING_CLASS_NAME)
                || theFieldInfo.type.isEnum() || theFieldInfo.isObjectField
                || theFieldInfo.isPrimitiveArray;
    }

    /** Converts first letter to lower. */
    static final String toLower(final String theName) {
        final char[] stringArray = theName.toCharArray();
        stringArray[0] = Character.toLowerCase(stringArray[0]);
        return new String(stringArray);
    }

    /** Converts first letter to upper. */
    static final String toUpper(final String theName) {
        final char[] stringArray = theName.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }
}
