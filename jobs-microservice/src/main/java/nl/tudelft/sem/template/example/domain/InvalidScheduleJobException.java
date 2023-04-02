package nl.tudelft.sem.template.example.domain;

import commons.NetId;
import commons.ScheduleJob;

public class InvalidScheduleJobException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public InvalidScheduleJobException(ScheduleJob scheduleJob) {
        super("There is a problem with the following Object: " + scheduleJob);
    }
}
