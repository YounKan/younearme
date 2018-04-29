package com.example.younearme.younearme;

import android.app.Activity;
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

public class ChatwithActivity extends AppCompatActivity {

    ListView chatList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> alId = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwith);
        chatList = (ListView) findViewById(R.id.chatList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        pd = new ProgressDialog(ChatwithActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://younearme-cae27.firebaseio.com/chatwith/"+UserDetails.username+".json";

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

        RequestQueue rQueue = Volley.newRequestQueue(ChatwithActivity.this);
        rQueue.add(request);

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = alId.get(position);
                UserDetails.nicknameChat=al.get(position);
                startActivity(new Intent(ChatwithActivity.this, ChatActivity.class));

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

                    String nickname = obj.getJSONObject(key).getString("nickname");
                    al.add(nickname);
                    alId.add(key);


                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 1){
            noUsersText.setVisibility(View.VISIBLE);
            chatList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            chatList.setVisibility(View.VISIBLE);
            chatList.setAdapter(new ArrayAdapter<String>(ChatwithActivity.this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(ChatwithActivity.this, SearchActivity.class));
                return true;
            case R.id.world:
                startActivity(new Intent(ChatwithActivity.this, WorldActivity.class));
                return true;
            case R.id.chat:
                return true;
            case R.id.profile:
                startActivity(new Intent(ChatwithActivity.this, UserActivity.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(ChatwithActivity.this, LoginActivity.class));
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
