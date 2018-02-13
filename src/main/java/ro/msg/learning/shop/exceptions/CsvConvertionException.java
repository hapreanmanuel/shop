package ro.msg.learning.shop.exceptions;

public class CsvConvertionException extends RuntimeException {

    private static final String message = "Unable to convert to CSV";

    private static final long serialVersionUID = 1L;

    public CsvConvertionException(){
        super();
    }

    public CsvConvertionException(String message){
        super(message);
    }

    public CsvConvertionException(String message, Throwable cause){
        super(message,cause);
    }

    @Override
    public String getMessage(){
        return message;
    }

}
