package model;

/**
 * CardInterface interface.
 * This interface is responsible for the card of the Sanguine game.
 */
public interface CardInterface {

  /**
   * Checks if card is valid.
   *
   * @param cost cost of card
   * @param value value of card
   * @param influence influence of card
   * @return true if card is valid, false if not
   */
  public boolean isValid(int cost, int value, boolean[][] influence);

  /**
   * Positions of card influence.
   *
   * @return array of Ts anf Fs where card has influence
   */
  public boolean[][] getInfluence();

  /**
   * cost of card.
   *
   * @return cost of card
   */
  public int getCost();

  /**
   * value of card.
   *
   * @return value of card.
   */
  public int getValue();

  /**
   * gets name of card.
   *
   * @return name of card
   */
  public String getName();

}
