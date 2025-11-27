package simon.view;

import simon.model.ColorGuess;

/**
 * The features the view needs implemented by some class in the
 * controller package.
 *
 * <p>Of note, this interface is declared in the view package because
 * the view specifically knows what needs to be observed by the controller
 * (or really any class willing to answer these calls).
 */
public interface ViewFeatures {

  /**
   * Checks if the guess indicated is correct. Refreshes the
   * view after checking the guess.
   *
   * @param guess the guess to check against the model
   */
  void selectedColor(ColorGuess guess);

  /**
   * Quits the program.
   */
  void quit();
}
