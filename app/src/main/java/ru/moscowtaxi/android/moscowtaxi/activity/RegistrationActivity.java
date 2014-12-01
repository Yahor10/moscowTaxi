package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiHttpClient;

/**
 * Created by alex-pers on 12/1/14.
 */
public class RegistrationActivity extends Activity {
    EditText edtPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_registration);

        edtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);
        findViewById(R.id.button_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WebUtils.isOnline(RegistrationActivity.this.getApplicationContext())) {
                    RegistrationTask registrationTask = new RegistrationTask(edtPhoneNumber.getText().toString(), Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), RegistrationActivity.this.getApplicationContext());
                    registrationTask.execute();
                }

            }
        });
    }

    private class RegistrationTask extends AsyncTask<Void, Void, Boolean> {
        String phoneNumber;
        String phoneId;
        Context context;

        public RegistrationTask(String number, String id, Context context) {
            this.phoneNumber = number;
            this.phoneId = id;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            TaxiHttpClient client = new TaxiHttpClient(context);
            String result = client.registrateUser(phoneNumber, phoneId);



            if (result == null || result.isEmpty()) {
                return false;
            } else {
                Log.d("REGISTRATION_TASK",result);
                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            String text = "";
            if (aBoolean) {
                text = "Ожидайте СМС";
            } else {
                text = " Регистрация не сработала";
            }

            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
