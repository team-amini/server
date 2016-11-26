package amini.config;

import java.math.BigInteger;
import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.google.common.io.Resources;

import amini.contract.MetaCoin;
import amini.model.Addresses;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Bean
	public Credentials credentials() throws Exception {
		val credentials = WalletUtils.loadCredentials("amini", Resources.getResource("wallet.json").getFile());
		log.info("{}", credentials);
		return credentials;
	}

	@Bean
	public MetaCoin contract(Web3j web3, Credentials credentials) throws Exception {
		val contractAddress = Addresses.CONTRACT_ADDRESS;
		val gasPrice = new BigInteger("30000");
		val gasLimit = new BigInteger("200000");
		return MetaCoin.load(contractAddress, web3, credentials, gasPrice, gasLimit);
	}

}
