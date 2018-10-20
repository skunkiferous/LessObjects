/*******************************************************************************
 * Copyright 2013 Sebastien Diot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 *
 */
package com.blockwithme.lessobjects.proxy;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.prim.BooleanConverter;
import com.blockwithme.prim.ByteConverter;
import com.blockwithme.prim.CharConverter;
import com.blockwithme.prim.DoubleConverter;
import com.blockwithme.prim.FloatConverter;
import com.blockwithme.prim.IntConverter;
import com.blockwithme.prim.LongConverter;
import com.blockwithme.prim.ShortConverter;

/**
 * An internal class used by the generated code to read/write array
 * fields.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ArrayUtil {

    /** Read boolean array. */
    public static Object readArrayBoolean(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final boolean[] results = new boolean[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final BooleanField<?, ?> theField = (BooleanField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read byte array. */
    public static Object readArrayByte(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final byte[] results = new byte[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final ByteField<?, ?> theField = (ByteField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read char array. */
    public static Object readArrayChar(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final char[] results = new char[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final CharField<?, ?> theField = (CharField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read double array. */
    public static Object readArrayDouble(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final double[] results = new double[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final DoubleField<?, ?> theField = (DoubleField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read float array. */
    public static Object readArrayFloat(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final float[] results = new float[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final FloatField<?, ?> theField = (FloatField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read int array. */
    public static Object readArrayInt(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final int[] results = new int[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final IntField<?, ?> theField = (IntField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read long array. */
    public static Object readArrayLong(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {

        final long[] results = new long[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final LongField<?, ?> theField = (LongField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read String array. */
    @SuppressWarnings({ "unchecked", "null" })
    public static Object readArrayObject(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final Class<?> type = theFieldArray[0].type();
        final Object[] results = (Object[]) java.lang.reflect.Array
                .newInstance(type, theFieldArray.length);
        for (int i = 0; i < theFieldArray.length; i++) {
            final ObjectField<?, ?> theField = (ObjectField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read short array. */
    public static Object readArrayShort(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final short[] results = new short[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final ShortField<?, ?> theField = (ShortField<?, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Read String array. */
    @SuppressWarnings("unchecked")
    public static Object readArrayString(final Storage theStorage,
            final Field<?, ?>[] theFieldArray) {
        final String[] results = new String[theFieldArray.length];
        for (int i = 0; i < theFieldArray.length; i++) {
            final ObjectField<String, ?> theField = (ObjectField<String, ?>) theFieldArray[i];
            if (theField != null) {
                results[i] = theStorage.read(theField);
            }
        }
        return results;
    }

    /** Write boolean array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final boolean[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final BooleanField<?, ?> theField = (BooleanField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write byte array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final byte[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final ByteField<?, ?> theField = (ByteField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write char array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final char[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final CharField<?, ?> theField = (CharField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write double array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final double[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final DoubleField<?, ?> theField = (DoubleField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write float array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final float[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final FloatField<?, ?> theField = (FloatField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write int array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final int[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final IntField<?, ?> theField = (IntField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write long array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final long[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final LongField<?, ?> theField = (LongField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write short array. */
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final short[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final ShortField<?, ?> theField = (ShortField<?, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write String array. */
    @SuppressWarnings("unchecked")
    public static void writeArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final String[] theValues) {
        for (int i = 0; i < theFieldArray.length; i++) {
            final ObjectField<String, ?> theField = (ObjectField<String, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write boolean object array. */
    @SuppressWarnings("unchecked")
    public static void writeBooleanToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final BooleanField<?, ?> theField = (BooleanField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final BooleanConverter<Object> converter = (BooleanConverter<Object>) theField
                        .converter();
                final boolean bval = converter.fromObject(theValues[i]);
                theStorage.write(theField, bval);
            }
        }
    }

    /** Write byte object array */
    @SuppressWarnings("unchecked")
    public static void writeByteToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final ByteField<?, ?> theField = (ByteField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final ByteConverter<Object> converter = (ByteConverter<Object>) theField
                        .converter();
                final byte bval = converter.fromObject(theValues[i]);
                theStorage.write(theField, bval);
            }
        }
    }

    /** Write char object array */
    @SuppressWarnings("unchecked")
    public static void writeCharToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final CharField<?, ?> theField = (CharField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final CharConverter<Object> converter = (CharConverter<Object>) theField
                        .converter();
                final char cval = converter.fromObject(theValues[i]);
                theStorage.write(theField, cval);
            }
        }
    }

    /** Write double object array */
    @SuppressWarnings("unchecked")
    public static void writeDoubleToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final DoubleField<?, ?> theField = (DoubleField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final DoubleConverter<Object> converter = (DoubleConverter<Object>) theField
                        .converter();
                final double dval = converter.fromObject(theValues[i]);
                theStorage.write(theField, dval);
            }
        }
    }

    /** Write float object array */
    @SuppressWarnings("unchecked")
    public static void writeFloatToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final FloatField<?, ?> theField = (FloatField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final FloatConverter<Object> converter = (FloatConverter<Object>) theField
                        .converter();
                final float fval = converter.fromObject(theValues[i]);
                theStorage.write(theField, fval);
            }
        }
    }

    /** Write int object array */
    @SuppressWarnings("unchecked")
    public static void writeIntToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final IntField<?, ?> theField = (IntField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final IntConverter<Object> converter = (IntConverter<Object>) theField
                        .converter();
                final int ival = converter.fromObject(theValues[i]);
                theStorage.write(theField, ival);
            }
        }
    }

    /** Write long object array */
    @SuppressWarnings("unchecked")
    public static void writeLongToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final LongField<?, ?> theField = (LongField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final LongConverter<Object> converter = (LongConverter<Object>) theField
                        .converter();
                final long lval = converter.fromObject(theValues[i]);
                theStorage.write(theField, lval);
            }
        }
    }

    /** Write object array. */
    @SuppressWarnings("unchecked")
    public static void writeObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final ObjectField<Object, ?> theField = (ObjectField<Object, ?>) theFieldArray[i];
            if (theField != null) {
                theStorage.write(theField, theValues[i]);
            }
        }
    }

    /** Write short object array */
    @SuppressWarnings("unchecked")
    public static void writeShortToObjectArray(final Storage theStorage,
            final Field<?, ?>[] theFieldArray, final Object[] theValues) {

        for (int i = 0; i < theFieldArray.length; i++) {
            final ShortField<?, ?> theField = (ShortField<?, ?>) theFieldArray[i];
            if (theField != null) {
                final ShortConverter<Object> converter = (ShortConverter<Object>) theField
                        .converter();
                final short sval = converter.fromObject(theValues[i]);
                theStorage.write(theField, sval);
            }
        }
    }

}
