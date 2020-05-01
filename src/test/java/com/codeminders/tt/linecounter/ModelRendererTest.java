package com.codeminders.tt.linecounter;

import static ch.qos.logback.classic.Level.ERROR;
import static com.codeminders.tt.linecounter.Utils.getResource;

import ch.qos.logback.classic.Logger;
import com.codeminders.tt.linecounter.model.ModelExtractor;
import com.codeminders.tt.linecounter.model.Tree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class ModelRendererTest {

  private static ModelExtractor extractor = new ModelExtractor(new LinesCounter());
  private static ModelRenderer renderingService = new ModelRenderer();

  @BeforeAll
  public static void beforeClass() {
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(ERROR);
  }

  @Test
  public void shouldRenderTree_whenSingleFileOnlyModel() {
    Tree model = extractor.extractModel(getResource("modelExtractor/casePlainDir/CaseSimple.java"));
    System.out.print(renderingService.renderModel(model));
  }

  @Test
  public void shouldRenderTree_whenSingleLevelDirModel() {
    Tree model = extractor.extractModel(getResource("modelExtractor/casePlainDir/"));
    System.out.print(renderingService.renderModel(model));
  }

  @Test
  public void shouldRenderTree_whenNestedDirsModel() {
    Tree model = extractor.extractModel(getResource("modelExtractor/caseNestedDir/"));
    System.out.print(renderingService.renderModel(model));
  }

  @Test
  public void shouldRenderTree_whenRootIsEmpty() {
    Tree model = extractor
        .extractModel(getResource("modelExtractor/caseNonJavaFiles/CasePlainText.txt"));
    System.out.print(renderingService.renderModel(model));
  }

}