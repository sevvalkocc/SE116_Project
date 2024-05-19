import java.util.HashMap;
import java.util.Map;

public class StationM {
    private Map<String, Station> stations;

    public StationM() {
        this.stations = new HashMap<>();
    }

    public void addStation(Station station) {
        if (station != null) {
            stations.put(station.getStationID(), station);
        }
    }

    public Station getBestStationForTask(TaskType task) {
        Station bestStation = null;
        int minQueueLength = Integer.MAX_VALUE;

        for (Station station : stations.values()) {
            if (station.canHandleTaskType(task.getTaskTypeID())) {
                int queueLength = station.getQueueLengthForTask(task.getTaskTypeID());
                if (queueLength < minQueueLength) {
                    minQueueLength = queueLength;
                    bestStation = station;
                }
            }
        }

        return bestStation;
    }

    public Map<String, Station> getStations() {
        return stations;
    }
}
