/**
 *
 */
package com.blockwithme.dir;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Maps bytes/String to long and back, recursively.
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public interface Directory {
    /**
     * Maps bytes to a unique long.
     * Fails, if a unique long cannot be created.
     */
    long map(final byte[] bytes);

    /**
     * Maps bytes to a unique long.
     * Fails, if a unique long cannot be created.
     */
    long map(final byte[] bytes, final int offset, final int length);

    /**
     * Maps a String to a unique long, by converting it to UTF-8 bytes.
     * Fails, if a unique long cannot be created.
     */
    long map(final CharSequence str);

    /** Unmaps an existing ID to it's bytes. Fails if the ID is unknown. */
    byte[] unmapBytes(final long id);

    /**
     * Unmaps an existing ID to a String. A failure might occur if the bytes
     * cannot be turned to a String. Fails if the ID is unknown.
     */
    String unmapString(final long id);

    /** Returns a child directory, under the given ID. The ID must already exist! */
    Directory child(final long id);

    /** Returns a child directory, by mapping the key to and ID first. */
    Directory child(final byte[] bytes);

    /** Returns a child directory, by mapping the key to and ID first. */
    Directory child(final byte[] bytes, final int offset, final int length);

    /** Returns a child directory, by mapping the key to and ID first. */
    Directory child(final CharSequence str);

    /** Returns all the IDs know so far. */
    long[] ids();
}
