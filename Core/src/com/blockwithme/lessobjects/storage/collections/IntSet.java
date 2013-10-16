/**
 *
 */
package com.blockwithme.lessobjects.storage.collections;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A hash set of <code>int</code>s, implemented using HPPC classes.
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public interface IntSet {

    /** Checks if the set contains a given int in it.*/
    boolean contains(int theElement);

    /**
     * Removes all occurrences of <code>theElement</code> from this collection.
     *
     * @param theElement Element to be removed from this collection, if present.
     * @return The number of removed elements as a result of this call.
     */
    int removeAllOccurrences(int theElement);

    /**
     * Adds <code>theElement</code> to the set.
     *
     * @return Returns <code>true</code> if this element was not part of the set before. Returns
     * <code>false</code> if an equal element is part of the set.
     */
    public boolean add(int theElement);
}
