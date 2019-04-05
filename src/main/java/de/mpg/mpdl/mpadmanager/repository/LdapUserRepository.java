package de.mpg.mpdl.mpadmanager.repository;

import java.util.List;

import javax.naming.Name;

import org.springframework.data.repository.CrudRepository;

import de.mpg.mpdl.mpadmanager.model.LdapUser;

public interface LdapUserRepository extends CrudRepository<LdapUser, Name> {

    LdapUser findByMuid(String muid);

    @Override
    void delete(LdapUser ldapUser);

    List<LdapUser> findAll();
}

