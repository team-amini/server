package amini.config;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import lombok.SneakyThrows;

@EnableScheduling
@Configuration
public class AppConfig {

	@Bean
	public Web3j web3(@Value("${network.url}") String url) {
		return Web3j.build(new HttpService(url));
	}

	@Bean
	@SneakyThrows
	@SuppressWarnings("resource")
	public Client client(@Value("${index.server}") String address) {
	    return new PreBuiltTransportClient(Settings.EMPTY)
	            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address), 9300));
	}
	
	
}