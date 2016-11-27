package amini.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import amini.model.Alert;
import amini.repository.AlertRepository;
import amini.service.AlertService;

@CrossOrigin
@RestController
public class AlertController {

	@Autowired
	AlertService service;
	@Autowired
	AlertRepository repository;

	@PostMapping("/alerts")
	public Alert register(@RequestBody Alert alert) {
		repository.save(alert);
		service.register(alert);

		return alert;
	}

	@GetMapping("/alerts")
	public List<Alert> list() {
		return repository.list();
	}

}
