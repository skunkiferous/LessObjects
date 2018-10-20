/**
 *
 */
package com.blockwithme.lessobjects.storage.collections;

import static com.blockwithme.lessobjects.util.StructConstants.MAX_CHAR_INDEX;

import javax.annotation.ParametersAreNonnullByDefault;

import com.carrotsearch.hppc.CharByteOpenHashMap;
import com.carrotsearch.hppc.CharCharOpenHashMap;
import com.carrotsearch.hppc.CharDoubleOpenHashMap;
import com.carrotsearch.hppc.CharFloatOpenHashMap;
import com.carrotsearch.hppc.CharIntOpenHashMap;
import com.carrotsearch.hppc.CharLongOpenHashMap;
import com.carrotsearch.hppc.CharObjectOpenHashMap;
import com.carrotsearch.hppc.CharOpenHashSet;
import com.carrotsearch.hppc.CharShortOpenHashMap;
import com.carrotsearch.hppc.IntByteOpenHashMap;
import com.carrotsearch.hppc.IntCharOpenHashMap;
import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.IntFloatOpenHashMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntLongOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntShortOpenHashMap;

/**
 * Implementation class of CollectionFactory interface.
 *
 * @author tarung
 *
 */

@ParametersAreNonnullByDefault
public class CollectionFactoryImpl implements CollectionFactory {

    /** The IntByteMap impl class using CharByteOpenHashMap */
    public static class IntByteMapCharImpl extends CharByteOpenHashMap
            implements IntByteMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public byte get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public byte put(final int theKey, final byte theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public byte remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntByteMap impl class using IntByteOpenHashMap */
    public static class IntByteMapImpl extends IntByteOpenHashMap implements
            IntByteMap {
        // NOP
    }

    /** The IntCharMap impl class using CharCharOpenHashMap */
    public static class IntCharMapCharImpl extends CharCharOpenHashMap
            implements IntCharMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public char get(final int theKey) {
            return super.get((char) theKey);
        }
        /** {@inheritDoc} */
        @Override
        public char put(final int theKey, final char theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public char remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntCharMap impl class using IntCharOpenHashMap */
    public static class IntCharMapImpl extends IntCharOpenHashMap implements
            IntCharMap {
        // NOP
    }

    /** The IntDoubleMap impl class using CharDoubleOpenHashMap */
    public static class IntDoubleMapCharImpl extends CharDoubleOpenHashMap
            implements IntDoubleMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public double get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public double put(final int theKey, final double theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public double remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntDoubleMap impl class using IntDoubleOpenHashMap */
    public static class IntDoubleMapImpl extends IntDoubleOpenHashMap implements
            IntDoubleMap {
        // NOP
    }

    /** The IntFloatMap impl class using CharFloatOpenHashMap */
    public static class IntFloatMapCharImpl extends CharFloatOpenHashMap
            implements IntFloatMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public float get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public float put(final int theKey, final float theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public float remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntFloatMap impl class using IntFloatOpenHashMap */
    public static class IntFloatMapImpl extends IntFloatOpenHashMap implements
            IntFloatMap {
        // NOP
    }

    /** The IntIntMap impl class using CharIntOpenHashMap */
    public static class IntIntMapCharImpl extends CharIntOpenHashMap implements
            IntIntMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public int get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public int put(final int theKey, final int theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public int remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntIntMap impl class using IntIntOpenHashMap */
    public static class IntIntMapImpl extends IntIntOpenHashMap implements
            IntIntMap {
        // NOP
    }

    /** The IntLongMap impl class using CharLongOpenHashMap */
    public static class IntLongMapCharImpl extends CharLongOpenHashMap
            implements IntLongMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public long get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public long put(final int theKey, final long theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public long remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntLongMap impl class using IntLongOpenHashMap */
    public static class IntLongMapImpl extends IntLongOpenHashMap implements
            IntLongMap {
        // NOP
    }

    /** The IntObjectMap impl class using CharObjectOpenHashMap */
    @SuppressWarnings("null")
    public static class IntObjectMapCharImpl<T> extends
            CharObjectOpenHashMap<T> implements IntObjectMap<T> {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public T get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public T put(final int theKey, final T theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        @SuppressWarnings("unchecked")
        public int putAll(final IntObjectMap<T> theOther) {

            if (theOther instanceof CharObjectOpenHashMap) {
                return super.putAll((CharObjectOpenHashMap<T>) theOther);
            }
            int count = 0;
            if (theOther instanceof IntObjectOpenHashMap) {
                final IntObjectOpenHashMap<T> otherMap = (IntObjectOpenHashMap<T>) theOther;
                if (otherMap.size() < MAX_CHAR_INDEX) {
                    final int[] keys1 = otherMap.keys;
                    final T[] values1 = otherMap.values;
                    for (int i = 0; i < keys1.length; i++) {
                        count += super.put((char) keys1[i], values1[i]) != null ? 1
                                : 0;
                    }
                } else {
                    throw new IllegalStateException(
                            "Maximum size exceeds the capacity of map : "
                                    + otherMap.size());
                }
                return count;
            }
            throw new IllegalStateException("Incompatible map types");
        }

        /** {@inheritDoc} */
        @Override
        public T remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntObjectMap impl class using IntObjectOpenHashMap */
    @SuppressWarnings("null")
    public static class IntObjectMapImpl<T> extends IntObjectOpenHashMap<T>
            implements IntObjectMap<T> {
        // NOP

        /** {@inheritDoc} */
        @Override
        @SuppressWarnings("unchecked")
        public int putAll(final IntObjectMap<T> theOther) {

            if (theOther instanceof IntObjectOpenHashMap) {
                return super.putAll((IntObjectOpenHashMap<T>) theOther);
            }
            int count = 0;
            if (theOther instanceof CharObjectOpenHashMap) {
                final CharObjectOpenHashMap<T> otherMap = (CharObjectOpenHashMap<T>) theOther;
                final char[] keys1 = otherMap.keys;
                final T[] values1 = otherMap.values;
                for (int i = 0; i < keys1.length; i++) {
                    count += super.put(keys1[i], values1[i]) != null ? 1 : 0;
                }
                return count;
            }
            throw new IllegalStateException("Incompatible map types");
        }

    }

    /** The IntSet impl class using CharOpenHashSet */
    public static class IntSetCharImpl extends CharOpenHashSet implements
            IntSet {

        /** {@inheritDoc} */
        @Override
        public boolean add(final int theElement) {
            return super.add((char) theElement);
        }

        /** {@inheritDoc} */
        @Override
        public boolean contains(final int theElement) {
            return super.contains((char) theElement);
        }
        /** {@inheritDoc} */
        @Override
        public int removeAllOccurrences(final int theElement) {
            return super.removeAllOccurrences((char) theElement);
        }
    }

    /** The IntSet impl class using IntOpenHashSet */
    public static class IntSetImpl extends IntOpenHashSet implements IntSet {
        // NOP
    }

    /** The IntShortMap impl class using CharShortOpenHashMap */
    public static class IntShortMapCharImpl extends CharShortOpenHashMap
            implements IntShortMap {

        /** {@inheritDoc} */
        @Override
        public boolean containsKey(final int theKey) {
            return super.containsKey((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public short get(final int theKey) {
            return super.get((char) theKey);
        }

        /** {@inheritDoc} */
        @Override
        public short put(final int theKey, final short theValue) {
            return super.put((char) theKey, theValue);
        }

        /** {@inheritDoc} */
        @Override
        public short remove(final int theKey) {
            return super.remove((char) theKey);
        }
    }

    /** The IntShortMap impl class using IntShortOpenHashMap */
    public static class IntShortMapImpl extends IntShortOpenHashMap implements
            IntShortMap {
        // NOP
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntByteMap createIntByteMap(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntByteMapCharImpl();
        }
        return new IntByteMapImpl();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntCharMap createIntCharMap(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntCharMapCharImpl();
        }
        return new IntCharMapImpl();

    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntDoubleMap createIntDoubleMap(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntDoubleMapCharImpl();
        }
        return new IntDoubleMapImpl();

    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntFloatMap createIntFloatMap(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntFloatMapCharImpl();
        }
        return new IntFloatMapImpl();

    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntIntMap createIntIntMap(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntIntMapCharImpl();
        }
        return new IntIntMapImpl();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntLongMap createIntLongMap(final int theCapacity) {
        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntLongMapCharImpl();
        }
        return new IntLongMapImpl();
    }

    /** {@inheritDoc} */
    @Override
    public <T> IntObjectMap<T> createIntObjectMap(final int theCapacity,
            final Class<T> theType) {
        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntObjectMapCharImpl<>();
        }
        return new IntObjectMapImpl<>();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "static-method", "unused" })
    public IntSet createIntSet(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            new IntSetCharImpl();
        }
        return new IntSetImpl();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("static-method")
    public IntShortMap createIntShortMap(final int theCapacity) {

        if (theCapacity <= MAX_CHAR_INDEX) {
            return new IntShortMapCharImpl();
        }
        return new IntShortMapImpl();
    }

}
