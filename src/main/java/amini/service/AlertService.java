package amini.service;

import static com.google.common.base.Strings.repeat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.google.common.collect.Maps;

import amini.model.Alert;
import amini.model.Event;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertService {

	@Autowired
	EventService eventService;

	/**
	 * Esper state.
	 */
	private EPServiceProvider provider;
	private EPRuntime runtime;
	private EPAdministrator admin;

	@PostConstruct
	public void init() {
		val configuration = new Configuration();
		configuration.addEventType("Event", Event.class.getName());

		// Setup engine
		this.provider = EPServiceProviderManager.getProvider(this.getClass().getName(), configuration);

		// Shorthands
		this.runtime = provider.getEPRuntime();
		this.admin = provider.getEPAdministrator();
	}

	public void update(Event event) {
		// Send event
		log.info("Updating Esper: {}", event);
		runtime.sendEvent(event);
	}

	public void register(Alert alert) {

		// Setup
		val epl = admin.createEPL(alert.getEpl(), alert.getId());
		epl.addListener(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement,
					EPServiceProvider epServiceProvider) {
				if (newEvents == null) {
					return;
				}

				// Buffer results
				for (val newEvent : newEvents) {
					val data = newEvent.getUnderlying();
					
					val result = Maps.newLinkedHashMap();
					result.put("timestamp", System.currentTimeMillis());
					result.put("type", "alert");
					result.put("id", alert.getId());
					result.put("data", data);
					
					// Log results
					log.info(repeat("-", 80));
					log.info("Result: {}", result);
					log.info(repeat("-", 80));

					eventService.sendEvent(result);
				}

			}
		});

		// Apply stimulus
		log.info(repeat("-", 80));
		log.info("Executing: {}", alert);
		log.info(repeat("-", 80));
	}

	public void enable(String id) {
		val statement = admin.getStatement(id);
		if (statement == null) return;
		if (statement.isStarted()) return;
		
		log.info("Enabling {}...", id);
		statement.start();
	}
	
	public void disable(String id) {
		val statement = admin.getStatement(id);
		if (statement == null) return;
		if (statement.isStopped()) return;
		
		log.info("Disabling {}...", id);
		statement.stop();
	}

}
