package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static CustomerService customerService;
    private final Map<String, Customer> customers = new HashMap<>();

    public static CustomerService getInstance() {
        if(customerService == null){
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void addCustomer(String firstName, String lastName, String email){
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail){
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers(){
        return customers.values();
    }

}
