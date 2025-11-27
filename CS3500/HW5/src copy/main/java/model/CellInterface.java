package model;

/**
 * Interface for Cell objects in the Sanguine game.
 *
 * @param <C> the type of card interface
 */
public interface CellInterface<C extends CardInterface> {
  /**
   * Checks if cell is empty or not.
   *
   * @return True for empty, False for not
   */
  public boolean isEmpty();

  /**
   * Number of Pawns in cell.
   *
   * @return number of pawns in cell
   */
  public int getNumPawns();

  /**
   * Card at Cell.
   *
   * @return card
   */
  public C getCard();

  /**
   * updates number of pawns in cell.
   *
   * @param pawns new number of pawns
   */
  public void updatePawns(int pawns);

  /**
   * places card in cell.
   *
   * @param card Card to be placed
   */
  public void setCard(C card);

  /**
   * Gets the owner of this cell.
   * The owner is the player who controls the pawns or card in this cell.
   *
   * @return the PlayerInterface representing the owner, or null if cell is empty
   */
  public PlayerInterface<C> getOwner();

  /**
   * Sets the owner of this cell.
   * This should be called when pawns or cards are placed/changed in the cell.
   *
   * @param owner the PlayerInterface representing the owner
   */
  public void setOwner(PlayerInterface<C> owner);

}
