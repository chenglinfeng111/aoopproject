import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * The NumberleModel class represents the model component of the Numberle game.
 * It implements the MyModel interface and extends the Observable class.
 */
public class NumberleModel extends Observable implements MyModel {
    private String targetNumber;
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private boolean gameWon;
    private List<String> equations;
    boolean flag1 = true;
    boolean flag2 = true;
    boolean RandomState = true;
    String defaultNumber = "1+3*2=7";
    int messageType = 0;

    /**
     * Constructs a new NumberleModel object.
     * It initializes the equations list by reading from a file.
     */
    public NumberleModel() {
        equations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\ALIENWARE\\Desktop\\AOOPcode\\202018010329AooP\\aoop\\Numberle\\Numberle\\equations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                equations.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the game by setting up the initial values for the target number, current guess,
     * remaining attempts, and game status. Notifies the observers of the changes.
     */
    @Override
    public void initialize() {
        Random rand = new Random();
        if (RandomState) {
            targetNumber = equations.get(rand.nextInt(equations.size()));
        } else {
            targetNumber = defaultNumber;
        }
        if (flag2) {
            System.out.println("target number: " + targetNumber);
            System.out.println("start");
        }
        currentGuess = new StringBuilder("       ");
        remainingAttempts = MAX_ATTEMPTS;
        gameWon = false;
        setChanged();
        notifyObservers();
    }

    /**
     * Processes the user input by updating the current guess, checking its validity, and updating the game status.
     *
     * @param input the user input to process
     * @return true if the input is valid and the game state is updated, false otherwise
     */
    @Override
    public boolean processInput(String input) {
        currentGuess = new StringBuilder(input);
        try {
            if (input.length() != 7 || !input.matches("[0-9+\\-×÷*/=]+")) {
                if (flag1){
                    //operator error
                    messageType = 1;
                    return false;
                }
            }
            if(!input.contains("="))
            {
                if (flag1){
                    //operator error
                    messageType = 3;
                    return false;
                }

            }
            if (!checkGuessValid() && flag1)
            {
                //left not equal to right
                messageType = 2;
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        remainingAttempts--;
        if (input.equals(targetNumber)) {
            gameWon = true;
        }
        setChanged();
        notifyObservers();
        return true;
    }

    /**
     * Checks if the current guess is valid by ensuring it has a left and right side separated by an equal sign,
     * and that the left side is equal to the right side when evaluated as mathematical expressions.
     *
     * @return true if the guess is valid, false otherwise
     */
    private boolean checkGuessValid() {
        String[] parts = currentGuess.toString().split("=");
        if (parts.length != 2) {
            return false;
        }
        String left = parts[0];
        String right = parts[1];

        try {
            double result = getResult(left);
            return result == getResult(right);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Evaluates the mathematical expression represented by the input and returns the result.
     *
     * @param input the mathematical expression to evaluate
     * @return the result of the evaluation
     */
    private double getResult(String input) {
        String[] parts = input.split("(?=[+\\-×÷*/])|(?<=[+\\-×÷*/])");
        double result = 0;
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            assert part != null && !part.isEmpty();
            if (part.matches("\\d+") || part.equals("+") || part.equals("-")) {
                stack.push(part);
            } else {
                double b = Double.parseDouble(stack.pop());
                assert (parts[i + 1] != null && parts[i + 1].matches("\\d+"));
                double a = Double.parseDouble(parts[i + 1]);
                i++;
                if (part.equals("×") || part.equals("*") || part.equals("x")) {
                    stack.push(String.valueOf(a * b));
                } else if (part.equals("÷") || part.equals("/")) {
                    stack.push(String.valueOf(b / a));
                }
            }
        }

        for (int i = 0; i < stack.size(); i++) {
            String s = stack.get(i);
            if (s.equals("+") || s.equals("-")) {
                assert i > 0 && i < stack.size() - 1;
                double b = Double.parseDouble(stack.get(i + 1));
                if (s.equals("+")) {
                    result = result + b;
                } else {
                    result = result - b;
                }
                i++;
            } else {
                assert s.matches("\\d+");
                result = Double.parseDouble(s);
            }
        }
        return result;
    }

    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    @Override
    public String getTargetNumber() {
        return targetNumber;
    }

    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    @Override
    public void startNewGame() {
        initialize();
    }

    /**
     * Gets the message type.
     *
     * @return the message type
     */
    @Override
    public int getMessageType()
    {
        return this.messageType;

    }

    /**
     * Displays the right numbers and operators in the guess that match the corresponding positions in the target.
     *
     * @param guess  the user's guess
     * @param target the target string to compare against
     */
    public static void displayRightNumbersOperators(String guess, String target) {
        StringBuilder rightNumbers = new StringBuilder();
        StringBuilder rightOperators = new StringBuilder();

        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            if (c == target.charAt(i)) {
                if (Character.isDigit(c)) {
                    rightNumbers.append(c).append(", ");
                } else if (isOperator(c)) {
                    rightOperators.append(c).append(", ");
                }
            }
        }

        if (rightNumbers.length() > 0) {
            System.out.println("Right Numbers: " + rightNumbers.substring(0, rightNumbers.length() - 2));
        }
        if (rightOperators.length() > 0) {
            System.out.println("Right Operators: " + rightOperators.substring(0, rightOperators.length() - 2));
        }
    }

    /**
     * Checks if the given character is an operator.
     *
     * @param c the character to check
     * @return true if the character is an operator, false otherwise
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    public void setRandomState(){
        RandomState = !RandomState;
    }
    public boolean getRandomState(){
        return RandomState;
    }
}

