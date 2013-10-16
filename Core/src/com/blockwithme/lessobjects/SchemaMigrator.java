/**
 *
 */
package com.blockwithme.lessobjects;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.storage.Storage;

/**
 * Schema of the data in the code will change over time, while there exists some data produced
 * using the old schema. SchemaMapping allows us to provide an implementation logic to "convert"
 * a struct, to some new struct schema.
 * Conversion might in most case be automatic, when old fields are dropped, or new fields just need
 * to get the default value, there are cases where the new, or even existing, fields need to be recomputed,
 * using the old data. We also have to cover the case where a field continues to exist, but
 * changes in type (boolean to enum is a common case).
 *
 * @author tarung
 *
 */
@ParametersAreNonnullByDefault
public interface SchemaMigrator {

    /**
     * This method produces a map wherein fields from the new schema are mapped to fields from the old schema.
     * All the fields in new Schema would be present as keys in the returned map, it is considered that a field
     * is added in the new schema with default value if the corresponding 'value' is null.
     *
     * @return the returned new to old map contains all the fields including that of children structs.
     */
    Map<Field<?, ?>, Field<?, ?>> mapSchema(Struct theNewStruct,
            Struct theOldStruct);

    /**
     * Post processor method is invoked at the time when Storage data is being copied from one internal structure
     * to another see - {@link Storage#copyStorage(Storage)}.
     *
     * @param theSource the source storage
     * @param theDestination the destination storage
     * @param theSourceIndex the element index in the Source
     * @param theDestinationIndex the element index in the Destination (usually same as source index)
     */
    void postProcessor(Storage theSource, Storage theDestination,
            int theSourceIndex, int theDestinationIndex);
}
