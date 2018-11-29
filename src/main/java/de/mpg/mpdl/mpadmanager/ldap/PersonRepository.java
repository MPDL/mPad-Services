package de.mpg.mpdl.mpadmanager.ldap;

import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

public interface PersonRepository extends CrudRepository<Person, Name> {


}