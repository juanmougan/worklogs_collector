package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class JiraClientManagerTestConfiguration {
    @Bean
    public JiraClientManager jiraClientManager() {
        return mockJiraRestClient();
    }

    private JiraClientManager mockJiraRestClient() {
        // TODO
        final JiraClientManager jiraClientManager = mock(JiraClientManager.class);
        final JiraRestClient jiraRestClient = mock(JiraRestClient.class);
        Mockito.when(jiraClientManager.getJiraRestClient()).thenReturn(jiraRestClient);
        return jiraClientManager;
    }
}
