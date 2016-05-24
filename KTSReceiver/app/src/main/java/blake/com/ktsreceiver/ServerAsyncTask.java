package blake.com.ktsreceiver;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raiders on 5/24/16.
 */
public class ServerAsyncTask extends AsyncTask<Void, Void, String> {

    private TextView name;
    private TextView birthday;
    private TextView age;

    public ServerAsyncTask(View name, View birthday, View age) {
        this.name = (TextView) name;
        this.birthday = (TextView) birthday;
        this.age = (TextView) age;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a string
             */

            final String personalInfo = Environment.getExternalStorageState();

            serverSocket.close();
            return personalInfo;
        } catch (IOException e) {
            Log.e(MainActivity.TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Start activity that can handle the JPEG image
     */
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            String[] data = result.split("/");

            name.setText(data[0]);
            birthday.setText(data[1] + "/" + data[2] + "/" + data[3]);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            String[] todaysDate = currentDate.split("-");
            if (Integer.valueOf(data[1]) < Integer.valueOf(todaysDate[1])) {
                age.setText(Integer.valueOf(todaysDate[0])-Integer.valueOf(data[3]) + "");
            }
            else if (Integer.valueOf(data[1]) > Integer.valueOf(todaysDate[1])) {
                age.setText(Integer.valueOf(todaysDate[0])-Integer.valueOf(data[3]) - 1 + "");
            }
            else {
                if (Integer.valueOf(data[2]) <= Integer.valueOf(todaysDate[2])) {
                    age.setText(Integer.valueOf(todaysDate[0])-Integer.valueOf(data[3]) + "");
                }
                else {
                    age.setText(Integer.valueOf(todaysDate[0])-Integer.valueOf(data[3]) - 1 + "");
                }
            }
        }
    }
}
