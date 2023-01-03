package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.my.vibras.databinding.ActivitySignUpBinding;
import com.ozcanalasalvar.library.view.datePicker.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUpAct extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private String strEmail="",strPass="",strDob="";
    private Date date;

    String LoginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up);

        if(getIntent()!=null)
        {
            LoginType=getIntent().getStringExtra("loginType").toString();
        }

        binding.RNext.setOnClickListener(v -> {

            strEmail = binding.edtEmail.getText().toString().trim();
            strPass = binding.edtPassword.getText().toString().trim();


            isValid();
        });

        binding.llLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpAct.this,LoginAct.class));
            finish();
        });

        binding.tvDOb.setOnClickListener(v ->
                {
                    try {
                        showDateSelection();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.edtEmail.setError(getString(R.string.enter_email));
        } else if (strPass.equalsIgnoreCase("")) {
            binding.edtPassword.setError(getString(R.string.enter_pass));
        }else if (strDob.equalsIgnoreCase("")) {
            Toast.makeText(SignUpAct.this, ""+getString(R.string.select_dare), Toast.LENGTH_SHORT).show();
        } else
        {
            startActivity(new
                    Intent(SignUpAct.this,CompleteSignUpAct.class)
                    .putExtra("email",strEmail).putExtra("pass",strPass)
                    .putExtra("dob",strDob).putExtra("loginType",LoginType));
            finish();
        }

    }

    public void showDateSelection() throws ParseException {

        String dateStr = "04/05/2010";
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat s2 = new SimpleDateFormat("ddMMyyyyHHmm");
        Date d = s1.parse("02/11/2012 23:11");
        String s3 = s2.format(d);
        System.out.println(s3);
        long l = Long.parseLong(s3);
        System.out.println(l);

        SimpleDateFormat s4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat s5 = new SimpleDateFormat("ddMMyyyyHHmm");
        Date d1 = s4.parse("02/11/2030 23:11");
        String s6 = s5.format(d1);
        System.out.println(s6);
        long l1 = Long.parseLong(s6);
        System.out.println(l);

        final Dialog dialog = new Dialog(SignUpAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_select_date);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        DatePicker datePicker = dialog.findViewById(R.id.datepicker);
        datePicker.setOffset(3);
        datePicker.setTextSize(19);
        datePicker.setPickerMode(DatePicker.DAY_ON_FIRST);
        AppCompatButton btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v ->
                {
                    long val = datePicker.getDate();
                    date=new Date(val);
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                    String dateText = df2.format(date);
                    binding.tvDOb.setText(dateText);
                    strDob = dateText;

//                    Duration duration = new Duration();
//                    duration.add( today );
//                    duration.substract( birthDate);
//                    int years = duration.getYears();
//                    int months = duration.getMonths();
//                    int days = duration.getDays();

                    dialog.dismiss();

                }
        );

        datePicker.setDataSelectListener(new DatePicker.DataSelectListener() {
            @Override
            public void onDateSelected(long date, int day, int month, int year) {

                long val = date;
                Date date1=new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                String dateText = df2.format(date1);
                binding.tvDOb.setText(dateText);
                strDob = dateText;

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}