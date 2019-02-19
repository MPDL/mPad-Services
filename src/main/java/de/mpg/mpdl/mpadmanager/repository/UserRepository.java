package de.mpg.mpdl.mpadmanager.repository;

import java.util.List;

import javax.naming.Name;

import org.springframework.data.repository.CrudRepository;

import de.mpg.mpdl.mpadmanager.model.User;

public interface UserRepository extends CrudRepository<User, Name> {

    User findByEmail(String email);

    @Override
    void delete(User user);

    List<User> findAll();
}
