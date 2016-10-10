package example.com.registrationform;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.codetroopers.betterpickers.*;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.datepicker.DatePicker;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

import java.sql.SQLInput;
import java.util.Calendar;

import static com.codetroopers.betterpickers.R.id.text;

import static example.com.registrationform.R.id.fab;
//import static example.com.registrationform.R.id.@+id/submit_form;

public class ScrollingActivity extends AppCompatActivity
        implements DatePickerDialogFragment.DatePickerDialogHandler{



    //Example code for taking photos...

    static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton iFab;

   // Log.d("ScrollingActivity","Called ScrollingActivity");

    /* Contact fields */
    EditText iNameFld;
    EditText iPhoneFld;
    EditText iEMailFld;
    EditText iAddressFld;

    Button iSelectDateBtn; //Date of Birth

    /* Radio buttons */
    RadioButton iMaleRb;
    RadioButton iFemaleRb;

    Spinner iNationalitySpn;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iFab.setImageBitmap(imageBitmap);


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iNationalitySpn = (Spinner)findViewById(R.id.SpnNationality);
        iNameFld = (EditText)findViewById(R.id.FldName);
        iPhoneFld = (EditText)findViewById(R.id.TxtPhone);
        iEMailFld = (EditText)findViewById(R.id.TxtEMailAddr);
        iAddressFld = (EditText)findViewById(R.id.MleAddress);

        iMaleRb = (RadioButton)findViewById(R.id.RbMale);
        iFemaleRb = (RadioButton)findViewById(R.id.RbFemale);

        ArrayAdapter<CharSequence> natAdapter = ArrayAdapter.createFromResource(this,
                R.array.nationalities, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        natAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        iNationalitySpn.setAdapter(natAdapter);

        iSelectDateBtn = (Button)findViewById(R.id.BtnSelectDate);
        iSelectDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View aView) {
              // Snackbar.make(aView, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();

                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setYearOptional(true);
                dpb.show();

                //DatePicker picker = dpb;



            }
        }) ;

        iFab = (FloatingActionButton) findViewById(fab);
        iFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aView) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if (id == R.id.MiSubmitForm) {
            //Need to collect from each field...

            //Need to implement the functions for DBIO...
            try {
                System.out.println("\r\nScrollingActivity : Pressed MiSubmitForm\r\n");
                SqLiteDbHelper hlp = new SqLiteDbHelper(this.getApplicationContext());
                SQLiteDatabase db = hlp.getWritableDatabase();
                hlp.onCreate(db);
                Log.d("ScrollingActivity", String.valueOf(db.rawQuery("SELECT * FROM users WHERE _id = ?", new String[]{"0"}).getColumnNames().length));

                //Try to write the data into the database...
                ContentValues users = new ContentValues();
                users.put(SqLiteDbHelper.KName, iNameFld.getText().toString());
                users.put(SqLiteDbHelper.KDateOfBirth, iSelectDateBtn.getText().toString());
                users.put(SqLiteDbHelper.KNationality, iNationalitySpn.getSelectedItemId());

                //Gender serialisation
                if (iMaleRb.isChecked()) {
                    users.put(SqLiteDbHelper.KGender, 0);
                }

                if (iFemaleRb.isChecked()) {
                    users.put(SqLiteDbHelper.KGender, 1);
                }

                users.put(SqLiteDbHelper.KPhoneNbr, iPhoneFld.getText().toString());
                users.put(SqLiteDbHelper.KEMailAddress, iEMailFld.getText().toString());

                    db.insert(SqLiteDbHelper.KTblUsers, null,users);
                db.close();

                //Now re-open
                db = hlp.getWritableDatabase();
                Cursor crsRead = db.query("users",
                        null, null, null, null, null, null);
                crsRead.moveToFirst();

                Log.d("ScrollingActivity", "Columns: " + crsRead.getColumnNames());
                db.close();

            }

            catch(android.database.sqlite.SQLiteException aSqlEx) {
                Log.d("ScrollingActivity", "DB activity failed: " + aSqlEx);
            }


            return true;
        }

        //Erase the contents of the fields...
        if (id == R.id.MiResetForm) {

            /* Reset the icon to the default, using the deprecated API */
            iFab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));

            //Now reset the text fields/buttons
            iSelectDateBtn.setText(getResources().getString(R.string.SelectStr));
            iNameFld.setText("");
            iPhoneFld.setText("");
            iEMailFld.setText("");
            iAddressFld.setText("");

            //Clear the radio buttons
            iMaleRb.setChecked(false);
            iFemaleRb.setChecked(false);

            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogDateSet(int aReference, int aYear, int aMonth, int aDay) {
        iSelectDateBtn.setText(getString(R.string.CalShortDateStr, aDay,(aMonth + 1), aYear));
    }
}
