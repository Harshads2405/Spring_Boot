package com.example.controller;

import com.example.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class CustomerController {
    private final CustomerService service;
    private final Scanner scanner;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nCustomer Management System");
            System.out.println("1. Create Customer");
            System.out.println("2. View Customer by ID");
            System.out.println("3. View All Customers");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> createCustomer();
                    case 2 -> viewCustomer();
                    case 3 -> viewAllCustomers();
                    case 4 -> updateCustomer();
                    case 5 -> deleteCustomer();
                    case 6 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createCustomer() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        service.createCustomer(firstName, lastName, email);
        System.out.println("Customer created successfully!");
    }

    private void viewCustomer() {
        System.out.print("Enter customer ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        Customer customer = service.getCustomerById(id);
        System.out.println(customer);
    }

    private void viewAllCustomers() {
        List<Customer> customers = service.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private void updateCustomer() {
        System.out.print("Enter customer ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter new last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        service.updateCustomer(id, firstName, lastName, email);
        System.out.println("Customer updated successfully!");
    }

    private void deleteCustomer() {
        System.out.print("Enter customer ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        service.deleteCustomer(id);
        System.out.println("Customer deleted successfully!");
    }
}