package com.company.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class FormatterUtil {
    public static void setIntegerFormatter(TextField textField) {
        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                });
        textField.setTextFormatter(formatter);
    }

    public static void setDoubleFormatter(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        });
        textField.setTextFormatter(formatter);
    }

    public static void setPhoneNumberFormatter(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d{0,11}")) {
                        return change;
                    }
                    return null;
                });
        textField.setTextFormatter(formatter);
    }
}
