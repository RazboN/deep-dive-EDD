package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    @Autowired
    private EventStore eventStore;

    @Autowired
    private EventProducer eventProducer;

    @Value("${spring.kafka.topic}")
    private String topic;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(),
                aggregate.getUncommittedChanges(),
                aggregate.getVersion());
                aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(evn ->
                    evn.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public void republishEvent() {
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggrId : aggregateIds) {
            var aggregate = getById(aggrId);
            if (aggregate == null || !aggregate.getActive())
                continue;

            var events = eventStore.getEvents(aggrId);
            for (var event : events) {
                eventProducer.produce(topic, event);
            }
        }
    }
}
