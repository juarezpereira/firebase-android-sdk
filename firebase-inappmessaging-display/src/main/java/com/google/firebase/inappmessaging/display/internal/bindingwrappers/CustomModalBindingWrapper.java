package com.google.firebase.inappmessaging.display.internal.bindingwrappers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.inappmessaging.display.R;
import com.google.firebase.inappmessaging.display.internal.InAppMessageLayoutConfig;
import com.google.firebase.inappmessaging.display.internal.injection.scopes.InAppMessageScope;
import com.google.firebase.inappmessaging.display.internal.layout.custom.FiamConstraintLayout;
import com.google.firebase.inappmessaging.model.InAppMessage;

import javax.inject.Inject;

/* @hide */
@InAppMessageScope
public class CustomModalBindingWrapper extends BindingWrapper {

  private FiamConstraintLayout layoutModalRoot;
  private ConstraintLayout layoutModal;

  private ScrollView modalScrollMessage;

  private TextView modalTitle;
  private TextView modalMessage;

  private Button modalAction;
  private Button modalCollapse;

  private ImageView modalImage;

  private ViewTreeObserver.OnGlobalLayoutListener layoutListener =
      new ScrollViewAdjustableListener();

  @Inject
  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public CustomModalBindingWrapper(
      InAppMessageLayoutConfig config, LayoutInflater inflater, InAppMessage message) {
    super(config, inflater, message);
  }

  @NonNull
  @Override
  public ViewTreeObserver.OnGlobalLayoutListener inflate(
      View.OnClickListener actionListener, View.OnClickListener dismissOnClickListener) {
    @SuppressLint("InflateParams")
    View root = inflater.inflate(R.layout.layout_modal, null);

    layoutModalRoot = root.findViewById(R.id.layoutModalRoot);
    layoutModal = root.findViewById(R.id.layoutModal);
    modalScrollMessage = root.findViewById(R.id.modalScrollMessage);
    modalTitle = root.findViewById(R.id.modalTitle);
    modalMessage = root.findViewById(R.id.modalMessage);
    modalAction = root.findViewById(R.id.modalAction);
    modalCollapse = root.findViewById(R.id.modalCollapse);
    modalImage = root.findViewById(R.id.modalImage);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      layoutModal.setElevation(dpToPixels(3));
      modalImage.setElevation(dpToPixels(3));
    }

    setMessage(message);
    setLayoutConfig(config);
    setDismissListener(dismissOnClickListener);
    setActionListener(actionListener);

    setModalColorOverrides();
    setButtonColorOverrides();

    return layoutListener;
  }

  @NonNull
  @Override
  public ImageView getImageView() {
    return modalImage;
  }

  @NonNull
  @Override
  public ViewGroup getRootView() {
    return layoutModalRoot;
  }

  @NonNull
  @Override
  public View getDialogView() {
    return layoutModal;
  }

  @NonNull
  @Override
  public InAppMessageLayoutConfig getConfig() {
    return config;
  }

  @NonNull
  public Button getActionButton() {
    return modalAction;
  }

  @NonNull
  public View getCollapseButton() {
    return modalCollapse;
  }

  private void setMessage(InAppMessage message) {
    if (TextUtils.isEmpty(message.getImageUrl())) {
      modalImage.setVisibility(View.GONE);
    } else {
      modalImage.setVisibility(View.VISIBLE);
    }

    if (message.getTitle() != null) {
      if (!TextUtils.isEmpty(message.getTitle().getText())) {
        modalTitle.setVisibility(View.VISIBLE);
        modalTitle.setText(message.getTitle().getText());
      } else {
        modalTitle.setVisibility(View.GONE);
      }

      if (!TextUtils.isEmpty(message.getTitle().getHexColor())) {
        modalTitle.setTextColor(Color.parseColor(message.getTitle().getHexColor()));
      }
    }

    if (message.getBody() != null && !TextUtils.isEmpty(message.getBody().getText())) {
      modalScrollMessage.setVisibility(View.VISIBLE);
    } else {
      modalScrollMessage.setVisibility(View.GONE);
    }

    if (message.getBody() != null) {
      if (!TextUtils.isEmpty(message.getBody().getText())) {
        modalMessage.setVisibility(View.VISIBLE);
        modalMessage.setText(message.getBody().getText());
      } else {
        modalMessage.setVisibility(View.GONE);
      }

      if (!TextUtils.isEmpty(message.getBody().getHexColor())) {
        modalMessage.setTextColor(Color.parseColor(message.getBody().getHexColor()));
      }
    }
  }

  private void setLayoutConfig(InAppMessageLayoutConfig config) {
    modalImage.setMaxHeight(config.getMaxImageHeight());
    modalImage.setMaxWidth(config.getMaxImageWidth());
  }

  private void setDismissListener(View.OnClickListener dismissListener) {
    modalCollapse.setOnClickListener(dismissListener);
    layoutModalRoot.setDismissListener(dismissListener);
  }

  private void setActionListener(View.OnClickListener actionListener) {
    modalAction.setOnClickListener(actionListener);
  }

  private void setButtonColorOverrides() {
    // Set the background color of the getAction button to be the FIAM color. We do this explicitly
    // to
    // allow for a rounded modal (b/c overloaded background for shape and color)

    if (modalAction != null
        && message.getActionButton() != null
        && message.getActionButton().getButtonHexColor() != null) {
      int buttonColor = Color.parseColor(message.getActionButton().getButtonHexColor());

      // Tint the button based on the background color
      Drawable buttonDrawable = generateDrawableButton(buttonColor);
      modalAction.setBackground(buttonDrawable);

      if (modalCollapse != null) {
        modalCollapse.setTextColor(buttonColor);
      }

      if (message.getActionButton() != null && message.getActionButton().getText() != null) {
        if (!TextUtils.isEmpty(message.getActionButton().getText().getText())) {
          modalAction.setVisibility(View.VISIBLE);
          modalAction.setText(message.getActionButton().getText().getText());
        } else {
          modalAction.setVisibility(View.GONE);
        }
        String buttonTextColorStr = message.getActionButton().getText().getHexColor();

        if (!TextUtils.isEmpty(buttonTextColorStr)) {
          modalAction.setTextColor(Color.parseColor(buttonTextColorStr));
        }
      }
    } else {
      if (modalAction != null) {
        modalAction.setVisibility(View.GONE);
      }
    }
  }

  private GradientDrawable generateDrawableButton(int color) {
    GradientDrawable shape = new GradientDrawable();
    shape.setShape(GradientDrawable.RECTANGLE);
    shape.setCornerRadius(dpToPixels(50));
    shape.setColor(color);
    return shape;
  }

  private void setModalColorOverrides() {
    // Set the background color of the Modal to be the FIAM color. We do this explicitly to
    // allow for a rounded modal (b/c overloaded background for shape and color)
    if (layoutModal != null) {
      setGradientDrawableBgColor(layoutModal, message.getBackgroundHexColor());
    }
  }

  @VisibleForTesting
  public void setLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
    layoutListener = listener;
  }

  /** Convert a value in "dp" to a "px" value for the current display. */
  private int dpToPixels(int dp) {
    if (getDialogView().getContext() == null) {
      return 0;
    }

    final DisplayMetrics displayMetrics =
        getDialogView().getContext().getResources().getDisplayMetrics();

    return (int)
        Math.floor(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics));
  }

  public class ScrollViewAdjustableListener implements ViewTreeObserver.OnGlobalLayoutListener {
    @Override
    public void onGlobalLayout() {
      modalImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
  }
}
