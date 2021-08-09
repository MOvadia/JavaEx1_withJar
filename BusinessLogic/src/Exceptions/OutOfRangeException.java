package Exceptions;

public class OutOfRangeException extends RuntimeException{
    private String message;

    public OutOfRangeException(String string) {
        this.message = string;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
