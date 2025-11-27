package model;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import model.Card;
import model.Player;
import model.SanguineModel;
import view.SanguineTextual;



/**
 * Main class for the Sanguine game.
 */
public class Sanguine {
  /**
   * Main method to start the game.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java Sanguine <deckFile>");
      return;
    }
    File deckFile = new File(args[0]);
    if (!deckFile.exists()) {
      System.out.println("File not found: " + args[0]);
      return;
    }
    try {
      Player red = new Player(Color.RED, deckFile.getName());
      Player blue = new Player(Color.BLUE, deckFile.getName());
      SanguineModel model = new SanguineModel(red, blue, 3, 5);
      SanguineTextual view = new SanguineTextual(model);
      
      playGame(model, view, red, blue);
    } catch (Exception e) {
      System.out.println("Error starting game: " + e.getMessage());
      e.printStackTrace();
    }
  }


  /**
   * Main game loop.
   *
   * @param model the game model
   * @param view the game view
   * @param red the red player
   * @param blue the blue player
   */
  private static void playGame(SanguineModel model, SanguineTextual view,
                                Player red, Player blue) {

    System.out.println("=== SANGUINE GAME STARTED ===");
    System.out.println("Board dimensions: 3 rows x 5 columns");
    System.out.println("Red player starts on the left, Blue on the right\n"); 

    int turnCount = 0;
    int maxTurns = 30;
    Scanner scanner = new Scanner(System.in);
    while (!model.isGameOver() && turnCount < maxTurns) {
      PlayerInterface<Card> current = model.getCurrentPlayer();
      String playerName = current.getPlayerColor().equals(Color.RED) ? "RED" : "BLUE";
      
      System.out.println("\n" + "=".repeat(50));
      System.out.println("Turn " + (turnCount + 1) + " - " + playerName + " player's turn");
      System.out.println("=".repeat(50));
      System.out.println("\nCurrent Board:");
      System.out.println(view.render());
      
      List<Card> hand = current.getHand();
      if (hand == null || hand.isEmpty()) {
        System.out.println("No cards in hand! Drawing new card...");
        try {
          current.drawNewCard();
          hand = current.getHand();
        } catch (Exception e) {
          System.out.println("Cannot draw card: " + e.getMessage());
          System.out.println(playerName + " must pass.");
          model.pass();
          turnCount++;
          continue;
        }
      }
      
      System.out.println("\nYour hand:");
      for (int i = 0; i < hand.size(); i++) {
        Card card = hand.get(i);
        System.out.println("  [" + i + "] " + card.getName() 
            + " (Cost: " + card.getCost() + ", Value: " + card.getValue() + ")");
      }
      
      System.out.println("\nEnter your move:");
      System.out.println("  Format: <card_index> <row> <col>");
      System.out.println("  Example: 0 1 2 (plays card 0 at row 1, col 2)");
      System.out.println("  Or type 'pass' to skip your turn");
      System.out.print("> ");
      
      String input = scanner.nextLine().trim();
      
      if (input.equalsIgnoreCase("pass")) {
        System.out.println(playerName + " passed their turn.");
        model.pass();
        turnCount++;
        continue;
      }
      
      String[] parts = input.split("\\s+");
      if (parts.length != 3) {
        System.out.println("Invalid input! Please use format: <card_index> <row> <col>");
        continue;
      }
      
      try {
        int cardIndex = Integer.parseInt(parts[0]);
        int row = Integer.parseInt(parts[1]);
        int col = Integer.parseInt(parts[2]);
        
        if (cardIndex < 0 || cardIndex >= hand.size()) {
          System.out.println("Invalid card index! Choose between 0 and " 
              + (hand.size() - 1));
          continue;
        }
        
        Card selectedCard = hand.get(cardIndex);
        
        if (!model.isValidMove(row, col, selectedCard)) {
          System.out.println("Invalid move! Requirements:");
          System.out.println("  - Position must be on the board (0-2 for rows, 0-4 for cols)");
          System.out.println("  - Cell must be owned by you");
          System.out.println("  - Cell must have enough pawns (>= card cost)");
          continue;
        }
        
        Card playedCard = current.playCard(cardIndex);
        model.placeCard(row, col, playedCard);
        
        try {
          current.drawNewCard();
        } catch (Exception e) {
          System.out.println("Deck is empty, no new card drawn.");
        }
        
        System.out.println("\n" + playerName + " played " + playedCard.getName() 
            + " at position (" + row + ", " + col + ")");
        
        turnCount++;
        
      } catch (NumberFormatException e) {
        System.out.println("Invalid input! Please enter numbers only.");
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
    
    System.out.println("\n" + "=".repeat(50));
    System.out.println("GAME OVER!");
    System.out.println("=".repeat(50));
    System.out.println("\nFinal Board:");
    System.out.println(view.render());
    
    if (turnCount >= maxTurns) {
      System.out.println("Game ended after " + maxTurns + " turns.");
    }
    
    System.out.println("\nThanks for playing Sanguine!");
    
    scanner.close();
  }
}
