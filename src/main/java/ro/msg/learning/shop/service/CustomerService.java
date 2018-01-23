package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {this.customerRepository = customerRepository;}

    public Customer getCustomer(int customerId){return customerRepository.findOne(customerId);}

    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }

    public void addCustomer(Customer customer) { customerRepository.save(customer);}

    public void updateCustomer(Customer customer) { customerRepository.save(customer);}

    public void deleteCustomer(Customer customer) { customerRepository.delete(customer);}

}

