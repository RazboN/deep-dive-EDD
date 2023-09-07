package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.events.BaseEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    public String getId() {
        return this.id;
    }
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public List<BaseEvent> getUncommittedChanges() {
        return this.changes;
    }
    public void markChangesAsCommitted() {
        this.changes.clear();
    }
    protected void applyChanges(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
        } catch (NoSuchMethodException ex) {
            logger.log(Level.WARNING,
                    MessageFormat.format("The Apply method was not found in the aggregate for {0}",
                            event.getClass().getName()));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error applying event to aggregate", ex);
        } finally {
            if(isNewEvent) {
                changes.add(event);
            }
        }
    }
    public void raiseEvent(BaseEvent event) {
        applyChanges(event, true);
    }
    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChanges(event, false));
    }
}