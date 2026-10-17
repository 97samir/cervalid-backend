package com.cervalid.platform.blockchain.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.3.
 */
@SuppressWarnings("rawtypes")
public class DocumentRegistry extends Contract {
    public static final String BINARY = "0x608060405234801562000010575f80fd5b5060405162001dda38038062001dda83398181016040528101906200003691906200028d565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603620000a7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016200009e906200031b565b60405180910390fd5b620000bb5f801b82620000c360201b60201c565b50506200033b565b5f620000d68383620001be60201b60201c565b620001b45760015f808581526020019081526020015f205f015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550620001506200022160201b60201c565b73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16847f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a460019050620001b8565b5f90505b92915050565b5f805f8481526020019081526020015f205f015f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16905092915050565b5f33905090565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f62000257826200022c565b9050919050565b62000269816200024b565b811462000274575f80fd5b50565b5f8151905062000287816200025e565b92915050565b5f60208284031215620002a557620002a462000228565b5b5f620002b48482850162000277565b91505092915050565b5f82825260208201905092915050565b7f496e76616c69642061646d696e000000000000000000000000000000000000005f82015250565b5f62000303600d83620002bd565b91506200031082620002cd565b602082019050919050565b5f6020820190508181035f8301526200033481620002f5565b9050919050565b611a9180620003495f395ff3fe608060405234801561000f575f80fd5b50600436106100fe575f3560e01c80637814046811610095578063af5b229111610064578063af5b2291146102d3578063b85a0a6f146102ef578063d547741f1461030b578063fea88c5414610327576100fe565b806378140468146102345780638747d5ed1461026957806391d1485414610285578063a217fddf146102b5576100fe565b80631b929c21116100d15780631b929c21146101ae578063248a9ca3146101cc5780632f2ff15d146101fc57806336568abe14610218576100fe565b806301ffc9a7146101025780630331df0714610132578063096aad781461016257806311dcc91214610192575b5f80fd5b61011c60048036038101906101179190610fb2565b610357565b6040516101299190610ff7565b60405180910390f35b61014c6004803603810190610147919061106a565b6103d0565b6040516101599190610ff7565b60405180910390f35b61017c600480360381019061017791906110eb565b610402565b6040516101899190611138565b60405180910390f35b6101ac60048036038101906101a791906111b2565b610434565b005b6101b66106cf565b6040516101c39190611138565b60405180910390f35b6101e660048036038101906101e19190611223565b6106f3565b6040516101f39190611138565b60405180910390f35b6102166004803603810190610211919061124e565b61070f565b005b610232600480360381019061022d919061124e565b610731565b005b61024e600480360381019061024991906110eb565b6107ac565b604051610260969594939291906113b0565b60405180910390f35b610283600480360381019061027e919061106a565b610910565b005b61029f600480360381019061029a919061124e565b61094a565b6040516102ac9190610ff7565b60405180910390f35b6102bd6109ad565b6040516102ca9190611138565b60405180910390f35b6102ed60048036038101906102e8919061106a565b6109b3565b005b610309600480360381019061030491906110eb565b610a5b565b005b6103256004803603810190610320919061124e565b610c3f565b005b610341600480360381019061033c91906110eb565b610c61565b60405161034e9190610ff7565b60405180910390f35b5f7f7965db0b000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614806103c957506103c882610cae565b5b9050919050565b5f6103fb7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d18361094a565b9050919050565b5f8183604051602001610416929190611416565b60405160208183030381529060405280519060200120905092915050565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d161045e81610d17565b5f801b85036104a2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161049990611487565b60405180910390fd5b5f6104ad8686610402565b90505f60015f8381526020019081526020015f206002015414610505576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104fc906114ef565b60405180910390fd5b6040518060c001604052808781526020018660018111156105295761052861128c565b5b81526020013373ffffffffffffffffffffffffffffffffffffffff16815260200142815260200160011515815260200185858080601f0160208091040260200160405190810160405280939291908181526020018383808284375f81840152601f19601f8201169050808301925050505050505081525060015f8381526020019081526020015f205f820151815f01556020820151816001015f6101000a81548160ff021916908360018111156105e3576105e261128c565b5b021790555060408201518160010160016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550606082015181600201556080820151816003015f6101000a81548160ff02191690831515021790555060a082015181600401908161066d9190611734565b509050503373ffffffffffffffffffffffffffffffffffffffff1686827fe89b5199cd81c757e66a51b2fc73ec4bd8d193de199a48475e38b53e6f47a7fe884289896040516106bf949392919061183d565b60405180910390a4505050505050565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d181565b5f805f8381526020019081526020015f20600101549050919050565b610718826106f3565b61072181610d17565b61072b8383610d2b565b50505050565b610739610e14565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161461079d576040517f6697b23200000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b6107a78282610e1b565b505050565b5f805f805f60605f6107be8989610402565b90505f60015f8381526020019081526020015f2090505f81600201540361081a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610811906118c5565b60405180910390fd5b805f0154816001015f9054906101000a900460ff168260010160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff168360020154846003015f9054906101000a900460ff168560040180805461087a90611567565b80601f01602080910402602001604051908101604052809291908181526020018280546108a690611567565b80156108f15780601f106108c8576101008083540402835291602001916108f1565b820191905f5260205f20905b8154815290600101906020018083116108d457829003601f168201915b5050505050905097509750975097509750975050509295509295509295565b5f801b61091c81610d17565b6109467f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d183610c3f565b5050565b5f805f8481526020019081526020015f205f015f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16905092915050565b5f801b81565b5f801b6109bf81610d17565b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603610a2d576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a249061192d565b60405180910390fd5b610a577f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d18361070f565b5050565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d1610a8581610d17565b5f610a908484610402565b90505f60015f8381526020019081526020015f2090505f816002015403610aec576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ae3906118c5565b60405180910390fd5b806003015f9054906101000a900460ff16610b3c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b3390611995565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168160010160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610bce576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bc5906119fd565b60405180910390fd5b5f816003015f6101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff16827f58dab06ff4a881de7cd15a590640a447aec068b61e18a8b9b4ae28a8984fe3d642604051610c309190611a1b565b60405180910390a35050505050565b610c48826106f3565b610c5181610d17565b610c5b8383610e1b565b50505050565b5f80610c6d8484610402565b90505f60015f8381526020019081526020015f2090505f816002015414158015610ca45750806003015f9054906101000a900460ff165b9250505092915050565b5f7f01ffc9a7000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149050919050565b610d2881610d23610e14565b610f04565b50565b5f610d36838361094a565b610e0a5760015f808581526020019081526020015f205f015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550610da7610e14565b73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16847f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a460019050610e0e565b5f90505b92915050565b5f33905090565b5f610e26838361094a565b15610efa575f805f8581526020019081526020015f205f015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550610e97610e14565b73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16847ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b60405160405180910390a460019050610efe565b5f90505b92915050565b610f0e828261094a565b610f515780826040517fe2517d3f000000000000000000000000000000000000000000000000000000008152600401610f48929190611a34565b60405180910390fd5b5050565b5f80fd5b5f80fd5b5f7fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b610f9181610f5d565b8114610f9b575f80fd5b50565b5f81359050610fac81610f88565b92915050565b5f60208284031215610fc757610fc6610f55565b5b5f610fd484828501610f9e565b91505092915050565b5f8115159050919050565b610ff181610fdd565b82525050565b5f60208201905061100a5f830184610fe8565b92915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f61103982611010565b9050919050565b6110498161102f565b8114611053575f80fd5b50565b5f8135905061106481611040565b92915050565b5f6020828403121561107f5761107e610f55565b5b5f61108c84828501611056565b91505092915050565b5f819050919050565b6110a781611095565b81146110b1575f80fd5b50565b5f813590506110c28161109e565b92915050565b600281106110d4575f80fd5b50565b5f813590506110e5816110c8565b92915050565b5f806040838503121561110157611100610f55565b5b5f61110e858286016110b4565b925050602061111f858286016110d7565b9150509250929050565b61113281611095565b82525050565b5f60208201905061114b5f830184611129565b92915050565b5f80fd5b5f80fd5b5f80fd5b5f8083601f84011261117257611171611151565b5b8235905067ffffffffffffffff81111561118f5761118e611155565b5b6020830191508360018202830111156111ab576111aa611159565b5b9250929050565b5f805f80606085870312156111ca576111c9610f55565b5b5f6111d7878288016110b4565b94505060206111e8878288016110d7565b935050604085013567ffffffffffffffff81111561120957611208610f59565b5b6112158782880161115d565b925092505092959194509250565b5f6020828403121561123857611237610f55565b5b5f611245848285016110b4565b91505092915050565b5f806040838503121561126457611263610f55565b5b5f611271858286016110b4565b925050602061128285828601611056565b9150509250929050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602160045260245ffd5b600281106112ca576112c961128c565b5b50565b5f8190506112da826112b9565b919050565b5f6112e9826112cd565b9050919050565b6112f9816112df565b82525050565b6113088161102f565b82525050565b5f819050919050565b6113208161130e565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b8381101561135d578082015181840152602081019050611342565b5f8484015250505050565b5f601f19601f8301169050919050565b5f61138282611326565b61138c8185611330565b935061139c818560208601611340565b6113a581611368565b840191505092915050565b5f60c0820190506113c35f830189611129565b6113d060208301886112f0565b6113dd60408301876112ff565b6113ea6060830186611317565b6113f76080830185610fe8565b81810360a08301526114098184611378565b9050979650505050505050565b5f6040820190506114295f8301856112f0565b6114366020830184611129565b9392505050565b7f496e76616c6964206861736800000000000000000000000000000000000000005f82015250565b5f611471600c83611330565b915061147c8261143d565b602082019050919050565b5f6020820190508181035f83015261149e81611465565b9050919050565b7f446f63756d656e7420616c7265616479206578697374730000000000000000005f82015250565b5f6114d9601783611330565b91506114e4826114a5565b602082019050919050565b5f6020820190508181035f830152611506816114cd565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061157e57607f821691505b6020821081036115915761159061153a565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026115f37fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826115b8565b6115fd86836115b8565b95508019841693508086168417925050509392505050565b5f819050919050565b5f61163861163361162e8461130e565b611615565b61130e565b9050919050565b5f819050919050565b6116518361161e565b61166561165d8261163f565b8484546115c4565b825550505050565b5f90565b61167961166d565b611684818484611648565b505050565b5b818110156116a75761169c5f82611671565b60018101905061168a565b5050565b601f8211156116ec576116bd81611597565b6116c6846115a9565b810160208510156116d5578190505b6116e96116e1856115a9565b830182611689565b50505b505050565b5f82821c905092915050565b5f61170c5f19846008026116f1565b1980831691505092915050565b5f61172483836116fd565b9150826002028217905092915050565b61173d82611326565b67ffffffffffffffff8111156117565761175561150d565b5b6117608254611567565b61176b8282856116ab565b5f60209050601f83116001811461179c575f841561178a578287015190505b6117948582611719565b8655506117fb565b601f1984166117aa86611597565b5f5b828110156117d1578489015182556001820191506020850194506020810190506117ac565b868310156117ee57848901516117ea601f8916826116fd565b8355505b6001600288020188555050505b505050505050565b828183375f83830152505050565b5f61181c8385611330565b9350611829838584611803565b61183283611368565b840190509392505050565b5f6060820190506118505f8301876112f0565b61185d6020830186611317565b8181036040830152611870818486611811565b905095945050505050565b7f446f63756d656e7420646f6573206e6f742065786973740000000000000000005f82015250565b5f6118af601783611330565b91506118ba8261187b565b602082019050919050565b5f6020820190508181035f8301526118dc816118a3565b9050919050565b7f496e76616c696420696e737469747574696f6e000000000000000000000000005f82015250565b5f611917601383611330565b9150611922826118e3565b602082019050919050565b5f6020820190508181035f8301526119448161190b565b9050919050565b7f416c7265616479207265766f6b656400000000000000000000000000000000005f82015250565b5f61197f600f83611330565b915061198a8261194b565b602082019050919050565b5f6020820190508181035f8301526119ac81611973565b9050919050565b7f4e6f7420646f63756d656e74206f776e657200000000000000000000000000005f82015250565b5f6119e7601283611330565b91506119f2826119b3565b602082019050919050565b5f6020820190508181035f830152611a14816119db565b9050919050565b5f602082019050611a2e5f830184611317565b92915050565b5f604082019050611a475f8301856112ff565b611a546020830184611129565b939250505056fea26469706673582212205e2922441f199138e710a96b5d2772f186624a9e7c3ea83ad382c38c8452b98164736f6c63430008140033";

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_INSTITUTION_ROLE = "INSTITUTION_ROLE";

    public static final String FUNC_ADDINSTITUTION = "addInstitution";

    public static final String FUNC_GETDOCUMENT = "getDocument";

    public static final String FUNC_GETREGISTRYKEY = "getRegistryKey";

    public static final String FUNC_GETROLEADMIN = "getRoleAdmin";

    public static final String FUNC_GRANTROLE = "grantRole";

    public static final String FUNC_HASROLE = "hasRole";

    public static final String FUNC_ISINSTITUTION = "isInstitution";

    public static final String FUNC_REGISTERDOCUMENT = "registerDocument";

    public static final String FUNC_REMOVEINSTITUTION = "removeInstitution";

    public static final String FUNC_RENOUNCEROLE = "renounceRole";

    public static final String FUNC_REVOKEDOCUMENT = "revokeDocument";

    public static final String FUNC_REVOKEROLE = "revokeRole";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_VERIFYDOCUMENT = "verifyDocument";

    public static final Event DOCUMENTREGISTERED_EVENT = new Event("DocumentRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Uint8>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event DOCUMENTREVOKED_EVENT = new Event("DocumentRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ROLEADMINCHANGED_EVENT = new Event("RoleAdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event ROLEGRANTED_EVENT = new Event("RoleGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event ROLEREVOKED_EVENT = new Event("RoleRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected DocumentRegistry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DocumentRegistry(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DocumentRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DocumentRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<DocumentRegisteredEventResponse> getDocumentRegisteredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DOCUMENTREGISTERED_EVENT, transactionReceipt);
        ArrayList<DocumentRegisteredEventResponse> responses = new ArrayList<DocumentRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DocumentRegisteredEventResponse typedResponse = new DocumentRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.registryKey = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.documentHash = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.institution = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.documentType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.metadataURI = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DocumentRegisteredEventResponse getDocumentRegisteredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DOCUMENTREGISTERED_EVENT, log);
        DocumentRegisteredEventResponse typedResponse = new DocumentRegisteredEventResponse();
        typedResponse.log = log;
        typedResponse.registryKey = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.documentHash = (byte[]) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.institution = (String) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.documentType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.metadataURI = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<DocumentRegisteredEventResponse> documentRegisteredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDocumentRegisteredEventFromLog(log));
    }

    public Flowable<DocumentRegisteredEventResponse> documentRegisteredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DOCUMENTREGISTERED_EVENT));
        return documentRegisteredEventFlowable(filter);
    }

    public static List<DocumentRevokedEventResponse> getDocumentRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DOCUMENTREVOKED_EVENT, transactionReceipt);
        ArrayList<DocumentRevokedEventResponse> responses = new ArrayList<DocumentRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DocumentRevokedEventResponse typedResponse = new DocumentRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.registryKey = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DocumentRevokedEventResponse getDocumentRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DOCUMENTREVOKED_EVENT, log);
        DocumentRevokedEventResponse typedResponse = new DocumentRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.registryKey = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<DocumentRevokedEventResponse> documentRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDocumentRevokedEventFromLog(log));
    }

    public Flowable<DocumentRevokedEventResponse> documentRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DOCUMENTREVOKED_EVENT));
        return documentRevokedEventFlowable(filter);
    }

    public static List<RoleAdminChangedEventResponse> getRoleAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<RoleAdminChangedEventResponse> responses = new ArrayList<RoleAdminChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RoleAdminChangedEventResponse getRoleAdminChangedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ROLEADMINCHANGED_EVENT, log);
        RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
        typedResponse.log = log;
        typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRoleAdminChangedEventFromLog(log));
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEADMINCHANGED_EVENT));
        return roleAdminChangedEventFlowable(filter);
    }

    public static List<RoleGrantedEventResponse> getRoleGrantedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEGRANTED_EVENT, transactionReceipt);
        ArrayList<RoleGrantedEventResponse> responses = new ArrayList<RoleGrantedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RoleGrantedEventResponse getRoleGrantedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ROLEGRANTED_EVENT, log);
        RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
        typedResponse.log = log;
        typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRoleGrantedEventFromLog(log));
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEGRANTED_EVENT));
        return roleGrantedEventFlowable(filter);
    }

    public static List<RoleRevokedEventResponse> getRoleRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEREVOKED_EVENT, transactionReceipt);
        ArrayList<RoleRevokedEventResponse> responses = new ArrayList<RoleRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RoleRevokedEventResponse getRoleRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ROLEREVOKED_EVENT, log);
        RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRoleRevokedEventFromLog(log));
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEREVOKED_EVENT));
        return roleRevokedEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> DEFAULT_ADMIN_ROLE() {
        final Function function = new Function(FUNC_DEFAULT_ADMIN_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> INSTITUTION_ROLE() {
        final Function function = new Function(FUNC_INSTITUTION_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> addInstitution(String institution) {
        final Function function = new Function(
                FUNC_ADDINSTITUTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, institution)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple6<byte[], BigInteger, String, BigInteger, Boolean, String>> getDocument(byte[] documentHash, BigInteger documentType) {
        final Function function = new Function(FUNC_GETDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(documentHash), 
                new org.web3j.abi.datatypes.generated.Uint8(documentType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint8>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple6<byte[], BigInteger, String, BigInteger, Boolean, String>>(function,
                new Callable<Tuple6<byte[], BigInteger, String, BigInteger, Boolean, String>>() {
                    @Override
                    public Tuple6<byte[], BigInteger, String, BigInteger, Boolean, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<byte[], BigInteger, String, BigInteger, Boolean, String>(
                                (byte[]) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue(), 
                                (String) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<byte[]> getRegistryKey(byte[] documentHash, BigInteger documentType) {
        final Function function = new Function(FUNC_GETREGISTRYKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(documentHash), 
                new org.web3j.abi.datatypes.generated.Uint8(documentType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> getRoleAdmin(byte[] role) {
        final Function function = new Function(FUNC_GETROLEADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> grantRole(byte[] role, String account) {
        final Function function = new Function(
                FUNC_GRANTROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> hasRole(byte[] role, String account) {
        final Function function = new Function(FUNC_HASROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isInstitution(String account) {
        final Function function = new Function(FUNC_ISINSTITUTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> registerDocument(byte[] documentHash, BigInteger documentType, String metadataURI) {
        final Function function = new Function(
                FUNC_REGISTERDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(documentHash), 
                new org.web3j.abi.datatypes.generated.Uint8(documentType), 
                new org.web3j.abi.datatypes.Utf8String(metadataURI)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeInstitution(String institution) {
        final Function function = new Function(
                FUNC_REMOVEINSTITUTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, institution)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceRole(byte[] role, String callerConfirmation) {
        final Function function = new Function(
                FUNC_RENOUNCEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, callerConfirmation)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeDocument(byte[] documentHash, BigInteger documentType) {
        final Function function = new Function(
                FUNC_REVOKEDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(documentHash), 
                new org.web3j.abi.datatypes.generated.Uint8(documentType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeRole(byte[] role, String account) {
        final Function function = new Function(
                FUNC_REVOKEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> verifyDocument(byte[] documentHash, BigInteger documentType) {
        final Function function = new Function(FUNC_VERIFYDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(documentHash), 
                new org.web3j.abi.datatypes.generated.Uint8(documentType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static DocumentRegistry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DocumentRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DocumentRegistry load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DocumentRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DocumentRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DocumentRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(DocumentRegistry.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(DocumentRegistry.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(DocumentRegistry.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(DocumentRegistry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class DocumentRegisteredEventResponse extends BaseEventResponse {
        public byte[] registryKey;

        public byte[] documentHash;

        public String institution;

        public BigInteger documentType;

        public BigInteger timestamp;

        public String metadataURI;
    }

    public static class DocumentRevokedEventResponse extends BaseEventResponse {
        public byte[] registryKey;

        public String institution;

        public BigInteger timestamp;
    }

    public static class RoleAdminChangedEventResponse extends BaseEventResponse {
        public byte[] role;

        public byte[] previousAdminRole;

        public byte[] newAdminRole;
    }

    public static class RoleGrantedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }

    public static class RoleRevokedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }
}
