package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class JiraRestClientConfiguration {
  @Autowired
  private JiraClientManager jiraClientManager;

  @Bean
  public JiraRestClient jiraRestClient() {
    return jiraClientManager.getJiraRestClient();
  }
}
