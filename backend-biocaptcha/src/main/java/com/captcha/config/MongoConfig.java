package com.captcha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.captcha.entity.Settings;

@Configuration
public class MongoConfig {

	@Autowired
	private ReactiveMongoTemplate template;

	@Bean
	public void ConfigMongo() {
		Settings setting = new Settings();
		setting.setAttempts(4);
		setting.setCaptchalength(5);
		setting.setMode("ALPHANUMERIC");
		setting.setCaseeSensitive(false);
		
		template.save(setting).block();
		
	}
	

}
