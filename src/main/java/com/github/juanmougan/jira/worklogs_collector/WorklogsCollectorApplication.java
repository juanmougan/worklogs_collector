package com.github.juanmougan.jira.worklogs_collector;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WorklogsCollectorApplication implements CommandLineRunner {

  @Autowired
  private WorklogService worklogService;

  public static void main(String[] args) {
    SpringApplication.run(WorklogsCollectorApplication.class, args);
  }

  @SneakyThrows
  @Override
  public void run(final String... args) {
    final DailyWorklog worklog = worklogService.getDailyWorklog();
    System.out.println(new ObjectMapper().writeValueAsString(worklog));
  }
}
