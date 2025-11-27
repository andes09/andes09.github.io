package strategy;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Cell;
import model.PlayerInterface;
import model.SanguineModelInterface;

/**
 * Strategy that chooses the move that gives the current player
 * ownership of the most cells on the board.
 * In case of ties, chooses uppermost-leftmost position, then leftmost card.
 */
public class ControllBoardStrategy implements SanguineStrategyInterface {

  @Override
  public List<Move> chooseMoves(SanguineModelInterface<Card, Cell<Card>> model,
                                PlayerInterface<Card> player) {

    List<Move> bestMoves = new ArrayList<>();
    int maxCellsControlled = -1;

    for (int cardIdx = 0; cardIdx < player.getHand().size(); cardIdx++) {
      // Try each position (top-to-bottom, left-to-right)
      for (int row = 0; row < model.getNumRows(); row++) {
        for (int col = 0; col < model.getNumCols(); col++) {
          if (model.isLegalMove(row, col, cardIdx)) {
            int cellsControlled = calculateCellsControlled(model, player,
                row, col, cardIdx);

            if (cellsControlled > maxCellsControlled) {
              // Found a better move
              maxCellsControlled = cellsControlled;
              bestMoves.clear();
              bestMoves.add(new Move(cardIdx, row, col));
            } else if (cellsControlled == maxCellsControlled) {
              // Tie - add to list (will naturally be in correct order)
              bestMoves.add(new Move(cardIdx, row, col));
            }
          }
        }
      }
    }
    return bestMoves;
  }

  private int calculateCellsControlled(SanguineModelInterface<Card, Cell<Card>> model,
                                       PlayerInterface<Card> player, int row, int col,
                                       int cardIdx) {
    int currentCells = countPlayerCells(model, player);

    Card card = player.getHand().get(cardIdx);
    boolean[][] influence = card.getInfluence();
    int potentialNewCells = 0;

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (influence[i][j]) {
          int targetRow = row + i - 2;
          int targetCol = col + j - 2;

          // Check if target is in bounds
          if (targetRow >= 0 && targetRow < model.getNumRows()
              && targetCol >= 0 && targetCol < model.getNumCols()) {

            Cell<Card> targetCell = model.getCellAt(targetRow, targetCol);

            // If cell is empty, we'll gain it
            if (targetCell.isEmpty()) {
              potentialNewCells++;
            } else if (targetCell.getOwner() != player
                && targetCell.getCard() == null
                && targetCell.getNumPawns() == 1) {
              potentialNewCells++;
            }
          }
        }
      }
    }
    return currentCells + potentialNewCells;
  }

  private int countPlayerCells(SanguineModelInterface<Card, Cell<Card>> model,
                               PlayerInterface<Card> player) {
    int count = 0;
    for (int row = 0; row < model.getNumRows(); row++) {
      for (int col = 0; col < model.getNumCols(); col++) {
        Cell<Card> cell = model.getCellAt(row, col);
        if (!cell.isEmpty() && cell.getOwner() == player) {
          count++;
        }
      }
    }
    return count;
  }
}

