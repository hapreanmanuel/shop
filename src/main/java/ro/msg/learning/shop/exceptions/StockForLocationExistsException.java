package ro.msg.learning.shop.exceptions;

public class StockForLocationExistsException extends RuntimeException {

    private static final String message = "Invalid shipment location";

    @Override
    public String getMessage(){
        return message;
    }
}
