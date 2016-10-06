package com.example.fernando.appcivico.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.example.fernando.appcivico.R;

/**
 * Created by fernando on 06/10/16.
 */
public class StaticFunctions {

    public static void exibeMensagemEFecha(String mensagem, final FragmentActivity fragmentActivity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                fragmentActivity);


        alertDialogBuilder
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton(fragmentActivity.getString(R.string.confirmar),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        fragmentActivity.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
