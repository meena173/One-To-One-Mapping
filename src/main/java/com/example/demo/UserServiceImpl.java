package com.example.demo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("unused")
	@Autowired
    private AddressRepository addressRepository;

    
    @Transactional
    public User saveUser(User user) {
        Address address = user.getAddress();
        if (address != null) {
            if (address.getId() != null) {
                address = entityManager.merge(address);
            } else {
                entityManager.persist(address);
            }
            user.setAddress(address);
        }
        return userRepository.save(user);
    }

    
    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Transactional
    public User updateUserById(Long id, User user) {
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            User originalUser = user1.get();
            if (Objects.nonNull(user.getName()) && !"".equalsIgnoreCase(user.getName())) {
                originalUser.setName(user.getName());
            }
            if (Objects.nonNull(user.getAddress())) {
                Address address = user.getAddress();
                if (address.getId() != null) {
                    address = entityManager.merge(address);
                } else {
                    entityManager.persist(address);
                }
                originalUser.setAddress(address);

            }
            return userRepository.save(originalUser);
        }
        return null;
    }

    
    public String deleteUserById(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User Deleted Successfully";
        }
        return "No Such User in Database";
    }
}

