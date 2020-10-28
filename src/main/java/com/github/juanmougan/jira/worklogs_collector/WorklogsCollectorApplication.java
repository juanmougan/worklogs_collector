package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WorklogsCollectorApplication implements CommandLineRunner {

  @Autowired
  private JiraClientManager jiraClientManager;

  @Autowired
  private WorklogService worklogService;

  public static void main(String[] args) {
    SpringApplication.run(WorklogsCollectorApplication.class, args);
  }

  @Override
  public void run(final String... args) {
    final JiraRestClient jiraRestClient = jiraClientManager.getJiraRestClient();

    final DailyWorklog worklog = worklogService.getDailyWorklog(jiraRestClient);

    System.out.printf("Today logged %d minutes in %d tickets%n", worklog.getTodayLoggedMinutes(), worklog.getTotalIssuesWithLoggedTime());
  }
}
