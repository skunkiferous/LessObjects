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

/** The Interface Base. */
public interface ProxyWithChild {

    byte getByteField();

    char getCharField();

    ProxyChild1 getChild1();

    double getDoubleField();

    float getFloatField();

    int getIntField();

    long getLongField();

    ProxyChild1 getSecond();

    short getShortField();

    boolean isBooleanField();

    void setBooleanField(final boolean theBoolean);

    void setByteField(final byte theByte);

    void setCharField(final char theChar);

    void setDoubleField(final double theDouble);

    void setFloatField(final float theFloat);

    void setIntField(final int theInt);

    void setLongField(final long theLong);

    void setShortField(final short theShort);

}
