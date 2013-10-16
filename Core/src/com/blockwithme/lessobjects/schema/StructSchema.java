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
package com.blockwithme.lessobjects.schema;

import static com.blockwithme.lessobjects.util.JSONParser.mapper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blockwithme.lessobjects.Struct;
import com.blockwithme.lessobjects.StructInfo;
import com.blockwithme.murmur.MurmurHash;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * The Struct Schema binding class used to generate
 * struct schema and its meta data.
 *
 * @author tarung
 */
@ParametersAreNonnullByDefault
@JsonPropertyOrder(alphabetic = true)
public class StructSchema {
    /**
     * We need two different Json views :<br>
     *  <li>The CompleteView when the properties like defaultValues, optional are included. </li>
     *  <li>The SignatureView where properties needed to generate signature are included. </li>
     */
    public static class JSONViews {
        /** The Class CompleteView. */
        public static class CompleteView {
            // NOP
        }

        /** The Class SignatureView. */
        public static class SignatureView {
            // NOP
        }
    }

    /** The Constant default created by. */
    public static final String DEFAULT_CREATED_BY = "System";

    /** The Constant default schema version. */
    public static final String DEFAULT_SCHEMA_VERSION = "0.0.0";

    /** The Constant json version. */
    public static final String JSON_VERSION = "1.0.0";

    /** The binding. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private StructBinding binding;

    /** The created by. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private String createdBy;

    /** The create on. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private Date createdOn;

    // JSON string along with murmur hash is used to calculate a property called
    // fullHash which is currently used for equality check, (probability of 64
    // bit non-unique values is very less)
    /** The full hash. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private long fullHash;

    /** The json version. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private String jsonVersion;

    /** The name. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private String name;

    /** The json version. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private String schemaVersion;

    /** The signature. */
    @JsonProperty
    @JsonView(JSONViews.CompleteView.class)
    private long signature;

    /** The struct. */
    @JsonIgnore
    private Struct struct;

    /**
     * Instantiates a new struct envelope.
     */
    public StructSchema() {
        // NOP
    }

    /** Instantiates a new struct envelope from Struct object. */
    @SuppressWarnings("null")
    public StructSchema(final Struct theStruct) {
        checkNotNull(theStruct);
        final StructInfo structInfo = theStruct.structInfo();
        if (structInfo != null) {
            createdBy = structInfo.createdBy();
            createdOn = structInfo.createdOn();
            schemaVersion = structInfo.schemaVersion();
        } else {
            createdBy = DEFAULT_CREATED_BY;
            createdOn = new Date();
            schemaVersion = DEFAULT_SCHEMA_VERSION;
        }
        jsonVersion = JSON_VERSION;
        struct = theStruct;
        binding = new StructBinding(struct);
        try {
            final ObjectMapper mapper = mapper();
            final ObjectWriter writer = mapper
                    .writerWithView(JSONViews.SignatureView.class);
            final String signatureStr = writer.writeValueAsString(binding);
            final ObjectWriter writer2 = mapper
                    .writerWithView(JSONViews.CompleteView.class);
            final String fullString = writer2.writeValueAsString(binding);
            signature = MurmurHash.hash64(signatureStr);
            fullHash = MurmurHash.hash64(fullString);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /** The struct binding. */
    @SuppressWarnings("null")
    public StructBinding binding() {
        return binding;
    }

    /** Create by. */
    @SuppressWarnings("null")
    public String createdBy() {
        return createdBy;
    }

    /** Create on. */
    @SuppressWarnings("null")
    public Date createdOn() {
        return createdOn;
    }

    /** The full has of the json String. */
    @SuppressWarnings("null")
    public long fullHash() {
        return fullHash;
    }

    /** Json version. */
    @SuppressWarnings("null")
    public String jsonVersion() {
        return jsonVersion;
    }

    /** The Schema name. */
    @SuppressWarnings("null")
    public String name() {
        return name;
    }

    /** The Schema Version. */
    @SuppressWarnings("null")
    public String schemaVersion() {
        return schemaVersion;
    }

    /** The signature. */
    @SuppressWarnings("null")
    public long signature() {
        return signature;
    }

    /** The struct. */
    @SuppressWarnings("null")
    public Struct struct() {
        return struct;
    }

}
