package cbridge.android.adouble.com.cbridge;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.auth = FirebaseAuth.getInstance();

        final EditText txtEmail = findViewById(R.id.txtEmail);
        final EditText txtPassword = findViewById(R.id.txtPassword);

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Ensira um email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Ensira uma senha!", Toast.LENGTH_SHORT).show();
                    return;
                }

                register(email, password, Register.this);
            }
        });
    }

    private void register(String email, String password, final Activity activity) {
        this.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else{
                    Log.w("ERRO_REGISTER", task.getException());
                    Toast.makeText(getApplicationContext(), "Erro desculpe!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
