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
/**
 *
 */
package com.blockwithme.lessobjects.juint.struct;

import java.util.List;

import com.blockwithme.lessobjects.TransactionListener;
import com.blockwithme.lessobjects.ValueChangeListener;
import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.storage.ActionSet;
import com.blockwithme.lessobjects.storage.Storage;

//CHECKSTYLE IGNORE FOR NEXT 10 LINES
@SuppressWarnings("all")
public class TestListener implements ValueChangeListener, TransactionListener {

    /** {@inheritDoc} */
    @Override
    public void onChange(final ChangeInfo theChange, final Storage theSource,
            final List<Object> theResultEvents) {
        final String response = theChange.field().name()
                + theSource.getSelectedStructure();
        theResultEvents.add(response);
    }

    /** {@inheritDoc} */
    @Override
    public void postCommit(final ActionSet theActions, final Storage theSource,
            final boolean isCommit) {
        // NOP
    }

    /** {@inheritDoc} */
    @Override
    public void preCommit(final ActionSet theActions, final Storage theSource,
            final boolean isCommit) {
        // NOP
    }
}
