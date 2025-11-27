package listadt;

import java.util.function.Function;

/**
 * This is the implementation of a generic list. Specifically it implements
 * the listadt.ListADT interface
 */
public class ListAdtImpl<T> implements ListAdt<T> {
  private GenericListAdtNode<T> head;

  /**
   * Creates an empty list.
   */
  public ListAdtImpl() {
    head = new GenericEmptyNode();
  }

  //a private constructor that is used internally (see map)
  private ListAdtImpl(GenericListAdtNode<T> head) {
    this.head = head;
  }

  @Override
  public void addFront(T object) {
    head = head.addFront(object);
  }

  @Override
  public void addBack(T object) {
    head = head.addBack(object);
  }

  @Override
  public void add(int index, T object) {
    head = head.add(index, object);
  }

  @Override
  public int getSize() {
    return head.count();
  }

  @Override
  public void remove(T object) {
    head = head.remove(object);
  }

  @Override
  public T get(int index) throws IllegalArgumentException {
    if ((index >= 0) && (index < getSize())) {
      return head.get(index);
    }
    throw new IllegalArgumentException("Invalid index");

  }

  @Override
  public <R> ListAdt<R> map(Function<T, R> converter) {
    return new ListAdtImpl(head.map(converter));
  }

  @Override
  public String toString() {
    return "(" + head.toString() + ")";
  }
}
