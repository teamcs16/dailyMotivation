package com.teamcs.mm.dailymotivation;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends AppCompatActivity {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode;

    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken onCreate",newToken);
                // we have to send to server with other credential
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "msg_subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d("sub ops ", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        Button check_btn = (Button) findViewById(R.id.btn_check);
        Button opt_btn = (Button) findViewById(R.id.btn_opt);
        final EditText opt_no = (EditText) findViewById(R.id.opt_no);
        final EditText ph_no = (EditText) findViewById(R.id.phone_no);
        StartFirebaseLogin();
        opt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = ph_no.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        MainActivity.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks
            }
        });
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opt_str=opt_no.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, opt_str);
                SigninWithPhone(credential);
            }
        });
    }
    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"OTP Success",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(MainActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this,"verification fail "+e.toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(MainActivity.this,"Code sent as "+s,Toast.LENGTH_SHORT).show();
            }
        };
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_text_swticher) {
            Intent intent = new Intent(this, TextSwticherActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_motivation_list) {
            Intent intent = new Intent(this, MotivationListActivity.class);
            //EditText editText = (EditText) findViewById(R.id.editText);
            //String message = editText.getText().toString();
            // intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
