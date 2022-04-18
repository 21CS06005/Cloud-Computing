package com.lakshyajit.notes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		NotesApplication.class,
		Jsr310JpaConverters.class
})
public class NotesApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	public static void main(String[] args) {
		SpringApplication.run(NotesApplication.class, args);
	}

	// Extra

private final static String SECURITY_USER_CONSTRAINT = "CONFIDENTIAL";
private final static String REDIRECT_PATTERN = "/*";
private final static String CONNECTOR_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
private final static String CONNECTOR_SCHEME = "http";

	@Bean
	public TomcatServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat =
				new TomcatServletWebServerFactory() {
	
					@Override
					protected void postProcessContext(Context context) {
						SecurityConstraint securityConstraint = new SecurityConstraint();
						securityConstraint.setUserConstraint(SECURITY_USER_CONSTRAINT);
						SecurityCollection collection = new SecurityCollection();
						collection.addPattern(REDIRECT_PATTERN);
						securityConstraint.addCollection(collection);
						context.addConstraint(securityConstraint);
					}
				};
		tomcat.addAdditionalTomcatConnectors(createHttpConnector());
		return tomcat;
	}
	
	private Connector createHttpConnector() {
		Connector connector =
				new Connector(CONNECTOR_PROTOCOL);
		connector.setScheme(CONNECTOR_SCHEME);
		connector.setSecure(false);
		connector.setPort(80);
		connector.setRedirectPort(443);
		return connector;
	}
}
