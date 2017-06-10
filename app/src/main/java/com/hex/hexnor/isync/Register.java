package com.hex.hexnor.isync;

import android.app.Activity;
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

public class Register extends Activity {
    EditText name,email,pass,cpass;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name= (EditText) findViewById(R.id.rname);
        email= (EditText) findViewById(R.id.remail);
        pass= (EditText) findViewById(R.id.rpass);
        cpass= (EditText) findViewById(R.id.rcpass);
        btn= (Button) findViewById(R.id.register);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String n, p, cp, em;
                n = name.getText().toString();
                p = pass.getText().toString();
                em = email.getText().toString();
                cp = cpass.getText().toString();

               // Toast.makeText(Register.this, em + ""+p + " "+cp, Toast.LENGTH_SHORT).show();
                if (p.equals(cp)) {
                    String url = "http://isyncweb.herokuapp.com/api/register";
                    Toast.makeText(Register.this, "Button pressed", Toast.LENGTH_SHORT).show();
                    RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                          //  Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.get("created").equals("true")){
                                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("token", (String) jsonObject.get("token"));
                                    bundle.putString("email",em);
                                    bundle.putString("Name",n);
                                    Intent i=new Intent(Register.this,Profile.class);
                                    i.putExtras(bundle);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(Register.this, "Account EXIST", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(Register.this, volleyError.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", n);
                            params.put("password", p);
                            params.put("emailid", em);
                            params.put("updaterequired", "false");
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
                } else {
                    Toast.makeText(Register.this, "Password dont match", Toast.LENGTH_SHORT).show();
                }
            }
            });}

}
