package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController 
{

    @Autowired
    private AddressRepository addressRepository;

    
    @PostMapping
    public Address createAddress(@RequestBody Address address) 
    {
        return addressRepository.save(address);
    }

    // Get all addresses
    @GetMapping
    public List<Address> getAllAddresses() 
    {
        return addressRepository.findAll();
    }

    // Get address by ID
    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id)
    {
        return addressRepository.findById(id).orElse(null);
    }

  
    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody Address addressDetails)
    {
        Address address = addressRepository.findById(id).orElseThrow();

        if (addressDetails.getStreet() != null) 
        {
            address.setStreet(addressDetails.getStreet());
        }
        if (addressDetails.getCity() != null) 
        {
            address.setCity(addressDetails.getCity());
        }

        return addressRepository.save(address);
    }

 
    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable Long id)
    {
        if (addressRepository.existsById(id)) 
        {
            addressRepository.deleteById(id);
            return "Address deleted successfully";
        } 
        else 
        {
            return "Address not found";
        }
    }



    @GetMapping("/page/{pageNo}/{pageSize}")
    public List<Address> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize) 
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Address> pagedResult = addressRepository.findAll(pageable);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }



    @GetMapping("/sort")
    public List<Address> getAllSorted(@RequestParam String field, @RequestParam String direction)
    {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return addressRepository.findAll(Sort.by(sortDirection, field));
    }


    @GetMapping("/page/{pageNo}/{pageSize}/sort")
    public List<Address> getPaginatedAndSorted(@PathVariable int pageNo, @PathVariable int pageSize,
                                               @RequestParam String sortField, @RequestParam String sortDir) 
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Address> pagedResult = addressRepository.findAll(pageable);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }
}
