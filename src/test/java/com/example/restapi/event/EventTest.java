package com.example.restapi.event;

import junitparams.Parameters;
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
  /*  private Object[] parametersForTestFree() {
        return new Object[] {
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }
    @Test
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }*/


}