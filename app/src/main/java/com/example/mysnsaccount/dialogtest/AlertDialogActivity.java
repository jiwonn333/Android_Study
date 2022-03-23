package com.example.mysnsaccount.dialogtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.R;

import java.util.ArrayList;

public class AlertDialogActivity extends AppCompatActivity {
    Button alertDialog, btnDialog, listDialog, checkBoxDialog, raidoDialog, customDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertdialog);

        alertDialog = findViewById(R.id.btn1);
        btnDialog = findViewById(R.id.btn2);
        listDialog = findViewById(R.id.btn3);
        checkBoxDialog = findViewById(R.id.btn4);
        raidoDialog = findViewById(R.id.btn5);
        customDialog = findViewById(R.id.btn6);


        // 첫번째 버튼 (다이얼로그 사용하여 팝업창 띄우기)
        alertDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("인사말").setMessage("안녕하세요").create().show();
        });


        // 두번째 버튼 (AlertDialog에 3개 버튼 추가)
        btnDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("종료 알림").setMessage("종료하시겠습니까?")
                    .setPositiveButton("네", (dialogInterface, i) -> {
                        Toast.makeText(AlertDialogActivity.this, "앱을 종료했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("아니요", (dialogInterface, i) -> Toast.makeText(AlertDialogActivity.this, "종료하지 않습니다.", Toast.LENGTH_SHORT).show())
                    .setNeutralButton("취소", (dialogInterface, i) -> Toast.makeText(AlertDialogActivity.this, "취소했습니다.", Toast.LENGTH_SHORT).show())
                    .create().show();
        });


        // 세번째 버튼 (list 추가)
        listDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("리스트 추가 예제")
                    .setItems(R.array.LAN, (dialogInterface, position) -> {
                        String[] items = getResources().getStringArray(R.array.LAN);
                        Toast.makeText(AlertDialogActivity.this, items[position], Toast.LENGTH_SHORT).show();
                    }).create().show();
        });


        // 네번째 버튼 (체크박스 추가 / 다중선택목록)
        checkBoxDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final ArrayList<String> selectedItems = new ArrayList<String>();
            final String[] items = getResources().getStringArray(R.array.LAN);

            builder.setTitle("체크박스 추가 예제")
                    .setMultiChoiceItems(R.array.LAN, null, (dialogInterface, position, isChecked) -> {
                        if (isChecked == true) {
                            selectedItems.add(items[position]);
                        } else {
                            selectedItems.remove(items[position]);
                        }
                    }).setPositiveButton("OK", (dialogInterface, i) -> {
                String selectedItemsString = "";

                for (i = 0; i < selectedItems.size(); i++) {
                    selectedItemsString += selectedItems.get(i) + " ";
                }
                Toast.makeText(AlertDialogActivity.this, selectedItemsString + "가 선택되었습니다.", Toast.LENGTH_SHORT).show();
            }).create().show();
        });


        // 다섯번째 버튼 (라디오버튼 추가 / 단일선택목록)
        raidoDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final ArrayList<String> selectedItems = new ArrayList<String>();
            final String[] items = getResources().getStringArray(R.array.LAN);
            selectedItems.add(items[0]);

            builder.setTitle("라디오버튼 추가 예제")
                    .setSingleChoiceItems(R.array.LAN, 0, (dialogInterface, position) -> {
                        selectedItems.clear();
                        selectedItems.add(items[position]);
                    }).setPositiveButton("OK", (dialogInterface, i) -> Toast.makeText(AlertDialogActivity.this, "선택된 항목 : " + selectedItems.get(0), Toast.LENGTH_SHORT).show())
                    .setNegativeButton("CANCLE", (dialogInterface, i) -> Toast.makeText(AlertDialogActivity.this, "선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()).create().show();
        });


        // 여섯번째 버튼 (커스텀 다이얼로그)
        customDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View customView = getLayoutInflater().inflate(R.layout.activity_customdialog, null);
            final EditText etName = (EditText) customView.findViewById(R.id.edit_view_name);
            final EditText etNickName = (EditText) customView.findViewById(R.id.edit_view_nickname);

            builder.setView(customView);

            builder.setPositiveButton("OK", (dialogInterface, position) -> {
                String name = "이름 : " + etName.getText().toString();
                String nickName = "별명 : " + etNickName.getText().toString();

                Toast.makeText(AlertDialogActivity.this, name + "\n" + nickName, Toast.LENGTH_SHORT).show();
            }).create().show();
        });


    }
}
