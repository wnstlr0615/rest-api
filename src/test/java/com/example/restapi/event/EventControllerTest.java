package com.example.restapi.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void createEvetnt() throws Exception {
        EventDto eventDto = createEventDto();
        mvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON.toString()))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.toString()))
                .andDo(print())
        ;
    }

    @Test
    public void createEventBadRequest() throws Exception {
        Event event=createEvent();
        mvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(event))
        )
                .andExpect(status().isBadRequest() )
        ;
    }
    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        mvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest())
                ;


    }
    private EventDto createEventDto() {
        return EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();
    }
    private Event createEvent() {
        return Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();
    }
}