package simon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Main implementation class of the game of Simon says. Allows access to a builder
 * to customize the game itself.
 */
public class SimonSays implements Simon {
  private final List<ColorGuess> colorGuesses;
  private final Random random;

  /**
   * INVARIANT: 0 <= currentColorIndex < Colors.size().
   * INTERPRETATION: the current progress the player has made in guessing the color sequence
   */
  private int currentColorIndex;

  /**
   * Builder for the class SimonSays. Allows for customization of the sequences in the game.
   */
  public static class Builder {
    Random random;
    List<ColorGuess> initialSequence;

    /**
     * Constructs the builder with a default Random object and an empty sequence.
     */
    public Builder() {
      this.random = new Random();
      this.initialSequence = new ArrayList<>();
      setInitialLength(1);
    }

    /**
     * Sets the random object for the builder to construct SimonSays with.
     *
     * @param random the random object for SimonSays
     * @return the builder that has this random object saved
     */
    public Builder setRandom(Random random) {
      this.random = Objects.requireNonNull(random);
      return this;
    }
      
    /**
     * Sets the initial sequence length for the builder to construct SimonSays with.
     *
     * @param initialLength the length of the initial sequence
     * @return the builder that has this length saved
     */      
    public Builder setInitialLength(int initialLength) {
      if (initialLength < 1) {
        throw new IllegalArgumentException("Length must be positive");
      }
      this.initialSequence.clear();
      for (int i = 0; i < initialLength; i++) {
        this.initialSequence.add(SimonSays.getRandomColor(this.random));
      }
      return this;
    }

    /**
     * Sets the initial sequence for the builder to construct SimonSays with.
     *
     * @param colorGuesses the sequence of Colors for the starting sequence
     * @return the builder that has this sequence saved
     */      
    public Builder setInitialSequence(ColorGuess... colorGuesses) {
      for (ColorGuess s : colorGuesses) {
        Objects.requireNonNull(s);
      }
      this.initialSequence.clear();
      this.initialSequence.addAll(List.of(colorGuesses));
      return this;
    }

    /**
     * Constructs the SimonSays object with the options saved
     * in the builder.
     *
     * @return a SimonSays object with all customizations made previously
     */     
    public SimonSays build() {
      return new SimonSays(this.random, this.initialSequence);
    }
  }

  private SimonSays(Random random, List<ColorGuess> initialSequence) {
    this.random = random;
    this.colorGuesses = new ArrayList<>(initialSequence);
  }

  private void addNewColor() {
    this.colorGuesses.add(getRandomColor(this.random));
    this.currentColorIndex = 0;
  }

  private static ColorGuess getRandomColor(Random random) {
    return ColorGuess.values()[random.nextInt(ColorGuess.values().length)];
  }

  @Override
  public List<ColorGuess> getCurrentSequence() {
    return Collections.unmodifiableList(this.colorGuesses);
  }

  @Override
  public boolean enterNextColor(ColorGuess guess) {
    if (guess == this.colorGuesses.get(this.currentColorIndex)) {
      this.currentColorIndex++;
      if (this.currentColorIndex == this.colorGuesses.size()) {
        addNewColor();
      }
      return true;
    } else {
      this.currentColorIndex = 0;
      return false;
    }
  }
}
