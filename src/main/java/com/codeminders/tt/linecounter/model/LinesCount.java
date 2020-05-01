package com.codeminders.tt.linecounter.model;

import java.nio.file.Path;

/**
 * Data holder object class
 */
public class LinesCount {

  private final Path path;
  private Long linesCount;

  public Path getPath() {
    return path;
  }

  public Long getLinesCount() {
    return linesCount;
  }

  public boolean isDirectory() {
    return path.toFile().isDirectory();
  }

  //package protected to avoid mutations by irrelevant classes
  LinesCount(Path filename, Long linesCount) {
    this.path = filename;
    this.linesCount = linesCount;
  }

  void setLinesCount(Long linesCount) {
    this.linesCount = linesCount;
  }
}
