package com.my.vibras.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.braintreepayments.cardform.view.CardForm;
import com.my.vibras.R;
import com.my.vibras.databinding.ActivityAddPaymentCardBinding;

public class AddPaymentCard extends AppCompatActivity {

    ActivityAddPaymentCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_payment_card);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(AddPaymentCard.this);
    }
}