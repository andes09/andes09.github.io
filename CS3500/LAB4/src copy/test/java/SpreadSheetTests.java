import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import spreadsheet.MockSpreadSheetClass;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetController;

/**
 * Provided file for testing your spreadsheet controller. Unit tests and
 * integration tests are crucial for controllers. You must write tests
 * for this lab according to its instructions.
 */
public class SpreadSheetTests {
  private Readable rd;
  private Appendable ap;
  private SpreadSheet ss;
  private SpreadSheetController ssc;


  public SpreadSheetController setUp(InputStreamReader reader, StringBuilder sb) throws Exception {
    rd = reader;
    ap = sb;
    ss = new SparseSpreadSheet();
    ssc = new SpreadSheetController(ss, rd, ap);
    return ssc;
  }

  @Test
  public void testWelcomeMessage() throws Exception {
    setUp(new InputStreamReader(System.in), new StringBuilder());
    ssc.control();
    String[] str = ap.toString().split("\n");
    assertEquals("Welcome to the spreadsheet program!", str[0]);
    assertEquals("Supported user instructions are: ", str[1]);
    assertEquals("assign-value row-num col-num value (set a cell to a value)", str[2]);
    assertEquals("print-value row-num col-num (print the value at a given cell)", str[3]);
    assertEquals("menu (Print supported instruction list)", str[4]);
    assertEquals("q or quit (quit the program) ", str[5]);
  }

  @Test
  public void testFarewellMessage() throws Exception {
    setUp(new InputStreamReader(System.in), new StringBuilder());
    ssc.control();
    String[] str = ap.toString().split("\n");
    assertEquals("Thank you for using this program!", str[6]);
  }

  @Test public void testControlerSetVal() throws Exception {
    setUp(new InputStreamReader(System.in), new StringBuilder());
    MockSpreadSheetClass mock = new MockSpreadSheetClass(ap);
    mock.logSetValues(-1,3);
    mock.logSetValues(1,3);
    String[] str = ap.toString().split("\n");
    assertEquals("Row or column cannot be negativeValue set to -1, 3", str[0] + str[1]);
    assertEquals("Value set to 1, 3", str[2]);

  }
}
