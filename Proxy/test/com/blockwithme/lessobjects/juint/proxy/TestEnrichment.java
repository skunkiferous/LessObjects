/**
 *
 */
package com.blockwithme.lessobjects.juint.proxy;

import static com.blockwithme.lessobjects.juint.proxy.Constants.COMPILERS;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.blockwithme.lessobjects.Compiler;
import com.blockwithme.lessobjects.juint.beans.EnrichProxy;
import com.blockwithme.lessobjects.juint.beans.EnrichProxyChild;
import com.blockwithme.lessobjects.juint.beans.ProxyWithChild;
import com.blockwithme.lessobjects.proxy.ProxyCursor;

//CHECKSTYLE IGNORE FOR NEXT 200 LINES
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "all")
@SuppressWarnings("all")
public class TestEnrichment {
    /** The Constant CAPACITY. */
    private static final int _CAPACITY = 10;

    private CompiledProxy[] proxies;

    @Before
    public void setUp() {
        proxies = new CompiledProxy[COMPILERS.length];
        int count = 0;
        for (final Compiler cmplr : COMPILERS) {
            proxies[count] = new CompiledProxy();
            proxies[count].cursor = new ProxyCursor(new Class<?>[] {
                    ProxyWithChild.class, EnrichProxyChild.class,
                    EnrichProxy.class }, cmplr, _CAPACITY);
            proxies[count].compiler = cmplr;
            count++;
        }
    }

    @Test
    public void testEnrichChild() {
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestEnrichment.testEnrichment() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithChild b = proxy.cursor.getInstance(i,
                        ProxyWithChild.class);
                final EnrichProxyChild enriched = (EnrichProxyChild) b
                        .getChild1();
                enriched.setEnrichedInt(i);
                final EnrichProxyChild enriched2 = (EnrichProxyChild) b
                        .getSecond();
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithChild b = proxy.cursor.getInstance(i,
                        ProxyWithChild.class);
                final EnrichProxyChild enriched = (EnrichProxyChild) b
                        .getChild1();
                assertEquals(message, i, enriched.getEnrichedInt());
            }
        }
    }

    @Test
    public void testEnrichProxy() {
        for (final CompiledProxy proxy : proxies) {
            final String message = "TestEnrichment.testEnrichment() : Test failed for Compiler -"
                    + proxy.compiler.compilerName();

            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithChild b = proxy.cursor.getInstance(i,
                        ProxyWithChild.class);
                final EnrichProxy enriched = (EnrichProxy) b;
                enriched.setEnrichedByte(i);
            }
            for (int i = 0; i < _CAPACITY; i++) {
                final ProxyWithChild b = proxy.cursor.getInstance(i,
                        ProxyWithChild.class);
                final EnrichProxy enriched = (EnrichProxy) b;
                assertEquals(message, i, enriched.getEnrichedByte());
            }
        }
    }
}
