package at.test.nfctests;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.nfc.tech.*;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.charset.Charset;


public class NfcMainApp extends ActionBarActivity {

    private JavaClient client;
    private boolean isInReaderMode;
    private NfcAdapter nfcAdapter;
    private Tag currentTag;
    private Menu menu;
    private AlertDialog readerDialog;

    /**
     * This method sends an APDU request to the current NFC tag if possible.
     * @param request The raw request to be sent to the NFC tag.
     * @return The raw response from the NFC tag. null if anything went wrong.
     */
    private byte[] sendAPDUToTag(byte[] request) {
        IsoDep isoDep = IsoDep.get(currentTag);

        byte[] response = null;

        try {
            isoDep.connect();
        } catch (Exception e) {
            //TODO: Define message for "Error connecting to NFC Tag"
            client.sendError("Error connecting to NFC Tag", e);
            Log.e("NFC-READER", "There was a problem connecting to the NFC Tag!", e);
        } finally {
            try {
                response = isoDep.transceive(request);
            } catch (Exception e) {
                //TODO: Define message for "Error connecting to NFC Tag"
                client.sendError("Error sending request to NFC Tag", e);
                Log.e("NFC-READER", "There was a problem sending the request to the NFC Tag!", e);
            }
            try {
                isoDep.close();
            } catch (Exception e) {
                //TODO: Define message for "Error disconnecting from the NFC Tag"
                client.sendError("Error disconnecting from the NFC Tag", e);
                Log.e("NFC-READER", "There was a problem disconnecting from the NFC Tag!", e);
            }
        }

        return response;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i("NFC-APP", "NFC-App successfully started.");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        client = new JavaClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_logout:
                return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    /**
     * This Method starts/stops the NFC reader mode of the device.
     */
    public void readerMode(final String requestId) {
        TextView statusView = (TextView) findViewById(R.id.status_status);

        if(!isInReaderMode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(getLayoutInflater().inflate(R.layout.dialog_reader, null));
            readerDialog = builder.create();
            readerDialog.show();

            nfcAdapter.enableReaderMode(this, new ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    Log.w("NFC_ADAPTER", "A Tag was detected!");

                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        Log.e("NFC-APP", "Error playing notification sound:", e);
                    }

                    Log.w("NFC-ADAPTER", "Tag Id: " + new String(tag.getId(), Charset.forName("US-ASCII")));

                    currentTag = tag;

                    final ImageView image = (ImageView) readerDialog.findViewById(R.id.dialog_reader_image);
                    final TextView text = (TextView) readerDialog.findViewById(R.id.dialog_reader_text);

                    image.post(new Runnable() {
                        @Override
                        public void run() {
                            image.setBackground(getDrawable(R.drawable.ok));
                            text.setText("Found NFC Tag!\nKeep it attached!");
                        }
                    });

                    client.sendNFCDetectEvent(readerDialog, tag, null);
                }
            }, 1, null);
            client.sendSetNFCModeResponse(requestId, null);
            Log.i("NFC-APP", "Reader mode was started!");

            statusView.setText("reader mode");
            isInReaderMode=true;
        }
        else {
            nfcAdapter.disableReaderMode(this);
            readerDialog.hide();
            Log.i("NFC-App", "Reader mode was ended!");

            statusView.setText("default mode");
            isInReaderMode=false;
            readerDialog.dismiss();
        }
    }

    public void login(View view) {
        Button button = (Button) view;
        String user = ((TextView)findViewById(R.id.login_user)).getText().toString();
        String pass = ((TextView)findViewById(R.id.login_pass)).getText().toString();
        TextView loginNotification = (TextView) findViewById(R.id.login_notification);

        boolean worked = client.sendOpenSessionRequest(user, pass, null);

        if(worked) {
            setContentView(R.layout.activity_status);
            loginNotification.setText("");
            getMenuInflater().inflate(R.menu.menu_status, menu);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            readerMode(null);
        }
        else {
            loginNotification.setTextColor(Color.RED);
            loginNotification.setText("Login failed");
        }
    }

    public void logout(MenuItem item) {
        setContentView(R.layout.activity_login);
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TextView loginNotification = (TextView) findViewById(R.id.login_notification);

        client.sendCloseSessionRequest(null);
        if(isInReaderMode) {
            readerMode(null);
        }
        loginNotification.setTextColor(Color.GREEN);
        loginNotification.setText("Successfully logged out!");
    }
}
