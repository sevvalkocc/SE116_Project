public class Station {
    private String stationID;
    private int maxCapacity;
    private boolean multiFlag;
    private boolean fifoFlag;
    private String taskSizeInStation;
    private double changedSpeed;

    public Station(String stationID, int maxCapacity, boolean multiFlag, boolean fifoFlag, String taskSizeInStation, double changedSpeed) {
        this.stationID = stationID;
        this.maxCapacity = maxCapacity;
        this.multiFlag = multiFlag;
        this.fifoFlag = fifoFlag;
        this.taskSizeInStation = taskSizeInStation;
        this.changedSpeed = changedSpeed;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean isMultiFlag() {
        return multiFlag;
    }

    public void setMultiFlag(boolean multiFlag) {
        this.multiFlag = multiFlag;
    }

    public boolean isFifoFlag() {
        return fifoFlag;
    }

    public void setFifoFlag(boolean fifoFlag) {
        this.fifoFlag = fifoFlag;
    }

    public String getTaskSizeInStation() {
        return taskSizeInStation;
    }

    public void setTaskSizeInStation(String taskSizeInStation) {
        this.taskSizeInStation = taskSizeInStation;
    }

    public double getChangedSpeed() {
        return changedSpeed;
    }

    public void setChangedSpeed(double changedSpeed) {
        this.changedSpeed = changedSpeed;
    }



}
