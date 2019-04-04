package de.mpg.mpdl.mpadmanager.model;

import org.springframework.ldap.odm.annotations.*;

import javax.naming.Name;

@Entry(base = "ou=mpaduser,dc=mpadmanager,dc=de", objectClasses = "MpadUser")
public final class User {

    @Id
    private Name id;
    @Attribute(name = "firstName")
	private String firstName;
    
    @Attribute(name = "lastName")
	private String lastName;
        
    @DnAttribute(value = "cn")
	private String cn;
    
    @Attribute(name = "password")
    private String password;
    
    @Attribute(name = "organization")
	private String organization;
    
    @Attribute(name = "department")
	private String department;
    
    @Attribute(name = "enabled")
    private String enabled;
    
    @Attribute(name = "secret")
    private String secret;
    
	public Name getId() {
		return id;
	}
	public void setId(Name id) {
		this.id = id;
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
		return cn;
	}
	public void setEmail(String email) {
		this.cn = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
    public String getEnabled() {
        return enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((cn == null) ? 0 : cn.hashCode());
        return result;
    }
	
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        if (!cn.equals(user.cn)) {
            return false;
        }
        return true;
    }
	
}
