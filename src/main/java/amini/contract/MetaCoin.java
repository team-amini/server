package amini.contract;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.Contract;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.</p>
 */
@SuppressWarnings("rawtypes")
public final class MetaCoin extends Contract {
    private static final String BINARY = "6060604052600160a060020a033316600090815260208190526040902061271090556101c28061002f6000396000f3606060405260e060020a6000350463402ce7898114610029578063f8b2cb4f146100a3575b610002565b3461000257604080516020600460443581810135601f81018490048402850184019095528484526100d5948235946024803595606494929391909201918190840183828082843750949650505050505050600160a060020a033316600090815260208190526040812054839010156100e9575060006101bb565b3461000257600160a060020a036004351660009081526020819052604090205460408051918252519081900360200190f35b604080519115158252519081900360200190f35b33600160a060020a039081166000818152602081815260408083208054899003905588851680845281842080548a0190558151898152808401838152895193820193909352885191963016947f3a0319c82e186c99fc0f5c8fe84297e5ebbd40c25ef7f453224efdad8af27074948b948b94909260608501928681019291829185918391869190600490601f850104600302600f01f150905090810190601f1680156101a95780820380516001836020036101000a031916815260200191505b50935050505060405180910390a45060015b939250505056";

    private MetaCoin(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public Future<TransactionReceipt> sendCoin(Address receiver, Uint256 amount, DynamicBytes meta) {
		Function function = new Function("sendCoin", Arrays.<Type>asList(receiver, amount, meta), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> getBalance(Address addr) {
        Function function = new Function("getBalance", Arrays.<Type>asList(addr), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<MetaCoin> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(MetaCoin.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public EventValues processRegulatedTransactionEvent(TransactionReceipt transactionReceipt) {
        Event event = new Event("RegulatedTransaction", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        return extractEventParameters(event, transactionReceipt);
    }

    public static MetaCoin load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MetaCoin(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }
}
