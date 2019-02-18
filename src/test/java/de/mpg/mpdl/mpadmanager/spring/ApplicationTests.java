package de.mpg.mpdl.mpadmanager.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void findAll() {

		try {
			userRepository.findAll().forEach(p -> {
				System.out.println(p);
				System.out.println(p.getEmail());
				System.out.println(p.getFirstName() + p.getLastName());
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void save() throws Exception {

		try {
			User user = new User();
//			user.setUid("31415926");
			user.setEmail("paipaibear90@gmail.com");
			user.setFirstName("ying");
			user.setLastName("li");
			user.setPassword("pass");
			user.setOrganization("mpdl");
			user.setDepartment("digital lab");
			userRepository.save(user);

			userRepository.findAll().forEach(p -> {
				System.out.println(p);
//				System.out.println(p.getUid());
				System.out.println(p.getEmail());
			});

			System.out.println(userRepository.findByEmail("paipaibear90@gmail.com").getPassword());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
