package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class JiraRestClientTestConfiguration {
    private JiraClientManager jiraClientManager;

    @Bean
    public JiraRestClient jiraRestClient() {
        return mockJiraClientManager();
    }

    private JiraRestClient mockJiraClientManager() {
        // TODO
        jiraClientManager = Mockito.mock(JiraClientManager.class);
        return jiraClientManager.getJiraRestClient();
    }
}
