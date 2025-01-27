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

package com.ververica.cdc.common.data.binary;

import org.apache.flink.core.memory.MemorySegment;

import com.ververica.cdc.common.annotation.Internal;
import com.ververica.cdc.common.utils.Preconditions;

/** A basic implementation of {@link BinaryFormat} which describe a section of memory. */
@Internal
public class BinarySection implements BinaryFormat {

    protected MemorySegment[] segments;
    protected int offset;
    protected int sizeInBytes;

    public BinarySection() {}

    public BinarySection(MemorySegment[] segments, int offset, int sizeInBytes) {
        Preconditions.checkArgument(segments != null);
        this.segments = segments;
        this.offset = offset;
        this.sizeInBytes = sizeInBytes;
    }

    public final void pointTo(MemorySegment segment, int offset, int sizeInBytes) {
        pointTo(new MemorySegment[] {segment}, offset, sizeInBytes);
    }

    public void pointTo(MemorySegment[] segments, int offset, int sizeInBytes) {
        Preconditions.checkArgument(segments != null);
        this.segments = segments;
        this.offset = offset;
        this.sizeInBytes = sizeInBytes;
    }

    public MemorySegment[] getSegments() {
        return segments;
    }

    public int getOffset() {
        return offset;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BinarySection that = (BinarySection) o;
        return sizeInBytes == that.sizeInBytes
                && BinarySegmentUtils.equals(
                        segments, offset, that.segments, that.offset, sizeInBytes);
    }

    @Override
    public int hashCode() {
        return BinarySegmentUtils.hash(segments, offset, sizeInBytes);
    }
}
