package com.projeto.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MinhaConta extends AppCompatActivity {

    private TextView nome, email, telefone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_principal_minha_conta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        telefone = findViewById(R.id.telefone);
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String nomeString = documentSnapshot.getString("nome");
                String telefoneString = documentSnapshot.getString("telefone");
                String emailString = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                nome.setText("Nome: " + nomeString);
                telefone.setText("Telefone: " + telefoneString);
                email.setText("Email: " + emailString);
            }
        });
    }

    public void telaBarbearia(View v){
        Intent tela = new Intent(MinhaConta.this, TelaPrincipalBarbearia.class);
        startActivity(tela);
    }

    public void telaAgendar(View v){
        Intent tela = new Intent(MinhaConta.this, TelaPrincipalAgendar.class);
        startActivity(tela);
    }

    public void deslogar(View v){
        FirebaseAuth.getInstance().signOut();

        Intent tela = new Intent(MinhaConta.this, Login.class);
        startActivity(tela);
        finish();
    }

}