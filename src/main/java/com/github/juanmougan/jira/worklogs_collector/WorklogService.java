package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.TimeTracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@PropertySource({
        "classpath:application-local.properties"
})
public class WorklogService {
  @Value("${jira.worklogs.query}")
  private String query;

  @Value("${jira.daily.threshold.minutes}")
  private int thresholdInMinutes;

  @Autowired
  private JiraClientManager jiraClientManager;

  private JiraRestClient jiraRestClient;

  @PostConstruct
  public void init() {
    jiraRestClient = jiraClientManager.getJiraRestClient();
  }

  public DailyWorklog getDailyWorklog() {
    final SearchResult loggedToday = jiraRestClient
            .getSearchClient()
            .searchJql(query)
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

    return new DailyWorklog(totalIssuesWithLoggedTime, todayLoggedMinutes, calculateStatus(todayLoggedMinutes));
  }

  // TODO refactor
  private Status calculateStatus(final int todayLoggedMinutes) {
    if (todayLoggedMinutes < thresholdInMinutes) {
      return Status.BELOW;
    } else if (todayLoggedMinutes == thresholdInMinutes) {
      return Status.OK;
    } else {
      return Status.OVERTIME;
    }
  }
}
