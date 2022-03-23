package com.example.mysnsaccount.dialogtest.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.mysnsaccount.R;

public class OptionCodeTypeDialog extends Dialog {

    private Context context;

    public OptionCodeTypeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }


}
