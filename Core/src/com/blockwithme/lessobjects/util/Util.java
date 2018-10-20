/**
 *
 */
package com.blockwithme.lessobjects.util;

import static com.blockwithme.lessobjects.util.StructConstants.INT_BITS;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.object.ObjectField;

/**
 * Class containing some utility methods.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Util {

    /** The Constant INT_MASK. */
    private static final long INT_MASK = 0xFFFFFFFFL;

    /** Converts a list of children to array. */
    @SuppressWarnings("null")
    public static Struct[] childrenArray(final List<Struct> theChildren) {
        return theChildren.toArray(new Struct[theChildren.size()]);
    }

    /** Returns the class for a name.
     * @throws ClassNotFoundException */
    public static Class<?> classForName(final String theClassName)
            throws ClassNotFoundException {
        // TODO Use ClassNameConverter instead
        return Class.forName(theClassName);
    }

    /** Converts a list of fields to array. */
    @SuppressWarnings("null")
    public static Field<?, ?>[] fieldArray(final List<Field<?, ?>> theList) {
        return theList.toArray(new Field<?, ?>[theList.size()]);
    }

    /** Returns the fully qualified name of a class. */
    public static String getClassName(final Class<?> clazz) {
        // TODO Use ClassNameConverter instead
        return clazz == null ? null : clazz.getName();
    }

    /** Combines two ints into a long. */
    public static long intsToLong(final int theA, final int theB) {
        return (long) theA << INT_BITS | theB & INT_MASK;
    }

    /** Splits a long into two ints (reverse of 'intsToLong') */
    public static void longToInts(final long theLong, final int[] ints) {
        ints[0] = (int) (theLong >> INT_BITS);
        ints[1] = (int) theLong;
    }

    /** Converts a list of object fields to array. */
    @SuppressWarnings("null")
    public static ObjectField<?, ?>[] objectArray(
            final List<ObjectField<?, ?>> theList) {
        return theList.toArray(new ObjectField<?, ?>[theList.size()]);
    }
}
