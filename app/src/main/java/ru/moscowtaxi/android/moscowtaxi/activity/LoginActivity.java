package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;

/**
 * Created by alex-pers on 12/1/14.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    protected static final String TAG_ERROR_DIALOG_FRAGMENT = "errorDialog";
    EditText edtPhoneNumber;
    EditText edtSmsCode;
    Button butAuthorize;
    Button butRegistration;

    private static int getVersionFromPackageManager(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos =
                packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open
                // gl es version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    } else {
                        return 1; // Lack of property means OpenGL ES
                        // version 1
                    }
                }
            }
        }
        return 1;
    }

    // following from
    // https://android.googlesource.com/platform/cts/+/master/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java

  /*
   * Copyright (C) 2010 The Android Open Source Project
   *
   * Licensed under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in
   * compliance with the License. You may obtain a copy of
   * the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in
   * writing, software distributed under the License is
   * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   * CONDITIONS OF ANY KIND, either express or implied. See
   * the License for the specific language governing
   * permissions and limitations under the License.
   */

    /**
     * @see FeatureInfo#getGlEsVersion()
     */
    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }



    protected boolean readyToGo() {
        int status =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            if (getVersionFromPackageManager(this) >= 2) {
                return (true);
            } else {
                Toast.makeText(this, R.string.no_playservices, Toast.LENGTH_LONG).show();
                finish();
            }
        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            ErrorDialogFragment.newInstance(status)
                    .show(getFragmentManager(),
                            TAG_ERROR_DIALOG_FRAGMENT);
        } else {
            Toast.makeText(this, R.string.no_playservices, Toast.LENGTH_LONG).show();
            finish();
        }

        return (false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);
        if (!readyToGo()) {
            return;
        }
        edtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);
        edtSmsCode = (EditText) findViewById(R.id.edt_code_sms);

        butAuthorize = (Button) findViewById(R.id.button_authorize);
        butRegistration = (Button) findViewById(R.id.button_registration);

        findViewById(R.id.button_go_main).setOnClickListener(this);

        butAuthorize.setOnClickListener(this);
        butRegistration.setOnClickListener(this);

//        startActivity(new Intent(this,MainActivity.class));
//        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_authorize:
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(TaxiApi.MAIN_URL)
                        .build();
                TaxiApi service = restAdapter.create(TaxiApi.class);
                String phone = edtPhoneNumber.getText().toString();
                String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                BaseModelApi model = new BaseModelApi();
                model.phone = phone;
                model.imei = id;
                service.login(model, new Callback<Response>() {
                    @Override
                    public void success(Response s, Response response) {
                        try {
                            Log.d("LOGIN", WebUtils.getResponseString(s));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    public static class ErrorDialogFragment extends DialogFragment {
        static final String ARG_STATUS = "status";

        static ErrorDialogFragment newInstance(int status) {
            Bundle args = new Bundle();

            args.putInt(ARG_STATUS, status);

            ErrorDialogFragment result = new ErrorDialogFragment();

            result.setArguments(args);

            return (result);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();

            return GooglePlayServicesUtil.getErrorDialog(args.getInt(ARG_STATUS),
                    getActivity(), 0);
        }

        @Override
        public void onDismiss(DialogInterface dlg) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }


    public class BaseModelApi {
        public String phone;
        public String imei;
    }
}
