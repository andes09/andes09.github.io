package klondike.model.hw04;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;

/**
 * Factory class for vreatng different variant of Klondike.
 */

public class KlondikeCreator {

  /**
   * Enum representing different game types.
   */
  public enum GameType {
    BASIC,
    WHITEHEAD
  }

  /**
   * Creates a klondike model of the specified type.
   *
   * @param gameType type of game to create
   * @return Klondike model of the specified type
   */
  public static KlondikeModel<SingleCard> create(GameType gameType) {
    switch (gameType) {
      case BASIC:
        return new BasicKlondike();
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }
}
