import java.util.Scanner;

/**
 * The NumberleCLI class is a command-line interface for the Numberle game.
 * It allows users to interact with the NumberleModel and play the game by guessing numbers.
 */
public class NumberleCLI {
    public static void main(String[] args) {
        //use the Model logic
        NumberleModel model = new NumberleModel();

        model.initialize();

        Scanner scanner = new Scanner(System.in);

        while (!model.isGameOver()) {
            System.out.println("Enter your guess:");
            String input = scanner.nextLine();

            if (!model.processInput(input)) {
                System.out.println("Invalid input! Please enter a valid equation.");
            } else {

                // Display the right numbers and operators
                NumberleModel.displayRightNumbersOperators(input, model.getTargetNumber());
            }

            if (model.isGameWon()) {
                System.out.println("Congratulations! You guessed the number correctly: " + model.getTargetNumber());
            } else {
                System.out.println("Remaining attempts: " + model.getRemainingAttempts());
            }
        }

        scanner.close();
    }

}
