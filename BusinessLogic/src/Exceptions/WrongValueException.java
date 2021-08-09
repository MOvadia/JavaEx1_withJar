package Exceptions;

public class WrongValueException extends RuntimeException{
    private String insertedVal;

    public WrongValueException(Object insertedVal) {
        this.insertedVal = insertedVal.toString();
    }

    @Override
    public String getMessage() {
        return "The value: " + this.insertedVal + " is not answer the demands.";
    }
}
