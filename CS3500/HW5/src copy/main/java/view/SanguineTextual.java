package view;

import java.awt.Color;
import model.Cell;
import model.SanguineModel;

/**
 * SanguineTextual class implements SanguineTextualView.
 * This class is responsible for the textual view of the Sanguine game.
 */
public class SanguineTextual implements SanguineTextualView {
  private final SanguineModel model;

  /**
   * Constructor for SanguineTextual.
   *
   * @param model SanguineModel
   */
  public SanguineTextual(SanguineModel model) {
    this.model = model;
  }

  @Override
  public String render() {
    StringBuilder sb = new StringBuilder();
    Cell[][] board = model.getBoard();
    for (int i = 0; i < board.length; i++) {
      // Calculate red score for this row
      int redScore = 0;
      int blueScore = 0;
      for (Cell cell : board[i]) {
        if (cell.getCard() != null && cell.getOwner() != null) {
          if (cell.getOwner().getPlayerColor().equals(Color.RED)) {
            redScore += cell.getCard().getValue();
          } else {
            blueScore += cell.getCard().getValue();
          }
        }
      }
      sb.append(redScore).append(" ");
      for (int j = 0; j < board[i].length; j++) {
        Cell cell = board[i][j];
        if (cell.isEmpty()) {
          sb.append("_");
        } else if (cell.getCard() != null) {
          if (cell.getOwner().getPlayerColor().equals(Color.RED)) {
            sb.append("R");
          } else {
            sb.append("B");
          }
        } else if (cell.getOwner() != null) {
          sb.append(cell.getNumPawns());
        } else {
          sb.append("_");
        }
      }
      sb.append(" ").append(blueScore);
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return render();
  }
}
