package strategy;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Cell;
import model.PlayerInterface;
import model.SanguineModelInterface;


/**
 * Strategy that chooses the first legal move found.
 * Searches top-to-bottom, left-to-right through the board,
 * and leftmost card first in the hand.
 */
public class FillFirstStrategy implements SanguineStrategyInterface {

  @Override
  public List<Move> chooseMoves(SanguineModelInterface<Card, Cell<Card>> model,
                                PlayerInterface<Card> player) {
    List<Move> moves = new ArrayList<>();

    for (int cardIdx = 0; cardIdx < player.getHand().size(); cardIdx++) {

      for (int row = 0; row < model.getNumRows(); row++) {
        for (int col = 0; col < model.getNumCols(); col++) {
          if (model.isLegalMove(row, col, cardIdx)) {
            moves.add(new Move(cardIdx, row, col));
            return moves;
          }
        }
      }
    }

    return moves;
  }
}
