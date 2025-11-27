package strategy;

/**
 * Represents a move in the Sanguine game.
 * Contains the card index in hand and the board coordinates.
 */
public class Move {
  private final int cardIndex;
  private final int row;
  private final int col;

  /**
   * Constructs a move.
   *
   * @param cardIndex the index of the card in the player's hand
   * @param row the row coordinate
   * @param col the column coordinate
   */
  public Move(int cardIndex, int row, int col) {
    this.cardIndex = cardIndex;
    this.row = row;
    this.col = col;
  }

  /**
   * Gets the card index.
   *
   * @return the card index
   */
  public int getCardIndex() {
    return cardIndex;
  }

  /**
   * Gets the row.
   *
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /**
   * Gets the column.
   *
   * @return the column
   */
  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return "Move{card=" + cardIndex + ", row=" + row + ", col=" + col + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Move)) {
      return false;
    }
    Move other = (Move) obj;
    return this.cardIndex == other.cardIndex
        && this.row == other.row
        && this.col == other.col;
  }

  @Override
  public int hashCode() {
    return cardIndex * 1000 + row * 100 + col;
  }
}
