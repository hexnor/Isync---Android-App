package com.hex.hexnor.isync;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by hexnor on 6/9/17.
 */

class Contentadd extends AppCompatActivity {
    Bundle b;
    EditText editText,editText2;
    Button button,btnpaste,btnclear;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewnote);
        editText= (EditText) findViewById(R.id.addtitle);
        editText2= (EditText) findViewById(R.id.addtext);
        button= (Button) findViewById(R.id.addsave);
        b=getIntent().getExtras();
        final String email=b.getString("email");
        btnclear= (Button) findViewById(R.id.btnclear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText("");
                editText.setText("");
            }
        });
        btnpaste= (Button) findViewById(R.id.btnpaste);
        btnpaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipMan = (ClipboardManager)getSystemService(v.getContext().CLIPBOARD_SERVICE);
                editText2.setText(editText2.getText().toString()+clipMan.getText());
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=editText.getText().toString();
                String text=editText2.getText().toString();
                String url = "http://codersarena.me:8080/api/add";
                RequestQueue requestQueue = Volley.newRequestQueue(Contentadd.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Contentadd.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Contentadd.this,Profile.class);
                        Bundle b=new Bundle();
                        Bundle bundle = getIntent().getExtras();
                        b.putString("email",bundle.getString("email"));
                        b.putString("token",bundle.getString("token"));
                        b.putString("username",bundle.getString("username"));
                        i.putExtras(b);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(Contentadd.this, ""+volleyError, Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", email);

                        try {
                            params.put("text", editText2.getText().toString());
                            params.put("title",editText.getText().toString());

                            // Toast.makeText(ReadNotes.this, "encrypted"+encryption.encrypt(b.getString("token"),editText.getText().toString()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




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
