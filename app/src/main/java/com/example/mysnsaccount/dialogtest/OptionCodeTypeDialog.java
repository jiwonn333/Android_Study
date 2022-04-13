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
    TextView dialogTextView;
    String dialogText;


    public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }

    public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener, String dialogTextView) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
        dialogText = dialogTextView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_codetype_dialog);

        tvTitle = findViewById(R.id.dialog_title);
        tvNegative = findViewById(R.id.dialog_negative);
        tvPositive = findViewById(R.id.dialog_positive);

        dialogTextView = findViewById(R.id.dialog_text);
        dialogTextView.setText(dialogText);


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
