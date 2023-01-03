package com.my.vibras;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.vibras.Company.HomeComapnyAct;
import com.my.vibras.act.HomeUserAct;
import com.my.vibras.databinding.ActivityChooseLanguageBinding;
import com.my.vibras.retrofit.Constant;
import com.my.vibras.utility.SharedPreferenceUtility;

import static com.my.vibras.retrofit.Constant.updateResources;

public class ChooseLanguage extends AppCompatActivity {

    ActivityChooseLanguageBinding binding;
    String from = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);
        boolean val = SharedPreferenceUtility.getInstance(ChooseLanguage.this)
                .getBoolean(Constant.SELECTED_LANGUAGE);
        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");

        }
        if (!val) {
            updateResources(ChooseLanguage.this, "en");
            binding.radio1.setChecked(true);
            binding.radio3.setChecked(false);
        } else {
            updateResources(ChooseLanguage.this, "es");
            binding.radio3.setChecked(true);
            binding.radio1.setChecked(false);
        }

        binding.radio1.setOnClickListener(v ->
                {
                    updateResources(ChooseLanguage.this, "en");
                    binding.radio3.setChecked(false);
                    SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, false);
                }
        );

        binding.radio3.setOnClickListener(v ->
                {
                    updateResources(ChooseLanguage.this, "es");
                    binding.radio1.setChecked(false);
                    SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, true);
                }
        );

        binding.btnNext.setOnClickListener(v ->
                {

                    int id = binding.radioGroup.getCheckedRadioButtonId();

                    if (binding.radio1.getId() == id) {
                        SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, false);
                    } else {
                        SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, true);
                    }

                    if (from.equalsIgnoreCase("login")) {
                        startActivity
                                (new Intent(
                                        ChooseLanguage.this,
                                        SelectViberLoginAct.class));finish();
                    } else {

                        if (from.equalsIgnoreCase("company")) {

                            startActivity
                                    (new Intent(
                                            ChooseLanguage.this,
                                            HomeComapnyAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            startActivity
                                    (new Intent(
                                            ChooseLanguage.this,
                                            HomeUserAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    }

                }
        );
    }


}