/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.goodwill.store;

import com.ning.metrics.goodwill.access.GoodwillSchema;
import com.ning.metrics.goodwill.sink.GoodwillSink;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public abstract class GoodwillStore
{
    private static Logger log = Logger.getLogger(GoodwillStore.class);

    final ObjectMapper mapper = new ObjectMapper();

    protected GoodwillSink sink;

    protected Map<String, GoodwillSchema> goodwillSchemata;

    /**
     * Given a Thrift name, find it in the store
     *
     * @param typeName name of the Thrift to search
     * @return the ThriftType if found, null otherwise
     */
    public GoodwillSchema findByName(String typeName)
    {
        try {
            for (GoodwillSchema schema : getTypes()) {
                if (schema.getName().equals(typeName)) {
                    return schema;
                }
            }
        }
        catch (IOException e) {
            log.warn("Unable to fetch Thrift types", e);
        }

        return null;
    }

    /**
     * Serialize all Thrifts in the store in JSON format
     *
     * @return JSONArray representation
     * @throws IOException for serialization issues
     */
    public ByteArrayOutputStream toJSON() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, this);
        return out;
    }

    public void setSink(GoodwillSink sink)
    {
        this.sink = sink;
    }

    public abstract Collection<GoodwillSchema> getTypes() throws IOException;

    /**
     * Add a new type to the store
     *
     * @param schema GoodwillSchema to add
     */
    public abstract void addType(GoodwillSchema schema);

    /**
     * Update a type to the store
     *
     * @param schema GoodwillSchema to update
     * @return true is success, false otherwise
     */
    public abstract boolean updateType(GoodwillSchema schema);

    /**
     * Delete a type
     *
     * @param schema GoodwillSchema to delete
     * @return true is success, false otherwise
     */
    public abstract boolean deleteType(GoodwillSchema schema);
}
