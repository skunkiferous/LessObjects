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

import java.lang.instrument.IllegalClassFormatException;
import java.util.List;
import java.util.Map;

import javassist.CtClass;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The Parser interface, classes implementing this interface provide methods to
 * parse a bean-style interface and get the field information from it.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface InterfaceParser {

    /** Gets the field information from a bean-style interface.
     *
     * @param theInterface the interface to be parsed.
     * @return the field information map
     * @throws IllegalClassFormatException the illegal class format exception */
    Map<String, FieldInfo> getFieldInfo(final CtClass theInterface,
            final boolean theMergeConflictsFlag)
            throws IllegalClassFormatException;

    /** Gets the field information from a bean-style interface.
     *
     * @param theInterface the interface to be parsed.
     * @return the field information map
     * @throws IllegalClassFormatException the illegal class format exception */
    Map<String, FieldInfo> getFieldInfo(final CtClass theInterface,
            @Nullable final List<CtClass> theAdditionalInterfaces,
            final boolean theMergeConflictsFlag)
            throws IllegalClassFormatException;

}
