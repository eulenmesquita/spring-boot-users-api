package com.eulen.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eulen.api.dao.UserDao;
import com.eulen.api.model.User;
import com.eulen.api.model.User.Gender;
import com.eulen.api.resrouce.UserResourceSpringMVC;

@Service
public class UserService {

	private UserDao userDao;

	@Autowired
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public List<User> getUsers(Optional<String> gender) {
		List<User> users = userDao.selectAllUsers();
		if (!gender.isPresent()) {
			return users;
		}
		try {
			Gender theGender = User.Gender.valueOf(gender.get().toUpperCase());
			return users.stream()
					.filter( user -> user.getGender().equals(theGender))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new IllegalStateException("Gender invalid");
		}
	}

	public Optional<User> getUser(UUID userUid) {
		return userDao.selectUserByUserId(userUid);
	}
	
	public int updateUser(User user) {
		Optional<User> optionalUser = getUser(user.getUserUId());
		
		if (UserResourceSpringMVC.isPresent(optionalUser)) {
			return userDao.updateUser(user);
		}
		throw new NotFoundException("user " + user.getUserUId() + "not found");
	}

	public int removeUser(UUID uid) {
		UUID userUId = getUser(uid)
				.map(User::getUserUId)
				.orElseThrow(() -> new NotFoundException("user " + uid + "not found"));
		return userDao.deleteUserByUserId(userUId);
	}
	
	public int insertUser(User user) {
		UUID userId = user.getUserUId() == null ? UUID.randomUUID() : user.getUserUId();
		return  userDao.insertUser(userId, user.newUser(userId, user));
	}
}
