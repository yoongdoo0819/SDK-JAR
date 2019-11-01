/****************************************************** 
 *  Copyright 2018 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */
package assets.chaincode;

import assets.client.CAClient;
import assets.client.ChannelClient;
import assets.client.FabricClient;
import assets.config.Config;
import assets.user.UserContext;
import assets.util.Util;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class InvokeChaincode {

	private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
	private static final String EXPECTED_EVENT_NAME = "event";

	public static boolean initToken() {
		try {
			String result="";
			Util.cleanUp();
			String caUrl = Config.CA_ORG_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG);
			adminUserContext.setMspId(Config.ORG_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

			FabricClient fabClient = new FabricClient(adminUserContext);

			ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();

			//for(int i=0; i<Config.ORG_PEER.length; i++) {
				Peer peer = fabClient.getInstance().newPeer(Config.ORG_PEER, Config.ORG_PEER_URL);
				channel.addPeer(peer);
			//}

			//for(int i=0; i<Config.ORDERER_NAME.length; i++) {
				Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
				channel.addOrderer(orderer);
			//}

			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
			channel.addEventHub(eventHub);

			//channel.addPeer(peer);
			channel.addEventHub(eventHub);
			//channel.addOrderer(orderer);
			channel.initialize();

			TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
			ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_NAME).build();
			request.setChaincodeID(ccid);
			request.setFcn("initToken");
			String[] arguments = { "token", "woori", "100", "sangwon", "sangwon", "sig" };

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
				Status status = res.getStatus();
				Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"Updated on "+Config.CHAINCODE_NAME + ". Status - " + status);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String args[]) {

	}

}
