/**
 *
 */
package com.blockwithme.dir;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author monster
 *
 */
@ParametersAreNonnullByDefault
public class DummyDirectory extends BaseDirectory {

    private static class BytesAndChild {
        public byte[] bytes;
        public Directory child;
    }

    private final Map<Long, BytesAndChild> map = new HashMap<Long, BytesAndChild>();

    /** really bad hash */
    private static long hash(final byte[] bytes, final int offset,
            final int length) {
        long result = 1;
        for (int i = offset; i < offset + length; i++) {
            result = result * 17 + bytes[i];
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#map(byte[], int, int)
     */
    @Override
    public long map(final byte[] theBytes, final int offset, final int length) {
        final long id = hash(theBytes, offset, length);
        BytesAndChild o = map.get(id);
        if (o != null) {
            // TODO : check equality of byte array (range), and throw if not equals
        } else {
            o = new BytesAndChild();
            if ((offset == 0) && (length == theBytes.length)) {
                o.bytes = theBytes.clone();
            } else {
                final byte[] copy = new byte[length];
                System.arraycopy(theBytes, offset, copy, 0, length);
                o.bytes = copy;
            }
        }
        return 0;
    }

    private BytesAndChild unmap(final long id) {
        final BytesAndChild result = map.get(id);
        if (result == null) {
            throw new IllegalArgumentException("Not found: " + id);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#unmapBytes(long)
     */
    @Override
    public byte[] unmapBytes(final long id) {
        final BytesAndChild o = unmap(id);
        return o.bytes.clone();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#child(long)
     */
    @Override
    public Directory child(final long id) {
        final BytesAndChild o = unmap(id);
        if (o.child == null) {
            o.child = new DummyDirectory();
        }
        return o.child;
    }

    /** Returns all the IDs know so far. */
    @Override
    public long[] ids() {
        final long[] result = new long[map.size()];
        int index = 0;
        for (final Long l : map.keySet()) {
            result[index++] = l;
        }
        return result;
    }

}
