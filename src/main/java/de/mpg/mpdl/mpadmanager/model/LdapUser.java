package de.mpg.mpdl.mpadmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapNameBuilder;

@Entry(objectClasses = {"top", "inetOrgPerson"})
public class LdapUser {
      
    @Id
    private Name dn;
    
    @DnAttribute(value = "uid")
    private String uid;
    
    @Attribute(name="cn")
    private String fullName;
    
    @Attribute(name = "givenName")
    private String givenName;
    
    @Attribute(name = "sn")
    private String sn;
    
    @Attribute(name = "userPassword")
    private String password;
    
    @Attribute(name = "mail")
    private String email;
    
    @Transient
    @DnAttribute(value = "ou")
    private String ou;
    
    @Attribute(name = "telephoneNumber")
    private String telephoneNumber;
    
    @Attribute(name = "description")
    private String description;
    
    @Attribute(name = "departmentNumber")
    private List<String> departmentNumber = new ArrayList<String>(1);
    
    @Attribute(name = "postalCode")
    private List<String> zip = new ArrayList<String>(1);
    
    @Attribute(name = "postalAddress")
    private List<String> address = new ArrayList<String>(1);

    public LdapUser() {
    }

    public LdapUser(String givenName, String sn, String password, String ou, String email,
            String telephoneNumber, String description, String departmentNumberStr, String zipStr, String addressStr) {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "mpg")
                .add("ou", ou)
                .add("uid", email)
                .build();
        this.dn = dn;
        this.fullName = givenName + " " +sn;
        this.givenName = givenName;
        this.sn = sn;
        this.password = password;
        this.email = email;
        this.ou = ou;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
        setDepartmentNumber(departmentNumberStr);
        setZip(zipStr);
        setAddress(addressStr);
    }
    
    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentNumber() {
        if (departmentNumber.size() > 0) {
            return departmentNumber.get(0);
        }
        return null;
    }

    public void setDepartmentNumber(String value) {
        departmentNumber.clear();
        value = ( value == null ) ? "" : value.trim();
        if (!"".equals(value)) {
            departmentNumber.add(value);
        }
    }
    
    
    public String getZip() {
        if (zip.size() > 0) {
            return zip.get(0);
        }
        return null;
    }

    public void setZip(String value) {
        zip.clear();
        value = ( value == null ) ? "" : value.trim();
        if (!"".equals(value)) {
            zip.add(value);
        }
    }

    public String getAddress() {
        if (address.size() > 0) {
            return address.get(0);
        }
        return null;
    }

    public void setAddress(String value) {
        address.clear();
        value = ( value == null ) ? "" : value.trim();
        if (!"".equals(value)) {
            address.add(value);
        }
    }

    @Override
    public String toString() {
        return "LdapUser{" +
                "dn=" + dn +
                ", uid='" + uid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", lastName='" + sn + '\'' +
                ", email='" + email + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", departmentNumber='" + departmentNumber + '\'' +
                ", ou='" + ou + '\'' +
                '}';
    }
}
