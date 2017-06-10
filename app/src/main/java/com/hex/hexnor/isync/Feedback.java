package com.hex.hexnor.isync;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
Button btn;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        btn= (Button) findViewById(R.id.feedback);
        editText= (EditText) findViewById(R.id.editText3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "yokeshrana@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Isync Feedback");
                email.putExtra(Intent.EXTRA_TEXT,editText.getText().toString() );
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                Toast.makeText(Feedback.this, "Successfully submitted feedback", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
