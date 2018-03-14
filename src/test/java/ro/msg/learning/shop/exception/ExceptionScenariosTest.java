package ro.msg.learning.shop.exception;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.service.ShopService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExceptionScenariosTest {

    @Autowired
    private ShopService shopService;

    private final String username = "test";

    private final List<ShoppingCartEntry> invalidList = new ArrayList<>();
    private final Address invalidAddress = Address.builder().build();

    private final List<ShoppingCartEntry> validList = Arrays.asList(
            new ShoppingCartEntry(1,1),
            new ShoppingCartEntry(2,300)
    );
    private final Address validAddress = Address.builder()
            .city("Cluj-Napoca").country("Romania").region("CJ").fullAddress("Str. Dave Mira 111").build();

    @Test
    public void exceptionsTest(){
        OrderCreationDto request = new OrderCreationDto();

        request.setShoppingCart(invalidList);
        request.setAddress(invalidAddress);

        try{
            log.info("Trying to create OrderSpecifications with invalid address. ");
            shopService.createOrderSpecifications(request, username);
        }catch (Exception e){
            log.info("Intercepted exception message: {}", e.getMessage());
            assertThat(e).isInstanceOf(InvalidShippmentAddressException.class);
        }

        //Set valid address
        request.setAddress(validAddress);

        try{
            log.info("Trying to create OrderSpecifications with empty shopping cart. ");
            shopService.createOrderSpecifications(request, username);
        }catch (Exception e){
            log.info("Intercepted exception message: {}", e.getMessage());
            assertThat(e).isInstanceOf(EmptyShoppingCartException.class);
        }

        request.setShoppingCart(validList);

        log.info("Trying to create OrderSpecifications with valid input. ");
        //Should return a valid response now
        OrderSpecifications os = shopService.createOrderSpecifications(request, username);

        log.info("Success");

        assertThat(os.getCustomer().getFirstName()).isEqualTo(username);
    }
}
