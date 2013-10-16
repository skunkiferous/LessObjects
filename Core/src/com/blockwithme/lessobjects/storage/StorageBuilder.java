package com.blockwithme.lessobjects.storage;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.multidim.Arity;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntStack;

//CHECKSTYLE.OFF: IllegalType
/**
 * A Builder class used to construct Storage Objects for serialization and deserialization.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class StorageBuilder {

    /** The arity. */
    private Arity arity;

    /** The long array. */
    private long[] array;

    /** The base storage. */
    private Storage baseStorage;

    /** The capacity of this storage. */
    private int capacity;

    /** The global fields storage. */
    private AbstractStorage globalFieldsStorage;

    /** The index map. */
    private IntIntOpenHashMap indexMap;

    /** The is global storage. */
    private boolean isGlobalStorage;

    /** The is secondary. */
    private boolean isSecondary;

    /** The last inserted. */
    private IntStack lastInserted;

    /** The storage objects for object fields. */
    private Object[] objectStorageObjects;

    /** The optional storage. */
    private OptionalObjectStore optionalStorage;

    /** The storage objects for optional fields. */
    private Object[] optionalStorageObjects;

    /** The primary index. */
    private int primaryIndex;

    /** Indicates storage in writable state. */
    private boolean readOnly;

    /** The selected flag. */
    private boolean selected;

    /** The current struct which is backed by this storage, (transient) */
    private Struct struct;

    /** The selected structure. */
    private int structure;

    /** The position of the current struct, as an array Index. */
    private int structureIndex;

    /** The position of the current struct, in bits. */
    private long structurePosition;

    /** The disable transactions flag */
    private boolean transactionsDisabled;

    /** Gets the arity. */
    @SuppressWarnings("null")
    public Arity getArity() {
        return arity;
    }

    /** Gets the array. */
    @SuppressWarnings("null")
    public long[] getArray() {
        return array;
    }

    /** Gets the base storage. */
    @Nullable
    public Storage getBaseStorage() {
        return baseStorage;
    }

    /** Gets the capacity. */
    public int getCapacity() {
        return capacity;
    }

    /** Gets the global fields storage. */
    @SuppressWarnings("null")
    public AbstractStorage getGlobalFieldsStorage() {
        return globalFieldsStorage;
    }

    /** Gets the index map. */
    @SuppressWarnings("null")
    public IntIntOpenHashMap getIndexMap() {
        return indexMap;
    }

    /** Gets the last inserted. */
    @SuppressWarnings("null")
    public IntStack getLastInserted() {
        return lastInserted;
    }

    /** Gets the object storage objects. */
    @SuppressWarnings("null")
    public Object[] getObjectStorageObjects() {
        return objectStorageObjects;
    }

    /** Gets the optional storage. */
    @SuppressWarnings("null")
    public OptionalObjectStore getOptionalStorage() {
        return optionalStorage;
    }

    /** Gets the optional storage objects. */
    @SuppressWarnings("null")
    public Object[] getOptionalStorageObjects() {
        return optionalStorageObjects;
    }

    /** Gets the primary index. */
    public int getPrimaryIndex() {
        return primaryIndex;
    }

    /** Gets the Struct. */
    @SuppressWarnings("null")
    public Struct getStruct() {
        return struct;
    }

    /** Gets the Structure. */
    public int getStructure() {
        return structure;
    }

    /** Gets the Structure index. */
    public int getStructureIndex() {
        return structureIndex;
    }

    /** Gets the Structure position. */
    public long getStructurePosition() {
        return structurePosition;
    }

    /** Checks if is global storage. */
    public boolean isGlobalStorage() {
        return isGlobalStorage;
    }

    /** Checks if is read only. */
    public boolean isReadOnly() {
        return readOnly;
    }

    /** Checks if is secondary. */
    public boolean isSecondary() {
        return isSecondary;
    }

    /** Checks if is selected. */
    public boolean isSelected() {
        return selected;
    }

    /** Checks if is transactions disabled. */
    public boolean isTransactionsDisabled() {
        return transactionsDisabled;
    }

    /** Sets the arity. */
    public void setArity(final Arity theArity) {
        arity = theArity;
    }

    /** Sets the array. */
    public void setArray(final long[] theArray) {
        array = theArray;
    }

    /** Sets the base storage. */
    public void setBaseStorage(final Storage theBaseStorage) {
        baseStorage = theBaseStorage;
    }

    /** Sets the capacity. */
    public void setCapacity(final int theCapacity) {
        capacity = theCapacity;
    }

    /** Sets the global fields storage. */
    public void setGlobalFieldsStorage(
            final AbstractStorage theGlobalFieldsStorage) {
        globalFieldsStorage = theGlobalFieldsStorage;
    }

    /** Sets the 'is global storage' flag. */
    public void setGlobalStorage(final boolean theGlobalStorageFlag) {
        isGlobalStorage = theGlobalStorageFlag;
    }

    /** Sets the index map. */
    public void setIndexMap(final IntIntOpenHashMap theIndexMap) {
        indexMap = theIndexMap;
    }

    /** Sets the last inserted. */
    public void setLastInserted(final IntStack theLastInserted) {
        lastInserted = theLastInserted;
    }

    /** Sets the object storage objects. */
    public void setObjectStorageObjects(final Object[] theObjectStorageObjects) {
        objectStorageObjects = theObjectStorageObjects;
    }

    /** Sets the optional storage. */
    public void setOptionalStorage(final OptionalObjectStore theOptionalStorage) {
        optionalStorage = theOptionalStorage;
    }

    /** Sets the optional storage objects. */
    public void setOptionalStorageObjects(
            final Object[] theOptionalStorageObjects) {
        optionalStorageObjects = theOptionalStorageObjects;
    }

    /** Sets the primary index. */
    public void setPrimaryIndex(final int thePrimaryIndex) {
        primaryIndex = thePrimaryIndex;
    }

    /** Sets the read only. */
    public void setReadOnly(final boolean theReadOnly) {
        readOnly = theReadOnly;
    }

    /** Sets the secondary storage flag. */
    public void setSecondary(final boolean theSecondaryStorageFlag) {
        isSecondary = theSecondaryStorageFlag;
    }

    /** Sets the selected. */
    public void setSelected(final boolean theSelected) {
        selected = theSelected;
    }

    /** Sets the struct. */
    public void setStruct(final Struct theStruct) {
        struct = theStruct;
    }

    /** Sets the selected structure position. */
    public void setStructure(final int theStructure) {
        structure = theStructure;
    }

    /** The position of the current struct, as an array Index. */
    public void setStructureIndex(final int theStructureIndex) {
        structureIndex = theStructureIndex;
    }

    /** The position of the current struct, in bits. */
    public void setStructurePosition(final long theStructurePosition) {
        structurePosition = theStructurePosition;
    }

    /** The disable transactions flag */
    public void setTransactionsDisabled(
            final boolean theTransactionsDisabledFlag) {
        transactionsDisabled = theTransactionsDisabledFlag;
    }
}
