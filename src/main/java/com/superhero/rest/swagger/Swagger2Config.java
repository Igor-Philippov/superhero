package com.superhero.rest.swagger;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	
	@Autowired
	private Environment env;
	
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.select()
        		.apis(RequestHandlerSelectors.basePackage("com.superhero.rest.controller"))
        		.paths(PathSelectors.regex("/.*"))
        		.build()
        		.apiInfo(apiInfo());
    }
    
    ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(env.getProperty("info.build.artifact.title"))
				.description(env.getProperty("info.build.artifact.description") +  
						" - ENVIRONMENT: " +  (env.getActiveProfiles().length > 0 ? Arrays.toString(env.getActiveProfiles()) : Arrays.toString(env.getDefaultProfiles())))
				.version(env.getProperty("info.build.artifact.version"))
				.license(env.getProperty("info.build.artifact.copyright"))
				.licenseUrl(env.getProperty("info.build.artifact.copyright.link"))
				.contact(new Contact(env.getProperty("info.build.artifact.contact.name"), 
						             env.getProperty("info.build.artifact.contact.site"), 
						             env.getProperty("info.build.artifact.contact.email")))
				.build();
    }  
}