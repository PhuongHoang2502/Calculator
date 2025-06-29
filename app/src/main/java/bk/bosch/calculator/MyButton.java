package bk.bosch.calculator;

public class MyButton {
    private String text;
    private int backgroundColor;
    private int textColor;
    private ButtonType type;

    public enum ButtonType{
        NUMBER,
        OPERATOR,
        FUNCTION,
        EQUAL
    }

    public MyButton(String text, int backgroundColor, int textColor, ButtonType type) {
        this.text = text;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public ButtonType getType() {
        return type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setType(ButtonType type) {
        this.type = type;
    }
}
