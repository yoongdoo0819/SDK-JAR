package assets.chaincode.ERC721;

import assets.chaincode.EERC721.EERC721;
import assets.chaincode.InvokeChaincode;
import assets.chaincode.QueryChaincode;
import assets.client.CAClient;
import assets.client.ChannelClient;
import assets.client.FabricClient;
import assets.config.Config;
import assets.user.UserContext;
import assets.util.Util;
import assets.service.RedisService;
import org.hyperledger.fabric.sdk.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

//@Component
public class ERC721 {

    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private static final String EXPECTED_EVENT_NAME = "event";

    //@Autowired
    //RedisService redisService;
    String certificate;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String mint(String tokenId, String owner) {

        String name = owner;
        String result = "";
        try {

            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(name);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            // for (int i = 0; i < Config.ORG_PEER; i++) {
            Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER, Config.ORG_PEER_URL);
            channel.addPeer(peer);
            //  }
/*
            // for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
            Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            channel.addOrderer(orderer);
            //  }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            //channel.addOrderer(orderer);
            channel.initialize();
            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("mint");
            String[] arguments = {tokenId, owner};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res : responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO, "mint on " + Config.CHAINCODE_NAME + ". Status - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
                //result = (String)res.getStatus();

            }*/
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "abc";
    }

    public static void main(String args[]) {
        //ERC721 erc721 = new ERC721();
        //erc721.mint("0", "test1");
    }
}
/*
    public String balanceOf(String owner) {

        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(owner);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = owner;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "balanceOf", new String[]{owner});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }



        return result;
    }

    public String ownerOf(String tokenId, String owner) {

        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(owner);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = owner;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            RedisService redisService = new RedisService();
            String certificate = redisService.getCertificate(owner);
            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_NAME, "ownerOf", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String approve(String approved, String tokenId, String owner) {
        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(owner);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = owner;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            RedisService redisService = new RedisService();
            String certificate = redisService.getCertificate(owner);
            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("approve");
            String[] arguments = {approved, tokenId};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res : responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO, "approve on " + Config.CHAINCODE_NAME + ". Status - " + status);
                result = res.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public String getApproved(String tokenId, String owner) {

        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(owner);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = owner;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            RedisService redisService = new RedisService();
            String certificate = redisService.getCertificate(owner);
            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            Thread.sleep(1000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_NAME, "getApproved", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                //result = stringResponse;
                result = pres.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String setApprovedForAll(String caller, String operator, String approved) {
        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(caller);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = caller;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            RedisService redisService = new RedisService();
            String certificate = redisService.getCertificate(caller);
            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("setApprovalForAll");
            String[] arguments = {caller, operator, approved};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res : responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO, "setApprovalForAll on " + Config.CHAINCODE_NAME + ". Status - " + status);
                result = res.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public String isApprovedForAll(String owner, String operator) {
        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to OrMSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(owner);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = owner;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            RedisService redisService = new RedisService();
            String certificate = redisService.getCertificate(owner);
            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "isApprovedForAll", new String[]{owner, operator});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public String transferToken(String owner, String receiver, String tokenId) {
        String result = "";
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to OrGMSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(owner);
            adminUserContext.setAffiliation(Config.ORG);
            adminUserContext.setMspId(Config.ORG_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register user
            UserContext userContext = new UserContext();
            String name = owner;
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG);
            userContext.setMspId(Config.ORG_MSP);

            RedisService redisService = new RedisService();
            String certificate = redisService.getCertificate(owner);
            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for (int i = 0; i < Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for (int i = 0; i < Config.ORDERER_NAME.length; i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME[i], Config.ORDERER_URL[i]);
                channel.addOrderer(orderer);
            }

            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            channel.addEventHub(eventHub);

            //channel.addPeer(peer);
            channel.addEventHub(eventHub);
            //channel.addOrderer(orderer);
            channel.initialize();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("transferFrom");
            String[] arguments = {owner, receiver, tokenId};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res : responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO, "transferFrom on " + Config.CHAINCODE_NAME + ". Status - " + status);
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }
*/
