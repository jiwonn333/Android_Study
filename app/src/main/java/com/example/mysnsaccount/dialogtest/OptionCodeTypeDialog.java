package com.example.mysnsaccount.dialogtest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mysnsaccount.R;

public class OptionCodeTypeDialog extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    TextView tvTitle, tvNegative, tvPositive;

    public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_codetype_dialog);

        tvTitle = findViewById(R.id.dialog_title);
        tvNegative = findViewById(R.id.dialog_negativ);
        tvPositive = findViewById(R.id.dialog_positive);

        tvPositive.setOnClickListener(view -> {
            this.customDialogClickListener.onPositiveClick();
            dismiss();
        });

        tvNegative.setOnClickListener(view -> {
            this.customDialogClickListener.onNegativeClick();
            dismiss();// dialog 완전하게 종료
        });

    }


}
