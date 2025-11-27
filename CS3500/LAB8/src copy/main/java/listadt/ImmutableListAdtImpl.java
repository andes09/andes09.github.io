package listadt;

import java.util.function.Function;

/**
 * This is the implementation of an immutable list. It implements the ImmutableListADT
 * interface using an object adapter pattern over ListAdtImpl.
 *
 * @param <T> the type of elements in this list
 */
public class ImmutableListAdtImpl<T> implements ImmutableListAdt<T> {
  private final ListAdtImpl<T> delegate;

  /**
   * Private constructor that creates an immutable list from a ListAdtImpl.
   * This is private to prevent creation of unmodifiable (rather than immutable) lists.
   */
  private ImmutableListAdtImpl() {
    this.delegate = new ListAdtImpl<>();
  }

  /**
   * Private method to add an element to the back of this list.
   * Only accessible to the builder.
   *
   * @param object the object to add
   */
  private void addBack(T object) {
    delegate.addBack(object);
  }

  @Override
  public int getSize() {
    return delegate.getSize();
  }

  @Override
  public T get(int index) throws IllegalArgumentException {
    return delegate.get(index);
  }

  @Override
  public <R> ImmutableListAdt<R> map(Function<T, R> converter) {
    ImmutableListAdtImpl.Builder<R> builder = new ImmutableListAdtImpl.Builder<>();
    for (int i = 0; i < this.getSize(); i++) {
      builder.add(converter.apply(this.get(i)));
    }
    return builder.build();
  }

  @Override
  public MutableListAdt<T> getMutableList() {
    MutableListAdtImpl<T> mutableList = new MutableListAdtImpl<>();
    for (int i = 0; i < this.getSize(); i++) {
      mutableList.addBack(this.get(i));
    }
    return mutableList;
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

  /**
   * Builder class for constructing an ImmutableListADTImpl.
   *
   * @param <T> the type of elements in the list
   */
  public static class Builder<T> {
    private final ImmutableListAdtImpl<T> list;

    /**
     * Creates a new builder for an immutable list.
     */
    public Builder() {
      this.list = new ImmutableListAdtImpl<>();
    }

    /**
     * Add an element to the back of the list being built.
     *
     * @param object the object to add
     * @return this builder for method chaining
     */
    public Builder<T> add(T object) {
      list.addBack(object);
      return this;
    }

    /**
     * Build and return the immutable list.
     *
     * @return the constructed immutable list
     */
    public ImmutableListAdt<T> build() {
      return list;
    }
  }
}

