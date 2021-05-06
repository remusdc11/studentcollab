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

    private AlertDialog messageDialog = null;
    private TextView messageTextView;
    private View confirmButton;
    private View cancelButton;
    private String message;

    public ConfirmationDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.message_dialog, null));
        builder.setCancelable(true);

        this.message = message;

        messageDialog = builder.create();
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setCancelable(false);
    }

    public void setMessage(String message) {
        this.message = message;
        if(messageTextView != null)
            messageTextView.setText(this.message);
    }

    public void show() {
        messageDialog.show();
        messageTextView = messageDialog.findViewById(R.id.message_dialog_TextView_message);
        cancelButton = messageDialog.findViewById(R.id.message_dialog_Button_ok);

        messageTextView.setText(this.message);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void dismiss() {
        if(messageDialog != null)
            messageDialog.dismiss();
    }


}
