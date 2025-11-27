package model;

/**
 * Cell class implements CellInterface.
 * This class is responsible for the cell of the Sanguine game.
 */
public class Cell implements CellInterface<CardInterface> {
  // INVARIANT: If (pawns == 0 AND card == null), then empty must be true and owner must be null.
  // This invariant ensures that a truly empty cell (no pawns and no card) is marked as empty
  // and has no owner. However, a cell with a card can have pawns == 0 and still have an owner,
  // since placing a card removes pawns but maintains ownership. This is enforced in the
  // updatePawns method which only clears the owner when pawns reach 0 (typically when a card
  // will be placed), and setCard always sets empty to false.
  private boolean empty;
  private int pawns;
  private CardInterface card;
  private PlayerInterface<CardInterface> owner;

  /**
   * sets up initial cell.
   */
  public Cell() {
    empty = true;
    pawns = 0;
    card = null;
    owner = null;
  }

  @Override
  public boolean isEmpty() {
    return empty;
  }

  @Override
  public int getNumPawns() {
    return pawns;
  }

  @Override
  public CardInterface getCard() {
    return card;
  }

  @Override
  public void updatePawns(int pawns) {
    this.pawns = pawns;
    if (pawns == 0 && card == null) {
      empty = true;
      owner = null;
    } else {
      empty = false;
    }
  }

  @Override
  public void setCard(CardInterface card) {
    this.card = card;
    empty = false;
  }

  @Override
  public PlayerInterface<CardInterface> getOwner() {
    return owner;
  }

  @Override
  public void setOwner(PlayerInterface<CardInterface> owner) {
    this.owner = owner;
  }
}
