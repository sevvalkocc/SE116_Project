public class Station {
    private String stationId;
    private double stationSpeed;
    private int capacity;
    private String singleTaskTypes;
    private String multipleTaskTypes;

    public Station(String stationId, double stationSpeed, int capacity, String singleTaskTypes,String multipleTaskTypes) {
        this.stationId = stationId;
        this.stationSpeed = stationSpeed;
        this.capacity = capacity;
        this.singleTaskTypes = singleTaskTypes;
        this.multipleTaskTypes = multipleTaskTypes;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public double getStationSpeed() {
        return stationSpeed;
    }
    public void setStationSpeed(double stationSpeed) {
        this.stationSpeed = stationSpeed;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public String isSingleTaskTypes() {
        return singleTaskTypes;
    }
    public void setSingleTaskTypes(String singleTaskTypes) {
        this.singleTaskTypes = singleTaskTypes;
    }
    public String isMultipleTaskTypes() {
        return multipleTaskTypes;
    }
    public void setMultipleTaskTypes(String multipleTaskTypes) {
        this.multipleTaskTypes = multipleTaskTypes;
    }


}
