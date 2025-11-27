package view;

/**
 * Interface for rendering a textual view of the Sanguine game.
 * The textual view displays the current state of the game board including:
 * - Row scores for both red and blue players
 * - Cell contents (empty cells, pawns, or cards)
 * - Current player ownership of cells
 */
public interface SanguineTextualView {

  /**
   * Renders the current state of the game as a string.
   * The format follows the pattern: [red_row_score] [cells] [blue_row_score]
   * where cells are represented as:
   * - '_' for empty cells
   * - '1', '2', or '3' for the number of pawns in a cell
   * - 'R' for a card owned by the red player
   * - 'B' for a card owned by the blue player
   *
   * @return a string representation of the current game state
   */
  String render();

  /**
   * Converts the current game state to a string representation.
   * This is equivalent to calling render().
   *
   * @return a string representation of the current game state
   */
  @Override
  String toString();
}

