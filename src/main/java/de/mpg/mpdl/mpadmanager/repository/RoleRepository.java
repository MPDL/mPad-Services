package de.mpg.mpdl.mpadmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.mpg.mpdl.mpadmanager.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String name);
	
	@Override
	void delete(Role role);
}
