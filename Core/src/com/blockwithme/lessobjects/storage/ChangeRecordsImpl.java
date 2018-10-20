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

import static com.blockwithme.lessobjects.storage.AbstractStorage.STRUCT;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.BooleanValueChange;
import com.blockwithme.lessobjects.beans.ByteValueChange;
import com.blockwithme.lessobjects.beans.CharValueChange;
import com.blockwithme.lessobjects.beans.DoubleValueChange;
import com.blockwithme.lessobjects.beans.FloatValueChange;
import com.blockwithme.lessobjects.beans.IntValueChange;
import com.blockwithme.lessobjects.beans.LongValueChange;
import com.blockwithme.lessobjects.beans.ObjectValueChange;
import com.blockwithme.lessobjects.beans.ShortValueChange;
import com.blockwithme.lessobjects.beans.ValueChange;

/**
 * The Change Records Implementation class, allows you to iterate over the changes.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class ChangeRecordsImpl implements ChangeRecords {

    /** The Value Change Objects. */
    public static class ValueChangeObjects {

        /** The boolean value change. */
        private BooleanValueChange booleanValueChange;

        /** The byte value change. */
        private ByteValueChange byteValueChange;

        /** The char value change. */
        private CharValueChange charValueChange;

        /** The double value change. */
        private DoubleValueChange doubleValueChange;

        /** The float value change. */
        private FloatValueChange floatValueChange;

        /** The int value change. */
        private IntValueChange intValueChange;

        /** The long value change. */
        private LongValueChange longValueChange;

        /** The object value change. */
        @SuppressWarnings("rawtypes")
        private ObjectValueChange objectValueChange;

        /** The short value change. */
        private ShortValueChange shortValueChange;

        @SuppressWarnings("null")
        ByteValueChange byteValueChange() {
            if (byteValueChange == null) {
                byteValueChange = new ByteValueChange();
            }
            return byteValueChange;
        }

        @SuppressWarnings("null")
        CharValueChange charValueChange() {
            if (charValueChange == null) {
                charValueChange = new CharValueChange();
            }
            return charValueChange;
        }

        @SuppressWarnings("null")
        DoubleValueChange doubleValueChange() {
            if (doubleValueChange == null) {
                doubleValueChange = new DoubleValueChange();
            }
            return doubleValueChange;
        }

        @SuppressWarnings("null")
        FloatValueChange floatValueChange() {
            if (floatValueChange == null) {
                floatValueChange = new FloatValueChange();
            }
            return floatValueChange;
        }

        @SuppressWarnings("null")
        IntValueChange intValueChange() {
            if (intValueChange == null) {
                intValueChange = new IntValueChange();
            }
            return intValueChange;
        }

        @SuppressWarnings("null")
        LongValueChange longValueChange() {
            if (longValueChange == null) {
                longValueChange = new LongValueChange();
            }
            return longValueChange;
        }

        @SuppressWarnings({ "null", "rawtypes" })
        ObjectValueChange objectValueChange() {
            if (objectValueChange == null) {
                objectValueChange = new ObjectValueChange();
            }
            return objectValueChange;
        }

        @SuppressWarnings("null")
        ShortValueChange shortValueChange() {
            if (shortValueChange == null) {
                shortValueChange = new ShortValueChange();
            }
            return shortValueChange;
        }

        @SuppressWarnings("null")
        public BooleanValueChange booleanValueChange() {
            if (booleanValueChange == null) {
                booleanValueChange = new BooleanValueChange();
            }
            return booleanValueChange;
        }
    }

    /** The reference to the change storage. */
    private final ChangeStorage changeStorage;

    /** The children. */
    private final Set<ChangeRecordsImpl> children;

    /** The number of changes. */
    private int numberOfChanges;

    /** Instantiates a new change records impl.
     *
     * @param theChangeStorage the change storage
     */
    public ChangeRecordsImpl(final ChangeStorage theChangeStorage) {
        changeStorage = theChangeStorage;
        children = new HashSet<>();
    }

    /**
     * Instantiates a new change records impl.
     *
     * @param theChangeStorage the the change storage
     * @param theChildren the the children
     */
    public ChangeRecordsImpl(final ChangeStorage theChangeStorage,
            final Set<ChangeRecordsImpl> theChildren) {

        changeStorage = theChangeStorage;
        children = theChildren;
    }

    public void addChild(final ChangeRecordsImpl theChild) {
        children.add(theChild);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<ValueChange<?>> changes(final Struct theStruct) {

        return new Iterator<ValueChange<?>>() {

            private Iterator<Iterator<ValueChange<?>>> childrenIterator;

            private Iterator<ValueChange<?>> currentChild;

            /** The current position. */
            private int currentPos;

            private ValueChangeObjects valueChangeObjects;

            {
                valueChangeObjects = new ValueChangeObjects();
                if (!children.isEmpty()) {

                    childrenIterator = new Iterator<Iterator<ValueChange<?>>>() {
                        Iterator<ChangeRecordsImpl> itr = children.iterator();

                        @Override
                        public boolean hasNext() {
                            return itr.hasNext();
                        }

                        @Override
                        public Iterator<ValueChange<?>> next() {
                            return itr.next().changes(theStruct);
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException(
                                    "Remove not supported for this iterator !");
                        }
                    };
                } else {
                    childrenIterator = Collections.EMPTY_LIST.iterator();
                }

            }

            @Override
            public boolean hasNext() {
                if (currentPos < numberOfChanges) {
                    return true;
                }
                while ((currentChild == null || !currentChild.hasNext())
                        && childrenIterator.hasNext()) {
                    currentChild = childrenIterator.next();
                }
                if (currentChild != null) {
                    return currentChild.hasNext();
                }

                return false;
            }

            @SuppressWarnings("null")
            @Override
            public ValueChange<?> next() {
                checkState(hasNext(), "No more elements in the iterator !");
                if (currentPos < numberOfChanges) {
                    changeStorage.selectStructure(currentPos++);
                    final ChangeType type = STRUCT.changeType().readAny(
                            changeStorage);
                    final int index = changeStorage.read(STRUCT.index());
                    final String fieldName = changeStorage.read(STRUCT
                            .fieldName());
                    @SuppressWarnings("rawtypes")
                    // TODO the 'field' method call should be replaced with some
                    // other method which is more efficient.
                    final Field f = theStruct.field(fieldName);
                    return type.loadFromStorage(changeStorage, index, f,
                            valueChangeObjects);
                }
                return currentChild.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "Remove is not supported on this iterator");
            }
        };
    }

    /** The Change storage.*/
    @SuppressWarnings("null")
    public ChangeStorage changeStorage() {
        return changeStorage;
    }

    /**  The children Change Records */
    @SuppressWarnings("null")
    @Override
    public Set<ChangeRecordsImpl> children() {
        return children;
    }

    /**
     * Internal method.
     *
     * @return the current change number and increments the counter.
     */
    public int incrementChanges() {
        return numberOfChanges++;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        if (numberOfChanges > 0) {
            return false;
        } else if (!children.isEmpty()) {
            for (final ChangeRecordsImpl child : children) {
                if (!child.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
