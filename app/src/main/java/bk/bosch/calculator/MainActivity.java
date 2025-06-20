package bk.bosch.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equal);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);

    }

    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if(buttonText.equals("AC")){
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }
        if(buttonText.equals("=")){
            String result = getResult(dataToCalculate);
            if(!result.equals("Error")){
                resultTv.setText(result);
                solutionTv.setText(dataToCalculate);
            }
            else {
                resultTv.setText("Error");
            }
            return;
        }
        if(buttonText.equals("C")){
            if(dataToCalculate.length() > 0){
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
        }
        else {
            dataToCalculate += buttonText;
        }

        solutionTv.setText(dataToCalculate);
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