package scheduler;

public class SchedulerMain {

	public static void main(String[] args) {
		
		ConferenceScheduler scheduler =  new ConferenceScheduler();
		scheduler.schedule("talks.json");	

	}

}
