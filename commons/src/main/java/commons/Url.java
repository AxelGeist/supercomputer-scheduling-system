package commons;

public class Url {

    private static final String authenticationUrl = "http://localhost:8081";
    private static final String jobsUrl = "http://localhost:8083";
    private static final String schedulerUrl = "http://localhost:8084";
    private static final String clustersUrl = "http://localhost:8085";

    public static String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public static String getJobsUrl() {
        return jobsUrl;
    }

    public static String getSchedulerUrl() {
        return schedulerUrl;
    }

    public static String getClustersUrl() {
        return clustersUrl;
    }
}
