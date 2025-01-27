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

/** A test for the {@link ByteSerializer}. */
class ByteSerializerTest extends SerializerTestBase<Byte> {

    @Override
    protected TypeSerializer<Byte> createSerializer() {
        return ByteSerializer.INSTANCE;
    }

    @Override
    protected int getLength() {
        return 1;
    }

    @Override
    protected Class<Byte> getTypeClass() {
        return Byte.class;
    }

    @Override
    protected Byte[] getTestData() {
        Random rnd = new Random(874597969123412341L);
        byte[] byteArray = new byte[1];
        rnd.nextBytes(byteArray);

        return new Byte[] {
            Byte.valueOf((byte) 0),
            Byte.valueOf((byte) 1),
            Byte.valueOf((byte) -1),
            Byte.MAX_VALUE,
            Byte.MIN_VALUE,
            Byte.valueOf(byteArray[0]),
            Byte.valueOf((byte) -byteArray[0])
        };
    }
}
