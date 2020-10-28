package com.github.juanmougan.jira.worklogs_collector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@ComponentScan("com.baeldung.constructordi")
@PropertySource({
    "classpath:jira-rest-client.properties"
})
public class JiraPropertiesConfiguration {

  @Value("${jira.url}")
  private String jiraUrl;

  @Value("${jira.username}")
  private String jiraUsername;

  @Value("${jira.password}")
  private String jiraPassword;

  @Bean
  public JiraProperties jiraProperties() {
    return new JiraProperties(jiraUrl, jiraUsername, jiraPassword);
  }
}
