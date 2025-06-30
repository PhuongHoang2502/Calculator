package bk.bosch.calculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment implements MyAdapter.OnButtonClickListener{
    private TextView resultTv, solutionTv;
    private RecyclerView buttonsRecyclerView;
    private MyAdapter adapter;
    private List<MyButton> buttons;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }
    private void initViews(View view){
        resultTv = view.findViewById(R.id.result_tv);
        solutionTv = view.findViewById(R.id.solution_tv);
        buttonsRecyclerView = view.findViewById(R.id.buttons_recyclerview);
    }
    private void setupRecyclerView(){
        buttons = createButtons();
        adapter = new MyAdapter(buttons, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        buttonsRecyclerView.setLayoutManager(gridLayoutManager);
        buttonsRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();

    }
    private List<MyButton> createButtons(){
        List<MyButton> buttonList = new ArrayList<>();

        //Colors
        int orange = android.R.color.holo_orange_dark;
        int gray = android.R.color.darker_gray;
        int red = android.R.color.holo_red_dark;
        int white = android.R.color.white;

        // Row 1: C, (, ), /
        buttonList.add(new MyButton("C", red, white, MyButton.ButtonType.FUNCTION));
        buttonList.add(new MyButton("(", orange, white, MyButton.ButtonType.OPERATOR));
        buttonList.add(new MyButton(")", orange, white, MyButton.ButtonType.OPERATOR));
        buttonList.add(new MyButton("÷", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 2: 7, 8, 9, *
        buttonList.add(new MyButton("7", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("8", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("9", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("×", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 3: 4, 5, 6, -
        buttonList.add(new MyButton("4", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("5", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("6", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("-", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 4: 1, 2, 3, +
        buttonList.add(new MyButton("1", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("2", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("3", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("+", orange, white, MyButton.ButtonType.OPERATOR));

        // Row 5: AC, 0, ., =
        buttonList.add(new MyButton("AC", red, white, MyButton.ButtonType.FUNCTION));
        buttonList.add(new MyButton("0", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton(".", gray, white, MyButton.ButtonType.NUMBER));
        buttonList.add(new MyButton("=", orange, white, MyButton.ButtonType.EQUAL));

        return buttonList;
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
    @Override
    public void onButtonClick(String buttonText, MyButton.ButtonType buttonType) {
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
            } else {
                resultTv.setText("Error");
            }
            return;
        }

        if(buttonText.equals("C")){
            if(dataToCalculate.length() > 0) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
        } else {
            // Add the button text to the calculation string
            dataToCalculate += buttonText;
        }

        solutionTv.setText(dataToCalculate);
    }
}