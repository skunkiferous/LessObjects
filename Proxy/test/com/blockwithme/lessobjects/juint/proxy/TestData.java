/**
 *
 */
package com.blockwithme.lessobjects.juint.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import com.blockwithme.lessobjects.SchemaFormat;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
import com.blockwithme.lessobjects.fields.optional.FloatOptionalField;
import com.blockwithme.lessobjects.fields.optional.LongOptionalField;
import com.blockwithme.lessobjects.fields.optional.ShortOptionalField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.prim.BooleanConverter;
import com.blockwithme.prim.ByteConverter;
import com.blockwithme.prim.CharConverter;
import com.blockwithme.prim.ConverterRegistry;
import com.blockwithme.prim.DoubleConverter;
import com.blockwithme.prim.FloatConverter;
import com.blockwithme.prim.IntConverter;
import com.blockwithme.prim.LongConverter;
import com.blockwithme.prim.ShortConverter;

// CHECKSTYLE IGNORE FOR NEXT 600 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestData {

    public static class BooleanObject {

        boolean content;

        public BooleanObject(final boolean theContent) {
            content = theContent;
        }

        public String toString() {
            return new Boolean(content).toString();
        }
    }

    public static final class BooleanPrimConverter implements
            BooleanConverter<BooleanObject> {
        @Override
        public int bits() {
            return 1;
        }

        @Override
        public boolean fromObject(final BooleanObject obj) {
            if (obj == null) {
                return false;
            }
            return obj.content;
        }

        @Override
        public BooleanObject toObject(final boolean value) {
            return new BooleanObject(value);
        }

        @Override
        public Class<BooleanObject> type() {
            return BooleanObject.class;
        }
    }

    public static class ByteObject {

        byte content;

        public ByteObject(final byte theContent) {
            content = theContent;
        }

        public String toString() {
            return new Byte(content).toString();
        }
    }

    public static final class BytePrimConverter implements
            ByteConverter<ByteObject> {
        @Override
        public int bits() {
            return 8;
        }

        @Override
        public byte fromObject(final ByteObject obj) {
            if (obj == null) {
                return 0;
            }
            return obj.content;
        }

        @Override
        public ByteObject toObject(final byte value) {
            return new ByteObject(value);
        }

        @Override
        public Class<ByteObject> type() {
            return ByteObject.class;
        }
    }

    public static class CharObject {

        char content;

        public CharObject(final char theContent) {
            content = theContent;
        }

        public String toString() {
            return new Character(content).toString();
        }
    }

    public static final class CharPrimConverter implements
            CharConverter<CharObject> {
        @Override
        public int bits() {
            return 16;
        }

        @Override
        public char fromObject(final CharObject obj) {
            if (obj == null) {
                return 0;
            }
            return obj.content;
        }

        @Override
        public CharObject toObject(final char value) {
            return new CharObject(value);
        }

        @Override
        public Class<CharObject> type() {
            return CharObject.class;
        }
    }

    public static class DoubleObject {

        double content;

        public DoubleObject(final double theContent) {
            content = theContent;
        }

        public String toString() {
            return new Double(content).toString();
        }
    }

    public static final class DoublePrimConverter implements
            DoubleConverter<DoubleObject> {
        @Override
        public int bits() {
            return 64;
        }

        @Override
        public double fromObject(final DoubleObject obj) {
            if (obj == null) {
                return 0d;
            }
            return obj.content;
        }

        @Override
        public DoubleObject toObject(final double value) {
            return new DoubleObject(value);
        }

        @Override
        public Class<DoubleObject> type() {
            return DoubleObject.class;
        }
    }

    public static class FloatObject {

        float content;

        public FloatObject(final float theContent) {
            content = theContent;
        }

        public String toString() {
            return new Float(content).toString();
        }
    }

    public static final class FloatPrimConverter implements
            FloatConverter<FloatObject> {
        @Override
        public int bits() {
            return 32;
        }

        @Override
        public float fromObject(final FloatObject obj) {
            if (obj == null) {
                return 0f;
            }
            return obj.content;
        }

        @Override
        public FloatObject toObject(final float value) {
            return new FloatObject(value);
        }

        @Override
        public Class<FloatObject> type() {
            return FloatObject.class;
        }
    }

    public static class IntObject {

        int content;

        public IntObject(final int theContent) {
            content = theContent;
        }

        public String toString() {
            return new Integer(content).toString();
        }
    }

    public static final class IntPrimConverter implements
            IntConverter<IntObject> {
        @Override
        public int bits() {
            return 32;
        }

        @Override
        public int fromObject(final IntObject obj) {
            if (obj == null) {
                return 0;
            }
            return obj.content;
        }

        @Override
        public IntObject toObject(final int value) {
            return new IntObject(value);
        }

        @Override
        public Class<IntObject> type() {
            return IntObject.class;
        }
    }

    public static class LongObject {

        long content;

        public LongObject(final long theContent) {
            content = theContent;
        }

        public String toString() {
            return new Long(content).toString();
        }
    }

    public static final class LongPrimConverter implements
            LongConverter<LongObject> {
        @Override
        public int bits() {
            return 64;
        }

        @Override
        public long fromObject(final LongObject obj) {
            if (obj == null) {
                return 0l;
            }
            return obj.content;
        }

        @Override
        public LongObject toObject(final long value) {
            return new LongObject(value);
        }

        @Override
        public Class<LongObject> type() {
            return LongObject.class;
        }
    }

    public static class ShortObject {

        short content;

        public ShortObject(final short theContent) {
            content = theContent;
        }

        public String toString() {
            return new Short(content).toString();
        }
    }

    public static final class ShortPrimConverter implements
            ShortConverter<ShortObject> {
        @Override
        public int bits() {
            return 16;
        }

        @Override
        public short fromObject(final ShortObject obj) {
            if (obj == null) {
                return 0;
            }
            return obj.content;
        }

        @Override
        public ShortObject toObject(final short value) {
            return new ShortObject(value);
        }

        @Override
        public Class<ShortObject> type() {
            return ShortObject.class;
        }
    }

    protected static final int _CAPACITY = 10;

    protected static final Random rand = new Random(System.currentTimeMillis());

    /** The registry. */
    public static ConverterRegistry registry;

    /** The factory. */
    protected CompiledStorage[] COMPILED;

    static {
        registry = new ConverterRegistry();
        registry.register(new BooleanPrimConverter(), BooleanObject.class);
        registry.register(new BytePrimConverter(), ByteObject.class);
        registry.register(new CharPrimConverter(), CharObject.class);
        registry.register(new DoublePrimConverter(), DoubleObject.class);
        registry.register(new FloatPrimConverter(), FloatObject.class);
        registry.register(new IntPrimConverter(), IntObject.class);
        registry.register(new LongPrimConverter(), LongObject.class);
        registry.register(new ShortPrimConverter(), ShortObject.class);

    }

    protected static boolean[] booleans() {

        final boolean[] booleans = new boolean[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            booleans[i] = rand.nextBoolean();
        }
        return booleans;
    }

    protected static byte[] bytes() {

        final byte[] bytes = new byte[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            bytes[i] = (byte) rand.nextInt();
        }
        return bytes;
    }

    protected static char[] chars() {

        final char[] chars = new char[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            chars[i] = (char) rand.nextInt();
        }
        return chars;
    }

    protected static double[] doubles() {

        final double[] doubles = new double[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            doubles[i] = rand.nextDouble();
        }
        return doubles;
    }

    protected static TestEnum[] enums() {

        final TestEnum[] allEnums = TestEnum.class.getEnumConstants();
        final TestEnum[] randomEnums = new TestEnum[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            randomEnums[i] = allEnums[Math
                    .abs(rand.nextInt() % allEnums.length)];
        }
        return randomEnums;
    }

    protected static float[] floats() {

        final float[] floats = new float[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            floats[i] = rand.nextFloat();
        }
        return floats;
    }

    protected static long[] int_longs() {

        final long[] int_longs = new long[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            int_longs[i] = Long.valueOf(Integer.toBinaryString(rand.nextInt()),
                    2);
        }
        return int_longs;
    }

    protected static int[] ints() {

        final int[] ints = new int[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            ints[i] = rand.nextInt();
        }
        return ints;
    }

    protected static long[] longs() {

        final long[] longs = new long[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            longs[i] = rand.nextLong();
        }
        return longs;
    }

    protected static short[] shorts() {

        final short[] shorts = new short[_CAPACITY];
        for (int i = 0; i < _CAPACITY; i++) {
            shorts[i] = (short) rand.nextInt();
        }
        return shorts;
    }

    protected static String[] strings() {

        final String[] strings = new String[_CAPACITY];
        final int[] ints = ints();
        for (int i = 0; i < _CAPACITY; i++) {
            strings[i] = "str" + ints[i];
        }
        return strings;
    }

    public static void checkSchema(final Struct compiledStructs) {
        final String jsonSchema = compiledStructs
                .schemaString(SchemaFormat.JSON);
        final Struct deserialized = Struct.fromSchemaString(jsonSchema,
                SchemaFormat.JSON);
        final String jsonSchema2 = deserialized.schemaString(SchemaFormat.JSON);
        // System.out.println("compiledStructs:\n" + compiledStructs);
        // System.out.println("deserialized:\n" + deserialized);
        assertEquals(compiledStructs, deserialized);
        assertEquals(jsonSchema, jsonSchema2);
        assertEquals(compiledStructs.signature(), deserialized.signature());
        assertTrue(compiledStructs.isCompatible(deserialized));
    }

    protected int checkBooleanChange(final BooleanField bField1, final int i,
            final List<Object> events, int changeCount) {

        final Object actual = events.get(changeCount);
        assertEquals(bField1.name() + i, actual);
        changeCount++;
        assertEquals("old: true", events.get(changeCount++));
        assertEquals("new: false", events.get(changeCount++));
        return changeCount;
    }

    protected int checkBooleanChange(final BooleanOptionalField bOptional,
            final int i, final List<Object> events, int changeCount) {

        final Object actual = events.get(changeCount);
        assertEquals(bOptional.name() + i, actual);
        changeCount++;
        assertEquals("old: true", events.get(changeCount++));
        assertEquals("new: false", events.get(changeCount++));
        return changeCount;
    }

    protected int checkByteChange(final ByteField byteField, final int i,
            final List<Object> events, int changeCount, final byte[] bytes) {

        final Object actual = events.get(changeCount);
        assertEquals(byteField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + bytes[i], events.get(changeCount++));
        assertEquals("new: 0", events.get(changeCount++));
        return changeCount;
    }

    protected int checkByteChange(final ByteOptionalField byteOptional, final int i,
            final List<Object> events, int changeCount, final byte[] bytes) {

        final Object actual = events.get(changeCount);
        assertEquals(byteOptional.name() + i, actual);
        changeCount++;
        assertEquals("old: " + bytes[i], events.get(changeCount++));
        assertEquals("new: " + (byte) 0, events.get(changeCount++));
        return changeCount;
    }

    protected int checkCharChange(final CharField charField, final int i,
            final List<Object> events, int changeCount, final char[] chars) {

        final Object actual = events.get(changeCount);
        assertEquals(charField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + chars[i], events.get(changeCount++));
        assertEquals("new: " + (char) 0, events.get(changeCount++));
        return changeCount;
    }

    protected int checkCharChange(final CharOptionalField cOptional, final int i,
            final List<Object> events, int changeCount, final char[] chars) {

        final Object actual = events.get(changeCount);
        assertEquals(cOptional.name() + i, actual);
        changeCount++;
        assertEquals("old: " + chars[i], events.get(changeCount++));
        assertEquals("new: " + (char) 0, events.get(changeCount++));
        return changeCount;
    }

    protected int checkDoubleChange(final DoubleField doubleField, final int i,
            final List<Object> events, int changeCount, final double[] doubles) {

        final Object actual = events.get(changeCount);
        assertEquals(doubleField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + doubles[i], events.get(changeCount++));
        assertEquals("new: " + 0d, events.get(changeCount++));
        return changeCount;
    }

    protected int checkDoubleChange(final DoubleOptionalField dOptional,
            final int i, final List<Object> events, int changeCount,
            final double[] doubles) {

        final Object actual = events.get(changeCount);
        assertEquals(dOptional.name() + i, actual);
        changeCount++;
        assertEquals("old: " + doubles[i], events.get(changeCount++));
        assertEquals("new: " + 0d, events.get(changeCount++));
        return changeCount;
    }

    protected int checkFloatChange(final FloatField floatField, final int i,
            final List<Object> events, int changeCount, final float[] floats) {

        final Object actual = events.get(changeCount);
        assertEquals(floatField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + floats[i], events.get(changeCount++));
        assertEquals("new: " + (float) 0, events.get(changeCount++));
        return changeCount;
    }

    protected int checkFloatChange(final FloatOptionalField fOptional, final int i,
            final List<Object> events, int changeCount, final double[] floats) {

        final Object actual = events.get(changeCount);
        assertEquals(fOptional.name() + i, actual);
        changeCount++;
        assertEquals("old: " + floats[i], events.get(changeCount++));
        assertEquals("new: " + 0f, events.get(changeCount++));
        return changeCount;
    }

    protected int checkIntChange(final IntField newFullIntField, final int i,
            final List<Object> events, int changeCount, final int[] ints) {

        final Object actual = events.get(changeCount);
        assertEquals(newFullIntField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + ints[i], events.get(changeCount++));
        assertEquals("new: " + 0, events.get(changeCount++));
        return changeCount;
    }

    protected int checkLongChange(final LongField LongField, final int i,
            final List<Object> events, int changeCount, final long[] longs) {

        final Object actual = events.get(changeCount);
        assertEquals(LongField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + longs[i], events.get(changeCount++));
        assertEquals("new: " + 0l, events.get(changeCount++));
        return changeCount;
    }

    protected int checkLongChange(final LongOptionalField lOptional, final int i,
            final List<Object> events, int changeCount, final long[] longs) {

        final Object actual = events.get(changeCount);
        assertEquals(lOptional.name() + i, actual);
        changeCount++;
        assertEquals("old: " + longs[i], events.get(changeCount++));
        assertEquals("new: " + 0l, events.get(changeCount++));
        return changeCount;
    }

    protected int checkShortChange(final ShortField shortField, final int i,
            final List<Object> events, int changeCount, final short[] shorts) {

        final Object actual = events.get(changeCount);
        assertEquals(shortField.name() + i, actual);
        changeCount++;
        assertEquals("old: " + shorts[i], events.get(changeCount++));
        assertEquals("new: " + 0, events.get(changeCount++));
        return changeCount;
    }

    protected int checkShortChange(final ShortOptionalField sOptional, final int i,
            final List<Object> events, int changeCount, final short[] shorts) {

        final Object actual = events.get(changeCount++);
        assertEquals(sOptional.name() + i, actual);
        assertEquals("old: " + shorts[i], events.get(changeCount++));
        assertEquals("new: " + (short) 0, events.get(changeCount++));
        return changeCount;
    }

}