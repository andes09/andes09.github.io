package model;

import java.awt.Color;
import java.util.List;

/**
 * Interface for Player objects in the Sanguine game.
 *
 * @param <C> the type of card interface
 */
public interface PlayerInterface<C extends CardInterface> {
  /**
   * gets player color.
   *
   * @return player color
   */
  public Color getPlayerColor();


  /**
   * Gets player hands.
   *
   * @return players hand
   */
  public List<C> getHand();

  /**
   * Whenever card is played a new card is added to hand.
   *
   * @return new hand
   */
  public List<C> drawNewCard();

  /**
   * players plays card and removes it from hand.
   *
   * @param card card poistion
   */
  public C playCard(int card);

}
