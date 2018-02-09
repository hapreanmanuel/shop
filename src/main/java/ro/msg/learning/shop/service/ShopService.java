package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.tables.*;
import ro.msg.learning.shop.repository.*;

import java.util.List;

/*
    This class handles basic repository access for the shop (CRUD operations)
    Entities handled by ShopService:
        - customers
        - locations
        - product categories
        - products
        - suppliers
 */
@Service
public class ShopService {

    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;
    private final ProductCategoryRepository productCategoryRepository ;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ShopService(CustomerRepository customerRepository, LocationRepository locationRepository, ProductCategoryRepository productCategoryRepository, ProductRepository productRepository, SupplierRepository supplierRepository){
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    /*
        Repository access
     */
    //Customers
    public Customer getCustomer(int customerId){return customerRepository.findOne(customerId);}
    public Customer getCustomerByUserame(String customerUsername){return customerRepository.findByUserName(customerUsername); }
    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }
    public void addCustomer(Customer customer) { customerRepository.save(customer);}
    public void deleteCustomer(int customerId) {customerRepository.delete(customerId);}

    //Location
    public Location getLocation(int locationId) {return locationRepository.findOne(locationId);}
    public List<Location> getAllLocations() { return locationRepository.findAll();}
    public void addLocation(Location location) { locationRepository.save(location);}
    public void deleteLocation(Location location) { locationRepository.delete(location);}

    //Product category
    public ProductCategory getProductCategory(int categoryId){return productCategoryRepository.findOne(categoryId);}
    public List<ProductCategory> getAllProductCategories() {return productCategoryRepository.findAll();}
    public void addProductCategory(ProductCategory productCategory){productCategoryRepository.save(productCategory);}
    public void deleteProductCategory(ProductCategory productCategory){productCategoryRepository.delete(productCategory);}

    //Product
    public Product getProduct(int productId){return productRepository.findOne(productId);}
    public List<Product> getAllProducts(){return productRepository.findAll();}
    public void addProduct(Product product){productRepository.save(product);}
    public void deleteProduct(Product product){productRepository.delete(product);}

    //Supplier
    public Supplier getSupplier(int supplierId){return supplierRepository.findOne(supplierId);}
    public List<Supplier> getAllSuppliers() { return supplierRepository.findAll(); }
    public void addSupplier(Supplier supplier) { supplierRepository.save(supplier);}
    public void deleteSupplier(Supplier supplier) { supplierRepository.delete(supplier);}

}
