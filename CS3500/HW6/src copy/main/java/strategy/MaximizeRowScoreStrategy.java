package strategy;


import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Cell;
import model.PlayerInterface;
import model.SanguineModelInterface;




/**
 * Strategy that tries to win rows by maximizing row score.
 * Visits rows from top to bottom, looking for moves that would
 * make the player's row score higher than the opponent's.
 */
public class MaximizeRowScoreStrategy implements SanguineStrategyInterface {
  @Override
  public List<Move> chooseMoves(SanguineModelInterface<Card, Cell<Card>> model, 
                                PlayerInterface<Card> player) {
    List<Move> moves = new ArrayList<>();
    
    PlayerInterface<Card> opponent;
    if (player == model.getRedPlayer()) {
      opponent = model.getBluePlayer();
    } else {
      opponent = model.getRedPlayer();
    }
    
    for (int row = 0; row < model.getNumRows(); row++) {
      int playerScore = model.getRowScore(player, row);
      int opponentScore = model.getRowScore(opponent, row);
      
      if (playerScore <= opponentScore) {
        Move winningMove = findRowWinningMove(model, player, opponent, row, opponentScore);
        if (winningMove != null) {
          moves.add(winningMove);
          return moves;
        }
      }
    }
    
    return moves;
  }

  private Move findRowWinningMove(SanguineModelInterface<Card, Cell<Card>> model,
                                  PlayerInterface<Card> player,
                                  PlayerInterface<Card> opponent,
                                  int row, int opponentScore) {
    // Try each card
    for (int cardIdx = 0; cardIdx < player.getHand().size(); cardIdx++) {
      // Try each column in this row
      for (int col = 0; col < model.getNumCols(); col++) {
        if (model.isLegalMove(row, col, cardIdx)) {
          // Simulate the move to see if it wins the row
          if (wouldWinRow(model, player, opponent, row, col, cardIdx, opponentScore)) {
            return new Move(cardIdx, row, col);
          }
        }
      }
    }
    return null;
  }

  private boolean wouldWinRow(SanguineModelInterface<Card, Cell<Card>> model,
                             PlayerInterface<Card> player,
                             PlayerInterface<Card> opponent,
                             int row, int col, int cardIdx, int opponentScore) {
    // Create a copy of the model and simulate the move
    // For simplicity, we'll calculate what the new score would be
    Card card = player.getHand().get(cardIdx);
    int currentScore = model.getRowScore(player, row);
    int newScore = currentScore + card.getValue();
    
    return newScore > opponentScore;
  }
}
