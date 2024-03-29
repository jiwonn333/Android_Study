package com.example.mysnsaccount.encryption;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.AESEncType;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private EditText inputEncryptText;
    private TextView encryptTextView;
    private Button btnEncode;
    private Button btnDecode;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        spinner = findViewById(R.id.encrypt_spinner);
        inputEncryptText = findViewById(R.id.encrypt_text);
        encryptTextView = findViewById(R.id.encrypt_text_view);
        btnEncode = findViewById(R.id.btnEncode);
        btnDecode = findViewById(R.id.btnDecode);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.encrypt_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        KeyPair keyPair = HashUtils.genRSAKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        btnEncode.setOnClickListener(view -> {
            String encryptText = inputEncryptText.getText().toString();
            int selectItem = spinner.getSelectedItemPosition();
            GLog.d("selectItem :" + selectItem);
            switch (selectItem) {
                case 0:
                    encryptTextView.setText(HashUtils.getBase64Encrypt(encryptText));
                    break;
                case 1:
                    encryptTextView.setText(HashUtils.getAESEncrypt(encryptText, AESEncType.AES_128));
                    break;
                case 2:
                    encryptTextView.setText(HashUtils.getAESEncrypt(encryptText, AESEncType.AES_192));
                    break;
                case 3:
                    encryptTextView.setText(HashUtils.getAESEncrypt(encryptText, AESEncType.AES_256));
                    break;
                case 4:
                    encryptTextView.setText(HashUtils.getRSAEncrypt(encryptText, publicKey));
                    break;
                case 5:
                    encryptTextView.setText(HashUtils.getSHA256Encrypt(encryptText));
                    break;
                default:
                    GLog.d("해당없음");
                    break;
            }

        });

        btnDecode.setOnClickListener(view -> {
            int selectItem = spinner.getSelectedItemPosition();
            String decryptText = encryptTextView.getText().toString();
            switch (selectItem) {
                case 0:
                    encryptTextView.setText(HashUtils.getBase64Decrypt(decryptText));
                    break;
                case 1:
                    encryptTextView.setText(HashUtils.getAESDecrypt(decryptText, AESEncType.AES_128));
                    break;
                case 2:
                    encryptTextView.setText(HashUtils.getAESDecrypt(decryptText, AESEncType.AES_192));
                    break;
                case 3:
                    encryptTextView.setText(HashUtils.getAESDecrypt(decryptText, AESEncType.AES_256));
                    break;
                case 4:
                    encryptTextView.setText(HashUtils.getRSADecrypt(decryptText, privateKey));
                    break;
                case 5:
                    Toast.makeText(this, "복호화 불가능", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    GLog.d("해당없음");
                    break;
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
