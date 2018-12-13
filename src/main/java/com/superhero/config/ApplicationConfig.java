package com.superhero.config;

import java.util.Arrays;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties
@Validated
@Component
public class ApplicationConfig {
		
	@Autowired
	private Environment env;
	
    @Value("${spring.security.username}")
    @NotBlank
    private String springSecurityUserName;
        
    @Value("${spring.security.password}")
    @NotBlank
    private String springSecurityUserPwd;
    
    @Value("${spring.security.role}")
    @NotBlank
    private String springSecurityRole;
        
    
    @Value("${info.build.artifact.title}")
    @NotBlank
    private String infoBuildTitle;
    
    @Value("${info.build.artifact.description}")
    @NotBlank
    private String infoBuildDescription;
    
    @Value("${info.build.artifact.version}")
    @NotBlank
    private String infoBuildVersion;
    
    @Value("${info.build.artifact.copyright}")
    @NotBlank
    private String infoBuildCopyright;
    
    @Value("${info.build.artifact.copyright.link}")
    @NotBlank
    private String infoBuildCopyrightLink;
    
    @Value("${info.build.artifact.contact.name}")
    @NotBlank
    private String infoBuildContactName;
    
    @Value("${info.build.artifact.contact.site}")
    @NotBlank
    private String infoBuildContactSite;
    
    @Value("${info.build.artifact.contact.email}")
    @Email(message = "Email should be valid")
    private String infoBuildContactEmail;

    
	public String getSpringSecurityUserName() {
		return springSecurityUserName;
	}

	public void setSpringSecurityUserName(String springSecurityUserName) {
		this.springSecurityUserName = springSecurityUserName;
	}

	public String getSpringSecurityUserPwd() {
		return springSecurityUserPwd;
	}

	public void setSpringSecurityUserPwd(String springSecurityUserPwd) {
		this.springSecurityUserPwd = springSecurityUserPwd;
	}

	public String getSpringSecurityRole() {
		return springSecurityRole;
	}

	public void setSpringSecurityRole(String springSecurityRole) {
		this.springSecurityRole = springSecurityRole;
	}

	public String getInfoBuildTitle() {
		return infoBuildTitle;
	}

	public void setInfoBuildTitle(String infoBuildTitle) {
		this.infoBuildTitle = infoBuildTitle;
	}

	public String getInfoBuildDescription() {
		return infoBuildDescription + " - ENVIRONMENT: " +  (env.getActiveProfiles().length > 0 ? Arrays.toString(env.getActiveProfiles()) : Arrays.toString(env.getDefaultProfiles()));
	}

	public void setInfoBuildDescription(String infoBuildDescription) {
		this.infoBuildDescription = infoBuildDescription;
	}

	public String getInfoBuildVersion() {
		return infoBuildVersion;
	}

	public void setInfoBuildVersion(String infoBuildVersion) {
		this.infoBuildVersion = infoBuildVersion;
	}

	public String getInfoBuildCopyright() {
		return infoBuildCopyright;
	}

	public void setInfoBuildCopyright(String infoBuildCopyright) {
		this.infoBuildCopyright = infoBuildCopyright;
	}

	public String getInfoBuildCopyrightLink() {
		return infoBuildCopyrightLink;
	}

	public void setInfoBuildCopyrightLink(String infoBuildCopyrightLink) {
		this.infoBuildCopyrightLink = infoBuildCopyrightLink;
	}

	public String getInfoBuildContactName() {
		return infoBuildContactName;
	}

	public void setInfoBuildContactName(String infoBuildContactName) {
		this.infoBuildContactName = infoBuildContactName;
	}

	public String getInfoBuildContactSite() {
		return infoBuildContactSite;
	}

	public void setInfoBuildContactSite(String infoBuildContactSite) {
		this.infoBuildContactSite = infoBuildContactSite;
	}

	public String getInfoBuildContactEmail() {
		return infoBuildContactEmail;
	}

	public void setInfoBuildContactEmail(String infoBuildContactEmail) {
		this.infoBuildContactEmail = infoBuildContactEmail;
	}
}
