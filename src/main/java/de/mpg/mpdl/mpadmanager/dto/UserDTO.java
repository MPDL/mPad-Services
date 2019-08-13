package de.mpg.mpdl.mpadmanager.dto;

import java.util.List;

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
	
	@NotNull
	private String telephone;
	
	private String department;

	private List<String> coordinateTeams; 
		
	private String zip;
	
	private String country;
	
	private String city;
	
	private String address;

	private boolean mattermost;

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

	public List<String> getCoordinateTeams() {
		return coordinateTeams;
	}

	public void setCoordinateTeams(List<String> coordinateTeams) {
		this.coordinateTeams = coordinateTeams;
	}

	public String getZip() {
			return zip;
	}

	public void setZip(String zip) {
			this.zip = zip;
	}

	public String getAddress() {
			return address;
	}


	public void setAddress(String address) {
			this.address = address;
	}


	public String getTelephone() {
			return telephone;
	}


	public void setTelephone(String telephone) {
			this.telephone = telephone;
	}


	public String getCountry() {
			return country;
	}


	public void setCountry(String country) {
			this.country = country;
	}


	public String getCity() {
			return city;
	}


	public void setCity(String city) {
			this.city = city;
	}

	public boolean getMattermost() {
			return mattermost; 
	}

  public void setMattermost(boolean mattermost) {
			this.mattermost = mattermost;
	} 
}
