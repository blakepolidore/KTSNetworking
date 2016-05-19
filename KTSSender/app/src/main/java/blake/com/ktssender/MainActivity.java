package blake.com.ktssender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

    private void setViews() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        monthSpinner = (Spinner) findViewById(R.id.month_spinner);
        daySpinner = (Spinner) findViewById(R.id.day_spinner);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        submitButton = (Button) findViewById(R.id.send_button);
    }

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
        monthSpinner.setAdapter(adapterMonth);
        daySpinner.setAdapter(adapterDay);
        yearSpinner.setAdapter(adapterYear);
        monthSpinner.setPrompt("Select Month");
        daySpinner.setPrompt("Select Day");
        yearSpinner.setPrompt("Select Year");
    }

    private String getSpinnerSelections(Spinner spinner) {
        TextView textView = (TextView) spinner.getSelectedView();
        String spinnerText = textView.getText().toString();
        return spinnerText;
    }

    private void sendData() {
        ArrayList<PersonalInfo> personalInfoList = new ArrayList<>();
        PersonalInfo personalInfo = new PersonalInfo(nameEditText.getText().toString(),
                getSpinnerSelections(monthSpinner), getSpinnerSelections(daySpinner),
                getSpinnerSelections(yearSpinner));
        personalInfoList.add(personalInfo);
        
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_TEXT, personalInfoList);
        shareIntent.setType("text/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

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
}
