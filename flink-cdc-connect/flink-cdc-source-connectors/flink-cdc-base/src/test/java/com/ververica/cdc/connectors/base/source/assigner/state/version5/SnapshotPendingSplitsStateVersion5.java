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

package com.ververica.cdc.connectors.base.source.assigner.state.version5;

import com.ververica.cdc.connectors.base.source.assigner.state.PendingSplitsState;
import com.ververica.cdc.connectors.base.source.enumerator.IncrementalSourceEnumerator;
import com.ververica.cdc.connectors.base.source.meta.offset.Offset;
import com.ververica.cdc.connectors.base.source.meta.split.SchemalessSnapshotSplit;
import com.ververica.cdc.connectors.base.source.reader.IncrementalSourceSplitReader;
import io.debezium.relational.TableId;
import io.debezium.relational.history.TableChanges;

import java.util.List;
import java.util.Map;

/**
 * The 5th version of PendingSplitsStateSerializer. The modification of the 6th version: Change
 * isAssignerFinished(boolean) to assignStatus in SnapshotPendingSplitsState to represent a more
 * comprehensive assignment status.
 */
public class SnapshotPendingSplitsStateVersion5 extends PendingSplitsState {

    /** The tables in the checkpoint. */
    private final List<TableId> remainingTables;

    /**
     * The paths that are no longer in the enumerator checkpoint, but have been processed before and
     * should this be ignored. Relevant only for sources in continuous monitoring mode.
     */
    private final List<TableId> alreadyProcessedTables;

    /** The splits in the checkpoint. */
    private final List<SchemalessSnapshotSplit> remainingSplits;

    /**
     * The snapshot splits that the {@link IncrementalSourceEnumerator} has assigned to {@link
     * IncrementalSourceSplitReader}s.
     */
    private final Map<String, SchemalessSnapshotSplit> assignedSplits;

    /**
     * The offsets of finished (snapshot) splits that the {@link IncrementalSourceEnumerator} has
     * received from {@link IncrementalSourceSplitReader}s.
     */
    private final Map<String, Offset> splitFinishedOffsets;

    /**
     * Whether the snapshot split assigner is finished, which indicates there is no more splits and
     * all records of splits have been completely processed in the pipeline.
     */
    private final boolean isAssignerFinished;

    /** Whether the table identifier is case sensitive. */
    private final boolean isTableIdCaseSensitive;

    /** Whether the remaining tables are keep when snapshot state. */
    private final boolean isRemainingTablesCheckpointed;

    private final Map<TableId, TableChanges.TableChange> tableSchemas;

    public SnapshotPendingSplitsStateVersion5(
            List<TableId> alreadyProcessedTables,
            List<SchemalessSnapshotSplit> remainingSplits,
            Map<String, SchemalessSnapshotSplit> assignedSplits,
            Map<TableId, TableChanges.TableChange> tableSchemas,
            Map<String, Offset> splitFinishedOffsets,
            boolean isAssignerFinished,
            List<TableId> remainingTables,
            boolean isTableIdCaseSensitive,
            boolean isRemainingTablesCheckpointed) {
        this.alreadyProcessedTables = alreadyProcessedTables;
        this.remainingSplits = remainingSplits;
        this.assignedSplits = assignedSplits;
        this.splitFinishedOffsets = splitFinishedOffsets;
        this.isAssignerFinished = isAssignerFinished;
        this.remainingTables = remainingTables;
        this.isTableIdCaseSensitive = isTableIdCaseSensitive;
        this.isRemainingTablesCheckpointed = isRemainingTablesCheckpointed;
        this.tableSchemas = tableSchemas;
    }

    public List<TableId> getAlreadyProcessedTables() {
        return alreadyProcessedTables;
    }

    public List<SchemalessSnapshotSplit> getRemainingSplits() {
        return remainingSplits;
    }

    public Map<String, SchemalessSnapshotSplit> getAssignedSplits() {
        return assignedSplits;
    }

    public Map<TableId, TableChanges.TableChange> getTableSchemas() {
        return tableSchemas;
    }

    public Map<String, Offset> getSplitFinishedOffsets() {
        return splitFinishedOffsets;
    }

    public boolean isAssignerFinished() {
        return isAssignerFinished;
    }

    public List<TableId> getRemainingTables() {
        return remainingTables;
    }

    public boolean isTableIdCaseSensitive() {
        return isTableIdCaseSensitive;
    }

    public boolean isRemainingTablesCheckpointed() {
        return isRemainingTablesCheckpointed;
    }
}
