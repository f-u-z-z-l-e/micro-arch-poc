package ch.fuzzle.event;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class BaseEvent {

    private UUID eventId;

    private ZonedDateTime timestamp;

}
