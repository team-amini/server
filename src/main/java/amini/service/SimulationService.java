package amini.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimulationService {

	@Autowired
	ContractService service;
	@Value("classpath:transactions.csv")
	private Resource transactionsFile;

	@PostConstruct
	public void init() {
		simulate();
	}

	@Async
	@SneakyThrows
	public void simulate() {
		val reader = new BufferedReader(new InputStreamReader(transactionsFile.getInputStream()));
		String line = null;
		boolean flag = true;
		while((line = reader.readLine()) != null) {
			if (flag) {
				flag = false;
				continue;
			}
			
			String[] fields = line.split("\t");
			
			// "City	Population	Latitude	Longitude	Latitude.Point.From	Longitude.Point.From	To	From	Value	Time	Longitude.Point.To	Latitude.Point.To"
			int i = 0;
			String city = fields[i++];
			String population = fields[i++];
			String latitude = fields[i++];
			String longitude = fields[i++];
			String latitudeFrom = fields[i++];
			String longitudeFrom = fields[i++];
			String to = fields[i++];
			String from = fields[i++];
			String value = fields[i++];
			String time = fields[i++];
			String latitudeTo = fields[i++];
			String longitudeTo = fields[i++];
			
			log.info("{}", Arrays.toString(fields));
			service.send();
		}
		
	}

}
