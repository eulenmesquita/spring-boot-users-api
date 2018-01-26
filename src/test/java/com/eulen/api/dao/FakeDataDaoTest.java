package com.eulen.api.dao;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.eulen.api.model.User;
import com.eulen.api.model.User.Gender;
import com.eulen.api.resrouce.UserResourceSpringMVC;

public class FakeDataDaoTest {

	private FakeDataDao fakeDataDao;
	
	@Before
	public void setUp() throws Exception {
		fakeDataDao = new FakeDataDao();
	}

	@Test
	public void shouldSelectAllUsers() {
		List<User> users = fakeDataDao.selectAllUsers();
		
		assertThat(users).hasSize(1);
		
		User user = users.get(0);
		
		assertThat(user.getAge()).isEqualTo(22);
		assertThat(user.getFirstName()).isEqualTo("Joe");
		assertThat(user.getLastName()).isEqualTo("Johnes");
		assertThat(user.getGender()).isEqualTo(Gender.MALE);
		assertThat(user.getEmail()).isEqualTo("joe.johnes@gmail.com");
		
	}

	@Test
	public void shouldSelectUserByUserId() {
		UUID annaId = UUID.randomUUID();
		User anna = new User(annaId, "anna", "montana", Gender.FEMALE, 27, "anna.montana@gmail.com");
		
		fakeDataDao.insertUser(annaId, anna);
		
		assertThat(fakeDataDao.selectAllUsers()).hasSize(2);
		
		Optional<User> userOptional = fakeDataDao.selectUserByUserId(annaId);
		
		assertThat(userOptional.isPresent()).isTrue();
		assertThat(userOptional.get()).isEqualToComparingFieldByField(anna);
	}
	
	@Test
	public void shouldNotSelectUserByRandomUserId() {
		
		Optional<User> user = fakeDataDao.selectUserByUserId(UUID.randomUUID());
		assertThat(user.isPresent()).isFalse();
		
	}

	@Test
	public void shouldUpdateUser() {
		UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUId();
		User newJoe = new User(joeUserUid, "anna", "montana", Gender.FEMALE, 27, "anna.montana@gmail.com");
		fakeDataDao.updateUser(newJoe);
		Optional<User> user = fakeDataDao.selectUserByUserId(joeUserUid);
		assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
		assertThat(user.get()).isEqualToComparingFieldByField(newJoe);
	}

	@Test
	public void testDeleteUserByUserId() {
		UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUId();
		fakeDataDao.deleteUserByUserId(joeUserUid);
		assertThat(fakeDataDao.selectAllUsers()).isEmpty();
		assertThat(UserResourceSpringMVC.isPresent(fakeDataDao.selectUserByUserId(joeUserUid))).isFalse();
	}

	@Test
	public void shouldInsertUser() {
		UUID joeUserUid = UUID.randomUUID();
		User newJoe = new User(joeUserUid, "Joseph", "Just", Gender.MALE, 19, "just@gmail.com");
		
		fakeDataDao.insertUser(joeUserUid, newJoe);
		assertThat(fakeDataDao.selectAllUsers()).hasSize(2);
		assertThat(fakeDataDao.selectUserByUserId(joeUserUid).get()).isEqualToComparingFieldByField(newJoe);
	}

}
