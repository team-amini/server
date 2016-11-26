package amini.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import amini.model.Event;
import amini.repository.EventRepository;

@CrossOrigin
@RestController
public class EventController {

	@Autowired
	EventRepository repository;

	@GetMapping("/events")
	public List<Event> getEvents(@RequestParam(required = false) Long startTime,
			@RequestParam(required = false) Long endTime) {
		return repository.list(startTime, endTime);
	}

}
