/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ververica.cdc.debezium.event;

import com.ververica.cdc.common.annotation.Internal;
import com.ververica.cdc.common.types.DataType;
import org.apache.kafka.connect.data.Schema;

/** {@link DataType} inference for kafka connect {@link Schema}. */
@Internal
public interface SchemaDataTypeInference {

    /**
     * Infer {@link DataType} from {@link Schema}.
     *
     * @param value the value corresponding value to SCHEMA
     * @param schema the kafka connect schema
     * @return the inferred data type
     */
    DataType infer(Object value, Schema schema);
}
