package strategy;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Cell;
import model.PlayerInterface;
import model.SanguineModelInterface;

/**
 * A composite strategy that chains multiple strategies together.
 * Uses the first strategy to find candidate moves, then uses subsequent
 * strategies to break ties.
 */
public class CompositeStrategy implements SanguineStrategyInterface {
  private final List<SanguineStrategyInterface> strategies;

  /**
   * Constructs a composite strategy with the given list of strategies.
   * Strategies are applied in order to break ties.
   *
   * @param strategies list of strategies to chain
   */
  public CompositeStrategy(List<SanguineStrategyInterface> strategies) {
    if (strategies == null || strategies.isEmpty()) {
      throw new IllegalArgumentException("Must provide at least one strategy");
    }
    this.strategies = new ArrayList<>(strategies);
  }

  /**
   * Convenience constructor for two strategies.
   *
   * @param first the first strategy
   * @param second the second strategy (used to break ties)
   */
  public CompositeStrategy(SanguineStrategyInterface first,
                          SanguineStrategyInterface second) {
    this.strategies = new ArrayList<>();
    this.strategies.add(first);
    this.strategies.add(second);
  }

  /**
   * Convenience constructor for three strategies.
   *
   * @param first the first strategy
   * @param second the second strategy (used to break ties)
   * @param third the third strategy (used to break remaining ties)
   */
  public CompositeStrategy(SanguineStrategyInterface first,
                          SanguineStrategyInterface second,
                          SanguineStrategyInterface third) {
    this.strategies = new ArrayList<>();
    this.strategies.add(first);
    this.strategies.add(second);
    this.strategies.add(third);
  }

  @Override
  public List<Move> chooseMoves(SanguineModelInterface<Card, Cell<Card>> model,
                                PlayerInterface<Card> player) {
    List<Move> candidateMoves = strategies.get(0).chooseMoves(model, player);

    if (candidateMoves.size() <= 1) {
      return candidateMoves;
    }

    for (int i = 1; i < strategies.size() && candidateMoves.size() > 1; i++) {
      candidateMoves = filterMoves(candidateMoves, strategies.get(i),
                                   model, player);
    }

    return candidateMoves;
  }

  private List<Move> filterMoves(List<Move> candidates,
                                 SanguineStrategyInterface filterStrategy,
                                 SanguineModelInterface<Card, Cell<Card>> model,
                                 PlayerInterface<Card> player) {
    List<Move> filterMoves = filterStrategy.chooseMoves(model, player);

    List<Move> filtered = new ArrayList<>();
    for (Move candidate : candidates) {
      if (containsMove(filterMoves, candidate)) {
        filtered.add(candidate);
      }
    }

    return filtered.isEmpty() ? candidates : filtered;
  }

  private boolean containsMove(List<Move> moves, Move target) {
    for (Move move : moves) {
      if (move.equals(target)) {
        return true;
      }
    }
    return false;
  }
}

