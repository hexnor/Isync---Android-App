package com.hex.hexnor.isync;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , AdapterView.OnItemClickListener{

    ListView listView;
    ArrayList NotesTitle=new ArrayList();
    ArrayList Notespk=new ArrayList();
    ArrayList NotesDate=new ArrayList();
    List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
    String Email,Username,Token;
    SimpleAdapter  arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        Email=bundle.getString("email");
        Username=bundle.getString("username");
        Token=bundle.getString("token");

        Toast.makeText(this, "Welcome"+ Username +"      "+ Email , Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Profile.this,Contentadd.class);
                Bundle b=new Bundle();
                b.putString("email",Email);
                b.putString("username",Username);
                b.putString("token",Token);
                i.putExtras(b);
                startActivity(i);


            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setdata("short");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView=(ListView)findViewById(R.id.listview);


        String[] from = { "no","title","date" };
        int[] to = { R.id.no,R.id.title,R.id.date};


        arrayAdapter= new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
        TextView pem= (TextView)  navigationView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.profileemail);
        TextView pun= (TextView)   navigationView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.profileusername);
            pem.setText(Email);
            pun.setText(Username);




        listView.setAdapter(arrayAdapter);
         arrayAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(Profile.this,ReadNotes.class);
                Bundle b=new Bundle();
                b.putString("pk", (String) Notespk.get(position));
                b.putString("text", (String) Notespk.get(position));
                b.putString("email",Email);
                b.putString("username",Username);
                b.putString("token",Token);
                b.putString("title",(String)NotesTitle.get(position));
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setdata("short");
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(arrayAdapter);
        setdata("short");
    }

    private void setdata(final String option) {
        String url = "http://codersarena.me:8080/api/show";
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(Profile.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                      aList.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject ob = jsonArray.getJSONObject(i);
                        String pk= " "+ob.get("pk");
                        String title= (String) ob.get("title");
                       NotesTitle.add(i,title);
                       Notespk.add(i,pk);
                        NotesDate.add(i,(String)ob.get("date"));
                        HashMap<String, String> hm = new HashMap<String,String>();
                        HashMap<String, String> hp = new HashMap<String,String>();
                        hm.put("title",""+ NotesTitle.get(i));
                        hm.put("date",""+NotesDate.get(i));
                        hm.put("no", (i+1)+" " );

                        aList.add(hm);
                    }
                    arrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Profile.this, volleyError.toString(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Email);
                params.put("token",Token);
                params.put("option",option);
                return params;
            }


        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      if (id == R.id.search) {

        } else if (id == R.id.newnote) {

          Intent i=new Intent(Profile.this,Contentadd.class);
          Bundle b=new Bundle();
          b.putString("email",Email);
          b.putString("username",Username);
          b.putString("token",Token);
          i.putExtras(b);
          startActivity(i);
        }  else if (id == R.id.emailus) {
          Intent email = new Intent(Intent.ACTION_SEND);
          email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "yokeshrana@gmail.com"});
          email.setType("message/rfc822");
          startActivity(Intent.createChooser(email, "Choose an Email client :"));
        } else if (id == R.id.Feedback) {
          Intent email = new Intent(Intent.ACTION_SEND);
          email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "yokeshrana@gmail.com"});
          email.putExtra(Intent.EXTRA_SUBJECT, "Isync Feedback");
          email.setType("message/rfc822");
          startActivity(Intent.createChooser(email, "Choose an Email client :"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
