package za.ac.cput.guiltfreecookie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.guiltfreecookie.domain.Customer;
import za.ac.cput.guiltfreecookie.domain.Name;
import za.ac.cput.guiltfreecookie.service.CustomerService;
import za.ac.cput.guiltfreecookie.util.Helper;

import java.util.List;

// Admin-only (see SecurityConfig): customer records are personal information.
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    static final int MAX_NAME_LENGTH = 100;

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer cleaned = clean(customer);
        if (!isValid(cleaned)) {
            return ResponseEntity.badRequest().build();
        }
        if (customerService.read(cleaned.getCustomerEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Customer createdCustomer = customerService.create(cleaned);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Customer> read(@PathVariable String email){
        Customer customer = customerService.read(Helper.cleanEmail(email));
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll(){
        List<Customer> customers = customerService.getAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer){
        Customer cleaned = clean(customer);
        if (!isValid(cleaned)) {
            return ResponseEntity.badRequest().build();
        }
        Customer updatedCustomer = customerService.update(cleaned);
        if (updatedCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String email){
        customerService.delete(Helper.cleanEmail(email));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static Customer clean(Customer customer) {
        Name name = customer.getName();
        Name cleanedName = name == null ? null : new Name.Builder()
                .setFirstName(Helper.clean(name.getFirstName()))
                .setMiddleName(Helper.clean(name.getMiddleName()))
                .setLastName(Helper.clean(name.getLastName()))
                .build();
        return new Customer.Builder()
                .setCustomerEmail(Helper.cleanEmail(customer.getCustomerEmail()))
                .setName(cleanedName)
                .setMobileNumber(Helper.clean(customer.getMobileNumber()))
                .build();
    }

    private static boolean isValid(Customer customer) {
        Name name = customer.getName();
        return Helper.isValidEmail(customer.getCustomerEmail())
                && name != null
                && Helper.isPresent(name.getFirstName(), MAX_NAME_LENGTH)
                && Helper.isPresent(name.getLastName(), MAX_NAME_LENGTH)
                && Helper.isWithin(name.getMiddleName(), MAX_NAME_LENGTH)
                && (Helper.isNullOrEmpty(customer.getMobileNumber())
                        || Helper.isValidMobileNumber(customer.getMobileNumber()));
    }
}
