package assets.chaincode.EERC721;

import assets.chaincode.AddressUtils;
import assets.chaincode.ERC721.ERC721;
import assets.config.UserConfig;
import assets.client.ChannelClient;
import assets.client.FabricClient;
import assets.config.Config;
import assets.user.UserContext;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.identity.X509Identity;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EERC721 {


    public String register(String tokenId, String type, String owner, int pages, String hash, String signers, String path, String pathHash) {

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
            String[] arguments = { tokenId, type, addr, Integer.toString(pages), hash, signers, path, pathHash};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

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

    public String balanceOf(String owner, String type) {

        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "balanceOf", new String[]{addr, type});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(EERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String deactivate(String tokenId) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            
            request.setFcn("deactivate");
            String[] arguments = { tokenId };
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"deactivated on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String divide(String tokenId, String[] newIds, String[] values, int index) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("divide");
            String[] arguments = { tokenId,  Arrays.toString(newIds), Arrays.toString(values), Integer.toString(index) };
            
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"divided on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String update(String tokenId, String index, String attr) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("setXAttr");
            String[] arguments = { tokenId, index, attr };
            
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"update on "+Config.CHAINCODE_1_NAME + ". STATUS - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    public String query(String tokenId) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "query", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(EERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String queryHistory(String tokenId, String owner) {

        String result = "";
        try {
            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "queryHistory", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(EERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
}
