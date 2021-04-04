/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.j2cl.common;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.io.File;
import javax.annotation.Nullable;

/**
 * Describes the location of a node in the original source in the form of a range
 * (line,column)-(line,column); where both line and column are zero-based.
 */
@AutoValue
public abstract class SourcePosition implements Comparable<SourcePosition> {

  public abstract FilePosition getStartFilePosition();

  public abstract FilePosition getEndFilePosition();

  public abstract @Nullable String getFilePath();

  public abstract @Nullable String getName();

  @Override
  public int compareTo(SourcePosition o) {
    if (getFilePath() != null) {
      int pathComparisonResult = getFilePath().compareTo(o.getFilePath());
      if (pathComparisonResult != 0) {
        return pathComparisonResult;
      }
    }
    if (getStartFilePosition().getLine() == o.getStartFilePosition().getLine()) {
      return getStartFilePosition().getColumn() - o.getStartFilePosition().getColumn();
    }
    return getStartFilePosition().getLine() - o.getStartFilePosition().getLine();
  }

  @Memoized
  @Nullable
  public String getFileName() {
    String filePath = getFilePath();
    return filePath == null ? filePath : getFileName(filePath);
  }

  abstract Builder toBuilder();

  public static Builder newBuilder() {
    return new AutoValue_SourcePosition.Builder();
  }

  /** A Builder for SourcePosition. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setStartFilePosition(FilePosition filePosition);

    public abstract Builder setEndFilePosition(FilePosition filePosition);

    public abstract Builder setFilePath(String filePath);

    public abstract Builder setName(String name);

    public static Builder from(SourcePosition sourcePosition) {
      return sourcePosition.toBuilder();
    }

    abstract SourcePosition autoBuild();

    public SourcePosition build() {
      return autoBuild();
    }
  }

  /** Returns the file name portion of a path. */
  public static String getFileName(String filePath) {
    // Do not use String.split(File.separator) because the parameter to String.split() is a regex,
    // therefore, for example on windows, the file separator ("\") would need escaping.
    // Conversely, Splitter.on() takes a string parameter literally.
    return Iterables.getLast(Splitter.on(File.separator).split(filePath));
  }
}
