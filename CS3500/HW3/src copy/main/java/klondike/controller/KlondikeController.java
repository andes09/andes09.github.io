package klondike.controller;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * controller interface.
 */

public interface KlondikeController {

  /**
   * plays the game with given controller.
   *
   * @param model game model
   * @param deck deck for the game
   * @param shuffle weather ornot to shuffle deck
   * @param numPiles number of piles in game
   * @param numDraw number of draw cards
   * @param <C> Single Card
   * @throws IllegalArgumentException for illeagal arguemts
   * @throws IllegalStateException if game hasnt started
   */
  <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle,
                                 int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException;
}
