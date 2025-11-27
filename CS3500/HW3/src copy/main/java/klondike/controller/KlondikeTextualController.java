package klondike.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;
import klondike.view.TextualView;

/**
 * Controller class for the textual view of the game.
 * reads inputs and logs the outputs
 */
public class KlondikeTextualController implements KlondikeController {

  private final Readable readable;
  private final Appendable appendable;

  /**
   * Sets up controller class.
   *
   * @param readable   input
   * @param appendable output
   * @throws IllegalArgumentException for invalid or null readable
   */
  public KlondikeTextualController(Readable readable, Appendable appendable)
      throws IllegalArgumentException {

    if (readable == null || appendable == null) {
      throw new IllegalArgumentException("Readable and Appendable cannot be null");
    }
    this.readable = readable;
    this.appendable = appendable;
  }

  @Override
  public <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck,
                                        boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    KlondikeTextualView view = new KlondikeTextualView(model, appendable);
    try {
      try {
        model.startGame(deck, shuffle, numPiles, numDraw);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException("Cannot start game", e);
      }
      // Render initial board once before the loop
      view.render();
      appendable.append("Score: " + model.getScore() + "\n");
      Scanner scanner = new Scanner(readable);
      while (!model.isGameOver()) {
        if (!scanner.hasNext()) {
          throw new IllegalStateException("No more input available");
        }
        String command = scanner.next();
        if (command.equalsIgnoreCase("q")) {
          quitSequence(view, model);
          return;
        }
        boolean moveSuccessful = processCommand(command, model, scanner);
        // Only print board after successful moves (if game continues)
        if (moveSuccessful && !model.isGameOver()) {
          view.render();
          appendable.append("Score: " + model.getScore() + "\n");
        }
      }
      // Game is over - render final state
      view.render();
      // When game is over, check if it's a win or loss
      if (checkIfWon(model)) {
        appendable.append("You win!\n");
      } else {
        appendable.append("Game over. Score: " + model.getScore() + "\n");
      }
    } catch (IOException e) {
      if (e.getMessage().equals("QUIT_SIGNAL")) {
        quitSequence(view, model);
      } else {
        throw new IllegalStateException("Unable to read input or write output", e);
      }
    }
  }

  private <C extends Card> boolean processCommand(String command,
                                                  KlondikeModel<C> model, Scanner scanner)
      throws IOException {
    try {
      switch (command.toLowerCase()) {
        case "mpp":
          int srcPile = readValidInt(scanner) - 1;
          int numCards = readValidInt(scanner);
          int destPile = readValidInt(scanner) - 1;
          model.movePile(srcPile, numCards, destPile);
          appendable.append("Move pile to cascade pile\n");
          return true; // Move successful
        case "md":
          int cascadePile = readValidInt(scanner) - 1;
          model.moveDraw(cascadePile);
          appendable.append("Move draw to cascade pile\n");
          return true; // Move successful
        case "mpf":
          int srcPileF = readValidInt(scanner) - 1;
          int foundationPile = readValidInt(scanner) - 1;
          model.moveToFoundation(srcPileF, foundationPile);
          appendable.append("Move pile to foundation pile\n");
          return true; // Move successful
        case "mdf":
          int foundationPileD = readValidInt(scanner) - 1;
          model.moveDrawToFoundation(foundationPileD);
          appendable.append("Move draw to foundation pile\n");
          return true; // Move successful
        case "dd":
          model.discardDraw();
          appendable.append("Discard draw\n");
          return true; // Move successful
        default:
          appendable.append("Invalid move. Play again. Unknown command\n");
          return false; // Move failed
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      appendable.append("Invalid move. Play again. " + e.getMessage() + "\n");
      return false; // Move failed
    }
  }

  private int readValidInt(Scanner scanner) throws IOException {
    while (scanner.hasNext()) {
      if (scanner.hasNextInt()) {
        return scanner.nextInt();
      } else {
        String input = scanner.next();
        if (input.equalsIgnoreCase("q")) {
          throw new IOException("QUIT_SIGNAL");
        }
      }
    }
    throw new IllegalStateException("No more input available");
  }

  /**
   * Events that occur when user quits game.
   *
   * @param view model view
   * @param model model itself
   * @param <C> card type
   */
  private <C extends Card> void quitSequence(TextualView view, KlondikeModel<C> model) {
    try {
      appendable.append("Game quit!\n");
      appendable.append("State of game when quit:\n");
      view.render();
      appendable.append("Score: " + model.getScore() + "\n");
    } catch (Exception e) {
      throw new IllegalStateException("Unable to write output", e);
    }
  }

  /**
   * Returns weather or not a player won.
   *
   * @param model model itself.
   * @param <C> subclass of Card
   * @return true if player won, false if not
   */
  private <C extends Card> boolean checkIfWon(KlondikeModel<C> model) {
    for (int i = 0; i < model.getNumPiles(); i++) {
      if (model.getPileHeight(i) > 0) {
        return false;
      }
    }
    return true;
  }
}