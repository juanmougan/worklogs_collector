package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import io.atlassian.util.concurrent.Promise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class WorklogServiceTests {

	private JiraRestClient jiraRestClient;

	@Value("${jira.daily.threshold.minutes}")
	private int thresholdInMinutes;

	@Autowired
	private WorklogService worklogService;

	@BeforeEach
	void setUp() {
		jiraRestClient = mock(JiraRestClient.class);
	}

	@Test
	void givenWorklogBelowThreshold_whenGetDailyWorklog_thenReturnBelowStatus() {
		givenWorklogThresholdMatchingMinutes(thresholdInMinutes - 1);
		final DailyWorklog worklog = worklogService.getDailyWorklog();
		assertThat(worklog).extracting(DailyWorklog::getStatus).isEqualTo(Status.BELOW);
	}

	@Test
	void givenWorklogMatchingThreshold_whenGetDailyWorklog_thenReturnOkStatus() {
		givenWorklogThresholdMatchingMinutes(thresholdInMinutes);
		final DailyWorklog worklog = worklogService.getDailyWorklog();
		assertThat(worklog).extracting(DailyWorklog::getStatus).isEqualTo(Status.OK);
	}

	private void givenWorklogThresholdMatchingMinutes(int minutes) {
		final SearchResult searchResult = mockJiraRestClient();
		when(searchResult.getTotal()).thenReturn(minutes);
	}

	@SuppressWarnings("unchecked")
	private SearchResult mockJiraRestClient() {
		final SearchRestClient searchRestClient = mock(SearchRestClient.class);
		when(jiraRestClient.getSearchClient()).thenReturn(searchRestClient);
		final Promise<SearchResult> resultPromise = mock(Promise.class);
		when(searchRestClient.searchJql(any())).thenReturn(resultPromise);
		final SearchResult searchResult = mock(SearchResult.class);
		when(resultPromise.claim()).thenReturn(searchResult);
		return searchResult;
	}

}
