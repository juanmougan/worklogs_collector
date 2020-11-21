package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
@Profile("!test")
public class JiraClientManager {

  @Autowired
  private JiraProperties jiraProperties;

  @Getter
  private JiraRestClient jiraRestClient;

  @PostConstruct
  public void init() {
    final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
    final AuthenticationHandler basicHttpAuthenticationHandler = new BasicHttpAuthenticationHandler(jiraProperties.getUsername(), jiraProperties.getPassword());
    this.jiraRestClient = factory.create(URI.create(jiraProperties.getUrl()), basicHttpAuthenticationHandler);
  }
}
