package amini.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import amini.model.Event;
import lombok.SneakyThrows;
import lombok.val;

@Repository
public class EventRepository {
	
	private static final String INDEX_NAME = "amini";
	private static final String TYPE_NAME = "events";

	static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	Client client;
	
	@PostConstruct
	public void  init() {
		val indices = client.admin().indices();
		if (!indices.prepareExists(INDEX_NAME).get().isExists()) {
			indices.prepareCreate(INDEX_NAME).get();
		}
	}

	public void save(Event event) {
		val source = convert(event);
		client.prepareIndex(INDEX_NAME, TYPE_NAME).setSource(source).get();
	}

	public List<Event> list(Long startTime, Long endTime) {
		val response = client.prepareSearch(INDEX_NAME).get();
		val hits = response.getHits().getHits();
		return Stream.of(hits).map(this::convert).collect(toList()); 
	}

	private Event convert(SearchHit hit) {
		return MAPPER.convertValue(hit.getSource(), Event.class);
	}

	@SneakyThrows
	private String convert(Event event) {
		return MAPPER.writeValueAsString(event);
	}

}
