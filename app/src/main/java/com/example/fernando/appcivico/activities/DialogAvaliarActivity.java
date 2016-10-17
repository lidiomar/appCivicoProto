package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.DialogAvaliarFragment;

/**
 * Created by fernando on 16/10/16.
 */
public class DialogAvaliarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_container);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new DialogAvaliarFragment()).commit();

    }
}