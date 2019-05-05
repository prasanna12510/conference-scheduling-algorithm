package scheduler;

import java.util.List;
import java.time.LocalTime;
import java.util.ArrayList;

public class Track {

	private List<Event> morningSessionTasks = new ArrayList<Event>();
	private List<Event> afterNoonSessionTasks = new ArrayList<Event>();
	private List<Event> afterBreakSessionTasks = new ArrayList<Event>();

	/* Initialise the duration in Mins for each slot */
	public static final int MORNING_SESSION_DURATION = 210;
	public static final int AFTERNOON_SESSION_DURATION = 90;
	public static final int AFTERTEA_SESSION_DURATION = 105;

	/* Get the total duration of the all the events */
	private int getScheduledTime(List<Event> talks) {
		int scheduledTime = 0;
		if (talks == null) {
			return scheduledTime;
		}
		for (Event talk : talks) {
			scheduledTime += talk.getDuration();
		}
		return scheduledTime;
	}

	/* find the remaining capacity based for each slot */

	private int getFreeTimeInAfterNoonSession() {
		return AFTERNOON_SESSION_DURATION - getScheduledTime(afterNoonSessionTasks);
	}

	private int getFreeTimeInMorningSession() {
		return MORNING_SESSION_DURATION - getScheduledTime(morningSessionTasks);
	}

	private int getFreeTimeinAfterTeaSession() {
		return AFTERTEA_SESSION_DURATION - getScheduledTime(afterBreakSessionTasks);
	}

	/*
	 * Add Event based on the capacity and duration in each slot
	 * 
	 * 1.MorningSlot Starts at 9.00 AM 
	 * 2.Afternoon Slot starts at 13.30 PM
	 * 3.AfterTea Slot starts at 15.15 PM
	 */

	public boolean addEvent(Event event) {

		if (getFreeTimeinAfterTeaSession() >= event.getDuration()) {

			if (afterBreakSessionTasks.size() == 0) {
				LocalTime startTime = LocalTime.of(15, 15);
				event.setStartTime(startTime);
			} else {
				Event lastEvent = afterBreakSessionTasks.get(afterBreakSessionTasks.size() - 1);
				LocalTime startTime = lastEvent.getEndTime();
				event.setStartTime(startTime);

			}
			afterBreakSessionTasks.add(event);
			return true;

		} else if (getFreeTimeInAfterNoonSession() >= event.getDuration()) {
			if (afterNoonSessionTasks.size() == 0) {
				LocalTime startTime = LocalTime.of(13, 30);
				event.setStartTime(startTime);
			} else {
				Event lastEvent = afterNoonSessionTasks.get(afterNoonSessionTasks.size() - 1);
				LocalTime startTime = lastEvent.getEndTime();
				event.setStartTime(startTime);
			}

			afterNoonSessionTasks.add(event);
			return true;
		}

		else if (getFreeTimeInMorningSession() >= event.getDuration()) {

			Event lastTalk = morningSessionTasks.get(morningSessionTasks.size() - 1);
			LocalTime startTime = lastTalk.getEndTime();
			event.setStartTime(startTime);

			morningSessionTasks.add(event);
			return true;

		} else {
			return false;
		}
	}

	/*
	 * Print schedule in Order add all morningTasks, LUNCH, AfterLunchTasks, TEA,
	 * AfterTeaTasks.
	 */
	public void printSchedule() {
		List<Event> eventList = new ArrayList<Event>(morningSessionTasks);
		Event lunch = new Event("LUNCH", 60, "");
		LocalTime lunchstartTime = LocalTime.of(12, 30);
		lunch.setStartTime(lunchstartTime);
		eventList.add(lunch);
		eventList.addAll(afterNoonSessionTasks);

		Event tea = new Event("TEA", 15, "");
		LocalTime teastartTime = LocalTime.of(15, 00);
		tea.setStartTime(teastartTime);
		eventList.add(tea);
		eventList.addAll(afterBreakSessionTasks);

		for (Event event : eventList) {
			System.out.println(event);
		}
	}

	/*
	 * Add keynote event at the start of MorningTasks
	 */
	public void addStartingEvent(Event event) {

		if (event.getType().equals("KEYNOTE")) {

			event.setStartTime(LocalTime.of(9, 0));
			morningSessionTasks.add(event);
		}

	}

	/*
	 * Add closing event at the end of AfterteaTasks
	 */
	public void addClosingEvent(Event event) {
		Event lastTalk = afterNoonSessionTasks.get(afterNoonSessionTasks.size() - 1);

		if (!lastTalk.getType().equals("CLOSING")) {

			LocalTime startTime = lastTalk.getEndTime().isBefore(LocalTime.of(17, 0)) ? LocalTime.of(17, 0)
					: lastTalk.getEndTime();
			event.setStartTime(startTime);
			afterBreakSessionTasks.add(event);
		}
	}

}
