/*
 * Copyright (C) 2013 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blockwithme.lessobjects.storage.serialization;

//CHECKSTYLE.OFF: IllegalType
import com.blockwithme.lessobjects.storage.ChangeStorage;
import com.blockwithme.lessobjects.storage.packed.PackedSparseStorage;
import com.blockwithme.msgpack.templates.AbstractTemplate;

/**
 * This class contains templates for HPPC classes.
 *
 * @author tarung
 */
public class StructTemplates {

    /** The all HPPC templates. */
    public static AbstractTemplate<?>[] templates() {
        return new AbstractTemplate<?>[] { new IntByteOpenHashMapTemplate(),
                new IntCharOpenHashMapTemplate(),
                new IntDoubleOpenHashMapTemplate(),
                new IntFloatOpenHashMapTemplate(),
                new IntIntOpenHashMapTemplate(),
                new IntLongOpenHashMapTemplate(),
                new IntObjectMapTemplate(),
                new IntOpenHashSetTemplate(),
                new IntShortOpenHashMapTemplate(),
                new IntStackTemplate(),

                new StructTemplate(),
                new Aligned64StorageTemplate(),
                new Aligned64CompositeStorageTemplate(),
                new Aligned64SparseStorageTemplate(),
                new PackedCompositeStorageTemplate(),
                new PackedSparseStorageTemplate<>(PackedSparseStorage.class),
                new PackedSparseStorageTemplate<>(ChangeStorage.class),
                new PackedStorageTemplate(), new ChangeRecordsTemplate() };
    }
}
