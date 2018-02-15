package ro.msg.learning.shop.exception;

public class EmptyShoppingCartException extends RuntimeException{

    public EmptyShoppingCartException(){
        super("Shopping cart can not be empty");
    }
}
