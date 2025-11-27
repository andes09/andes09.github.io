package spreadsheet;

import java.io.InputStreamReader;

/**
 * The driver of this application. Reads input from
 * the command line after program launch.
 */
public class SpreadSheetProgram {

  /**
   * Starts the spreadsheet program. Reads additional input from
   * the command line. Ignores any additional arguments.
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
