package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.TimeTracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WorklogService {
  @Value("${jira.worklogs.query}")
  private String query;

  @Value("${jira.daily.threshold.minutes}")
  private int thresholdInMinutes;

  @Autowired
  private JiraClient jiraClient;

  public DailyWorklog getDailyWorklog() {
    final SearchResult loggedToday = jiraClient.search(query);
    final int totalIssuesWithLoggedTime = loggedToday.getTotal();

    final List<Issue> issuesLoggedToday = StreamSupport.stream(loggedToday.getIssues().spliterator(), false)
            .map(Issue::getKey)
            .map(key -> jiraClient.getIssue(key))
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
