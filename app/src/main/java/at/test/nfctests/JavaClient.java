package at.test.nfctests;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by mschaefer on 24/07/15.
 */
public class JavaClient {
//TODO: Define Communication Protocols

    /**
     * This Method sends a message to the client.
     * @param msg The message to be sent
     */
    public void sendMessage(String msg) {
        //TODO: Send Message
    }

    /**
     * This Method sends an exception to the client.
     * @param msg The message to be sent
     * @param e Optional - An error to be sent.
     */
    public void sendError(String msg, Exception e) {
        //TODO: Send Exception
    }

    /**
     * This Method sends an APDU request to the client.
     * @param msg A message to be sent with the request e.g. request type identifiers.
     * @param request The request to be sent.
     * @return The response from the client.
     */
    public byte[] sendAPDURequest(String msg, byte[] request) {
        //TODO: Send request and return response
        return null;
    }

    /**
     * This Method sends given user credentials to the server.
     * @param user The username to be sent.
     * @param pass The password to be sent.
     * @return true if the login was successful, false otherwise.
     */
    public boolean login(String user, String pass) {
        //TODO: Send Login credentials and return true if it worked and false otherwise
        return user.equals("a");
    }

    /**
     * This Method generates a sha256 hash of the given String
     * @param base A String value to generate the has from.
     * @return The generated sha256 hash. null if an exception occurred.
     */
    public String generateSHA256Hash(String base) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(base.getBytes());
            byte[] digest = sha.digest();
            return new String(digest);
        } catch (Exception e) {
            Log.e("NFC-CLIENT", "Could not generate sha256 hash!", e);
        }
        return null;
    }

    public void logout() {
        //TODO: Send logout request to server
    }
}
