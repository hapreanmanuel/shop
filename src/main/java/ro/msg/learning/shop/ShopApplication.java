package ro.msg.learning.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.misc.Address;
import ro.msg.learning.shop.domain.tables.Customer;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.domain.tables.ProductCategory;
import ro.msg.learning.shop.domain.tables.Supplier;
import ro.msg.learning.shop.service.CustomerService;
import ro.msg.learning.shop.service.LocationService;
import ro.msg.learning.shop.service.ProductCategoryService;
import ro.msg.learning.shop.service.SupplierService;


@ComponentScan
@PropertySource(value={"classpath:application.properties"})
@SpringBootApplication
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
	@Autowired
	private CustomerService customerService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private SupplierService supplierService;

	@Override
	public void run(String... strings) {
		//Customers
		Customer c1 = new Customer(); c1.setFirstName("Davidson"); c1.setLastName("Jones"); c1.setUserName("djones223");
		Customer c2 = new Customer(); c2.setFirstName("Electronica"); c2.setLastName("Association"); c2.setUserName("electrassociation");
		Customer c3 = new Customer(); c3.setFirstName("Mock Customer"); c3.setLastName(""); c3.setUserName("shopster3333");

		customerService.addCustomer(c1);customerService.addCustomer(c2);customerService.addCustomer(c3);

		//Locations
		Location l1 = new Location(); l1.setName("Main Location");l1.setAddress(new Address()); l1.getAddress().setFullAddress("Str. Dorobantilor 77"); l1.getAddress().setCity("Cluj-Napoca"); l1.getAddress().setRegion("CJ");l1.getAddress().setCountry("Romania");
		Location l2 = new Location(); l2.setName("Secondary Location");l2.setAddress(new Address()); l2.getAddress().setFullAddress("Str. Traian 23B7"); l2.getAddress().setCity("Bucuresti"); l2.getAddress().setRegion("B");l2.getAddress().setCountry("Romania");

		locationService.addLocation(l1); locationService.addLocation(l2);

		//Product categories
		ProductCategory pc1 = new ProductCategory(); pc1.setName("Accessories");pc1.setDescription("We sell top quality electronic accessories!");
		ProductCategory pc2 = new ProductCategory(); pc2.setName("Desktop Computers"); pc2.setDescription("The best components for desktop computers.");
		ProductCategory pc3 = new ProductCategory(); pc3.setName("Flat Screens"); pc3.setDescription("Full HD flat screens for the best user experience");

		productCategoryService.addProductCategory(pc1);productCategoryService.addProductCategory(pc2);productCategoryService.addProductCategory(pc3);

		//Suppliers
		Supplier s1 = new Supplier(); s1.setName("PC-Garage");
		Supplier s2 = new Supplier(); s2. setName("Altex");
		Supplier s3 = new Supplier(); s3. setName("NVidia");
		Supplier s4 = new Supplier(); s4. setName("Intel");

		supplierService.addSupplier(s1);supplierService.addSupplier(s2);supplierService.addSupplier(s3);supplierService.addSupplier(s4);

		//Products

		//The rest of the entities (stocks, order and orderdetails) are created by specialised services

	}
}