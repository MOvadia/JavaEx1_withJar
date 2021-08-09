package Exceptions;

public class WrongReferenceException extends RuntimeException{
    private String referencedTo;
    private String referencedBy;

    public WrongReferenceException(String referencedTo, String referencedBy) {
        this.referencedTo = referencedTo;
        this.referencedBy = referencedBy;
    }

    @Override
    public String getMessage() {
        return this.referencedBy + " cannot reference to:" + this.referencedTo;
    }
}
