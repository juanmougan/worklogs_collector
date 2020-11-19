package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
@Profile("!test")
public class JiraClientManager {

  @Autowired
  private final JiraProperties jiraProperties;

  public JiraRestClient getJiraRestClient() {
    final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
    final AuthenticationHandler basicHttpAuthenticationHandler = new BasicHttpAuthenticationHandler(jiraProperties.getUsername(), jiraProperties.getPassword());
    return factory.create(URI.create(jiraProperties.getUrl()), basicHttpAuthenticationHandler);
  }
}
