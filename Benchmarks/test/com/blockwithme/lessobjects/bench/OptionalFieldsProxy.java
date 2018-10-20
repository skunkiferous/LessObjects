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
package com.blockwithme.lessobjects.bench;

import com.blockwithme.lessobjects.annotations.StructField;

//CHECKSTYLE IGNORE FOR NEXT 600 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public interface OptionalFieldsProxy {

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

    int getIntField2();

    @StructField(optional = true)
    long getLongField();

    @StructField(optional = true)
    short getShortField();

    @StructField(optional = true)
    boolean isBooleanField();

    void setBooleanField(boolean theBoolean);

    void setByteField(byte theByte);

    void setCharField(char theChar);

    void setDoubleField(double theDouble);

    void setFloatField(float theFloat);

    void setIntField(int theInt);

    void setIntField2(int theInt);

    void setLongField(long theLong);

    void setShortField(short theShort);

}
