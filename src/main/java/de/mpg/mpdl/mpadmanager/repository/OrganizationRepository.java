package de.mpg.mpdl.mpadmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.mpg.mpdl.mpadmanager.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

  Organization findByName(String name);

    @Override
    void delete(Organization organization);

    List<Organization> findAll();
}
