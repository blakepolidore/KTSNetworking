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

/**
 * Activity to send personal information to receiver application
 */
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
     * Fills the spinners with correct information and puts them in the adpaters
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

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, intentString);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share info to.."));
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
}
