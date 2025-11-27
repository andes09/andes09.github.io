package klondike.model.hw02;

import java.util.Objects;

/**
 * Simulates a single playing card.
 * Contains card value, color and suit
 * Also has if its face up or not
 */
public class SingleCard implements SingleCardInterface {

  private final int value;
  public final String color;
  private final String suit;
  private boolean faceUp;

  /**
   *  Sets up basic card values.
   *  Makes sure all card values are legal.
   *  no black diamonds, or red spades, etc
   *  ALl cards are face down
   *
   * @param value the face value or number of the card.
   * @param suit the suit of teh card( heart, diamond,
   */
  public SingleCard(int value, String suit) {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Card value must be between 1 and 13");
    }

    this.value = value;

    if (!(suit.equalsIgnoreCase("clubs") || suit.equalsIgnoreCase("spades")
        || suit.equalsIgnoreCase("hearts") || suit.equalsIgnoreCase("diamonds"))) {
      throw new IllegalArgumentException("Card suit not valid");
    }

    this.suit = suit;

    if (suit.equalsIgnoreCase("clubs") || suit.equalsIgnoreCase("spades")) {
      this.color = "BLACK";
    } else {
      this.color = "RED";
    }
    this.faceUp = false;
  }

  @Override
  public String toString() {
    String str = "";
    switch (this.value) {
      case 1:
        str += "A";
        break;
      case 11:
        str += "J";
        break;
      case 12:
        str += "Q";
        break;
      case 13:
        str += "K";
        break;
      default:
        str += this.value;
    }

    if (this.suit.equalsIgnoreCase("clubs")) {
      return str += "♣";
    } else if (this.suit.equalsIgnoreCase("spades")) {
      return str += "♠";
    } else if (this.suit.equalsIgnoreCase("hearts")) {
      return str += "♡";
    } else {
      return str += "♢";
    }
  }


  @Override
  public boolean equals(Object o) {
    if (o instanceof SingleCard) {
      SingleCard card = (SingleCard) o;
      return (this.value == card.value && this.suit.equals(card.suit));
    }
    return false;
  }


  @Override
  public int hashCode() {
    return Objects.hash(this.value, this.suit);
  }

  @Override
  public void flipCard() {
    this.faceUp = !this.faceUp;
  }

  @Override
  public boolean isFaceUp() {
    return faceUp;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public String getSuit() {
    return suit;
  }

  @Override
  public String getColor() {
    return color;
  }
}
