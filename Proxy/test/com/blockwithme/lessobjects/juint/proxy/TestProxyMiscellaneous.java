/**
 *
 */
// $codepro.audit.disable
package com.blockwithme.lessobjects.juint.proxy;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.beans.ChangeInfo;
import com.blockwithme.lessobjects.compiler.Aligned64Compiler;
import com.blockwithme.lessobjects.juint.beans.DerivedFrmProxyChildrenArray;
import com.blockwithme.lessobjects.juint.beans.HasProxyChildrenArrayChild;
import com.blockwithme.lessobjects.juint.beans.ListChild;
import com.blockwithme.lessobjects.juint.beans.Proxy2;
import com.blockwithme.lessobjects.juint.beans.ProxyChildrenArray;
import com.blockwithme.lessobjects.juint.beans.TestObject;
import com.blockwithme.lessobjects.proxy.ProxyCursor;
import com.blockwithme.lessobjects.proxy.ProxyTransactionListener;
import com.blockwithme.lessobjects.proxy.ProxyValueChangeListener;
import com.blockwithme.lessobjects.storage.ActionSet;

//CHECKSTYLE IGNORE FOR NEXT 400 LINES
/** The Class TestCoreClasses. */
@SuppressWarnings({ "PMD", "all" })
public class TestProxyMiscellaneous extends TestData {

    private static class ChangeListenerImpl implements
            ProxyValueChangeListener, ProxyTransactionListener {

        int onChangeInvoked = 0;

        int postCommitInvoked = 0;

        int preCommitInvoked = 0;

        @Override
        public void onChange(final ChangeInfo theChange,
                final ProxyCursor theSource, final List<Object> theResultEvents) {
            onChangeInvoked++;
        }

        @Override
        public void postCommit(final ActionSet theActions,
                final ProxyCursor theSource, final boolean isCommit) {
            postCommitInvoked++;
        }

        @Override
        public void preCommit(final ActionSet theActions,
                final ProxyCursor theSource, final boolean isCommit) {
            preCommitInvoked++;
        }
    }

    @Test
    public void testDerivedFrmProxyChildrenArray() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final ProxyCursor proxy = new ProxyCursor(
                    new Class<?>[] { DerivedFrmProxyChildrenArray.class },
                    cmplr, 10, registry);
            final DerivedFrmProxyChildrenArray p = proxy.getInstance(0,
                    DerivedFrmProxyChildrenArray.class);
        }
    }

    @Test
    public void testHasProxyChildrenArrayChild() {

        for (final Compiler cmplr : Constants.COMPILERS) {
            final ProxyCursor proxy = new ProxyCursor(
                    new Class<?>[] { HasProxyChildrenArrayChild.class }, cmplr,
                    10);
            final HasProxyChildrenArrayChild p = proxy.getInstance(0,
                    HasProxyChildrenArrayChild.class);
        }
    }

    @Test
    public void testListenersOnProxy() {
        final ProxyCursor proxy = new ProxyCursor(
                new Class<?>[] { Proxy2.class }, new Aligned64Compiler(), 10);

        final Proxy2 p = proxy.getInstance(0, Proxy2.class);
        final ChangeListenerImpl parentListner = new ChangeListenerImpl();
        p.addListener("simpleObjectArray", parentListner);

        final ChangeListenerImpl childListner = new ChangeListenerImpl();
        p.addListener("list", childListner);

        // global listener
        final ChangeListenerImpl globalListener = new ChangeListenerImpl();
        p.addListener(globalListener);

        // pre-commit listener
        final ChangeListenerImpl preCommitListener = new ChangeListenerImpl();
        p.addPreCommitListener(preCommitListener);

        // post-commit listener
        final ChangeListenerImpl postCommitListener = new ChangeListenerImpl();
        p.addPostCommitListener(postCommitListener);

        final TestObject[] array = new TestObject[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = new TestObject("test" + i);
        }
        p.setSimpleObjectArray(array);
        p.setByteField((byte) 123);

        final Iterator<ListChild> children = p.getList(10);
        int count = 1;

        while (children.hasNext()) {
            final ListChild child = children.next();
            child.setIntField(count++);
            child.setLongField(count++);
        }

        assertEquals(10, parentListner.onChangeInvoked);
        assertEquals(20, childListner.onChangeInvoked);
        proxy.transactionManager().commit();
        assertEquals(1, preCommitListener.preCommitInvoked);
        assertEquals(1, postCommitListener.postCommitInvoked);

    }

    @Test
    public void testProxyWithChildrenArray() {
        for (final Compiler cmplr : Constants.COMPILERS) {
            final ProxyCursor proxy = new ProxyCursor(
                    new Class<?>[] { ProxyChildrenArray.class }, cmplr, 10);
            final ProxyChildrenArray p = proxy.getInstance(0,
                    ProxyChildrenArray.class);
        }
    }
}
