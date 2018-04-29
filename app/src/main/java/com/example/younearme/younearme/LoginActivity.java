package com.example.younearme.younearme;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton loginFButton;

    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    String userF,genderF,birthdayF,nameF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);


        registerUser = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginFButton = (LoginButton) findViewById(R.id.login_button);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        callbackManager = CallbackManager.Factory.create();
        loginFButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else {
                    String url = "https://younearme-cae27.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (s.equals("null")) {
                                Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                    } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        UserDetails.nicknameUser = obj.getJSONObject(user).getString("nickname");
                                       startActivity(new Intent(LoginActivity.this, WorldActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                    rQueue.add(request);
                }

            }
        });

        loginFButton.setReadPermissions(Arrays.asList("public_profile","user_birthday"));
        loginFButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                GraphRequest mGraphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    userF = me.optString("id");
                                    nameF = me.optString("name");
                                    genderF = me.optString("gender");
                                    birthdayF = me.optString("birthday");

                                    Log.i("ss", userF);
                                    Log.i("ss", nameF);
                                    Log.i("ss", genderF);
                                    Log.i("ss", birthdayF);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender, birthday");
                mGraphRequest.setParameters(parameters);
                mGraphRequest.executeAsync();


                String url = "https://younearme-cae27.firebaseio.com/users.json";
                StringRequest requestFace = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReferenceFromUrl("https://younearme-cae27.firebaseio.com/users");
                        String cap = genderF.substring(0, 1).toUpperCase() + genderF.substring(1);
                        if (s.equals("null")) {
                            reference.child(userF).child("nickname").setValue(nameF);
                            reference.child(userF).child("gender").setValue(cap);
                            reference.child(userF).child("birthday").setValue(birthdayF);
                            reference.child(userF).child("password").setValue("");
                            reference.child(userF).child("city").setValue("");
                        } else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(user)) {

                                    reference.child(userF).child("nickname").setValue(nameF);
                                    reference.child(userF).child("gender").setValue(cap);
                                    reference.child(userF).child("birthday").setValue(birthdayF);
                                    reference.child(userF).child("password").setValue("");
                                    reference.child(userF).child("city").setValue("");
                                }
                                UserDetails.username = userF;
                               startActivity(new Intent(LoginActivity.this, WorldActivity.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);

                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                rQueue.add(requestFace);

            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

