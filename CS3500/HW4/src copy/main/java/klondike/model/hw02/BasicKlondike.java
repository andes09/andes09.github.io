package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import klondike.model.hw04.AbstractKlondikeModel;

/**
 * Implementation of klondike game.
 * Allows for fully custom deck
 * Otherwise all other rules and regulations are the same
 */
public class BasicKlondike extends AbstractKlondikeModel {

  @Override
  protected void dealInitialCards(List<SingleCard> deckCopy, int numPiles) {
    for (int startPile = 0; startPile < numPiles; startPile++) {
      for (int pile = startPile; pile < numPiles; pile++) {
        if (!deckCopy.isEmpty()) {
          this.cascadePiles.get(pile).add(deckCopy.removeFirst());
        } else {
          throw new IllegalArgumentException("Deck too small");
        }
      }
      this.cascadePiles.get(startPile).getLast().flipCard();
    }
  }

  @Override
  protected boolean isValidCascadeMove(int destPile, SingleCard card) {
    List<SingleCard> pile = this.cascadePiles.get(destPile);
    SingleCard topCard = pile.getLast();
    return topCard.getValue() == card.getValue() + 1 && !topCard.getColor().equals(card.getColor());
  }

  @Override
  protected boolean isValidPileMove(List<SingleCard> cardsToMove) {
    for (SingleCard card : cardsToMove) {
      if (!card.isFaceUp()) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean canMoveToEmptyPile(SingleCard card) {
    return card.getValue() == 13;
  }
}