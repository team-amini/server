package amini.service;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;

import com.fasterxml.jackson.databind.ObjectMapper;

import amini.contract.MetaCoin;
import amini.model.Addresses;
import amini.model.Event;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContractService {

	static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	MetaCoin contract;

	public void send() throws InterruptedException, ExecutionException {
		val event = new Event();
		event.setSenderAccount("foo");
		
		log.info("Sending: {}", event);
		send(event);
	}

	@SneakyThrows
	public void send(Event event) {
		val address = new Address(Addresses.ADDRESS2);
		val amount = new Uint256(new BigInteger("1"));
		val meta = new DynamicBytes(MAPPER.writeValueAsBytes(event));
		val response = contract.sendCoin(address, amount, meta).get();
		log.info("Response: {}", response.getBlockNumber());
	}

}
