package ro.msg.learning.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.service.*;
import java.math.BigDecimal;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class ShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
}





/*
	Add some entries to the database
 */
@Component
class AddMockDataToDatabase implements CommandLineRunner {

	//Service classes ref
	private final ShopService shopService;

	private final StockService stockService;

	@Autowired
	public AddMockDataToDatabase(ShopService shopService, StockService stockService) {
		this.shopService = shopService;
		this.stockService = stockService;
	}

	@Override
	public void run(String... strings) {
		//Customers
		Customer c1 = new Customer(); c1.setFirstName("Davidson"); c1.setLastName("Jones"); c1.setUserName("djones223");
		Customer c2 = new Customer(); c2.setFirstName("Electronica"); c2.setLastName("Association"); c2.setUserName("electrassociation");
		Customer c3 = new Customer(); c3.setFirstName("Mock Customer"); c3.setLastName(""); c3.setUserName("shopster3333");
		Customer c4 = new Customer(); c4.setFirstName("dummyFirstName"); c4.setLastName("dummyLastName"); c4.setUserName("dummy");

		shopService.addCustomer(c1);shopService.addCustomer(c2);shopService.addCustomer(c3);shopService.addCustomer(c4);

		//Locations
		Location l1 = new Location(); l1.setName("Main Location");l1.setAddress(new Address()); l1.getAddress().setFullAddress("Str. Dorobantilor 77"); l1.getAddress().setCity("Cluj-Napoca"); l1.getAddress().setRegion("CJ");l1.getAddress().setCountry("Romania");
		Location l2 = new Location(); l2.setName("Secondary Location");l2.setAddress(new Address()); l2.getAddress().setFullAddress("Str. Traian 23B7"); l2.getAddress().setCity("Bucuresti"); l2.getAddress().setRegion("B");l2.getAddress().setCountry("Romania");

		stockService.addLocation(l1); stockService.addLocation(l2);

		//Product categories
		ProductCategory pc1 = new ProductCategory(); pc1.setName("Gadgets");pc1.setDescription("We sell top quality electronic accessories!");
		ProductCategory pc2 = new ProductCategory(); pc2.setName("Desktop Computers"); pc2.setDescription("The best components for desktop computers.");
		ProductCategory pc3 = new ProductCategory(); pc3.setName("Flat Screens"); pc3.setDescription("Full HD flat screens for the best user experience");

		shopService.addProductCategory(pc1);shopService.addProductCategory(pc2);shopService.addProductCategory(pc3);

		//Suppliers
		Supplier s1 = new Supplier(); s1.setName("PC-Garage");
		Supplier s2 = new Supplier(); s2. setName("Altex");
		Supplier s3 = new Supplier(); s3. setName("NVidia");
		Supplier s4 = new Supplier(); s4. setName("Intel");
		Supplier s5 = new Supplier(); s5.setName("Jay Electronics");

		stockService.addSupplier(s1);stockService.addSupplier(s2);stockService.addSupplier(s3);stockService.addSupplier(s4);stockService.addSupplier(s5);

		//Products
		Product p1 = new Product(); p1.setCategory(pc1);p1.setName("FLR214-222");p1.setDescription("Ultra professional flashlight");p1.setSupplier(s5);p1.setPrice(BigDecimal.valueOf(10));p1.setWeight(0.3);
		Product p2 = new Product();p2.setCategory(pc1);p2.setName("FLR217-280");p2.setDescription("Ultra professional flashlight with extended battery capacity");p2.setSupplier(s5);p2.setPrice(BigDecimal.valueOf(20));p2.setWeight(0.4);
		Product p3 = new Product();p3.setCategory(pc2);p3.setName("AMD I5");p3.setDescription("Accessible desktop computer");p3.setSupplier(s1);p3.setPrice(BigDecimal.valueOf(569));p3.setWeight(13.2);
		Product p4 = new Product();p4.setCategory(pc2);p4.setName("GTX-1060");p4.setDescription("Latest generation GPU");p4.setSupplier(s3);p4.setPrice(BigDecimal.valueOf(134));p4.setWeight(2.3);
		Product p5 = new Product();p5.setCategory(pc3);p5.setName("LG-1231");p5.setDescription("Ultra wide smart screen");p5.setSupplier(s2);p5.setPrice(BigDecimal.valueOf(344));p5.setWeight(7.8);

		shopService.addProduct(p1);shopService.addProduct(p2);shopService.addProduct(p3);shopService.addProduct(p4);shopService.addProduct(p5);

		//Stocks
		int l1Quantity = 100;
		int l2Quantity = 200;

		stockService.createStocksForLocation(l1, l1Quantity );
		stockService.createStocksForLocation(l2,l2Quantity );

		//Create an order for customer c1
		OrderSpecifications orderSpecifications = shopService.createBasicOrderSpecificationsForCustomer(c1.getCustomerId());

		//Delivery address
		Address address = new Address();
		address.setCity("Cluj-Napoca");
		address.setCountry("Romania");
		address.setRegion("CJ");
		address.setFullAddress("Dorobantilor 112B AP6");

		orderSpecifications.setAddress(address);

		//Add some products to shopping cart
		orderSpecifications.getShoppingCart().add(new ShoppingCartEntry(3, 10));
		orderSpecifications.getShoppingCart().add(new ShoppingCartEntry(4,20));

		//Submit the order by calling the order creation method from orderService
		shopService.createNewOrder(orderSpecifications);

	}
}