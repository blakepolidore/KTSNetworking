package blake.com.ktssender;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

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
        String userInfoString = nameEditText.getText().toString() + "/" + getSpinnerSelections(monthSpinner)
                + "/" + getSpinnerSelections(daySpinner) + "/" + getSpinnerSelections(yearSpinner);
        ClientAsyncTask clientAsyncTask = new ClientAsyncTask();
        clientAsyncTask.execute(userInfoString);
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

    private void transferString(String data) {
        Context context = this.getApplicationContext();
        String host = getLocalIpAddress();
        //String host = "10.0.3.2";
        int port = 8888;
        int len;
        Socket socket = new Socket();
        byte buf[]  = new byte[1024];
        try {
            /**
             * Create a client socket with the host,
             * port, and timeout information.
             */

            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 500);
            Log.d(TAG, data);
            /**
             * Create a byte stream from a string and pipe it to the output stream
             * of the socket. This data will be retrieved by the server device.
             */
            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            inputStream = cr.openInputStream(Uri.parse(data));
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            Log.d("FileNotFoundException", e.toString());
        } catch (IOException e) {
            Log.d("IOException", e.toString());
        }

    /**
    * Clean up any open sockets when done
    * transferring or if an exception occurred.
    */
        finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }
    }

    private class ClientAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            transferString(params[0]);
            return null;
        }
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {}
        return null;
    }
}
