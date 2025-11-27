package spreadsheet;

/**
 * Sets up newer version of spreadsheet that allows use of macros
 */
public interface SpreadSheetWithMacros extends SpreadSheet {
  public void applyMacro(Macro macro);
}
