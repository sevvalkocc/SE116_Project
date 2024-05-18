import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSimulation {
    private List<Event> events;

    public EventSimulation() {
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
        Collections.sort(events);
    }
    public Event getNextEvent(){
        if(events.isEmpty()){
            return null;
        }else{
            return  events.remove(0);
        }

    }
    public boolean isEmpty(){
        return events.isEmpty();
    }
}
