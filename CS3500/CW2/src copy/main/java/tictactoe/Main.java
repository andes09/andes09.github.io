package tictactoe;

import java.io.InputStreamReader;

/**
 * Run a Tic Tac Toe game interactively on the console.
 */
public class Main {
  /**
   * Run a Tic Tac Toe game interactively on the console.
   * Ignores all command line arguments.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    //This can be simplified into a line or two, but I chose
    //to write everything out to clarify what is going on.
    TicTacToe model = new TicTacToeModel();
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    TicTacToeController controller = new TicTacToeConsoleController(input, output);
    controller.playGame(model);
  }
}
