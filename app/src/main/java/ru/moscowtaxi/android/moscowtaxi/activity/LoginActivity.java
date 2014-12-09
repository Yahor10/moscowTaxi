package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by alex-pers on 12/1/14.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    EditText edtPhoneNumber;
    EditText edtSmsCode;
    Button butAuthorize;
    Button butRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);
        edtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);
        edtSmsCode = (EditText) findViewById(R.id.edt_code_sms);

        butAuthorize = (Button) findViewById(R.id.button_authorize);
        butRegistration = (Button) findViewById(R.id.button_registration);

        findViewById(R.id.button_go_main).setOnClickListener(this);

        butAuthorize.setOnClickListener(this);
        butRegistration.setOnClickListener(this);

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_authorize:


                Toast.makeText(this, "Authorize", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_registration:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.button_go_main:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
