package spreadsheet;

public class BulkAssignMacro implements Macro {
  private int startRow;
  private int startCol;
  private int endRow;
  private int endCol;
  private double value;

  /**
   * Sets cell range and value for cells
   * @param startRow the starting row of the cell range
   * @param startCol the starting column of the cell range
   * @param endRow the ending row of the cell range
   * @param endCol the ending column of the cell range
   * @param setValue value to set the cell
   * @throws IllegalArgumentException incase cell range out of bounds
   */
  public BulkAssignMacro(int startRow, int startCol,int endRow, int endCol, double setValue)
      throws IllegalArgumentException {
    if (startCol < 0 || startRow < 0 || endCol < 0 
    || endRow < 0 || startRow > endRow || startCol > endCol) {
      throw new IllegalArgumentException("Invalid cell selection");
    }
    this.startRow = startRow;
    this.startCol = startCol;
    this.endRow = endRow;
    this.endCol = endCol;
    this.value = setValue;
  }

  @Override
  public void applyMacro(SpreadSheet sp) {
    for(int row = startRow; row < endRow; row++) {
      for(int col = startCol; col < endCol; col++) {
        sp.set(row, col, value);
      }
    }
  }
}
