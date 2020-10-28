package com.github.juanmougan.jira.worklogs_collector;

import lombok.Value;

@Value
public class JiraProperties {
  private final String url;
  private final String username;
  private final String password;
}
