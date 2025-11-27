package listadt;

/**
 * This interface represents an immutable list. It extends CommonListADT to provide
 * all observer operations, and adds a method to get a mutable counterpart.
 *
 * @param <T> the type of elements in this list
 */
public interface ImmutableListAdt<T> extends CommonListAdt<T> {
  /**
   * Return a mutable counterpart of this immutable list. The mutable list contains
   * the same data in the same sequence as this list, but can be mutated.
   *
   * @return a mutable list with the same data in the same sequence
   */
  MutableListAdt<T> getMutableList();
}

