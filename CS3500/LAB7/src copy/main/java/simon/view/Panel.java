package simon.view;

import java.awt.Dimension;

/**
 * General panel methods for most views. This is partially
 * SimonSays specific given the method advance, but the other
 * two methods are helpful to expose or have in any Panel
 * you create.
 */
public interface Panel {

  /**
   * This is actually from JPanel, but we declare it here
   * so the client/reader knows this is a public behavior.
   * This documentation is re-written in SimonPanel to
   * clarify what the dimensions actually are.
   *
   * @return the preferred size of the panel
   */
  Dimension getPreferredSize();

  /**
   * Adds the features object to the panel, binding the features as
   * the observer of this panel subject. features should expect to be called
   * when an event occurs in the view.
   *
   * @param features the observer to bind to the listeners and this panel
   */
  void addFeaturesListener(ViewFeatures features);

  /**
   * Advances the state of the panel to the next sequence in the game
   * and displays it.
   */
  void advance();
}
