package klondike;

import java.io.InputStreamReader;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;
import klondike.model.hw04.KlondikeCreator;


/**
 * Main class to run the Klondike game.
 */
public final class Klondike {
  /**
   * Main method to start a Klondike game.
   * Usage: java Klondike [basic|whitehead] [numPiles] [numDraw]
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No game type provided");
    }

    KlondikeModel<SingleCard> model;
    String gameType = args[0].toLowerCase();

    if (gameType.equals("basic")) {
      model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    } else if (gameType.equals("whitehead")) {
      model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    } else {
      throw new IllegalArgumentException("Invalid game type");
    }

    int numPiles = 7;
    int numDraw = 3;

    if (args.length >= 2) {
      try {
        numPiles = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        numPiles = 7;
      }
      if (args.length >= 3) {
        try {
          numDraw = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
          numDraw = 3;
        }
      }
    }
    KlondikeController controller =
        new KlondikeTextualController(new InputStreamReader(System.in), System.out);

    try {
      controller.playGame(model, model.createNewDeck(), true, numPiles, numDraw);
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("Error starting game: " + e.getMessage());
    }
  }

}
