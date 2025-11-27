package simon;

import simon.controller.SimonController;
import simon.model.Simon;
import simon.model.SimonSays;
import simon.view.SimonView;
import simon.view.SimpleSimonView;

/**
 * Runner for the game of Simon Says.
 */
public class SimonGame {

  /**
   * Main method that runs the game. Opens a window for the game to be played.
   *
   * @param args the command-line arguments of the program
   */
  public static void main(String[] args) {
    Simon model = new SimonSays.Builder().build(); // Feel free to customize this as desired
    SimonView view = new SimpleSimonView(model);
    SimonController simonController = new SimonController(model, view);
    simonController.runGame();
  }
}
