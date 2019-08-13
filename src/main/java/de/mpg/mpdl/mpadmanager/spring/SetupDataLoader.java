package de.mpg.mpdl.mpadmanager.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.mpg.mpdl.mpadmanager.model.CoordinateTeam;
import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.repository.CoordinateTeamRepository;
import de.mpg.mpdl.mpadmanager.repository.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CoordinateTeamRepository coordinateTeamRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		// == create initial user
		User testUser = createUserIfNotFound("test@test.com", "fistname", "lastname", "test", "MPDL", "DigitalLabs", "130");
		CoordinateTeam tag1 = createCoordinateTeamIfNotFound("Mattermost");
		CoordinateTeam tag2 =createCoordinateTeamIfNotFound("KEEPER");
		CoordinateTeam tag3 =createCoordinateTeamIfNotFound("Github");
		CoordinateTeam tag4 =createCoordinateTeamIfNotFound("Markdown");
		CoordinateTeam tag5 =createCoordinateTeamIfNotFound("Slack");
		CoordinateTeam tag6 =createCoordinateTeamIfNotFound("WhatsApp");
		CoordinateTeam tag7 =createCoordinateTeamIfNotFound("Trello");

		testUser.getCoordinateTeams().add(tag1);
		testUser.getCoordinateTeams().add(tag2);

		tag1.getUsers().add(testUser);
		tag2.getUsers().add(testUser);

		alreadySetup = true;
	}
	
	private final User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final String organization, final String department, final String telephone) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(passwordEncoder.encode(password));
			user.setEmail(email);
			user.setOrganization(organization);
			user.setDepartment(department);
			user.setTelephone(telephone);
			user.setEnabled(true);
		}
		user = userRepository.save(user);
		return user;
	}

	private final CoordinateTeam createCoordinateTeamIfNotFound(final String name) {
		CoordinateTeam coordinateTeam = coordinateTeamRepository.findByName(name);
		if (coordinateTeam == null) {
			coordinateTeam = new CoordinateTeam(name);
		}
		coordinateTeam = coordinateTeamRepository.save(coordinateTeam);
		return coordinateTeam;
	}
}
