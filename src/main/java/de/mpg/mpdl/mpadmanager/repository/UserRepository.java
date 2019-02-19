package de.mpg.mpdl.mpadmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import de.mpg.mpdl.mpadmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Override
    void delete(User user);

    List<User> findAll();
}
