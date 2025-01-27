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

package com.ververica.cdc.composer.flink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.ververica.cdc.common.annotation.Internal;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Utilities for {@link org.apache.flink.streaming.api.environment.StreamExecutionEnvironment}. */
@Internal
public class FlinkEnvironmentUtils {

    /**
     * Add the specified JAR to {@link StreamExecutionEnvironment} so that the JAR will be uploaded
     * together with the job graph.
     */
    public static void addJar(StreamExecutionEnvironment env, URL jarUrl) {
        try {
            Class<StreamExecutionEnvironment> envClass = StreamExecutionEnvironment.class;
            Field field = envClass.getDeclaredField("configuration");
            field.setAccessible(true);
            Configuration configuration = ((Configuration) field.get(env));
            List<String> jars =
                    configuration.getOptional(PipelineOptions.JARS).orElse(new ArrayList<>());
            jars.add(jarUrl.toString());
            configuration.set(PipelineOptions.JARS, jars);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add JAR to Flink execution environment", e);
        }
    }
}
