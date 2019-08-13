package de.mpg.mpdl.mpadmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "coordinate_teams")
public class CoordinateTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @NotNull
    @Column
    private String name;

    @ManyToMany(mappedBy = "coordinateTeams")
    private List<User> users = new ArrayList<>();
    
    public CoordinateTeam() {}

    public CoordinateTeam(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<User> getUsers() {
      return users;
    }

    public void setUsers(List<User> users) {
      this.users = users;
    }

}