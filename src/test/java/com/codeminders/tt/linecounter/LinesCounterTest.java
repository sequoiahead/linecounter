package com.codeminders.tt.linecounter;

import static com.codeminders.tt.linecounter.Utils.getResource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinesCounterTest {

  private final LinesCounter lineCounter = new LinesCounter();

  @Test
  public void shouldReturnZero_whenNonJavaFileEncountered() {
    Long count = lineCounter.countLines(getResource("lineCountingService/CasePlainText.txt"));
    Assertions.assertEquals(0, count);
  }

  @Test
  public void shouldReturnCorrectCount_whenJavaFileEncountered() {
    Long count = lineCounter.countLines(getResource("lineCountingService/CaseSimple.java"));
    Assertions.assertEquals(3, count);
  }

  @Test
  public void shouldReturnCorrectCount_whenTrickyCommentsInJavaFileEncountered() {
    Long count = lineCounter.countLines(getResource("lineCountingService/CaseTrickyComments.java"));
    Assertions.assertEquals(5, count);
  }

  @Test
  public void shouldReturnCorrectCount_whenJavadocInJavaFileEncountered() {
    Long count = lineCounter.countLines(getResource("lineCountingService/CaseJavadoc.java"));
    Assertions.assertEquals(7, count);
  }

  @Test
  public void shouldReturnCorrectCount_whenCommentsBlockInsideStringEncountered() {
    Long count = lineCounter
        .countLines(getResource("lineCountingService/CaseCommentBlockInsideAString.java"));
    Assertions.assertEquals(17, count);
  }

}
