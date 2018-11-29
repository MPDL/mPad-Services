package de.mpg.mpdl.mpadmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.mpg.mpdl.mpadmanager.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{

	Privilege findByName(String name);
	
	@Override
	void delete(Privilege privilege);
}
