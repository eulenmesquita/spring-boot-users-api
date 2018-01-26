package com.eulen.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.eulen.api.clientproxy.UserResourceV1;
import com.eulen.api.model.User;
import com.eulen.api.model.User.Gender;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ApiApplicationTests {

	@Autowired
	private UserResourceV1 userResourceV1;
	
	@Test
	public void itShouldFetchAllUsers() {
		List<User> users = userResourceV1.fetchUsers(null);
		assertThat(users.size()).isGreaterThan(0);
		
		User joe =  new User(null,"Joe","Johnes", Gender.MALE, 22, "joe.johnes@gmail.com");
		
		assertThat(users.get(0)).isEqualToIgnoringGivenFields(joe, "userUId");
		assertThat(users.get(0).getUserUId()).isInstanceOf(UUID.class);
		assertThat(users.get(0).getUserUId()).isNotNull();
	}
	
	@Test
	public void shouldInsertUser() {
		UUID userUId = UUID.randomUUID();
		User joe = new User(userUId,"Joe","Johnes", Gender.MALE, 22, "joe.johnes@gmail.com");
		 
		userResourceV1.insertNewUser(joe);
		
		User user = userResourceV1.fetchUser(userUId);
		assertThat(user.getUserUId()).isInstanceOf(UUID.class);
		assertThat(user.getUserUId()).isEqualTo(userUId);
		assertThat(user).isEqualToComparingFieldByField(joe);
	}
	
	@Test
	public void shouldDeleteUser() {
		UUID userUId = UUID.randomUUID();
		User joe = new User(userUId,"Joe","Johnes", Gender.MALE, 22, "joe.johnes@gmail.com");
		
		userResourceV1.insertNewUser(joe);
		
		User user = userResourceV1.fetchUser(userUId);
		assertThat(user).isEqualToComparingFieldByField(joe);

		userResourceV1.deleteUser(userUId);
		
		assertThatThrownBy(() -> userResourceV1.fetchUser(userUId)).isInstanceOf(NotFoundException.class);
	}
	
	@Test
	public void shouldUpdateUser() {
		UUID userUId = UUID.randomUUID();
		User joe = new User(userUId,"Joe","Johnes", Gender.MALE, 22, "joe.johnes@gmail.com");
		
		userResourceV1.insertNewUser(joe);
		User alex = new User(userUId,"Alex","Johnes", Gender.MALE, 22, "alex.johnes@gmail.com");
		
		userResourceV1.updateUser(alex);
		
		User user = userResourceV1.fetchUser(userUId);
		assertThat(user).isEqualToComparingFieldByField(alex);
	}
	

}
