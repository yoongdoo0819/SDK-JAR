package assets.chaincode;

import assets.config.Config;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

public class EnrollmentUser {

    X509Identity identity = null;
    String userID = null;

    AddressUtils addressUtils = new AddressUtils();


    public void enrollAdmin() throws Exception {

        // if url starts as https.., need to set SSL
        HFCAClient caClient = HFCAClient.createNewInstance(Config.CA_ORG1_URL, null/*props*/);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        if(wallet == null)
            System.out.println("wallet fail");
        else
            System.out.println(wallet.toString()+"------------------------------");
        // Check to see if we've already enrolled the admin user.
        boolean adminExists = wallet.exists("admin");
        if (adminExists) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll(Config.ADMIN, Config.ADMIN_PASSWORD, enrollmentRequestTLS);
        Identity user = Identity.createIdentity("Org1MSP", enrollment.getCert(), enrollment.getKey());
        wallet.put("admin", user);
        System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
    }

    public Enrollment registerUser(String _userID) throws Exception {

        this.userID = _userID;

        // if url starts as https.., need to set SSL
        HFCAClient caClient = HFCAClient.createNewInstance(Config.CA_ORG1_URL, null/*props*/);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the user.
        boolean userExists = wallet.exists(this.userID);
        if (userExists) {
            System.out.println("An identity for the user " + this.userID + " already exists in the wallet");
            return null;
        }

        userExists = wallet.exists("admin");
        if (!userExists) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return null;
        }

        Identity adminIdentity = wallet.get("admin");
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return adminIdentity.getCertificate();
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }
        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(this.userID);
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID(this.userID);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(this.userID, enrollmentSecret);
        Identity user = Identity.createIdentity("Org1MSP", enrollment.getCert(), enrollment.getKey());
        wallet.put(this.userID, user);
        System.out.println("Successfully enrolled user " + this.userID + " and imported it into the wallet");

        return enrollment;
    }

}
