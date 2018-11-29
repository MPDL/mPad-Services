package de.mpg.mpdl.mpadmanager.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.mpg.mpdl.mpadmanager.ldap.Person;
import de.mpg.mpdl.mpadmanager.ldap.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void findAll() {

		try {
			personRepository.findAll().forEach(p -> {
				System.out.println(p);
				System.out.println(p.getUid());
				System.out.println(p.getCommonName());
			});
		} catch (Exception e) {
			System.out.println(e);
		}


	}

	@Test
	public void save() throws Exception {
		
		try {
			Person person = new Person();
			person.setUid("31415926");
			person.setSuerName("AAA");
			person.setCommonName("aaa");
			person.setUserPassword("123456");
			personRepository.save(person);

			personRepository.findAll().forEach(p -> {
				System.out.println(p);
				System.out.println(p.getUid());
				System.out.println(p.getCommonName());
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
