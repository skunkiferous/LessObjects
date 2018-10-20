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
// $codepro.audit.disable
/**
 *
 */
package com.blockwithme.lessobjects.juint.beans;

import com.blockwithme.base40.Base40;
import com.blockwithme.prim.LongConverter;

//CHECKSTYLE IGNORE FOR NEXT 50 LINES
/** The Class TestObjectToByteConverter. */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class TestObjectToLongConverter implements LongConverter<TestObject> {

    /** {@inheritDoc} */
    @Override
    public int bits() {
        return 64;
    }

    /** {@inheritDoc} */
    @Override
    public long fromObject(final TestObject theObject) {
        if (theObject != null) {
            return new Base40(Base40.getDefaultCharacterSet(), theObject.name())
                    .asLong();
        } else {
            return -1L;
        }
    }

    /** {@inheritDoc} */
    @Override
    public TestObject toObject(final long theValue) {
        if (theValue == -1L) {
            return null;
        } else {
            return new TestObject(new Base40(Base40.getDefaultCharacterSet(),
                    theValue).toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<TestObject> type() {
        return TestObject.class;
    }
}
