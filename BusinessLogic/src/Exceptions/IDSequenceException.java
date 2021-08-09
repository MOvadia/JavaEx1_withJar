package Exceptions;

public class IDSequenceException extends RuntimeException{
    private int missingNum;
    private Class classType;

    public IDSequenceException(int missingNum, Class classType) {
        this.missingNum = missingNum;
        this.classType = classType;
    }

    @Override
    public String getMessage() {
        return "The id: " + this.missingNum + " is missing from count running in class: " + this.classType.getSimpleName();
    }
}
