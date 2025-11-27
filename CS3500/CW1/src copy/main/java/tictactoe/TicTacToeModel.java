package tictactoe;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Sets up tic tac toe and starts the game.
 * Simulates game with 2 players and a 3 by 3 grid
 */
public class TicTacToeModel implements TicTacToe {
  // add your implementation here
  private Player[][] board;

  private enum State { PLAYING, WON, TIE }

  private State currentState = State.PLAYING;
  private Player currentPlayer;

  /**
   * sets baord side, and first player.
   *
   */
  public TicTacToeModel() {
    this.board = new Player[2][2];
    currentPlayer = Player.X;
  }

  @Override
  public void move(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row > board.length - 1 || col < 0 || col > board[0].length - 1) {
      throw new IllegalArgumentException("Invalid row or column");
    }
    if (board[row][col] != null) {
      throw new IllegalArgumentException("This cell is already occupied");
    }
    board[row][col] = currentPlayer;
    if (currentPlayer == Player.X) {
      currentPlayer = Player.O;
    } else {
      currentPlayer = Player.X;
    }
  }

  @Override
  public Player getTurn() {
    return currentPlayer;
  }

  @Override
  public boolean isGameOver() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[0].length; col++) {
        if (board[row][col] == null) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Player getWinner() {
    Player winner = null;
    // horizontal
    for (int i = 0; i < board.length; i++) {
      if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])) {
        winner = board[i][0];
      }
    }
    // vertical
    for (int i = 0; i < board[0].length; i++) {
      if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])) {
        winner = board[0][i];
      }
    }

    return winner;
  }

  @Override
  public Player[][] getBoard() {
    Player[][] copy = new Player[3][3];
    for (int row  = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        copy[row][col] = board[row][col];
      }
    }
    return copy;
  }

  @Override
  public Player getMarkAt(int row, int col) {
    return board[row][col];
  }

  @Override
  public String toString() {
    // Using Java stream API to save code:
    return Arrays.stream(getBoard()).map(
            row -> " " + Arrays.stream(row).map(
                p -> p == null ? " " : p.toString()).collect(Collectors.joining(" | ")))
        .collect(Collectors.joining("\n-----------\n"));
    // This is the equivalent code as above, but using iteration, and still using the helpful
    // built-in String.join method.
    // List<String> rows = new ArrayList<>();
    // for(Player[] row : getBoard()) {
    //   List<String> rowStrings = new ArrayList<>();
    //   for(Player p : row) {
    //     if(p == null) {
    //       rowStrings.add(" ");
    //     } else {
    //       rowStrings.add(p.toString());
    //     }
    //   }
    //   rows.add(" " + String.join(" | ", rowStrings));
    // }
    // return String.join("\n-----------\n", rows);
  }
}
