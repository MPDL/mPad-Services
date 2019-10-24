package de.mpg.mpdl.mpadmanager.spring;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.mpg.mpdl.mpadmanager.model.LdapUser;
import de.mpg.mpdl.mpadmanager.repository.LdapGroupRepository;
import de.mpg.mpdl.mpadmanager.repository.LdapUserRepository;
import de.mpg.mpdl.mpadmanager.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LdapUserRepository ldapUserRepository;
		
	@Autowired
	private LdapGroupRepository ldapGroupRepository;

//	@Test
//	public void findAll() {
//
//		try {
//			userRepository.findAll().forEach(p -> {
//				System.out.println(p);
//				System.out.println(p.getEmail());
//				System.out.println(p.getFirstName() + p.getLastName());
//			});
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
//
	// @Test
	// public void save() throws Exception {

	// 	try {
	// 		User user = new User();
	// 		user.setEmail("paipaibear90@gmail.com");
	// 		user.setFirstName("ying");
	// 		user.setLastName("li");
	// 		user.setPassword("pass");
	// 		user.setOrganization("mpdl");
	// 		user.setDepartment("digital lab");
	// 		userRepository.save(user);
	// 		userRepository.findAll().forEach(p -> {
	// 			System.out.println(p);
	// 			System.out.println(p.getEmail());
	// 		});
			
	// 		System.out.println("password: " + userRepository.findByEmail("paipaibear90@gmail.com").getPassword());
	// 		System.out.println("secret: " + userRepository.findByEmail("paipaibear90@gmail.com").getSecret());
	// 	} catch (Exception e) {
	// 		System.out.println(e);
	// 	}
	// }
	
  @Test
   public void LdapSave() throws Exception {


			NewThread t1 = new NewThread();
			t1.setName("MyThread-1");
			NewThread t2 = new NewThread();
			t2.setName("MyThread-2");
			t1.start();
			t2.start();


			// LdapGroup ldapGroup = new LdapGroup("test");
			
			// ldapGroupRepository.create(ldapGroup);

			// LdapGroup mpdl = ldapGroupRepository.findBy("ou", "test");
			// throw(new Exception(mpdl.toString()));


			for (int i = 0; i < 500; i++) {
				new NewThread().run();
			// 	String email = i+"@mpdl.mpg.de";

			// 		LdapUser dummy_user = new LdapUser("John", "Snow", "{SSHA}BWKjEHUGRErcZmrEJqBBDeN2ijM0x1rm", "Test Max Planck Digital Library", email, "017637229897", "asesome", "Digital Labs", "80886", "Marienplatz");
			// 		ldapUserRepository.create(dummy_user); 
			// 		System.out.println(dummy_user.getPassword());
			}
			// List<LdapUser> ldapUsers = ldapUserRepository.findAll();
			// System.out.println(ldapUsers);
	 }
	 

	 public class NewThread extends Thread {
    public void run() {

				UUID uuid = UUID.randomUUID();
				LdapUser dummy_user = new LdapUser("John", "Snow", "{SSHA}BWKjEHUGRErcZmrEJqBBDeN2ijM0x1rm", "Test Max Planck Digital Library", uuid.toString(), "017637229897", "asesome", "Digital Labs", "80886", "Marienplatz");
				ldapUserRepository.create(dummy_user); 
    }
	}
}


