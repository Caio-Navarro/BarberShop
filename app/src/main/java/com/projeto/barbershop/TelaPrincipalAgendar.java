package com.projeto.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class TelaPrincipalAgendar extends AppCompatActivity {

    CheckBox cb1, cb2, cb3, cb4;
    boolean corte, barba, limpeza, escova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_principal_agendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
    }

    public void telaPrincipalBarbearia(View v){
        Intent tela = new Intent(TelaPrincipalAgendar.this, TelaPrincipalBarbearia.class);
        startActivity(tela);
    }

    public void minhaConta(View v){
        Intent tela = new Intent(TelaPrincipalAgendar.this, MinhaConta.class);
        startActivity(tela);
    }

    public void btnAvancar(View v){
        corte = cb1.isChecked();
        barba = cb2.isChecked();
        limpeza = cb3.isChecked();
        escova = cb4.isChecked();

        if(!corte && !barba && !limpeza && !escova){
            Toast ts =  Toast.makeText(getApplicationContext(), "Marque algum serviço!", Toast.LENGTH_SHORT);
            ts.show();
        }else{
            Intent tela = new Intent(TelaPrincipalAgendar.this, TelaAgendamento2.class);
            tela.putExtra("corte", corte);
            tela.putExtra("barba", barba);
            tela.putExtra("limpeza", limpeza);
            tela.putExtra("escova", escova);
            startActivity(tela);
        }
    }
}