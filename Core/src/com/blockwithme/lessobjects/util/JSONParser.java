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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.SchemaFormat;
import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.StructInfo;
import com.blockwithme.lessobjects.schema.StructSchema;
import com.blockwithme.lessobjects.schema.StructSchema.JSONViews;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * The JSON Builder utility class.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
public class JSONParser {

    /** The generated json string. */
    private String jsonString;

    /** The struct. */
    private StructSchema schema;

    /** The generated xml string. */
    private String xmlString;

    /** The input json or xml string. */
    private String jsonOrXMLString;

    /** The input format. */
    private SchemaFormat format;

    /** From schema flag, or from jsonOrXMLString ? */
    private final boolean fromSchema;

    /**
     * @return
     */
    @SuppressWarnings("null")
    private static ObjectWriter initMapper() {
        return mapper().writerWithDefaultPrettyPrinter().withView(
                JSONViews.CompleteView.class);
    }

    @SuppressWarnings("null")
    public static ObjectMapper mapper() {
        final ObjectMapper jsonMapper = new ObjectMapper()
                .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
                .setSerializationInclusion(Include.NON_NULL);
        return jsonMapper;
    }

    public JSONParser(final String theJsonOrXMLString,
            final SchemaFormat theFormat) {
        checkNotNull(theJsonOrXMLString);
        checkNotNull(theFormat);
        jsonOrXMLString = theJsonOrXMLString;
        format = theFormat;
        fromSchema = false;
    }

    /** Instantiates a new JSON builder. */
    public JSONParser(final StructSchema theSchema) {
        checkNotNull(theSchema);
        schema = theSchema;
        fromSchema = true;
    }

    @SuppressWarnings("null")
    private StructSchema getBinding() throws IOException {
        checkNotNull(jsonOrXMLString);
        return mapper().readValue(jsonOrXMLString, StructSchema.class);
    }

    @SuppressWarnings("null")
    public Struct struct() {
        if (!fromSchema && schema == null) {
            try {
                schema = getBinding();
            } catch (final IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return schema.binding().toStruct(
                new StructInfo(schema.createdBy(), schema.createdOn(), schema
                        .schemaVersion()));

    }

    /**
     * To json string.
     *
     * @return the string
     */
    @SuppressWarnings("null")
    public String toJSONString() {

        if (jsonString == null) {
            if (fromSchema) {
                try {
                    final ObjectWriter writer = initMapper();
                    jsonString = writer.writeValueAsString(schema);
                } catch (final JsonProcessingException jpe) {
                    throw new IllegalStateException(jpe.getMessage(), jpe);
                }
            } else {
                try {
                    if (format == SchemaFormat.JSON) {
                        jsonString = jsonOrXMLString;
                    } else {
                        if (xmlString == null) {
                            xmlString = jsonOrXMLString;
                        }
                        final XmlMapper xmlMapper = new XmlMapper();
                        schema = xmlMapper.readValue(jsonOrXMLString,
                                StructSchema.class);
                        final ObjectMapper jsonMapper = new ObjectMapper();
                        jsonString = jsonMapper.writeValueAsString(schema);
                    }
                } catch (final IOException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
        return jsonString;
    }

    /**
     * To xml string.
     *
     * @return the string
     */
    @SuppressWarnings("null")
    public String toXMLString() {

        if (xmlString == null) {
            if (!fromSchema) {
                try {
                    final XmlMapper mapper = new XmlMapper();
                    xmlString = mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(schema);
                } catch (final JsonProcessingException jpe) {
                    throw new IllegalStateException(jpe.getMessage(), jpe);
                }
            } else {
                try {
                    if (format == SchemaFormat.JSON) {
                        if (jsonString == null) {
                            jsonString = jsonOrXMLString;
                        }
                        schema = getBinding();
                        xmlString = new XmlMapper()
                                .writerWithDefaultPrettyPrinter()
                                .writeValueAsString(schema);
                    } else {
                        xmlString = jsonOrXMLString;
                    }
                } catch (final IOException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
        return xmlString;
    }
}
