package Exceptions;

public class SomethingDoesntExistException extends RuntimeException{

        String val;

        public SomethingDoesntExistException(String val)
        {
            this.val = val;
        }

        @Override
        public String getMessage() {
            return "An " + val + " doesn't exist in the system.";
        }
}
