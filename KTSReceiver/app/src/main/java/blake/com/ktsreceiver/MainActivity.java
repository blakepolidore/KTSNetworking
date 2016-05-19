package blake.com.ktsreceiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private TextView birthday;
    private TextView age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
    }

    private void setViews() {
        name = (TextView) findViewById(R.id.name_text);
        birthday = (TextView) findViewById(R.id.birthday_text);
        age = (TextView) findViewById(R.id.age_text);
    }
}
