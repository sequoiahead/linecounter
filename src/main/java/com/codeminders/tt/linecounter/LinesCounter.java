package com.codeminders.tt.linecounter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinesCounter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LinesCounter.class);

  private final static Pattern blockCommentsPattern = Pattern
      .compile("/\\*.*?\\*/", Pattern.DOTALL);

  public Long countLines(File input) {
    if (!isJavaFile(input)) {
      LOGGER.debug("Skipping {} as this is not a Java file", input.toPath());
      return 0L;
    }
    String fileContents;
    try {
      fileContents = new String(Files.readAllBytes(input.toPath()));
    } catch (IOException e) {
      LOGGER.error("Failed to read file {}", input.toPath());
      return 0L;
    }

    fileContents = removeBlockComments(fileContents);
    LOGGER.debug("Removed block comments: {}", fileContents);

    return Arrays.stream(fileContents.split(System.lineSeparator()))
        .map(StringUtils::strip)
        .peek(l -> LOGGER.debug("Stripped string: '{}'", l))
        .filter(StringUtils::isNotBlank)
        .filter(l -> !l.startsWith("//"))
        .count();
  }

  private String removeBlockComments(String input) {
    return blockCommentsPattern.matcher(input).replaceAll("");
  }

  private boolean isJavaFile(File file) {
    String filename = file.getName();
    return file.isFile() && filename.substring(filename.lastIndexOf(".") + 1).equals("java");
  }

}