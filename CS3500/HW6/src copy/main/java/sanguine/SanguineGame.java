package sanguine;


import controller.StubController;
import java.awt.Color;
import model.Card;
import model.Cell;
import model.Player;
import model.SanguineModel;
import view.SanguineFrame;



/**
 * Main class to run the Sanguine game with GUI.
 */
public final class SanguineGame {

  /**
   * Main method to start the game.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    // Create players with deck configuration files
    Player redPlayer = new Player(Color.RED, "example.deck");
    Player bluePlayer = new Player(Color.BLUE, "example.deck");
    
    // Create model with 5 rows and 7 columns
    SanguineModel model = new SanguineModel(redPlayer, bluePlayer, 3, 5);
    
    // Create view
    SanguineFrame view = new SanguineFrame(model);
    
    // Create stub controller
    StubController controller = new StubController(model, view);
    
    // Show the view
    view.setVisible(true);
  }
}
