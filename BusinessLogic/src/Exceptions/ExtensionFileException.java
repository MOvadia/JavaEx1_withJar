package Exceptions;

public class ExtensionFileException extends RuntimeException{
    private final String fileExtension;

    public ExtensionFileException(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public String getMessage() {
        return "Wrong file extension, the file extension should be: " + this.fileExtension;
    }
}
