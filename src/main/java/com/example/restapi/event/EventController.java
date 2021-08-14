package com.example.restapi.event;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
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
        event.setId(1l);
        EventResource eventResource=new EventResource(newEvent);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("http://localhost:63342/Rest-Api/target/classes/static/docs/index.html?_ijt=e3m094i57ed7dsdj570br7mbej#resources-events-create").withRel("profile"));
        URI createUri = selfLinkBuilder.toUri();
        return ResponseEntity.created(createUri).body(eventResource);
    }
    @GetMapping("")
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        Page<Event> events = eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> entityModels = assembler.toModel(events, EventResource::new);
        entityModels.add(linkTo(EventController.class).withRel("create-event"));
        entityModels.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable("id") Long eventId){
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if(eventOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        EventResource eventResource = new EventResource(eventOptional.get());
        eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Long id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event existingEvent = optionalEvent.get();


        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }
}
