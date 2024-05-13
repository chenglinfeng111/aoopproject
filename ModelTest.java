import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    NumberleModel instance;

    @BeforeEach
    void setUp() {
        instance = new NumberleModel();
        instance.startNewGame();
    }



    /**
     * Tests the input processing functionality of the NumberleModel.
     *
     * The testInputProcessing method is a unit test designed to verify the input processing behavior of a NumberleModel object.
     * It tests the scenario where the random generation of the target equation is disabled.
     *
     * <hr>
     *
     * <h3>Preconditions:</h3>
     * The NumberleModel instance is created. The target equation is not generated randomly.
     *
     * <h3>Postconditions:</h3>
     * - The remaining attempts are decreased by 1.
     * - The input "#$!12" is processed, and it returns true.
     *
     * <hr>
     *
     * @requires model != null;
     * @requires !model.isRandomGenerationEnabled();
     * @ensures model.getRemainingAttempts() == NumberleModel.MAX_ATTEMPTS - 1;
     * @ensures model.processInput("#$!12") == true;
     */
    @Test
    void InputProcessingTest() {
        instance.flag1 = false;
        assertTrue(instance.processInput("#$!12"));
        assertEquals(instance.getRemainingAttempts(),5);
    }

    /**
     * Tests the game winning scenario in the NumberleModel.
     *
     * The testGameWin method is a unit test designed to verify the game-winning behavior of a NumberleModel object.
     * It tests the scenario where the target number matches the default number.
     *
     * <hr>
     *
     * <h3>Preconditions:</h3>
     * The NumberleModel instance is created. The input processing result for the default number is true.
     *
     * <h3>Postconditions:</h3>
     * - If the target number matches the default number:
     *     - The target number is equal to the default number.
     *     - The game is won.
     * - If the target number does not match the default number:
     *     - The game is not won.
     *
     * <hr>
     *
     * @requires model != null;
     * @requires model.processInput(model.defaultNumber) == true;
     * @ensures if (model.getTargetNumber().equals(model.defaultNumber)) {
     *               model.getTargetNumber() == model.defaultNumber;
     *               model.isGameWon() == true;
     *           } else {
     *               model.isGameWon() == false;
     *           }
     */
    @Test
    void GameWinningTest() {
        assertTrue(instance.processInput(instance.defaultNumber));

        if (instance.getTargetNumber() .equals(instance.defaultNumber)) {
            assertEquals(instance.getTargetNumber(),instance.defaultNumber);
            assertTrue(instance.isGameWon());
        }else {
            assertFalse(instance.isGameWon());
        }

    }

    /**
     * Tests the initialization of the NumberleModel and the random function.
     *
     * The testCheckGuessValid method is a unit test designed to verify the behavior of the checkGuessValid() method in the NumberleModel class.
     * It is divided into two main parts:
     * - Testing a valid guess.
     * - Testing various invalid guesses.
     *
     * <hr>
     *
     * <h3>Preconditions:</h3>
     * The NumberleModel instance is created.
     *
     * <h3>Postconditions:</h3>
     * - The result of a valid guess is true.
     * - The result of invalid guesses is false.
     *
     * <hr>
     *
     * @requires instance != null;
     * @requires instance.getTargetNumber().equals("1+2+3=6");
     * @ensures instance.getRemainingAttempts() == NumberleModel.MAX_ATTEMPTS;
     * @ensures !instance.isGameOver();
     * @ensures !instance.isGameWon();
     * @ensures instance.getUnusedCharacters().size() == 14;
     *
     */
    @Test
    void CheckGuessValidTest() {
        // valid guess
        assertTrue(instance.processInput("2+1=1+2"));

        // InValid guess
        assertFalse(instance.processInput("1+1=2"));
        assertFalse(instance.processInput("1+1=2+3"));
        assertFalse(instance.processInput("1+1+1+1"));
        assertFalse(instance.processInput("1+1+=2"));

    }
}