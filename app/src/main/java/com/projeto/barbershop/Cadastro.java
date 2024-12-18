package com.projeto.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    private EditText nome, telefone, email, senha, confirmarSenha;
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nome = findViewById(R.id.nome);
        telefone = findViewById(R.id.telefone);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        confirmarSenha = findViewById(R.id.confirmarSenha);
    }

    public void cadastrar(View v) {
        String nomeString = nome.getText().toString().trim();
        String telefoneString = telefone.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String senhaString = senha.getText().toString().trim();
        String confirmarString = confirmarSenha.getText().toString().trim();

        if (!validarCampos(nomeString, telefoneString, emailString, senhaString, confirmarString)) {
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString, senhaString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            salvarDadosUsuario(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao criar usuário: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean validarCampos(String nome, String telefone, String email, String senha, String confirmarSenha) {
        if (nome.isEmpty() || nome.length() < 3) {
            mostrarErro("O nome deve ter pelo menos 3 caracteres!");
            return false;
        }

        if (telefone.isEmpty() || !telefone.matches("\\d{8,11}")) {
            mostrarErro("O telefone deve conter apenas números e ter entre 8 e 11 dígitos!");
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarErro("Insira um e-mail válido!");
            return false;
        }

        if (senha.isEmpty() || senha.length() < 6) {
            mostrarErro("A senha deve ter pelo menos 6 caracteres!");
            return false;
        }

        if (!senha.equals(confirmarSenha)) {
            mostrarErro("As senhas não coincidem!");
            return false;
        }

        return true;
    }

    private void mostrarErro(String mensagem) {
        Toast toast = Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT);
        toast.show();
    }


    private void salvarDadosUsuario(View v) {
        String nomeString = nome.getText().toString().trim();
        String telefoneString = telefone.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nomeString);
        usuarios.put("telefone", telefoneString);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(), "Erro ao obter ID do usuário!", Toast.LENGTH_SHORT).show();
            return;
        }

        usuarioId = auth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
        documentReference.set(usuarios)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    telaLogin(v);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Erro ao salvar dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


    public void telaLogin(View v){
        Intent tela = new Intent(Cadastro.this, Login.class);
        startActivity(tela);
        finish();
    }
}