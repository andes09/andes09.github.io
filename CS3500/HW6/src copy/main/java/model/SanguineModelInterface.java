package model;

import java.util.List;

/**
 * Interface for the Sanguine game model.
 *
 * @param <C> the type of card interface
 * @param <J> the type of cell interface
 */
public interface SanguineModelInterface<C extends CardInterface, J extends CellInterface> {
  /**
   * Sets up game.
   * checks if player decks are valid.
   *
   * @param rows number of rows
   * @param cols number of columns
   * @param blueHand Blue Player Hand
   * @param redHand Red Player Hand
   */
  void start(int rows, int cols, C[] blueHand, C[] redHand);

  /**
   * Current Board.
   *
   * @return Board
   */
  J[][] getBoard();

  /**
   * updates board and pawns on board.
   *
   * @param board board
   * @return new updated board
   */
  J[][] updateBoard(J[][] board);

  /**
   * checks if game is over.
   *
   * @return true if game is over, false if not
   */
  boolean isGameOver();

  /**
   * Pass Move for Player.
   */
  void pass();

  /**
   * Places Card at specified position.
   *
   * @param row row number
   * @param col column number
   * @param card card to be played
   */
  void placeCard(int row, int col, C card);

  /**
   * calculates each rows score.
   *
   * @param row row number
   * @return score
   */
  int calculateRowScore(J[] row);


  /**
   * Gets the number of rows in the game board.
   *
   * @return number of rows
   */
  int getNumRows();

  /**
   * Gets the number of columns in the game board.
   *
   * @return number of columns
   */
  int getNumCols();

  /**
   * Gets the cell at the specified coordinates.
   *
   * @param row the row index
   * @param col the column index
   * @return the cell at that position
   * @throws IllegalArgumentException if coordinates are out of bounds
   */
  J getCellAt(int row, int col);

  /**
   * Gets the current player's hand.
   *
   * @return list of cards in current player's hand
   */
  List<C> getCurrentPlayerHand();

  /**
   * Gets a specific player's hand.
   *
   * @param player the player whose hand to get
   * @return list of cards in that player's hand
   */
  List<C> getPlayerHand(PlayerInterface<C> player);

  /**
   * Gets the owner of the cell at the specified coordinates.
   *
   * @param row the row index
   * @param col the column index
   * @return the player who owns that cell, or null if empty
   */
  PlayerInterface<C> getCellOwner(int row, int col);

  /**
   * Checks if it's legal for the current player to play the given card.
   *
   * @param row the row index
   * @param col the column index
   * @param card the card to play
   * @return true if the move is legal, false otherwise
   */
  boolean isLegalMove(int row, int col, C card);

  /**
   * Checks if it's legal to play card at given index from hand.
   *
   * @param row the row index
   * @param col the column index
   * @param cardIndex the index of card in current player's hand
   * @return true if the move is legal, false otherwise
   */
  boolean isLegalMove(int row, int col, int cardIndex);

  /**
   * Gets the row score for a specific player in a specific row.
   *
   * @param player the player
   * @param rowIndex the row index
   * @return the score for that player in that row
   */
  int getRowScore(PlayerInterface<C> player, int rowIndex);

  /**
   * Gets the total score for a player across all rows.
   *
   * @param player the player
   * @return the player's total score
   */
  int getPlayerScore(PlayerInterface<C> player);

  /**
   * Gets the winner of the game, if any.
   *
   * @return the winning player, or null if game not over or tied
   */
  PlayerInterface<C> getWinner();

  /**
   * Gets the current player.
   *
   * @return the current player
   */
  PlayerInterface<C> getCurrentPlayer();

  /**
   * Gets the red player.
   *
   * @return the red player
   */
  PlayerInterface<C> getRedPlayer();

  /**
   * Gets the blue player.
   *
   * @return the blue player
   */
  PlayerInterface<C> getBluePlayer();

  /**
   * Creates a deep copy of the current board.
   *
   * @return a copy of the board with copied cells
   */
  J[][] copyBoard();

  /**
   * Plays a card from the current player's hand at the specified position.
   * Automatically draws a new card after playing.
   *
   * @param row the row index
   * @param col the column index
   * @param cardIndex the index of the card in the hand
   */
  void playCardFromHand(int row, int col, int cardIndex);

  /**
   * Checks if a move is valid.
   *
   * @param row  the row
   * @param col  the column
   * @param card the card
   * @return true if valid, false otherwise
   */
  public boolean isValidMove(int row, int col, Card card);

  /**
   * Sets game over state.
   *
   * @param over true if game is over
   */
  public void setGameOver(boolean over);

}
