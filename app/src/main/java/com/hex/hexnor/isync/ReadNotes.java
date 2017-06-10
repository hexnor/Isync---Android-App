package com.hex.hexnor.isync;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ReadNotes extends AppCompatActivity {


    TextView textView,textView2;
    String pk="";
    Bundle b;
    Button save;
    Button btndelete;
    EditText editText,editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_notes);
        textView= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView2);
        btndelete= (Button) findViewById(R.id.btndelete);
        editText= (EditText) findViewById(R.id.editText);
        editText2= (EditText) findViewById(R.id.editText2);
        b=getIntent().getExtras();
        pk=b.getString("pk");
        pk=pk.trim();
        textView2.setText(b.getString("title"));
        textView.setText("");
        textView.setMovementMethod(new ScrollingMovementMethod());
        setdata();
        textView2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btndelete.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                textView2.clearFocus();
                editText2.requestFocus();
                InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(editText2, 0);
                return true;
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btndelete.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                textView.clearFocus();
                editText.requestFocus();
                InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(editText, 0);
                return true;
            }


        });

        save= (Button) findViewById(R.id.btnsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btndelete.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.INVISIBLE);
                editText.clearFocus();
                textView.requestFocus();
                update();




            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(pk);
                Intent i=new Intent(ReadNotes.this,Profile.class);
                Bundle b=new Bundle();
                Bundle bundle = getIntent().getExtras();
                b.putString("email",bundle.getString("email"));
                b.putString("token",bundle.getString("token"));
                b.putString("username",bundle.getString("username"));
                i.putExtras(b);
                startActivity(i);
               // Toast.makeText(ReadNotes.this, "Clicked delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delete(final String pk) {

        String url = "http://isyncweb.herokuapp.com/api/delete";
        RequestQueue requestQueue = Volley.newRequestQueue(ReadNotes.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(ReadNotes.this, "Deleted"+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
              //  Toast.makeText(ReadNotes.this, volleyError.toString(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", b.getString("email"));
                params.put("token",b.getString("token"));
                params.put("pk",pk);
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

    private void update() {
        String url = "http://isyncweb.herokuapp.com/api/update";
        RequestQueue requestQueue = Volley.newRequestQueue(ReadNotes.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("update").equals("true")){
                        Toast.makeText(ReadNotes.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(ReadNotes.this,Profile.class);
                        Bundle b=new Bundle();
                        Bundle bundle = getIntent().getExtras();
                        b.putString("email",bundle.getString("email"));
                        b.putString("token",bundle.getString("token"));
                        b.putString("username",bundle.getString("username"));
                        i.putExtras(b);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(ReadNotes.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               // Toast.makeText(ReadNotes.this, volleyError.toString(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", b.getString("email"));
                params.put("token",b.getString("token"));
                params.put("pk",pk);

                try {
                    params.put("text", editText.getText().toString());
                    params.put("title",editText2.getText().toString());

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





    private void setdata() {
        String url = "http://isyncweb.herokuapp.com/api/show/"+pk+"/";
        RequestQueue requestQueue = Volley.newRequestQueue(ReadNotes.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(ReadNotes.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                   String te= jsonObject.getString("text");
                    //Toast.makeText(ReadNotes.this, "te " + te, Toast.LENGTH_SHORT).show();
                    textView.setText(te);
                    editText.setText(jsonObject.getString("text"));
                    editText2.setText(jsonObject.getString("title"));
                    String d=editText.getText().toString();
                    try{
                        d=AESCrypt.encrypt(b.getString("token"),d);
                        String Enc=AESCrypt.decrypt(b.getString("token"),d);
                       // Toast.makeText(ReadNotes.this,Enc , Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                       // Toast.makeText(ReadNotes.this, "Exception occured "+ e.toString(), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
              //  Toast.makeText(ReadNotes.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", b.getString("email"));
                params.put("token",b.getString("token"));
                params.put("pk",b.getString("pk"));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
