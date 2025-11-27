package model;

/**
 * SanguineModel class implements SanguineModelInterface.
 * This class is responsible for the model of the Sanguine game.
 */
public class SanguineModel implements SanguineModelInterface<Card, Cell> {
  private Cell[][] board;
  private Player red;
  private Player blue;
  private Player current;
  private boolean gameOver;

  /**
   * Constructor for SanguineModel.
   *
   * @param red  Red Player
   * @param blue Blue Player
   * @param rows Number of rows
   * @param cols Number of columns
   * @throws IllegalArgumentException if parameters are invalid
   */
  public SanguineModel(Player red, Player blue, int rows, int cols)
      throws IllegalArgumentException {
    if (rows < 0 || cols < 0) {
      throw new IllegalArgumentException("Invalid board size");
    }
    if (red == null || blue == null) {
      throw new IllegalArgumentException("Invalid player");
    }
    this.red = red;
    this.blue = blue;
    this.current = red;
    board = new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new Cell();
      }
    }
    for (int i = 0; i < rows; i++) {
      board[i][0].updatePawns(1);
      board[i][0].setOwner(red);
      board[i][cols - 1].updatePawns(1);
      board[i][cols - 1].setOwner(blue);
    }
    gameOver = false;
  }

  @Override
  public void start(int rows, int cols, Card[] blueHand, Card[] redHand)
      throws IllegalArgumentException {
    if (blueHand.length != 15 || redHand.length != 15) {
      throw new IllegalArgumentException("Invalid hand size");
    }
    if (rows < 0 || cols < 0) {
      throw new IllegalArgumentException("Invalid board size");
    }
    if (blueHand == null || redHand == null) {
      throw new IllegalArgumentException("Invalid hand");
    }
    board = new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new Cell();
      }
    }
    for (int i = 0; i < rows; i++) {
      board[i][0].updatePawns(1);
      board[i][0].setOwner(red);
      board[i][cols - 1].updatePawns(1);
      board[i][cols - 1].setOwner(blue);
    }
    gameOver = false;
  }

  @Override
  public Cell[][] getBoard() {
    return board;
  }

  @Override
  public Cell[][] updateBoard(Cell[][] newBoard) {
    board = newBoard;
    return board;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public void pass() {
    nextTurn();
  }

  /**
   * Switches the current player to the next player.
   */
  private void nextTurn() {
    if (current == red) {
      current = blue;
    } else {
      current = red;
    }
  }


  /**
   * Gets the current player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return current;
  }

  /**
   * Gets the red player.
   *
   * @return the red player
   */
  public Player getRedPlayer() {
    return red;
  }

  /**
   * Gets the blue player.
   *
   * @return the blue player
   */
  public Player getBluePlayer() {
    return blue;
  }

  /**
   * Checks if a move is valid.
   *
   * @param row  the row
   * @param col  the column
   * @param card the card
   * @return true if valid, false otherwise
   */
  public boolean isValidMove(int row, int col, Card card) {
    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      return false;
    }
    Cell cell = board[row][col];
    if (cell.isEmpty() || cell.getOwner() != current) {
      return false;
    }
    if (cell.getNumPawns() < card.getCost()) {
      return false;
    }
    return true;
  }

  /**
   * Sets game over state.
   *
   * @param over true if game is over
   */
  public void setGameOver(boolean over) {
    this.gameOver = over;
  }

  @Override
  public void placeCard(int row, int col, Card card) {
    if (card == null) {
      throw new IllegalArgumentException("Invalid card");
    }
    if (board[row][col].isEmpty() || board[row][col].getOwner() != current) {
      throw new IllegalArgumentException("Invalid card placement");
    }
    if (board[row][col].getNumPawns() < card.getCost()) {
      throw new IllegalArgumentException("Insufficient pawns");
    }
    if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
      throw new IllegalArgumentException("Invalid card placement");
    }

    board[row][col].setCard(card);
    board[row][col].updatePawns(0);
    applyInfluence(row, col, card);
    nextTurn();
  }

  private void applyInfluence(int row, int col, Card card) {
    boolean[][] inf = card.getInfluence();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (inf[i][j]) {
          int r = row + i - 2;
          int c = col + j - 2;
          if (r >= 0 && c >= 0 && r < board.length && c < board[0].length) {
            Cell cell = board[r][c];
            if (cell.isEmpty()) {
              cell.updatePawns(1);
              cell.setOwner(current);
            } else if (cell.getCard() == null) {
              if (cell.getOwner() == current) {
                cell.updatePawns(Math.min(3, cell.getNumPawns() + 1));
              } else {
                int enemyPawns = cell.getNumPawns();
                if (enemyPawns == 1) {
                  // 1 pawn cancels out - cell becomes empty
                  cell.updatePawns(0);
                  cell.setOwner(null);
                } else {
                  // Multiple pawns: decrease by 1, change owner
                  cell.updatePawns(enemyPawns - 1);
                  cell.setOwner(current);
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public int calculateRowScore(Cell[] row) {
    int score = 0;
    for (Cell c : row) {
      if (c.getCard() != null && c.getOwner() == current) {
        score += c.getCard().getValue();
      }
    }
    return score;
  }
}
