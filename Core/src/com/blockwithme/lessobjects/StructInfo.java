/**
 *
 */
package com.blockwithme.lessobjects;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The Struct Info class used to define Struct's meta data.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class StructInfo {

    /** The created by. */
    @Nonnull
    private final String createdBy;

    /** The create on. */
    @Nonnull
    private final Date createdOn;

    /** The version. */
    @Nonnull
    private final String schemaVersion;

    /**
     * Instantiates a new struct info.
     */
    public StructInfo(final String theCreatedByName,
            final Date theCreatedOnDate, final String theSchemaVersion) {
        createdBy = theCreatedByName;
        createdOn = theCreatedOnDate;
        schemaVersion = theSchemaVersion;
    }

    /** The Created by Name. */
    @SuppressWarnings("null")
    public String createdBy() {
        return createdBy;
    }

    /** The Created on Date. */
    @SuppressWarnings("null")
    public Date createdOn() {
        return createdOn;
    }

    /** The Schema version.*/
    @SuppressWarnings("null")
    public String schemaVersion() {
        return schemaVersion;
    }
}
