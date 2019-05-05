package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 *This class is used for Parse Input file,apply scheduling algorithm and print tracks
 *
 */
public class ConferenceScheduler {

	private List<Event> eventList = new ArrayList<Event>();
	private List<Track> trackList = new ArrayList<Track>();
	private Stack<Event> startEventStack = new Stack<Event>();
	private Stack<Event> closeEventStack = new Stack<Event>();
	HashMap<String, Integer> talks = new HashMap<>();

	/*
	 * Initialise the dictionary of event-duration for lookup
	 * */
	public ConferenceScheduler() {
		talks.put("LIGHTNING", 10);
		talks.put("REGULAR_TALK", 30);
		talks.put("KEYNOTE", 30);
		talks.put("WORKSHOP", 60);
		talks.put("CLOSING", 30);
	}

	
	/*
	 * Parse the input json file and store it in event object 
	 * used stack to separately place closing event
	 * */
		
	private void parseInput(String fileName) {
		
		JSONParser parser = new JSONParser();

		try {
			File inputFile = new File(fileName).getAbsoluteFile();

			Object obj = parser.parse(new FileReader(inputFile));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray talkList = (JSONArray) jsonObject.get("talks");

			for (Object talk : talkList) {

				JSONObject talkObj = (JSONObject) talk;

				String topic = (String) talkObj.get("title");
				String type = (String) talkObj.get("type");
				int duration = talks.get(type);

				if (type.equals("KEYNOTE")) {
					Event event = new Event();
					event.setTopic(topic);
					event.setDuration(duration);
					event.setType(type);
					startEventStack.push(event);
					
				} else if(type.equals("CLOSING")){
					Event event = new Event();
					event.setTopic(topic);
					event.setDuration(duration);
					event.setType(type);
					closeEventStack.push(event);

				}else {
					Event event = new Event(topic,duration,type);
					this.eventList.add(event);

				}

			}

		} catch (IOException e) {

			System.out.println("IOEXception while reading file " + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void scheduleTasks() {

		binPackingTasks();
	}
	
	/*This method schedule tasks using bin packing packing fitting largest
	 * element first
	 * Sort the event based on the duration and reverse the list to fit the longest duration event into track.
	 * If there is no capacity it tries to fit the event by creating new track.
	 * once all events are occupied it places the closing event at the end of each track.
	 * 
	 * */

	private void binPackingTasks() {

		Collections.sort(eventList);
		Collections.reverse(eventList);

		Event longestEvent = eventList.get(0);
		int longestEventDuration = longestEvent.getDuration();

		Event shortestEvent = eventList.get(eventList.size() - 1);
		int shortestEventDuration = shortestEvent.getDuration();

		if (longestEventDuration > Math.max(Math.max(Track.MORNING_SESSION_DURATION, Track.AFTERNOON_SESSION_DURATION),
				Track.AFTERTEA_SESSION_DURATION) || shortestEventDuration < 0) {

			System.out.println("Error: Tasks are out of range.\nExiting...");
			System.exit(1);
		}
		
		
		/*add starting event at the start of the track*/
		while(!startEventStack.isEmpty()) {
			
			Track track = new Track();
			track.addStartingEvent(startEventStack.pop());
			trackList.add(track);
		}
		

		for (Event talk : eventList) {

			if (!talk.getType().equals("CLOSING")) {

				boolean isTalkEngaged = false;

				for (Track track : trackList) {

					if (track.addEvent(talk)) {
						isTalkEngaged = true;
						break;
					}
				} // for

				if (!isTalkEngaged) {

					Track track = new Track();
					track.addEvent(talk);
					trackList.add(track);
				}

			} // if

		} // for
		
		
		/*add closing event at the end of the track*/
		for (Track track : trackList) {
			if (!closeEventStack.isEmpty()) {
				track.addClosingEvent(closeEventStack.pop());
			}
		}

	}// binpackingTasks

	private void printTracks() {

		int trackCount = 1;
		for (Track track : trackList) {

			System.out.println("Track " + trackCount++ + ":");
			track.printSchedule();
		}
	}

	public void schedule(String filename) {

		parseInput(filename);
		scheduleTasks();
		printTracks();
	}

}
