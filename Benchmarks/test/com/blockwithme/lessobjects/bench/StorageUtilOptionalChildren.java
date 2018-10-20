/*******************************************************************************
 *
 * Copyright 2013 Sebastien Diot
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

// CHECKSTYLE IGNORE FOR NEXT 700 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class StorageUtilOptionalChildren {

    BeanStorage<Bean> beanStorage;

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

    IntField myIntField = FACTORY.newIntField("myIntField");

    public static void main(final String[] args) {
        final StorageUtilOptionalChildren util = new StorageUtilOptionalChildren(
                ALIGNED64, _64K, true, true);
        util.randomReads(_64K, 1);
        util.randomWrites(_64K, 1);
        util.sequentialReads(_64K, 1);
        util.sequentialWrites(_64K, 1);
    }

    public StorageUtilOptionalChildren(final Compiler theCompiler,
            final int theStroageCapacity, final boolean recordChanges,
            final boolean prePopulate) {
        storageCapacity = theStroageCapacity;
        compiler = theCompiler;
        this.recordChanges = recordChanges;

        for (int i = 0; i < RANDOM_INDEX_ARRAY.length; i++) {
            RANDOM_INDEX_ARRAY[i] = Math.abs(INTS[i] % storageCapacity);
        }

        // Create struct will primitive type fields.
        final Struct tmp;
        Struct child = new Struct("OptionalChild", new Struct[] {},
                new Field<?, ?>[] { boolField, intField, longField, byteField,
                        shortField, floatField, doubleField, charField });

        child = child.setOptional(true);
        tmp = new Struct("OptionalChildTest", new Struct[] { child },
                new Field<?, ?>[] { myIntField });

        final Struct compiled = compiler.compile(tmp);
        initFields(compiled);
        storage = compiler.initStorage(compiled, storageCapacity, false,
                recordChanges);
        if (prePopulate) {
            // calling so that there is data gets populated.
            storage.enableTransactions(false);
            sequentialWrites(storageCapacity, 1);
            storage.enableTransactions(recordChanges);
        }
    }

    private void initFields(final Struct compiled) {
        byteField = compiled.field("OptionalChild.byteField");
        boolField = compiled.field("OptionalChild.booleanField");
        charField = compiled.field("OptionalChild.charField");
        doubleField = compiled.field("OptionalChild.doubleField");
        floatField = compiled.field("OptionalChild.floatField");
        intField = compiled.field("OptionalChild.intField");
        longField = compiled.field("OptionalChild.longField");
        shortField = compiled.field("OptionalChild.shortField");
        myIntField = compiled.field("myIntField");
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
        BOOLEANS_READ[i] = storage.read(boolField);
        BYTES_READ[i] = storage.read(byteField);
        CHARS_READ[i] = storage.read(charField);
        DOUBLES_READ[i] = storage.read(doubleField);
        FLOATS_READ[i] = storage.read(floatField);
        INTS_READ[i] = storage.read(intField);
        LONGS_READ[i] = storage.read(longField);
        SHORTS_READ[i] = storage.read(shortField);
    }

    private void writeProxy(final int i, final SimpleProxy prxy) {
        prxy.setBooleanField(BOOLEANS[i]);
        prxy.setByteField(BYTES[i]);
        prxy.setCharField(CHARS[i]);
        prxy.setDoubleField(DOUBLES[i]);
        prxy.setFloatField(FLOATS[i]);
        prxy.setIntField(INTS[i]);
        prxy.setLongField(LONGS[i]);
        prxy.setShortField(SHORTS[i]);
    }

    private void writeStorage(final int i) {
        storage.write(boolField, BOOLEANS[i]);
        storage.write(byteField, BYTES[i]);
        storage.write(charField, CHARS[i]);
        storage.write(doubleField, DOUBLES[i]);
        storage.write(floatField, FLOATS[i]);
        storage.write(intField, INTS[i]);
        storage.write(longField, LONGS[i]);
        storage.write(shortField, SHORTS[i]);
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
                        storage.struct());
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
                        storage.struct());
            }
        }
    }
}
