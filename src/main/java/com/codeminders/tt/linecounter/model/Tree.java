package com.codeminders.tt.linecounter.model;

import java.util.LinkedList;
import java.util.List;

public class Tree {

  private LinesCount data;
  private Integer level;
  private Tree root;
  private Tree parent;
  private LinkedList<Tree> leafs;

  public List<Tree> getLeafs() {
    return leafs;
  }

  public LinesCount getData() {
    return data;
  }

  public Integer getLevel() {
    return level;
  }

  public Tree getParent() {
    return parent;
  }

  public Boolean isRoot() {
    return parent == null;
  }

  public Tree getRoot() {
    return root;
  }

  //package protected to avoid mutations by irrelevant classes
  Tree(Tree parent, LinesCount data) {
    this.data = data;
    this.parent = parent;
    this.level = parent == null ? 0 : parent.getLevel() + 1;
    this.root = parent == null ? this : parent.root;
    this.leafs = new LinkedList<>();
  }

  Tree(LinesCount data) {
    this(null, data);
  }

  Tree addLeaf(LinesCount data) {
    Tree leaf = new Tree(this, data);
    leafs.add(leaf);
    return leaf;
  }

  void setLinesCount(Long count) {
    data.setLinesCount(count);
  }
}
