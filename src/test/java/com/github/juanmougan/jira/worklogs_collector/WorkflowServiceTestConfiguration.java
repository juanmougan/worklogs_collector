package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.bc.workflow.WorkflowService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class WorkflowServiceTestConfiguration {
  @Bean
  @Primary
  public WorkflowService workflowService() {
    return Mockito.mock(WorkflowService.class);
  }
}
