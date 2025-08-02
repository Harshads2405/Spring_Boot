package com.example.service;

import com.example.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public void createCustomer(String firstName, String lastName, String email) {
        Customer customer = new Customer(firstName, lastName, email);
        repository.create(customer);
    }

    public Customer getCustomerById(Long id) {
        Customer customer = repository.findById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public void updateCustomer(Long id, String firstName, String lastName, String email) {
        Customer customer = repository.findById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        repository.update(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = repository.findById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }
        repository.delete(id);
    }
}