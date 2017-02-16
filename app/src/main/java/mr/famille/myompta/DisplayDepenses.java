package mr.famille.myompta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DisplayDepenses extends FragmentActivity implements View.OnClickListener {
    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb ;
    TextView design;
    TextView montant;
    TextView beneficiaire;
    TextView dateO;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        design = (TextView) findViewById(R.id.editTextDesignation);
        montant = (TextView) findViewById(R.id.editTextMontant);
        beneficiaire = (TextView) findViewById(R.id.editTextBeneficiaire);
        dateO=(TextView) findViewById(R.id.editTextDateOp);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                 rs.moveToFirst();

                String ide = rs.getString(rs.getColumnIndex(DBHelper.DEPENSES_COLUMN_ID));
                String designatio = rs.getString(rs.getColumnIndex(DBHelper.DEPENSES_COLUMN_DESIGNATION));
                String montan = rs.getString(rs.getColumnIndex(DBHelper.DEPENSES_COLUMN_MONTANT));
                String beneficiair = rs.getString(rs.getColumnIndex(DBHelper.DEPENSES_COLUMN_BENEFICIAIRE));
                String dateO = rs.getString(rs.getColumnIndex(DBHelper.DEPENSES_COLUMN_DATE_OP));

                if (!rs.isClosed())  {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                design.setText((CharSequence) designatio);
                design.setFocusable(false);
                design.setClickable(false);

                montant.setText((CharSequence) montan);
                montant.setFocusable(false);
                montant.setClickable(false);



                beneficiaire.setText((CharSequence) beneficiair);
                beneficiaire.setFocusable(false);
                beneficiaire.setClickable(false);

                dateOp.setText((CharSequence) dateO);
                dateOp.setFocusable(false);
                dateOp.setClickable(false);


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.menu_display_contact, menu);
            } else{
                getMenuInflater().inflate(R.menu.menu_main,menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                design.setEnabled(true);
                design.setFocusableInTouchMode(true);
                design.setClickable(true);

                montant.setEnabled(true);
                montant.setFocusableInTouchMode(true);
                montant.setClickable(true);

             /*   email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);
*/
                beneficiaire.setEnabled(true);
                beneficiaire.setFocusableInTouchMode(true);
                beneficiaire.setClickable(true);

              /*  place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);*/

                return true;
            case R.id.Delete_Contact:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.supDepence)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateContact(id_To_Update, design.getText().toString(),
                        montant.getText().toString(),
                        beneficiaire.getText().toString(), dateO.getText().toString()) ){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                if(mydb.insertContact(design.getText().toString(), montant.getText().toString(), beneficiaire.getText().toString()
                        , dateO.getText().toString() )){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
    private EditText fromDateEtxt;
    private EditText dateOp;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog dateOpDataPicker;

    private SimpleDateFormat dateFormatter;
    private void findViewsById() {


        dateOp = (EditText) findViewById(R.id.editTextDateOp);
        dateOp.setInputType(InputType.TYPE_NULL);
    }
    private void setDateTimeField() {

        dateOp.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        dateOpDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateOp.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {

     if(view == dateOp) {
            dateOpDataPicker.show();
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(this.getSupportFragmentManager(),"datePicker");
    }
}



