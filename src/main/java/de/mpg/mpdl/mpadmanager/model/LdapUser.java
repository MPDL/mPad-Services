package de.mpg.mpdl.mpadmanager.model;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(base = "ou=mpaduser,dc=mpadmanager,dc=de", objectClasses = "MpadUser")
public class LdapUser {
  
    @Id
    private Name id;
    @DnAttribute(value = "muid", index = 3 )
    private String muid;
    
    @Attribute(name = "firstName")
    private String firstName;
    
    @Attribute(name = "lastName")
    private String lastName;
    
    @Attribute(name = "password")
    private String password;
    
    @Attribute(name = "organization")
    private String organization;
    
    @Attribute(name = "department")
    private String department;
    
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

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
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
 
    
}
