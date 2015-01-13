package ru.moscowtaxi.android.moscowtaxi.dialogs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by alex-pers on 1/12/15.
 */
@SuppressLint("ValidFragment")
public class DialogMessageAndTitle extends DialogFragment implements View.OnClickListener {
    String message = "Taxi";
    String title = "Taxi";

    public DialogMessageAndTitle(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.dialog_title_message, null);
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        ((TextView) v.findViewById(R.id.txt_message)).setText(message);
        ((TextView) v.findViewById(R.id.txt_title)).setText(title);

        return v;
    }

    public void onClick(View v) {
        dismiss();
    }

}
