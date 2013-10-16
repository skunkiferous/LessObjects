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

import static com.blockwithme.lessobjects.bench.Constants.FACTORY;
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
import com.blockwithme.lessobjects.bench.bean.Factory;
import com.blockwithme.lessobjects.fields.optional.BooleanOptionalField;
import com.blockwithme.lessobjects.fields.optional.ByteOptionalField;
import com.blockwithme.lessobjects.fields.optional.CharOptionalField;
import com.blockwithme.lessobjects.fields.optional.DoubleOptionalField;
import com.blockwithme.lessobjects.fields.optional.FloatOptionalField;
import com.blockwithme.lessobjects.fields.optional.IntOptionalField;
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
import com.blockwithme.lessobjects.proxy.ProxyCursor;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;
import com.blockwithme.lessobjects.storage.StorageWrapper;
import com.blockwithme.lessobjects.storage.StorageWrapperImpl;

// CHECKSTYLE IGNORE FOR NEXT 700 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class StorageUtil {

    BeanStorage<Bean> beanStorage;

    Compiler compiler;

    ProxyCursor proxy;

    boolean recordChanges;

    Storage storage;

    StorageWrapper wrapper;

    StorageWrapper wrapper2L;

    int storageCapacity;

    BooleanField boolField = FACTORY.newBooleanField("booleanField");

    BooleanOptionalField boolFieldOptional = FACTORY
            .newBooleanOptional("booleanFieldOptional");

    ByteField byteField = FACTORY.newByteField("byteField");

    ByteOptionalField byteFieldOptional = FACTORY
            .newByteOptional("byteFieldOptional");

    CharField charField = FACTORY.newCharField("charField");

    CharOptionalField charFieldOptional = FACTORY
            .newCharOptional("charFieldOptional");

    DoubleField doubleField = FACTORY.newDoubleField("doubleField");

    DoubleOptionalField doubleFieldOptional = FACTORY
            .newDoubleOptional("doubleFieldOptional");

    FloatField floatField = FACTORY.newFloatField("floatField");

    FloatOptionalField floatFieldOptional = FACTORY
            .newFloatOptional("floatFieldOptional");

    IntField intField = FACTORY.newIntField("intField");

    IntOptionalField intFieldOptional = FACTORY.newIntOptional("intFieldOptional");

    LongField longField = FACTORY.newLongField("longField");

    LongOptionalField longFieldOptional = FACTORY
            .newLongOptional("longFieldOptional");

    ShortField shortField = FACTORY.newShortField("shortField");

    ShortOptionalField shortFieldOptional = FACTORY
            .newShortOptional("shortFieldOptional");

    public StorageUtil(final Compiler theCompiler,
            final int theStroageCapacity, final boolean recordChanges,
            final boolean prePopulate, final boolean createStorage,
            final boolean createProxy, final boolean createBeanStorage,
            final boolean createWrapper, final boolean createWrapper2L,
            final boolean optionalFields) {

        storageCapacity = theStroageCapacity;
        compiler = theCompiler;
        this.recordChanges = recordChanges;

        for (int i = 0; i < RANDOM_INDEX_ARRAY.length; i++) {
            RANDOM_INDEX_ARRAY[i] = Math.abs(INTS[i] % storageCapacity);
        }

        if (createProxy) {
            if (!optionalFields) {
                proxy = new ProxyCursor(new Class<?>[] { SimpleProxy.class },
                        compiler, storageCapacity);
                if (prePopulate) {
                    // calling so that there is data; gets populated.
                    proxy.enableTransactions(false);
                    sequentialWritesProxy(storageCapacity, 1);
                }
                proxy.enableTransactions(recordChanges);
            } else {
                proxy = new ProxyCursor(
                        new Class<?>[] { OptionalFieldsProxy.class }, compiler,
                        storageCapacity);
                if (prePopulate) {
                    // calling so that there is data gets populated.
                    proxy.enableTransactions(false);
                    sequentialOptionalWritesProxy(storageCapacity, 1);
                }
                proxy.enableTransactions(recordChanges);
            }
        }

        if (createStorage) {
            // Create struct will primitive type fields.
            final Struct tmp;
            if (!optionalFields) {
                tmp = new Struct("BenchmarkTest", new Struct[] {},
                        new Field<?, ?>[] { intField, longField, byteField,
                                shortField, floatField, doubleField, charField,
                                boolField });

                final Struct compiled = compiler.compile(tmp);
                initFields(compiled);
                storage = compiler.initStorage(compiled, storageCapacity,
                        false, recordChanges);
                if (prePopulate) {
                    // calling so that there is data gets populated.
                    storage.enableTransactions(false);
                    sequentialWrites(storageCapacity, 1);
                    storage.enableTransactions(recordChanges);
                }
            } else {
                tmp = new Struct("BenchmarkTest", new Struct[] {},
                        new Field<?, ?>[] { byteField, intFieldOptional,
                                longFieldOptional, byteFieldOptional,
                                shortFieldOptional, floatFieldOptional,
                                doubleFieldOptional, charFieldOptional,
                                boolFieldOptional });

                final Struct compiled = compiler.compile(tmp);
                initOptionalFields(compiled);
                storage = compiler.initStorage(compiled, storageCapacity,
                        false, recordChanges);

                if (prePopulate) {
                    // calling so that there is data gets populated.
                    storage.enableTransactions(false);
                    sequentialOptionalWrites(storageCapacity, 1);
                    storage.enableTransactions(recordChanges);
                }
            }
        }

        if (createBeanStorage) {
            beanStorage = new BeanStorage<Bean>(Bean.class, storageCapacity,
                    new Factory<Bean>() {
                        @Override
                        public Bean createNewInstance(final int elementIndex) {
                            final Bean b = new Bean();
                            if (prePopulate) {
                                writeBean(elementIndex, b);
                            }
                            return b;
                        }
                    }, recordChanges);
        }
        if (createWrapper) {
            wrapper = new StorageWrapperImpl(storage, 1l);
        } else if (createWrapper2L) {
            wrapper2L = new StorageWrapperImpl(storage, 1l);
            wrapper = new StorageWrapperImpl(wrapper, 2l);
        }
    }

    private void initFields(final Struct compiled) {
        boolField = compiled.field("booleanField");
        byteField = compiled.field("byteField");
        charField = compiled.field("charField");
        doubleField = compiled.field("doubleField");
        floatField = compiled.field("floatField");
        intField = compiled.field("intField");
        longField = compiled.field("longField");
        shortField = compiled.field("shortField");
    }

    private void initOptionalFields(final Struct compiled) {
        boolFieldOptional = compiled.field("booleanFieldOptional");
        byteFieldOptional = compiled.field("byteFieldOptional");
        charFieldOptional = compiled.field("charFieldOptional");
        doubleFieldOptional = compiled.field("doubleFieldOptional");
        floatFieldOptional = compiled.field("floatFieldOptional");
        intFieldOptional = compiled.field("intFieldOptional");
        longFieldOptional = compiled.field("longFieldOptional");
        shortFieldOptional = compiled.field("shortFieldOptional");
    }

    private void readBean(final int i, final Bean b) {
        BOOLEANS_READ[i] = b.isBooleanField();
        BYTES_READ[i] = b.getByteField();
        CHARS_READ[i] = b.getCharField();
        DOUBLES_READ[i] = b.getDoubleField();
        FLOATS_READ[i] = b.getFloatField();
        INTS_READ[i] = b.getIntField();
        LONGS_READ[i] = b.getLongField();
        SHORTS_READ[i] = b.getShortField();
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

    private void readOptionalStorage(final int i, final Storage strg) {
        BOOLEANS_READ[i] = strg.read(boolFieldOptional);
        BYTES_READ[i] = strg.read(byteFieldOptional);
        CHARS_READ[i] = strg.read(charFieldOptional);
        DOUBLES_READ[i] = strg.read(doubleFieldOptional);
        FLOATS_READ[i] = strg.read(floatFieldOptional);
        INTS_READ[i] = strg.read(intFieldOptional);
        LONGS_READ[i] = strg.read(longFieldOptional);
        SHORTS_READ[i] = strg.read(shortFieldOptional);
    }

    private void readProxy(final OptionalFieldsProxy prxy, final int i) {
        BOOLEANS_READ[i] = prxy.isBooleanField();
        BYTES_READ[i] = prxy.getByteField();
        CHARS_READ[i] = prxy.getCharField();
        DOUBLES_READ[i] = prxy.getDoubleField();
        FLOATS_READ[i] = prxy.getFloatField();
        INTS_READ[i] = prxy.getIntField();
        LONGS_READ[i] = prxy.getLongField();
        SHORTS_READ[i] = prxy.getShortField();
    }

    private void readProxy(final SimpleProxy prxy, final int i) {
        BOOLEANS_READ[i] = prxy.isBooleanField();
        BYTES_READ[i] = prxy.getByteField();
        CHARS_READ[i] = prxy.getCharField();
        DOUBLES_READ[i] = prxy.getDoubleField();
        FLOATS_READ[i] = prxy.getFloatField();
        INTS_READ[i] = prxy.getIntField();
        LONGS_READ[i] = prxy.getLongField();
        SHORTS_READ[i] = prxy.getShortField();
    }

    private void readStorage(final int i, final Storage strg) {
        BOOLEANS_READ[i] = strg.read(boolField);
        BYTES_READ[i] = strg.read(byteField);
        CHARS_READ[i] = strg.read(charField);
        DOUBLES_READ[i] = strg.read(doubleField);
        FLOATS_READ[i] = strg.read(floatField);
        INTS_READ[i] = strg.read(intField);
        LONGS_READ[i] = strg.read(longField);
        SHORTS_READ[i] = strg.read(shortField);
    }

    private void writeBean(final int elementIndex, final Bean b) {
        b.setBooleanField(BOOLEANS[elementIndex]);
        b.setByteField(BYTES[elementIndex]);
        b.setCharField(CHARS[elementIndex]);
        b.setDoubleField(DOUBLES[elementIndex]);
        b.setFloatField(FLOATS[elementIndex]);
        b.setIntField(INTS[elementIndex]);
        b.setLongField(LONGS[elementIndex]);
        b.setShortField(SHORTS[elementIndex]);
    }

    private void writeOptionalStorage(final int i, final Storage strg) {
        strg.write(boolFieldOptional, BOOLEANS[i]);
        strg.write(byteFieldOptional, BYTES[i]);
        strg.write(charFieldOptional, CHARS[i]);
        strg.write(doubleFieldOptional, DOUBLES[i]);
        strg.write(floatFieldOptional, FLOATS[i]);
        strg.write(intFieldOptional, INTS[i]);
        strg.write(longFieldOptional, LONGS[i]);
        strg.write(shortFieldOptional, SHORTS[i]);

    }

    private void writeProxy(final int i, final OptionalFieldsProxy prxy) {
        prxy.setBooleanField(BOOLEANS[i]);
        prxy.setByteField(BYTES[i]);
        prxy.setCharField(CHARS[i]);
        prxy.setDoubleField(DOUBLES[i]);
        prxy.setFloatField(FLOATS[i]);
        prxy.setIntField(INTS[i]);
        prxy.setLongField(LONGS[i]);
        prxy.setShortField(SHORTS[i]);
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

    private void writeStorage(final int i, final Storage strg) {
        strg.write(boolField, BOOLEANS[i]);
        strg.write(byteField, BYTES[i]);
        strg.write(charField, CHARS[i]);
        strg.write(doubleField, DOUBLES[i]);
        strg.write(floatField, FLOATS[i]);
        strg.write(intField, INTS[i]);
        strg.write(longField, LONGS[i]);
        strg.write(shortField, SHORTS[i]);
    }

    public void randomOptionalReads(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(RANDOM_INDEX_ARRAY[i]);
                readOptionalStorage(i, storage);
            }
        }
    }

    public void randomOptionalReadsProxy(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final OptionalFieldsProxy prxy = proxy.getInstance(
                        RANDOM_INDEX_ARRAY[i], OptionalFieldsProxy.class);
                readProxy(prxy, i);
            }
        }
    }

    public void randomOptionalWrites(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(RANDOM_INDEX_ARRAY[count]);
                writeOptionalStorage(count, storage);
                count++;
            }
            if (storage.transactionsEnabled()) {
                readChanges(storage.transactionManager().commit(),
                        storage.rootStruct());
            }
        }
    }

    public void randomOptionalWritesProxy(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final OptionalFieldsProxy prxy = proxy.getInstance(
                        RANDOM_INDEX_ARRAY[count], OptionalFieldsProxy.class);
                writeProxy(count, prxy);
                count++;
            }
            if (proxy.transactionsEnabled()) {
                readChanges(proxy.transactionManager().commit(),
                        proxy.rootStruct());
            }
        }
    }

    public void randomReads(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(RANDOM_INDEX_ARRAY[i]);
                readStorage(i, storage);
            }
        }
    }

    public void randomReadsProxy(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final SimpleProxy prxy = proxy.getInstance(
                        RANDOM_INDEX_ARRAY[i], SimpleProxy.class);
                readProxy(prxy, i);
            }
        }
    }
    public void randomReadsWrapper(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                wrapper.selectStructure(RANDOM_INDEX_ARRAY[i]);
                readStorage(i, wrapper);
            }
        }
    }

    public void randomWrites(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(RANDOM_INDEX_ARRAY[count]);
                writeStorage(count, storage);
                count++;
            }
            if (storage.transactionsEnabled()) {
                readChanges(storage.transactionManager().commit(),
                        storage.rootStruct());
            }
        }
    }

    public void randomWritesProxy(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final SimpleProxy prxy = proxy.getInstance(
                        RANDOM_INDEX_ARRAY[count], SimpleProxy.class);
                writeProxy(count, prxy);
                count++;
            }
            if (proxy.transactionsEnabled()) {
                readChanges(proxy.transactionManager().commit(),
                        proxy.rootStruct());
            }
        }
    }

    public void randomWritesWrapper(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                wrapper.selectStructure(RANDOM_INDEX_ARRAY[count]);
                writeStorage(count, wrapper);
                count++;
            }
            if (wrapper.transactionsEnabled()) {
                readChanges(wrapper.transactionManager().commit(),
                        wrapper.rootStruct());
            }
        }
    }

    public void readBean(final int numberOfElementsAccess, final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final Bean b = beanStorage.instanceAt(i);
                readBean(i, b);
            }
        }
    }

    public void sequentialOptionalReads(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(i);
                readOptionalStorage(i, storage);
            }
        }
    }

    public void sequentialOptionalReadsProxy(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final OptionalFieldsProxy prxy = proxy.getInstance(i,
                        OptionalFieldsProxy.class);
                readProxy(prxy, i);
            }
        }
    }

    public void sequentialOptionalWrites(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(i);
                writeOptionalStorage(count, storage);
                count++;
            }
            if (storage.transactionsEnabled()) {
                readChanges(storage.transactionManager().commit(),
                        storage.rootStruct());
            }
        }
    }

    public void sequentialOptionalWritesProxy(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final OptionalFieldsProxy prxy = proxy.getInstance(i,
                        OptionalFieldsProxy.class);
                writeProxy(count, prxy);
                count++;
            }
            if (proxy.transactionsEnabled()) {
                readChanges(proxy.transactionManager().commit(),
                        proxy.rootStruct());
            }
        }
    }

    public void sequentialReads(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(i);
                readStorage(i, storage);
            }
        }
    }

    public void sequentialReadsProxy(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final SimpleProxy prxy = proxy
                        .getInstance(i, SimpleProxy.class);
                readProxy(prxy, i);
            }
        }
    }

    public void sequentialReadsWrapper(final int numberOfElementsAccess,
            final int iterations) {
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                wrapper.selectStructure(i);
                readStorage(i, wrapper);
            }
        }
    }

    public void sequentialWrites(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                storage.selectStructure(i);
                writeStorage(count, storage);
                count++;
            }
            if (storage.transactionsEnabled()) {
                readChanges(storage.transactionManager().commit(),
                        storage.rootStruct());
            }
        }
    }

    public void sequentialWritesProxy(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final SimpleProxy prxy = proxy
                        .getInstance(i, SimpleProxy.class);
                writeProxy(count, prxy);
                count++;
            }
            if (proxy.transactionsEnabled()) {
                readChanges(proxy.transactionManager().commit(),
                        proxy.rootStruct());
            }
        }
    }

    public void sequentialWritesWrapper(final int numberOfElementsAccess,
            final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                wrapper.selectStructure(i);
                writeStorage(count, wrapper);
                count++;
            }
            if (wrapper.transactionsEnabled()) {
                readChanges(wrapper.transactionManager().commit(),
                        wrapper.rootStruct());
            }
        }
    }

    public void writeBean(final int numberOfElementsAccess, final int iterations) {
        int count = 0;
        for (int j = 0; j < iterations; j++) {
            for (int i = 0; i < numberOfElementsAccess; i++) {
                final Bean b = beanStorage.instanceAt(i);
                writeBean(count, b);
                count++;
            }
            if (beanStorage.transactionsEnabled()) {
                readChanges(beanStorage.commit(), storage.rootStruct());
            }
        }
    }
}
