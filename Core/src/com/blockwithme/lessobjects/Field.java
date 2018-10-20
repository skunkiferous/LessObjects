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
package com.blockwithme.lessobjects;

import static com.blockwithme.lessobjects.FieldCategory.GLOBAL;
import static com.blockwithme.lessobjects.FieldCategory.NORMAL;
import static com.blockwithme.lessobjects.FieldCategory.OPTIONAL;
import static com.blockwithme.lessobjects.FieldCategory.VIRTUAL;
import static com.blockwithme.lessobjects.util.StructConstants.LONG_BITS;
import static com.blockwithme.lessobjects.util.Util.intsToLong;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.beans.ChildProperties;
import com.blockwithme.lessobjects.beans.FieldProperties;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl.StorageBuffer;
import com.blockwithme.lessobjects.util.StructConstants;
import com.blockwithme.murmur.MurmurHash;
import com.blockwithme.prim.Converter;
import com.carrotsearch.hppc.LongObjectOpenHashMap;

/**
 * Represents a field in a structure/union.
 *
 * @param <E> the element type
 * @param <F> the generic type
 *
 * @author monster
 */
@ParametersAreNonnullByDefault
public abstract class Field<E, F extends Field<E, F>> extends Child {

    /** The compiled. */
    private final boolean compiled;

    /** The storage field (true for a struct or global field) */
    private final boolean storageField;

    /** Is virtual ? */
    private final boolean virtual;

    /** Indicates if this field is an object field */
    protected final boolean object;

    /** Is it a meta-data field ? (For example, union discriminator) */
    private final boolean metadata;

    /** The field index (first, second, ...) in that structure. */
    private final int index;

    /** The mask for this field. Only valid within a 64 bit-aligned long. */
    private final long mask64;

    /** The negated mask64 */
    private final long negMask64;

    /** The bit offset into that structure. */
    private final int offset;

    /** The bit offset into that structure, divided by 64. */
    private final int offsetDiv64;

    /** The bit offset into that structure, modulo 64. */
    private final int offsetMod64;

    /** String representation*/
    @Nonnull
    private final String toString;

    /** The type enum. */
    @Nonnull
    private final FieldType typeEnum;

    /** The listener Index of this field */
    private final int uniqueIndex;

    /** The hash key. */
    protected final int hashKey;

    /** The field properties. */
    @Nonnull
    protected final FieldProperties properties;

    /** The object converter. */
    @Nonnull
    protected final Converter<E> converter;

    /** A private field added for json binding purposes. */
    // TODO Simple class name are not good enough with OSGi
    protected final String converterClassName;

    /** HACK! We keep a temporary reference to the original field, when
     * compiling. */
    // TODO Can we somehow do without?
    @SuppressWarnings("rawtypes")
    @Nullable
    private Field original;

    /** Computes a bitmask */
    protected static long mask(final int theBitsPerValue) {
        return -1L >>> StructConstants.LONG_BITS - theBitsPerValue;
    }

    /** Validates the properties, and returns the field properties. */
    protected static FieldProperties validate(
            final FieldProperties theProperties,
            final Class<?> theConverterClass, final int theMaxBits) {

        final int bits = theProperties.bits();
        if (bits > theMaxBits) {
            throw new IllegalArgumentException("bits(" + bits + ") > maxBits("
                    + theMaxBits + ") field name: " + theProperties.name());
        }
        final Converter<?> converter = theProperties.converter();
        if (!theConverterClass.isInstance(converter)) {
            throw new IllegalArgumentException("converter("
                    + converter.getClass() + ") must be " + theConverterClass);
        }
        return theProperties;
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName) {
        this(theConverter, theName, theConverter.bits(), false, false, false);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final boolean isObject) {
        this(theConverter, theName, theConverter.bits(), false, false, false,
                isObject);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final boolean isGlobal, final boolean isOptional,
            final boolean isVirtual) {
        this(theConverter, theName, theConverter.bits(), isGlobal, isOptional,
                isVirtual);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final boolean isGlobal, final boolean isOptional,
            final boolean isVirtual, final boolean isObject) {
        this(theConverter, theName, theConverter.bits(), isGlobal, isOptional,
                isVirtual, isObject);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final int theBits) {
        this(theConverter, theName, theBits, false, false, false);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final int theBits, final boolean isObject) {
        this(theConverter, theName, theBits, false, false, false, isObject);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual) {
        this(theConverter, theName, theBits, isGlobal, isOptional, isVirtual,
                false);
    }

    /** Constructor. */
    protected Field(final Converter<E> theConverter, final String theName,
            final int theBits, final boolean isGlobal,
            final boolean isOptional, final boolean isVirtual,
            final boolean isObject) {

        this((FieldProperties) new FieldProperties(theName, theBits)
                .setObject(isObject).setVirtual(isVirtual)
                .setConverter(theConverter).setOptional(isOptional)
                .setGlobal(isGlobal));
    }

    /** Constructor. */
    @SuppressWarnings({ "unchecked", "null" })
    protected Field(final FieldProperties theProperties) {
        super(theProperties, true);
        checkNotNull(theProperties);
        if (!theProperties.compiled()) {
            original = theProperties.original();
        } else {
            original = null;
        }
        properties = theProperties;
        metadata = theProperties.metadata();
        converter = (Converter<E>) checkNotNull(theProperties.converter(),
                "converter of field must be specified");
        converterClassName = converter.getClass().getName();
        final Class<E> type = checkNotNull(converter.type(),
                "Type of field must be specified");
        index = theProperties.index();
        offset = theProperties.offset();
        offsetDiv64 = offset / LONG_BITS;
        offsetMod64 = offset % LONG_BITS;
        mask64 = mask(bits()) << offsetMod64;
        negMask64 = ~mask64;
        String typeName = type.getName();
        virtual = properties.virtual();
        uniqueIndex = theProperties.uniqueIndex();
        typeEnum = FieldType.getType(type);
        object = theProperties.object();
        checkState(object == this instanceof ObjectField,
                "An ObjectField must have it's object flag true: " + name());
        compiled = theProperties.compiled();

        if (typeName.startsWith("java.lang.")) {
            typeName = typeName.substring("java.lang.".length());
        }
        storageField = !optional && !object && !virtual;
        if (storageField) {
            final int bits = bits();
            final int maxBits = maxBits();
            checkState(bits <= maxBits,
                    "The field size must be not be more than : " + maxBits
                            + " for field " + name() + " But was: " + bits);
        }

        checkState(!(virtual && isOptional()),
                "A field cannot be optional and virtual both at the same time.");
        String typeStr = global ? "global" : isOptional() ? "optional"
                : virtual ? "virtual" : "normal";

        if (object) {
            typeStr += "[object]";
        }
        final String position = storageField ? " #" + index + " @" + offset
                : "";
        toString = typeName + " " + name() + position + "[uniqueIndex:"
                + uniqueIndex + " localIndex:" + localIndex() + ", type:"
                + typeStr + "] qualifiedName: " + qualifiedName();

        hashKey = MurmurHash.hash32(toString);
    }

    /** Creates a new copy, with the given parameters. */
    protected abstract F copyInternal(final FieldProperties theProperties);

    /** The maximum size of the field in bits. */
    protected abstract int maxBits();

    // This method is added for json string generation.
    @Override
    public int bits() {
        return super.bits();
    }

    /** Creates a new copy, with the given parameters. */
    public abstract void clear(final Storage theStorage);

    /** Clear the field value from storage wrapper's buffer. */
    public abstract void clear(Storage theStorage, final StorageBuffer theBuffer);

    /** Compiled? */
    public final boolean compiled() {
        return !storageField || compiled;
    }

    /** Checks if the Buffer contains value for this field at the selected index. */
    public boolean contains(final StorageBuffer theBuffer,
            final int theSelectedIndex) {
        final LongObjectOpenHashMap<?> map = theBuffer.getObjectBuffer();
        final long key = intsToLong(theSelectedIndex, uniqueIndex());
        return map.containsKey(key);
    }

    /** Returns the converter. */
    @Nullable
    public Converter<E> converter() {
        return converter;
    }

    /** Returns the converter class name if the field has a value converter */
    @SuppressWarnings("static-method")
    @Nullable
    public String converterClassName() {
        return converterClassName;
    }

    /** Creates a new copy, with the given parameters. */
    @SuppressWarnings("null")
    public F copy(final ChildProperties theChildProperties) {
        if (theChildProperties instanceof FieldProperties) {
            return copyInternal((FieldProperties) theChildProperties);
        }
        return copyInternal(properties.setChildFields(theChildProperties));
    }

    /** Sets a new name and returns a modified copy of this field
     *
     * @return the modified Field */
    @SuppressWarnings("null")
    public F copy(final Converter<?> theConverter) {
        return copy(properties.setConverter(theConverter));
    }

    /** Creates a new copy, with the given parameters. */
    public F copy(final FieldProperties theProperties) {
        return copyInternal(theProperties);
    }

    /** Sets a new name and returns a modified copy of this field
     *
     * @param theName the new name
     * @return the modified Field */
    @SuppressWarnings("null")
    public F copy(final String theName) {
        return copy((FieldProperties) properties.setName(theName));
    }

    /** Sets new values for name, index, and offset and returns the modified copy
     * of this field, this is method used internally.
     *
     * @param theName the name
     * @param theIndex the field index
     * @param theOffset the offset
     * @return the f */
    public F copy(final String theName, final int theIndex, final int theOffset) {
        return copy((FieldProperties) properties.setOffset(theOffset)
                .setIndex(theIndex).setName(theName));
    }

    /** Generic Copy method, available in all field types. */
    public abstract void copyValue(final Storage theSource,
            final Field<?, ?> theDestinationField, final Storage theDestination);

    @SuppressWarnings({ "null", "unused" })
    @Override
    public boolean equals(final Object theOther) {
        if (this == theOther) {
            return true;
        }
        if (theOther == null) {
            return false;
        }
        if (getClass() != theOther.getClass()) {
            return false;
        }
        final Field<?, ?> other = (Field<?, ?>) theOther;
        if (toString.equals(other.toString)) {
            return false;
        }
        return true;
    }

    /**
     * Field category,
     *
     * If a field is not GLOBAL, VIRTUAL or object then its a NORMAL field
     * If a field is global 'GLOBAL' is returned.
     * If a field is virtual 'VIRTUAL' is returned.
     * If a field is optional 'OPTIONAL'
     * If a field is global GLOBAL is returned.
     *
     * @return the field category
     */
    @SuppressWarnings("null")
    public FieldCategory fieldCategory() {
        if (global) {
            return GLOBAL;
        }
        if (virtual) {
            return VIRTUAL;
        }
        if (optional) {
            return OPTIONAL;
        }
        return NORMAL;
    }

    /** Returns the Field type */
    @SuppressWarnings("null")
    public FieldType fieldType() {
        return FieldType.from(type());
    }

    /** @return the fullBits flag */
    public boolean fullBits() {
        return super.bits() == maxBits();
    }

    /** @return the global flag */
    @Override
    public boolean global() {
        return global;
    }

    @Override
    public int hashCode() {
        return hashKey;
    }

    /** @return the index */
    public int index() {
        return index;
    }

    /** Returns the mapper class name if the field has a value mapper */
    @SuppressWarnings("static-method")
    @Nullable
    public String mapperClassName() {
        // return null by default.
        return null;
    }

    /** @return the mask64 */
    public long mask64() {
        return mask64;
    }

    /**
     * Is it a meta-data field ? union discriminator
     * (fieldIndex) is a meta-data field.
     */
    public boolean metadata() {
        return metadata;
    }

    /** @return the negMask64 */
    public long negMask64() {
        return negMask64;
    }

    /** Is Object ? */
    public boolean object() {
        return object;
    }

    /**
     * The field offset value (relative start position),
     * this value is set by the compiler.
     */
    public int offset() {
        return offset;
    }

    /**
     * The field offset value divided by 64,
     * this value is set by the compiler.
     */
    public int offsetDiv64() {
        return offsetDiv64;
    }

    /**
     * Field offset modulus by 64,
     * this value is set by the compiler.
     */
    public int offsetMod64() {
        return offsetMod64;
    }

    /**
     * Original field reference, returns null.
     * this value is used by the compiler and later its set to null.
     */
    @Nullable
    public Field<?, ?> original() {
        return original;
    }

    /** Field level properties of this field. */
    @SuppressWarnings("null")
    public final FieldProperties properties() {
        return properties;
    }

    /** Generic read method, available in all field types. */
    @Nullable
    public abstract E readAny(final Storage theStorage);

    /** Root Struct Object for this Field used by check access method. */
    @SuppressWarnings("null")
    @Override
    public Struct root() {
        // added a duplicate methods since the method in field
        // class does not return null.
        return root;
    }

    /** Returns true if its a normal struct field or global field. */
    public boolean storageField() {
        return storageField;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public final String toString() {
        return toString;
    }

    /** The field type represented as a class */
    @SuppressWarnings("null")
    public Class<E> type() {
        return converter.type();
    }

    /** The field type represented as a Enum. */
    @SuppressWarnings("null")
    public FieldType typeEnum() {
        return typeEnum;
    }

    /**  The field unique index. */
    public int uniqueIndex() {
        return uniqueIndex;
    }

    /** Is it a virtual field ? */
    public boolean virtual() {
        return virtual;
    }

    /** Generic Write method, available in all field types. */
    public abstract void writeAny(final E theValue, final Storage theStorage);

    /** Converts a long value to the corresponding primitive value and Writes
     * the field value to the actual storage. */
    public abstract void writeAnyLong(long theValue, final Storage theStorage);

}
