package com.codeminders.tt.linecounter;

import java.io.File;

public class Utils {

  public static File getResource(String path) {
    return new File(LinesCounterTest.class.getClassLoader().getResource(path).getFile());
  }

}
