# worklogs_collector

## What is it?
Jira client that collects all the work you've done today, and how many tickets you've worked on, and reports that in `stdout`.

### Motivation
Plain and simple: I don't want to forget to log my hours :)

## Usage

1. Create a `jira-rest-client.properties` in your `src/main/resources/` folder, with the following content:

```
jira.url=<your Jira URL>
jira.username=<Your username>
jira.password=<Your password>
```

2. Configure your daily threshold on a `application-local.properties` file, in the same `resources` folder:

```
jira.daily.threshold.minutes=390
```

3. Run it from command line

4. You'll see something like

```
{"totalTickets":1,"minutesLogged":420,"status":"OVERTIME"}
```

### When building the project

I'd use a `./mvnw clean install`, so that the JAR gets created in a predictable path, for client's usage. The output path for the JAR would be:

```
.m2/repository/com/github/juanmougan/jira/worklogs_collector/0.0.1-SNAPSHOT/worklogs_collector-0.0.1-SNAPSHOT.jar
```

### Known issues

If jta package can't be found, [check this fix](https://jira.atlassian.com/browse/JRASERVER-43031?focusedCommentId=1828906&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-1828906)

Enjoy!
