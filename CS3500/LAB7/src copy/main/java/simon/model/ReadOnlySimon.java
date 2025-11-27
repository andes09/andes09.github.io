package simon.model;

import java.util.List;

/**
 * The observations for the game of SimonSays. To be used
 * wherever someone needs access to the model for observations.
 */

public interface ReadOnlySimon {
  /**
   * Each round of Simon consists of a sequence of colors, generated randomly.
   *
   * @return The current round's sequence of colors, as an unmodifiable list.
   */
  List<ColorGuess> getCurrentSequence();
}
