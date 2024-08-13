package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController 
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    
    @PostMapping
    public User saveUser(@RequestBody User user) 
    {
        Address address = user.getAddress();
        if (address != null)
        {
            if (address.getId() != null)
            {
                address = addressRepository.findById(address.getId()).orElse(null);
            } 
            else
            {
                address = addressRepository.save(address);
            }
            user.setAddress(address);
        }
        return userRepository.save(user);
    }

    
    @GetMapping
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

   
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) 
    {
        return userRepository.findById(id).orElse(null);
    }

   
    @PutMapping("/{id}")
    public User updateUserById(@PathVariable("id") Long id, @RequestBody User user) 
    {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) 
        {
            User originalUser = userOptional.get();
            if (Objects.nonNull(user.getName()) && !"".equalsIgnoreCase(user.getName()))
            {
                originalUser.setName(user.getName());
            }
            if (Objects.nonNull(user.getAddress())) 
            {
                Address address = user.getAddress();
                if (address.getId() != null) 
                {
                    address = addressRepository.findById(address.getId()).orElse(null);
                } 
                else 
                {
                    address = addressRepository.save(address);
                }
                originalUser.setAddress(address);
            }
            return userRepository.save(originalUser);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") Long id) 
    {
        if (userRepository.existsById(id)) 
        {
            userRepository.deleteById(id);
            return "User Deleted Successfully";
        }
        return "No Such User in Database";
    }

    @GetMapping("/page/{pageNo}/{pageSize}")
    public List<User> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize) 
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> pagedResult = userRepository.findAll(pageable);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @GetMapping("/sort")
    public List<User> getAllSorted(@RequestParam String field, @RequestParam String direction) 
    {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return userRepository.findAll(Sort.by(sortDirection, field));
    }

    @GetMapping("/page/{pageNo}/{pageSize}/sort")
    public List<User> getPaginatedAndSorted(@PathVariable int pageNo, @PathVariable int pageSize,
                                            @RequestParam String sortField, @RequestParam String sortDir) 
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> pagedResult = userRepository.findAll(pageable);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }
}
