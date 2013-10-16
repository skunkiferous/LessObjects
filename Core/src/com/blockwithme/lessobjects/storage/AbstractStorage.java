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
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.storage.ChangeType.BOOLEAN_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.BYTE_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.CHAR_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.DOUBLE_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.FLOAT_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.INT_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.LONG_OPTIONAL;
import static com.blockwithme.lessobjects.storage.ChangeType.SHORT_OPTIONAL;
import static com.blockwithme.lessobjects.util.StructConstants.COLL_FACTORY;
import static com.blockwithme.lessobjects.util.StructConstants.MAX_STORAGE_CAPACITY;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.Struct.UnionDiscriminatorValueMapping;
import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.beans.MultiDimensionalSupport;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
import com.blockwithme.lessobjects.fields.optional.FloatOptionalField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
import com.blockwithme.lessobjects.fields.optional.LongOptionalField;
import com.blockwithme.lessobjects.fields.optional.OptionalField;
import com.blockwithme.lessobjects.fields.optional.ShortOptionalField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.multidim.Arity;
import com.blockwithme.lessobjects.multidim.MultiDimensionSupportImpl;
import com.blockwithme.lessobjects.multidim.Point;
import com.blockwithme.lessobjects.storage.collections.IntByteMap;
import com.blockwithme.lessobjects.storage.collections.IntCharMap;
import com.blockwithme.lessobjects.storage.collections.IntDoubleMap;
import com.blockwithme.lessobjects.storage.collections.IntFloatMap;
import com.blockwithme.lessobjects.storage.collections.IntIntMap;
import com.blockwithme.lessobjects.storage.collections.IntLongMap;
import com.blockwithme.lessobjects.storage.collections.IntObjectMap;
import com.blockwithme.lessobjects.storage.collections.IntSet;
import com.blockwithme.lessobjects.storage.collections.IntShortMap;
import com.carrotsearch.hppc.IntOpenHashSet;

//CHECKSTYLE.OFF: IllegalType
/**
 * The Abstract Storage class, implements methods common to all storage types
 *
 * @see Storage
 *
 * @author monster tarung
 */
@ParametersAreNonnullByDefault
public abstract class AbstractStorage implements Storage {

    /** The empty object array. */
    public static final Object[] EMPTY_FIELDS = new Object[0];

    /** The change struct constant. */
    @SuppressWarnings("null")
    public static final ChangeStruct STRUCT = new ChangeStruct(
            new Aligned64Compiler());

    /** The change object to pass the changed values to change publisher */
    @Nonnull
    private final ChangeInfo change;

    /** The multi dimensional support gets created lazily. */
    private MultiDimensionalSupport mdSupport;

    /**
     * The transaction manager.
     *
     * Transactions manager will not be serialized, this object is created lazily.
     * We should throw exception if storage is serialized while there is some uncommitted data.
     */
    @Nonnull
    private transient TransactionManagerImpl tm;

    /** The arity. */
    protected final Arity arity;

    /** The base storage. If no base storage used, then equals this. */
    @Nonnull
    protected final Storage baseStorage;

    /** The capacity of this storage. */
    protected int capacity;

    /** The global fields storage. */
    @Nullable
    protected final AbstractStorage globalFieldsStorage;

    /** The is global storage. */
    protected final boolean isGlobalStorage;

    /** The secondary flag. */
    protected boolean isSecondary;

    /** The change listener support. */
    @Nonnull
    protected transient ChangeListenerSupportImpl listenerSupport;

    /**
     * The storage objects for object fields.
     * ObjectStorageObjects are manually serialized/de-serialized.
     */
    @Nonnull
    protected transient Object[] objectStorageObjects;

    /**
     * The storage objects for optional fields.
     * OptionalStorageObjects contain HPPC collections
     * which do not implement Serializable interface, these objects are serialized/de-serialized
     * manually
     */
    @Nonnull
    protected transient Object[] optionalStorageObjects;

    /** Indicates storage in writable state. */
    protected boolean readOnly;

    /** The current struct which is backed by this storage, (transient) */
    @Nonnull
    protected transient Struct struct;

    /** The selected structure. */
    protected int structure;

    /** The disable transactions flag */
    protected boolean transactionsDisabled;

    /** Validates storage capacity */
    private static void checkCapacity(final int theCapacity) {
        checkArgument(theCapacity > 0, "Capacity cannot be negative or zero: "
                + theCapacity);
        checkArgument(theCapacity <= MAX_STORAGE_CAPACITY,
                "Maximum capacity exceeded: " + theCapacity);
    }

    /**
     * Check if the field belongs to a parent which is of a list type.
     *
     * @return returns the list type parent if its a list field else returns null.
     */
    @Nullable
    private static Struct checkIsListField(final Field<?, ?> theField,
            final Struct theCurrentStruct) {

        Struct s = theField.parent();
        while (s != null) {
            if (s == theCurrentStruct) {
                return null;
            }
            if (s.list()) {
                return s;
            }
            s = s.parent();
        }
        return null;
    }

    /**
     * If the access of this field of this Struct in this storage is invalid,
     * returns an error message.
     * */
    @SuppressWarnings("null")
    @Nullable
    static String doAccessCheck(final Field<?, ?> theField,
            final Struct theStruct, final Storage theStorage) {

        checkNotNull(theField);
        if (!theField.compiled()) {
            return "The field " + theField.name()
                    + " is not a compiled field. ";
        }
        if (!theStruct.equals(theField.root())) {
            return "The field " + theField.name()
                    + " does not belong to the struct which is backed "
                    + "by the current storage!";
        }

        final UnionDiscriminatorValueMapping[] udMap = theStruct
                .uDMapping(theField);
        for (final UnionDiscriminatorValueMapping udField : udMap) {
            if (udField.value() != theStorage.read(udField.field())) {
                return "Cannot read/write " + theField.name()
                        + " Union position is not set appropriately.";
            }
        }
        return null;
    }

    /** Instantiates a new abstract storage using a builder object */
    @SuppressWarnings("null")
    protected AbstractStorage(final StorageBuilder theBuilder) {

        checkCapacity(theBuilder.getCapacity());

        arity = theBuilder.getArity();
        capacity = theBuilder.getCapacity();
        globalFieldsStorage = theBuilder.getGlobalFieldsStorage();
        isGlobalStorage = theBuilder.isGlobalStorage();
        isSecondary = theBuilder.isSecondary();
        objectStorageObjects = theBuilder.getObjectStorageObjects();
        optionalStorageObjects = theBuilder.getOptionalStorageObjects();
        readOnly = theBuilder.isReadOnly();
        struct = theBuilder.getStruct();
        structure = theBuilder.getStructure();
        transactionsDisabled = theBuilder.isTransactionsDisabled();
        change = new ChangeInfo();

        if (theBuilder.getBaseStorage() == null) {
            listenerSupport = new ChangeListenerSupportImpl(struct, this);
            baseStorage = this;
        } else {
            baseStorage = theBuilder.getBaseStorage();
            listenerSupport = (ChangeListenerSupportImpl) baseStorage
                    .changeListenerSupport();
        }
    }

    /**
     * Instantiates a new abstract storage.
     *
     * @param theStruct the struct
     * @param theBaseStorage the base storage - pass null if no parent storage.
     * @param theCapacity the capacity
     * @param theTransactionalFlag if the transactional flag switched off the storage will *not*
     * record any change history, roll-backs cannot be performed and change Field Change listeners
     * will not be invoked.
     */
    @SuppressWarnings("null")
    protected AbstractStorage(final Struct theStruct,
            @Nullable final Storage theBaseStorage, final int theCapacity,
            final boolean theTransactionalFlag, final Arity theArity) {

        checkCapacity(theCapacity);

        struct = theStruct;
        capacity = theCapacity;
        transactionsDisabled = !theTransactionalFlag;
        isSecondary = theBaseStorage != null;
        change = new ChangeInfo();
        isGlobalStorage = struct.global();
        arity = theArity;

        if (theBaseStorage == null) {
            listenerSupport = new ChangeListenerSupportImpl(theStruct, this);
            baseStorage = this;
        } else {
            listenerSupport = (ChangeListenerSupportImpl) theBaseStorage
                    .changeListenerSupport();
            baseStorage = theBaseStorage;
            // Add reference to this tm inside parent tm
        }

        if (theStruct.optionalFieldsCount() > 0) {
            optionalStorageObjects = new Object[theStruct.optionalFieldsCount()];
            initOptionalStorage(theStruct, optionalStorageObjects, capacity);

        } else {
            optionalStorageObjects = EMPTY_FIELDS;
        }
        final int objectCount = theStruct.objectCount();
        if (objectCount > 0) {
            objectStorageObjects = new Object[objectCount];
            initObjectStorage(theStruct);
        } else {
            objectStorageObjects = EMPTY_FIELDS;
        }
        final Struct globalStruct = theStruct.globalStruct();
        if (globalStruct != null) {
            globalFieldsStorage = getSingleStorage(globalStruct);
        } else {
            globalFieldsStorage = null;
        }
    }

    /** Returns the action set. */
    @SuppressWarnings("null")
    private ActionSetImpl actionSetImpl() {
        return (ActionSetImpl) ((TransactionManagerImpl) transactionManager())
                .actionSet();
    }

    /** Actually calls listeners. */
    @SuppressWarnings("null")
    private void callListeners() {

        final boolean oldReadOnly = readOnly;
        readOnly = true;
        try {
            listenerSupport.fireValueChange(change);
        } catch (final Exception exp) {
            // to rollback the transaction the storage should be writable.
            readOnly = false;
            transactionManager().rollback();
            throw new IllegalStateException(
                    "Error occurred while invoking change listeners,"
                            + " the TRANSACTION WAS ROLLED BACK!", exp);
        } finally {
            readOnly = oldReadOnly;
        }
    }

    /** Validates the usage of a union descriptor */
    @SuppressWarnings("null")
    private boolean checkUnionDescriptor(final Field<?, ?> theChild,
            final Struct theStruct) {

        final UnionDiscriminatorValueMapping[] udMap = theStruct
                .uDMapping(theChild);
        for (final UnionDiscriminatorValueMapping udField : udMap) {
            if (udField.value() != read(udField.field())) {
                return false;
            }
        }
        return true;
    }

    /** Copies the value of one field from another storage into this storage, for the selected structure. */
    private void copyField(final Storage theOther,
            final Map<Field<?, ?>, Field<?, ?>> theFieldMapping,
            final Field<?, ?> theNewField) {
        final Field<?, ?> oldField = theFieldMapping.get(theNewField);
        if (oldField != null) {
            oldField.copyValue(this, theNewField, theOther);
        }
    }

    /** @return the change object */
    @SuppressWarnings("null")
    ChangeInfo change() {
        return change;
    }

    void enableTransactionsInternal(final boolean theEnableFlag) {
        transactionsDisabled = !theEnableFlag; // NOPMD
    }

    /** Initializes the optional storage. */
    final void initOptionalStorage(final Struct theStruct,
            final Object[] theStorage, final int theCapacity) {

        if (theStruct.structOptionalFields().length > 0) {
            for (final Field<?, ?> f : theStruct.structOptionalFields()) {
                final Class<?> oType = f.type();
                final int oIndex = ((OptionalField<?, ?>) f)
                        .optionalFieldIndex();
                if (oType.equals(Boolean.class)) {
                    theStorage[oIndex] = COLL_FACTORY.createIntSet(theCapacity);
                } else if (oType.equals(Byte.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntByteMap(theCapacity);
                } else if (oType.equals(Character.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntCharMap(theCapacity);
                } else if (oType.equals(Double.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntDoubleMap(theCapacity);
                } else if (oType.equals(Float.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntFloatMap(theCapacity);
                } else if (oType.equals(Integer.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntIntMap(theCapacity);
                } else if (oType.equals(Long.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntLongMap(theCapacity);
                } else if (oType.equals(Short.class)) {
                    theStorage[oIndex] = COLL_FACTORY
                            .createIntShortMap(theCapacity);
                }
            }
        }
        if (theStruct.structChildren().length > 0) {
            for (final Struct child : theStruct.structChildren()) {
                if (child != null) {
                    initOptionalStorage(child, theStorage, capacity);
                }
            }
        }
    }

    /** Read bytes. */
    @SuppressWarnings({ "null", "unchecked" })
    <E, F extends ObjectField<E, F>> E readObject(final F theField) {

        final int objectFieldIndex = theField.properties().objectFieldIndex();
        if (theField.properties().isOptional()) {
            final IntObjectMap<Object> map = COLL_FACTORY.createIntObjectMap(
                    capacity, Object.class);
            return (E) map.get(structure);
        }
        final Object[] array = (Object[]) objectStorageObjects[objectFieldIndex];
        return (E) array[structure];
    }
    /** Blank copy without any data and defaults *not* initialized.  */
    protected abstract Storage blankCopy();

    /** Clears all children at current position and sets default values. */
    protected abstract void clearAllChildren();

    /** Clears all the field at current positions and sets corresponding default
     * values. */
    @SuppressWarnings("null")
    protected void clearAllFields() {
        for (final Field<?, ?> field : struct.allStorageFields()) {
            // checks if union discriminator position is set appropriately
            // before clearing the field.
            if (checkUnionDescriptor(field, struct)) {
                field.clear(this);
            }
        }
    }

    /** Called at the time of resizing the storage, this method resizes and
     * initializes the storage arrays for object field types.
     *
     * @param theStruct the struct
     * @param theOldObjectStorage the old object storage
     * @param theNewObjectStorage the new object storage */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void copyObjectStorage(final Struct theStruct,
            final Object[] theOldObjectStorage,
            final Object[] theNewObjectStorage) {
        if (theStruct.structObjectFields().length > 0) {
            for (final ObjectField<?, ?> f : theStruct.structObjectFields()) {
                if (!f.properties().isOptional()) {
                    final Object[] oldArray = (Object[]) theOldObjectStorage[f
                            .properties().objectFieldIndex()];
                    final int objectFieldIndex = f.properties()
                            .objectFieldIndex();
                    final Object[] newArray = (Object[]) theNewObjectStorage[objectFieldIndex];
                    final int length = Math.min(oldArray.length,
                            newArray.length);
                    System.arraycopy(oldArray, 0, newArray, 0, length);
                } else {
                    final IntObjectMap oldMap = (IntObjectMap) theOldObjectStorage[f
                            .properties().objectFieldIndex()];
                    final IntObjectMap newMap = (IntObjectMap) theNewObjectStorage[f
                            .properties().objectFieldIndex()];
                    newMap.putAll(oldMap);
                }
            }
        }
        if (theStruct.structChildren().length > 0) {
            for (final Struct child : theStruct.structChildren()) {
                if (child != null) {
                    copyObjectStorage(child, theOldObjectStorage,
                            theNewObjectStorage);
                }
            }
        }
    }

    /** Gets a storage instance that can store single element, used for global fields. */
    protected abstract AbstractStorage getSingleStorage(
            final Struct theGlobalStruct);

    /** Initializes object arrays for storing object field types. */
    @SuppressWarnings("rawtypes")
    protected final void initObjectStorage(final Struct theStruct) {

        if (theStruct.structObjectFields().length > 0) {

            for (final ObjectField<?, ?> f : theStruct.structObjectFields()) {

                final int objectFieldIndex = f.properties().objectFieldIndex();
                if (!f.properties().isOptional()) {
                    final Object[] objects = new Object[capacity];
                    objectStorageObjects[objectFieldIndex] = objects;
                } else {
                    objectStorageObjects[objectFieldIndex] = COLL_FACTORY
                            .createIntObjectMap(capacity, Object.class);
                }
            }
        }
        if (theStruct.structChildren().length > 0) {

            for (final Struct child : theStruct.structChildren()) {
                if (child != null) {
                    initObjectStorage(child);
                }
            }
        }

    }

    /** Checks if the Storage and Struct types are different. */
    protected boolean isDifferent(final Storage theOther) {
        if (theOther instanceof StorageWrapperImpl) {
            return true;
        }
        return !(theOther.getClass() == getClass())
                || theOther.struct().equals(struct());
    }

    /** Initializes object arrays for storing object field types. */
    @SuppressWarnings("rawtypes")
    protected final void reInitObjectStorage(final Struct theStruct,
            final Object[] theObjectStorage) {
        if (theStruct.structObjectFields().length > 0) {
            for (final ObjectField<?, ?> f : theStruct.structObjectFields()) {
                if (!f.properties().isOptional()) {
                    theObjectStorage[f.properties().objectFieldIndex()] = new Object[capacity];
                } else {
                    final int objectFieldIndex = f.properties()
                            .objectFieldIndex();
                    theObjectStorage[objectFieldIndex] = COLL_FACTORY
                            .createIntObjectMap(capacity, Object.class);
                }
            }
        }
        if (theStruct.structChildren().length > 0) {
            for (final Struct child : theStruct.structChildren()) {
                if (child != null) {
                    reInitObjectStorage(child, theObjectStorage);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public ChangeListenerSupport changeListenerSupport() {
        return listenerSupport;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void checkAccess(final Field<?, ?> theField) {
        final String error = doAccessCheck(theField, struct, this);
        checkState(error == null, error);
    }

    /**
     * Checks if the storage is read only.
     *
     * @throws IllegalStateException if this storage is readOnly
     */
    public void checkReadOnly() {
        checkState(!readOnly,
                "Attempt to modify a value when the current Storage is in "
                        + "readOnly state.");
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        clearAllFields();
        clearAllChildren();
    }

    /** Clears an object field. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <E, F extends ObjectField<E, F>> void clearObject(
            final ObjectField<E, F> theField) {

        if (theField.global() && globalFieldsStorage != null) {
            globalFieldsStorage.clearObject(theField);
        } else {

            checkReadOnly();
            checkAccess(theField);
            boolean valueChanged;
            E oldObject;

            final int objectFieldIndex = theField.properties()
                    .objectFieldIndex();

            if (theField.properties().isOptional()) {

                final IntObjectMap map = (IntObjectMap) objectStorageObjects[objectFieldIndex];
                if (map.containsKey(structure)) {

                    valueChanged = true;
                    oldObject = (E) map.remove(structure);
                } else {

                    valueChanged = false;
                    oldObject = null;
                }
            } else {

                final Object[] array = (Object[]) objectStorageObjects[objectFieldIndex];
                oldObject = (E) array[structure];
                array[structure] = null;
                valueChanged = oldObject != null;

            }
            if (!transactionsDisabled && valueChanged) {

                publishChange(theField, null, oldObject, getSelectedStructure());
            }
        }
    }

    /** Clears an optional boolean field. */
    @SuppressWarnings({ "unchecked", "null", "rawtypes" })
    public void clearOptional(final BooleanOptionalField theField) {

        checkReadOnly();
        checkAccess(theField);

        final IntSet set = (IntSet) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final boolean oldValue = set.contains(structure) ? set
                .removeAllOccurrences(structure) > 0 : false;
        if (!transactionsDisabled && oldValue) {
            publishChange((BooleanField) theField, BOOLEAN_OPTIONAL, false,
                    oldValue, getSelectedStructure());
        }
    }

    /** Clears an optional byte field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final ByteOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntByteMap map = (IntByteMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final byte oldValue = map.containsKey(structure) ? map
                .remove(structure) : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange((ByteField) theField, BYTE_OPTIONAL, (byte) 0,
                    oldValue, getSelectedStructure());
        }
    }

    /** Clears an optional char field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final CharOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntCharMap map = (IntCharMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final char oldValue = map.containsKey(structure) ? map
                .remove(structure) : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange((CharField) theField, CHAR_OPTIONAL, (char) 0,
                    oldValue, getSelectedStructure());
        }
    }

    /** Clears an optional double field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final DoubleOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntDoubleMap map = (IntDoubleMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final double oldValue = map.containsKey(structure) ? map
                .remove(structure) : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange((DoubleField) theField, DOUBLE_OPTIONAL, 0, oldValue,
                    getSelectedStructure());
        }
    }

    /** Clears an optional float field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final FloatOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntFloatMap map = (IntFloatMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final float oldValue = map.containsKey(structure) ? map
                .remove(structure) : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange((FloatField) theField, FLOAT_OPTIONAL, 0, oldValue,
                    getSelectedStructure());
        }
    }

    /** Clears an optional int field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final IntOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntIntMap map = (IntIntMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final int oldValue = map.containsKey(structure) ? map.remove(structure)
                : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange((IntField) theField, INT_OPTIONAL, 0, oldValue,
                    getSelectedStructure());
        }
    }

    /** Clears an optional long field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final LongOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntLongMap map = (IntLongMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final long oldValue = map.containsKey(structure) ? map
                .remove(structure) : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange(theField, LONG_OPTIONAL, 0, oldValue,
                    getSelectedStructure());
        }
    }

    /** Clears an optional short field. */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    public void clearOptional(final ShortOptionalField theField) {
        checkReadOnly();
        checkAccess(theField);
        final IntShortMap map = (IntShortMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final short oldValue = map.containsKey(structure) ? map
                .remove(structure) : 0;
        if (!transactionsDisabled && oldValue != 0) {
            publishChange((ShortField) theField, SHORT_OPTIONAL, (short) 0,
                    oldValue, getSelectedStructure());
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Storage copy() {
        checkState(
                !isSecondary,
                "This is a child storage of some other storage, copy can be created only for a base storage instances.");
        checkState(!transactionManager().isUncommitted(),
                "The current Storage has uncommitted data.");
        final Storage result = blankCopy();
        copyStorage(result);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public void copyStorage(final Storage theOther) {
        copyStorage(theOther, Struct.DEFAULT_SCHEMA_MIGRATOR);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("null")
    public void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator) {

        checkState(theOther.getSize() >= capacity, "The storage object size "
                + " must be equal to or greater than: " + capacity);
        // Hold the transactional flag into a temp variable.
        // Hold selected index of current storage in a temp field.
        final boolean transactionEnabled = theOther.transactionsEnabled();
        final int thisCurrentIndex = getSelectedStructure();
        final int otherCurrentIndex = getSelectedStructure();

        // Set selected index to zero and disable transactions on 'theOther'
        // storage.
        selectStructure(0);
        theOther.selectStructure(0);
        theOther.enableTransactions(false);

        final Struct otherStruct = theOther.struct();
        final Struct thisStruct = struct;

        final Map<Field<?, ?>, Field<?, ?>> schemaMigrator = theSchemaMigrator
                .mapSchema(otherStruct, thisStruct);

        final Map<Field<?, ?>, Struct> listFields = new HashMap<>();
        final Map<Field<?, ?>, Struct> listOldFields = new HashMap<>();
        final List<Field<?, ?>> globalFields = new ArrayList<>();
        final List<Field<?, ?>> nonListFields = new ArrayList<>();

        // check if field types match.
        for (final Field<?, ?> newField : schemaMigrator.keySet()) {

            final Field<?, ?> oldField = schemaMigrator.get(newField);
            if (newField.fieldType() != oldField.fieldType()) {
                throw new IllegalStateException("Field types for fields "
                        + newField.qualifiedName() + " and "
                        + oldField.qualifiedName() + " do not match!");
            }
            final Struct s = checkIsListField(newField, theOther.struct());
            if (s != null) {
                final Field<?, ?> oldListField = schemaMigrator.get(newField);
                if (oldListField != null) {
                    final Struct oldS = checkIsListField(oldListField,
                            thisStruct);
                    listFields.put(newField, s);
                    listOldFields.put(oldListField, oldS);
                }

            } else if (newField.global()) {
                globalFields.add(newField);
            } else {
                nonListFields.add(newField);
            }
        }

        // Copy Global fields only once.
        for (final Field<?, ?> newField : globalFields) {
            copyField(theOther, schemaMigrator, newField);
        }

        // Copy Field data for all
        for (int i = 0; i < capacity; i++) {
            selectStructure(i);
            theOther.selectStructure(i);

            for (final Field<?, ?> newField : nonListFields) {
                if (newField.virtual() || newField.global()) {
                    continue;
                }
                copyField(theOther, schemaMigrator, newField);
            }
            for (final Field<?, ?> newField : listFields.keySet()) {
                final Field<?, ?> oldField = schemaMigrator.get(newField);
                final Struct oldFieldParent = listOldFields.get(oldField);
                final Struct newFieldParent = listFields.get(newField);

                final Storage childStorage = list(oldFieldParent);

                if (childStorage != null) {
                    final Storage otherChildStorage = theOther
                            .createOrClearList(newFieldParent,
                                    childStorage.getCapacity());
                    otherChildStorage.enableTransactions(false);
                    childStorage.copyStorage(otherChildStorage,
                            theSchemaMigrator);
                    otherChildStorage.enableTransactions(childStorage
                            .transactionsEnabled());
                }
            }
            // call post processor here.
            theSchemaMigrator.postProcessor(this, theOther, i, i);
        }
        // reset the selected index.
        // reset the transactional flag.
        selectStructure(thisCurrentIndex);
        theOther.selectStructure(otherCurrentIndex);
        theOther.transactionManager().commit();
        theOther.enableTransactions(transactionEnabled);
    }

    /** {@inheritDoc} */
    @Override
    public void enableTransactions(final boolean theEnableFlag) {

        checkState(!transactionManager().isUncommitted(),
                "The current Storage has uncommitted data.");
        enableTransactionsInternal(theEnableFlag);
    }

    @SuppressWarnings("null")
    public StorageBuilder getBuilder() {

        final StorageBuilder builder = new StorageBuilder();
        builder.setArity(arity);
        if (baseStorage != this) {
            builder.setBaseStorage(baseStorage);
        }
        builder.setCapacity(capacity);
        builder.setGlobalFieldsStorage(globalFieldsStorage);
        builder.setGlobalStorage(isGlobalStorage);
        builder.setSecondary(isSecondary);
        builder.setObjectStorageObjects(objectStorageObjects);
        builder.setOptionalStorageObjects(optionalStorageObjects);
        builder.setReadOnly(readOnly);
        builder.setStruct(struct);
        builder.setStructure(structure);
        builder.setTransactionsDisabled(transactionsDisabled);
        return builder;
    }

    /** {@inheritDoc} */
    @Override
    public Point getSelectedPoint() {
        return multiDimensionalSupport().toPoint(getSelectedStructure());
    }

    /**
     * Global fields storage.
     *
     * @return the abstract storage
     */
    @Nullable
    public AbstractStorage globalFieldsStorage() {
        return globalFieldsStorage;
    }

    /** The is global storage. */
    public boolean isGlobalStorage() {
        return isGlobalStorage;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public MultiDimensionalSupport multiDimensionalSupport() {

        if (mdSupport == null) {
            mdSupport = new MultiDimensionSupportImpl(capacity, arity);
        }
        return mdSupport;
    }

    /** Publish boolean change to change listeners and Change tracking.*/
    public <F extends BooleanField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final boolean theValue,
            final boolean theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** Publish byte change to change listeners and Change tracking.*/
    public <F extends ByteField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final byte theValue,
            final byte theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();

    }

    /** Publish char change to change listeners and Change tracking.*/
    public <F extends CharField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final char theValue,
            final char theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** Publish double change to change listeners and Change tracking.*/
    public <F extends DoubleField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final double theValue,
            final double theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** Publish float change to change listeners and Change tracking.*/
    public <F extends FloatField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final float theValue,
            final float theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();

    }

    /** Publish int change to change listeners and Change tracking.*/
    public <F extends IntField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final int theValue,
            final int theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** Publish long change to change listeners and Change tracking.*/
    public <F extends LongField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final long theValue,
            final long theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** Publish short change to change listeners and Change tracking.*/
    public <F extends ShortField<?, F>> void publishChange(final F theField,
            final ChangeType theType, final short theValue,
            final short theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theType,
                theOldValue, theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** Publish Object change to change listeners and Change tracking.*/
    public <E, F extends ObjectField<E, F>> void publishChange(
            final ObjectField<E, F> theField, @Nullable final E theValue,
            @Nullable final E theOldValue, final int thePrimaryIndex) {

        actionSetImpl().logChange(thePrimaryIndex, theField, theOldValue,
                theValue);
        change.update(thePrimaryIndex, theField, theOldValue, theValue);
        callListeners();
    }

    /** {@inheritDoc} */
    @Override
    public boolean read(final BooleanField<?, ?> theField) {
        return theField.readBoolean(this);
    }

    /** {@inheritDoc} */
    @Override
    public byte read(final ByteField<?, ?> theField) {
        return theField.readByte(this);
    }

    /** {@inheritDoc} */
    @Override
    public char read(final CharField<?, ?> theField) {
        return theField.readChar(this);
    }

    /** {@inheritDoc} */
    @Override
    public double read(final DoubleField<?, ?> theField) {
        return theField.readDouble(this);
    }

    /** {@inheritDoc} */
    @Override
    public float read(final FloatField<?, ?> theField) {
        return theField.readFloat(this);
    }

    /** {@inheritDoc} */
    @Override
    public int read(final IntField<?, ?> theField) {
        return theField.readInt(this);
    }

    /** {@inheritDoc} */
    @Override
    public long read(final LongField<?, ?> theField) {
        return theField.readLong(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Override
    @Nullable
    public <E, F extends ObjectField<E, F>> E read(
            final ObjectField<E, F> theField) {

        if (theField.global() && !isGlobalStorage) {
            return globalFieldsStorage.read(theField);
        }
        checkAccess(theField);

        final int objectFieldIndex = theField.properties().objectFieldIndex();
        if (theField.properties().isOptional()) {
            final IntObjectMap map = (IntObjectMap) objectStorageObjects[objectFieldIndex];
            if (map.containsKey(structure)) {
                return (E) map.get(structure);
            }
            return null;
        }
        final Object[] array = (Object[]) objectStorageObjects[objectFieldIndex];
        return (E) array[structure];
    }

    /** {@inheritDoc} */
    @Override
    public short read(final ShortField<?, ?> theField) {
        return theField.readShort(this);
    }

    /** The Actual internal implementation of read boolean */
    public abstract <F extends BooleanField<?, F>> boolean readBoolean(
            final F theField);

    /** The Actual internal implementation of read byte */
    public abstract <F extends ByteField<?, F>> byte readByte(final F theField);

    /** The Actual internal implementation of read character */
    public abstract <F extends CharField<?, F>> char readChar(final F theField);

    /** The Actual internal implementation of read double */
    public abstract <F extends DoubleField<?, F>> double readDouble(
            final F theField);

    /** The Actual internal implementation of read float */
    public abstract <F extends FloatField<?, F>> float readFloat(
            final F theField);

    /** The Actual internal implementation of read character */
    public abstract <F extends IntField<?, F>> int readInt(final F theField);

    /** The Actual internal implementation of read long */
    public abstract <F extends LongField<?, F>> long readLong(final F theField);

    /** Reads an optional Boolean field. */
    public boolean readOptional(final BooleanOptionalField<?, ?> theField) {
        final IntSet set = (IntSet) optionalStorageObjects[theField
                .optionalFieldIndex()];
        return set.contains(structure);
    }

    /** Reads an optional Byte field. */
    public byte readOptional(final ByteOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntByteMap map = (IntByteMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** Reads an optional Char field. */
    public char readOptional(final CharOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntCharMap map = (IntCharMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** Reads an optional Double field. */
    public double readOptional(final DoubleOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntDoubleMap map = (IntDoubleMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** Reads an optional Float field. */
    public float readOptional(final FloatOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntFloatMap map = (IntFloatMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** Reads an optional Int field. */
    public int readOptional(final IntOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntIntMap map = (IntIntMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** Reads an optional Long field. */
    public long readOptional(final LongOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntLongMap map = (IntLongMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** Reads an optional Short field. */
    public short readOptional(final ShortOptionalField<?, ?> theField) {
        checkAccess(theField);
        final IntShortMap map = (IntShortMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        if (map.containsKey(structure)) {
            return map.lget();
        }
        return 0;
    }

    /** The Actual internal implementation of read short */
    public abstract <F extends ShortField<?, F>> short readShort(
            final F theField);

    /** Selects a structure using a point. */
    @Override
    public boolean selectPoint(final Point thePoint) {
        return selectStructure(multiDimensionalSupport().getIndex(thePoint));
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public Struct struct() {
        return struct;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public TransactionManager transactionManager() {
        if (tm == null) {
            final TransactionManagerImpl parentTm = isSecondary ? (TransactionManagerImpl) baseStorage
                    .transactionManager() : null;
            tm = new TransactionManagerImpl(this, parentTm);
        }
        return tm;
    }

    /** Transaction Recording enabled ? */
    @Override
    public boolean transactionsEnabled() {
        return !transactionsDisabled;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final BooleanField<?, ?> theField, final boolean theValue) {
        theField.writeBoolean(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final ByteField<?, ?> theField, final byte theValue) {
        theField.writeByte(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final CharField<?, ?> theField, final char theValue) {
        theField.writeChar(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final DoubleField<?, ?> theField, final double theValue) {
        theField.writeDouble(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final FloatField<?, ?> theField, final float theValue) {
        theField.writeFloat(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final IntField<?, ?> theField, final int theValue) {
        theField.writeInt(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final LongField<?, ?> theField, final long theValue) {
        theField.writeLong(this, theValue);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <E, F extends ObjectField<E, F>> void write(
            final ObjectField<E, F> theField, @Nullable final E theValue) {

        if (theField.global() && !isGlobalStorage) {
            globalFieldsStorage.write(theField, theValue);
            return;
        }

        checkReadOnly();
        checkAccess(theField);

        E oldValue;

        final int objectFieldIndex = theField.properties().objectFieldIndex();

        if (theField.properties().isOptional()) {
            final IntObjectMap map = (IntObjectMap) objectStorageObjects[objectFieldIndex];
            oldValue = (E) map.get(structure);
            map.put(structure, theValue);
        } else {
            final Object[] array = (Object[]) objectStorageObjects[objectFieldIndex];
            oldValue = (E) array[structure];
            array[structure] = theValue;
        }
        if (!transactionsDisabled && !Objects.equals(oldValue, theValue)) {
            publishChange(theField, theValue, oldValue, getSelectedStructure());
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public void write(final ShortField<?, ?> theField, final short theValue) {
        theField.writeShort(this, theValue);
    }

    /** The Actual internal implementation of write */
    public abstract <F extends BooleanField<?, F>> boolean writeImpl(
            final F theField, final boolean theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends ByteField<?, F>> byte writeImpl(
            final F theField, final byte theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends CharField<?, F>> char writeImpl(
            final F theField, final char theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends DoubleField<?, F>> double writeImpl(
            final F theField, final double theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends FloatField<?, F>> float writeImpl(
            final F theField, final float theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends IntField<?, F>> int writeImpl(final F theField,
            final int theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends LongField<?, F>> long writeImpl(
            final F theField, final long theValue);

    /** The Actual internal implementation of write */
    public abstract <F extends ShortField<?, F>> short writeImpl(
            final F theField, final short theValue);

    /** Writes an optional Boolean field. */
    @SuppressWarnings("null")
    public boolean writeOptional(final BooleanOptionalField<?, ?> theField,
            final boolean theValue) {
        final IntOpenHashSet set = (IntOpenHashSet) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final boolean oldValue = set.contains(structure);
        if (theValue) {
            set.add(structure);
        } else if (oldValue != theValue) {
            set.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Byte field. */
    @SuppressWarnings("null")
    public byte writeOptional(final ByteOptionalField<?, ?> theField,
            final byte theValue) {
        final IntByteMap map = (IntByteMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final byte oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Char field. */
    @SuppressWarnings("null")
    public char writeOptional(final CharOptionalField<?, ?> theField,
            final char theValue) {
        final IntCharMap map = (IntCharMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final char oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Double field. */
    @SuppressWarnings("null")
    public double writeOptional(final DoubleOptionalField<?, ?> theField,
            final double theValue) {
        final IntDoubleMap map = (IntDoubleMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final double oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Float field. */
    @SuppressWarnings("null")
    public float writeOptional(final FloatOptionalField<?, ?> theField,
            final float theValue) {
        final IntFloatMap map = (IntFloatMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final float oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Int field. */
    @SuppressWarnings("null")
    public int writeOptional(final IntOptionalField<?, ?> theField,
            final int theValue) {
        final IntIntMap map = (IntIntMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final int oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Long field. */
    @SuppressWarnings("null")
    public long writeOptional(final LongOptionalField<?, ?> theField,
            final long theValue) {
        final IntLongMap map = (IntLongMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final long oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }

    /** Writes an optional Short field. */
    @SuppressWarnings("null")
    public short writeOptional(final ShortOptionalField<?, ?> theField,
            final short theValue) {
        final IntShortMap map = (IntShortMap) optionalStorageObjects[theField
                .optionalFieldIndex()];
        final short oldValue = map.containsKey(structure) ? map.lget() : 0;
        if (theValue != 0) {
            map.put(structure, theValue);
        } else if (oldValue != 0) {
            map.remove(structure);
        }
        return oldValue;
    }
}
