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

2. Run it from command line
3. You'll see something like

```
2020-10-27 15:15:01.877  INFO 22224 --- [           main] c.g.j.j.w.WorklogsCollectorApplication   : Today logged 180 minutes in 1 tickets
```

### Known issues

If jta package can't be found, [check this fix](https://jira.atlassian.com/browse/JRASERVER-43031?focusedCommentId=1828906&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-1828906)

Enjoy!
