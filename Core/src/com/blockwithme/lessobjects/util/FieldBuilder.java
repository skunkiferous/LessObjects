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
package com.blockwithme.lessobjects.util;

import com.blockwithme.lessobjects.FieldCategory;
import com.blockwithme.lessobjects.FieldType;
import com.blockwithme.prim.Converter;

/**
 * A Builder class can be passed to FieldFactory to create a Field Object.
 *
 * @author tarung
 */
public class FieldBuilder {

    /** The size of this field. */
    private int _bits;

    /** The converter. */
    private Converter<?> _converter;

    /** The mapper. */
    private Object _mapper;

    /** The converter registry. */
    private Object _converterRegistry;

    /** The field category. */
    private FieldCategory _fieldCategory;

    /** The field Name */
    private String _fieldName;

    /** The field type. */
    private FieldType _fieldType;

    /** The from converter registry. */
    private boolean _fromConverterRegistry;

    /** Is optional ? */
    private boolean _optional;

    /** The is Compiled ? */
    private boolean compiled;

    /** The field Name */
    private int index;

    /** The bit offset into that structure. */
    private int offset;

    /** Gets the bits. */
    public int getBits() {
        return _bits;
    }

    /** The converter. */
    public Converter<?> getConverter() {
        return _converter;
    }

    /** The mapper. */
    public Object getMapper() {
        return _mapper;
    }

    /** The converter registry. */
    public Object getConverterRegistry() {
        return _converterRegistry;
    }

    /** The field category. */
    public FieldCategory getFieldCategory() {
        return _fieldCategory;
    }

    /** Gets the field name. */
    public String getFieldName() {
        return _fieldName;
    }

    /** The field type. */
    public FieldType getFieldType() {
        return _fieldType;
    }

    /** The field index */
    public int getIndex() {
        return index;
    }

    /** Gets the offset. */
    public int getOffset() {
        return offset;
    }

    /** Checks if the field is compiled. */
    public boolean isCompiled() {
        return compiled;
    }

    /** The from converter registry. */
    public boolean isFromConverterRegistry() {
        return _fromConverterRegistry;
    }

    /** Checks if field is optional. */
    public boolean isOptional() {
        return _optional;
    }

    /** Sets the bits. */
    public void setBits(final int theBits) {
        _bits = theBits;
    }

    /** Sets the compiled flag. */
    public void setCompiled(final boolean theCompiled) {
        compiled = theCompiled;
    }

    /** The converter. */
    public void setConverter(final Converter<?> theConverter) {
        _converter = theConverter;
    }

    /** The mapper. */
    public void setMapper(final Object theMapper) {
        _mapper = theMapper;
    }

    /** The converter registry. */
    public void setConverterRegistry(final Object theConverterRegistry) {
        _converterRegistry = theConverterRegistry;
    }

    /** The field category. */
    public void setFieldCategory(final FieldCategory theFieldCategory) {
        _fieldCategory = theFieldCategory;
        _optional = theFieldCategory == FieldCategory.OPTIONAL;
    }

    /** Sets the field name. */
    public void setFieldName(final String theFieldName) {
        _fieldName = theFieldName;
    }

    /** The field type. */
    public void setFieldType(final FieldType theFieldType) {
        _fieldType = theFieldType;
    }

    /** The from converter registry. */
    public void setFromConverterRegistry(final boolean isConverterRegistry) {
        _fromConverterRegistry = isConverterRegistry;
    }

    /** The field index */
    public void setIndex(final int index) {
        this.index = index;
    }

    /** Sets the offset. */
    public void setOffset(final int theOffset) {
        offset = theOffset;
    }

    /** Sets the optional flag. */
    public void setOptional(final boolean theOptional) {
        _optional = theOptional;
    }
}
