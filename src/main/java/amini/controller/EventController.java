package amini.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import amini.model.Event;
import amini.repository.EventRepository;
import amini.service.EventService;
import lombok.val;

@CrossOrigin
@RestController
public class EventController {

	@Autowired
	EventRepository repository;
	@Autowired
	EventService service;

	@GetMapping("/history")
	public List<Event> getHistory(@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime) {
		return repository.list(startTime, endTime);
	}
	
	@GetMapping("/stream")
	public ResponseEntity<ResponseBodyEmitter> getEvents() {
		val emitter = new ResponseBodyEmitter(-1L);
		service.register(emitter);
		
		return ResponseEntity.ok().contentType(new MediaType("text", "event-stream")).body(emitter);
	}

}
