package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.HashUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_authorize:
                if (edtPhoneNumber.getText().length() < 7) {
                    edtPhoneNumber.setError("Слишком короткий номер");
                    return;
                } else {
                    PreferenceUtils.setCurrentPhone(this, edtPhoneNumber.getText().toString());
                }
                if (edtSmsCode.getText().length() < 3) {
                    edtSmsCode.setError("Слишком короткий код");
                    return;
                } else {
                    PreferenceUtils.setCurrentHash(this, HashUtils.md5(edtSmsCode.getText().toString()));
                }
                if (WebUtils.isOnline(this)) {

                    String phone = edtPhoneNumber.getText().toString();
//                    String phone = PreferenceUtils.getCurrentUserPhone(this);
                    String id = PreferenceUtils.getDeviceId(this);
                    String hash = HashUtils.md5(edtSmsCode.getText().toString());
                    loginUser(phone, hash, id);

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                }
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

        // FOR DEBUG
//        if ("".equals(PreferenceUtils.getCurrentUserPhone(this))) {
//            PreferenceUtils.setCurrentPhone(this, "79510677310");
//        }

//        if ("".equals(PreferenceUtils.getCurrentUserHash(this))) {
//            PreferenceUtils.setCurrentHash(this, "eaf441351bf076375ab3a90f8b89b696");
//        }

        if ("".equals(PreferenceUtils.getDeviceId(this))) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceid = manager.getDeviceId();
//            String deviceid="128128";
            PreferenceUtils.setDeviceId(this, deviceid);
        }
        String phone = PreferenceUtils.getCurrentUserPhone(getApplicationContext());
//                    String phone = PreferenceUtils.getCurrentUserPhone(this);
        String id = PreferenceUtils.getDeviceId(this);
        String hash = PreferenceUtils.getCurrentUserHash(getApplicationContext());

        if (WebUtils.isOnline(this)) {
            if (!"".equals(phone) && !"".equals(hash)) {
                loginUser(phone, hash, id);
            }
        }

//        startActivity(new Intent(this,MainActivity.class));
//        finish();
    }

    public void loginUser(String phone, String hash, String id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TaxiApi.MAIN_URL)
                .build();
        TaxiApi service = restAdapter.create(TaxiApi.class);

//

        service.login(phone, id, hash, new Callback<Response>() {
            @Override
            public void success(Response s, Response response) {
                try {

                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    String str = WebUtils.getResponseString(s);
                    LoginRequest loginRequest = gson.fromJson(str, LoginRequest.class);
                    if (loginRequest.c >= 1) {
                        Toast.makeText(getApplicationContext(), "Авторизация прошла успешно!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    }

                    Log.d("LOGIN", WebUtils.getResponseString(s));

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
            }
        });
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

    public class LoginRequest {
        public int c;
        public ArrayTypes d;


        public class ArrayTypes {
            ArrayList<e> classes;

            public class e {
                int id;
                String name;
            }
        }

    }


}
