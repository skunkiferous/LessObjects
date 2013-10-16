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
package com.blockwithme.lessobjects.beans;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This class contains all the properties used to define a Struct instance.
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public class StructProperties {

    /** Is it already compiled ? */
    private final boolean compiled;

    /** Is a list ? */
    private final boolean list;

    /** Field to indicate an internal state while struct is being compiled. */
    private final boolean metaDataExtracted;

    /** Is this struct a union? */
    private final boolean union;

    /**
     * Instantiates a new struct properties.
     *
     * @param theMetaDataExtractedFlag the meta data extracted flag, property used by the
     * compiler to flag an intermediate state.
     * @param isUnion is union flag
     * @param isList is list flag.
     */
    public StructProperties(final boolean theMetaDataExtractedFlag,
            final boolean isUnion, final boolean isList,
            final boolean isCompiled) {

        metaDataExtracted = theMetaDataExtractedFlag;
        union = isUnion;
        list = isList;
        compiled = isCompiled;

    }

    /** Is it already Compiled ? */
    public boolean compiled() {
        return compiled;
    }

    /** Is it a list child Struct ?*/
    public boolean isList() {
        return list;
    }

    /**
     * The meta data extracted flag; property used by the compiler to indicate an
     * intermediate state.
     */
    public boolean isMetaDataExtracted() {
        return metaDataExtracted;
    }

    /**  Sets the 'compiled' flag.*/
    public StructProperties setCompiled(final boolean isCompiled) {
        return new StructProperties(metaDataExtracted, union, list, isCompiled);
    }

    /** Sets the 'is-list' flag.*/
    public StructProperties setList(final boolean isList) {
        return new StructProperties(metaDataExtracted, union, isList, compiled);
    }

    /** Sets the 'meta data extracted' flag, The meta data extracted flag, property used by
     * the compiler to indicate an intermediate state.  */
    public StructProperties setMetaDataExtracted(
            final boolean theMetaDataExtractedFlag) {
        return new StructProperties(theMetaDataExtractedFlag, union, list,
                compiled);
    }

    /** Is it a union ?*/
    public boolean union() {
        return union;
    }

}
