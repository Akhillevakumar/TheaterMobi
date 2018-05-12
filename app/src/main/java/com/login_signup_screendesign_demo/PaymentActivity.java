package com.login_signup_screendesign_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        final CardForm cardForm = findViewById(R.id.card_form);
        final Button button = findViewById(R.id.test);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .actionLabel("Purchase")
                .setup(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    Toast.makeText(PaymentActivity.this, "Valid card" + cardForm.isValid(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentActivity.this, PlayVideoActivity.class));
                } else {
                    Toast.makeText(PaymentActivity.this, "Invalid card", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }


}
