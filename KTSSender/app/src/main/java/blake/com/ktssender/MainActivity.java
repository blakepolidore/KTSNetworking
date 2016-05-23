package blake.com.ktssender;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to send personal information to receiver application
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main activity";

    private EditText nameEditText;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private Spinner yearSpinner;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setSpinners();
        setClickListener();
    }

    /**
     * Sets views
     */
    private void setViews() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        monthSpinner = (Spinner) findViewById(R.id.month_spinner);
        daySpinner = (Spinner) findViewById(R.id.day_spinner);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        submitButton = (Button) findViewById(R.id.send_button);
    }

    /**
     * Fills the spinners with correct information and puts them in the adapters
     */
    private void setSpinners() {
        ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapterDay);
        monthSpinner.setAdapter(adapterMonth);
        yearSpinner.setAdapter(adapterYear);
    }

    /**
     * Grabs selected spinner data
     * @param spinner
     * @return
     */
    private String getSpinnerSelections(Spinner spinner) {
        TextView textView = (TextView) spinner.getSelectedView();
        String spinnerText = textView.getText().toString();
        return spinnerText;
    }

    /**
     * Method that sends the data as a string to the receiver activity
     */
    private void sendData() {
        String intentString = nameEditText.getText().toString() + "/" + getSpinnerSelections(monthSpinner)
                + "/" + getSpinnerSelections(daySpinner) + "/" + getSpinnerSelections(yearSpinner);
        startRegistration(intentString);

//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, intentString);
//        shareIntent.setType("text/plain");
//        startActivity(Intent.createChooser(shareIntent, "Share info to.."));
    }

    /**
     * Sets the button to send the data to next application but only if edit text has been filled
     */
    private void setClickListener() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameEditText.getText().toString().isEmpty()) {
                    sendData();
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_enter_name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startRegistration(String data) {
        //  Create a string map containing information about your service.
        Map record = new HashMap();
        record.put("personalInfo", data);

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.

        WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel;
        channel = manager.initialize(this, getMainLooper(), null);
        manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "It worked");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d(TAG, "It failed");
            }
        });
    }

}
