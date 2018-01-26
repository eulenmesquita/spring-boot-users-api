package com.eulen.api.resrouce;

import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eulen.api.formatter.ErrorMessage;
import com.eulen.api.model.User;
import com.eulen.api.service.UserService;

//@RestController
//@RequestMapping(path = "api/v1/users")
public class UserResourceSpringMVC {

	public static <T> boolean isPresent(Optional<T> optional) {
		return optional.isPresent();
	}

	public UserService userService;
		
	@Autowired
	public UserResourceSpringMVC(UserService userService) {
		super();
		this.userService = userService;
	}

	@RequestMapping(
		method = RequestMethod.GET
	)
	public ResponseEntity<?> fetchUsers(@QueryParam("gender") String gender) {
		try {
			return ResponseEntity.ok(userService.getUsers(Optional.ofNullable(gender)));
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
		}
		
	}
	
	@RequestMapping(
		method = RequestMethod.GET,
		path = "{userId}"
	)
	public ResponseEntity<?> fetchUser(@PathVariable("userId") UUID userId) {
		Optional<User> optionalUser =  userService.getUser(userId);
		
		if (UserResourceSpringMVC.isPresent(optionalUser)) {
			return ResponseEntity.ok(optionalUser.get());
		} 
			
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("user " + userId + " was not found"));
			
	}
	
	@RequestMapping(
		method = RequestMethod.POST,
		consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
		int result = userService.insertUser(user);
		
		return getIntegerResponseEntity(result);
	}
	
	@RequestMapping(
		method = RequestMethod.PATCH,
		consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Integer> updateUser(@RequestBody User user) {
		int result = userService.updateUser(user);
		return getIntegerResponseEntity(result);
	}

	@RequestMapping(
		method = RequestMethod.DELETE,
		path = "{userId}"
	)
	public ResponseEntity<Integer> deleteUser(@PathVariable("userId") UUID userId) {
		int result = userService.removeUser(userId);
		return getIntegerResponseEntity(result);
	}
	
	private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
		if (result == 1) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
