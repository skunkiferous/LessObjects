/**
 *
 */
package com.blockwithme.lessobjects.storage.collections;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public interface IntDoubleMap {

    /**
     * Returns <code>true</code> if this container has an association to a value for
     * the given key.
     */
    public boolean containsKey(int theKey);

    /**
     * Returns the value for a given key.
     *
     * @return The value stored under the given key in the map is returned.
     */
    public double get(int theKey);

    /**
     * Returns the last value saved in a call to {@link #containsKey}.
     *
     * @see #containsKey
     */
    public double lget();

    /**
     * Place a given key and value in the container.
     *
     * @return The value previously stored under the given key in the map is returned.
     */
    public double put(int theKey, double theValue);

    /**
     * Remove all values at the given key. The default value for the key type is returned
     * if the value does not exist in the map.
     */
    public double remove(int theKey);

}
