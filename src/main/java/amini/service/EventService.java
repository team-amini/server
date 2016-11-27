package amini.service;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthLog.LogObject;
import org.web3j.protocol.core.methods.response.EthLog.LogResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import amini.model.Event;
import amini.repository.EventRepository;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

	static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	Web3j web3;
	volatile BigInteger filterId;

	@Autowired
	EventRepository repository;
	
	@Autowired
	AlertService esperService;

	final Set<ResponseBodyEmitter> emitters = Sets.newConcurrentHashSet();

	@PostConstruct
	public void init() throws Exception {
		val response = web3.ethNewFilter(allFilter()).send();
		filterId = response.getFilterId();
	}

	public void register(ResponseBodyEmitter emitter) {
		emitters.add(emitter);
	}

	@SneakyThrows
	@Scheduled(fixedRate = 500)
	public void poll() {
		if (filterId == null)
			return;

		try {
			val response = web3.ethGetFilterChanges(filterId).send();
			for (val entry : getLogs(response)) {
				log.info("Parsing event...");
				val event = parseEvent(entry);
				log.info(" - {}", event);
				sendEvent(event);

				if (event != null) {
					log.info("Registering event...");
					esperService.update(event);
					
					log.info("Saving event...");
					repository.save(event);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendEvent(Object event) {
		val iterator = emitters.iterator();
		while (iterator.hasNext()) {
			val emitter = iterator.next();
			try {
				emitter.send("data: " + MAPPER.writeValueAsString(event) + "\n\n");
			} catch (Exception e) {
				log.warn("{}", e.getMessage());
			}
		}
	}

	@SneakyThrows
	private Event parseEvent(EthLog.LogResult<EthLog.LogObject> entry) {
		try {
			String data = getString(entry.get().getData());
			data = data.substring(data.indexOf("{"));
			log.info("data: {}", data);

			return MAPPER.readValue(data, Event.class);
		} catch (Exception e) {
			return null;
		}
	}

	private static EthFilter allFilter() {
		val address = ImmutableList.<String>of();
		return new EthFilter(EARLIEST, LATEST, address);
	}

	@SneakyThrows
	private static String getString(String value) {
		val data = Hex.decodeHex(value.substring(2).toCharArray());
		return new String(data);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<LogResult<LogObject>> getLogs(EthLog response) {
		return (List) response.getLogs();
	}

}
