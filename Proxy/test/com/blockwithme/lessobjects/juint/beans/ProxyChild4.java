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

import com.blockwithme.lessobjects.annotations.StructField;

/** The Interface ProxyChild1. */
public interface ProxyChild4 {
    @StructField(optional = true)
    byte getByteField();

    @StructField(optional = true)
    char getCharField();

    @StructField(optional = true)
    double getDoubleField();

    @StructField(optional = true)
    float getFloatField();

    @StructField(optional = true)
    int getIntField();

    @StructField(optional = true)
    long getLongField();

    @StructField(optional = true)
    short getShortField();

    short getShortField2();

    @StructField(optional = true)
    boolean isBooleanField();

    void setBooleanField(boolean theBoolean);

    void setByteField(byte theByte);

    void setCharField(char theChar);

    void setDoubleField(double theDouble);

    void setFloatField(float theFloat);

    void setIntField(int theInt);

    void setLongField(long theLong);

    void setShortField(short theShort);

    void setShortField2(short theShort);

}
