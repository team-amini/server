package amini.service;

import static java.lang.Float.parseFloat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import amini.model.Event;
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

	@Async
	@SneakyThrows
	@SuppressWarnings("unused")
	public void simulate() {
		log.info("[Simulation] >>>> START!!!");

		for (int j = 1; j < 3; j++) {
			val reader = new BufferedReader(new InputStreamReader(transactionsFile.getInputStream()));
			String line = null;
			boolean flag = true;

			int n = 1;
			while ((line = reader.readLine()) != null) {
				if (flag) {
					flag = false;
					continue;
				}

				String[] fields = line.split("\t");

				int i = 0;
				String cityFrom = fields[i++];
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
				String cityTo = fields[i++];

				val event = new Event().setCityFrom(cityFrom).setCityTo(cityTo).setLatitudeFrom(latitudeFrom)
						.setLongitudeFrom(longitudeFrom).setLatitudeTo(latitudeFrom).setLongitudeTo(longitudeTo)
						.setSenderAccount(from).setReceiverAccount(to).setAmount(parseFloat(value))
						.setBalance(randFloat(1000f, 10000000f));

				log.info("[Simulation][{}]: {}", n++, event);
				val receipt = service.send(event);
				// log.info("Recept: {}", receipt.get());
			}
		}
		log.info("[Simulation] <<< END!!!");
	}

	private static float randFloat(float min, float max) {
		val r = new Random();
		return min + (float) (r.nextFloat() * ((1 + max) - min));
	}

}
