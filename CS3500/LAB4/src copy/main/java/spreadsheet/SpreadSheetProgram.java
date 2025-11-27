package spreadsheet;

import java.io.InputStreamReader;

/**
 * The driver of this application. Sets up an interactive
 * program for spreadsheets. User enters commands through
 * the terminal.
 */
public class SpreadSheetProgram {
  /**
   * The main method of the program. No arguments are expected.
   *
   * @param args any command line arguments
   */
  public static void main(String[] args) {
    SpreadSheet model = new SparseSpreadSheet();
    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    SpreadSheetController controller = new SpreadSheetController(model, rd, ap);
    controller.control();
  }
}
