package com.github.juanmougan.jira.worklogs_collector;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import io.atlassian.util.concurrent.Promise;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class WorklogServiceTests {

	@Value("${jira.daily.threshold.minutes}")
	private final int thresholdInMinutes = 390;

	@Autowired
	private WorklogService worklogService;

	@Autowired
	private JiraClientManager jiraClientManager;

	@Autowired
	private GenericReactiveWebApplicationContext context;

	@Test
	void givenWorklogBelowThreshold_whenGetDailyWorklog_thenReturnBelowStatus() {
		mockSearchRestClientBelow();
		final DailyWorklog worklog = worklogService.getDailyWorklog();
		assertThat(worklog).extracting(DailyWorklog::getStatus).isEqualTo(Status.BELOW);
	}

	@Test
	void givenWorklogMatchingThreshold_whenGetDailyWorklog_thenReturnOkStatus() {
		mockResultPromiseOk();
		final DailyWorklog worklog = worklogService.getDailyWorklog();
		assertThat(worklog).extracting(DailyWorklog::getStatus).isEqualTo(Status.OK);
	}

	private void mockSearchRestClient(final Supplier<Promise<SearchResult>> expectedResult) {
		final JiraRestClient jiraRestClient = jiraClientManager.getJiraRestClient();
		final SearchRestClient searchRestClient = mock(SearchRestClient.class);
		when(jiraRestClient.getSearchClient()).thenReturn(searchRestClient);
		when(searchRestClient.searchJql(any())).thenReturn(expectedResult.get());
	}

	private void mockSearchRestClientBelow() {
		mockSearchRestClient(mockResultPromise(thresholdInMinutes - 1));
	}

	private void mockResultPromiseOk() {
		mockSearchRestClient(mockResultPromise(thresholdInMinutes));
	}

	private Supplier<Promise<SearchResult>> mockResultPromise(int minutes) {
		final Promise<SearchResult> resultPromise = mock(Promise.class);
		final SearchResult searchResult = mock(SearchResult.class);
		when(searchResult.getTotal()).thenReturn(minutes);
		when(resultPromise.claim()).thenReturn(searchResult);
		return () -> resultPromise;
	}

	@TestConfiguration
	static class JiraClientManagerTestConfiguration {
		@Bean
		public JiraClientManager jiraClientManager() {
			final JiraClientManager jiraClientManager = mock(JiraClientManager.class);
			final JiraRestClient jiraRestClient = mock(JiraRestClient.class);
			when(jiraClientManager.getJiraRestClient()).thenReturn(jiraRestClient);
			return jiraClientManager;
		}
	}
}
