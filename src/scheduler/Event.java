package scheduler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event implements Comparable<Event> {
	
	
	private String topic;
	private int duration;
	private String type;
	private LocalTime startTime;
	private String TIME_PATTERN="hh:mm a";
	private final String SPACE=" ";
	
	public Event() {
		this.duration=0;
		this.topic="default";
		this.type="default";
	}
	
	
	public Event(String topic, int duration,String type) {
	
		this.topic=topic;
		this.duration=duration;
		this.type=type;
	}


	@Override
	public int compareTo(Event o) {
		return Integer.compare(this.duration, o.duration);	
	}
	
	
	public LocalTime getEndTime() {
		return startTime.plusMinutes(duration);
	}
	
	
	@Override
	public String toString() {
	
		StringBuilder scheduleStringBuilder = new StringBuilder();
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
		String startTimeString = formatter.format(startTime);
		scheduleStringBuilder.append(startTimeString);

		scheduleStringBuilder.append(SPACE);
		scheduleStringBuilder.append(topic);

		if (!topic.equals("LUNCH") && !topic.equals("TEA"))
		{
			scheduleStringBuilder.append(SPACE);
			scheduleStringBuilder.append(type);
			scheduleStringBuilder.append(SPACE);
			scheduleStringBuilder.append(duration + "min");
		}
		
		return scheduleStringBuilder.toString();
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public LocalTime getStartTime() {
		return startTime;
	}


	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	

}
