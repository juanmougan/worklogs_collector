package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.TimeTracking;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
@Slf4j
@PropertySource({
    "classpath:jira-rest-client.properties"
})
public class WorklogsCollectorApplication implements CommandLineRunner {

  @Value("${jira.url}")
  private String jiraUrl;

  @Value("${jira.username}")
  private String jiraUsername;

  @Value("${jira.password}")
  private String jiraPassword;

  public static void main(String[] args) {
    SpringApplication.run(WorklogsCollectorApplication.class, args);
  }

  @Override
  public void run(final String... args) {
    final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
    final AuthenticationHandler basicHttpAuthenticationHandler = new BasicHttpAuthenticationHandler(jiraUsername, jiraPassword);
    final JiraRestClient jiraRestClient = factory.create(URI.create(jiraUrl), basicHttpAuthenticationHandler);

    final SearchResult loggedToday = jiraRestClient
        .getSearchClient()
        .searchJql("worklogAuthor = currentUser() AND worklogDate >= startOfDay() AND worklogDate <= endOfDay()")
        .claim();
    final int totalIssuesWithLoggedTime = loggedToday.getTotal();

    final List<Issue> issuesLoggedToday = StreamSupport.stream(loggedToday.getIssues().spliterator(), false)
        .map(Issue::getKey)
        .map(key -> jiraRestClient.getIssueClient().getIssue(key).claim())
        .collect(Collectors.toList());

    final int todayLoggedMinutes = issuesLoggedToday.stream()
        .map(Issue::getTimeTracking)
        .filter(Objects::nonNull)
        .map(TimeTracking::getTimeSpentMinutes)
        .reduce(0, Integer::sum);

    log.info("Today logged {} minutes in {} tickets", todayLoggedMinutes, totalIssuesWithLoggedTime);
  }
}
