package Exceptions;

public class NotExistValException extends RuntimeException{
    private String val;
    private Class classType;

    public NotExistValException(String val, Class classType) {
        this.val = val;
        this.classType = classType;
    }

    public NotExistValException(Integer val, Class classType) {
        this.val = val.toString();
        this.classType = classType;
    }

    @Override
    public String getMessage() {
        return "The value: " +  this.val + " is not exist in " + this.classType.getSimpleName();
    }
}
