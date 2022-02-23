package com.rakbank.commons.event;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public interface Event {

    UUID getEventId();

    LocalDateTime getDate();
}
