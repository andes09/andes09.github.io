import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

import spreadsheet.Macro;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;

public class SparseSpreadSheetControllerWithMacroTest {
  private SpreadSheet sp;
  private Appendable ap;
  private SparseSpreadSheetControllerWithMacros controller;
  

  @Before
  public void setUp() {
    sp = new SparseSpreadSheet();
    ap = new StringBuilder();
  }

  @Test
  public void testControllerWithBulkAssignCommand() {
    String input = "bulk-assign-value 1 1 2 2 4";
    StringReader  reader = new StringReader(input);
    controller = new SparseSpreadSheetControllerWithMacros(sp, reader, ap);
    controller.control();

    for (int row =0; row<2; row++) {
      for (int col =0; col<2; col++) {
        assertEquals(4, sp.get(row, col), 0.001);
      }
    }
  }

}
