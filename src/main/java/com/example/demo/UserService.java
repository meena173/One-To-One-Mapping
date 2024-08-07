package com.example.demo;

import java.util.List;
public interface UserService 
{
	User saveUser(User user);
	List<User> fetchAllUsers();
	User getUserById(Long id);
	User updateUserById(Long id, User user);
	String deleteUserById(long id);
}
