import java.util.Scanner;
import spreadsheet.BulkAssignMacro;
import spreadsheet.Macro;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetController;
import spreadsheet.SpreadSheetWithMacros;

public class SparseSpreadSheetControllerWithMacros extends SpreadSheetController {
  private Macro macro;

  public SparseSpreadSheetControllerWithMacros(SpreadSheet sheet, Readable readable,
                                               Appendable appendable) {
    super(sheet, readable, appendable);
  }

  @Override
  protected void processCommand(String input, Scanner scan, SpreadSheet sheet) {
    if ("bulk-assign-value".equalsIgnoreCase(input)) {

      int rowStart = scan.nextInt();
      int colStart = scan.nextInt();
      int rowEnd = scan.nextInt();
      int colEnd = scan.nextInt();
      double value = scan.nextDouble();

      rowStart -= 1;
      colStart -= 1;

      if (rowStart > rowEnd || colStart > colEnd) {
        writeMessage("Invalid row or column number \n");
        return;
      }
      if (sheet instanceof SpreadSheetWithMacros) {
        macro = new BulkAssignMacro(rowStart, colStart, rowEnd, colEnd, value);
        macro.applyMacro(sheet);
        writeMessage("Macro assigned successfully \n");
      } else {
        writeMessage("Invalid sheet \n");
      }
    } else {
      super.processCommand(input, scan, sheet);
    }
  }
}
