package spreadsheet;

import java.io.IOException;

public class MockSpreadSheetClass  extends SparseSpreadSheet {
  private Appendable log;

  /**
   * sets up lon
   *
   * @param log new appendable object
   */
  public MockSpreadSheetClass(Appendable log) {
    this.log = log;
  }

  /**
   * sets value to specific row and column then  logs it
   *
   * @param row row
   * @param col column
   * @throws IOException for invalid row and column parameters
   */
  public void logSetValues(int row, int col) throws IOException {
    try {
      set(row, col, 3.9);
    } catch(Exception e) {
      log.append(e.getMessage() + "\n");
    }
    log.append("Value set to " + row + ", " + col + "\n");
  }




}
