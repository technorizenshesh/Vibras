package com.my.vibras.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.vibras.R;
import com.my.vibras.databinding.ActivityHelpContactBinding;

public class HelpContactActivity extends AppCompatActivity {
    ActivityHelpContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_help_contact);
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.emailNow.setOnClickListener(v -> {
            //info@vibrasapp.com
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" +"info@vibrasapp.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact For Help ");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

            startActivity(Intent.createChooser(emailIntent, "Vibras Help"));
        });


    }
}