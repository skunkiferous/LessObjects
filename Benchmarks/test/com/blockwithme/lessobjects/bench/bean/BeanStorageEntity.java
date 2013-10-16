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
package com.blockwithme.lessobjects.bench.bean;

import java.util.List;

import com.blockwithme.lessobjects.beans.ValueChange;

//CHECKSTYLE IGNORE FOR NEXT 50 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public interface BeanStorageEntity extends Cloneable {

    public Object clone() throws CloneNotSupportedException;

    public void processChanges(final int elementIndex,
            final BeanStorageEntity initialState,
            final List<ValueChange<?>> changeList);
}
