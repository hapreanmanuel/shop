package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.domain.OrderDetail;
import ro.msg.learning.shop.exception.EmptyShoppingCartException;
import ro.msg.learning.shop.exception.InvalidShippmentAddressException;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderOperatinonsTests {

    @Autowired
    private ShopService shopService;
    @Autowired
    private StockService stockService;

    @Test
    public void getUserByUserName(){
//        Customer dummy = shopService.getCustomerByUserame("dummy");
//        assertThat(dummy).isInstanceOf(Customer.class);
//        System.out.println(dummy.getUserName());
    }

    @Test
    public void newOrderCreationTest(){

        //Get dummy customer    (entry created by component 'AddMockDataToDatabase')
        Customer dummyCustomer = shopService.getCustomer(1);

        OrderCreationDto o = new OrderCreationDto();

        Address adr = Address.builder()
                .region("CJ")
                .country("Romania")
                .city("Cluj-Napoca")
                .fullAddress("Home street avenue 111").build();
        o.setAddress(adr);

        ShoppingCartEntry sh1 = new ShoppingCartEntry(1, 10);
        ShoppingCartEntry sh2 = new ShoppingCartEntry(2,20);

        o.getShoppingCart().add(sh1);
        o.getShoppingCart().add(sh2);


        /*
            Create an OrderSpecifications object (single input for order creation)
         */
        OrderSpecifications orderSpecifications = shopService.createOrderSpecifications(o, dummyCustomer.getUser().getUsername());



        //Submit the order by calling the order creation method from orderService
        Order dummyOrder = shopService.createNewOrder(orderSpecifications);

        /*
            Checkpoints
         */
        assertThat(shopService.getAllOrders()).isNotEmpty();   //The order should be findable by the service
        //Check if the stock has been updated for the first product
        assertThat(stockService.findStock(dummyOrder.getLocation().getLocationId(), 1).getQuantity()).isEqualTo(90);
        //Check if the stock has been updated for the second product
        assertThat(stockService.findStock(dummyOrder.getLocation().getLocationId(), 2).getQuantity()).isEqualTo(80);

        //Print order to system log
        System.out.println(dummyOrder.toString());


        //Check if orderDetails are persisted as well
        List<OrderDetail> dummyOrderDetails = shopService.getAllDetailsForOrder(dummyOrder.getOrderId());

        assertThat(dummyOrderDetails).size().isEqualTo(2);  // we added two product requests

        //Check if orderDetails are also accesible through the 'Order' object refence
        List<OrderDetail> dummyOrderDetailsFromOrder = dummyOrder.getOrderDetails();

        assertThat(dummyOrderDetailsFromOrder).size().isEqualTo(2);

    }
    @Test
    public void orderCreationExceptionHandlingTest(){

        Customer dummyCustomer = shopService.getCustomer(1);


        OrderCreationDto o = new OrderCreationDto();

        Address adr = Address.builder()
                .region("CJ")
                .country("Romania")
                .city("Cluj-Napoca")
                .fullAddress("Home street avenue 111").build();

        ShoppingCartEntry sh1 = new ShoppingCartEntry(1, 10);

        ShoppingCartEntry sh2 = new ShoppingCartEntry(2,20);

        //Invalid address exception
        try{
            shopService.createOrderSpecifications(o, dummyCustomer.getUser().getUsername());
        } catch (Exception e){
            assertThat(e).isInstanceOf(InvalidShippmentAddressException.class);
        }

        o.setAddress(adr);

        //Empty shopping cart exception
        try{
            shopService.createOrderSpecifications(o,dummyCustomer.getUser().getUsername());
        } catch (Exception e){
            assertThat(e).isInstanceOf(EmptyShoppingCartException.class);
        }

        o.getShoppingCart().add(sh1);
        o.getShoppingCart().add(sh2);

        try{
            shopService.createOrderSpecifications(o,dummyCustomer.getUser().getUsername());
        } catch(Exception e){
            assertThat(false).isEqualTo(true);  //No exception should be thrown here
        }

    }

}
