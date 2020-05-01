package com.codeminders.tt.linecounter;


import com.codeminders.tt.linecounter.model.ModelExtractor;
import com.codeminders.tt.linecounter.model.Tree;
import java.io.File;

public class App {

  private ModelExtractor modelExtractor;
  private ModelRenderer renderer;

  private App() {
    modelExtractor = new ModelExtractor(new LinesCounter());
    renderer = new ModelRenderer();
  }

  public static void main(String[] args) {
    App app = new App();
    if (args.length <= 0) {
      showHelp();
      java.lang.System.exit(0);
    }
    for (String input: args) {
      try {
        String output = app.countLines(input);
        showOutput(output);
      } catch (IllegalArgumentException ex) {
        showInputError(input, ex);
      } catch (RuntimeException ex) {
        showUnexpectedError(ex);
      }
    }

    java.lang.System.exit(0);
  }

  private String countLines(String filename) throws RuntimeException {
    validateInput(filename);
    File input = new File(filename);
    Tree model = modelExtractor.extractModel(input);
    return renderer.renderModel(model);
  }

  private void validateInput(String filename) throws IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("File name is not valid");
    }
    File file = new File(filename);
    if (!file.exists()) {
      throw new IllegalArgumentException("File or directory does not exist");
    }
    if (!file.canRead()) {
      throw new IllegalArgumentException(
          "File or directory can not be read. Please check permissions");
    }
  }

  private static void showOutput(String output) {
    java.lang.System.out.println(output);
  }

  private static void showHelp() {
    showOutput("Please provide directory name or filename. Multiple arguments are accepted");
    showOutput("java -jar linecounter-1.0-SNAPSHOT.jar <dirname/filename> [dirname/filename]...");
  }

  private static void showInputError(String input, Exception ex) {
    showOutput(String.format("Argument '%s' is not valid. Reason: %s", input, ex.getMessage()));
  }

  private static void showUnexpectedError(RuntimeException ex) {
    showOutput(String.format("Unexpected error occurred: %s", ex.getMessage()));
    ex.printStackTrace();
  }

}
