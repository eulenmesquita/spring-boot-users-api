package com.eulen.api.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.eulen.api.model.User;

public interface UserDao {

	List<User> selectAllUsers();
	Optional<User> selectUserByUserId(UUID userUid);
	int updateUser(User user);
	int deleteUserByUserId(UUID userUid);
	int insertUser(UUID userUid, User user);
	
}
