package amini.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import amini.model.Event;
import amini.service.ContractService;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
public class ContractController {

	@Autowired
	ContractService service;

	@GetMapping("/send")
	public void send() throws InterruptedException, ExecutionException {
		val event = new Event();
		event.setSenderAccount("/send event");
		log.info("Sending: {}", event);
		
		service.send(event);
	}

}
