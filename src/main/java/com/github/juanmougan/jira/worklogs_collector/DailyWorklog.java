package com.github.juanmougan.jira.worklogs_collector;

import lombok.Value;

@Value
public class DailyWorklog {
  int totalTickets;
  int minutesLogged;
  Status status;
}
