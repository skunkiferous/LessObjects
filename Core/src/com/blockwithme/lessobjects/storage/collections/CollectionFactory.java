/**
 *
 */
package com.blockwithme.lessobjects.storage.collections;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A class that provides factory methods of primitive collections types.
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public interface CollectionFactory {

    /**
     * Creates a int byte map.
     *
     * @param theCapacity the max capacity
     * @return the int byte map
     */
    public abstract IntByteMap createIntByteMap(int theCapacity);

    /**
     * Creates a int char map.
     *
     * @param theCapacity the max capacity
     * @return the int char map
     */
    public abstract IntCharMap createIntCharMap(int theCapacity);

    /**
     * Creates a int double map.
     *
     * @param theCapacity the max capacity
     * @return the int double map
     */
    public abstract IntDoubleMap createIntDoubleMap(int theCapacity);

    /**
     * Creates a int float map.
     *
     * @param theCapacity the max capacity
     * @return the int float map
     */
    public abstract IntFloatMap createIntFloatMap(int theCapacity);

    /**
     * Creates a int-int map.
     *
     * @param theCapacity the max capacity
     * @return the int-int map
     */
    public abstract IntIntMap createIntIntMap(int theCapacity);

    /**
     * Creates int long map.
     *
     * @param theCapacity the max capacity
     * @return the int long map
     */
    public abstract IntLongMap createIntLongMap(int theCapacity);

    /**
     * Creates a int object map.
     *
     * @param <T> the object type
     * @param theCapacity the max capacity
     * @param theType the type
     * @return the int object map.
     */
    public abstract <T> IntObjectMap<T> createIntObjectMap(int theCapacity,
            Class<T> theType);

    /**
     * Creates a int set.
     *
     * @param theCapacity the max capacity
     * @return the int set
     */
    public abstract IntSet createIntSet(int theCapacity);

    /**
     * Creates a int short map.
     *
     * @param theCapacity the max capacity
     * @return the int short map
     */
    public abstract IntShortMap createIntShortMap(int theCapacity);

}