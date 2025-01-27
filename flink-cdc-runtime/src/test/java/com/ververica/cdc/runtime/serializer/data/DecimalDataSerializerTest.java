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

package com.ververica.cdc.runtime.serializer.data;

import com.ververica.cdc.common.data.DecimalData;
import com.ververica.cdc.runtime.serializer.SerializerTestBase;

/** A test for the {@link DecimalDataSerializer}. */
class DecimalDataSerializerTest extends SerializerTestBase<DecimalData> {

    @Override
    protected DecimalDataSerializer createSerializer() {
        return new DecimalDataSerializer(5, 2);
    }

    @Override
    protected int getLength() {
        return -1;
    }

    @Override
    protected Class<DecimalData> getTypeClass() {
        return DecimalData.class;
    }

    @Override
    protected DecimalData[] getTestData() {
        return new DecimalData[] {
            DecimalData.fromUnscaledLong(1, 5, 2),
            DecimalData.fromUnscaledLong(2, 5, 2),
            DecimalData.fromUnscaledLong(3, 5, 2),
            DecimalData.fromUnscaledLong(4, 5, 2)
        };
    }
}
