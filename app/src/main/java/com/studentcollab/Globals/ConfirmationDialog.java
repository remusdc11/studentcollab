package com.studentcollab.Globals;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.studentcollab.R;

//NOT EVEN CLOSE TO BEING DONE
public class ConfirmationDialog {

    private AlertDialog confirmationDialog = null;
    private TextView messageTextView;
    private View confirmButton;
    private View cancelButton;
    private String message;
    private View.OnClickListener onClickListener = null;

    public ConfirmationDialog(Activity activity, String message, View.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.confirmation_dialog, null));
        builder.setCancelable(true);

        this.message = message;
        this.onClickListener = onClickListener;

        confirmationDialog = builder.create();
        confirmationDialog.setCanceledOnTouchOutside(false);
        confirmationDialog.setCancelable(false);
    }

    public void setMessage(String message) {
        this.message = message;
        if(messageTextView != null)
            messageTextView.setText(this.message);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        if (cancelButton != null)
            cancelButton.setOnClickListener(onClickListener);
    }

    public void show() {
        confirmationDialog.show();
        messageTextView = confirmationDialog.findViewById(R.id.message_dialog_TextView_message);
        confirmButton = confirmationDialog.findViewById(R.id.message_dialog_Button_ok);
        cancelButton = confirmationDialog.findViewById(R.id.message_dialog_Button_cancel);

        messageTextView.setText(this.message);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (this.onClickListener != null)
            confirmButton.setOnClickListener(this.onClickListener);
    }

    public void dismiss() {
        if(confirmationDialog != null)
            confirmationDialog.dismiss();
    }
}
