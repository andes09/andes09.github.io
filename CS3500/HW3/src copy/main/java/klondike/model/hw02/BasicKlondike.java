package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of klondike game.
 * Allows for fully custom deck
 * Otherwise all other rules and regulations are the same
 */
public class BasicKlondike implements KlondikeModel<SingleCard> {
  private List<SingleCard> deck;
  private List<SingleCard> remainingCards;
  private List<List<SingleCard>> foundationPiles;
  private List<List<SingleCard>> cascadePiles;
  private List<SingleCard> drawPile;

  private int runSize;
  private String[] suits;

  private boolean shuffle;

  private boolean gameStarted;
  private boolean gameOver;

  private int score;
  private int numPiles;
  private int numDraw;

  /**
   * Sets up game with normal 52 card deck.
   * and three draw cards
   */
  public BasicKlondike() {
    this.score = 0;
    this.runSize = 13;
    this.numPiles = 7;
    this.suits = new String[] {"clubs", "diamonds", "hearts", "spades"};
    this.shuffle = false;
    this.deck = createNewDeck();
    this.cascadePiles = new ArrayList<>();
    this.foundationPiles = new ArrayList<>();
    this.drawPile = new ArrayList<>();
    this.remainingCards = new ArrayList<>();
    this.numDraw = 3;
    this.gameStarted = false;
    this.gameOver = false;

  }

  @Override
  public List<SingleCard> createNewDeck() {
    List<SingleCard> newDeck = new ArrayList<>();
    for (String suit : suits) {
      for (int j = 1; j <= this.runSize; j++) {
        newDeck.add(new SingleCard(j, suit));
      }
    }
    return newDeck;
  }

  @Override
  public void startGame(List<SingleCard> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {

    if (numDraw < 0 || numPiles <= 0 || deck == null) {
      throw new IllegalArgumentException("Invalid Parameters");
    }
    for (SingleCard deckCard : deck) {
      if (deckCard == null) {
        throw new IllegalArgumentException("Card is null");
      }
    }
    if (this.gameStarted) {
      throw new IllegalStateException("Game already started");
    }
    Set<String> allSuits = new HashSet<>();
    Set<String> suitsWithAce = new HashSet<>();

    for (SingleCard deckCard : deck) {
      allSuits.add(deckCard.getSuit());
      if (deckCard.getValue() == 1) {
        suitsWithAce.add(deckCard.getSuit());
      }
    }
    if (!suitsWithAce.equals(allSuits)) {
      throw new IllegalArgumentException("Invalid Parameters");
    }
    List<SingleCard> deckCopy = new ArrayList<>(deck);

    if (shuffle) {
      Collections.shuffle(deckCopy);
    }
    for (int num = 0; num < numPiles; num++) {
      this.cascadePiles.add(new ArrayList<SingleCard>());
    }
    for (int startPile = 0; startPile < numPiles; startPile++) {
      for (int pile = startPile; pile < numPiles; pile++) {
        if (!deckCopy.isEmpty()) {
          this.cascadePiles.get(pile).add(deckCopy.removeFirst());
        } else {
          throw new IllegalArgumentException("Deck to small");
        }

      }
      this.cascadePiles.get(startPile).getLast().flipCard();
    }
    for (String suit : allSuits) {
      this.foundationPiles.add(new ArrayList<SingleCard>());
    }

    for (int num = 0; num < numDraw; num++) {
      if (!deckCopy.isEmpty()) {
        deckCopy.getFirst().flipCard();
        this.drawPile.add(deckCopy.removeFirst());
      }
    }
    this.numDraw = numDraw;
    this.numPiles = numPiles;
    this.gameStarted = true;
    this.remainingCards = new ArrayList<SingleCard>(deckCopy);
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (srcPile < 0 || srcPile >= this.cascadePiles.size()
        || this.cascadePiles.get(srcPile).isEmpty()) {
      throw new IllegalArgumentException("Invalid source Pile");
    }
    if (destPile < 0 || destPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid source Pile");
    }
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Source and destination cannot be the same");
    }
    if (numCards <= 0 || numCards > this.cascadePiles.get(srcPile).size()) {
      throw new IllegalArgumentException("Invalid number of cards");
    }
    List<SingleCard> initPile = this.cascadePiles.get(srcPile);
    List<SingleCard> subPile = initPile.subList(initPile.size() - numCards, initPile.size());
    List<SingleCard> finalPile = this.cascadePiles.get(destPile);
    SingleCard initCard = subPile.getFirst();

    if (checkReqs(destPile, initCard, "c")) {
      // Add all cards to destination first
      for (SingleCard card : subPile) {
        this.cascadePiles.get(destPile).add(card);
      }
      
      // Then remove them from source (remove from end to avoid index issues)
      List<SingleCard> sourcePile = this.cascadePiles.get(srcPile);
      for (int i = 0; i < numCards; i++) {
        sourcePile.removeLast();
      }

      // Flip the new top card if needed
      if (!sourcePile.isEmpty() && !sourcePile.getLast().isFaceUp()) {
        sourcePile.getLast().flipCard();
      }
    } else {
      throw new IllegalStateException("Invalid Move");
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (destPile < 0 || destPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid source Pile");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }

    if (checkReqs(destPile, this.drawPile.getFirst(), "c")) {
      this.cascadePiles.get(destPile).add(this.drawPile.removeFirst());

      if (!this.remainingCards.isEmpty()) {
        this.remainingCards.getFirst().flipCard();
        this.drawPile.add(this.remainingCards.removeFirst());
      }
    } else {
      throw new IllegalArgumentException("Invalid Move");
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (srcPile < 0 || srcPile >= this.cascadePiles.size()
        || this.cascadePiles.get(srcPile).isEmpty()) {
      throw new IllegalArgumentException("Invalid source Pile");
    }
    if (foundationPile >= this.foundationPiles.size() || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation Pile");
    }
    SingleCard topCard = this.cascadePiles.get(srcPile).getLast();

    if (checkReqs(foundationPile, topCard, "f")) {
      this.foundationPiles.get(foundationPile).add(topCard);
      removeAndFlipNext(srcPile, true);
      this.score++;
    } else {
      throw new IllegalStateException("Invalid Move");
    }

  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (foundationPile >= this.foundationPiles.size() || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation Pile");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }
    SingleCard card = this.drawPile.getFirst();

    if (checkReqs(foundationPile, card, "f")) {
      this.foundationPiles.get(foundationPile).add(card);
      this.drawPile.removeFirst();
      if (!this.remainingCards.isEmpty()) {
        SingleCard newCard = this.remainingCards.removeFirst();
        newCard.flipCard();
        this.drawPile.add(newCard);
      }
      this.score++;
    } else {
      throw new IllegalStateException("Invalid Move");
    }

  }

  @Override
  public void discardDraw() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Pile is empty");
    }
    SingleCard removedCard = this.drawPile.removeFirst();
    removedCard.flipCard();
    this.remainingCards.addLast(removedCard);

    // Draw the next card from the remaining deck (not including the one we just discarded)
    for (int i = 0; i < this.remainingCards.size() - 1; i++) {
      SingleCard nextCard = this.remainingCards.get(i);
      if (!nextCard.isFaceUp()) {
        nextCard.flipCard();
        this.drawPile.add(nextCard);
        this.remainingCards.remove(i);
        break;
      }
    }
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    int maxRows = 0;
    for (List<SingleCard> pile : this.cascadePiles) {
      if (pile.size() > maxRows) {
        maxRows = pile.size();
      }
    }
    return maxRows;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return cascadePiles.size();
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return drawPile.size();
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }

    // Check if won (all foundations complete)
    boolean won = true;
    for (int i = 0; i < this.getNumFoundations(); i++) {
      if (this.getCardAt(i) == null || this.getCardAt(i).toString().charAt(0) != 'K') {
        won = false;
        break;
      }
    }

    // Game is over if won OR if no valid moves exist
    return won || !hasValidMoves();
  }


  @Override
  public int getScore() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return this.score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum < 0 || pileNum >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    return this.cascadePiles.get(pileNum).size();
  }

  @Override
  public SingleCard getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum < 0 || pileNum >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    if (card < 0 || card >= this.cascadePiles.get(pileNum).size()) {
      throw new IllegalArgumentException("Invalid card number");
    }

    return this.cascadePiles.get(pileNum).get(card);
  }

  @Override
  public SingleCard getCardAt(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (foundationPile >= this.foundationPiles.size() || foundationPile < 0) {
      throw new IllegalArgumentException("Foundation pile is out of range or does not exist");
    }
    if (this.foundationPiles.get(foundationPile).isEmpty()) {
      return null;
    }
    List<SingleCard> pile = this.foundationPiles.get(foundationPile);

    return pile.getLast();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum < 0 || pileNum >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    if (card < 0 || card >= this.cascadePiles.get(pileNum).size()) {
      throw new IllegalArgumentException("Invalid card number");
    }
    List<SingleCard> chosenPile = this.cascadePiles.get(pileNum);
    SingleCard chosenCard = chosenPile.get(card);
    return chosenCard.isFaceUp();
  }

  @Override
  public List<SingleCard> getDrawCards() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return new ArrayList<>(this.drawPile);
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return this.foundationPiles.size();
  }

  /**
   * removes top card from source pile then makes new card visible.
   *
   * @param srcPile pile that needs modifying.
   */
  private void removeAndFlipNext(int srcPile, boolean remove) {
    if (remove) {
      if (!this.cascadePiles.get(srcPile).isEmpty()) {
        this.cascadePiles.get(srcPile).removeLast();
      }
    } else {
      if (!this.cascadePiles.get(srcPile).isEmpty()
          && !this.cascadePiles.get(srcPile).getLast().isFaceUp()) {
        this.cascadePiles.get(srcPile).getLast().flipCard();
      }
    }
  }

  /**
   * checks if moving card to foundation pile is valid move.
   *
   * @param destPile Specific Pile
   * @param card     specific card
   * @return whether or not it is a valid move or not
   */

  private boolean checkReqs(int destPile, SingleCard card, String type) {

    if (type.equals("f")) {
      List<SingleCard> pile = this.foundationPiles.get(destPile);
      if (pile.size() >= this.runSize) {
        return false;
      }
      if (!pile.isEmpty()) {
        SingleCard topCard = pile.getLast();
        return topCard.getValue() == card.getValue() - 1
            && topCard.getSuit().equals(card.getSuit());
      } else {
        return card.getValue() == 1;
      }
    } else { // cascade logic
      List<SingleCard> pile = this.cascadePiles.get(destPile);
      if (pile.isEmpty()) {
        return card.getValue() == 13;
      } else {
        if (pile.getLast().getValue() == 1) {
          return false;
        }
        SingleCard topCard = pile.getLast();
        return topCard.getValue() == card.getValue() + 1
            && !topCard.getColor().equals(card.getColor());
      }
    }
  }

  /**
   * Cheks if player has valids moves.
   *
   * @return whether or not the player has valid moves
   */

  private boolean hasValidMoves() {
    // Check moves from cascade piles to other cascade piles
    for (int src = 0; src < getNumPiles(); src++) {
      if (getPileHeight(src) > 0) {
        // checks for valid moves at cascade piles
        SingleCard srcCard = getCardAt(src, getPileHeight(src) - 1);
        for (int dest = 0; dest < getNumPiles(); dest++) {
          if (src != dest && checkReqs(dest, srcCard, "c")) {
            return true;
          }
        }
        //checks for valid moves at foundation piles
        for (int foundation = 0; foundation < getNumFoundations(); foundation++) {
          if (checkReqs(foundation, srcCard, "f")) {
            return true;
          }
        }
      }
    }
    
    // Check moves from draw pile (if draw pile has cards)
    if (!drawPile.isEmpty()) {
      SingleCard drawCard = drawPile.getFirst();
      
      // Check if draw card can move to any cascade pile
      for (int dest = 0; dest < getNumPiles(); dest++) {
        if (checkReqs(dest, drawCard, "c")) {
          return true;
        }
      }
      
      // Check if draw card can move to any foundation pile
      for (int foundation = 0; foundation < getNumFoundations(); foundation++) {
        if (checkReqs(foundation, drawCard, "f")) {
          return true;
        }
      }
      
      // Can always discard draw cards if there are remaining cards or multiple draw cards
      if (!remainingCards.isEmpty() || drawPile.size() > 1) {
        return true;
      }
    }
    
    return false;
  }
}