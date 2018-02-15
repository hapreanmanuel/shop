package ro.msg.learning.shop.exception;

public class CsvConvertionException extends RuntimeException {

    public CsvConvertionException(){
        super("Unable to convert to CSV");
    }
}
