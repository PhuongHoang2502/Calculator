package bk.bosch.calculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CalculatorViewModel extends ViewModel {

//    modified:   app/build.gradle
//    modified:   app/src/main/java/bk/bosch/calculator/CalculatorFragment.java
//    modified:   app/src/main/java/bk/bosch/calculator/CalculatorViewModel.java
//    modified:   app/src/main/res/layout/fragment_calculator.xml
    private MutableLiveData<String> _solutionText = new MutableLiveData<>("");
    private MutableLiveData<String> _resultText = new MutableLiveData<>("0");

    public LiveData<String> solutionText = _solutionText;
    public LiveData<String> resultText = _resultText;

    private List<MyButton> buttons;
    public CalculatorViewModel() {
        createButtons();
    }

    public List<MyButton> getButtons() {
        if (buttons == null) {
            createButtons();
        }
        return buttons;
    }
    private void createButtons(){
        buttons = new ArrayList<>();

        //Colors
        int orange = android.R.color.holo_orange_dark;
        int gray = android.R.color.darker_gray;
        int red = android.R.color.holo_red_dark;
        int white = android.R.color.white;

        // Row 1: C, (, ), /
        buttons.add(new MyButton("C", red, white, MyButton.ButtonType.FUNCTION));
        buttons.add(new MyButton("(", orange, white, MyButton.ButtonType.OPERATOR));
        buttons.add(new MyButton(")", orange, white, MyButton.ButtonType.OPERATOR));
        buttons.add(new MyButton("÷", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 2: 7, 8, 9, *
        buttons.add(new MyButton("7", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("8", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("9", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("×", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 3: 4, 5, 6, -
        buttons.add(new MyButton("4", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("5", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("6", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("-", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 4: 1, 2, 3, +
        buttons.add(new MyButton("1", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("2", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("3", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("+", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 5: AC, 0, ., =
        buttons.add(new MyButton("AC", red, white, MyButton.ButtonType.FUNCTION));
        buttons.add(new MyButton("0", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton(".", gray, white, MyButton.ButtonType.NUMBER));
        buttons.add(new MyButton("=", orange, white, MyButton.ButtonType.EQUAL));

    }

    public void onButtonClick(String buttonText, MyButton.ButtonType buttonType) {
        String currentSolution = _solutionText.getValue();
        if (currentSolution == null) {
            currentSolution = "";
        }

        if(buttonText.equals("AC")){
            _solutionText.setValue("");
            _resultText.setValue("0");
            return;
        }

        if(buttonText.equals("=")){
            String result = getResult(currentSolution);
            if (!result.equals("Error")) {
                _resultText.setValue(result);
                _solutionText.setValue(currentSolution);
            } else {
                _resultText.setValue("Error");
            }
            return;
        }

        if(buttonText.equals("C")){
            if(currentSolution.length() > 0) {
                currentSolution = currentSolution.substring(0, currentSolution.length() - 1);
                _solutionText.setValue(currentSolution);
            }
        } else {
            // Add the button text to the calculation string
            currentSolution += buttonText;
            _solutionText.setValue(currentSolution);
        }
    }
    String getResult(String data){
        try {
            // Replace display symbols with calculation symbols
            data = data.replaceAll("×", "*");
            data = data.replaceAll("÷", "/");

            // Simple expression evaluator
            return String.valueOf(evaluateExpression(data));
        } catch (Exception e) {
            return "Error";
        }
    }

    // Simple expression evaluator that handles basic arithmetic with parentheses
    private double evaluateExpression(String expression) throws Exception {
        // Remove spaces
        expression = expression.replaceAll("\\s+", "");

        if (expression.isEmpty()) {
            return 0;
        }

        return evaluate(expression);
    }

    private double evaluate(String expression) throws Exception {
        // Handle parentheses first
        while (expression.contains("(")) {
            int start = -1;
            for (int i = 0; i < expression.length(); i++) {
                if (expression.charAt(i) == '(') {
                    start = i;
                } else if (expression.charAt(i) == ')') {
                    if (start == -1) {
                        throw new Exception("Mismatched parentheses");
                    }
                    String subExpr = expression.substring(start + 1, i);
                    double result = evaluate(subExpr);
                    expression = expression.substring(0, start) + result + expression.substring(i + 1);
                    break;
                }
            }
        }

        return evaluateSimple(expression);
    }

    private double evaluateSimple(String expression) throws Exception {
        // Handle multiplication and division first
        String[] parts = expression.split("(?=[+\\-])|(?<=[+\\-])");
        double result = 0;
        String operator = "+";

        for (String part : parts) {
            if (part.equals("+") || part.equals("-")) {
                operator = part;
            } else if (!part.isEmpty()) {
                double value = evaluateMultDiv(part);
                if (operator.equals("+")) {
                    result += value;
                } else {
                    result -= value;
                }
            }
        }

        return result;
    }

    private double evaluateMultDiv(String expression) throws Exception {
        String[] parts = expression.split("(?=[*/])|(?<=[*/])");
        double result = 1;
        String operator = "*";
        boolean firstNumber = true;

        for (String part : parts) {
            if (part.equals("*") || part.equals("/")) {
                operator = part;
            } else if (!part.isEmpty()) {
                double value = Double.parseDouble(part);
                if (firstNumber) {
                    result = value;
                    firstNumber = false;
                } else {
                    if (operator.equals("*")) {
                        result *= value;
                    } else {
                        if (value == 0) {
                            throw new Exception("Division by zero");
                        }
                        result /= value;
                    }
                }
            }
        }

        return result;
    }
}
