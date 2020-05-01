package com.codeminders.tt.linecounter;

import com.codeminders.tt.linecounter.model.Tree;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Renders model into a string
 */
public class ModelRenderer {

  private final static Integer INDENTATION_SIZE = 2;
  private final static char INDENTATION_CHAR = ' ';
  private final Set<Option> options;

  public ModelRenderer() {
    this(
        EnumSet.of(Option.SHOW_DIRECTORIES_FIRST, Option.HIDE_ZERO_COUNTS, Option.SHOW_EMPTY_ROOT));
  }

  public ModelRenderer(Set<Option> options) {
    this.options = options;
  }

  public String renderModel(Tree tree) {
    StringBuilder output = new StringBuilder();
    //special case if root is empty
    if (tree.getRoot().getData().getLinesCount() <= 0) {
      if (options.contains(Option.SHOW_EMPTY_ROOT)) {
        renderItem(tree.getRoot(), output);
      }
    } else {
      renderLevel(tree.getRoot(), output);
    }
    return output.toString();
  }

  private void renderLevel(Tree node, StringBuilder output) {
    if (node == null) {
      return;
    }
    if (options.contains(Option.HIDE_ZERO_COUNTS) && node.getData().getLinesCount() <= 0) {
      return;
    }

    renderItem(node, output);

    if (options.contains(Option.SHOW_DIRECTORIES_FIRST)) {
      Collections.sort(node.getLeafs(), new DirectoryFirstComparator());
    }
    for (Tree leaf : node.getLeafs()) {
      renderLevel(leaf, output);
    }
  }

  private void renderItem(Tree node, StringBuilder output) {
    output.append(indentation(node)).append(renderCount(node));
  }

  private String renderCount(Tree node) {
    return String.format("%s: %d%s", renderPath(node), node.getData().getLinesCount(),
        System.lineSeparator());
  }

  private String renderPath(Tree node) {
    return node.getData().getPath().getFileName().toString();
  }

  private String indentation(Tree node) {
    return StringUtils.repeat(INDENTATION_CHAR,
        node.getLevel() * INDENTATION_SIZE);
  }

  public enum Option {
    SHOW_DIRECTORIES_FIRST, HIDE_ZERO_COUNTS, SHOW_EMPTY_ROOT
  }

  private static class DirectoryFirstComparator implements Comparator<Tree> {

    @Override
    public int compare(Tree o1, Tree o2) {
      if (o1.getData().isDirectory() && o2.getData().isDirectory()) {
        return 0;
      }
      if (o1.getData().isDirectory() && !o2.getData().isDirectory()) {
        return -1;
      }
      return 1;
    }
  }
}
