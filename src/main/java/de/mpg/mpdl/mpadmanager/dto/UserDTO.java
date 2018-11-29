package de.mpg.mpdl.mpadmanager.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mpg.mpdl.mpadmanager.validation.ValidEmail;


public class UserDTO {

	@NotNull
	@Size(min = 1, message = "{Size.userDTO.firstName}")
	private String firstName;
	
	@NotNull
	@Size(min = 1, message = "{Size.userDTO.lastName}")
	private String lastName;
	
//    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

	@ValidEmail
	@NotNull
	@Size(min = 1, message = "{Size.userDTO.email}")
	private String email;
	
	@NotNull
	@Size(min = 1, message = "{Size.userDTO.organization}")
	private String organization;
	
	private String department;
	    
	public UserDTO( ) {
	}
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	
	
}
