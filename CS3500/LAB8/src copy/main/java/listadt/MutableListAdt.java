package listadt;

/**
 * This interface represents a mutable list. It extends ListAdt to provide
 * all mutating and observer operations, and adds a method to get an immutable counterpart.
 *
 * @param <T> the type of elements in this list
 */
public interface MutableListAdt<T> extends ListAdt<T> {
  /**
   * Return an immutable counterpart of this mutable list. The immutable list contains
   * the same data in the same sequence as this list, but cannot be mutated.
   *
   * @return an immutable list with the same data in the same sequence
   */
  ImmutableListAdt<T> getImmutableList();
}

