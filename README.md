Cron4Joda
=========

I've used Cron4J (http://www.sauronsoftware.it/projects/cron4j/) a while on a private project of mine with some minor modification to add support for time zones. When that private project was updated to only use Joda Time (http://joda-time.sourceforge.net/) it was desirable to also have a cron pattern parser exclusively using Joda Time internally as well.

Hence, Cron4Joda was born. It's a stripped down fork of Cron4J 2.2.4, only including the cron pattern parsing code. All internal time representations and manipulations exclusively use Joda Time without any dependency on native Java date and time classes.

I didn't have any unit testing on the original Cron4J project, so I added some myself. If you find any issues/bugs with the software feel free to send me a patch + added unit test illustrating the issue.
