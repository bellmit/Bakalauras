package com.imckify.bakis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EnableCaching
@EnableScheduling
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

	@Component
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public class CorsFilter implements Filter {

		public void init(FilterConfig filterConfig) {} // not needed

		public void destroy() {} //not needed

		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
			HttpServletResponse response = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods",
					"ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers",
					"Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				chain.doFilter(req, res);
			}
		}
	}

	@Bean
	public MetadataStore metadataStore() {
		PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
		String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("application.properties/..")).getPath();
		metadataStore.setBaseDirectory(path + "temp");
		return metadataStore;
	}

	@Component
	@ConfigurationProperties(prefix="api.edgar")
	public class PropsConfig {

		private List<URL> feeds = new ArrayList<>();

		public List<URL> getFeeds() { return this.feeds; }
	}

	@ResponseStatus(value =  HttpStatus.NOT_FOUND)
	public static class ResourceNotFoundException extends RuntimeException{
		public  ResourceNotFoundException(String message){
			super(message);
		}
	}
}