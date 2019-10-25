package de.mpg.mpdl.mpadmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.mpg.mpdl.mpadmanager.model.ResearchField;


public interface ResearchFieldRepository extends JpaRepository<ResearchField, Long> {
  ResearchField findByName(String name);
}