package model;

import java.util.List;

/**
 * SanguineModel class implements SanguineModelInterface.
 * This class is responsible for the model of the Sanguine game.
 */
public class SanguineModel implements SanguineModelInterface<Card, Cell<Card>> {
  private Cell<Card>[][] board;
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
    board = (Cell<Card>[][]) new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new Cell<Card>();
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
    board = (Cell<Card>[][]) new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new Cell<Card>();
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
  public Cell<Card>[][] getBoard() {
    return board;
  }

  @Override
  public Cell<Card>[][] updateBoard(Cell<Card>[][] newBoard) {
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


  @Override
  public boolean isValidMove(int row, int col, Card card) {
    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      return false;
    }
    Cell<Card> cell = board[row][col];
    if (cell.isEmpty() || cell.getOwner() != current) {
      return false;
    }
    if (cell.getNumPawns() < card.getCost()) {
      return false;
    }
    return true;
  }

  @Override
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
            Cell<Card> cell = board[r][c];
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
  public int calculateRowScore(Cell<Card>[] row) {
    int score = 0;
    for (Cell<Card> c : row) {
      if (c.getCard() != null && c.getOwner() == current) {
        score += c.getCard().getValue();
      }
    }
    return score;
  }

  @Override
  public int getNumRows() {
    return board.length;
  }

  @Override
  public int getNumCols() {
    return board.length > 0 ? board[0].length : 0;
  }

  @Override
  public Cell<Card> getCellAt(int row, int col) {
    if (row < 0 || row >= board.length || col < 0 
        || col >= board[0].length) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    return board[row][col];
  }

  @Override
  public List<Card> getCurrentPlayerHand() {
    return current.getHand();
  }

  @Override
  public List<Card> getPlayerHand(PlayerInterface<Card> player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    return player.getHand();
  }

  @Override
  public PlayerInterface<Card> getCellOwner(int row, int col) {
    if (row < 0 || row >= board.length || col < 0 
        || col >= board[0].length) {
      return null;
    }
    return board[row][col].getOwner();
  }

  @Override
  public boolean isLegalMove(int row, int col, Card card) {
    return isValidMove(row, col, card);
  }

  @Override
  public boolean isLegalMove(int row, int col, int cardIndex) {
    List<Card> hand = current.getHand();
    if (cardIndex < 0 || cardIndex >= hand.size()) {
      return false;
    }
    return isValidMove(row, col, hand.get(cardIndex));
  }

  @Override
  public int getRowScore(PlayerInterface<Card> player, int rowIndex) {
    if (rowIndex < 0 || rowIndex >= board.length) {
      return 0;
    }
    int score = 0;
    for (Cell<Card> cell : board[rowIndex]) {
      if (cell.getCard() != null && cell.getOwner() == player) {
        score += cell.getCard().getValue();
      }
    }
    return score;
  }

  @Override
  public int getPlayerScore(PlayerInterface<Card> player) {
    int totalScore = 0;
    for (int i = 0; i < board.length; i++) {
      totalScore += getRowScore(player, i);
    }
    return totalScore;
  }

  @Override
  public PlayerInterface<Card> getWinner() {
    if (!isGameOver()) {
      return null;
    }
    int redScore = getPlayerScore(red);
    int blueScore = getPlayerScore(blue);
    if (redScore > blueScore) {
      return red;
    } else if (blueScore > redScore) {
      return blue;
    }
    return null; // Tie
  }

  @Override
  public PlayerInterface<Card> getCurrentPlayer() {
    return current;
  }

  @Override
  public PlayerInterface<Card> getRedPlayer() {
    return red;
  }

  @Override
  public PlayerInterface<Card> getBluePlayer() {
    return blue;
  }

  @Override
  public Cell<Card>[][] copyBoard() {
    Cell<Card>[][] copy = (Cell<Card>[][]) new Cell[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        Cell<Card>  original = board[i][j];
        Cell<Card> newCell = new Cell<Card>();
        newCell.updatePawns(original.getNumPawns());
        if (original.getCard() != null) {
          newCell.setCard(original.getCard());
        }
        if (original.getOwner() != null) {
          newCell.setOwner(original.getOwner());
        }
        copy[i][j] = newCell;
      }
    }
    return copy;
  }

  @Override
  public void playCardFromHand(int row, int col, int cardIndex) {
    List<Card> hand = current.getHand();
    if (cardIndex < 0 || cardIndex >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index");
    }
    Card card = current.playCard(cardIndex);
    placeCard(row, col, card);
    // Try to draw a new card
    try {
      current.drawNewCard();
    } catch (IllegalStateException e) {
      // Deck is empty, no card to draw - this is fine
    }
  }
}
