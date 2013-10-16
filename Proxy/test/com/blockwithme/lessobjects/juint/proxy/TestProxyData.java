/**
 *
 */
package com.blockwithme.lessobjects.juint.proxy;

import com.blockwithme.lessobjects.juint.beans.TestObject;

// CHECKSTYLE IGNORE FOR NEXT 600 LINES
@SuppressWarnings({ "PMD", "all" })
public class TestProxyData extends TestData {

    protected TestObject[] objects() {

        final TestObject[] objects = new TestObject[_CAPACITY];
        final String[] strings = strings();
        for (int i = 0; i < _CAPACITY; i++) {
            objects[i] = new TestObject(strings[i]);
        }
        return objects;
    }

}