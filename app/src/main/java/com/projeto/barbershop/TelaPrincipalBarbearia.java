package com.projeto.barbershop;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class TelaPrincipalBarbearia extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_principal_barbearia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String nomeUsuario = documentSnapshot.getString("nome");
            }
        });
    }

    public void agendar(View v) {
        Intent tela = new Intent(TelaPrincipalBarbearia.this, TelaPrincipalAgendar.class);
        startActivity(tela);
    }

    public void minhaConta(View v) {
        Intent tela = new Intent(TelaPrincipalBarbearia.this, MinhaConta.class);
        startActivity(tela);
    }

    public void sobreNos(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaPrincipalBarbearia.this);
        builder.setTitle("Sobre nós");
        builder.setMessage("Transformamos cuidados em estilo. Aqui, você é nossa prioridade! Faça já seu atendimento. 😎");
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog popup = builder.create();
        popup.show();
    }

    public void contato(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaPrincipalBarbearia.this);
        builder.setTitle("Contato");
        builder.setMessage("WhatsApp: (75) 9999-99999 📲\nTelefone: (75) 1234-5678 📞");
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog popup = builder.create();
        popup.show();
    }

    public void valores(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaPrincipalBarbearia.this);
        builder.setTitle("Valores");
        builder.setMessage("Corte de Cabelo: R$ 40,00 ✅\nBarba: R$ 20,00 ✅\nLimpeza de Pele: R$ 15,00 ✅\nEscova: R$ 22,00 ✅");
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog popup = builder.create();
        popup.show();
    }

    public void nosApoie(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaPrincipalBarbearia.this);
        builder.setTitle("Nos apoie");
        builder.setMessage("Nosso PIX: (75) 9999-9999\nAgradecemos! 😁❤️");
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog popup = builder.create();
        popup.show();
    }

}
