package com.my.vibras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.my.vibras.databinding.ActivityVerificationBinding;
import com.my.vibras.model.SuccessResSignup;
import com.my.vibras.retrofit.ApiClient;
import com.my.vibras.retrofit.VibrasInterface;
import com.my.vibras.utility.DataManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.vibras.retrofit.Constant.showToast;

public class VerificationAct extends AppCompatActivity {

    ActivityVerificationBinding binding;
    String LoginType,userId,finalOtp;

    private VibrasInterface apiInterface;

    private Context mContext;
    private EditText[] editTexts;
 String otppp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_verification);

        mContext = VerificationAct.this;

        apiInterface = ApiClient.getClient().create(VibrasInterface.class);

        if(getIntent()!=null)
        {

            otppp=getIntent().getStringExtra("otp").toString();
            LoginType=getIntent().getStringExtra("type").toString();
            userId=getIntent().getStringExtra("user_id").toString();
        //    Toast.makeText(getApplicationContext(),otppp,Toast.LENGTH_SHORT).show();
            if (otppp!=null&&otppp.length()>3){
                for (int i = 0; i <otppp.length() ; i++) {
                    String  s[]= otppp.split("");

                    binding.et1.setText(s[0]);
                    binding.et2.setText(s[1]);
                    binding.et3.setText(s[2]);
                    binding.et4.setText(s[3]);
                }

            }
            signup();
        }

        editTexts = new EditText[]{binding.et1, binding.et2, binding.et3, binding.et4};
        binding.et1   .addTextChangedListener(new PinTextWatcher(0));
        binding.et2   .addTextChangedListener(new PinTextWatcher(1));
        binding.et3  .addTextChangedListener(new PinTextWatcher(2));
        binding.et4  .addTextChangedListener(new PinTextWatcher(3));
        binding.et1  .setOnKeyListener(new PinOnKeyListener(0));
        binding.et2  .setOnKeyListener(new PinOnKeyListener(1));
        binding.et3  .setOnKeyListener(new PinOnKeyListener(2));
        binding.et4   .setOnKeyListener(new PinOnKeyListener(3));
        binding.RVerify.setOnClickListener(v ->
        {

            if (TextUtils.isEmpty(binding.et1.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.et2.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.et3.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.et4.getText().toString().trim())) {
                Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
            } else {
                finalOtp =
                        binding.et1.getText().toString().trim() +
                                binding.et2.getText().toString().trim() +
                                binding.et3.getText().toString().trim() +
                                binding.et4.getText().toString().trim();
                ;
                signup();
            }

        });

    }

    public void signup()
    {
        DataManager.getInstance().showProgressMessage(VerificationAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("otp",otppp);

        Call<SuccessResSignup> signupCall = apiInterface.verifyOtp(map);
        signupCall.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    if (data.status.equals("1")) {
                        showToast(VerificationAct.this, data.message);

                        if(LoginType.equalsIgnoreCase("user"))
                        {
                            startActivity(new Intent(VerificationAct.this,
                                    LoginAct.class).putExtra("loginType","user"));
                        }else
                        {
                            startActivity(new Intent(VerificationAct.this,
                                    LoginAct.class).putExtra("loginType","company"));

                        }

                    } else if (data.status.equals("0")) {
                        showToast(VerificationAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    public class PinTextWatcher implements TextWatcher {

        private final int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();

        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);
            editTexts[currentIndex].setBackground(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.border_gray));
            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                // hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }
    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private final int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }


    }

}