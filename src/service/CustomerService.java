package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// only 1 instance for this class is needed (a Singleton class)
public class CustomerService {
    // instance static reference
    private static CustomerService INSTANCE = null;
    private static final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() {
        // it's perfectly fine to have this Singleton constructor empty
        // Private constructor to prevent instantiation outside the class
    }

    public static CustomerService getInstance() {
        if (INSTANCE == null)
            // creates a new instance if there isn't one yet
            INSTANCE = new CustomerService();

        // otherwise, returns the already created one;
        return INSTANCE;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        try {
            Customer newGuest = new Customer(firstName, lastName, email);
            // we'll find customers by their email, which will pull up all their info
            customers.putIfAbsent(email, newGuest);
        } catch (IllegalArgumentException e) {
            System.out.println("Could Not Add Customer.");
        }
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
