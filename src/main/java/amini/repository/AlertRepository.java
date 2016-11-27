package amini.repository;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE;

import java.util.List;
import java.util.stream.Stream;

import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import amini.model.Alert;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class AlertRepository {

	private static final String INDEX_NAME = "amini";
	private static final String TYPE_NAME = "alerts";

	static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	Client client;

	public void save(Alert alert) {
		val source = convert(alert);
		val response = client.prepareIndex(INDEX_NAME, TYPE_NAME).setSource(source).setId(alert.getId()).setRefreshPolicy(IMMEDIATE).get();
		alert.setId(response.getId());
	}

	public List<Alert> list() {
		val request = client.prepareSearch(INDEX_NAME);
		log.info("Request: {}", request);

		val response = request.get();
		val hits = response.getHits().getHits();
		return Stream.of(hits).map(this::convert).collect(toList());
	}

	private Alert convert(SearchHit hit) {
		return MAPPER.convertValue(hit.getSource(), Alert.class);
	}

	@SneakyThrows
	private String convert(Alert alert) {
		return MAPPER.writeValueAsString(alert);
	}

}