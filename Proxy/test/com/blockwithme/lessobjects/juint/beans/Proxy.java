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

/** The Interface that has all the possible field Types. */
public interface Proxy {

    @StructField(arraySize = 10)
    boolean[] getBooleanArray();

    @StructField(arraySize = 10)
    byte[] getByteArray();

    byte getByteField();

    @StructField(arraySize = 10)
    char[] getCharArray();

    char getCharField();

    @StructField(arraySize = 10)
    double[] getDoubleArray();

    double getDoubleField();

    @StructField(arraySize = 10)
    float[] getFloatArray();

    float getFloatField();

    @StructField(arraySize = 10)
    long[] getFullLongArray();

    @StructField(arraySize = 10)
    int[] getIntArray();

    int getIntField();

    Iterator<ListChild> getList(int theSize);

    @StructField(arraySize = 10, bits = 33)
    long[] getLongArray();

    long getLongField();

    @StructField(arraySize = 10)
    String[] getMyStrings();

    @StructField(converter = TestObjectToByteConverter.class)
    TestObject getObject();

    @StructField(arraySize = 10, converter = TestObjectToByteConverter.class)
    TestObject[] getObjectArray();

    @StructField(arraySize = 10)
    short[] getShortArray();

    short getShortField();

    boolean isBooleanField();

    void setBooleanArray(boolean[] theArray);

    void setBooleanField(boolean theBoolean);

    void setByteArray(byte[] theArray);

    void setByteField(byte theByte);

    void setCharArray(char[] theArray);

    void setCharField(char theChar);

    void setDoubleArray(double[] theArray);

    void setDoubleField(double theDouble);

    void setFloatArray(float[] theArray);

    void setFloatField(float theFloat);

    void setFullLongArray(long[] theArray);

    void setIntArray(int[] theIntArray);

    void setIntField(int theInt);

    void setLongArray(long[] theArray);

    void setLongField(long theLong);

    void setMyStrings(String[] theStrs);

    void setObject(TestObject theObject);

    void setObjectArray(TestObject[] theArray);

    void setShortArray(short[] theArray);

    void setShortField(short theShort);

}
