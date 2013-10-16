/**
 *
 */
package com.blockwithme.lessobjects.juint.beans;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.annotations.StructField;

/** The Interface ProxyChildreArray.  */
@ParametersAreNonnullByDefault
public interface ProxyChildrenArray {

    byte getByteField();

    @StructField(arraySize = 10)
    ProxyChild1[] getChildArray();

    void setByteField(byte theByte);

}
