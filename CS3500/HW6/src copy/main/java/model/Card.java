package model;

/**
 * Card class implements CardInterface.
 * This class is responsible for the card of the Sanguine game.
 */
public class Card implements CardInterface {
  private String name;
  private int cost;
  private int value;
  private boolean[][] influence;

  /**
   * sets up single card.
   *
   * @param name name of card
   * @param cost cost of card
   * @param value value of card
   * @param influence 5 x 5 grid of cards influence
   */
  public Card(String name, int cost, int value, boolean[][] influence) {
    this.name = name;
    this.cost = cost;
    this.value = value;
    this.influence = influence;
  }

  @Override
  public boolean isValid(int cost, int value, 
      boolean[][] influence) {
    if (cost < 1 || cost > 3 || value < 1) {
      throw new IllegalArgumentException("Invalid Card");
    }
    if (influence.length != 5) {
      throw new IllegalArgumentException("Invalid Card ");
    }
    for (boolean[] row : influence) {
      if (row.length != 5 || row == null) {
        throw new IllegalArgumentException("Invalid Card ");
      }
    }

    return true;
  }

  @Override
  public boolean[][] getInfluence() {
    return influence;
  }

  @Override
  public int getCost() {
    return cost;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public String getName() {
    return name;
  }
}
