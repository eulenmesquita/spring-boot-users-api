package com.eulen.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.eulen.api.model.User;
import com.eulen.api.model.User.Gender;

@Repository
public class FakeDataDao implements UserDao{

	private Map<UUID, User> database;
	
	public FakeDataDao() {
		database = new HashMap<>();
		UUID joeUserID = UUID.randomUUID();
		database.put(joeUserID, new User(joeUserID,"Joe","Johnes", Gender.MALE, 22, "joe.johnes@gmail.com"));
	}

	@Override
	public List<User> selectAllUsers() {
		return new ArrayList<>(database.values());
	}

	@Override
	public Optional<User> selectUserByUserId(UUID userUid) {
		return Optional.ofNullable(database.get(userUid));
	}

	@Override
	public int updateUser(User user) {
		database.put(user.getUserUId(), user);
		return 1;
	}

	@Override
	public int deleteUserByUserId(UUID userUid) {
		database.remove(userUid);
		return 1;
	}

	@Override
	public int insertUser(UUID userUuid, User user) {
		database.put(userUuid, user);
		return 1;
	}
}
