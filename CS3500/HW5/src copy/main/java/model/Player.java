package model;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a player in the Sanguine game.
 */
public class Player implements PlayerInterface {

  private Color color;
  private List<Card> hand;
  private List<Card> deck;
  private String fileName;

  /**
   * Constructs a Player with a color and deck file.
   *
   * @param color the player's color
   * @param fileName the deck file name
   * @throws IllegalStateException if deck is invalid
   */
  public Player(Color color, String fileName) throws IllegalStateException {
    this.color = color;
    this.fileName = fileName;
    try {
      this.deck = createDeck();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    if (this.deck.size() != 15) {
      throw new IllegalStateException("Invalid deck size");
    }
    this.hand = createHand(deck);

  }

  /**
   * Constructs a Player with only a color.
   *
   * @param color the player's color
   */
  public Player(Color color) {
  }

  @Override
  public Color getPlayerColor() {
    return color;
  }


  @Override
  public List<Card> getHand() {
    return hand;
  }

  @Override
  public List<Card> drawNewCard() throws IllegalStateException {
    if (!this.deck.isEmpty()) {
      hand.add(deck.removeFirst());
    } else {
      throw new IllegalStateException("Deck is Empty");
    }
    return hand;
  }

  private List<Card> createDeck() throws IOException {
    List<Card> deck = new ArrayList<>();
    String filePath = "docs" + File.separator + this.fileName;
    File config = new File(filePath);
    if (config.exists()) {
      try {
        Scanner sc = new Scanner(config);
        while (sc.hasNextLine()) {
          String[] nameCostValues =  sc.nextLine().split(" ");
          boolean[][] influence = new boolean[5][5];
          for (int row = 0; row < 5; row++) {
            influence[row] = convertConfig(sc.nextLine());
          }
          deck.add(new Card(nameCostValues[0],
              Integer.parseInt(nameCostValues[1]),
              Integer.parseInt(nameCostValues[2]), influence));
        }
      } catch (Exception e) {
        throw new IOException("Error reading config file");
      }
    } else {
      throw new IllegalArgumentException("Deck File Not Found");
    }
    Collections.shuffle(deck);
    return deck;
  }

  private List<Card> createHand(List<Card> deck) throws IllegalArgumentException {
    if (deck == null) {
      throw new IllegalArgumentException("Deck is null");
    }
    List<Card> hand = new ArrayList<>();
    for (int count = 0; count < 5; count++) {
      hand.add(deck.removeFirst());
    }
    return hand;
  }

  @Override
  public Card playCard(int card) throws IllegalArgumentException {
    if (card < 0 || card > 5) {
      throw new IllegalArgumentException("Invalid Card Number");
    }
    return hand.remove(card);
  }

  private boolean[] convertConfig(String row) {
    boolean[] influenceRow = new boolean[5];
    String[] values = row.split("");
    for (int value = 0; value < values.length; value++) {
      if (values[value].equals("X")) {
        influenceRow[value] = false;
      } else if (values[value].equals("I")) {
        influenceRow[value] = true;
      } else if (values[value].equals("C")) {
        influenceRow[value] = false; // figure out how display
      }
    }
    return influenceRow;
  }

}
