package ro.msg.learning.shop.exception;

public class EmptyShoppingCartException extends RuntimeException{

    private static final String MESSAGE = "Shopping cart can not be empty";

    private static final long serialVersionUID = 1L;

    public EmptyShoppingCartException(){
        super();
    }

    public EmptyShoppingCartException(String message){
        super(message);
    }

    public EmptyShoppingCartException(String message, Throwable cause){
        super(message,cause);
    }

    @Override
    public String getMessage(){
        return MESSAGE;
    }

}
