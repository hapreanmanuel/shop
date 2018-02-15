package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.domain.OrderDetail;
import ro.msg.learning.shop.exception.EmptyShoppingCartException;
import ro.msg.learning.shop.exception.InvalidLocationException;
import ro.msg.learning.shop.exception.NoSuitableStrategyException;
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
        Customer dummy = shopService.getCustomerByUserame("dummy");
        assertThat(dummy).isInstanceOf(Customer.class);
        System.out.println(dummy.getUserName());
    }

    @Test
    public void newOrderCreationTest(){

        //Get dummy customer    (entry created by component 'AddMockDataToDatabase')
        Customer dummyCustomer = shopService.getCustomerByUserame("dummy");

        /*
            Create an OrderSpecifications object (single input for order creation)
         */
        OrderSpecifications orderSpecifications = shopService.createBasicOrderSpecificationsForCustomer(dummyCustomer.getCustomerId());

        //Delivery address
        Address dummyDevileryAddress = new Address();
        dummyDevileryAddress.setCity("Cluj-Napoca");
        dummyDevileryAddress.setCountry("Romania");
        dummyDevileryAddress.setRegion("CJ");
        dummyDevileryAddress.setFullAddress("Dummy home St LakePark 22321B");

        orderSpecifications.setAddress(dummyDevileryAddress);

        ShoppingCartEntry sh1 = new ShoppingCartEntry(1, 10);
        ShoppingCartEntry sh2 = new ShoppingCartEntry(2,20);

        orderSpecifications.getShoppingCart().add(sh1);
        orderSpecifications.getShoppingCart().add(sh2);

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
        /*
        Currently a NullPointerException is thrown if not all fields of an address have been assigned
         */

        //Shipment address
        Address address = new Address();
        address.setCity("Cluj-Napoca");
        address.setCountry("Romania");
        address.setRegion("CJ");
        address.setFullAddress("");

        OrderSpecifications orderSpecifications = shopService.createBasicOrderSpecificationsForCustomer(1);
        orderSpecifications.setAddress(address);

        //Should throw an 'invalid shippment location' exception
        try{
            shopService.createNewOrder(orderSpecifications);
        }catch(Exception e) {
            assertThat(e.getMessage()).isEqualTo(new InvalidLocationException().getMessage());
        }

        address.setCity("Cluj-Napoca");
        address.setCountry("Romania");
        address.setRegion("CJ");
        address.setFullAddress("Dorobantilor 112B AP6");
        orderSpecifications.setAddress(address);

        //Should throw an 'empty shopping cart' exception
        try{
            shopService.createNewOrder(orderSpecifications);
        }catch(Exception e) {
            assertThat(e.getMessage()).isEqualTo(new EmptyShoppingCartException().getMessage());
        }

        orderSpecifications.getShoppingCart().add(new ShoppingCartEntry(1,301));

        //Should throw an 'no suitable strategy' exception
        try{
            shopService.createNewOrder(orderSpecifications);
        }catch(Exception e) {
            assertThat(e.getMessage()).isEqualTo(new NoSuitableStrategyException().getMessage());
        }
    }

}
