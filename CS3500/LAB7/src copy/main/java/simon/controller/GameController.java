package simon.controller;

/**
 * A controller interface for games. This interface
 * does not assume whether the controller is asynchronous
 * or synchronous.
 *
 * <p>Methods to make this controller asynchronous (e.g. adding features callbacks)
 * should be in a different interface, not this one.
 */
public interface GameController {

  /**
   * Starts the entire game. Note that for
   * asynchronous controllers, the only way to regain
   * control is by having the view call another object
   * via a callback.
   */
  void runGame();
}
