package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Wrapper for {@link com.atlassian.jira.rest.client.api.JiraRestClient}
 * that only provides the functionality we'll need.
 */
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class JiraClient {

    private final JiraClientManager jiraClientManager;

    public SearchResult search(final String query) {
        return jiraClientManager.getJiraRestClient()
                .getSearchClient()
                .searchJql(query)
                .claim();
    }

    public Issue getIssue(final String key) {
        return jiraClientManager.getJiraRestClient()
                .getIssueClient()
                .getIssue(key)
                .claim();
    }
}
