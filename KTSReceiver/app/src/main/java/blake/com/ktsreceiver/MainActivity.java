package blake.com.ktsreceiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

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

    private void setViews() {
        name = (TextView) findViewById(R.id.name_text);
        birthday = (TextView) findViewById(R.id.birthday_text);
        age = (TextView) findViewById(R.id.age_text);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("text/")) {
                handleIntent(intent);
            }
        }
    }

    private void handleIntent(Intent intent) {
        ArrayList<PersonalInfo> personalInfoList = intent.getParcelableArrayListExtra(Intent.EXTRA_TEXT);
        if (personalInfoList != null) {
            PersonalInfo personalInfo = personalInfoList.get(0);
            name.setText(personalInfo.name);
            String fullBirthday = personalInfo.month + "/" + personalInfo.day + "/" + personalInfo;
            birthday.setText(fullBirthday);
        }
    }
}
