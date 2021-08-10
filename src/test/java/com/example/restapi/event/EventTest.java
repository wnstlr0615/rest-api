package com.example.restapi.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    public void builder(){
        Event event=Event.builder().build();
        assertThat(event).isNotNull();
    }
    @Test
    public void defaultEventStatus(){
        Event event = Event.builder().build();
        assertThat(event.getEventStatus()).isNull();
    }
}