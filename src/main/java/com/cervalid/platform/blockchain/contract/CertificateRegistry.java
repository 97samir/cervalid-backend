package com.cervalid.platform.blockchain.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@SuppressWarnings("rawtypes")
@Generated("org.web3j.codegen.SolidityFunctionWrapperGenerator")
public class CertificateRegistry extends Contract {
    public static final String BINARY = "608060405234801562000010575f80fd5b5060405162001df138038062001df183398181016040528101906200003691906200021c565b6200004a5f801b826200005260201b60201c565b50506200024c565b5f6200006583836200014d60201b60201c565b620001435760015f808581526020019081526020015f205f015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550620000df620001b060201b60201c565b73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16847f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a46001905062000147565b5f90505b92915050565b5f805f8481526020019081526020015f205f015f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16905092915050565b5f33905090565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f620001e682620001bb565b9050919050565b620001f881620001da565b811462000203575f80fd5b50565b5f815190506200021681620001ed565b92915050565b5f60208284031215620002345762000233620001b7565b5b5f620002438482850162000206565b91505092915050565b611b97806200025a5f395ff3fe608060405234801561000f575f80fd5b50600436106100fe575f3560e01c80638747d5ed11610095578063c6cbc52a11610064578063c6cbc52a146102ba578063d547741f146102d6578063f333fe08146102f2578063ff34464914610326576100fe565b80638747d5ed1461023457806391d1485414610250578063a217fddf14610280578063af5b22911461029e576100fe565b8063248a9ca3116100d1578063248a9ca31461019c5780632f2ff15d146101cc57806336568abe146101e8578063850c176814610204576100fe565b806301ffc9a7146101025780630331df07146101325780631a8f985b146101625780631b929c211461017e575b5f80fd5b61011c60048036038101906101179190611184565b610342565b60405161012991906111c9565b60405180910390f35b61014c6004803603810190610147919061123c565b6103bb565b60405161015991906111c9565b60405180910390f35b61017c600480360381019061017791906113d6565b6103ed565b005b6101866105ed565b604051610193919061143f565b60405180910390f35b6101b660048036038101906101b19190611458565b610611565b6040516101c3919061143f565b60405180910390f35b6101e660048036038101906101e19190611483565b61062d565b005b61020260048036038101906101fd9190611483565b61064f565b005b61021e60048036038101906102199190611458565b6106ca565b60405161022b91906111c9565b60405180910390f35b61024e6004803603810190610249919061123c565b610819565b005b61026a60048036038101906102659190611483565b610853565b60405161027791906111c9565b60405180910390f35b6102886108b6565b604051610295919061143f565b60405180910390f35b6102b860048036038101906102b3919061123c565b6108bc565b005b6102d460048036038101906102cf9190611458565b6108f6565b005b6102f060048036038101906102eb9190611483565b610ad1565b005b61030c60048036038101906103079190611458565b610af3565b60405161031d959493929190611562565b60405180910390f35b610340600480360381019061033b9190611458565b610c9c565b005b5f7f7965db0b000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614806103b457506103b382610e77565b5b9050919050565b5f6103e67f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d183610853565b9050919050565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d161041781610ee0565b5f801b830361045b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161045290611604565b60405180910390fd5b5f60015f8581526020019081526020015f2090505f8160020154146104b5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104ac9061166c565b60405180910390fd5b6040518060a001604052808581526020013373ffffffffffffffffffffffffffffffffffffffff1681526020014281526020016001151581526020018481525060015f8681526020019081526020015f205f820151815f01556020820151816001015f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550604082015181600201556060820151816003015f6101000a81548160ff02191690831515021790555060808201518160040190816105929190611884565b509050503373ffffffffffffffffffffffffffffffffffffffff16847f49d98094d52ad91874926ffc45cbe9fc4863dfca722a57839a9316014c83481742866040516105df929190611953565b60405180910390a350505050565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d181565b5f805f8381526020019081526020015f20600101549050919050565b61063682610611565b61063f81610ee0565b6106498383610ef4565b50505050565b610657610fdd565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16146106bb576040517f6697b23200000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b6106c58282610fe4565b505050565b5f8060015f8481526020019081526020015f206040518060a00160405290815f8201548152602001600182015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160028201548152602001600382015f9054906101000a900460ff1615151515815260200160048201805461077a906116b7565b80601f01602080910402602001604051908101604052809291908181526020018280546107a6906116b7565b80156107f15780601f106107c8576101008083540402835291602001916107f1565b820191905f5260205f20905b8154815290600101906020018083116107d457829003601f168201915b50505050508152505090505f816040015114158015610811575080606001515b915050919050565b5f801b61082581610ee0565b61084f7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d183610ad1565b5050565b5f805f8481526020019081526020015f205f015f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16905092915050565b5f801b81565b5f801b6108c881610ee0565b6108f27f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d18361062d565b5050565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d161092081610ee0565b5f60015f8481526020019081526020015f2090505f81600201540361097a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610971906119cb565b60405180910390fd5b60011515816003015f9054906101000a900460ff161515146109d1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016109c890611a33565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff16816001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610a62576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a5990611a9b565b60405180910390fd5b5f816003015f6101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff16837f85fe6dd2dfc4757558754564c7ff020182ec3c918257c2ff3a217a63791ec24f42604051610ac49190611ab9565b60405180910390a3505050565b610ada82610611565b610ae381610ee0565b610aed8383610fe4565b50505050565b5f805f8060605f60015f8881526020019081526020015f206040518060a00160405290815f8201548152602001600182015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160028201548152602001600382015f9054906101000a900460ff16151515158152602001600482018054610ba8906116b7565b80601f0160208091040260200160405190810160405280929190818152602001828054610bd4906116b7565b8015610c1f5780601f10610bf657610100808354040283529160200191610c1f565b820191905f5260205f20905b815481529060010190602001808311610c0257829003601f168201915b50505050508152505090505f816040015103610c70576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c67906119cb565b60405180910390fd5b805f01518160200151826040015183606001518460800151955095509550955095505091939590929450565b7f01f18e249044142f95a61b39bad5f99f3c70699c83bb85a1ad87fe9e68bd85d1610cc681610ee0565b5f60015f8481526020019081526020015f2090505f816002015403610d20576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610d17906119cb565b60405180910390fd5b5f1515816003015f9054906101000a900460ff16151514610d76576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610d6d90611b1c565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff16816001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610e07576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610dfe90611a9b565b60405180910390fd5b6001816003015f6101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff16837f8715fb30c14da3439544babc7c09ae9bd692d480791dfb8424a3177e9fc1487842604051610e6a9190611ab9565b60405180910390a3505050565b5f7f01ffc9a7000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149050919050565b610ef181610eec610fdd565b6110cd565b50565b5f610eff8383610853565b610fd35760015f808581526020019081526020015f205f015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550610f70610fdd565b73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16847f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a460019050610fd7565b5f90505b92915050565b5f33905090565b5f610fef8383610853565b156110c3575f805f8581526020019081526020015f205f015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff021916908315150217905550611060610fdd565b73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16847ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b60405160405180910390a4600190506110c7565b5f90505b92915050565b6110d78282610853565b61111a5780826040517fe2517d3f000000000000000000000000000000000000000000000000000000008152600401611111929190611b3a565b60405180910390fd5b5050565b5f604051905090565b5f80fd5b5f80fd5b5f7fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b6111638161112f565b811461116d575f80fd5b50565b5f8135905061117e8161115a565b92915050565b5f6020828403121561119957611198611127565b5b5f6111a684828501611170565b91505092915050565b5f8115159050919050565b6111c3816111af565b82525050565b5f6020820190506111dc5f8301846111ba565b92915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f61120b826111e2565b9050919050565b61121b81611201565b8114611225575f80fd5b50565b5f8135905061123681611212565b92915050565b5f6020828403121561125157611250611127565b5b5f61125e84828501611228565b91505092915050565b5f819050919050565b61127981611267565b8114611283575f80fd5b50565b5f8135905061129481611270565b92915050565b5f80fd5b5f80fd5b5f601f19601f8301169050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b6112e8826112a2565b810181811067ffffffffffffffff82111715611307576113066112b2565b5b80604052505050565b5f61131961111e565b905061132582826112df565b919050565b5f67ffffffffffffffff821115611344576113436112b2565b5b61134d826112a2565b9050602081019050919050565b828183375f83830152505050565b5f61137a6113758461132a565b611310565b9050828152602081018484840111156113965761139561129e565b5b6113a184828561135a565b509392505050565b5f82601f8301126113bd576113bc61129a565b5b81356113cd848260208601611368565b91505092915050565b5f80604083850312156113ec576113eb611127565b5b5f6113f985828601611286565b925050602083013567ffffffffffffffff81111561141a5761141961112b565b5b611426858286016113a9565b9150509250929050565b61143981611267565b82525050565b5f6020820190506114525f830184611430565b92915050565b5f6020828403121561146d5761146c611127565b5b5f61147a84828501611286565b91505092915050565b5f806040838503121561149957611498611127565b5b5f6114a685828601611286565b92505060206114b785828601611228565b9150509250929050565b6114ca81611201565b82525050565b5f819050919050565b6114e2816114d0565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b8381101561151f578082015181840152602081019050611504565b5f8484015250505050565b5f611534826114e8565b61153e81856114f2565b935061154e818560208601611502565b611557816112a2565b840191505092915050565b5f60a0820190506115755f830188611430565b61158260208301876114c1565b61158f60408301866114d9565b61159c60608301856111ba565b81810360808301526115ae818461152a565b90509695505050505050565b7f496e76616c6964206861736800000000000000000000000000000000000000005f82015250565b5f6115ee600c836114f2565b91506115f9826115ba565b602082019050919050565b5f6020820190508181035f83015261161b816115e2565b9050919050565b7f436572746966696361746520616c7265616479206578697374730000000000005f82015250565b5f611656601a836114f2565b915061166182611622565b602082019050919050565b5f6020820190508181035f8301526116838161164a565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f60028204905060018216806116ce57607f821691505b6020821081036116e1576116e061168a565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026117437fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611708565b61174d8683611708565b95508019841693508086168417925050509392505050565b5f819050919050565b5f61178861178361177e846114d0565b611765565b6114d0565b9050919050565b5f819050919050565b6117a18361176e565b6117b56117ad8261178f565b848454611714565b825550505050565b5f90565b6117c96117bd565b6117d4818484611798565b505050565b5b818110156117f7576117ec5f826117c1565b6001810190506117da565b5050565b601f82111561183c5761180d816116e7565b611816846116f9565b81016020851015611825578190505b611839611831856116f9565b8301826117d9565b50505b505050565b5f82821c905092915050565b5f61185c5f1984600802611841565b1980831691505092915050565b5f611874838361184d565b9150826002028217905092915050565b61188d826114e8565b67ffffffffffffffff8111156118a6576118a56112b2565b5b6118b082546116b7565b6118bb8282856117fb565b5f60209050601f8311600181146118ec575f84156118da578287015190505b6118e48582611869565b86555061194b565b601f1984166118fa866116e7565b5f5b82811015611921578489015182556001820191506020850194506020810190506118fc565b8683101561193e578489015161193a601f89168261184d565b8355505b6001600288020188555050505b505050505050565b5f6040820190506119665f8301856114d9565b8181036020830152611978818461152a565b90509392505050565b7f436572746966696361746520646f6573206e6f742065786973740000000000005f82015250565b5f6119b5601a836114f2565b91506119c082611981565b602082019050919050565b5f6020820190508181035f8301526119e2816119a9565b9050919050565b7f416c7265616479207265766f6b656400000000000000000000000000000000005f82015250565b5f611a1d600f836114f2565b9150611a28826119e9565b602082019050919050565b5f6020820190508181035f830152611a4a81611a11565b9050919050565b7f4e6f74206f776e657200000000000000000000000000000000000000000000005f82015250565b5f611a856009836114f2565b9150611a9082611a51565b602082019050919050565b5f6020820190508181035f830152611ab281611a79565b9050919050565b5f602082019050611acc5f8301846114d9565b92915050565b7f416c7265616479206163746976650000000000000000000000000000000000005f82015250565b5f611b06600e836114f2565b9150611b1182611ad2565b602082019050919050565b5f6020820190508181035f830152611b3381611afa565b9050919050565b5f604082019050611b4d5f8301856114c1565b611b5a6020830184611430565b939250505056fea2646970667358221220043c25005c903d80e0487781652806d6b653254e34d02d0529b4bb44c6d0366b64736f6c63430008140033";

    private static String librariesLinkedBinary;

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_INSTITUTION_ROLE = "INSTITUTION_ROLE";

    public static final String FUNC_ADDINSTITUTION = "addInstitution";

    public static final String FUNC_GETCERTIFICATE = "getCertificate";

    public static final String FUNC_GETROLEADMIN = "getRoleAdmin";

    public static final String FUNC_GRANTROLE = "grantRole";

    public static final String FUNC_HASROLE = "hasRole";

    public static final String FUNC_ISINSTITUTION = "isInstitution";

    public static final String FUNC_REACTIVATECERTIFICATE = "reactivateCertificate";

    public static final String FUNC_REGISTERCERTIFICATE = "registerCertificate";

    public static final String FUNC_REMOVEINSTITUTION = "removeInstitution";

    public static final String FUNC_RENOUNCEROLE = "renounceRole";

    public static final String FUNC_REVOKECERTIFICATE = "revokeCertificate";

    public static final String FUNC_REVOKEROLE = "revokeRole";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_VERIFYCERTIFICATE = "verifyCertificate";

    public static final Event CERTIFICATEREACTIVATED_EVENT = new Event("CertificateReactivated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CERTIFICATEREGISTERED_EVENT = new Event("CertificateRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event CERTIFICATEREVOKED_EVENT = new Event("CertificateRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CERTIFICATEVERIFIED_EVENT = new Event("CertificateVerified", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
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
    protected CertificateRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CertificateRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CertificateRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CertificateRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CertificateReactivatedEventResponse> getCertificateReactivatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CERTIFICATEREACTIVATED_EVENT, transactionReceipt);
        ArrayList<CertificateReactivatedEventResponse> responses = new ArrayList<CertificateReactivatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CertificateReactivatedEventResponse typedResponse = new CertificateReactivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CertificateReactivatedEventResponse getCertificateReactivatedEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CERTIFICATEREACTIVATED_EVENT, log);
        CertificateReactivatedEventResponse typedResponse = new CertificateReactivatedEventResponse();
        typedResponse.log = log;
        typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<CertificateReactivatedEventResponse> certificateReactivatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCertificateReactivatedEventFromLog(log));
    }

    public Flowable<CertificateReactivatedEventResponse> certificateReactivatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CERTIFICATEREACTIVATED_EVENT));
        return certificateReactivatedEventFlowable(filter);
    }

    public static List<CertificateRegisteredEventResponse> getCertificateRegisteredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CERTIFICATEREGISTERED_EVENT, transactionReceipt);
        ArrayList<CertificateRegisteredEventResponse> responses = new ArrayList<CertificateRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CertificateRegisteredEventResponse typedResponse = new CertificateRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.metadataURI = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CertificateRegisteredEventResponse getCertificateRegisteredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CERTIFICATEREGISTERED_EVENT, log);
        CertificateRegisteredEventResponse typedResponse = new CertificateRegisteredEventResponse();
        typedResponse.log = log;
        typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.metadataURI = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<CertificateRegisteredEventResponse> certificateRegisteredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCertificateRegisteredEventFromLog(log));
    }

    public Flowable<CertificateRegisteredEventResponse> certificateRegisteredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CERTIFICATEREGISTERED_EVENT));
        return certificateRegisteredEventFlowable(filter);
    }

    public static List<CertificateRevokedEventResponse> getCertificateRevokedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CERTIFICATEREVOKED_EVENT, transactionReceipt);
        ArrayList<CertificateRevokedEventResponse> responses = new ArrayList<CertificateRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CertificateRevokedEventResponse typedResponse = new CertificateRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CertificateRevokedEventResponse getCertificateRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CERTIFICATEREVOKED_EVENT, log);
        CertificateRevokedEventResponse typedResponse = new CertificateRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.institution = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<CertificateRevokedEventResponse> certificateRevokedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCertificateRevokedEventFromLog(log));
    }

    public Flowable<CertificateRevokedEventResponse> certificateRevokedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CERTIFICATEREVOKED_EVENT));
        return certificateRevokedEventFlowable(filter);
    }

    public static List<CertificateVerifiedEventResponse> getCertificateVerifiedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CERTIFICATEVERIFIED_EVENT, transactionReceipt);
        ArrayList<CertificateVerifiedEventResponse> responses = new ArrayList<CertificateVerifiedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CertificateVerifiedEventResponse typedResponse = new CertificateVerifiedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.verifier = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.valid = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CertificateVerifiedEventResponse getCertificateVerifiedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CERTIFICATEVERIFIED_EVENT, log);
        CertificateVerifiedEventResponse typedResponse = new CertificateVerifiedEventResponse();
        typedResponse.log = log;
        typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.verifier = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.valid = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<CertificateVerifiedEventResponse> certificateVerifiedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCertificateVerifiedEventFromLog(log));
    }

    public Flowable<CertificateVerifiedEventResponse> certificateVerifiedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CERTIFICATEVERIFIED_EVENT));
        return certificateVerifiedEventFlowable(filter);
    }

    public static List<RoleAdminChangedEventResponse> getRoleAdminChangedEvents(
            TransactionReceipt transactionReceipt) {
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

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEADMINCHANGED_EVENT));
        return roleAdminChangedEventFlowable(filter);
    }

    public static List<RoleGrantedEventResponse> getRoleGrantedEvents(
            TransactionReceipt transactionReceipt) {
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

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEGRANTED_EVENT));
        return roleGrantedEventFlowable(filter);
    }

    public static List<RoleRevokedEventResponse> getRoleRevokedEvents(
            TransactionReceipt transactionReceipt) {
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

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
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

    public RemoteFunctionCall<Tuple5<byte[], String, BigInteger, Boolean, String>> getCertificate(
            byte[] hash) {
        final Function function = new Function(FUNC_GETCERTIFICATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple5<byte[], String, BigInteger, Boolean, String>>(function,
                new Callable<Tuple5<byte[], String, BigInteger, Boolean, String>>() {
                    @Override
                    public Tuple5<byte[], String, BigInteger, Boolean, String> call() throws
                            Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<byte[], String, BigInteger, Boolean, String>(
                                (byte[]) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (String) results.get(4).getValue());
                    }
                });
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

    public RemoteFunctionCall<TransactionReceipt> reactivateCertificate(byte[] hash) {
        final Function function = new Function(
                FUNC_REACTIVATECERTIFICATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> registerCertificate(byte[] hash,
            String metadataURI) {
        final Function function = new Function(
                FUNC_REGISTERCERTIFICATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash), 
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

    public RemoteFunctionCall<TransactionReceipt> renounceRole(byte[] role,
            String callerConfirmation) {
        final Function function = new Function(
                FUNC_RENOUNCEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, callerConfirmation)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeCertificate(byte[] hash) {
        final Function function = new Function(
                FUNC_REVOKECERTIFICATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)), 
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

    public RemoteFunctionCall<Boolean> verifyCertificate(byte[] hash) {
        final Function function = new Function(FUNC_VERIFYCERTIFICATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static CertificateRegistry load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CertificateRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CertificateRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CertificateRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CertificateRegistry load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CertificateRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CertificateRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CertificateRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CertificateRegistry> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(CertificateRegistry.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<CertificateRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(CertificateRegistry.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CertificateRegistry> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(CertificateRegistry.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CertificateRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)));
        return deployRemoteCall(CertificateRegistry.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class CertificateReactivatedEventResponse extends BaseEventResponse {
        public byte[] hash;

        public String institution;

        public BigInteger timestamp;
    }

    public static class CertificateRegisteredEventResponse extends BaseEventResponse {
        public byte[] hash;

        public String institution;

        public BigInteger timestamp;

        public String metadataURI;
    }

    public static class CertificateRevokedEventResponse extends BaseEventResponse {
        public byte[] hash;

        public String institution;

        public BigInteger timestamp;
    }

    public static class CertificateVerifiedEventResponse extends BaseEventResponse {
        public byte[] hash;

        public String verifier;

        public BigInteger timestamp;

        public Boolean valid;
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
