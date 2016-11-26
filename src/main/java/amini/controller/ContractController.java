package amini.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import amini.service.ContractService;

@CrossOrigin
@RestController
public class ContractController {

	@Autowired
	ContractService service;

	@GetMapping("/send")
	public void send() throws InterruptedException, ExecutionException {
		service.send();
	}

}
