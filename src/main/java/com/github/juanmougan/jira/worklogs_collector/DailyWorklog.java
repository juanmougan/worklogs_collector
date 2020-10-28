package com.github.juanmougan.jira.worklogs_collector;

import lombok.Value;

@Value
public class DailyWorklog {
  int totalIssuesWithLoggedTime;
  int todayLoggedMinutes;
}
