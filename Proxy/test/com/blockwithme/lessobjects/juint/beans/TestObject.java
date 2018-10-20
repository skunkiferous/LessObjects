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
package com.blockwithme.lessobjects.juint.beans;

/** The Class TestObject. */
@SuppressWarnings("PMD")
public class TestObject {

    /** The name. */
    private String name;

    public TestObject(final String theName) {
        name = theName;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object theObj) {
        if (this == theObj) {
            return true;
        }
        if (theObj == null) {
            return false;
        }
        if (getClass() != theObj.getClass()) {
            return false;
        }
        final TestObject other = (TestObject) theObj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public String name() {
        return name;
    }

    public void name(final String theName) {
        name = theName;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "TestObject [name=" + name + "]";
    }
}
