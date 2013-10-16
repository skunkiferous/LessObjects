/**
 *
 */
package com.blockwithme.lessobjects;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The Interface ChildrenIterator.
 *
 * Iterator of all the children of a Struct Object includes fields and Structs both
 *
 * @author tarung.
 */
@ParametersAreNonnullByDefault
public interface ChildrenIterator extends Iterable<Child> {

    /**
     * @return the base struct Object.
     */
    Struct struct();

}
