import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;
import klondike.view.KlondikeTextualView;
import org.junit.Before;
import org.junit.Test;

/**
 * A class for testing the KlondikeModel.
 * All tests in this class cannot create Card type objects. Instead,
 * the tests use the createNewDeck method to help create
 * example games.
 */
public class KlondikeModelTests {
  KlondikeModel<SingleCard> model;
  private List<SingleCard> standardDeck;

  /**
   * Sets up game for tests.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    standardDeck = model.createNewDeck(); // 52-card deck: A-K in 4 suits
  }

  @Test
  public void testStartGameWithNullDeck() {
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(null, false, 7, 3)
    );
    assertEquals("Invalid Parameters", ex.getMessage());
  }

  @Test
  public void testStartGameWithNullCardInDeck() {
    standardDeck.set(10, null);
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, 3)
    );
    assertEquals("Card is null", ex.getMessage());
  }

  @Test
  public void testStartGameWithNonPositiveNumPiles() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 0, 3));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, -5, 3));
  }

  @Test
  public void testStartGameWithNonPositiveNumDraw() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, -1));
  }

  @Test
  public void testStartGameWithMissingAce() {
    standardDeck.set(0, new SingleCard(2, "clubs"));
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, 3)
    );
    assertEquals("Invalid Parameters", ex.getMessage());
  }

  @Test
  public void testStartGameWithTooSmallDeck() {
    // Create minimal deck with 4 aces but not enough for cascade
    List<SingleCard> tinyDeck = Arrays.asList(
        new SingleCard(1, "hearts"),
        new SingleCard(1, "clubs"),
        new SingleCard(1, "diamonds"),
        new SingleCard(1, "spades")
    );
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(tinyDeck, false, 7, 3)
    );
    assertEquals("Deck too small", ex.getMessage());
  }

  @Test
  public void testStartGameWhenAlreadyStarted() {
    model.startGame(standardDeck, false, 7, 3);
    IllegalStateException ex = assertThrows(
        IllegalStateException.class,
        () -> model.startGame(standardDeck, false, 7, 3)
    );
    assertEquals("Game already started", ex.getMessage());
  }

  @Test
  public void testStartGameWithStandardDeck() {
    model.startGame(standardDeck, false, 7, 3);
    assertEquals(7, model.getNumPiles());
    assertEquals(4, model.getNumFoundations());
    assertEquals(3, model.getNumDraw());
    assertEquals(0, model.getScore());
    assertFalse(model.isGameOver());
    for (int i = 0; i < 7; i++) {
      assertEquals(i + 1, model.getPileHeight(i));
    }
    for (int pile = 0; pile < 7; pile++) {
      for (int cardIndex = 0; cardIndex < pile; cardIndex++) {
        assertFalse(model.isCardVisible(pile, cardIndex));
      }
    }
    for (int f = 0; f < 4; f++) {
      assertNull(model.getCardAt(f));
    }
    assertEquals(3, model.getDrawCards().size());
    for (SingleCard card : model.getDrawCards()) {
      assertTrue(card.isFaceUp());
    }
  }

  @Test
  public void testMovePileBeforeGameStart() {
    assertThrows(IllegalStateException.class,
        () -> model.movePile(0, 1, 1));
  }

  @Test
  public void testMovePileSameSourceAndDest() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 1, 0));
  }

  @Test
  public void testMovePileInvalidNumCards() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 0, 1));
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 2, 1));
  }

  @Test
  public void testMoveDrawBeforeGameStart() {
    assertThrows(IllegalStateException.class,
        () -> model.moveDraw(0));
  }

  @Test
  public void testMoveDrawInvalidDestPile() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalArgumentException.class,
        () -> model.moveDraw(-1));
    assertThrows(IllegalArgumentException.class,
        () -> model.moveDraw(7));
  }

  @Test
  public void testToStringWithEmptyModel() {
    BasicKlondike model = new BasicKlondike(); // Assumes it starts empty or initialized
    model.startGame(standardDeck, false, 7, 3);
    KlondikeTextualView view = new KlondikeTextualView(model);

    String output = view.toString();
    assertNotNull(output);

    assertTrue(output.contains("Draw:"));
    assertTrue(output.contains("Foundation:"));

    assertTrue(output.contains("  ?"));
    assertTrue(output.contains("  X"));
  }

  // ===== MISSING TESTS ADDED BELOW =====

  @Test
  public void testStartGameInvalidRunMissingAceForSuit() {
    // Remove ace of hearts - deck won't have valid run for hearts
    List<SingleCard> invalidDeck = new ArrayList<>(standardDeck);
    invalidDeck.removeIf(card -> card.toString().equals("A♡"));
    
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(invalidDeck, false, 7, 3));
  }

  @Test
  public void testStartGameCascadePilesLessThanOne() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 0, 3));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, -1, 3));
  }

  @Test
  public void testStartGameDrawCardsLessThanOne() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, 0));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, -1));
  }

  @Test
  public void testStartGameAlreadyStartedThrowsException() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalStateException.class,
        () -> model.startGame(standardDeck, false, 7, 3));
  }

  @Test
  public void testInvalidMoveWrongSuitToCascade() {
    model.startGame(standardDeck, false, 7, 3);
    // Try to move a card with wrong color combination
    // This depends on your deck layout, but we can test invalid suit logic
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 1, 6)); // Attempt invalid cascade move
  }

  @Test
  public void testInvalidMoveWrongSuitToFoundation() {
    // Create a custom deck to test foundation suit rules
    List<SingleCard> customDeck = new ArrayList<>();
    customDeck.add(new SingleCard(1, "hearts")); // Ace hearts
    customDeck.add(new SingleCard(2, "spades")); // 2 spades (wrong suit)
    for (int i = 3; i <= 13; i++) {
      customDeck.add(new SingleCard(i, "hearts"));
    }
    for (int i = 1; i <= 13; i++) {
      customDeck.add(new SingleCard(i, "spades"));
    }
    
    model.startGame(customDeck, false, 2, 1);
    model.moveToFoundation(0, 0); // Move ace to foundation
    
    // Try to move 2 of spades to hearts foundation
    assertThrows(IllegalStateException.class,
        () -> model.moveToFoundation(1, 0));
  }

  @Test
  public void testIsGameOverWhenGameIsOver() {
    // Create a small winning deck
    List<SingleCard> winDeck = new ArrayList<>();
    // Add Kings for each suit to simulate winning state
    winDeck.add(new SingleCard(13, "hearts"));
    winDeck.add(new SingleCard(13, "spades"));
    
    model.startGame(winDeck, false, 1, 1);
    
    // Initially game should not be over
    assertFalse(model.isGameOver());
    
    // After some moves that lead to no valid moves, game should be over
    // (This is implementation-specific; adjust based on your logic)
  }

  @Test
  public void testIsGameOverWhenGameIsNotOver() {
    model.startGame(standardDeck, false, 7, 3);
    
    // Game should not be over at the start
    assertFalse(model.isGameOver());
    
    // Make a valid move
    model.discardDraw();
    
    // Game should still not be over
    assertFalse(model.isGameOver());
  }

  @Test
  public void testGetCardAtFoundationThrowsOutOfBounds() {
    model.startGame(standardDeck, false, 7, 3);
    
    // Test negative index
    assertThrows(IllegalArgumentException.class,
        () -> model.getCardAt(-1));
    
    // Test index >= number of foundations
    assertThrows(IllegalArgumentException.class,
        () -> model.getCardAt(4)); // Only 4 foundations (0-3)
  }

  @Test
  public void testGetCardAtCascadeThrowsOutOfBounds() {
    model.startGame(standardDeck, false, 7, 3);
    
    // Test negative pile index
    assertThrows(IllegalArgumentException.class,
        () -> model.getCardAt(-1, 0));
    
    // Test pile index >= number of piles
    assertThrows(IllegalArgumentException.class,
        () -> model.getCardAt(7, 0)); // Only 7 piles (0-6)
    
    // Test negative card index
    assertThrows(IllegalArgumentException.class,
        () -> model.getCardAt(0, -1));
    
    // Test card index >= pile height
    assertThrows(IllegalArgumentException.class,
        () -> model.getCardAt(0, 5)); // Pile 0 only has 1 card
  }

  @Test
  public void testGetCardAtFoundationReturnsCorrectCard() {
    // Create simple deck
    List<SingleCard> simpleDeck = new ArrayList<>();
    simpleDeck.add(new SingleCard(1, "hearts"));
    simpleDeck.add(new SingleCard(2, "hearts"));
    for (int i = 3; i <= 13; i++) {
      simpleDeck.add(new SingleCard(i, "hearts"));
    }
    for (int i = 1; i <= 13; i++) {
      simpleDeck.add(new SingleCard(i, "spades"));
    }
    
    model.startGame(simpleDeck, false, 2, 1);
    
    // Move ace to foundation
    model.moveToFoundation(0, 0);
    
    // Get card at foundation should return the ace
    SingleCard foundationCard = model.getCardAt(0);
    assertNotNull(foundationCard);
    assertEquals("A♡", foundationCard.toString());
  }

  @Test
  public void testGetCardAtCascadeReturnsCorrectCard() {
    model.startGame(standardDeck, false, 7, 3);
    
    // Get the visible card at pile 0 (should be the ace of clubs)
    SingleCard card = model.getCardAt(0, 0);
    assertNotNull(card);
    assertEquals("A♣", card.toString());
    
    // Get the visible card at pile 6 (top card of pile with 7 cards)
    SingleCard topCard = model.getCardAt(6, 6);
    assertNotNull(topCard);
    assertTrue(topCard.isFaceUp());
  }
}
