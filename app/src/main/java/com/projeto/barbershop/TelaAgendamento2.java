package com.projeto.barbershop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TelaAgendamento2 extends AppCompatActivity {

    private String usuarioId;
    private String nomeCliente = "";
    String servicos;
    Spinner spSelecionarBarbeiro;
    Spinner spSelecionarHorario;
    String barbeiroEscolhido = "Joao";
    String horarioEscolhido = "9:00";
    String dataSelecionadaString;
    DatePicker dataAgendamento;
    Date dataAgendamentoDate;
    boolean corte;
    boolean barba;
    boolean limpeza;
    boolean escova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_agendamento2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spSelecionarBarbeiro = findViewById(R.id.spSelecionarBarbeiro);
        dataAgendamento = findViewById(R.id.dataAgendamento);
        spSelecionarHorario = findViewById(R.id.spSelecionarHorario);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(locale);
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }

    @Override
    protected void onStart() {
        super.onStart();

        corte = getIntent().getBooleanExtra("corte", false);
        barba = getIntent().getBooleanExtra("barba", false);
        limpeza = getIntent().getBooleanExtra("limpeza", false);
        escova = getIntent().getBooleanExtra("escova", false);

        StringBuilder servicosBuilder = new StringBuilder();

        if (corte) {
            servicosBuilder.append("Corte");
        }
        if (barba) {
            if (servicosBuilder.length() > 0) servicosBuilder.append(", ");
            servicosBuilder.append("Barba");
        }
        if (limpeza) {
            if (servicosBuilder.length() > 0) servicosBuilder.append(", ");
            servicosBuilder.append("Limpeza de Pele");
        }
        if (escova) {
            if (servicosBuilder.length() > 0) servicosBuilder.append(", ");
            servicosBuilder.append("Escova");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nomeCliente = documentSnapshot.getString("nome");
            }
        });

        servicos = servicosBuilder.toString();

        spSelecionarBarbeiro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                barbeiroEscolhido = parent.getItemAtPosition(position).toString();
                Log.d("Spinner", "Barbeiro selecionado: " + barbeiroEscolhido);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                barbeiroEscolhido = "João";
            }
        });

        spSelecionarHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horarioEscolhido = parent.getItemAtPosition(position).toString();
                Log.d("Spinner", "Horário selecionado: " + horarioEscolhido);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                horarioEscolhido = "9:00";
            }
        });
    }

    public void telaPrincipalBarbearia(View v){
        Intent tela = new Intent(TelaAgendamento2.this, TelaPrincipalBarbearia.class);
        startActivity(tela);
    }

    public void minhaConta(View v){
        Intent tela = new Intent(TelaAgendamento2.this, MinhaConta.class);
        startActivity(tela);
    }

    public void agendar(View v){
        Intent tela = new Intent(TelaAgendamento2.this, TelaPrincipalAgendar.class);
        startActivity(tela);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btnAgendar(View v){
        int day = dataAgendamento.getDayOfMonth();
        int month = dataAgendamento.getMonth();
        int year = dataAgendamento.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0,0);

        dataAgendamentoDate = calendar.getTime();

        LocalDate dataSelecionada = LocalDate.of(year, month + 1, day);
        LocalDate dataAtual = LocalDate.now();

        if (dataSelecionada.isBefore(dataAtual)) {
            Toast ts =  Toast.makeText(getApplicationContext(), "A data não é válida!", Toast.LENGTH_SHORT);
            ts.show();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataSelecionadaString = dataSelecionada.format(formatter);

        salvarDadosAgendamento(v);
    }

    private void salvarDadosAgendamento(View v) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Agendamentos")
                .whereEqualTo("barbeiro", barbeiroEscolhido)
                .whereEqualTo("data", dataSelecionadaString)
                .whereEqualTo("horario", horarioEscolhido)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Toast ts =  Toast.makeText(getApplicationContext(), "Esse horário está ocuapado com o barbeiro: " + barbeiroEscolhido, Toast.LENGTH_SHORT);
                        ts.show();
                    } else {
                        Map<String, Object> agendamentos = new HashMap<>();
                        agendamentos.put("nomeCliente", nomeCliente);
                        agendamentos.put("barbeiro", barbeiroEscolhido);
                        agendamentos.put("data", dataSelecionadaString);
                        agendamentos.put("horario", horarioEscolhido);
                        agendamentos.put("servicos", servicos);

                        String agendamentoId = db.collection("Agendamentos").document().getId();

                        db.collection("Agendamentos")
                                .document(agendamentoId)
                                .set(agendamentos)
                                .addOnSuccessListener(unused -> {
                                    Toast ts =  Toast.makeText(getApplicationContext(), "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT);
                                    ts.show();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            telaPrincipalBarbearia(v);
                                        }
                                    }, 1900);

                                })
                                .addOnFailureListener(e -> {
                                    Toast ts =  Toast.makeText(getApplicationContext(), "Erro ao salvar agendamento. Tente novamente", Toast.LENGTH_SHORT);
                                    ts.show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast ts =  Toast.makeText(getApplicationContext(), "Erro ao verificar disponibilidade. Tente novamente", Toast.LENGTH_SHORT);
                    ts.show();
                });
    }

}
