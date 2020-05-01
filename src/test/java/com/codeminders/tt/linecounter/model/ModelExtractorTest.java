package com.codeminders.tt.linecounter.model;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.INFO;
import static com.codeminders.tt.linecounter.Utils.getResource;

import ch.qos.logback.classic.Logger;
import com.codeminders.tt.linecounter.LinesCounter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class ModelExtractorTest {

  private static ModelExtractor extractor = new ModelExtractor(new LinesCounter());

  @BeforeAll
  public static void beforeClass() {
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(INFO);
    Logger current = (Logger) LoggerFactory.getLogger(ModelExtractor.class);
    current.setLevel(DEBUG);
  }

  @Test
  public void shouldReturnCorrectCount_whenSingleJavaFileEncountered() {
    Tree model = extractor.extractModel(getResource("modelExtractor/casePlainDir/CaseSimple.java"));
    Assertions.assertEquals(3L, model.getData().getLinesCount());
  }

  @Test
  public void shouldReturnCorrectCount_whenSingleLevelDirEncountered() {
    Tree model = extractor.extractModel(getResource("modelExtractor/casePlainDir/"));
    Assertions.assertEquals(8L, model.getData().getLinesCount());
  }

  @Test
  public void shouldReturnCorrectCount_whenNestedDirsAreEncountered() {
    Tree model = extractor.extractModel(getResource("modelExtractor/caseNestedDir/"));
    Assertions.assertEquals(32L, model.getData().getLinesCount());
  }

  @Test
  public void shouldReturnCorrectCount_whenNonJavaFilesEncountered() {
    Tree model = extractor.extractModel(getResource("modelExtractor/caseNonJavaFiles/"));
    Assertions.assertEquals(8L, model.getData().getLinesCount());
  }

}