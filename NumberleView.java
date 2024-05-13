import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Observer;

/**
 * The NumberleView class is responsible for displaying the Numberle game UI and handling user interactions.
 * It implements the Observer interface to receive updates from the model.
 */
public class NumberleView implements Observer {
    private final MyModel model;
    private final NumberleController controller;
    private final JFrame frame = new JFrame("Numberle");
    private JTextField[][] guessTexts;
    private JButton[] numberButtons;
    private JButton[] operatorButtons;
    private JLabel attemptsLabel = new JLabel();

    /**
     * Constructs a NumberleView object with the specified model and controller.
     * Initializes the frame and sets up the UI components.
     * @param model The model object representing the game state.
     * @param controller The controller object responsible for game logic.
     */
    public NumberleView(MyModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        this.controller.startNewGame();
        ((NumberleModel)this.model).addObserver(this);
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel)this.model, null);
    }

    /**
     * This method initializes the main frame of the game application. It sets up the user interface components
     * such as buttons, panels, menus, and event listeners for the game.
     */
    public void initializeFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\ALIENWARE\\Desktop\\AOOPcode\\202018010329AooP\\aoop\\Numberle\\Numberle\\src\\log.jpg");
        Image logoImage = logoIcon.getImage();
        frame.setIconImage(logoImage);

        JPanel panel1 = new JPanel();
        JPanel showInputPanel = new JPanel();
        showInputPanel.setLayout(new GridLayout(6,7, 10, 10));
        showInputPanel.setPreferredSize(new Dimension(420, 360));
        guessTexts = new JTextField[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                JTextField textField = new JTextField();
                textField.setFont(new Font("Dialog", Font.PLAIN, 30));
                textField.setBorder(createCustomBorder());
                textField.setEditable(false);
                textField.setBackground(Color.WHITE);
                textField.setHorizontalAlignment(JTextField.CENTER);

                guessTexts[i][j] = textField;
                showInputPanel.add(textField);
            }
        }

        //add number unit into the main paine
        panel1.add(showInputPanel);
        frame.add(panel1, BorderLayout.NORTH);

        JPanel panel2 = new JPanel();
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridLayout(3, 1));
        keyboardPanel.setPreferredSize(new Dimension(580, 180));
        JPanel attemptsPanel = new JPanel();
        attemptsPanel.add(attemptsLabel);
        JButton restartButton = new JButton("Restart");
        restartButton.setBackground(new Color(220, 225, 237));
        restartButton.setEnabled(false);
        restartButton.addActionListener(e -> {
            if (model.getRemainingAttempts() <= 5) {
                controller.startNewGame();
                restartButton.setEnabled(false);
            }
        });
        JButton answerButton = new JButton("Answer");
        answerButton.setBackground(new Color(220, 225, 237));
        answerButton.addActionListener(e -> {
            if (controller.isGameOver()) {
                JOptionPane.showMessageDialog(frame, "Game over! The target number was " + controller.getTargetNumber());
            } else {
                JOptionPane.showMessageDialog(frame, "The target number is " + controller.getTargetNumber());
            }
        });
        JButton randomButton = new JButton("Random");
        JButton errorButton = new JButton("Error");
        randomButton.addActionListener(e -> {
            if (controller.getRandomState()){
                controller.setRandomState();
                JOptionPane.showMessageDialog(frame, "Random model off " );
            }
            else {
                controller.setRandomState();
                JOptionPane.showMessageDialog(frame, "Random model on " );
            }
        });
        restartButton.setBackground(new Color(220, 225, 237));
        restartButton.setEnabled(false);

        attemptsPanel.add(restartButton);
        attemptsPanel.add(answerButton);
        attemptsPanel.add(randomButton);
        attemptsPanel.add(errorButton);

        keyboardPanel.add(attemptsPanel);

        //set numbers button
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(1, 10, 10, 10));
        Font numberFont = new Font("Calibri", Font.BOLD, 20);
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString((i+1)%10));
            button.setBorder(createCustomBorder());
            button.addActionListener(e -> {
                if (controller.isGameOver()){
                    return;
                }
                if (getCurrentColumn() == 6) {
                    return;
                }
                guessTexts[getCurrentRow()][getCurrentColumn()+1].setText(button.getText());
            });
            button.setPreferredSize(new Dimension(44, 44));
            button.setBackground(new Color(220, 225, 237));
            button.setFont(numberFont);
            numberButtons[i] = button;
            JPanel p = new JPanel();
            p.add(button);
            numberPanel.add(p);
        }
        keyboardPanel.add(numberPanel);

        //set delete button
        JPanel operatorPanel = new JPanel();
        JPanel deletePanel = new JPanel();
        JButton deleteButton = new JButton();
        deleteButton.setBorder(createCustomBorder());
        deleteButton.setPreferredSize(new Dimension(137, 50));
        ImageIcon icon = new ImageIcon("C:\\Users\\ALIENWARE\\Desktop\\AOOPcode\\202018010329AooP\\aoop\\Numberle\\Numberle\\src\\delButton.png");
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(45, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        deleteButton.setIcon(icon);
        deleteButton.addActionListener(e -> {
            if (controller.isGameOver()){
                return;
            }
            if (getCurrentColumn() == -1) {
                return;
            }
            guessTexts[getCurrentRow()][getCurrentColumn()].setText("");
        });
        deleteButton.setBackground(new Color(220, 225, 237));
        deletePanel.add(deleteButton);
        operatorPanel.add(deletePanel);

        //set the operator button
        operatorButtons = new JButton[5];
        String[] operators = {"+", "-", "*", "/", "="};
        Font operatorFont = new Font("Calibri", Font.BOLD, 20);
        for (int i = 0; i < 5; i++) {
            JButton button = new JButton(operators[i]);
            button.setBorder(createCustomBorder());
            button.addActionListener(e -> {
                if (controller.isGameOver()){
                    return;
                }
                guessTexts[getCurrentRow()][getCurrentColumn()+1].setText(button.getText());
            });
            button.setPreferredSize(new Dimension(50, 50));
            button.setBackground(new Color(220, 225, 237));
            button.setFont(operatorFont);
            operatorButtons[i] = button;
            operatorPanel.add(button);
        }

        UIManager.put("OptionPane.okButtonText", "confirm");

        //set the submit button
        JPanel submitPanel = new JPanel();
        JButton submitButton = new JButton("Enter");
        submitButton.setBorder(createCustomBorder());
        submitButton.setPreferredSize(new Dimension(137, 50));
        submitButton.addActionListener(e -> {
            if (controller.isGameOver()){
                JOptionPane.showMessageDialog(frame, "Game over! The target number was " + controller.getTargetNumber());
                return;
            }
            StringBuilder guess = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                guess.append(guessTexts[getCurrentRow()][i].getText());
            }
            //justify the equation, return the exception message
            if (guess.length() != 7) {
                JOptionPane.showMessageDialog(frame, "Too short!");
                return;
            }
            if (!controller.processInput(guess.toString())) {
                if (controller.getMessage() == 1)
                {
                    JOptionPane.showMessageDialog(frame, "Operator not valid", "message", 1);
                }
                if(controller.getMessage() == 2)
                {
                    JOptionPane.showMessageDialog(frame, "left side not equal to right side", "message", 1);
                }
                if(controller.getMessage() == 3)
                {
                    JOptionPane.showMessageDialog(frame, "must contain '='", "message", 1);
                }
            }

            restartButton.setEnabled(true);

        });
        submitButton.setBackground(new Color(220, 225, 237));
        submitPanel.add(submitButton);
        operatorPanel.add(submitPanel);

        keyboardPanel.add(operatorPanel);

        panel2.add(keyboardPanel);
        frame.add(panel2, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Updates the view based on changes in the model.
     * If the game is over, displays a message indicating whether the player won or lost.
     * Updates the attempts label with the remaining attempts.
     * Updates the UI components based on the current guess and target number.
     * @param o The Observable object (the model).
     * @param arg An optional argument passed from the model (not used in this method).
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        if (model.isGameOver()) {
            if (model.isGameWon()) {
                JOptionPane.showMessageDialog(frame, "Congratulations! You won!");
            } else {
                JOptionPane.showMessageDialog(frame, "Game over! The target number was " + controller.getTargetNumber());
            }
        }
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        String currentGuess = controller.getCurrentGuess().toString();
        String targetNumber = controller.getTargetNumber();
        if (getCurrentRow() <= 0){
            if (currentGuess.equals("       ")) {
                initButtons();
            }
            return;
        }
        for (int i = 0; i < currentGuess.toCharArray().length; i++) {
            char c = currentGuess.charAt(i);
            JButton button = getButton(String.valueOf(c));
            assert button != null;
            if (c == targetNumber.charAt(i)) {
                guessTexts[getCurrentRow() -1][i].setBackground(new Color(47, 193, 165));
                button.setBackground(new Color(47, 193, 165));
            } else if (targetNumber.contains(String.valueOf(c))) {
                guessTexts[getCurrentRow()-1][i].setBackground(new Color(247, 154, 111));
                button.setBackground(new Color(247, 154, 111));
            } else {
                guessTexts[getCurrentRow()-1][i].setBackground(new Color(164, 174, 196));
                button.setBackground(new Color(164, 174, 196));
            }
        }
    }

    /**
     * Creates a custom border with rounded corners and a gray color.
     * @return The custom border object.
     */
    private Border createCustomBorder() {
        return new LineBorder(Color.GRAY) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape borderShape = new RoundRectangle2D.Double(x, y, width - 1, height - 1, 20, 20);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(getLineColor());
                g2d.draw(borderShape);
                g2d.dispose();
            }
        };
    }

    /**
     * Initializes the buttons and text fields for a new game.
     * Sets the background color of number buttons and operator buttons to a light gray.
     * Clears the text and sets the background color of guess text fields to white.
     */
    private void initButtons() {
        for (JButton button : numberButtons) {
            button.setBackground(new Color(220, 225, 237));
        }
        for (JButton button : operatorButtons) {
            button.setBackground(new Color(220, 225, 237));
        }
        for (JTextField[] guessText : guessTexts) {
            for (JTextField jTextField : guessText) {
                jTextField.setText("");
                jTextField.setBackground(Color.WHITE);
            }
        }
    }

    /**
     * Retrieves a button with the specified text from the number buttons or operator buttons.
     * @param text The text of the button to retrieve.
     * @return The button with the specified text, or null if not found.
     */
    private JButton getButton(String text) {
        for (JButton button : numberButtons) {
            if (button.getText().equals(text)) {
                return button;
            }
        }
        for (JButton button : operatorButtons) {
            if (button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

    /**
     * Retrieves the current row in the game board.
     * The current row is calculated based on the maximum attempts and the remaining attempts in the model.
     * @return The current row in the game board.
     */
    private int getCurrentRow() {
        return MyModel.MAX_ATTEMPTS - model.getRemainingAttempts();
    }

    /**
     * Retrieves the current column in the game board.
     * The current column is determined by finding the first empty text field in the current row of the guessTexts array.
     * @return The current column in the game board.
     */
    private int getCurrentColumn() {
        for (int i = 0; i < 7; i++) {
            if (guessTexts[getCurrentRow()][i].getText().equals("")) {
                return i-1;
            }
        }
        return 6;
    }
}



