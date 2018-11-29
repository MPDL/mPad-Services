package de.mpg.mpdl.mpadmanager.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mpg.mpdl.mpadmanager.security.ActiveUserStore;

@Configuration
public class AppConfig {

	@Bean
	public ActiveUserStore activeUserStore() {
		return new ActiveUserStore();
	}
}
