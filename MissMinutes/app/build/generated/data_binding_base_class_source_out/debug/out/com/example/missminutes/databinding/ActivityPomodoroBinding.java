// Generated by view binder compiler. Do not edit!
package com.example.missminutes.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.missminutes.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPomodoroBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button BtnCancelBreak;

  @NonNull
  public final TextView cancelpomodoro;

  @NonNull
  public final ImageView imageView4;

  @NonNull
  public final TextView timeTextView;

  private ActivityPomodoroBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button BtnCancelBreak, @NonNull TextView cancelpomodoro,
      @NonNull ImageView imageView4, @NonNull TextView timeTextView) {
    this.rootView = rootView;
    this.BtnCancelBreak = BtnCancelBreak;
    this.cancelpomodoro = cancelpomodoro;
    this.imageView4 = imageView4;
    this.timeTextView = timeTextView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPomodoroBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPomodoroBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_pomodoro, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPomodoroBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.BtnCancelBreak;
      Button BtnCancelBreak = ViewBindings.findChildViewById(rootView, id);
      if (BtnCancelBreak == null) {
        break missingId;
      }

      id = R.id.cancelpomodoro;
      TextView cancelpomodoro = ViewBindings.findChildViewById(rootView, id);
      if (cancelpomodoro == null) {
        break missingId;
      }

      id = R.id.imageView4;
      ImageView imageView4 = ViewBindings.findChildViewById(rootView, id);
      if (imageView4 == null) {
        break missingId;
      }

      id = R.id.time_text_view;
      TextView timeTextView = ViewBindings.findChildViewById(rootView, id);
      if (timeTextView == null) {
        break missingId;
      }

      return new ActivityPomodoroBinding((ConstraintLayout) rootView, BtnCancelBreak,
          cancelpomodoro, imageView4, timeTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
