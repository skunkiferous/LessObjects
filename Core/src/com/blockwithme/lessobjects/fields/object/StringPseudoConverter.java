package com.blockwithme.lessobjects.fields.object;

import com.blockwithme.prim.Converter;

/**
 * Every field needs a Converter, even the fields that are not primitives.
 * So StringPseudoConverter serves as Converter for Strings.
 *
 * @author monster
 */
public class StringPseudoConverter implements Converter<String> {
	/** Singleton instance. */
	public static final StringPseudoConverter INSTANCE = new StringPseudoConverter();

	@Override
	public Class<String> type() {
		return String.class;
	}

	@Override
	public int bits() {
		return -1;
	}
}
