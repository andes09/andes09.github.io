import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


/**
 * A JUnit test class for the Person class.
 */
public class PersonTest {

  private Person john;

  /**
   * Set up.
   */

  @Before
  public void setUp() {

    john = new Person("John", "Doe", 1945);
  }

  @Test
  public void testFirst() {
    assertEquals("John", john.getFirstName());

  }

  @Test
  public void testSecond() {
    assertEquals("Doe", john.getLastName());
  }

  @Test
  public void testYearOfBirth() {
    assertEquals(1945, john.getYearOfBirth()); //should be 1945
  }

}
