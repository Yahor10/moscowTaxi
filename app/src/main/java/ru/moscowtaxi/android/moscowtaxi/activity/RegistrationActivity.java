package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

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
                if (edtPhoneNumber.getText().toString().length() < 7) {
                    edtPhoneNumber.setError("Слишком короткий номер");
                    return;
                }
                if (WebUtils.isOnline(RegistrationActivity.this.getApplicationContext())) {
//                    RegistrationTask registrationTask = new RegistrationTask(edtPhoneNumber.getText().toString(), Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), RegistrationActivity.this.getApplicationContext());
//                    registrationTask.execute();
                    final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setMessage(getString(R.string.wait));
                    progressDialog.show();
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(TaxiApi.MAIN_URL)
                            .build();

                    String phone = edtPhoneNumber.getText().toString();
//                    String phone = PreferenceUtils.getCurrentUserPhone(getApplicationContext());
                    String id = PreferenceUtils.getDeviceId(getApplicationContext());

                    TaxiApi service = restAdapter.create(TaxiApi.class);

                    service.registration(phone, id, new Callback<Response>() {


                        @Override
                        public void success(Response response, Response response2) {
                            try {
                                progressDialog.dismiss();
                                Log.d("REGISTRATION", WebUtils.getResponseString(response));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("REGISTRATION_FAIL", error.toString());
                            Toast.makeText(getApplicationContext(), R.string.error_connection_occured,Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }

}
