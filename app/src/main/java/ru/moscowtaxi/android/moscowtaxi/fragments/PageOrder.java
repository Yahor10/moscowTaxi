package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageOrder extends Fragment implements View.OnClickListener {


    public static Fragment newInstance() {
        PageOrder fragment = new PageOrder();
        return fragment;
    }

    public PageOrder(){

    }

    EditText edtFrom;
    EditText edtWhere;
    EditText edtKoment;
    View butDetermine;
    View butMyAdresesFrom;
    View butMyAdresesWhere;
    View butGetCarNow;
    View butClearKoment;
    Spinner spinnerHour;
    Spinner spinnerMinute;
    Spinner spinnerTariff;
    Spinner spinnerAdditionalSettings;
    Button butGetTaxi;
    Button butCallOperator;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_order, container, false);

        edtFrom = (EditText) rootView.findViewById(R.id.edt_from);
        edtWhere = (EditText) rootView.findViewById(R.id.edt_where);
        edtKoment = (EditText) rootView.findViewById(R.id.edt_koment);

        butDetermine = (View)rootView.findViewById(R.id.view_but_determine);
        butMyAdresesFrom = (View) rootView.findViewById(R.id.view_but_my_adreeses_from);
        butMyAdresesWhere = (View) rootView.findViewById(R.id.view_but_my_adreeses_where);
        butGetCarNow = (View) rootView.findViewById(R.id.view_but_get_car_now);
        butClearKoment = (View) rootView.findViewById(R.id.view_but_cleat_koment);

        spinnerHour = (Spinner) rootView.findViewById(R.id.spinner_hour);
        spinnerMinute = (Spinner) rootView.findViewById(R.id.spinner_minute);
        spinnerTariff = (Spinner) rootView.findViewById(R.id.spinner_tariff);
        spinnerAdditionalSettings = (Spinner) rootView.findViewById(R.id.spinner_additional_settings);

        Activity activity = getActivity();
        ContextThemeWrapper wrapper = new ContextThemeWrapper(activity,android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        MainActivity.CustomSpinnerAdapter tariffAdapter =
                (  MainActivity.CustomSpinnerAdapter)   MainActivity.CustomSpinnerAdapter.createFromResource(wrapper, R.array.tariffs,
                        android.R.layout.simple_spinner_dropdown_item);

        spinnerTariff.setAdapter(tariffAdapter);

        MainActivity.CustomSpinnerAdapter additionSettingsAdapter =
                (  MainActivity.CustomSpinnerAdapter)   MainActivity.CustomSpinnerAdapter.createFromResource(wrapper, R.array.additional_settings,
                        android.R.layout.simple_spinner_dropdown_item);
        spinnerAdditionalSettings.setAdapter(additionSettingsAdapter);

        butGetTaxi = (Button) rootView.findViewById(R.id.button_get_taxi);
        butCallOperator = (Button) rootView.findViewById(R.id.button_call_operator);

        butDetermine.setOnClickListener(this);
        butMyAdresesFrom.setOnClickListener(this);
        butMyAdresesWhere.setOnClickListener(this);
        butGetCarNow.setOnClickListener(this);
        butClearKoment.setOnClickListener(this);
        butGetTaxi.setOnClickListener(this);
        butCallOperator.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.view_but_determine:
                Toast.makeText(getActivity(),"DETERMINE",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.view_but_my_adreeses_from:
                Toast.makeText(getActivity(),"FROM",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.view_but_my_adreeses_where:
                Toast.makeText(getActivity(),"WHERE",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.view_but_get_car_now:
                Toast.makeText(getActivity(),"NOW",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.view_but_cleat_koment:
                edtKoment.setText("");
                break;
            case R.id.button_get_taxi:
                Toast.makeText(getActivity(),"I want Taxi",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.button_call_operator:
                Toast.makeText(getActivity(),"CALL",Toast.LENGTH_SHORT ).show();
                break;
        }
    }
}
