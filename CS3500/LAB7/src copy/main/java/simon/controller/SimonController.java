package simon.controller;

import simon.model.ColorGuess;
import simon.model.Simon;
import simon.view.SimonView;
import simon.view.ViewFeatures;


/**
 * The controller for the game of SimonSays. Acts as the callback
 * for visual components of the view.
 */
public class SimonController implements GameController, ViewFeatures {
  private final Simon model;
  private final SimonView view;

  /**
   * Constructs a controller with the given model and view.
   *
   * @param model the model for this game
   * @param view the view for the game
   */
  public SimonController(Simon model, SimonView view) {
    this.model = model;
    this.view = view;
    this.view.addFeatureListener(this);
  }

  @Override
  public void runGame() {
    this.view.display(true);
  }

  @Override
  public void selectedColor(ColorGuess guess) {
    boolean success = this.model.enterNextColor(guess);
    if (success) {
      this.view.advance();
    } else {
      this.view.error();
    }
  }

  @Override
  public void quit() {
    System.exit(0);
  }
}
