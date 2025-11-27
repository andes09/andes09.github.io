import java.util.ArrayList;
import java.util.List;

/**
 * Monitor Class.
 */
public class MoniotringPillCounterDecorator extends PillCounterDecorator {
  private List<Integer> addCounts;
  private int currentAddCount;

  /**
   * sets up class.
   *
   * @param delegate object
   */
  public MoniotringPillCounterDecorator(PillCounter delegate) {
    super(delegate);
    this.addCounts = new ArrayList<>();
    this.currentAddCount = 0;
  }

  @Override
  public void addPill(int count) {
    currentAddCount++;
    delegate.addPill(count);
  }

  @Override
  public void reset() {
    addCounts.add(currentAddCount);
    currentAddCount = 0;
    delegate.reset();
  }

  public List<Integer> getAddCounts() {
    return new ArrayList<>(addCounts);
  }
}
