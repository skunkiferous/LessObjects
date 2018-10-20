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
package com.blockwithme.lessobjects.bench;

import static com.blockwithme.lessobjects.bench.Constants.ALIGNED64;
import static com.blockwithme.lessobjects.bench.Constants.FACTORY;
import static com.blockwithme.lessobjects.bench.Constants._64K;
import static com.blockwithme.lessobjects.bench.TestData.BOOLEANS;
import static com.blockwithme.lessobjects.bench.TestData.BOOLEANS_READ;
import static com.blockwithme.lessobjects.bench.TestData.BYTES;
import static com.blockwithme.lessobjects.bench.TestData.BYTES_READ;
import static com.blockwithme.lessobjects.bench.TestData.CHARS;
import static com.blockwithme.lessobjects.bench.TestData.CHARS_READ;
import static com.blockwithme.lessobjects.bench.TestData.DOUBLES;
import static com.blockwithme.lessobjects.bench.TestData.DOUBLES_READ;
import static com.blockwithme.lessobjects.bench.TestData.FLOATS;
import static com.blockwithme.lessobjects.bench.TestData.FLOATS_READ;
import static com.blockwithme.lessobjects.bench.TestData.INTS;
import static com.blockwithme.lessobjects.bench.TestData.INTS_READ;
import static com.blockwithme.lessobjects.bench.TestData.LONGS;
import static com.blockwithme.lessobjects.bench.TestData.LONGS_READ;
import static com.blockwithme.lessobjects.bench.TestData.RANDOM_INDEX_ARRAY;
import static com.blockwithme.lessobjects.bench.TestData.SHORTS;
import static com.blockwithme.lessobjects.bench.TestData.SHORTS_READ;

import java.util.Iterator;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.Field;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.beans.ValueChange;
import com.blockwithme.lessobjects.bench.bean.Bean;
import com.blockwithme.lessobjects.bench.bean.BeanStorage;
import com.blockwithme.lessobjects.fields.primitive.BooleanField;
import com.blockwithme.lessobjects.fields.primitive.ByteField;
import com.blockwithme.lessobjects.fields.primitive.CharField;
import com.blockwithme.lessobjects.fields.primitive.DoubleField;
import com.blockwithme.lessobjects.fields.primitive.FloatField;
import com.blockwithme.lessobjects.fields.primitive.IntField;
import com.blockwithme.lessobjects.fields.primitive.LongField;
import com.blockwithme.lessobjects.fields.primitive.ShortField;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

// CHECKSTYLE IGNORE FOR NEXT 300 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class StorageUtilUnion {

    private static interface DoOne {
        public void doOne(int i);
    }

    BeanStorage<Bean> beanStorage;

    final Struct compiled;

    Compiler compiler;

    boolean recordChanges;

    Storage storage;

    int storageCapacity;

    BooleanField boolField = FACTORY.newBooleanField("booleanField");

    ByteField byteField = FACTORY.newByteField("byteField");

    CharField charField = FACTORY.newCharField("charField");

    DoubleField doubleField = FACTORY.newDoubleField("doubleField");

    FloatField floatField = FACTORY.newFloatField("floatField");

    IntField intField = FACTORY.newIntField("intField");

    LongField longField = FACTORY.newLongField("longField");

    ShortField shortField = FACTORY.newShortField("shortField");

    private final DoOne[] read, write;

    public static void main(final String[] args) {
        final StorageUtilUnion util = new StorageUtilUnion(ALIGNED64, _64K,
                true, true);
        util.randomReads(_64K, 1);
        util.randomWrites(_64K, 1);
        util.sequentialReads(_64K, 1);
        util.sequentialWrites(_64K, 1);
    }

    public StorageUtilUnion(final Compiler theCompiler,
            final int theStroageCapacity, final boolean recordChanges,
            final boolean prePopulate) {
        read = new DoOne[] { new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(boolField);
                for (int j = 0; j < 8; j++) {
                    BOOLEANS_READ[i] = storage.read(boolField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(byteField);
                for (int j = 0; j < 8; j++) {
                    BYTES_READ[i] = storage.read(byteField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(charField);
                for (int j = 0; j < 8; j++) {
                    CHARS_READ[i] = storage.read(charField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(doubleField);
                for (int j = 0; j < 8; j++) {
                    DOUBLES_READ[i] = storage.read(doubleField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(floatField);
                for (int j = 0; j < 8; j++) {
                    FLOATS_READ[i] = storage.read(floatField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(intField);
                for (int j = 0; j < 8; j++) {
                    INTS_READ[i] = storage.read(intField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(longField);
                for (int j = 0; j < 8; j++) {
                    LONGS_READ[i] = storage.read(longField);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(shortField);
                for (int j = 0; j < 8; j++) {
                    SHORTS_READ[i] = storage.read(shortField);
                }
            }
        } };

        write = new DoOne[] { new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(boolField);
                for (int j = 0; j < 8; j++) {
                    storage.write(boolField, BOOLEANS[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(byteField);
                for (int j = 0; j < 8; j++) {
                    storage.write(byteField, BYTES[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(charField);
                for (int j = 0; j < 8; j++) {
                    storage.write(charField, CHARS[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(doubleField);
                for (int j = 0; j < 8; j++) {
                    storage.write(doubleField, DOUBLES[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(floatField);
                for (int j = 0; j < 8; j++) {
                    storage.write(floatField, FLOATS[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(intField);
                for (int j = 0; j < 8; j++) {
                    storage.write(intField, INTS[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(longField);
                for (int j = 0; j < 8; j++) {
                    storage.write(longField, LONGS[i]);
                }
            }
        }, new DoOne() {
            @Override
            public void doOne(final int i) {
                storage.selectUnionPosition(shortField);
                for (int j = 0; j < 8; j++) {
                    storage.write(shortField, SHORTS[i]);
                }
            }
        } };

        storageCapacity = theStroageCapacity;
        compiler = theCompiler;
        this.recordChanges = recordChanges;

        for (int i = 0; i < RANDOM_INDEX_ARRAY.length; i++) {
            RANDOM_INDEX_ARRAY[i] = Math.abs(INTS[i] % storageCapacity);
        }

        // Create struct with primitive type fields.
        final Struct tmp = new Struct("Union", true, new Struct[0],
                new Field<?, ?>[] { boolField, intField, longField, byteField,
                        shortField, floatField, doubleField, charField });

        compiled = compiler.compile(tmp);

        initFields(compiled);
        storage = compiler.initStorage(compiled, storageCapacity, false,
                recordChanges);
        if (prePopulate) {
            // calling so that there is data; gets populated.
            storage.enableTransactions(false);
            sequentialWrites(storageCapacity, 1);
            storage.enableTransactions(recordChanges);
        }
    }

    private void initFields(final Struct compiled) {
        byteField = compiled.field("byteField");
        boolField = compiled.field("booleanField");
        charField = compiled.field("charField");
        doubleField = compiled.field("doubleField");
        floatField = compiled.field("floatField");
        intField = compiled.field("intField");
        longField = compiled.field("longField");
        shortField = compiled.field("shortField");
    }

    private void readChanges(final ActionSet actionSet, final Struct theStruct) {
        final Iterator<ValueChange<?>> iterator = actionSet.changeRecords()
                .changes(theStruct);
        while (iterator.hasNext()) {
            final ValueChange<?> change = iterator.next();
            INTS_READ[0] = change.structureIndex();
            if (change.oldValue() != null) {
                INTS_READ[0]++;
            }
            if (change.newValue() != null) {
                INTS_READ[0]++;
            }
        }
    }

    private void readStorage(final int i) {
        read[i % 8].doOne(i);
    }

    private void writeStorage(final int i) {
        write[i % 8].doOne(i);
    }

    public void randomReads(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(RANDOM_INDEX_ARRAY[i]);
                readStorage(i);
            }
        }
    }

    public void randomWrites(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(RANDOM_INDEX_ARRAY[count]);
                writeStorage(count);
                count++;
            }
            if (storage.transactionsEnabled()) {
                readChanges(storage.transactionManager().commit(),
                        storage.rootStruct());
            }
        }
    }

    public void sequentialReads(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(i);
                readStorage(i);
            }
        }
    }

    public void sequentialWrites(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(i);
                writeStorage(count);
                count++;
            }
            if (storage.transactionsEnabled()) {
                readChanges(storage.transactionManager().commit(),
                        storage.rootStruct());
            }
        }
    }
}
