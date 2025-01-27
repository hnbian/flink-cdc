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

package com.ververica.cdc.runtime.serializer;

import org.apache.flink.api.common.typeutils.TypeSerializer;

import java.util.Random;

/** A test for the {@link BooleanSerializer}. */
class BooleanSerializerTest extends SerializerTestBase<Boolean> {

    @Override
    protected TypeSerializer<Boolean> createSerializer() {
        return BooleanSerializer.INSTANCE;
    }

    @Override
    protected int getLength() {
        return 1;
    }

    @Override
    protected Class<Boolean> getTypeClass() {
        return Boolean.class;
    }

    @Override
    protected Boolean[] getTestData() {
        Random rnd = new Random(874597969123412341L);

        return new Boolean[] {true, false, rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean()};
    }
}
