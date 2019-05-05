# conference-scheduling-algorithm
event scheduling for conference management

## Steps to run code:
1. Go to home folder of project.
2. Execute `mvn clean install`
3. Go to newly created `target` folder.
4. Execute `java -jar <executable.jar>` <br />
OR <br />
1. Go to src/scheduler
2. Execute `java SchedulerMain.java`

## Assumptions:
1. The input file passed contains valid list of talks
2. If conference has multiple tracks, each track will have their own KEYNOTE and CLOSING event.

## References:
1. Wikipedia page for bin packing problem - https://en.wikipedia.org/wiki/Bin_packing_problem
