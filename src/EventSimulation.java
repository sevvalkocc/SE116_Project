import java.util.*;

public class EventSimulation {
    private PriorityQueue<Event> queue;

    public EventSimulation() {
        this.queue = new PriorityQueue<>(Comparator.comparingInt(Event::getTime));
    }

    public void addEvent(Event event) {
        queue.add(event);
    }

    public Event getNextEvent() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
