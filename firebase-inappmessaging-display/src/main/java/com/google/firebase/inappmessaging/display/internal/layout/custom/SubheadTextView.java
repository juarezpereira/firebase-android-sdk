package com.google.firebase.inappmessaging.display.internal.layout.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.google.firebase.inappmessaging.display.R;

public class SubheadTextView extends AppCompatTextView {

    public SubheadTextView(Context context) {
        super(context);
        setupFont(context);
    }

    public SubheadTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupFont(context);
    }

    public SubheadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupFont(context);
    }

    private void setupFont(Context context) {
        setTextColor(getResources().getColor(R.color.black_80));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.subhead));

        try {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Regular.ttf");
            setTypeface(face);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}