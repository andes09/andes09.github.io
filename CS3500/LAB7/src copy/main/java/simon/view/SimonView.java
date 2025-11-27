package simon.view;

/**
 * Behaviors for any view of SimonSays game. Of note, it exposes
 * a way to add a callback object if needed.
 */
public interface SimonView {
  /**
   * Sets the callback object for the view to the given features object.
   *
   * @param features the callback object for all components in the view
   */
  void addFeatureListener(ViewFeatures features);

  /**
   * Makes the view visible if show is true and invisible otherwise.
   *
   * @param show whether view should be visible
   */
  void display(boolean show);

  /**
   * Display the next color in the sequence.
   */
  void advance();

  /**
   * Tell the user that they guessed wrong.
   */
  void error();
}
