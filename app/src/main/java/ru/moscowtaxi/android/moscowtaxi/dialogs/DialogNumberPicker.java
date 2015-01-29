package ru.moscowtaxi.android.moscowtaxi.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.callbacks.NumberPickerCallBack;

/**
 * Created by alex-pers on 1/29/15.
 */
public class DialogNumberPicker extends DialogFragment implements View.OnClickListener {
    String title = "Taxi";
    NumberPicker picker ;
    int min;
    int max;
    NumberPickerCallBack callBack;

    public DialogNumberPicker( String title, int min, int max, NumberPickerCallBack callBack) {
        this.title = title;
        this.min = min;
        this.max = max;
        this. callBack = callBack;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View v = inflater.inflate(R.layout.dialog_number_picker, null);
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        ((TextView) v.findViewById(R.id.txt_title)).setText(title);
        picker = (NumberPicker) v.findViewById(R.id.number_picker);
        picker.setMinValue(min);
        picker.setMaxValue(max);


        return v;
    }

    public void onClick(View v) {
        callBack.setNumber(picker.getValue());
        dismiss();

    }

}
