package com.studentcollab.Globals;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.studentcollab.R;

public class MessageDialog {

    private AlertDialog messageDialog = null;
    private TextView messageTextView;
    private View okButton;
    private String message;
    private View.OnClickListener customDismissAction = null;

    public MessageDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.message_dialog, null));
        builder.setCancelable(true);

        this.message = message;

        messageDialog = builder.create();
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setCancelable(false);
    }

    public void removeCustomDismissAction() {
        this.customDismissAction = null;
    }

    public void setMessage(String message) {
        this.message = message;
        //this.customDismissAction = null;
        if(messageTextView != null)
            messageTextView.setText(this.message);
    }

    public void setCustomDismissAction(View.OnClickListener listener) {
        this.customDismissAction = listener;
    }

    public void show() {
        messageDialog.show();
        messageTextView = messageDialog.findViewById(R.id.message_dialog_TextView_message);
        okButton = messageDialog.findViewById(R.id.message_dialog_Button_ok);

        messageTextView.setText(this.message);
        if (customDismissAction == null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        else {
            okButton.setOnClickListener(customDismissAction);
        }
    }

    public void dismiss() {
        if(messageDialog != null)
            messageDialog.dismiss();
    }
}
