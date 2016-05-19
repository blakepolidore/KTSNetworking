package blake.com.ktsreceiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that receives the intent from the sender activity
 */
public class MainActivity extends AppCompatActivity {

    private TextView name;
    private TextView birthday;
    private TextView age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        getDataFromIntent();
    }

    /**
     * Sets activity views
     */
    private void setViews() {
        name = (TextView) findViewById(R.id.name_text);
        birthday = (TextView) findViewById(R.id.birthday_text);
        age = (TextView) findViewById(R.id.age_text);
    }

    /**
     * Grabs data from intent sent from sender application
     */
    private void getDataFromIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleIntent(intent);
            }
        }
    }

    /**
     * Handles the intent and puts the information in the correct text views
     * @param intent
     */
    private void handleIntent(Intent intent) {
        String intentString = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (intentString != null) {
            String[] data = intentString.split("/");
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
