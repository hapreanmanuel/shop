package ro.msg.learning.shop.exception;

public class StockForLocationExistsException extends RuntimeException {

    private static final String MESSAGE = "Invalid shipment location";

    @Override
    public String getMessage(){
        return MESSAGE;
    }
}
