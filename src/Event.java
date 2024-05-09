public class Event {
    private String eventId;
    private int eventTime;
    //private EventType eventType;


    public Event(String eventId, int eventTime /*EventType eventType*/) {
        this.eventId = eventId;
        this.eventTime = eventTime;
        //this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    /*public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }*/
}
