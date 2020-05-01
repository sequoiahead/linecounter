import java.util.Arrays;
import java.util.List;

public class StringTest {

  public static void main(java.lang.String[] args) {
    String separator = System.getProperty("\tline.separator");
    String sampleString = String.join(
        separator,
        "First line",
        "/*Second line*/",
        "Third line",
        "Forth line"
    );

    String[] items = sampleString.split(separator);
    List<String> itemList = Arrays.asList(items);
    System.out.println(itemList);
  }
}