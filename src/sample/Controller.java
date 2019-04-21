package sample;

// nie działa 5*= = = = =
// nie dziala klawiatura
// nie przedzialek w =

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;


public class Controller {
    private double memory = 0;
    private String operator = "";
    private boolean begin = true;

    CalculateInterface add = (double x, double y) -> x + y;
    CalculateInterface subtract = (double x, double y) -> x - y;
    CalculateInterface divide = (double x, double y) -> x / y;
    CalculateInterface multi = (double x, double y) -> x * y;
    CalculateInterface squareRoot = (double x, double y) -> sqrt(x);

    private double Calc(double x, double y, String z) throws ArithmeticException {
        double value;
        double scale = 10e5;                                // skala zaokraglenia, w celu unikniecia np (√5)^2 != 5
        switch (z) {
            case "+":
                value = add.Calculate(x, y);
                break;
            case "-":
                value = subtract.Calculate(x, y);
                break;
            case "x":
                value = multi.Calculate(x, y);
                break;
            case "÷":
                if (y == 0) throw new ArithmeticException();
                value = divide.Calculate(x, y);
                break;
            case "√":
                value = squareRoot.Calculate(x, 0);
                result.setText(Double.toString(value));
                operator = "";
                memory = 0;
                begin = true;
                break;
            default:
                value = 0;
        }
        value = round(value * scale) / scale;
        int remainder = (int) value;
        if (value - remainder < 5 / scale) return remainder;
        return value;
    }

    @FXML
    public Label result;

    @FXML
    public void processNum(ActionEvent event) {
        if (begin)
            if (!((Button) event.getSource()).getText().equals("0")) {
                result.setText("");
                begin = false;
            }

        if (!begin) {
            String value = "";
            if (((Button) event.getSource()).getText().equals(",")) {
                if (!result.getText().contains("."))
                    if (!result.getText().isEmpty()) value = ".";
                    else value = "0.";
            } else value = ((Button) event.getSource()).getText();
            result.setText(result.getText() + value);
        }
    }

    @FXML
    public void processOperator(ActionEvent event) {
        if (!((Button) event.getSource()).getText().equals("=")) {
            if (!operator.isEmpty()) {
                if (!begin) {
                    try {
                        memory = Calc(memory, Double.parseDouble(result.getText()), operator);
                        result.setText(Double.toString(memory));
                    } catch (ArithmeticException a) {
                        operator = "";
                        memory = 0;
                        result.setText("You can't divide by zero!");
                    }
                }
            } else memory = Double.parseDouble(result.getText());
            operator = ((Button) event.getSource()).getText();
        } else {
            try {
                result.setText(Double.toString(Calc(memory, Double.parseDouble(result.getText()), operator)));
            } catch (ArithmeticException a) {
                result.setText("You can't divide by zero!");
            } finally {
                operator = "";
                memory = 0;
            }
        }
        begin = true;
    }

    @FXML
    public void reset() {
        operator = "";
        memory = 0;
        result.setText("0");
        begin = true;
    }

    @FXML
    public void delete() {
        if (!result.getText().equals("0")) {
            if (result.getText().length() > 1) {
                result.setText(result.getText().substring(0, result.getText().length() - 1));
                if (result.getText().contains(".")) {
                    while ((result.getText().charAt(result.getText().length() - 1) == '0'))
                        result.setText(result.getText().substring(0, result.getText().length() - 1));
                    if ((result.getText().charAt(result.getText().length() - 1) == '.'))
                        result.setText(result.getText().substring(0, result.getText().length() - 1));
                }
            } else {
                begin = true;
                result.setText("0");
            }
        } else begin = true;
    }
}
