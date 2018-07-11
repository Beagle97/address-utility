Thank you for the code challenge. It has been a fun activity.

This project was built using Java 10.0.1 and Gradle 4.8.1. There are two projects, api and utilities. I placed the zip code range compacting service into a more general address utilities package because that is probably how I would organize it in a real world environment.

To build the solution and run the unit tests, from the root directory.

    ./gradlew clean build

Logging level is currently set to info, and to display results to the console. On windows I encountered an issue with the gradle build, or my configuration, in which the api/build/libs directory could not be deleted on some runs. Please re-run the build a second time and the issue clears itself. It may be a timing issue with the build tasks--maybe a lock file left in place at the wrong time.

I kept the number of comments inside the code small. I think too many comments take away from readability, and also get out of sync with the actual code as it is maintened over time. It is better if the code itself is written in an understandable fashion--not that I achieved that here.

In developing the solution, I tried to imagine how it might fit into a larger enterprise application. I thought about whether there might be outside integrations that could be called to do the same service, either now or down the road for instance with the USPS. I wondered whether there might be different environments in which it could be deployed. Perhaps some environments are smaller with less traffic and it might be desirable to inject different behavior into the algorythm based on that. In general I did more than I might have otherwise because the algorthym is small, it is a code challenge, and I did not have the benefit of collaborating with peers to get feedback and opinions on what direction to take.

Thanks for taking the time to look at it.
