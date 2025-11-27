package strategy;

import java.util.List;
import model.Card;
import model.Cell;
import model.PlayerInterface;
import model.SanguineModelInterface;

/**
 * Interface for a strategy in the Sanguine game.
 * A strategy determines the best move for a player.
 */
public interface SanguineStrategyInterface {

  /**
   * Chooses the best moves for the given player in the given model.
   * Returns a list of all equally good moves according to this strategy.
   *
   * @param model  the model of the game
   * @param player the player to choose moves for
   * @return list of all equally good moves according to this strategy
   */
  List<Move> chooseMoves(SanguineModelInterface<Card, Cell<Card>>
                             model, PlayerInterface<Card> player);
}
