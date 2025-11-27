package listadt;

import java.util.function.Function;

/**
 * This is the implementation of a mutable list. It extends ListAdtImpl and implements
 * the MutableListADT interface.
 *
 * @param <T> the type of elements in this list
 */
public class MutableListAdtImpl<T> extends ListAdtImpl<T> implements MutableListAdt<T> {

  /**
   * Creates an empty mutable list.
   */
  public MutableListAdtImpl() {
    super();
  }

  @Override
  public <R> MutableListAdt<R> map(Function<T, R> converter) {
    ListAdt<R> mapped = super.map(converter);
    MutableListAdtImpl<R> result = new MutableListAdtImpl<>();
    for (int i = 0; i < mapped.getSize(); i++) {
      result.addBack(mapped.get(i));
    }
    return result;
  }

  @Override
  public ImmutableListAdt<T> getImmutableList() {
    ImmutableListAdtImpl.Builder<T> builder = new ImmutableListAdtImpl.Builder<>();
    for (int i = 0; i < this.getSize(); i++) {
      builder.add(this.get(i));
    }
    return builder.build();
  }
}

