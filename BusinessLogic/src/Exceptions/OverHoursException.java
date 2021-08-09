package Exceptions;

public class OverHoursException extends RuntimeException{
    private int hours;
    private int days;

    public OverHoursException(int hours, int days) {
        this.hours = hours;
        this.days = days;
    }

    @Override
    public String getMessage() {
        int totalTime = this.days * this.hours;
        return "H*D = " + totalTime + "is more than the max hours allow";
    }
}
