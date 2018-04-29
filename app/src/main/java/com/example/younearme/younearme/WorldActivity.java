package com.example.younearme.younearme;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class WorldActivity extends AppCompatActivity {

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> alId = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    String idChatwith;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        pd = new ProgressDialog(WorldActivity.this);
        pd.setMessage("Loading...");
        pd.show();

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

        RequestQueue rQueue = Volley.newRequestQueue(WorldActivity.this);
        rQueue.add(request);

    usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UserDetails.chatWith = al.get(position);
            idChatwith = alId.get(position);
            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://younearme-cae27.firebaseio.com/chatwith/"+UserDetails.username);
           // reference.child(UserDetails.username).child("Idchaiwith").setValue(idChatwith);
            reference.child(idChatwith).child("chatwith").setValue(UserDetails.chatWith);

            startActivity(new Intent(WorldActivity.this, ChatActivity.class));

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

                if(!key.equals(UserDetails.username)) {
                  String nickname = obj.getJSONObject(key).getString("nickname");
                    al.add(nickname);
                    alId.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(WorldActivity.this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(WorldActivity.this, SearchActivity.class));
                return true;
            case R.id.world:
                return true;
            case R.id.chat:
                startActivity(new Intent(WorldActivity.this, ChatwithActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(WorldActivity.this, UserActivity.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(WorldActivity.this, LoginActivity.class));
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
