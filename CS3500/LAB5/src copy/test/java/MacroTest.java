import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import spreadsheet.BulkAssignMacro;
import spreadsheet.Macro;
import spreadsheet.SparseSpreadSheet;

/**
 * Test Macro Functionality
 */
public class MacroTest {
  SparseSpreadSheet sp;
  Macro macro;

  @Before
  public void setUp() {
    sp = new SparseSpreadSheet();
  }

  @Test
  public void testInvalidInputsWithBulkMacro(){

    assertThrows(IllegalArgumentException.class, () -> {
      ;macro = new BulkAssignMacro(-1, 3, 4,4,4);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      macro = new BulkAssignMacro(2, -3, 4,4,4);;
    });
    assertThrows(IllegalArgumentException.class, () -> {
      ;macro = new BulkAssignMacro(1, 3, -4,4,4);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      macro = new BulkAssignMacro(2,2, 4,-4,4);;
    });
    assertThrows(IllegalArgumentException.class, () -> {
      ;macro = new BulkAssignMacro(3, 3, 1,4,4);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      macro = new BulkAssignMacro(2,2, 4,1,4);;
    });
    
    
  }

  @Test
  public void testValidInputsWithBulkMacro() {
    macro = new BulkAssignMacro(1, 1, 2, 2, 4);
    macro.applyMacro(sp);
    for (int r = 1; r < 2; r++) {
      for (int c = 1; c < 2; c++){
        assertEquals(sp.get(r, c), 4, 0.001);
      }
    }
  }
}
