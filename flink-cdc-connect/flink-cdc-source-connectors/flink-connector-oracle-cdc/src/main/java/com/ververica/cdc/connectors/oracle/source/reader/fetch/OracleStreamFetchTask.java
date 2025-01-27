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

package com.ververica.cdc.connectors.oracle.source.reader.fetch;

import com.ververica.cdc.common.annotation.Internal;
import com.ververica.cdc.connectors.base.relational.JdbcSourceEventDispatcher;
import com.ververica.cdc.connectors.base.source.meta.split.SourceSplitBase;
import com.ververica.cdc.connectors.base.source.meta.split.StreamSplit;
import com.ververica.cdc.connectors.base.source.reader.external.FetchTask;
import io.debezium.config.Configuration;
import io.debezium.connector.oracle.OracleConnection;
import io.debezium.connector.oracle.OracleConnectorConfig;
import io.debezium.connector.oracle.OracleDatabaseSchema;
import io.debezium.connector.oracle.OracleOffsetContext;
import io.debezium.connector.oracle.OraclePartition;
import io.debezium.connector.oracle.OracleStreamingChangeEventSourceMetrics;
import io.debezium.connector.oracle.logminer.LogMinerStreamingChangeEventSource;
import io.debezium.connector.oracle.logminer.processor.LogMinerEventProcessor;
import io.debezium.pipeline.ErrorHandler;
import io.debezium.util.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The task to work for fetching data of Oracle table stream split. */
@Internal
public class OracleStreamFetchTask implements FetchTask<SourceSplitBase> {

    private final StreamSplit split;
    private volatile boolean taskRunning = false;

    public OracleStreamFetchTask(StreamSplit split) {
        this.split = split;
    }

    @Override
    public void execute(Context context) throws Exception {
        OracleSourceFetchTaskContext sourceFetchContext = (OracleSourceFetchTaskContext) context;
        taskRunning = true;
        RedoLogSplitReadTask redoLogSplitReadTask =
                new RedoLogSplitReadTask(
                        sourceFetchContext.getDbzConnectorConfig(),
                        sourceFetchContext.getConnection(),
                        sourceFetchContext.getDispatcher(),
                        sourceFetchContext.getErrorHandler(),
                        sourceFetchContext.getDatabaseSchema(),
                        sourceFetchContext.getSourceConfig().getOriginDbzConnectorConfig(),
                        sourceFetchContext.getStreamingChangeEventSourceMetrics(),
                        split);
        StoppableChangeEventSourceContext changeEventSourceContext =
                new StoppableChangeEventSourceContext();
        redoLogSplitReadTask.execute(
                changeEventSourceContext,
                sourceFetchContext.getPartition(),
                sourceFetchContext.getOffsetContext());
    }

    @Override
    public boolean isRunning() {
        return taskRunning;
    }

    @Override
    public StreamSplit getSplit() {
        return split;
    }

    @Override
    public void close() {
        taskRunning = false;
    }

    /**
     * A wrapped task to read all redo log for table and also supports read bounded (from
     * lowWatermark to highWatermark) redo log.
     */
    public static class RedoLogSplitReadTask extends LogMinerStreamingChangeEventSource {

        private static final Logger LOG = LoggerFactory.getLogger(RedoLogSplitReadTask.class);
        private final StreamSplit redoLogSplit;
        private final JdbcSourceEventDispatcher<OraclePartition> dispatcher;
        private final ErrorHandler errorHandler;
        private ChangeEventSourceContext context;
        private final OracleConnectorConfig connectorConfig;
        private final OracleConnection connection;

        private final OracleDatabaseSchema schema;

        private final OracleStreamingChangeEventSourceMetrics metrics;

        public RedoLogSplitReadTask(
                OracleConnectorConfig connectorConfig,
                OracleConnection connection,
                JdbcSourceEventDispatcher<OraclePartition> dispatcher,
                ErrorHandler errorHandler,
                OracleDatabaseSchema schema,
                Configuration jdbcConfig,
                OracleStreamingChangeEventSourceMetrics metrics,
                StreamSplit redoLogSplit) {
            super(
                    connectorConfig,
                    connection,
                    dispatcher,
                    errorHandler,
                    Clock.SYSTEM,
                    schema,
                    jdbcConfig,
                    metrics);
            this.redoLogSplit = redoLogSplit;
            this.dispatcher = dispatcher;
            this.errorHandler = errorHandler;
            this.connectorConfig = connectorConfig;
            this.connection = connection;
            this.metrics = metrics;
            this.schema = schema;
        }

        @Override
        public void execute(
                ChangeEventSourceContext context,
                OraclePartition partition,
                OracleOffsetContext offsetContext) {
            this.context = context;
            super.execute(context, partition, offsetContext);
        }

        /**
         * Delegate {@link EventProcessorFactory} to produce a LogMinerEventProcessor with enhanced
         * processRow method to distinguish whether is bounded.
         */
        @Override
        protected LogMinerEventProcessor createProcessor(
                ChangeEventSourceContext context,
                OraclePartition partition,
                OracleOffsetContext offsetContext) {
            return EventProcessorFactory.createProcessor(
                    context,
                    connectorConfig,
                    connection,
                    dispatcher,
                    partition,
                    offsetContext,
                    schema,
                    metrics,
                    errorHandler,
                    redoLogSplit);
        }
    }
}
