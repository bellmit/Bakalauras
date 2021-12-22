package com.imckify.bakis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = "com.imckify.bakis.repos")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Bakis {

	public static void main(String[] args) {
		SpringApplication.run(Bakis.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public MetadataStore metadataStore() {
		PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
		String path = this.getClass().getClassLoader().getResource("application.properties/..").getPath();
		metadataStore.setBaseDirectory(path);
		return metadataStore;
	}

	@Component
	@ConfigurationProperties(prefix="api.edgar")
	public class PropsConfig {

		private List<URL> feeds = new ArrayList<>();

		public List<URL> getFeeds() { return this.feeds; }
	}
}