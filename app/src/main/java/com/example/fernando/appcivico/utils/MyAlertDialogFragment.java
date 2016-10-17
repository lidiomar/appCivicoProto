package com.example.fernando.appcivico.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by fernando on 17/10/16.
 */
public class MyAlertDialogFragment extends DialogFragment {

    public static MyAlertDialogFragment newInstance(String title, String message) {
        if(message.isEmpty()) {
            message = "Carregando...";
        }

        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString("message", message);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(getArguments().getString("title"));
        dialog.setMessage(getArguments().getString("message"));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;

    }

}
