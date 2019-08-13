package de.mpg.mpdl.mpadmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.mpg.mpdl.mpadmanager.model.CoordinateTeam;

public interface CoordinateTeamRepository extends JpaRepository<CoordinateTeam, Long> {
  CoordinateTeam findByName(String name);
}