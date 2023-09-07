package com.techbank.cqrs.core.events;

import com.techbank.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor@AllArgsConstructor
@SuperBuilder
public class BaseEvent extends Message {
    private int version;
}
