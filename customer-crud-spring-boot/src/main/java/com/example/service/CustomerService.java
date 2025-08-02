package com.example.service;

import com.example.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public void createCustomer(String firstName, String lastName, String email) {
        Customer customer = new Customer(firstName, lastName, email);
        repository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = repository.findById(id);
        return customer.orElseThrow(() -> new IllegalArgumentException("Customer with ID " + id + " not found"));
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public void updateCustomer(Long id, String firstName, String lastName, String email) {
        Optional<Customer> optionalCustomer = repository.findById(id);
        Customer customer = optionalCustomer.orElseThrow(() -> new IllegalArgumentException("Customer with ID " + id + " not found"));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        repository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }
        repository.deleteById(id);
    }
}