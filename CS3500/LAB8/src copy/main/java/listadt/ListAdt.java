package listadt;

import java.util.function.Function;

/**
 * This interface represents a generic list.
 *
 * @param <T> the type of elements in this list
 */
public interface ListAdt<T> extends CommonListAdt<T> {
  /**
   * Add an object to the front of this list.
   *
   * @param object the object to be added to the front of this list
   */
  void addFront(T object);

  /**
   * Add an object to the back of this list (so it is the last object in the
   * list.
   *
   * @param object the object to be added to teh back of this list
   */
  void addBack(T object);

  /**
   * Add an object to this list so that it occupies the provided index. Index
   * begins with 0.
   *
   * @param index the index to be occupied by this object, beginning at 0
   * @param object     the object to be added to the list
   */
  void add(int index, T object);


  /**
   * Remove the first instance of this object from this list.
   *
   * @param object the object to be removed
   */
  void remove(T object);

  /**
   * A general purpose map higher order function on this list, that returns
   * the corresponding list of type R.
   *
   * @param converter the function that converts T into R
   * @param <R>       the type of data in the resulting list
   * @return the resulting list that is identical in structure to this list,
   *          but has data of type R
   */
  @Override
  <R> ListAdt<R> map(Function<T, R> converter);
  
  /**
   * Return the number of objects currently in this list.
   *
   * @return the size of the list
   */
  int getSize();

  /**
   * Get the (index)th object in this list.
   *
   * @param index the index of the object to be returned
   * @return the object at the given index
   * @throws IllegalArgumentException if an invalid index is passed
   */
  T get(int index) throws IllegalArgumentException;

}
