package com.blockwithme.lessobjects.fields.object;

import com.blockwithme.prim.ClassConfiguredConverter;

/**
 * GenericPseudoConverter is used for ObjectFields, which don't really need a converter.
 *
 * @param <E> the element type
 *
 * @author monster
 */
public class GenericPseudoConverter<E> extends ClassConfiguredConverter<E, E> {

    /** Constructor with object type. */
    public GenericPseudoConverter(final Class<E> theType) {
        super(theType);
    }

    /** Constructor with object type. */
    public GenericPseudoConverter(final String theType) {
        super(theType);
    }

    @Override
    public int bits() {
        return -1;
    }
}
