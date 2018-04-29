package com.example.younearme.younearme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Iterator;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<String> cityArray = new ArrayList<String>();
    private ArrayList<String> genderArray = new ArrayList<String>();
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> alId = new ArrayList<>();

    String city  ="", gender ="";
    String idChatwith;

    Button searchButton;
    ListView searchList;
    TextView noUsersText;

    private Spinner genderSpinner;
    private Spinner spinnercity;

    CustomAdapter customAdapter;

    int totalUsers = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchButton = (Button) findViewById(R.id.searchbtn);
        searchList = (ListView) findViewById(R.id.searchList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        genderSpinner = (Spinner) findViewById(R.id.genderselect);

        genderArray.add("select gender");
        genderArray.add("Female");
        genderArray.add("Male");
        genderArray.add("other");

        ArrayAdapter<String> adapterThai = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_dropdown_item_1line, genderArray);
        genderSpinner.setAdapter(adapterThai);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://younearme-cae27.firebaseio.com/users.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        doOnSuccess(s);
                        genderSpinner.setSelection(0);
                        spinnercity.setSelection(0);
                        city ="";
                        gender="";
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(SearchActivity.this);
                rQueue.add(request);
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                idChatwith = alId.get(position);
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://younearme-cae27.firebaseio.com/chatwith/"+UserDetails.username);
                // reference.child(UserDetails.username).child("Idchaiwith").setValue(idChatwith);
                reference.child(idChatwith).child("chatwith").setValue(UserDetails.chatWith);

                startActivity(new Intent(SearchActivity.this, ChatActivity.class));

            }
        });

        spinnercity = (Spinner) findViewById(R.id.cityselect);
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


         customAdapter = new CustomAdapter(SearchActivity.this, cityArray);
        spinnercity.setAdapter(customAdapter);

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    gender = genderArray.get(position);
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
            al.clear();
            alId.clear();

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {

                    if(obj.getJSONObject(key).getString("gender").equals(gender)&&obj.getJSONObject(key).getString("city").equals(city)){
                        String nickname = obj.getJSONObject(key).getString("nickname");
                        al.add(nickname);
                        alId.add(key);
                        //Log.i("ss", nickname);
                    }else if(obj.getJSONObject(key).getString("city").equals(city)&& gender.equals("")){
                        String nickname = obj.getJSONObject(key).getString("nickname");
                        al.add(nickname);
                        alId.add(key);
                    }else if(obj.getJSONObject(key).getString("gender").equals(gender)&&city.equals("")){

                        String nickname = obj.getJSONObject(key).getString("nickname");
                        al.add(nickname);
                        alId.add(key);
                    }
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            searchList.setVisibility(View.VISIBLE);
            searchList.setAdapter(new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, al));
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.world:
                startActivity(new Intent(SearchActivity.this, WorldActivity.class));
                return true;
            case R.id.chat:
                startActivity(new Intent(SearchActivity.this, ChatwithActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(SearchActivity.this, UserActivity.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(SearchActivity.this, LoginActivity.class));
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
