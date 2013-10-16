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
// $codepro.audit.disable
package com.blockwithme.lessobjects.juint;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.blockwithme.lessobjects.juint.ser.SerializationTest;

/**
 * Test suite class that combines all JUnit test classes.
 *
 * @author tarung
 */
@RunWith(Suite.class)
@SuiteClasses({ TestChangeListeners.class, TestCommitRollbackChanges.class,
        TestStructFields.class, TestStructGlobalFields.class,
        TestStructOptionalChildren.class, TestStructOptionalField.class,
        TestStructUnion.class, TestStructVariableStorage.class,
        TestListChild.class, TestStructThousandFieldsUnion.class,
        TestStructMiscellaneous.class, TestCopyStruct.class,
        TestCopyStructWithListChildren.class, TestListInsideList.class,
        TestOptionalChildInsideOptionalChild.class,
        TestListInsideOptionalChild.class, TestOptionalChildInsideList.class,
        TestListenersOtherFields.class, TestVirtualFields.class,
        TestMultiDimensionalSupport.class, TestOptionalChildInChild.class,
        TestFieldsInsideOptionalChild.class,
        TestFieldsInsideOptionalChild.class, TestStorageWrapper.class,
        TestCommitRollbackOptionalFields.class, SerializationTest.class })
public class AllTests {
    // no implementation
}
