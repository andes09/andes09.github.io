package tictactoe;

import java.io.IOException;
import java.util.Scanner;

/**
 * Creates a controller for tic tac toe.
 */
public class TicTacToeConsoleController implements TicTacToeController {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Creates the controller to read from in and print to out.
   *
   * @param in the input source
   * @param out the output destination
   * @throws IllegalArgumentException if in or out are null
   */
  public TicTacToeConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  private boolean processMove(TicTacToe model) throws IOException {
    while (true) {
      // Read row input
      if (!scan.hasNext()) {
        return false;
      }
      String rowInput = scan.next();
      if (rowInput.equalsIgnoreCase("q")) {
        return false;
      }

      int row;
      try {
        row = Integer.parseInt(rowInput);
      } catch (IllegalArgumentException e) {
        out.append("Invalid input. Please enter a number or 'q' to quit.\n");
        continue; // Continue reading next input
      }

      // Read column input
      if (!scan.hasNext()) {
        return false;
      }
      String colInput = scan.next();
      if (colInput.equalsIgnoreCase("q")) {
        return false;
      }

      int col;
      try {
        col = Integer.parseInt(colInput);
      } catch (IllegalArgumentException e) {
        out.append("Invalid input. Please enter a number or 'q' to quit.\n");
        continue; // Continue reading next input
      }

      // Try to make the move
      try {
        model.move(row - 1, col - 1);
        return true; // Move successful
      } catch (IllegalArgumentException e) {
        out.append("Invalid move. Try again.\n");
      } catch (IllegalStateException e) {
        out.append("Game is already over.\n");
        return true;
      }
    }
  }


  @Override
  public void playGame(TicTacToe model) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    try {
      while (!model.isGameOver()) {
        out.append(model.toString());
        out.append("\n");

        out.append("Enter a move for " + model.getTurn().toString() + ":\n");

        if (!processMove(model)) {
          out.append("Game quit! Ending game state:\n");
          out.append(model.toString());
          out.append("\n");
          return;
        }
      }

      out.append(model.toString());
      out.append("\n");
      out.append("Game is over! ");

      Player winner = model.getWinner();
      if (winner != null) {
        out.append(winner.toString() + " wins.\n");
      } else {
        out.append("Tie game.\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }
  }
}
