package amini.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import amini.service.SimulationService;

@Configuration
public class SimulationConfig {
	
	@Autowired
	SimulationService simulationService;
	
	@PostConstruct
	public void init() {
		simulationService.simulate();
	}

}
