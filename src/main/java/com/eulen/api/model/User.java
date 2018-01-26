package com.eulen.api.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private final UUID userUId;
	private final String firstName;
	private final String lastName;
	private final Gender gender;
	private final Integer age;
	private final String email;
	
	public enum Gender {
		MALE,
		FEMALE,
	}

	public User(
		@JsonProperty("userUId") UUID userUId, 
		@JsonProperty("firstName") String firstName, 
		@JsonProperty("lastName") String lastName, 
		@JsonProperty("gender") Gender gender, 
		@JsonProperty("age") Integer age, 
		@JsonProperty("email") String email) {

		this.userUId = userUId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.email = email;
	}

	//@JsonProperty("id")
	public UUID getUserUId() {
		return userUId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public Integer getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public Integer getDateOfBirth() {
		return LocalDate.now().minusYears(age).getYear();
	}
	
	public static User newUser(UUID userUid, User user) {
		return new User(userUid, user.getFirstName(), user.getLastName(), user.getGender(), user.getAge(), user.getEmail());
	}

	@Override
	public String toString() {
		return "User {userUId=" + userUId + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", age=" + age + ", email=" + email + "}";
	}

	
	
}
