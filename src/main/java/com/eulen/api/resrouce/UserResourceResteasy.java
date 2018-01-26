package com.eulen.api.resrouce;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.eulen.api.formatter.ErrorMessage;
import com.eulen.api.model.User;
import com.eulen.api.service.UserService;

@Component
@Path("api/v1/users")
public class UserResourceResteasy {

	private UserService userService;

	@Autowired
	public UserResourceResteasy(UserService userService) {
		this.userService = userService;
	}

	@GET
	@Produces(APPLICATION_JSON)
	public List<User> fetchUsers(@QueryParam("gender") String gender) {
		return userService.getUsers(Optional.ofNullable(gender));
	}

	@GET
	@Produces(APPLICATION_JSON)
	@Path("{userId}")
	public User fetchUser(@PathParam("userId") UUID userId) {
		return userService.getUser(userId)
		.orElseThrow(()-> new NotFoundException("user" + userId + "not found"));
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public void insertNewUser(@RequestBody User user) {
		userService.insertUser(user);
	}

	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public void updateUser(@RequestBody User user) {
		userService.updateUser(user);
	}

	@DELETE
	@Path("{userId}")
	@Produces(APPLICATION_JSON)
	public void deleteUser(@PathParam("userId") UUID userId) {
		userService.removeUser(userId);
		
	}

	private Response getIntegerResponseEntity(int result) {
		if (result == 1) {
			return Response.ok().build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}

}
