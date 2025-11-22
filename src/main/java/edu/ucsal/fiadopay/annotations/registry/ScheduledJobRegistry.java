package edu.ucsal.fiadopay.annotations.registry;

import edu.ucsal.fiadopay.annotations.ScheduledJob;
import java.util.ArrayList;
import java.util.List;

public class ScheduledJobRegistry {

    private static final List<ScheduledJob> jobs = new ArrayList<>();

    public static void register(ScheduledJob job) {
        jobs.add(job);
    }

    public static List<ScheduledJob> all() {
        return jobs;
    }
}
