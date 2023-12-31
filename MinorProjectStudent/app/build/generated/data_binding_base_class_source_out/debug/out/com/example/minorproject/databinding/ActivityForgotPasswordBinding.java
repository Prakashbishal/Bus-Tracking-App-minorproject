// Generated by view binder compiler. Do not edit!
package com.example.minorproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.minorproject.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityForgotPasswordBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView forgotPass;

  @NonNull
  public final EditText idNumber;

  @NonNull
  public final EditText rollNo;

  @NonNull
  public final Button searchButton;

  private ActivityForgotPasswordBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView forgotPass, @NonNull EditText idNumber, @NonNull EditText rollNo,
      @NonNull Button searchButton) {
    this.rootView = rootView;
    this.forgotPass = forgotPass;
    this.idNumber = idNumber;
    this.rollNo = rollNo;
    this.searchButton = searchButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityForgotPasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityForgotPasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_forgot_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityForgotPasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.forgotPass;
      TextView forgotPass = ViewBindings.findChildViewById(rootView, id);
      if (forgotPass == null) {
        break missingId;
      }

      id = R.id.idNumber;
      EditText idNumber = ViewBindings.findChildViewById(rootView, id);
      if (idNumber == null) {
        break missingId;
      }

      id = R.id.rollNo;
      EditText rollNo = ViewBindings.findChildViewById(rootView, id);
      if (rollNo == null) {
        break missingId;
      }

      id = R.id.searchButton;
      Button searchButton = ViewBindings.findChildViewById(rootView, id);
      if (searchButton == null) {
        break missingId;
      }

      return new ActivityForgotPasswordBinding((ConstraintLayout) rootView, forgotPass, idNumber,
          rollNo, searchButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
