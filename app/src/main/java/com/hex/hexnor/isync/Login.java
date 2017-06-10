package com.hex.hexnor.isync;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText Emailid;
    EditText Password;
    Button login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Emailid=(EditText)findViewById(R.id.emailid);
        Password=(EditText)findViewById(R.id.password);
        login= (Button) findViewById(R.id.login);
        register= (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String u = Emailid.getText().toString();
                final String p = Password.getText().toString();
                String url = "http://codersarena.me:8080/api/login";
                Toast.makeText(Login.this, "Button pressed", Toast.LENGTH_SHORT).show();
                        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    Toast.makeText(Login.this,jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                    String status=jsonObject.getString("login");
                                    String token=jsonObject.getString("token");
                                    if(status.equals("true")){
                                        Intent i=new Intent(Login.this,Profile.class);
                                        Bundle b=new Bundle();
                                        b.putString("email",u);
                                        b.putString("token",token);
                                        b.putString("username",jsonObject.getString("username"));
                                        i.putExtras(b);
                                        startActivity(i);
                                        Toast.makeText(Login.this, "Succesfully logged in", Toast.LENGTH_SHORT).show();

                                    }else
                                        Toast.makeText(Login.this, "Error" +
                                                "", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(Login.this, volleyError.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", u);
                                params.put("password",p);
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Content-Type", "application/x-www-form-urlencoded");
                                //params.put("authorization", "token ce3fe9a203703c7ea3da8727ff8fbafec8ddbf44");
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }



        });
}
}
