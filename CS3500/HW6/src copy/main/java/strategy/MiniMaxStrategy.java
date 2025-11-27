package strategy;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Cell;
import model.PlayerInterface;
import model.SanguineModelInterface;

/**
 * A minimax strategy that chooses moves to minimize the opponent's
 * best possible response. Assumes opponent uses a given strategy
 * and evaluates positions based on score differential.
 */
public class MiniMaxStrategy implements SanguineStrategyInterface {
  private final SanguineStrategyInterface opponentStrategy;

  /**
   * Constructs a minimax strategy.
   *
   * @param opponentStrategy the strategy assumed to be used by the opponent
   */
  public MiniMaxStrategy(SanguineStrategyInterface opponentStrategy) {
    this.opponentStrategy = opponentStrategy;
  }

  @Override
  public List<Move> chooseMoves(SanguineModelInterface<Card, Cell<Card>> model,
                                PlayerInterface<Card> player) {
    List<Move> bestMoves = new ArrayList<>();
    int bestScore = Integer.MIN_VALUE;

    PlayerInterface<Card> opponent = getOpponent(model, player);
    for (int cardIdx = 0; cardIdx < player.getHand().size(); cardIdx++) {
      for (int row = 0; row < model.getNumRows(); row++) {
        for (int col = 0; col < model.getNumCols(); col++) {
          if (model.isLegalMove(row, col, cardIdx)) {
            Move move = new Move(cardIdx, row, col);


            int score = evaluateMove(model, player, opponent, move);

            if (score > bestScore) {
              bestScore = score;
              bestMoves.clear();
              bestMoves.add(move);
            } else if (score == bestScore) {
              bestMoves.add(move);
            }
          }
        }
      }
    }

    return bestMoves;
  }

  /**
   * Evaluates a move by considering the opponent's best response.
   * Returns the score differential after opponent's best move.
   *
   * @param model    the game model
   * @param player   the player making the move
   * @param opponent the opponent
   * @param move     the move to evaluate
   * @return score (higher is better for player)
   */
  private int evaluateMove(SanguineModelInterface<Card, Cell<Card>> model,
                           PlayerInterface<Card> player,
                           PlayerInterface<Card> opponent,
                           Move move) {
    // Calculate immediate score after our move
    Card ourCard = player.getHand().get(move.getCardIndex());
    int ourCurrentScore = model.getPlayerScore(player);
    int ourScoreAfterMove = ourCurrentScore + ourCard.getValue();

    List<Move> opponentMoves = opponentStrategy.chooseMoves(model, opponent);

    if (opponentMoves.isEmpty()) {
      return ourScoreAfterMove + 1000;
    }


    int worstCaseScore = Integer.MAX_VALUE;
    for (Move oppMove : opponentMoves) {
      Card oppCard = opponent.getHand().get(oppMove.getCardIndex());
      int oppScore = model.getPlayerScore(opponent);
      int oppScoreAfterMove = oppScore + oppCard.getValue();

      int scoreDiff = ourScoreAfterMove - oppScoreAfterMove;
      worstCaseScore = Math.min(worstCaseScore, scoreDiff);
    }

    return worstCaseScore;
  }

  /**
   * Gets the opponent player.
   *
   * @param model  the game model
   * @param player the current player
   * @return the opponent
   */
  private PlayerInterface<Card> getOpponent(
      SanguineModelInterface<Card, Cell<Card>> model,
      PlayerInterface<Card> player) {
    if (player == model.getRedPlayer()) {
      return model.getBluePlayer();
    } else {
      return model.getRedPlayer();
    }
  }
}