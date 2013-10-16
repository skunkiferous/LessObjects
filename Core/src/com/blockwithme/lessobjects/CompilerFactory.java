/**
 *
 */
package com.blockwithme.lessobjects;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A factory class to get Compiler objects.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public interface CompilerFactory {

    /**
     * Creates a new Compiler object.
     *
     * @param theCompilerType the compiler type
     * @return the compiler
     */
    public Compiler createCompiler(final CompilerType theCompilerType);

}
