package com.studentcollab.Globals;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.studentcollab.R;

public class LoadingDialog {

    private AlertDialog loadingDialog = null;

    public LoadingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        loadingDialog = builder.create();
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
    }

    public void startLoadingDialog() {
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if(loadingDialog != null)
            loadingDialog.dismiss();
    }
}
