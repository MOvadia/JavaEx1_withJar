package Exceptions;

public class UserMustConfirmException extends RuntimeException{

    boolean previousResults;
    boolean algorithmInProcess;

    public UserMustConfirmException(boolean previousResults, boolean algorithmInProcess) {
        this.previousResults = previousResults;
        this.algorithmInProcess = algorithmInProcess;
    }

    @Override
    public String getMessage() {
        if(previousResults)
        {
            return "There are previous results in the system. You must confirm the renewal of the results.";
        }
        else
        {

            return "The algorithm is still running in the background. You must confirm the stop of the run.";
        }
    }
}
