package edu.ucsal.fiadopay.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScheduledJob {
    int intervalSeconds();
}
