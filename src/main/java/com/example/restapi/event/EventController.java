package com.example.restapi.event;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;
    @PostMapping()
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, BindingResult error){
        if(error.hasErrors()){// @어노테이션을 사용한 에러 처리
            return ResponseEntity.badRequest().body(error);
        }
        eventValidator.validate(eventDto, error);
        if(error.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = eventRepository.save(event);
        event.setId(1);
        EventResource eventResource=new EventResource(newEvent);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        URI createUri = selfLinkBuilder.toUri();
        return ResponseEntity.created(createUri).body(eventResource);
    }
}
