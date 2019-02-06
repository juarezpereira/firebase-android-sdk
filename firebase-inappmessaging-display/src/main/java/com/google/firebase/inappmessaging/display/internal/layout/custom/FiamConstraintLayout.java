package com.google.firebase.inappmessaging.display.internal.layout.custom;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.google.firebase.inappmessaging.display.internal.layout.BackButtonLayout;
import com.google.firebase.inappmessaging.display.internal.layout.util.BackButtonHandler;

public class FiamConstraintLayout extends ConstraintLayout implements BackButtonLayout {

    private BackButtonHandler mBackHandler;

    public FiamConstraintLayout(Context context) {
        super(context);
    }

    public FiamConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FiamConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDismissListener(OnClickListener listener) {
        mBackHandler = new BackButtonHandler(this, listener);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Boolean handled = mBackHandler.dispatchKeyEvent(event);
        if (handled != null) {
            return handled;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

}