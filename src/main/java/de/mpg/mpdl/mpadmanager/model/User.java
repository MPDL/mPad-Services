package de.mpg.mpdl.mpadmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

import org.jboss.aerogear.security.otp.api.Base32;


@Entity
@Table(name = "user_account")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String email;
    
    @Column(length = 60)
    private String password;
    
    @Column(nullable = false)
    private String organization;
    
    @Column()
    private String department;
    
    @Column(nullable = false)
    private String telephone;
    
    @ManyToMany
    @JoinTable(name = "user_coordinates",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "coordinate_team_id") })
    private List<CoordinateTeam> coordinateTeams = new ArrayList<>();

    @Column()
    private String zip;
    
    @Column()
    private String address;

    @Column()
    private boolean mattermost;
    
    private boolean enabled;
    private String secret;
   

    public User() {
        super();
        this.secret = Base32.random();
        this.enabled = false;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<CoordinateTeam> getCoordinateTeams() {
        return coordinateTeams;
    }

    public void setCoordinateTeams(List<CoordinateTeam> coordinateTeams) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getMattermost() {
        return mattermost;
    }

    public void setMattermost(boolean mattermost) {
        this.mattermost = mattermost;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
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
        if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }
}
        