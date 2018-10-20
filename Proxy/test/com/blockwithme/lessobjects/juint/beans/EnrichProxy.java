/**
 *
 */
package com.blockwithme.lessobjects.juint.beans;

import com.blockwithme.lessobjects.annotations.Enrich;

/**
 * The Interface EnrichProxy.
 */
@Enrich
public interface EnrichProxy extends ProxyWithChild {

    int getEnrichedByte();

    void setEnrichedByte(final int theInt);

}
