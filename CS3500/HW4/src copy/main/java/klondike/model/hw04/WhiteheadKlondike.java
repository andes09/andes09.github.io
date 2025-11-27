package klondike.model.hw04;

import java.util.List;
import klondike.model.hw02.SingleCard;
import klondike.model.hw04.AbstractKlondikeModel;



/**
 * Implementation of Whitehead Klondike.
 * 1. All cascades cards are death face up
 * 2. Builds must be same color
 * 3. When moving multiple cards, they must be the same suit
 * Any card, can be moved to an empty cascade pile.
 */
public class WhiteheadKlondike extends AbstractKlondikeModel {

  @Override
  protected void dealInitialCards(List<SingleCard> deckCopy, int numPiles) {
    for (int startPile = 0; startPile < numPiles; startPile++) {
      for (int pile = startPile; pile < numPiles; pile++) {
        if (!deckCopy.isEmpty()) {
          SingleCard card = deckCopy.removeFirst();
          card.flipCard();
          this.cascadePiles.get(pile).add(card);
        } else {
          throw new IllegalArgumentException("Deck too small");
        }
      }
    }
  }

  @Override
  protected boolean isValidCascadeMove(int destPile, SingleCard card) {
    List<SingleCard> pile = this.cascadePiles.get(destPile);
    SingleCard topCard = pile.getLast();
    return topCard.getValue() == card.getValue() + 1
        && topCard.getColor().equals(card.getColor());
  }

  @Override
  protected boolean canMoveToEmptyPile(SingleCard card) {
    return true;
  }

  @Override
  protected boolean isValidPileMove(List<SingleCard> cardsToMove) {
    if (cardsToMove.size() == 1) {
      return true;
    }

    String suit = cardsToMove.getFirst().getSuit();
    for (SingleCard card : cardsToMove) {
      if (!card.getSuit().equals(suit)) {
        return false;
      }
    }

    for (int card = 0; card < cardsToMove.size() - 1; card++) {
      if (cardsToMove.get(card).getValue() != cardsToMove.get(card + 1).getValue() + 1) {
        return false;
      }
    }
    return true;
  }

}
