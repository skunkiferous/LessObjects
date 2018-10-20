/**
 *
 */
package com.blockwithme.dir;

import java.io.UnsupportedEncodingException;

/**
 * @author monster
 *
 */
public abstract class BaseDirectory implements Directory {

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#map(byte[])
     */
    @Override
    public final long map(final byte[] bytes) {
        return map(bytes, 0, bytes.length);
    }

    private final long map(final String str) {
        try {
            return map(str.getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#map(java.lang.CharSequence)
     */
    @Override
    public final long map(final CharSequence str) {
        if (str instanceof String) {
            return map((String) str);
        }
        if ((str instanceof StringBuilder) || (str instanceof StringBuffer)) {
            return map(str.toString());
        }
        return map(new StringBuilder(str).toString());
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#unmapString(long)
     */
    @Override
    public final String unmapString(final long id) {
        try {
            return new String(unmapBytes(id), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#child(byte[])
     */
    @Override
    public final Directory child(final byte[] bytes) {
        return child(map(bytes));
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#child(byte[], int, int)
     */
    @Override
    public final Directory child(final byte[] bytes, final int offset,
            final int length) {
        return child(map(bytes, offset, length));
    }

    /* (non-Javadoc)
     * @see com.blockwithme.dir.Directory#child(java.lang.CharSequence)
     */
    @Override
    public final Directory child(final CharSequence str) {
        return child(map(str));
    }
}
