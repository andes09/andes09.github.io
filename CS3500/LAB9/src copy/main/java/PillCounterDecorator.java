/**
 * decerator class.
 */
public class PillCounterDecorator implements PillCounter {

  protected PillCounter delegate;

  /**
   * Sets up class.
   *
   * @param delegate object
   */
  public PillCounterDecorator(PillCounter delegate) {
    this.delegate = delegate;
  }

  @Override
  public void addPill(int count) {
    delegate.addPill(count);
  }


  @Override
  public void removePill() {
    delegate.removePill();
  }

  @Override
  public void reset() {
    delegate.reset();
  }

  @Override
  public int getPillCount() {
    return delegate.getPillCount();
  }
}
