package ru.moscowtaxi.android.moscowtaxi.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by ychabatarou on 15.12.2014.
 */
public class OrangeEditText extends AutoCompleteTextView {

    public OrangeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        int color = context.getResources().getColor(R.color.orange_color);
        getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

}
