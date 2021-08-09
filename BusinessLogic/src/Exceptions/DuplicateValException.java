package Exceptions;

public class DuplicateValException extends RuntimeException{
    private String val;
    private Class clazz;

    public DuplicateValException(String val, Class clazz) {
        this.val = val;
        this.clazz = clazz;
    }

    public DuplicateValException(Integer val) {
        this.val = val.toString();
    }

    @Override
    public String getMessage() {
        return "The val:" + this.val + "is already entered as a: " + this.clazz.getSimpleName();
    }
}
