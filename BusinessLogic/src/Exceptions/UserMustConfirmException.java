package Exceptions;

public class UserMustConfirmException extends RuntimeException{

    @Override
    public String getMessage() {
        return "There are previous results in the system. You must confirm the renewal of the results.";
    }
}
