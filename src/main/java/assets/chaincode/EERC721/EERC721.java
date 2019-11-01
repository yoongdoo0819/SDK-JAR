package assets.chaincode.EERC721;

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
//import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EERC721 {
/*
    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private static final String EXPECTED_EVENT_NAME = "event";

    String certificate;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String mint(String tokenId, String type, String owner, String hash, String signers, String path, String pathHash) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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
            request.setFcn("mint");
            String[] arguments = { tokenId, type, owner, hash, signers, path, pathHash};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"mint on "+Config.CHAINCODE_NAME + ". Status - " + status + " result - " + res.getMessage());
                result = res.getMessage();
                //result = (String)res.getStatus();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Failure";
        }

        return result;
    }

    public String balanceOf(String owner, String type) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "balanceOf", new String[]{owner, type});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String deactivate(String tokenId, String owner) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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
            request.setFcn("deactivate");
            String[] arguments = { tokenId };

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"deactivated on "+Config.CHAINCODE_NAME + ". Status - " + status);

                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String divide(String tokenId, String newId, String owner) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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
            request.setFcn("divide");
            String[] arguments = { tokenId, newId };
            //request.setFcn("createCar");
            //String[] arguments = { "CAR1", "Chevy", "Volt", "Red", "Nick" };
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"divided on "+Config.CHAINCODE_NAME + ". Status - " + status);
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String update(String tokenId, String index, String attr, String owner) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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
            request.setFcn("setXAttr");
            String[] arguments = { tokenId, index, attr };
            //request.setFcn("createCar");
            //String[] arguments = { "CAR1", "Chevy", "Volt", "Red", "Nick" };
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"divided on "+Config.CHAINCODE_NAME + ". Status - " + status);
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String query(String tokenId, String owner) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "query", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse2 = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());

                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String queryHistory(String tokenId, String owner) {

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

            userContext = caClient.enrollUser(userContext, certificate);

            FabricClient fabClient = new FabricClient(userContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();

            for(int i=0; i<Config.ORG_PEER.length; i++) {
                Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER[i], Config.ORG_PEER_URL[i]);
                channel.addPeer(peer);
            }

            for(int i=0; i<Config.ORDERER_NAME.length; i++) {
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

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "queryHistory", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse2 = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());

                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

*/
    public static void main(String args[]) {

    }
}