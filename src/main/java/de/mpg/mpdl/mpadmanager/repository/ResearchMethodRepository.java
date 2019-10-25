package de.mpg.mpdl.mpadmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.mpg.mpdl.mpadmanager.model.ResearchMethod;


public interface ResearchMethodRepository extends JpaRepository<ResearchMethod, Long> {
  ResearchMethod findByName(String name);
}