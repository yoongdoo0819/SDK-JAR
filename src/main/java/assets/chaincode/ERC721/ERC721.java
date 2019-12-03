package assets.chaincode.ERC721;

import assets.chaincode.AddressUtils;
import assets.config.UserConfig;
import assets.client.ChannelClient;
import assets.client.FabricClient;
import assets.config.Config;
import assets.user.UserContext;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.identity.X509Identity;


import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ERC721 {


    public String register(String tokenId, String owner) {

        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("mint");
            String[] arguments = { tokenId, addr};

            request.setArgs(arguments);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"mint on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String balanceOf(String owner) {

        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "balanceOf", new String[]{addr});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String ownerOf(String tokenId) {

        String result = "";
        try {

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "ownerOf", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String approve(String approved, String tokenId) {
        String result = "";
        try {


            UserConfig.initUserContextForOwner();

            UserContext userContext = UserConfig.initUserContextForApproved();
            X509Identity identity = new X509Identity(userContext);
            String addrApproved = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("approve");
            String[] arguments = { addrApproved, tokenId };

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"approve on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getApproved(String tokenId) {

        String result="";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "getApproved", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String setApprovalForAll(String owner, String operator, String approved) {
        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            UserContext userContext = UserConfig.initUserContextForOperator();
            X509Identity identity = new X509Identity(userContext);
            String addrOperator = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("setApprovalForAll");
            String[] arguments = { addrOperator , approved};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"setApprovalForAll on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String isApprovedForAll(String owner, String operator) {
        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            userContext = UserConfig.initUserContextForOperator();
            identity = new X509Identity(userContext);
            String addrOperator = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "isApprovedForAll", new String[]{addr, addrOperator});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String transfer(String owner, String receiver, String tokenId) {
        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            userContext = UserConfig.initUserContextForNewOwner();
            identity = new X509Identity(userContext);
            String newOwnerAddr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("transferFrom");
            String[] arguments = { addr, newOwnerAddr , tokenId};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"transfer on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) {

    }
}
