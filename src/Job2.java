public class Job2 {
    private String jobId;
    private String jobTypeId;
    private int startTime;
    private int duration;

    public Job2 (String jobId, String jobTypeId, int startTime, int duration) {
        this.jobId = jobId;
        this.jobTypeId = jobTypeId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobType() {
        return jobTypeId;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId='" + jobId + '\'' +
                ", jobTypeId='" + jobTypeId + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

}

