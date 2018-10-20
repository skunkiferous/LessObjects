/**
 *
 */
package com.blockwithme.lessobjects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.util.CompilerFactoryImpl;
import com.blockwithme.lessobjects.util.FieldFactoryImpl;

/**
 * The Factory class to get instances of required public interfaces.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class Factory {

    /** The compiler factory static handle */
    private static final CompilerFactoryImpl COMPILER_FACTORY = new CompilerFactoryImpl();

    /** The field factory static handle */
    private static final FieldFactoryImpl FIELD_FACTORY = new FieldFactoryImpl();

    /**
     * To get an instance of compiler factory {@link CompilerFactory}.
     *
     * @return the compiler factory
     */
    @SuppressWarnings("null")
    public static CompilerFactory compilerFactory() {
        return COMPILER_FACTORY;
    }

    /**
     * To get an instance of field factory {@link FieldFactory}
     *
     * @return the field factory
     */
    @SuppressWarnings("null")
    public static FieldFactory fieldFactory() {
        return FIELD_FACTORY;
    }

}
