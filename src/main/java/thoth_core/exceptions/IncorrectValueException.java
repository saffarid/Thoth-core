package thoth_core.exceptions;

public class IncorrectValueException extends Exception{

    private static final String MESSAGE_TEMPLATE = "Incorrect value: $1s";

    public IncorrectValueException(String value) {
        super(String.format(MESSAGE_TEMPLATE, value));
    }
}
