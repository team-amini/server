package amini.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import amini.model.Alert;
import amini.repository.AlertRepository;
import amini.service.AlertService;
import lombok.val;

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
	
	@PutMapping("/alerts/{id}/disable")
	public Alert disable(@RequestParam("id") String id) {
		val alert = repository.get(id);
		alert.setDisabled(true);
		repository.save(alert);
		service.disable(id);

		return alert;
	}
	
	@PutMapping("/alerts/{id}/enable")
	public Alert enable(@RequestParam("id") String id) {
		val alert = repository.get(id);
		alert.setDisabled(false);
		repository.save(alert);
		service.enable(id);

		return alert;
	}


	@GetMapping("/alerts")
	public List<Alert> list() {
		return repository.list();
	}

}
