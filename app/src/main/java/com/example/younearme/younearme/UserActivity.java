package com.example.younearme.younearme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class UserActivity extends AppCompatActivity {
    EditText username, password,nickname;
    Button registerButton;
    String user, pass,nick,gender ,birthday,city;
    TextView login;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private CheckBox checkBoxMale, checkBoxFemale, checkBoxOther;
    private ArrayList<String> cityArray = new ArrayList<String>();
    ArrayList<String> al = new ArrayList<>();
    Spinner spinnerMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        password = (EditText)findViewById(R.id.password);
        nickname = (EditText)findViewById(R.id.nickname);
        registerButton = (Button)findViewById(R.id.updateButton);


        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        checkBoxMale = (CheckBox) findViewById(R.id.Male);
        checkBoxFemale = (CheckBox) findViewById(R.id.Female);
        checkBoxOther = (CheckBox) findViewById(R.id.Other);


        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String url = "https://younearme-cae27.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(UserActivity.this);
        rQueue.add(request);




        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = password.getText().toString();
                nick = nickname.getText().toString();
                birthday = dateView.getText().toString();

                if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(UserActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://younearme-cae27.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl("https://younearme-cae27.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(UserDetails.username).child("password").setValue(pass);
                                reference.child(UserDetails.username).child("nickname").setValue(nick);
                                reference.child(UserDetails.username).child("gender").setValue(gender);
                                reference.child(UserDetails.username).child("birthday").setValue(birthday);
                                reference.child(UserDetails.username).child("city").setValue(city);
                                Toast.makeText(UserActivity.this, "Update successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (obj.has(UserDetails.username)) {
                                        reference.child(UserDetails.username).child("password").setValue(pass);
                                        reference.child(UserDetails.username).child("nickname").setValue(nick);
                                        reference.child(UserDetails.username).child("gender").setValue(gender);
                                        reference.child(UserDetails.username).child("birthday").setValue(birthday);
                                        reference.child(UserDetails.username).child("city").setValue(city);
                                        Toast.makeText(UserActivity.this, "Update successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(UserActivity.this, "Update Fail", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(UserActivity.this);
                    rQueue.add(request);
                }
            }
        });



        // Spinner with CustomAdapter
         spinnerMenu = (Spinner) findViewById(R.id.spinner_menu);
        cityArray.add("Select City");
        cityArray.add("America");
        cityArray.add("Argentina");
        cityArray.add("Australia");
        cityArray.add("Brazil");
        cityArray.add("Canada");
        cityArray.add("Egypt");
        cityArray.add("France");
        cityArray.add("German");
        cityArray.add("Hong Kong");
        cityArray.add("Italy");
        cityArray.add("Japan");
        cityArray.add("Mexico");
        cityArray.add("New Zealand");
        cityArray.add("South Africa");
        cityArray.add("South Korea");
        cityArray.add("United Kingdom");
        cityArray.add("Thailand");


        CustomAdapter customAdapter = new CustomAdapter(UserActivity.this, cityArray);
        spinnerMenu.setAdapter(customAdapter);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    city = cityArray.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
               

                if(key.equals(UserDetails.username)) {
                    int index = 0;
                    String cityG = obj.getJSONObject(key).getString("city");
                    String genderG = obj.getJSONObject(key).getString("gender");
                    for(int k =0 ; k <cityArray.size();k++ ){
                        if(cityArray.get(k).equals(cityG)){
                            index = k;
                        }
                    }
                    nickname.setText(obj.getJSONObject(key).getString("nickname"));
                    dateView.setText(obj.getJSONObject(key).getString("birthday"));
                    spinnerMenu.setSelection(index);
                    city = cityArray.get(index);
                    if(genderG.equals("Female")){
                        checkBoxFemale.setChecked(true);
                        gender="Female";
                    }
                    if(genderG.equals("Male")){
                        checkBoxMale.setChecked(true);
                        gender="Male";
                    }
                    if(genderG.equals("Other")){
                        checkBoxOther.setChecked(true);
                        gender="Other";
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        birthday = dateView.getText().toString();
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            gender = checkBox.getText().toString();
            switch(checkBox.getId()) {

                case R.id.Male:

                    checkBoxOther.setChecked(false);
                    checkBoxFemale.setChecked(false);
                    gender = checkBox.getText().toString();

                    break;

                case R.id.Female:

                    checkBoxMale.setChecked(false);
                    checkBoxOther.setChecked(false);
                    gender = checkBox.getText().toString();
                    break;

                case R.id.Other:

                    checkBoxFemale.setChecked(false);
                    checkBoxMale.setChecked(false);
                    gender = checkBox.getText().toString();
                    break;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(UserActivity.this, SearchActivity.class));
                return true;
            case R.id.world:
                startActivity(new Intent(UserActivity.this, WorldActivity.class));
                return true;
            case R.id.chat:
                startActivity(new Intent(UserActivity.this, ChatwithActivity.class));
                return true;
            case R.id.profile:
                // help action
                return true;
            case R.id.logout:
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
