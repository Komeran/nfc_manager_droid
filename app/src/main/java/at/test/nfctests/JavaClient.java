package at.test.nfctests;

import android.app.AlertDialog;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.MessageDigest;

/**
 * Created by mschaefer on 24/07/15.
 */
public class JavaClient {
    /**
     * This Method sends a message to the client.
     * @param msg The message to be sent
     */
    private void sendMessage(String msg) {
        //TODO: Send the Request/Response
    }

    public boolean sendOpenSessionRequest(String user, String pass, @Nullable String fault) {
        //TODO: Send OpenSessionRequest
        return user.equals("a");
    }

    public void sendCloseSessionRequest(@Nullable String fault) {
        //TODO: Send CloseSessionRequest
    }

    public void sendClientIdleEvent(@Nullable String fault) {
        //TODO: Send ClientIdleEvent
    }

    public void sendNFCDetectEvent(AlertDialog readerDialog, Tag tag, @Nullable String fault) {
        //TODO: Send NFCDetectEvent and communicate with tag

        //Placeholder procedure
        boolean stop = false;
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();
        }
        catch (Exception e) {
            stop = true;
        }

        while(!stop) {
            try {
                byte[] APDUCommand = {
                        (byte) 0x00, // CLA Class
                        (byte) 0xA4, // INS Instruction
                        (byte) 0x04, // P1  Parameter 1
                        (byte) 0x00, // P2  Parameter 2
                        (byte) 0x0A, // Length
                        0x63,0x64,0x63,0x00,0x00,0x00,0x00,0x32,0x32,0x31 // AID
                };
                isoDep.transceive(APDUCommand);
            } catch (Exception e) {
                stop = true;
            }
        }
        final AlertDialog readerDialogFinal = readerDialog;
        final ImageView image = (ImageView) readerDialog.findViewById(R.id.dialog_reader_image);
        final TextView text = (TextView) readerDialog.findViewById(R.id.dialog_reader_text);

        Log.w("NFC-CLIENT", "Tag disconnected!");

        image.post(new Runnable() {
            @Override
            public void run() {
                image.setBackground(readerDialogFinal.getContext().getDrawable(R.drawable.nfc_logo_blue));
                text.setText(R.string.dialog_reader_message);
            }
        });
    }

    public void sendAPDUExchangeRequest(byte[] apdu, @Nullable String fault) {
        //TODO: Send APDUExchangeRequest
    }

    public void sendSetNFCModeResponse(String requestId, @Nullable String fault) {
        //TODO: Send SetNFCModeResponse
    }

    public void sendAPDUExchangeRequestAck(String requestId, @Nullable String fault) {
        //TODO: Send APDUExchangeRequestAck
    }

    public void sendAPDUExchangeResponseAck(String requestId, @Nullable String fault) {

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
}
