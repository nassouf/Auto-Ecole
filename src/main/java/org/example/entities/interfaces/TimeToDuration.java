package org.example.entities.interfaces;

import java.sql.Time;
import java.time.Duration;
@FunctionalInterface
public interface TimeToDuration {
    public Duration transform(Time time);
}
