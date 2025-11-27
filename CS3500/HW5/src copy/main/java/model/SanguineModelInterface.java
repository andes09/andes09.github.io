package model;

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



}
