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
package com.blockwithme.lessobjects.juint.beans;

import java.util.Iterator;

import com.blockwithme.lessobjects.annotations.StructField;
import com.blockwithme.lessobjects.proxy.ListenerSupport;

/** The Interface Proxy2. */
public interface Proxy2 extends ListenerSupport {

    byte getByteField();

    Iterator<ListChild> getList(int theSize);

    @StructField(arraySize = 10, converter = TestObjectToLongConverter.class)
    TestObject[] getSimpleObjectArray();

    void setByteField(byte theByte);

    void setSimpleObjectArray(TestObject[] theArray);

}
