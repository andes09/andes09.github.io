package klondike.model.hw02;

/**
 * Additional methods for a singular card.
 * Such as flipping the card
 * and checking if its face up
 */
public interface SingleCardInterface extends Card {
  /**
   * Flips the card over ( changes state).
   *
   */
  public void flipCard();

  /**
   * Determines if card is face up or not.
   *
   * @return face up state of card
   */
  public boolean isFaceUp();

  /**
   * returns card value.
   *
   * @return card value
   */
  public int getValue();

  /**
   * returns the suit.
   *
   * @return suit
   */
  public String getSuit();

  /**
   * returns color of the card.
   *
   * @return color of card
   */
  public String getColor();
}
