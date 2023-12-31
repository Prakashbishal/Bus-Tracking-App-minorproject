// Generated by view binder compiler. Do not edit!
package com.example.minorproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.minorproject.R;
import com.google.android.material.button.MaterialButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityWelcomeBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final EditText Id;

  @NonNull
  public final TextView forgotPass;

  @NonNull
  public final EditText pass;

  @NonNull
  public final LinearLayout passwordLayout;

  @NonNull
  public final ProgressBar progressbar;

  @NonNull
  public final ImageButton showHideButton;

  @NonNull
  public final MaterialButton signInBtn;

  @NonNull
  public final TextView signUpnow;

  private ActivityWelcomeBinding(@NonNull RelativeLayout rootView, @NonNull EditText Id,
      @NonNull TextView forgotPass, @NonNull EditText pass, @NonNull LinearLayout passwordLayout,
      @NonNull ProgressBar progressbar, @NonNull ImageButton showHideButton,
      @NonNull MaterialButton signInBtn, @NonNull TextView signUpnow) {
    this.rootView = rootView;
    this.Id = Id;
    this.forgotPass = forgotPass;
    this.pass = pass;
    this.passwordLayout = passwordLayout;
    this.progressbar = progressbar;
    this.showHideButton = showHideButton;
    this.signInBtn = signInBtn;
    this.signUpnow = signUpnow;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityWelcomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityWelcomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_welcome, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityWelcomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Id;
      EditText Id = ViewBindings.findChildViewById(rootView, id);
      if (Id == null) {
        break missingId;
      }

      id = R.id.forgotPass;
      TextView forgotPass = ViewBindings.findChildViewById(rootView, id);
      if (forgotPass == null) {
        break missingId;
      }

      id = R.id.pass;
      EditText pass = ViewBindings.findChildViewById(rootView, id);
      if (pass == null) {
        break missingId;
      }

      id = R.id.passwordLayout;
      LinearLayout passwordLayout = ViewBindings.findChildViewById(rootView, id);
      if (passwordLayout == null) {
        break missingId;
      }

      id = R.id.progressbar;
      ProgressBar progressbar = ViewBindings.findChildViewById(rootView, id);
      if (progressbar == null) {
        break missingId;
      }

      id = R.id.showHideButton;
      ImageButton showHideButton = ViewBindings.findChildViewById(rootView, id);
      if (showHideButton == null) {
        break missingId;
      }

      id = R.id.sign_inBtn;
      MaterialButton signInBtn = ViewBindings.findChildViewById(rootView, id);
      if (signInBtn == null) {
        break missingId;
      }

      id = R.id.sign_upnow;
      TextView signUpnow = ViewBindings.findChildViewById(rootView, id);
      if (signUpnow == null) {
        break missingId;
      }

      return new ActivityWelcomeBinding((RelativeLayout) rootView, Id, forgotPass, pass,
          passwordLayout, progressbar, showHideButton, signInBtn, signUpnow);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
