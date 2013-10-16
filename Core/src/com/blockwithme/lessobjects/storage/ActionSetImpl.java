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
// $codepro.audit.disable com.instantiations.assist.eclipse.arrayIsStoredWithoutCopying, largeNumberOfMethods
package com.blockwithme.lessobjects.storage;

import static com.blockwithme.lessobjects.storage.AbstractStorage.STRUCT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

/**
 * The ActionSet implementation class, wraps the list of events generated by
 * all the change listeners and List of "ValueChange" objects. An instance of this
 * interface is returned at the time of commit or roll-back performed on a storage object.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ActionSetImpl implements ActionSet {

    /** The Initial Capacity. */
    static final int INITIAL_CAPACITY = 512;

    /** The change storage. */
    @Nonnull
    private final ChangeStorage changeStorage;

    /** The change records. */
    final ChangeRecordsImpl changeRecords;

    /** The children. */
    final Set<ActionSetImpl> children;

    /** The results list. */
    @Nonnull
    final List<Object> eventList;

    /** Writes one change to a Storage. */
    private static void writeChangeStruct(final SparseStorage theStorage,
            final int theStructIndex, final Field<?, ?> theField,
            final Field<?, ?> theNewField, final Field<?, ?> theOldField,
            final ChangeType theChangeType, final int theChangeNumber) {

        theStorage.selectStructure(theChangeNumber);
        theStorage.write(STRUCT.index(), theStructIndex);
        theStorage.write(STRUCT.fieldName(), theField.qualifiedName());
        STRUCT.changeType().writeAny(theChangeType, theStorage);
        theStorage.write(STRUCT.getNew().indexField(),
                (char) theNewField.localIndex());
        theStorage.write(STRUCT.getOld().indexField(),
                (char) theOldField.localIndex());
    }

    /** Instantiates a new action set impl. */
    @SuppressWarnings({ "rawtypes", "null" })
    ActionSetImpl() {

        changeStorage = new ChangeStorage();
        changeStorage.enableTransactions(false);
        eventList = new ArrayList<>();
        changeRecords = new ChangeRecordsImpl(changeStorage);
        children = new HashSet<>();
    }

    /** Adds a child ActionSet. */
    @SuppressWarnings("null")
    void addChild(final ActionSetImpl theChild) {
        children.add(theChild);
        changeRecords.addChild(theChild.changeRecords);
    }

    /** Log boolean value change. */
    @SuppressWarnings("null")
    <F extends BooleanField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType,
            final boolean theOldValue, final boolean theNewValue) {

        final BooleanField<?, ?> newField = STRUCT.getNew().booleanField();
        final BooleanField<?, ?> oldField = STRUCT.getOld().booleanField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log byte change. */
    @SuppressWarnings("null")
    <F extends ByteField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType, final byte theOldValue,
            final byte theNewValue) {

        final ByteField<?, ?> newField = STRUCT.getNew().byteField();
        final ByteField<?, ?> oldField = STRUCT.getOld().byteField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log char change. */
    @SuppressWarnings("null")
    <F extends CharField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType, final char theOldValue,
            final char theNewValue) {

        final CharField<?, ?> newField = STRUCT.getNew().charField();
        final CharField<?, ?> oldField = STRUCT.getOld().charField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log double value change. */
    @SuppressWarnings("null")
    <F extends DoubleField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType,
            final double theOldValue, final double theNewValue) {

        final DoubleField<?, ?> newField = STRUCT.getNew().doubleField();
        final DoubleField<?, ?> oldField = STRUCT.getOld().doubleField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log float value change. */
    @SuppressWarnings("null")
    <F extends FloatField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType,
            final float theOldValue, final float theNewValue) {

        final FloatField<?, ?> newField = STRUCT.getNew().floatField();
        final FloatField<?, ?> oldField = STRUCT.getOld().floatField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log int value change. */
    @SuppressWarnings("null")
    <F extends IntField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType, final int theOldValue,
            final int theNewValue) {

        final IntField<?, ?> newField = STRUCT.getNew().intField();
        final IntField<?, ?> oldField = STRUCT.getOld().intField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log Long change. */
    @SuppressWarnings("null")
    <F extends LongField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType, final long theOldValue,
            final long theNewValue) {

        final LongField<?, ?> newField = STRUCT.getNew().longField();
        final LongField<?, ?> oldField = STRUCT.getOld().longField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log short value change. */
    @SuppressWarnings("null")
    <F extends ShortField<?, F>> void logChange(final int theStructIndex,
            final F theField, final ChangeType theType,
            final short theOldValue, final short theNewValue) {

        final ShortField<?, ?> newField = STRUCT.getNew().shortField();
        final ShortField<?, ?> oldField = STRUCT.getOld().shortField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, theType, changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);
    }

    /** Log object value change. */
    @SuppressWarnings({ "rawtypes", "unchecked", "null" })
    <E, F extends ObjectField<E, F>> void logChange(final int theStructIndex,
            final ObjectField<E, F> theField, @Nullable final E theOldValue,
            @Nullable final E theNewValue) {

        final ObjectField newField = STRUCT.getNew().objectField();
        final ObjectField oldField = STRUCT.getOld().objectField();
        writeChangeStruct(changeStorage, theStructIndex, theField, newField,
                oldField, ChangeType.OBJECT_FIELD,
                changeRecords.incrementChanges());
        changeStorage.write(newField, theNewValue);
        changeStorage.write(oldField, theOldValue);

    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public ChangeRecords changeRecords() {
        return changeRecords;
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public ChangeStorage currentChanges() {
        return changeStorage;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("null")
    @Override
    public List<Object> events() {
        return eventList;
    }
}
