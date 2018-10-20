package com.blockwithme.lessobjects.juint.ser;

import java.io.IOException;

import com.blockwithme.lessobjects.juint.TestData;
import com.blockwithme.msgpack.impl.MessagePackPacker;
import com.blockwithme.msgpack.impl.ObjectPackerImpl;
import com.blockwithme.msgpack.schema.BasicSchemaManager;
import com.blockwithme.msgpack.schema.SchemaManager;
import com.blockwithme.msgpack.templates.PackerContext;
import com.blockwithme.msgpack.templates.Template;
import com.blockwithme.util.DataInputBuffer;
import com.blockwithme.util.DataOutputBuffer;

//CHECKSTYLE IGNORE FOR NEXT 100 LINES
@SuppressWarnings({ "PMD", "all" })
public abstract class BaseTest extends TestData {

    /** The schema manager. */
    private BasicSchemaManager schemaManager;

    @SuppressWarnings("rawtypes")
    protected abstract Template[] extended(final int theSchema);

    protected DataOutputBuffer newDataOutputBuffer() {
        return new DataOutputBuffer(2048);
    }

    protected ObjectPackerImpl newObjectPacker(final DataOutputBuffer theDob,
            final int theSchema) throws IOException {
        final PackerContext pc = new PackerContext(newSchemaManager(theSchema));
        pc.schemaID = theSchema;
        return new ObjectPackerImpl(newPacker(theDob), pc);
    }

    protected MessagePackPacker newPacker(final DataOutputBuffer theDob) {
        return new MessagePackPacker(theDob);
    }

    protected SchemaManager newSchemaManager(final int theSchema) {
        if (schemaManager == null) {
            schemaManager = new BasicSchemaManager(extended(theSchema)) {
                @Override
                protected int getBasicTemplateCount(final int theSchemaID) {
                    return 28;
                }
            };
        }
        return schemaManager;
    }

    protected DataInputBuffer toDataInputBuffer(final DataOutputBuffer theDob) {
        final byte[] bytes = theDob.buffer();
        final int size = theDob.size();
        return new DataInputBuffer(bytes, 0, size);
    }
}
