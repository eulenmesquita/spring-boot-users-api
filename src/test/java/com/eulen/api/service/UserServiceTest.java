package com.eulen.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eulen.api.dao.FakeDataDao;
import com.eulen.api.model.User;
import com.eulen.api.model.User.Gender;
import com.eulen.api.resrouce.UserResourceSpringMVC;

import jersey.repackaged.com.google.common.collect.ImmutableList;

public class UserServiceTest {

	@Mock
	private FakeDataDao fakeDataDao;
	
	private UserService userService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(fakeDataDao);
	}

	@Test
	public void shouldGetAllUsers() {
		UUID annaId = UUID.randomUUID();
		User anna = new User(annaId, "Anna", "Montana", Gender.FEMALE, 30, "montana@gmail.com");
		
		ImmutableList<User> users = new ImmutableList.Builder<User>().add(anna).build();
		
		given(fakeDataDao.selectAllUsers()).willReturn(users);
		
		List<User> allUsers = userService.getUsers(Optional.empty());
		assertThat(allUsers).hasSize(1);
		
		User user = users.get(0);
		
		assertUserFields(user);
	}

	@Test
	public void shouldGetUser() {
		UUID annaId = UUID.randomUUID();
		User anna = new User(annaId, "Anna", "Montana", Gender.FEMALE, 30, "montana@gmail.com");
		
		given(fakeDataDao.selectUserByUserId(annaId)).willReturn(Optional.of(anna));
		Optional<User> userOptional = userService.getUser(annaId);
		
		assertThat(UserResourceSpringMVC.isPresent(userOptional)).isTrue();
		
		User user = userOptional.get();
		
		assertUserFields(user);
	}

	private void assertUserFields(User user) {
		assertThat(user.getAge()).isEqualTo(30);
		assertThat(user.getFirstName()).isEqualTo("Anna");
		assertThat(user.getLastName()).isEqualTo("Montana");
		assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
		assertThat(user.getEmail()).isEqualTo("montana@gmail.com");
		assertThat(user.getUserUId()).isNotNull();
	}

	@Test
	public void shouldUpdateUser() {
		UUID annaId = UUID.randomUUID();
		User anna = new User(annaId, "Anna", "Montana", Gender.FEMALE, 30, "montana@gmail.com");
		
		given(fakeDataDao.selectUserByUserId(annaId)).willReturn(Optional.of(anna));
		given(fakeDataDao.updateUser(anna)).willReturn(1);
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		
		int updateResult = userService.updateUser(anna);
		
		verify(fakeDataDao).selectUserByUserId(annaId);
		verify(fakeDataDao).updateUser(captor.capture());
		
		User user = captor.getValue();
		assertUserFields(user);
		
		assertThat(updateResult).isEqualTo(1);
	}

	@Test
	public void shouldRemoveUser() {
		UUID annaId = UUID.randomUUID();
		User anna = new User(annaId, "Anna", "Montana", Gender.FEMALE, 30, "montana@gmail.com");
		
		given(fakeDataDao.selectUserByUserId(annaId)).willReturn(Optional.of(anna));
		given(fakeDataDao.deleteUserByUserId(annaId)).willReturn(1);
		
		int deleteResult = userService.removeUser(annaId);
		
		verify(fakeDataDao).selectUserByUserId(annaId);
		verify(fakeDataDao).deleteUserByUserId(annaId);

		assertThat(deleteResult).isEqualTo(1);
	}

	@Test
	public void shouldInsertUser() {
		UUID annaId = UUID.randomUUID();
		User anna = new User(annaId, "Anna", "Montana", Gender.FEMALE, 30, "montana@gmail.com");
		
		given(fakeDataDao.insertUser(any(UUID.class), any(User.class))).willReturn(1);
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		
		int insertResult = userService.insertUser(anna);
		
		verify(fakeDataDao).insertUser(eq(annaId), captor.capture());
		
		User userCaptor = captor.getValue();
		
		assertUserFields(userCaptor);
		
		assertThat(insertResult).isEqualTo(1);
		
	}

}
