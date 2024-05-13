/**
 * The NumberleController class controls the number guessing game.
 */
// NumberleController.java
public class NumberleController {
    private MyModel model;
    private NumberleView view;

    /**
     * Constructs a NumberleController object with the specified model.
     *
     * @param model The model object that handles game logic and data.
     */
    public NumberleController(MyModel model) {
        this.model = model;
    }

    /**
     * Sets the view object for displaying the game interface.
     *
     * @param view The view object to be set.
     */
    public void setView(NumberleView view) {
        this.view = view;
    }

    /**
     * Returns the message type from the model.
     *
     * @return The message type as an integer.
     */
    public int getMessage()
    {
        return model.getMessageType();
    }

    /**
     * Processes the user input using the model.
     *
     * @param input The user input as a string.
     * @return True if the input was successfully processed, false otherwise.
     */
    public boolean processInput(String input) {
        return model.processInput(input);
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return model.isGameOver();
    }

    /**
     * Checks if the game is won.
     *
     * @return True if the game is won, false otherwise.
     */
    public boolean isGameWon() {
        return model.isGameWon();
    }

    /**
     * Returns the target number from the model.
     *
     * @return The target number as a string.
     */
    public String getTargetNumber() {
        return model.getTargetNumber();
    }

    /**
     * Returns the current guess from the model.
     *
     * @return The current guess as a StringBuilder object.
     */
    public StringBuilder getCurrentGuess() {
        return model.getCurrentGuess();
    }

    /**
     * Returns the remaining attempts from the model.
     *
     * @return The remaining attempts as an integer.
     */
    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }

    /**
     * Starts a new game by calling the startNewGame method of the model.
     */
    public void startNewGame() {
        model.startNewGame();
    }

    public  boolean getRandomState(){
        return model.getRandomState();
    }

    public void setRandomState(){
        model.setRandomState();
    }
}


