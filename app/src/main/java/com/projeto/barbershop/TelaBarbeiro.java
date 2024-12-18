package com.projeto.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TelaBarbeiro extends AppCompatActivity {

    RecyclerView recycler;
    Adapter adapter;
    ArrayList<Agendamento> itens;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_barbeiro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        itens = new ArrayList<>();
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, itens);
        recycler.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        carregarDados();
    }

    public void btnAtendido(View v) {
        String documentId = (String) v.getTag();

        if (documentId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference documentReference = db.collection("Agendamentos").document(documentId);

            documentReference.delete()
                    .addOnSuccessListener(aVoid -> {
                        carregarDados();

                        Toast ts =  Toast.makeText(getApplicationContext(), "Cliente atendido!", Toast.LENGTH_SHORT);
                        ts.show();
                    });
        }
    }

    private void carregarDados() {
        db.collection("Agendamentos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                itens.clear();

                for (DocumentSnapshot document : querySnapshot) {
                    String nomeCliente = document.getString("nomeCliente");
                    String data = document.getString("data");
                    String horario = document.getString("horario");
                    String nomeBarbeiro = document.getString("barbeiro");

                    itens.add(new Agendamento(nomeCliente, horario, nomeBarbeiro, data, document.getId()));
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    public void voltar(View v){
        Intent tela = new Intent(TelaBarbeiro.this, Login.class);
        startActivity(tela);
        finish();
    }
}
