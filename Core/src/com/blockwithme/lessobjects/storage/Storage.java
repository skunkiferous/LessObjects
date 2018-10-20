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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.SchemaMigrator;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.MultiDimensionalSupport;
import com.blockwithme.lessobjects.fields.object.ObjectField;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.multidim.Point;

/**
 * A Storage is an object that actually wraps data for a schema that is defined by a Struct
 * object. The Storage is designed to work like an array of objects that can store
 * thousands of objects of same type where the type is defined by passing an instance
 * of compiled Struct at the Storage creation time. The interface design is similar to
 * that of a cursor where we first select the cursor position called 'storage index'.
 * We can then call read or write methods to access the data (object) at that particular index.
 *
 * @see TransactionManager
 * @see com.blockwithme.lessobjects.storage.ChangeRecords
 * @see com.blockwithme.lessobjects.ValueChangeListener
 * @see ActionSet
 *
 * @author monster, tarung
 */
@ParametersAreNonnullByDefault
public interface Storage {

    /** Change listener support.
     *
     * @return the change listener support */
    ChangeListenerSupport changeListenerSupport();

    /** Checks if access to the field is permitted and is backed by the current
     * storage */
    void checkAccess(final Field<?, ?> theChild);

    /** Clear (set to default values or 0/false) all the fields of the selected
     * structure. */
    void clear();

    /** Clear (set to 0/false) one field of the selected structure. */
    void clear(final Field<?, ?> theField);

    /** Clear (set to 0/false) one child of the selected structure. */
    void clearChild(final Struct theChild);

    /**
     * Creates a new copy of this Storage instance.
     *
     * @return the copy of this storage instance.
     * @throws IllegalStateException if the current transaction has some uncommitted data.
     */
    Storage copy();

    /**
     * Copies current storage data to 'theOther' storage. The 'transactional' property of 'theOther' storage
     * is disabled before the data is copied and reset to its original value later. This operation cannot be rolled-back.
     *
     * @param theOther the other storage
     * @param theSchemaMigrator the schema migrator in case the new schema is different than the original schema.
     * @throws IllegalStateException if there is a mismatch in the underlying structures of the
     *         two storage instances or enough space is not available in 'theOther' storage, or 'theOther' has some some
     *         uncommitted data.
     */
    void copyStorage(final Storage theOther,
            final SchemaMigrator theSchemaMigrator);

    /** Creates a new list-type storage and returns the reference, if there
     * exists a non-empty storage at this position, it is discarded and changes
     * are recorded.
     *
     * @param theListChild the list child
     * @return the resizable storage */
    Storage createOrClearList(final Struct theListChild);

    /** Creates a new list-type storage and returns the reference, if there
     * exists a non-empty storage at this position, it is discarded and changes
     * are recorded.
     *
     * @param theListChild the list child
     * @param theInitialSize the initial size
     * @return the resizable storage */
    Storage createOrClearList(final Struct theListChild,
            final int theInitialSize);

    /** Sets the transaction enabled property to true/false.
     * If this property is switched off the storage will not be transactional
     * i.e. the changes made to storage data will cannot be rolled back.
     * The changes listeners will also *not* be invoked.
     *
     * @throws IllegalStateException when there is uncommitted data in this storage*/
    void enableTransactions(final boolean theEnableFlag);

    /** Returns the current capacity of the underlying raw storage. */
    int getCapacity();

    /** Returns the position of the currently selected structure in form of coordinates represented by a Point object. */
    Point getSelectedPoint();

    /** Returns the position of the currently selected structure. */
    int getSelectedStructure();

    /** Returns maximum number of elements this storage can hold, the capacity
     * and size values are same for fixed size storage but may vary for
     * resizable storage */
    int getSize();

    /**
     * If the struct backed by this storage contains a list child, this
     * method returns the storage corresponding to the list child. Returns null
     * if the list has no elements.
     *
     * @param theListChild reference to the list child.
     * @return the list specific storage
     */
    @Nullable
    Storage list(final Struct theListChild);

    /** Reads a BooleanField. */
    boolean read(final BooleanField<?, ?> theField);

    /** Reads a ByteField. */
    byte read(final ByteField<?, ?> theField);

    /** Reads a CharField. */
    char read(final CharField<?, ?> theField);

    /** Reads a DoubleField. */
    double read(final DoubleField<?, ?> theField);

    /** Reads a FloatField. */
    float read(final FloatField<?, ?> theField);

    /** Reads a IntField. */
    int read(final IntField<?, ?> theField);

    /** Reads a LongField. */
    long read(final LongField<?, ?> theField);

    /** Reads a ObjectField<E>. */
    @Nullable
    <E, F extends ObjectField<E, F>> E read(final ObjectField<E, F> theField);

    /** Reads a ShortField. */
    short read(final ShortField<?, ?> theField);

    /** Modifies the capacity of the storage.
     * The old data that still fits in the new storage will be preserved. */
    void resizeStorage(final int theNewCapacity);

    /** Returns the Root struct that is backed by this storage */
    Struct rootStruct();

    /** Positions the storage to the position corresponding to the coordinates of a point. */
    boolean selectPoint(final Point thePoint);

    /** Positions the storage to the desired index position. */
    boolean selectStructure(final int theStructure);

    /** Change the "selected field" (position) of a union */
    void selectUnionPosition(final Field<?, ?> theField);

    /** Change the "selected field" (position) of a union */
    void selectUnionPosition(final Struct theStruct, final int thePosition);

    /**
     * Returns the Struct instance backed by this storage.
     * @return the struct instance
     */
    Struct struct();

    /**
     * @return the currently associated transaction manager instance.
     * @throws IllegalStateException if the storage is not transactional.
     * @see Storage#enableTransactions(boolean)  */
    TransactionManager transactionManager();

    /** Transactions enabled ? */
    boolean transactionsEnabled();

    /** Writes a BooleanField. */
    void write(final BooleanField<?, ?> theField, final boolean theValue);

    /** Writes a ByteField. */
    void write(final ByteField<?, ?> theField, final byte theValue);

    /** Writes a CharField. */
    void write(final CharField<?, ?> theField, final char theValue);

    /** Writes a DoubleField. */
    void write(final DoubleField<?, ?> theField, final double theValue);

    /** Writes a FloatField. */
    void write(final FloatField<?, ?> theField, final float theValue);

    /** Writes a IntField. */
    void write(final IntField<?, ?> theField, final int theValue);

    /** Writes a LongField. */
    void write(final LongField<?, ?> theField, final long theValue);

    /** Writes a ObjectField<E>. This method *must* accept null, but can
     * treat it as some default value. */
    <E, F extends ObjectField<E, F>> void write(
            final ObjectField<E, F> theField, @Nullable final E theValue);

    /** Writes a ShortField. */
    void write(final ShortField<?, ?> theField, final short theValue);

    /**
     * Copies current storage data to 'theOther' storage. The 'transactional' property of 'theOther' storage
     * is disabled before the data is copied and reset to its original value later. This operation cannot be rolled-back.
     *
     * @param theOther the other storage
     * @throws IllegalStateException if there is a mismatch in the underlying structures of the
     *         two storage instances or enough space is not available in 'theOther' storage, or 'theOther' has some some
     *         uncommitted data.
     */
    public void copyStorage(final Storage theOther);

    /** An object of MultiDimensionalSupport to access multidimensional data in this storage. */
    public MultiDimensionalSupport multiDimensionalSupport();
}
