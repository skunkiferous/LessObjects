/**
 *
 */
package com.blockwithme.lessobjects.juint.beans;

import com.blockwithme.lessobjects.annotations.Enrich;

/**
 * The Interface EnrichProxyChild.
 */
@Enrich
public interface EnrichProxyChild extends ProxyChild1 {

    int getEnrichedInt();

    void setEnrichedInt(final int theInt);

}
