/**
 *
 */
package com.blockwithme.lessobjects.juint.ser;

import static com.blockwithme.lessobjects.juint.Constants.COMPILERS;
import static com.blockwithme.lessobjects.juint.Constants.DELTA;
import static com.blockwithme.lessobjects.juint.Constants.FACTORY;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.BooleanValueChange;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.IntValueChange;
import com.blockwithme.lessobjects.beans.LongValueChange;
import com.blockwithme.lessobjects.beans.ShortValueChange;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
import com.blockwithme.lessobjects.fields.optional.LongOptionalField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.juint.CompiledStorage;
import com.blockwithme.lessobjects.juint.TestVirtualFields;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.ChangeRecords;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.serialization.StructTemplates;
import com.blockwithme.msgpack.impl.MessagePackUnpacker;
import com.blockwithme.msgpack.impl.ObjectPackerImpl;
import com.blockwithme.msgpack.impl.ObjectUnpackerImpl;
import com.blockwithme.msgpack.templates.Template;
import com.blockwithme.msgpack.templates.UnpackerContext;
import com.blockwithme.util.DataInputBuffer;
import com.blockwithme.util.DataOutputBuffer;
import com.carrotsearch.hppc.IntByteMap;
import com.carrotsearch.hppc.IntByteOpenHashMap;
import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntStack;

/** @author tarung */
// CHECKSTYLE IGNORE FOR NEXT 700 LINES
@SuppressWarnings({ "PMD", "all" })
@ParametersAreNonnullByDefault
public class SerializationTest extends BaseTest {

    /** The base struct. */
    protected Struct basestruct;

    /** The factory. */
    protected CompiledStorage[] COMPILED;

    final static boolean[] BOOLEANS = booleans();

    final static byte[] BYTES = bytes();

    final static char[] CHARS = chars();

    final static double[] DOUBLES = doubles();

    final static float[] FLOATS = floats();

    final static int[] INTS = ints();

    final static long[] LONGS = longs();

    final static short[] SHORTS = shorts();
    final static String[] STRINGS = strings();
    /** Validate the change object*/
    private void checkChanges(final ByteField byteField,
            final BooleanField bool1, final CharField charField,
            final DoubleField doubleField, final FloatField floatField,
            final IntField intField, final LongField longField,
            final ShortField shortField, final IntOptionalField intOptional,
            final LongOptionalField longOptional, final Storage storage,
            final ChangeRecords actions) {

        final Iterator<ValueChange<?>> changes = actions.changes(storage
                .rootStruct());
        int count = 0;

        while (changes.hasNext()) {

            final ValueChange c = changes.next();
            count = c.structureIndex();

            if (BOOLEANS[count] && c instanceof BooleanValueChange) {
                final BooleanValueChange change = (BooleanValueChange) c;
                assertEquals(bool1, change.field());
                assertEquals(false, change.booleanOldValue());
                assertEquals(BOOLEANS[count], change.booleanNewValue());
                assertEquals(false, change.oldValue());
                assertEquals(BOOLEANS[count], change.newValue());
            }

            if (BYTES[count] > 0 && c instanceof ByteValueChange) {
                final ByteValueChange change = (ByteValueChange) c;
                assertEquals(byteField, change.field());
                assertEquals((byte) 0, change.byteOldValue());
                assertEquals(BYTES[count], change.byteNewValue());
                assertEquals((byte) 0, change.oldValue().byteValue());
                assertEquals(BYTES[count], change.newValue().byteValue());
            }

            if (CHARS[count] > 0 && c instanceof CharValueChange) {
                final CharValueChange change = (CharValueChange) c;
                assertEquals(charField, change.field());
                assertEquals((char) 0, change.charOldValue());
                assertEquals(CHARS[count], change.charNewValue());
                assertEquals((char) 0, change.oldValue().charValue());
                assertEquals(CHARS[count], change.newValue().charValue());
            }

            if (DOUBLES[count] > 0 && c instanceof DoubleValueChange) {
                final DoubleValueChange change = (DoubleValueChange) c;
                assertEquals(doubleField, change.field());
                assertEquals(0d, change.doubleOldValue(), DELTA);
                assertEquals(DOUBLES[count], change.doubleNewValue(), DELTA);
                assertEquals(0d, change.oldValue().doubleValue(), DELTA);
                assertEquals(DOUBLES[count], change.newValue().doubleValue(),
                        DELTA);
            }

            if (FLOATS[count] > 0 && c instanceof FloatValueChange) {
                final FloatValueChange change = (FloatValueChange) c;
                assertEquals(floatField, change.field());
                assertEquals(0f, change.floatOldValue(), DELTA);
                assertEquals(FLOATS[count], change.floatNewValue(), DELTA);
                assertEquals(0f, change.oldValue().floatValue(), DELTA);
                assertEquals(FLOATS[count], change.newValue().floatValue(),
                        DELTA);
            }

            if (INTS[count] > 0 && c instanceof IntValueChange) {

                final IntValueChange change = (IntValueChange) c;
                final Field<?, ?> field = change.field();
                if (field instanceof IntOptionalField) {
                    assertEquals(intOptional, field);
                } else {
                    assertEquals(intField, field);
                }
                assertEquals(0, change.intOldValue());
                assertEquals(INTS[count], change.intNewValue());
                assertEquals(0, change.oldValue().intValue());
                assertEquals(INTS[count], change.newValue().intValue());
            }

            if (SHORTS[count] > 0 && c instanceof ShortValueChange) {
                final ShortValueChange change = (ShortValueChange) c;
                assertEquals(shortField, change.field());
                assertEquals((short) 0, change.shortOldValue());
                assertEquals(SHORTS[count], change.shortNewValue());
                assertEquals((short) 0, change.oldValue().shortValue());
                assertEquals(SHORTS[count], change.newValue().shortValue());
            }
            if (LONGS[count] > 0 && c instanceof LongValueChange) {
                final LongValueChange change = (LongValueChange) c;
                final Field<?, ?> field = change.field();
                if (field instanceof LongOptionalField) {
                    assertEquals(longOptional, field);
                } else {
                    assertEquals(longField, field);
                }
                assertEquals(0l, change.longOldValue());
                assertEquals(LONGS[count], change.longNewValue());
                assertEquals(0l, change.oldValue().longValue());
                assertEquals(LONGS[count], change.newValue().longValue());
            }
        }
    }
    private void serializeStruct(final Struct str) throws IOException {

        final DataOutputBuffer dob = newDataOutputBuffer();
        final ObjectPackerImpl packer = newObjectPacker(dob, 100);
        packer.writeObject(str);

        final DataInputBuffer dib = toDataInputBuffer(dob);
        final MessagePackUnpacker mpu = new MessagePackUnpacker(dib);
        final ObjectUnpackerImpl oui = new ObjectUnpackerImpl(mpu,
                new UnpackerContext(newSchemaManager(42)));
        final Object o = oui.readObject();
        assertEquals(str.toString(), o.toString());
    }
    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "rawtypes", "null" })
    protected Template[] extended(final int theSchema) {
        return StructTemplates.templates();
    }
    @Before
    public void setup() {
        basestruct = new Struct("FieldTest", new Struct[] {},
                new Field<?, ?>[] { FACTORY.newIntField("intField"),
                        FACTORY.newLongField("longField"),
                        FACTORY.newByteField("byteField"),
                        FACTORY.newShortField("shortField"),
                        FACTORY.newFloatField("floatField"),
                        FACTORY.newDoubleField("doubleField"),
                        FACTORY.newCharField("charField"),
                        FACTORY.newBooleanField("booleanField1"),
                        FACTORY.newBooleanField("booleanField2"),
                        FACTORY.newIntField("intField2"),
                        FACTORY.newIntOptional("intOptional"),
                        FACTORY.newLongOptional("longFieldOptional"),
                        FACTORY.newStringField("stringField1") });
        int count = 0;
        COMPILED = new CompiledStorage[COMPILERS.length];
        for (final Compiler cmplr : COMPILERS) {
            COMPILED[count] = new CompiledStorage();
            COMPILED[count].compiledStructs = cmplr.compile(basestruct);
            COMPILED[count].storage = cmplr.initStorage(
                    COMPILED[count].compiledStructs, _CAPACITY);
            COMPILED[count].compiler = cmplr;
            count++;
        }
    }
    @Test
    public void testMap1() throws IOException {

        final IntIntMap stk = new IntIntOpenHashMap();
        stk.put(5, 1);
        stk.put(4, 2);
        stk.put(3, 3);

        final DataOutputBuffer dob = newDataOutputBuffer();
        final ObjectPackerImpl packer = newObjectPacker(dob, 101);
        packer.writeObject(stk);

        final DataInputBuffer dib = toDataInputBuffer(dob);
        final MessagePackUnpacker mpu = new MessagePackUnpacker(dib);
        final ObjectUnpackerImpl oui = new ObjectUnpackerImpl(mpu,
                new UnpackerContext(newSchemaManager(42)));

        final IntIntMap out = (IntIntMap) oui.readObject();
        assertEquals(stk, out);

    }
    @Test
    public void testMap2() throws IOException {

        final IntByteMap stk = new IntByteOpenHashMap();
        stk.put(5, (byte) 1);
        stk.put(4, (byte) 2);
        stk.put(3, (byte) 3);

        final DataOutputBuffer dob = newDataOutputBuffer();
        final ObjectPackerImpl packer = newObjectPacker(dob, 102);
        packer.writeObject(stk);

        final DataInputBuffer dib = toDataInputBuffer(dob);
        final MessagePackUnpacker mpu = new MessagePackUnpacker(dib);
        final ObjectUnpackerImpl oui = new ObjectUnpackerImpl(mpu,
                new UnpackerContext(newSchemaManager(42)));

        assertEquals(stk, oui.readObject());

    }
    @Test
    public void testStack() throws IOException {

        final IntStack stk = new IntStack();
        stk.push(5);
        stk.push(4);
        stk.push(3);

        final DataOutputBuffer dob = newDataOutputBuffer();
        final ObjectPackerImpl packer = newObjectPacker(dob, 103);
        packer.writeObject(stk);

        final DataInputBuffer dib = toDataInputBuffer(dob);
        final MessagePackUnpacker mpu = new MessagePackUnpacker(dib);
        final ObjectUnpackerImpl oui = new ObjectUnpackerImpl(mpu,
                new UnpackerContext(newSchemaManager(42)));

        final IntStack out = (IntStack) oui.readObject();
        assertEquals(stk, out);

    }

    @Test
    public void testStorage() throws IOException {

        final CompiledStorage strg = COMPILED[0];
        final Struct cStruct = strg.compiledStructs;
        final ByteField byteField = cStruct.field("byteField");
        final BooleanField bool1 = cStruct.field("booleanField1");
        final CharField charField = cStruct.field("charField");
        final DoubleField doubleField = cStruct.field("doubleField");
        final FloatField floatField = cStruct.field("floatField");
        final IntField intField = cStruct.field("intField");
        final LongField longField = cStruct.field("longField");
        final ShortField shortField = cStruct.field("shortField");
        final ObjectField<String, ?> str1 = cStruct.field("stringField1");
        final IntOptionalField intOptional = cStruct.field("intOptional");
        final LongOptionalField longOptional = cStruct
                .field("longFieldOptional");

        final Storage storage = strg.storage;

        for (int i = 0; i < _CAPACITY; i++) {
            storage.selectStructure(i);
            storage.write(bool1, BOOLEANS[i]);
            storage.write(byteField, BYTES[i]);
            storage.write(charField, CHARS[i]);
            storage.write(doubleField, DOUBLES[i]);
            storage.write(intField, INTS[i]);
            storage.write(floatField, FLOATS[i]);
            storage.write(longField, LONGS[i]);
            storage.write(shortField, SHORTS[i]);
            storage.write(str1, "test" + INTS[i]);
            storage.write(intOptional, INTS[i]);
            storage.write(longOptional, LONGS[i]);
        }

        final ActionSet actions = storage.transactionManager().commit();

        checkChanges(byteField, bool1, charField, doubleField, floatField,
                intField, longField, shortField, intOptional, longOptional,
                storage, actions.changeRecords());

        final DataOutputBuffer dob = newDataOutputBuffer();
        final ObjectPackerImpl packer = newObjectPacker(dob, 104);
        packer.writeObject(storage);
        packer.writeObject(actions.changeRecords());

        final DataInputBuffer dib = toDataInputBuffer(dob);
        final MessagePackUnpacker mpu = new MessagePackUnpacker(dib);
        final ObjectUnpackerImpl oui = new ObjectUnpackerImpl(mpu,
                new UnpackerContext(newSchemaManager(42)));

        final Storage o = (Storage) oui.readObject();

        for (int i = 0; i < _CAPACITY; i++) {
            storage.selectStructure(i);

            assertEquals(BOOLEANS[i], storage.read(bool1));
            assertEquals(BYTES[i], storage.read(byteField));
            assertEquals(CHARS[i], storage.read(charField));
            assertEquals(DOUBLES[i], storage.read(doubleField), DELTA);
            assertEquals(FLOATS[i], storage.read(floatField), DELTA);
            assertEquals(INTS[i], storage.read(intField));
            assertEquals(LONGS[i], storage.read(longField));
            assertEquals(SHORTS[i], storage.read(shortField));
            assertEquals("test" + INTS[i], storage.read(str1));
            assertEquals(INTS[i], storage.read(intOptional));
            assertEquals(LONGS[i], storage.read(longOptional));
        }
        final ChangeRecords cr = (ChangeRecords) oui.readObject();
        checkChanges(byteField, bool1, charField, doubleField, floatField,
                intField, longField, shortField, intOptional, longOptional,
                storage, cr);
    }

    @Test
    public void testStruct() throws Exception {
        final DataOutputBuffer dob = newDataOutputBuffer();
        final ObjectPackerImpl packer = newObjectPacker(dob, 105);
        packer.writeObject(basestruct);
        final DataInputBuffer dib = toDataInputBuffer(dob);
        final MessagePackUnpacker mpu = new MessagePackUnpacker(dib);
        final ObjectUnpackerImpl oui = new ObjectUnpackerImpl(mpu,
                new UnpackerContext(newSchemaManager(42)));
        final Struct o = (Struct) oui.readObject();
        assertEquals(basestruct, o);
    }

    @Test
    public void testVirtual() throws IOException {
        final TestVirtualFields vf = new TestVirtualFields();
        vf.init();
        final Struct str = vf.COMPILED[0].compiledStructs;
        serializeStruct(str);
    }
}
