package com.codeminders.tt.linecounter.model;

import com.codeminders.tt.linecounter.LinesCounter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates data model of supplied file or directory
 */
public class ModelExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModelExtractor.class);

  private Tree currentNode;
  private LinesCounter counter;
  private CountingFileVisitor visitor;

  public ModelExtractor(LinesCounter counter) {
    this.counter = counter;
    this.visitor = new CountingFileVisitor();
  }

  public Tree extractModel(File input) {
    currentNode = null;
    if (input.isFile()) {
      handleSingleFile(input);
    } else {
      handleDirectory(input);
    }
    return currentNode.getRoot();
  }

  private void handleSingleFile(File input) {
    Long count = counter.countLines(input);
    currentNode = new Tree(new LinesCount(input.toPath(), count));
  }

  private void handleDirectory(File input) {
    try {
      Files.walkFileTree(input.toPath(),
          EnumSet.of(FileVisitOption.FOLLOW_LINKS),
          Integer.MAX_VALUE,
          visitor);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private class CountingFileVisitor implements FileVisitor<Path> {

    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
      LOGGER.debug("Pre visiting {}", dir);
      if (currentNode == null) {
        //root directory visited, create new Tree
        currentNode = new Tree(new LinesCount(dir, 0L));
        LOGGER.debug("New tree created");
      } else {
        //sub directory visited - descend to level down
        currentNode = currentNode.addLeaf(new LinesCount(dir, 0L));
        LOGGER.debug("Descended to {}", currentNode.getData().getPath());
      }
      //create new tree level
      return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
      //add leaf to current level
      File input = path.toFile();
      if (!input.canRead()) {
        LOGGER.warn("Could not read file {}. Skipping it from processing", path);
        return FileVisitResult.CONTINUE;
      }
      Long count = counter.countLines(input);
      currentNode.addLeaf(new LinesCount(path, count));
      LOGGER.debug("Counted {}", path);
      return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFileFailed(Path path, IOException exc) {
      LOGGER.warn("Somethign went wrong when visiting {}. Skipping it from processing", path);
      return FileVisitResult.CONTINUE;
    }

    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
      //run trough all nodes at the level, accumulate count to directory
      LOGGER.debug("Post visiting {}, current node is {}", dir, currentNode.getData().getPath());

      Long directoryCount = currentNode.getLeafs()
          .stream()
          .map(Tree::getData)
          .map(LinesCount::getLinesCount)
          .reduce(0L, Long::sum);
      currentNode.setLinesCount(directoryCount);

      LOGGER.debug("Directory total count {}", currentNode.getData().getLinesCount());

      //go one level up
      if (!currentNode.isRoot()) {
        currentNode = currentNode.getParent();
      }
      return FileVisitResult.CONTINUE;
    }
  }
}
