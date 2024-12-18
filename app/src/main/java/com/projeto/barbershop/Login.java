package com.projeto.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private EditText email, senha;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        pb = findViewById(R.id.pb);
    }

    public void telaCadastro(View v){
        Intent tela = new Intent(Login.this, Cadastro.class);
        startActivity(tela);
    }

    public void telaAdmin(View v){
        Intent tela = new Intent(Login.this, TelaBarbeiro.class);
        startActivity(tela);
    }

    public void login(View v){
        String emailString = email.getText().toString();
        String senhaString = senha.getText().toString();

        if(emailString.isEmpty() || senhaString.isEmpty()){
            Toast ts =  Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT);
            ts.show();
            return;
        }

        if(emailString.equals("admin") && senhaString.equals("123")){
            pb.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    telaAdmin(v);
                }
            },2000);

            Toast ts =  Toast.makeText(getApplicationContext(), "Usuário logado como admin!", Toast.LENGTH_SHORT);
            ts.show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailString, senhaString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pb.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaPrincipal();
                        }
                    },2000);

                    Toast ts =  Toast.makeText(getApplicationContext(), "Usuário logado!", Toast.LENGTH_SHORT);
                    ts.show();

                }else{
                    Toast ts =  Toast.makeText(getApplicationContext(), "Usuário ou senha inválidos!", Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            telaPrincipal();
        }

    }

    private void telaPrincipal(){
        Intent tela = new Intent(Login.this, TelaPrincipalBarbearia.class);
        startActivity(tela);
    }
}