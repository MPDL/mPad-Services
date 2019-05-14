package de.mpg.mpdl.mpadmanager.spring;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import de.mpg.mpdl.mpadmanager.model.LdapGroup;
import de.mpg.mpdl.mpadmanager.model.LdapUser;
import de.mpg.mpdl.mpadmanager.model.User;
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
	@Test
	public void save() throws Exception {

		try {
			User user = new User();
			user.setEmail("paipaibear90@gmail.com");
			user.setFirstName("ying");
			user.setLastName("li");
			user.setPassword("pass");
			user.setOrganization("mpdl");
			user.setDepartment("digital lab");
			userRepository.save(user);
			userRepository.findAll().forEach(p -> {
				System.out.println(p);
				System.out.println(p.getEmail());
			});
			
			System.out.println("password: " + userRepository.findByEmail("paipaibear90@gmail.com").getPassword());
			System.out.println("secret: " + userRepository.findByEmail("paipaibear90@gmail.com").getSecret());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
//   @Test
//    public void LdapSave() throws Exception {
//
//        try {            
//            LdapUser john_snow = new LdapUser("John", "Snow", "{SSHA}BWKjEHUGRErcZmrEJqBBDeN2ijM0x1rm", "Max Planck Digital Library", "jhonsnow@mpdl.mpg.de", "017637229897", "asesome", "Digital Labs");
//            ldapUserRepository.create(john_snow); 
//            System.out.println(john_snow.getPassword());
//            
//            List<LdapUser> ldapUsers = ldapUserRepository.findAll();
//            System.out.println(ldapUsers);
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

}
