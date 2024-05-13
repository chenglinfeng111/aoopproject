/**
 * The MyModel interface represents the model component of a game.
 * It defines the methods and constants required for the game logic.
 */
public interface MyModel {

    /**
     * The maximum number of attempts allowed in the game.
     */
    int MAX_ATTEMPTS = 6;

    /**
     * Initializes the game model.
     */
    void initialize();

    /**
     * Processes the user input.
     *
     * @param input the user input to process
     * @return {@code true} if the input is valid and processed successfully, {@code false} otherwise
     */
    boolean processInput(String input);

    /**
     * Checks if the game is over.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    boolean isGameOver();

    /**
     * Checks if the game is won.
     *
     * @return {@code true} if the game is won, {@code false} otherwise
     */
    boolean isGameWon();

    /**
     * Retrieves the target number of the game.
     *
     * @return the target number
     */
    String getTargetNumber();

    /**
     * Retrieves the current guess made by the user.
     *
     * @return the current guess
     */
    StringBuilder getCurrentGuess();

    /**
     * Retrieves the number of remaining attempts in the game.
     *
     * @return the remaining attempts
     */
    int getRemainingAttempts();

    /**
     * Starts a new game.
     */
    void startNewGame();

    /**
     * Retrieves the message type of the game.
     *
     * @return the message type
     */
    int getMessageType();

    boolean getRandomState();

    void setRandomState();
}