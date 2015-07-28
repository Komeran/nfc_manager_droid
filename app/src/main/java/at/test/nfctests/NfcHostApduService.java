package at.test.nfctests;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

/**
 * Created by mschaefer on 24/07/15.
 */
public class NfcHostApduService extends HostApduService {
    JavaClient client;

    public NfcHostApduService(JavaClient client) {
        this.client = client;
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        return client.sendAPDURequest("There's an APDU request", commandApdu);
    }

    @Override
    public void onDeactivated(int reason) {

    }
}
