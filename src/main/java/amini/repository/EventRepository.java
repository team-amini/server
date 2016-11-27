package amini.repository;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.sort.SortOrder.ASC;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EventRepository {

	private static final String INDEX_NAME = "amini";
	private static final String TYPE_NAME = "events";

	static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	Client client;

	@PostConstruct
	public void init() {
		log.info("Initializing index...");
		val indices = client.admin().indices();
		val delete = true;
		if (delete) {
			indices.prepareDelete(INDEX_NAME).get();
		}

		if (!indices.prepareExists(INDEX_NAME).get().isExists()) {
			indices.prepareCreate(INDEX_NAME).get();
		}
	}

	public void save(Event event) {
		val source = convert(event);
		client.prepareIndex(INDEX_NAME, TYPE_NAME).setSource(source).setRefreshPolicy(IMMEDIATE).get();
	}

	public List<Event> list(String type, String instrument, String senderAccount, Long startTime, Long endTime) {
		val query = boolQuery();
		if (startTime != null || endTime != null) {
			val timeRange = rangeQuery("timestamp");
			if (startTime != null) {
				timeRange.from(startTime);
			}
			if (endTime != null) {
				timeRange.to(endTime);
			}
			query.filter().add(timeRange);
		}

		if (type != null) {
			query.filter().add(termQuery("type", type));
		}
		if (instrument != null) {
			query.filter().add(termQuery("instrument", instrument));
		}
		if (senderAccount != null) {
			query.filter().add(termQuery("senderAccount", instrument));
		}

		val request = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(query).addSort("timestamp", ASC);
		log.info("Request: {}", request);

		val response = request.get();
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
