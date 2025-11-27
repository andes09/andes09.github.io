package klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;


/**
 * Abstract implementation of Klondike that contains common functionality
 * shared between different variants of the game.
 */
public abstract class AbstractKlondikeModel implements KlondikeModel<SingleCard> {
  protected List<SingleCard> deck;
  protected List<SingleCard> remainingCards;
  protected List<List<SingleCard>> foundationPiles;
  protected List<List<SingleCard>> cascadePiles;
  protected List<SingleCard> drawPile;

  protected int runSize;
  protected String[] suits;

  protected boolean shuffle;

  protected boolean gameStarted;
  protected boolean gameOver;

  protected int score;
  protected int numPiles;
  protected int numDraw;

  /**
   * Initializes the model with default values.
   */
  public AbstractKlondikeModel() {
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

    // Initialize cascade piles
    for (int num = 0; num < numPiles; num++) {
      this.cascadePiles.add(new ArrayList<SingleCard>());
    }

    // Deal cards - delegated to subclasses
    dealInitialCards(deckCopy, numPiles);

    // Check if we had enough cards
    if (deckCopy.isEmpty() && this.cascadePiles.get(0).isEmpty()) {
      throw new IllegalArgumentException("Deck too small");
    }

    // Initialize foundation piles
    for (String suit : allSuits) {
      this.foundationPiles.add(new ArrayList<SingleCard>());
    }

    // Draw initial draw cards
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

  /**
   * Deals initial cards to cascade piles.
   * this method is game-specific
   * so its defined in subclasses.
   *
   * @param deck     List of Cards
   * @param numPiles number of piles to distrbute cards to
   */
  protected abstract void dealInitialCards(List<SingleCard> deck, int numPiles);

  /**
   * Cehekcs if moving a card to cascade pile is valid
   * this method is game-specific
   * so its defined in subclasses.
   *
   * @param destPile index of destination pile
   * @param card     card to be moved
   * @return true if move is valid, false otherwise
   */
  protected abstract boolean isValidCascadeMove(int destPile, SingleCard card);

  /**
   * Checks if a card can be moved to an empty cascade pile.
   * this method is game-specific
   * so its defined in subclasses.
   *
   * @param card card to be checkwd
   * @return true if move is valid, false otherwise
   */
  protected abstract boolean canMoveToEmptyPile(SingleCard card);

  /**
   * Checks if a list of cards can be moved to a cascade pile.
   * this method is game-specific
   * so its defined in subclasses.
   *
   * @param cardsToMove list of cards to be moved
   * @return true if move is valid, false otherwise
   */
  protected abstract boolean isValidPileMove(List<SingleCard> cardsToMove);

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (srcPile < 0 || srcPile >= this.cascadePiles.size()
        || this.cascadePiles.get(srcPile).isEmpty()) {
      throw new IllegalArgumentException("Invalid source pile");
    }
    if (destPile < 0 || destPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid destination pile");
    }
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Source and destination cannot be the same");
    }
    if (numCards <= 0 || numCards > this.cascadePiles.get(srcPile).size()) {
      throw new IllegalArgumentException("Invalid number of cards");
    }

    List<SingleCard> initPile = this.cascadePiles.get(srcPile);
    List<SingleCard> subPile =
        new ArrayList<>(initPile.subList(initPile.size() - numCards, initPile.size()));
    SingleCard initCard = subPile.getFirst();

    if (!isValidPileMove(new ArrayList<>(subPile))) {
      throw new IllegalStateException("Invalid Move");
    }

    if (checkReqs(destPile, initCard, "c")) {
      for (SingleCard card : subPile) {
        this.cascadePiles.get(destPile).add(card);
      }

      List<SingleCard> sourcePile = this.cascadePiles.get(srcPile);
      for (int i = 0; i < numCards; i++) {
        sourcePile.removeLast();
      }

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
      throw new IllegalArgumentException("Invalid destination pile");
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
      throw new IllegalStateException("Invalid Move");
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
      throw new IllegalArgumentException("Invalid source pile");
    }
    if (foundationPile < 0 || foundationPile >= this.foundationPiles.size()) {
      throw new IllegalArgumentException("Invalid foundation pile");
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
    if (foundationPile < 0 || foundationPile >= this.foundationPiles.size()) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }
    SingleCard card = this.drawPile.getFirst();

    if (checkReqs(foundationPile, card, "f")) {
      this.foundationPiles.get(foundationPile).add(card);
      this.drawPile.removeFirst();
      if (!this.remainingCards.isEmpty()) {
        this.remainingCards.getFirst().flipCard();
        this.drawPile.add(this.remainingCards.removeFirst());
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

    // Draw the next card from the remaining deck (not including the one we just
    // discarded)
    while (this.drawPile.size() < this.numDraw && !this.remainingCards.isEmpty()) {
      SingleCard nextCard = this.remainingCards.removeFirst();
      if (!nextCard.isFaceUp()) {
        nextCard.flipCard();
        this.drawPile.add(nextCard);
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
   * Removes top card from source pile then makes new card visible.
   *
   * @param srcPile pile that needs modifying.
   * @param remove  whether to remove the card
   */
  protected void removeAndFlipNext(int srcPile, boolean remove) {
    if (remove) {
      if (!this.cascadePiles.get(srcPile).isEmpty()) {
        this.cascadePiles.get(srcPile).removeLast();

        if (!this.cascadePiles.get(srcPile).isEmpty()
            && !this.cascadePiles.get(srcPile).getLast().isFaceUp()) {
          this.cascadePiles.get(srcPile).getLast().flipCard();
        }
      }
    } else {
      if (!this.cascadePiles.get(srcPile).isEmpty()
          && !this.cascadePiles.get(srcPile).getLast().isFaceUp()) {
        this.cascadePiles.get(srcPile).getLast().flipCard();
      }
    }
  }

  /**
   * Checks if moving card to foundation or cascade pile is valid move.
   *
   * @param destPile Specific Pile
   * @param card     specific card
   * @param type     type of pile ("f" for foundation, "c" for cascade)
   * @return whether or not it is a valid move or not
   */
  protected boolean checkReqs(int destPile, SingleCard card, String type) {
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
        return canMoveToEmptyPile(card);
      } else {
        if (pile.getLast().getValue() == 1) {
          return false;
        }
        return isValidCascadeMove(destPile, card);
      }
    }
  }

  /**
   * Checks if player has valid moves.
   *
   * @return whether or not the player has valid moves
   */
  protected boolean hasValidMoves() {
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
        // checks for valid moves at foundation piles
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
      // Can always discard draw cards if there are remaining cards or multiple draw
      // cards
      if (!remainingCards.isEmpty() || drawPile.size() > 1) {
        return true;
      }
    }
    return false;
  }
}
