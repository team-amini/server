package amini.service;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthLog.LogObject;
import org.web3j.protocol.core.methods.response.EthLog.LogResult;

import amini.model.Event;
import amini.repository.EventRepository;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

	@Autowired
	Web3j web3;
	volatile BigInteger filterId;

	@Autowired
	EventRepository repository;
	
	@SneakyThrows
	@Scheduled(fixedRate = 500)
	public void poll() {
		if (filterId == null)
			return;

		val response = web3.ethGetFilterChanges(filterId).send();
		for (val entry : getLogs(response)) {
			val data = getString(entry.get().getData());
			
			val topics = entry.get().getTopics();
			log.info(" - {}: {}", data, topics);
			val event = new Event()
					.setInstrument("MetaCoin")
					.setTimestamp(System.currentTimeMillis())
					.setData(data);
			
			repository.save(event);
		}
	}

	@PostConstruct
	public void init() throws Exception {
		val response = web3.ethNewFilter(allFilter()).send();
		filterId = response.getFilterId();
	}

	private static EthFilter allFilter() {
		val address = (List<String>) null;
		return new EthFilter(EARLIEST, LATEST, address);
	}
	
	@SneakyThrows
	private static String getString(String value) {
		val data = Hex.decodeHex(value.substring(2).toCharArray());
		return new String(data);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<LogResult<LogObject>> getLogs(EthLog response) {
		return (List)response.getLogs();
	}

}
